package preprocessing.automatic.GUI.GUITabs;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jfree.layout.FormatLayout;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: 08/09/11
 * Time: 12:31
 * To change this template use File | Settings | File Templates.
 */
public class MethodUtilizationTabView extends JPanel {

    MethodUtilizationTabController controller;

    private JLabel runIndication;

    private JSlider runSelector;

    private JLabel attributeIndication;

    private JSlider attributeSelector;

    private JLabel typeSelectionStr;

    private JComboBox typeSelection;

    private JPanel graphArea;

    private int maxRunID = 0;

    CellConstraints cc;

    private int maxAttributeID;

    enum SliderEnum {

        attributeSelector, individualSelector
    }

    public MethodUtilizationTabView(MethodUtilizationTabController controller) {
        this.controller = controller;
        cc = new CellConstraints();
        createGUI();
    }

    private void createGUI() {
        FormLayout layout = new FormLayout("fill:10px:nogrow, right:150px:nogrow, left:250px:grow, fill:10px:nogrow", "10px:nogrow, fill:40px:nogrow, fill:40px:nogrow, fill:40px:nogrow, 10px:nogrow, fill:300px:grow, 10px:nogrow");
        setLayout(layout);
        runIndication = new JLabel("Run 0 / 0");
        add(runIndication, cc.xy(2, 2));
        runSelector = new JSlider();
        runSelector.setMaximum(0);
        runSelector.setValue(0);
        runSelector.setMinimum(0);
        runSelector.addChangeListener(controller);
        add(runSelector, cc.xy(3, 2));
        attributeIndication = new JLabel("All attributes");
        add(attributeIndication, cc.xy(2, 3));
        attributeSelector = new JSlider();
        attributeSelector.setMaximum(0);
        attributeSelector.setValue(0);
        attributeSelector.setMinimum(0);
        attributeSelector.addChangeListener(controller);
        add(attributeSelector, cc.xy(3, 3));
        typeSelectionStr = new JLabel("Set of individuals on graph: ");
        add(typeSelectionStr, cc.xy(2, 4));
        typeSelection = new JComboBox(new MethUtilizationAllRunsTabView.ShowUtilizationEnum[] { MethUtilizationAllRunsTabView.ShowUtilizationEnum.allIndividuals, MethUtilizationAllRunsTabView.ShowUtilizationEnum.betterHalfIndividuals, MethUtilizationAllRunsTabView.ShowUtilizationEnum.eliteIndividuals });
        typeSelection.addActionListener(controller);
        add(typeSelection, cc.xy(3, 4));
        graphArea = new JPanel(new FlowLayout());
        add(graphArea, cc.xywh(2, 6, 2, 1));
    }

    public void setMaxRunCount(int maxRuns) {
        runSelector.setMinimum(0);
        runSelector.setValue(0);
        runSelector.setMaximum(maxRuns);
        maxRunID = maxRuns;
        runIndication.setText("Run 0 / " + maxRuns);
    }

    public void setMaxAttributeCount(int numberOfAttributes) {
        attributeSelector.setMaximum(numberOfAttributes);
        attributeSelector.setValue(0);
        attributeIndication.setText("All attributes");
        maxAttributeID = numberOfAttributes;
    }

    public int getSelectedRun() {
        return runSelector.getValue();
    }

    public void setRunID(int runId) {
        runSelector.setValue(runId);
        runIndication.setText("Run " + runId + " / " + maxRunID);
    }

    public void setAttributeID(int selectedAttribute) {
        attributeSelector.setValue(selectedAttribute);
        if (selectedAttribute == 0) {
            attributeIndication.setText("All attributes");
        } else {
            attributeIndication.setText("Attribute " + selectedAttribute + " / " + maxAttributeID);
        }
    }

    public void clearGraph() {
        graphArea.removeAll();
    }

    public void setGraph(JComponent graph) {
        graphArea.removeAll();
        graphArea.add(graph);
        graphArea.revalidate();
        graphArea.repaint();
    }

    public int getSelectedAttribute() {
        return attributeSelector.getValue();
    }

    public MethUtilizationAllRunsTabView.ShowUtilizationEnum getSelectedIndividualsType() {
        return (MethUtilizationAllRunsTabView.ShowUtilizationEnum) typeSelection.getSelectedItem();
    }
}
