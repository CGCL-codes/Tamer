package da.gui2.rule;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.io.IOException;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.log4j.helpers.Loader;
import partex.gate.jape.PxRule;
import partex.gate.transducer.JapeTransducerWrapper;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import da.gui2.Utils;
import edu.uci.ics.jung.graph.impl.SparseTree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.TreeLayout;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import gate.Factory;
import gate.jape.parser.ParseCpsl;
import gate.jape.parser.ParseException;

public class RuleView extends JDialog {

    private static final long serialVersionUID = -1990732757069341877L;

    private GraphZoomScrollPane gzsp;

    private RuleModel ruleModel;

    private boolean canceled;

    private JTextArea japeString;

    private JFormattedTextField priority;

    private JTextField ruleName;

    private TreeLayout layout;

    public RuleView(Frame parent, PxRule model) {
        ruleModel = new RuleModel(model);
        initComponents();
        initEventHandling();
    }

    private void initComponents() {
        SparseTree graph = ruleModel.getGraph();
        Renderer renderer = ruleModel.getRenderer();
        layout = new TreeLayout(graph);
        VisualizationViewer vv = new VisualizationViewer(layout, renderer);
        gzsp = new GraphZoomScrollPane(vv);
        gzsp.setDoubleBuffered(true);
        japeString = new JTextArea();
        japeString.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        japeString.setTabSize(3);
        priority = BasicComponentFactory.createIntegerField(ruleModel.getBufferedModel(PxRule.PROPERTYNAME_PRIORITY));
        ruleName = BasicComponentFactory.createTextField(ruleModel.getBufferedModel(PxRule.PROPERTYNAME_RULE_NAME));
        DefaultModalGraphMouse graphMouse;
        graphMouse = new DefaultModalGraphMouse();
        graphMouse.setMode(Mode.EDITING);
        vv.setGraphMouse(graphMouse);
        vv.addGraphMouseListener(ruleModel.getGraphMouseListener());
    }

    private void initEventHandling() {
        PropertyConnector.connect(ruleModel, RuleModel.PROPERTYNAME_SELECTED_JAPE_ELEMENT, japeString, "text").updateProperty2();
    }

    /**
	 * @return
	 */
    public JComponent buildPanel() {
        FormLayout mainFormLayout = new FormLayout("fill:350px:grow, fill:250px:grow", "fill:100px, fill:400px:grow");
        FormLayout subFormLayout = new FormLayout("fill:50px:grow", "20px, 20px");
        DefaultFormBuilder mainPanelBuilder = new DefaultFormBuilder(mainFormLayout);
        DefaultFormBuilder subPanelBuilder = new DefaultFormBuilder(subFormLayout);
        CellConstraints cc = new CellConstraints();
        mainPanelBuilder.setDefaultDialogBorder();
        mainPanelBuilder.add(gzsp, cc.xywh(1, 1, 1, 2));
        mainPanelBuilder.add(japeString, cc.xy(2, 2));
        mainPanelBuilder.setLineGapSize(Sizes.ZERO);
        mainPanelBuilder.setBorder(BorderFactory.createLineBorder(Color.black));
        subPanelBuilder.append("Priority", priority);
        subPanelBuilder.append("Rule Name", ruleName);
        mainPanelBuilder.add(subPanelBuilder.getPanel(), cc.xy(2, 1));
        return mainPanelBuilder.getPanel();
    }

    public static void showRule(PxRule rule) {
        if (rule == null) throw new NullPointerException("Rule can't be null");
        JFrame jf = new JFrame();
        RuleView view = new RuleView(jf, rule);
        JComponent panel = view.buildPanel();
        jf.getContentPane().add(panel);
        jf.setVisible(true);
        jf.setSize(850, 600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(true);
        Utils.locateOnOpticalScreenCenter(jf);
    }

    public static void main(String[] args) throws IOException, ParseException {
        URL japeFile = Loader.getResource("url_pre.jape");
        ParseCpsl cpslParser = Factory.newJapeParser(japeFile, "ISO-8859-1");
        JapeTransducerWrapper transducer = new JapeTransducerWrapper(cpslParser.SinglePhaseTransducer());
        PxRule pxRule = transducer.getRule(0);
        showRule(pxRule);
    }

    /**
	 * Builds the dialog's content pane, packs it, sets the resizable property,
	 * and locates it on the screen. The dialog is then ready to be opened.
	 * <p>
	 * 
	 * Subclasses should rarely override this method.
	 */
    private void build() {
        setContentPane(buildPanel());
        pack();
        setResizable(false);
        Utils.locateOnOpticalScreenCenter(this);
    }

    /**
	 * Builds the dialog content, marks it as not canceled and makes it visible.
	 */
    public void open() {
        build();
        canceled = false;
        setVisible(true);
    }

    /**
	 * Closes the dialog: releases obsolete bindings, and disposes the dialog,
	 * which in turn releases all required OS resources.
	 */
    public void close() {
        dispose();
    }

    /**
	 * Checks and answers whether the dialog has been canceled. This indicator
	 * is set in #doAccept and #doCancel.
	 * 
	 * @return true indicates that the dialog has been canceled
	 */
    public boolean hasBeenCanceled() {
        return canceled;
    }
}
