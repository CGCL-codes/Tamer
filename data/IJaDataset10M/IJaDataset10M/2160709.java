package preprocessing.automatic.GUI.IndividualApplicator;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: cepekm1
 * Date: 1/26/11
 * Time: 10:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndividualApplicatorView extends JPanel {

    private JTabbedPane tabs;

    private IndividualApplicatorControl ctrl;

    private CellConstraints cc;

    private JButton loadTrainingDataBtn;

    private JButton loadTestingDataBtn;

    private JButton dropTestingDataBtn;

    private JButton dropAllDataBtn;

    private JButton applyIndividualsBtn;

    private JButton dropTrainingDataBtn;

    private JButton dropSelectedIndividualsBtn;

    private JButton dropAllIndividualsBtn;

    private JLabel trainingDataFilename;

    private JLabel testingDataFilename;

    public IndividualApplicatorView(IndividualApplicatorControl ctrl) {
        this.ctrl = ctrl;
        createGUI();
    }

    private void createGUI() {
        cc = new CellConstraints();
        setLayout(new FormLayout("fill:20px:nogrow, fill:200px:grow, fill:150px:nogrow, fill:150px:nogrow, fill:20px:nogrow", "fill:20px:nogrow, fill:350px:grow, fill:10px:nogrow, fill:30px:nogrow, fill:2px:nogrow, fill:30:nogrow, fill:2px:nogrow, fill:30px:nogrow, fill:2px:nogrow, fill:30px:nogrow, fill:20px:nogrow"));
        tabs = new JTabbedPane();
        add(tabs, cc.xywh(2, 2, 3, 1));
        loadTrainingDataBtn = new JButton("Load data");
        loadTrainingDataBtn.addActionListener(ctrl);
        add(loadTrainingDataBtn, cc.xy(3, 4));
        loadTestingDataBtn = new JButton("Load testing data");
        loadTestingDataBtn.addActionListener(ctrl);
        add(loadTestingDataBtn, cc.xy(3, 6));
        trainingDataFilename = new JLabel("Data: No file loaded");
        add(trainingDataFilename, cc.xy(2, 4));
        testingDataFilename = new JLabel("Testing data: No file loaded");
        add(testingDataFilename, cc.xy(2, 6));
        dropTrainingDataBtn = new JButton("Drop training data");
        dropTrainingDataBtn.addActionListener(ctrl);
        add(dropTrainingDataBtn, cc.xy(4, 4));
        dropTestingDataBtn = new JButton("Drop testing data");
        dropTestingDataBtn.addActionListener(ctrl);
        add(dropTestingDataBtn, cc.xy(4, 6));
        applyIndividualsBtn = new JButton("Apply selected individuals");
        applyIndividualsBtn.addActionListener(ctrl);
        add(applyIndividualsBtn, cc.xy(3, 8));
        dropAllDataBtn = new JButton("Drop all data");
        dropAllDataBtn.addActionListener(ctrl);
        add(dropAllDataBtn, cc.xy(4, 8));
        dropSelectedIndividualsBtn = new JButton("Drop selected individuals");
        dropSelectedIndividualsBtn.addActionListener(ctrl);
        add(dropSelectedIndividualsBtn, cc.xy(3, 10));
        dropAllIndividualsBtn = new JButton("Drop all inidividuals");
        dropAllIndividualsBtn.addActionListener(ctrl);
        add(dropAllIndividualsBtn, cc.xy(4, 10));
    }

    public void addTab(JPanel nextTab, String tabName) {
        tabs.add(tabName, nextTab);
    }

    public void setTrainingFilename(String trainingFilename) {
        trainingDataFilename.setText("Data: " + trainingFilename);
        trainingDataFilename.setToolTipText(trainingFilename);
    }

    public void setTestingDataFilename(String testingDataFilename) {
        this.testingDataFilename.setText(testingDataFilename);
    }

    public void setLoadTestingDataEnabled(boolean enabled) {
        loadTestingDataBtn.setEnabled(enabled);
    }

    public void setDropTestingDataEnabled(boolean enabled) {
        dropTestingDataBtn.setEnabled(enabled);
    }

    public void setDropAllDataEnabled(boolean enabled) {
        dropAllDataBtn.setEnabled(enabled);
    }

    public void setApplyIndividualsEnabled(boolean enabled) {
        applyIndividualsBtn.setEnabled(enabled);
    }

    public void setTrainingFilename() {
        setTrainingFilename("No data loaded");
    }

    public void setTestingDataFilename() {
        setTestingDataFilename("No data loaded");
    }

    public int getActiveTabIndex() {
        return tabs.getSelectedIndex();
    }

    public void setLoadTrainingDataEnabled(boolean enabled) {
        loadTrainingDataBtn.setEnabled(enabled);
    }

    public void setDropTrainingDataEnabled(boolean enabled) {
        dropTrainingDataBtn.setEnabled(enabled);
    }
}
