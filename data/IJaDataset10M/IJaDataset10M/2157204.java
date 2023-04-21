package spinja;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import spinja.concurrent.model.ConcurrentModel;
import spinja.concurrent.search.PartialOrderReduction;
import spinja.exceptions.ValidationException;
import spinja.options.BooleanOption;
import spinja.options.MultiStringOption;
import spinja.options.NumberOption;
import spinja.options.OptionParser;
import spinja.promela.model.NeverClaimModel;
import spinja.promela.model.PromelaModel;
import spinja.promela.model.PromelaTransition;
import spinja.search.AcceptanceCycleSearch;
import spinja.search.Algoritm;
import spinja.search.BreadthFirstSearch;
import spinja.search.ConcurrentBFSearch;
import spinja.search.DepthFirstSearch;
import spinja.search.TransitionCalculator;
import spinja.search.RandomSimulation;
import spinja.search.TrailSimulation;
import spinja.search.UserSimulation;
import spinja.store.AtomicHashTable;
import spinja.store.BitstateHashStore;
import spinja.store.HashCompactStore;
import spinja.store.HashTable;
import spinja.store.ProbingHashTable;
import spinja.store.StateStore;

public class Run extends Thread {

    private StateStore store = null;

    private Algoritm algo = null;

    private double startTime;

    private final MultiStringOption impl = new MultiStringOption('D', "use a specific implementation option", new String[] { "BFS", "NOREDUCE", "BITSTATE", "HC", "ARRAY", "GTRAIL", "GUSER", "GRANDOM" }, new String[] { "uses a Breadth First Search algorithm", "disables the partial order reduction algoritm", "uses bitstate hashing for storing states\n" + "(this method does not guarantee a complete search)", "uses hash compaction for storing states \n" + "(this method does not guarantee a complete search)", "uses a hash table that uses array lists \n" + "instead of probing", "uses the trail-file to guide the search", "uses user input to guide the search", "uses a randomizer to guide the search" });

    private final BooleanOption checkAccept = new BooleanOption('a', "checks for acceptance cycles");

    private final BooleanOption ignoreAssert = new BooleanOption('A', "assert statements will be ignored");

    private final BooleanOption errorExceedDepth = new BooleanOption('b', "exceeding the depth limit is considered an error");

    private final NumberOption nrErrors = new NumberOption('c', "stops the verification after N errors \n" + "(when set to 0 it does not stop for any error)", 1, 0, Integer.MAX_VALUE);

    private final NumberOption concurrent = new NumberOption('C', "starts several concurrent threads (*experimental*)", 1, 1, 1024);

    private final BooleanOption ignoreInvalidEnd = new BooleanOption('E', "ignore invalid end states (deadlocks)");

    private final NumberOption nrBits = new NumberOption('k', "sets N bits per state when using bitstate hashing", 3, 1, 1024);

    private final NumberOption maxDepth = new NumberOption('m', "sets the maximum search depth", 10000, 1, Integer.MAX_VALUE);

    private final BooleanOption ignoreNever = new BooleanOption('N', "ignores any never claim (if present)");

    private final BooleanOption onlyVersion = new BooleanOption('v', "prints the version number and exits");

    private final NumberOption hashEntries = new NumberOption('w', "sets the number of entries in the hash table to 2^N", 21, 3, 31);

    private byte done = 0;

    private TransitionCalculator<ConcurrentModel<PromelaTransition>, PromelaTransition> nextTransition = null;

    public boolean getIgnoreAssert() {
        return ignoreAssert.isSet();
    }

    public boolean getIgnoreNever() {
        return ignoreNever.isSet();
    }

    public boolean getIgnoreInvalidEnd() {
        return ignoreInvalidEnd.isSet();
    }

    public void parseArguments(final String[] args, final String name) {
        final String shortd = "SpinJa Model Checker - version " + Version.VERSION + " (" + Version.DATE + ")\n" + "(C) University of Twente, Formal Methods and Tools group";
        final String longd = "SpinJa Model Checker is a (generated) Java program which verifies\n" + "whether the original Promela model (+ never claim) was correct.";
        final OptionParser p = new OptionParser("java spinja." + name + "Model", shortd, longd, false);
        p.addOption(impl);
        p.addOption(checkAccept);
        p.addOption(ignoreAssert);
        p.addOption(errorExceedDepth);
        p.addOption(concurrent);
        p.addOption(nrErrors);
        p.addOption(ignoreInvalidEnd);
        p.addOption(nrBits);
        p.addOption(maxDepth);
        p.addOption(ignoreNever);
        p.addOption(onlyVersion);
        p.addOption(hashEntries);
        p.parse(args);
    }

    private boolean realMem() {
        final Runtime r = Runtime.getRuntime();
        r.runFinalization();
        for (int i = 0; i < 16; i++) {
            r.gc();
        }
        final double memory = (r.totalMemory() - r.freeMemory()) / (1024.0 * 1024.0);
        System.out.printf("%7g real memory usage (Mbyte)\n", memory);
        return true;
    }

    @Override
    public void run() {
        if (done == 0) {
            System.out.println("Warning: Search not completed!");
        } else if (done == -1) {
            return;
        }
        algo.printSummary();
        double memory = algo.getBytes() / (1024.0 * 1024.0);
        System.out.printf("%7g memory usage (Mbyte)\n", memory);
        System.out.println();
        final double seconds = (System.currentTimeMillis() - startTime) / 1e3;
        System.out.printf("spinja: elapsed time %.2f seconds\n", seconds);
        System.out.printf("spinja: rate %8d states/second\n", (int) (algo.getNrStates() / seconds));
        realMem();
    }

