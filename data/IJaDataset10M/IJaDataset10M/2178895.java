package test.cases.layer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import junit.framework.TestCase;
import layer.disseminator.applicationInterfaceAdapter.AIALayer;
import org.apache.log4j.PropertyConfigurator;
import apps.disseminator.DDU;
import combasics.event.LayerEvent;
import combasics.listener.LayerListener;

/**
 * Test des <code>SRLayer</code>s. Diese Klasse ist ein
 * <code>LayerListener</code>, der �berhalb der Schicht und unterhalb der
 * Schicht die von der Schicht erzeugten Ereignisse abgreift. Wenn der Inhalt
 * der Nutzdaten vor dem Senden und nach dem Empfangen durch die Schchicht
 * derselbe ist, so arbeitet die Schicht korrekt.
 * 
 * @author Yark Schroeder, Manuel Scholz
 * @version $Id: AILayerTest.java,v 1.7 2006/04/06 15:00:13 yark Exp $
 * @since 1.3
 */
public class AILayerTest extends TestCase implements LayerListener {

    /**
	 * Die zu testende Schicht.
	 */
    AIALayer mAILayer;

    private String mReceivedStrPayload;

    private Thread mReceiver;

    private Thread mWaiter;

    private Object mMutex;

    /**
	 * Alle relevanten Objekte werden erzeugt.
	 * 
	 * @since 1.3
	 */
    protected void setUp() throws Exception {
        PropertyConfigurator.configure(getLoggerProperties());
        mMutex = new Object();
        mAILayer = new AIALayer();
        mAILayer.start();
        mAILayer.addLayerListener(this);
        mReceiver = new Thread() {

            public void run() {
                try {
                    DDU kDataUnit = mAILayer.receive();
                    ByteArrayInputStream kByteArrayInputStream = new ByteArrayInputStream(kDataUnit.getData());
                    ObjectInputStream kObjectInputStream = new ObjectInputStream(kByteArrayInputStream);
                    try {
                        mReceivedStrPayload = (String) kObjectInputStream.readObject();
                        kObjectInputStream.close();
                        synchronized (mMutex) {
                            mMutex.notify();
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mReceiver.start();
    }

    /**
	 * Zun�chst werden Nutzdaten erzeugt, die der Schicht serialisiert �bergeben
	 * werden. Die Daten werden behandelt, als ob sie versendet werden m�ssten.
	 * Das beinhaltet die Weitergabe der Daten an die darunterliegende Schicht,
	 * verpackt in einem schichtspezifischen Rahmen. Dadurch, dass eine Schleife
	 * erzeugt wurde, werden die Daten der Schicht anschlie�end als empfangene
	 * Daten zur�ckgegeben und durchlaufen die Schicht abermals. Schlie�lich
	 * reicht die Schicht die empfangenen Daten nach Bearbeitung nach oben
	 * weiter. Nun l�sst sich der Inhalt der Daten vom Beginn und nach
	 * zweimaligem Durchlaufen der Schicht vergleichen. Liegt derselbe Inhalt
	 * vor, so arbeitet die Schicht korrekt.
	 * 
	 * @since 1.3
	 */
    public void testLayer() {
        final String strPayload = "Zun�chst werden Nutzdaten erzeugt, die der Schicht serialisiert �bergeben werden. Die Daten werden behandelt, als ob sie versendet werden m�ssten. Das beinhaltet die Weitergabe der Daten an die darunterliegende Schicht, verpackt in einem schichtspezifischen Rahmen. Dadurch, dass eine Schleife erzeugt wurde, werden die Daten der Schicht anschlie�end als empfangene Daten zur�ckgegeben und durchlaufen die Schicht abermals. Schlie�lich reicht die Schicht die empfangenen Daten nach Bearbeitung nach oben weiter. Nun l�sst sich der Inhalt der Daten vom Beginn und nach zweimaligem Durchlaufen der Schicht vergleichen. Liegt derselbe Inhalt vor, so arbeitet die Schicht korrekt.";
        mWaiter = new Thread() {

            public void run() {
                synchronized (mMutex) {
                    while (mReceivedStrPayload == null) {
                        try {
                            mMutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                assertEquals(strPayload, mReceivedStrPayload);
            }
        };
        disseminate(strPayload);
        mWaiter.start();
        try {
            mWaiter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mAILayer.stop();
    }

    private void disseminate(Object kObject) {
        try {
            ByteArrayOutputStream kByteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream kObjectOutputStream = new ObjectOutputStream(kByteArrayOutputStream);
            kObjectOutputStream.writeObject(kObject);
            kObjectOutputStream.close();
            mAILayer.disseminate(kByteArrayOutputStream.toByteArray());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void processLayerEventForDownwardTransport(LayerEvent kEvent) {
        mAILayer.processLayerEventForUpwardTransport(kEvent);
    }

    public void processLayerEventForUpwardTransport(LayerEvent kEvent) {
    }

    private Properties getLoggerProperties() {
        Properties kLoggerProperties = new Properties();
        kLoggerProperties.setProperty("log4j.rootLogger", "DEBUG, stdout");
        kLoggerProperties.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        kLoggerProperties.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        kLoggerProperties.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%-4r [%t] %-5p %c - %m%n");
        kLoggerProperties.setProperty("log4j.logger.layer.disseminator.transceiver.impl.AProcessor", "OFF");
        return kLoggerProperties;
    }

    public static void main(String[] args) {
        junit.swingui.TestRunner.run(AILayerTest.class);
    }
}
