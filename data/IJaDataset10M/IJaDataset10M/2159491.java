package test.latency;

import java.io.IOException;
import javax.realtime.ImmortalMemory;
import javax.realtime.MemoryArea;
import javax.realtime.PriorityParameters;
import jtools.jargo.ArgParser;
import jtools.jargo.ArgSpec;
import jtools.jargo.ArgValue;
import jtools.jargo.CommandLineSpec;
import jtools.time.HighResTime;
import jtools.time.HighResTimer;
import jtools.time.PerformanceReport;
import rtjdds.rtps.messages.elements.EntityId;
import rtjdds.rtps.messages.elements.EntityKindEnum;
import rtjdds.rtps.messages.elements.SerializedData;
import rtjdds.rtps.publication.Writer;
import rtjdds.rtps.structure.local.DomainParticipantFactory;
import rtjdds.rtps.structure.local.ParticipantImpl;
import rtjdds.rtps.types.EntityId_t;
import DDS.DataWriterQos;
import DDS.HistoryQosPolicy;
import DDS.ReliabilityQosPolicy;
import DDS.ReliabilityQosPolicyKind;
import DDS.TopicQos;

public class SendLatency extends javax.realtime.RealtimeThread {

    public static int NUM_ITERATIONS = 0;

    public static int SAMPLE_LENGTH = 0;

    public static byte[] copy = new byte[524288 * 2];

    public static void main(String[] args) throws Exception {
        ArgSpec iterArg = new ArgSpec("iteration", new String[] { "jtools.jargo.NaturalValidator" });
        ArgSpec sizeArg = new ArgSpec("size", new String[] { "jtools.jargo.NaturalValidator" });
        CommandLineSpec cmd_line = new CommandLineSpec();
        cmd_line.addRequiredArg(iterArg);
        cmd_line.addRequiredArg(sizeArg);
        ArgParser arg_parser = new ArgParser(cmd_line);
        arg_parser.parse(args);
        ArgValue iter_value = arg_parser.getArg(iterArg);
        ArgValue size_value = arg_parser.getArg(sizeArg);
        NUM_ITERATIONS = ((Integer) iter_value.getValue()).intValue();
        SAMPLE_LENGTH = ((Integer) size_value.getValue()).intValue();
        System.out.println("***************************************************");
        System.out.println("*                     SEND TEST                   *");
        System.out.println("***************************************************");
        SendLatency pubThread = new SendLatency(1, ImmortalMemory.instance());
        pubThread.start();
        pubThread.join();
    }

    HighResTimer timer = new HighResTimer();

    long _start_time = 0;

    long _stop_time = 0;

    public SendLatency(int priority, MemoryArea mem) {
        super(new PriorityParameters(priority + 10), null, null, mem, null, null);
    }

    public void run() {
        DDS.DomainParticipant participant = DomainParticipantFactory.create_participant(0, null, null);
        participant.enable();
        DDS.TopicQos topic_qos = new TopicQos();
        topic_qos.history = new HistoryQosPolicy();
        topic_qos.history.depth = 10;
        topic_qos.reliability = new ReliabilityQosPolicy();
        topic_qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
        EntityId pingTopicId = new EntityId(new EntityId_t(new byte[] { 1, 2, 3 }, EntityKindEnum.USER_DEF_TOPIC));
        int data_length = SAMPLE_LENGTH;
        DDS.Topic pingTopic = ((ParticipantImpl) participant).create_topic("ping_topic", pingTopicId, "no_type", data_length, null, null);
        DDS.Publisher publisher = participant.create_publisher(null, null);
        DDS.DataWriterQos writer_qos = new DataWriterQos();
        writer_qos.history = new HistoryQosPolicy();
        writer_qos.history.depth = 10;
        writer_qos.reliability = new ReliabilityQosPolicy();
        writer_qos.reliability.kind = ReliabilityQosPolicyKind.BEST_EFFORT_RELIABILITY_QOS;
        DDS.DataWriter writer = publisher.create_datawriter(pingTopic, writer_qos, null);
        PerformanceReport report = new PerformanceReport("SendLatencyReport");
        for (int dataLength = SAMPLE_LENGTH * 4; dataLength >= 32; dataLength /= 2) {
            System.out.println("TEST " + dataLength + " BYTES PACKET");
            SerializedData data = new SerializedData(new byte[dataLength]);
            for (int i = 0; i < NUM_ITERATIONS; i++) {
                timer.start();
                ((Writer) writer).writeUntyped(data, null);
                timer.stop();
                if (dataLength <= SAMPLE_LENGTH) report.addMeasuredVariable(dataLength + "", timer.getElapsedTime());
            }
        }
        System.out.println("DONE.");
        try {
            report.generateDataFile("./", "\t", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Don't forget to kill the subscriber, i'm too lazy to do it.");
    }
}
