package calclipse.lib.math.util.graph3d;

import java.awt.Dimension;
import javax.swing.JFrame;
import calclipse.lib.math.util.RealFunction;
import calclipse.lib.math.util.graph3d.plotting.Plot3D;
import calclipse.lib.math.util.graph3d.plotting.Simple3DPlot;

public final class GraphPanel3DTestApp {

    private GraphPanel3DTestApp() {
    }

    private static final class Func implements RealFunction {

        public Func() {
        }

        @Override
        public int numberOfVariables() {
            return 2;
        }

        @Override
        public double getValue(final double... args) {
            final double x = args[0];
            final double y = args[1];
            return (x * x + y * y - 40) / 5;
        }
    }

    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        final GraphPanel3D panel = new GraphPanel3D();
        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setPreferredSize(new Dimension(300, 300));
        frame.pack();
        frame.setVisible(true);
        panel.setClipping(true);
        final Graph3D graph = panel.createGraph();
        final Plot3D<?> plot = new Simple3DPlot(graph, new Func(), 12, 12);
        plot.execute();
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        panel.repaint();
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        panel.setVolume(new Volume(-7, 7, -7, 7, -7, 7));
    }
}