    public void search(final Class<? extends PromelaModel> clazz) {
        Runtime.getRuntime().addShutdownHook(this);
        if (onlyVersion.isSet()) {
            done = -1;
            return;
        }
        ConcurrentModel<PromelaTransition> model = null;
        try {
            model = clazz.getConstructor().newInstance();
        } catch (final IllegalArgumentException e) {
            System.err.println("The process model should have at least a contructor with one boolean as a parameter.");
            e.printStackTrace();
            return;
        } catch (final SecurityException e) {
            System.err.println("The process model should have at least an accesable contructor with one boolean as a parameter.");
            e.printStackTrace();
            return;
        } catch (final InstantiationException e) {
            e.printStackTrace();
            return;
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            return;
        } catch (final InvocationTargetException e) {
            System.err.println(e.getCause().getMessage());
            return;
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
            return;
        }
        if (!ignoreNever.isSet()) {
            try {
                model = NeverClaimModel.createNever((PromelaModel) model);
                if (model instanceof NeverClaimModel) {
                    ignoreInvalidEnd.parseOption(null);
                }
            } catch (final ValidationException e) {
                System.out.println("Warning: Could not create never-claim: " + e.getMessage());
            }
        }
        boolean guided = impl.isSet("GUSER") || impl.isSet("GTRAIL") || impl.isSet("GRANDOM");
        boolean reduced = !impl.isSet("NOREDUCE");
        if (reduced) {
            System.out.println("        + Partial Order Reduction");
            nextTransition = new PartialOrderReduction<ConcurrentModel<PromelaTransition>, PromelaTransition>();
        } else {
            nextTransition = new TransitionCalculator<ConcurrentModel<PromelaTransition>, PromelaTransition>();
        }
        System.out.println();
        System.out.println("Full statespace search for:");
        System.out.print("        never claim             ");
        System.out.println(model instanceof NeverClaimModel ? "+" : "-");
        System.out.print("        assertion violations    ");
        System.out.println(getIgnoreAssert() ? "-" : "+");
        System.out.print("        cycle checks            ");
        System.out.println(checkAccept.isSet() ? "+ Acceptance cycles" : "-");
        System.out.print("        invalid end states      ");
        System.out.println(ignoreInvalidEnd.isSet() ? "-" : "+");
        System.out.println();
        int maxDepth = this.maxDepth.getValue();
        if (!guided) {
            if (concurrent.getValue() > 1) {
                store = new AtomicHashTable(hashEntries.getValue());
            } else if (impl.isSet("BITSTATE")) {
                store = new BitstateHashStore(hashEntries.getValue(), nrBits.getValue());
            } else if (impl.isSet("HC")) {
                store = new HashCompactStore(hashEntries.getValue(), nrBits.getValue());
            } else if (impl.isSet("ARRAY")) {
                store = new HashTable(hashEntries.getValue());
            } else {
                store = new ProbingHashTable(hashEntries.getValue());
            }
        }
        if (impl.isSet("GUSER")) {
            algo = new UserSimulation<ConcurrentModel<PromelaTransition>, PromelaTransition>(model, nextTransition);
        } else if (impl.isSet("GTRAIL")) {
            try {
                algo = new TrailSimulation<ConcurrentModel<PromelaTransition>, PromelaTransition>(model, nextTransition);
            } catch (final FileNotFoundException ex) {
                System.out.println("Could not find trail-file: " + ex.getMessage());
                return;
            }
        } else if (impl.isSet("GRANDOM")) {
            algo = new RandomSimulation<ConcurrentModel<PromelaTransition>, PromelaTransition>(model, nextTransition);
        } else if (impl.isSet("BFS")) {
            algo = new BreadthFirstSearch<ConcurrentModel<PromelaTransition>, PromelaTransition>(model, store, errorExceedDepth.isSet(), nrErrors.getValue(), !ignoreInvalidEnd.isSet(), nextTransition);
        } else if (concurrent.getValue() > 1) {
            algo = new ConcurrentBFSearch<ConcurrentModel<PromelaTransition>, PromelaTransition>(concurrent.getValue(), model, store, !ignoreInvalidEnd.isSet(), nrErrors.getValue(), errorExceedDepth.isSet(), nextTransition);
        } else if (checkAccept.isSet()) {
            algo = new AcceptanceCycleSearch<ConcurrentModel<PromelaTransition>, PromelaTransition>(model, store, maxDepth, errorExceedDepth.isSet(), !ignoreInvalidEnd.isSet(), nrErrors.getValue(), nextTransition);
        } else {
            algo = new DepthFirstSearch<ConcurrentModel<PromelaTransition>, PromelaTransition>(model, store, maxDepth, errorExceedDepth.isSet(), !ignoreInvalidEnd.isSet(), nrErrors.getValue(), nextTransition);
        }
        startTime = System.currentTimeMillis();
        try {
            algo.execute();
            done = 1;
        } catch (final OutOfMemoryError er) {
            algo.freeMemory();
            System.out.println("spinja error: Maximum memory reached. Please make more memory available for the virtual machine.");
        } finally {
            if (concurrent.getValue() > 1) {
                AtomicHashTable table = (AtomicHashTable) store;
                table.shutdown();
                try {
                    table.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
