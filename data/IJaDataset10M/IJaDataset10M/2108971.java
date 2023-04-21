package net.sf.ij_plugins.util.progress;

/**
 * Example of using {@link ProgressAccumulator}.
 *
 * @author Jarek Sacha
 */
public final class ProgressExample3 {

    public static void main(String[] args) {
        CounterWithProgress counter1 = new CounterWithProgress('+');
        CounterWithProgress counter2 = new CounterWithProgress('*');
        ProgressAccumulator accumulator = new ProgressAccumulator();
        accumulator.addProgressReporter(counter1, 3, " + ");
        accumulator.addProgressReporter(counter2, 1, " * ");
        accumulator.setMinimumChange(0.1);
        accumulator.addProgressListener(new ProgressListener() {

            public void progressNotification(ProgressEvent e) {
                System.out.println("\nProgress listener: " + Math.round(e.getProgress() * 100) + "% " + e.getMessage());
            }
        });
        System.out.println("Counter '+'");
        counter1.count(100);
        System.out.println("Counter '*'");
        counter2.count(100);
    }
}
