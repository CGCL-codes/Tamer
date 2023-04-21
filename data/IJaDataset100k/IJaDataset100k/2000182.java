package dash.performance.sequential;

import java.util.ArrayList;
import java.util.List;
import dash.examples.provider.TestProvider;
import dash.performance.sequential.consumers.AtomicConsumer;
import dash.performance.sequential.consumers.Consumer;
import dash.performance.sequential.consumers.DirectConsumer;
import dash.performance.sequential.consumers.FieldObtainedConsumer;
import dash.performance.sequential.consumers.MethodObtainedConsumer;
import dash.providerFactory.ProviderFactory;

public class SequentialTiming {

    public static int SIZE = 1000;

    public static int LOOP = 5000;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        ProviderFactory.threadLocalProvider.set(new TestProvider());
        int size = SIZE;
        int loop = LOOP;
        if (args.length >= 1) {
            loop = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) {
            size = Integer.parseInt(args[1]);
        }
        SequentialTiming timer = new SequentialTiming();
        timer.run(size, loop);
    }

    private void run(int size, int loop) {
        System.out.println("Do timings for " + size + " consumers over " + loop + " iterations.");
        getLoopTiming(createDirectConsumers(1), 1);
        getLoopTiming(createAtomicConsumers(1), 1);
        getLoopTiming(createFieldObtainedConsumers(1), 1);
        getLoopTiming(createMethodObtainedConsumers(1), 1);
        List<Consumer> directConsumers = createDirectConsumers(size);
        List<Consumer> atomicConsumers = createAtomicConsumers(size);
        List<Consumer> fieldConsumers = createFieldObtainedConsumers(size);
        List<Consumer> methodConsumers = createMethodObtainedConsumers(size);
        publishTimings("Initial creation timings:", "Direct Consumer", getLoopTiming(directConsumers, 1), "Atomic Consumer", getLoopTiming(atomicConsumers, 1), "Field Consumer", getLoopTiming(fieldConsumers, 1), "Method Consumer", getLoopTiming(methodConsumers, 1));
        publishTimings("Looped access timings:", "Direct Consumer", getLoopTiming(directConsumers, loop), "Atomic Consumer", getLoopTiming(atomicConsumers, loop), "Field Consumer", getLoopTiming(fieldConsumers, loop), "Method Consumer", getLoopTiming(methodConsumers, loop));
    }

    private void publishTimings(String heading, String s1, long control, String s2, long t2, String s3, long t3, String s4, long t4) {
        System.out.println(heading);
        System.out.println("\tControl is " + s1 + " at " + control);
        if (control == 0) control = 1;
        System.out.println("\t" + s2 + " is " + (t2 / control) + "  [" + t2 + "]");
        System.out.println("\t" + s3 + " is " + (t3 / control) + "  [" + t3 + "]");
        System.out.println("\t" + s4 + " is " + (t4 / control) + "  [" + t4 + "]");
        System.out.println();
    }

    private long getLoopTiming(List<Consumer> list, int loop) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < LOOP; i++) {
            for (Consumer object : list) {
                if (object.getComponent() == null) {
                    throw new NullPointerException();
                }
            }
        }
        return System.currentTimeMillis() - start;
    }

    private List<Consumer> createDirectConsumers(int size) {
        List<Consumer> result = new ArrayList<Consumer>(size);
        for (int i = 0; i < size; i++) {
            result.add(new DirectConsumer());
        }
        return result;
    }

    private List<Consumer> createAtomicConsumers(int size) {
        List<Consumer> result = new ArrayList<Consumer>(size);
        for (int i = 0; i < size; i++) {
            result.add(new AtomicConsumer());
        }
        return result;
    }

    private List<Consumer> createFieldObtainedConsumers(int size) {
        List<Consumer> result = new ArrayList<Consumer>(size);
        for (int i = 0; i < size; i++) {
            result.add(new FieldObtainedConsumer());
        }
        return result;
    }

    private List<Consumer> createMethodObtainedConsumers(int size) {
        List<Consumer> result = new ArrayList<Consumer>(size);
        for (int i = 0; i < size; i++) {
            result.add(new MethodObtainedConsumer());
        }
        return result;
    }
}
