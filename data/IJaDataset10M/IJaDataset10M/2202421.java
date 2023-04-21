package submodule;

import java.util.Hashtable;
import main.Demo;
import main.MengNewMIDlet;
import com.sun.lwuit.Button;
import com.sun.lwuit.ButtonGroup;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.RadioButton;
import com.sun.lwuit.TabbedPane;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Style;
import ext.FormExt;
import ext.LineChartComponent;
import ext.PieChartComponent;
import ext.TreeComponent;
import ext.TreeModel;
import ext.TreeNode;

/**
 * Demot of the TabbedPane available in the UI
 * 
 * @author Tamir Shabat
 */
public class TabbedPaneDemo extends Demo {

    TabbedPane tp = null;

    public void cleanup() {
        tp = null;
    }

    public String getName() {
        return "Tabs";
    }

    protected String getHelp() {
        return "This Demo shows TabbedPane functionalities";
    }

    protected void execute(FormExt f) {
        f.setLayout(new BorderLayout());
        f.setScrollable(false);
        tp = new TabbedPane();
        PieChartComponent cd = new PieChartComponent(null);
        cd.setRatios(new double[] { 0.125, 0.20, 0.30, 0.15, 0.225 });
        cd.setNames(new String[] { "测试01", "测试02", "测试03", "测试04", "测试05" });
        tp.addTab("Tab 1", cd);
        double[][] values = { { 101, 82, 80, 31, 114, 130, 147, 122, 109, 55, 4 }, { 129, 9, 142, 134, 92, 79, 25, 31, 66, 149, 137 }, { 20, 6, 60, 71, 112, 29, 8, 105, 14, 10, 79 } };
        String[] names = { "部门01", "部门02", "部门03" };
        String[] arrayX = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11" };
        int countY = 10;
        int unitValueY = 16;
        LineChartComponent bc = new LineChartComponent();
        bc.setYNames(names);
        bc.setYValues(values);
        bc.setYCount(countY);
        bc.setYUnit(unitValueY);
        bc.setXValues(arrayX);
        bc.setXCount(arrayX.length);
        tp.addTab("Tab 2", bc);
        Container radioButtonsPanel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        RadioButton topRB = new RadioButton("Top");
        RadioButton LeftRB = new RadioButton("Left");
        RadioButton BottomRB = new RadioButton("Bottom");
        RadioButton RightRB = new RadioButton("Right");
        RadioListener myListener = new RadioListener();
        topRB.addActionListener(myListener);
        LeftRB.addActionListener(myListener);
        BottomRB.addActionListener(myListener);
        RightRB.addActionListener(myListener);
        ButtonGroup group1 = new ButtonGroup();
        group1.add(topRB);
        group1.add(LeftRB);
        group1.add(BottomRB);
        group1.add(RightRB);
        radioButtonsPanel.addComponent(new Label("Please choose a tab placement direction:"));
        radioButtonsPanel.addComponent(topRB);
        radioButtonsPanel.addComponent(LeftRB);
        radioButtonsPanel.addComponent(BottomRB);
        radioButtonsPanel.addComponent(RightRB);
        tp.addTab("Tab 3", radioButtonsPanel);
        Container panel = new Container(new BorderLayout());
        Image img1 = MengNewMIDlet.getResource().getImage("duke3_1.gif");
        Button button = new Button("North Button", img1);
        Style style = button.getStyle();
        style.setBorder(null);
        panel.addComponent("North", button);
        tp.addTab("Tab 4", panel);
        TreeNode[] sillyTree = { new TreeNode("Root 1", new TreeNode[] { new TreeNode("Child 1", new TreeNode[] { new TreeNode("Gand Child 1", new TreeNode[] {}), new TreeNode("Gand Child 2", new TreeNode[] {}), new TreeNode("Gand Child 3", new TreeNode[] {}), new TreeNode("Gand Child 4", new TreeNode[] {}) }), new TreeNode("Child 2", new TreeNode[] { new TreeNode("Something Else", new TreeNode[] {}), new TreeNode("More of the same", new TreeNode[] {}) }), new TreeNode("Child 3", new TreeNode[] {}), new TreeNode("Child 4", new TreeNode[] {}) }), new TreeNode("Root 2", new TreeNode[] { new TreeNode("Something Else", new TreeNode[] {}), new TreeNode("More of the same", new TreeNode[] {}) }), new TreeNode("Root 3", new TreeNode[] { new TreeNode("Something Else", new TreeNode[] {}), new TreeNode("More of the same", new TreeNode[] {}) }), new TreeNode("Root 4", new TreeNode[] {}) };
        TreeModel tm = new TreeModel(sillyTree);
        TreeComponent tree = new TreeComponent(tm);
        panel = new Container(new BorderLayout());
        panel.addComponent(BorderLayout.CENTER, tree);
        tp.addTab("Tab 5", panel);
        f.addComponent("Center", tp);
    }

    /** Listens to the radio buttons. */
    class RadioListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String title = ((RadioButton) e.getSource()).getText();
            if ("Top".equals(title)) {
                tp.setTabPlacement(TabbedPane.TOP);
            } else if ("Left".equals(title)) {
                tp.setTabPlacement(TabbedPane.LEFT);
            } else if ("Bottom".equals(title)) {
                tp.setTabPlacement(TabbedPane.BOTTOM);
            } else {
                tp.setTabPlacement(TabbedPane.RIGHT);
            }
        }
    }

    public String getZH_CN_Name() {
        return "报表测试";
    }

    public void execute(Hashtable table, FormExt fx) {
    }
}
