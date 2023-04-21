package xdevs.kernel.modeling.view;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import xdevs.kernel.simulation.Coordinator;

/**
 *
 * @author jlrisco
 */
public class CoordinatorView extends JFrame {

    protected mxGraph graph;

    protected CoupledView coupledView;

    protected Coordinator coordinator;

    protected boolean firstStep;

    public CoordinatorView(CoupledView coupledView) {
        super();
        this.graph = new mxGraph();
        this.coupledView = coupledView;
        try {
            coupledView.buildNodes(graph.getDefaultParent(), graph);
            coupledView.buildEdges(graph);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        coordinator = new Coordinator(coupledView);
        this.firstStep = true;
        this.initialize();
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setTitle("xDEVS Simulator");
    }

    private void initialize() {
        mxGraphComponent graphPanel = new mxGraphComponent(graph);
        JButton buttonStep = new JButton();
        buttonStep.setText("Step");
        buttonStep.setEnabled(true);
        buttonStep.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                double t = coordinator.getTN();
                if (!firstStep) {
                    coordinator.clear();
                } else {
                    firstStep = false;
                }
                coordinator.lambda(t);
                coordinator.deltfcn(t);
            }
        });
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.add(buttonStep, null);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(graphPanel);
        content.add(panelButtons);
        super.setContentPane(content);
        super.pack();
    }
}
