package ch.romix.jsens.gui.effects;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import ch.romix.jsens.gui.utils.JSensBorder;

public class EffectStructureView extends JPanel implements IEffectStructureView {

    private JSplitPane jSplitPane1 = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JButton addVariable = null;

    private JButton addEdge = null;

    private JScrollPane scrollPane = null;

    private JPanel GraphPanel = null;

    private EffectPropertyView effectProperty = null;

    /**
	 * This is the default constructor
	 */
    public EffectStructureView() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        BorderLayout borderLayout1 = new BorderLayout();
        borderLayout1.setHgap(5);
        borderLayout1.setVgap(5);
        this.setLayout(borderLayout1);
        this.setSize(764, 561);
        this.add(getJSplitPane1(), BorderLayout.CENTER);
    }

    /**
	 * This method initializes GraphPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    public JPanel getGraphPanel() {
        if (GraphPanel == null) {
            GraphPanel = new JPanel();
            GraphPanel.setLayout(new BorderLayout());
        }
        return GraphPanel;
    }

    /**
	 * This method initializes addEdge
	 * 
	 * @return javax.swing.JButton
	 */
    public JButton getAddEdge() {
        if (addEdge == null) {
            addEdge = new JButton();
            addEdge.setIcon(new ImageIcon(getClass().getResource("/res/addEffect.png")));
            addEdge.setToolTipText("Beziehung hinzufügen");
        }
        return addEdge;
    }

    /**
	 * This method initializes addVariable
	 * 
	 * @return javax.swing.JButton
	 */
    public JButton getAddVariable() {
        if (addVariable == null) {
            addVariable = new JButton();
            addVariable.setIcon(new ImageIcon(getClass().getResource("/res/addVar.png")));
            addVariable.setToolTipText("Variable hinzufügen");
        }
        return addVariable;
    }

    /**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(FlowLayout.LEFT);
            jPanel1 = new JPanel();
            jPanel1.setLayout(flowLayout);
            jPanel1.add(getAddEdge(), null);
            jPanel1.add(getAddVariable(), null);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setHgap(5);
            borderLayout.setVgap(5);
            jPanel = new JPanel();
            jPanel.setLayout(borderLayout);
            jPanel.setBorder(new JSensBorder("Variablen und Beziehungen"));
            jPanel.add(getJPanel1(), BorderLayout.NORTH);
            jPanel.add(getScrollPane(), BorderLayout.CENTER);
        }
        return jPanel;
    }

    /**
	 * This method initializes scrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getGraphPanel());
        }
        return scrollPane;
    }

    /**
	 * This method initializes jSplitPane1
	 * 
	 * @return javax.swing.JSplitPane
	 */
    private JSplitPane getJSplitPane1() {
        if (jSplitPane1 == null) {
            jSplitPane1 = new JSplitPane();
            jSplitPane1.setResizeWeight(0.8D);
            jSplitPane1.setLeftComponent(getJPanel());
            jSplitPane1.setRightComponent(getEffectProperty());
        }
        return jSplitPane1;
    }

    public EffectPropertyView getEffectProperty() {
        if (effectProperty == null) {
            effectProperty = new EffectPropertyView();
        }
        return effectProperty;
    }
}
