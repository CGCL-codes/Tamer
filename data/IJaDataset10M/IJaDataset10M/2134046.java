package edu.diseno.jaspict3d.gui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import org.jgrapht.Graph;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import edu.diseno.jaspict3d.control.SceneManager;
import edu.diseno.jaspict3d.model.ModelComponent;
import edu.diseno.jaspict3d.render.GraphicScene;
import edu.diseno.jaspict3d.render.JaspictRenderer;
import edu.diseno.jaspict3d.render.engine.AbstractRendererFactory;
import edu.diseno.jaspict3d.render.engine.jmonkey.JMonkeyRendererDefault;
import edu.diseno.jaspict3d.render.engine.jmonkey.JMonkeyRendererFactory;
import edu.diseno.jaspict3d.render.layout.RandomLayout;

public class GuiJaspictMainForm {

    private JFrame frame;

    protected SceneManager sceneManager;

    private GraphicScene graphicScene;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
        }
        new GuiJaspictMainForm().buildMainFrame();
    }

    public void buildMainFrame() {
        frame = new JFrame();
        frame.setTitle("Jaspict - An Aspect visualization tool");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setJMenuBar(buildMenuBar());
        JComponent panel = buildMainPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel buildMainPanel() {
        FormLayout layout = new FormLayout("pref:grow,pref", "pref,pref,pref,pref");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(buildToolBar(), cc.xyw(1, 1, 2));
        builder.add(buildSceneOptions(), cc.xy(1, 3));
        builder.add(buildConsole(), cc.xyw(1, 4, 2));
        return builder.getPanel();
    }

    public JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JMenuItem menuItem11 = new JMenuItem("New..");
        JMenuItem menuItem12 = new JMenuItem("Close");
        JMenuItem menuItem13 = new JMenuItem("Exit");
        menu1.add(menuItem11);
        menu1.add(menuItem12);
        menu1.add(menuItem13);
        JMenu menu2 = new JMenu("Scene");
        JMenuItem menuItem21 = new JMenuItem("Open...");
        JMenuItem menuItem22 = new JMenuItem("Close");
        JMenuItem menuItem23 = new JMenuItem("Build");
        menu2.add(menuItem21);
        menu2.add(menuItem22);
        menu2.add(menuItem23);
        JMenu menu3 = new JMenu("Help");
        JMenuItem menuItem31 = new JMenuItem("Help");
        JMenuItem menuItem32 = new JMenuItem("About");
        menu3.add(menuItem31);
        menu3.add(menuItem32);
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        return menuBar;
    }

    public JPanel buildDrawArea() {
        FormLayout layout = new FormLayout("300dlu:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon("jgraphExample.jpg");
        label.setIcon(icon);
        builder.appendSeparator("Scene Area");
        builder.append(label);
        return builder.getPanel();
    }

    public JTabbedPane buildSceneOptions() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty("jgoodies.noContentBorder", Boolean.TRUE);
        tabbedPane.add(buildOptionTab(), "Scene");
        tabbedPane.add(buildSettingsTab(), "Settings");
        return tabbedPane;
    }

    private JPanel buildOptionTab() {
        JComboBox comboBox = new JComboBox(new String[] { "JMonkey_2_1", "JGraph2D" });
        int MIN = 0;
        int MAX = 3;
        int INIT = 3;
        JSlider slider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, INIT);
        slider.setMajorTickSpacing(3);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        FormLayout layout = new FormLayout("right:pref, 3dlu, min:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setRowGroupingEnabled(true);
        builder.appendSeparator("Scene Options");
        builder.append("Draw Engine", comboBox);
        builder.append("Abstraction Level", slider);
        return builder.getPanel();
    }

    private JPanel buildSettingsTab() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, min:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setRowGroupingEnabled(true);
        builder.appendSeparator("Source File");
        builder.append("Location", new JTextField("c:/jaspict3d/example/statsSystem/aop-stats.xml"));
        builder.appendSeparator("Intermediate scene file");
        builder.append("Default location", new JTextField("c:/jaspict3d/target/scene001.gml"));
        return builder.getPanel();
    }

    public JPanel buildConsole() {
        FormLayout layout = new FormLayout("pref:grow");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.appendSeparator("Jaspict Output");
        builder.appendRow("fill:60dlu:grow");
        builder.append(new JScrollPane(new JTextArea()));
        return builder.getPanel();
    }

    public JPanel buildTreeModelView() {
        return null;
    }

    public JToolBar buildToolBar() {
        JCheckBox checkBox = new JCheckBox("Start Build Immediately");
        checkBox.setSelected(true);
        JToolBar toolBar = new JToolBar();
        toolBar.add(new JButton(new LaunchRamdomTestAction("Launch")));
        toolBar.addSeparator();
        toolBar.add(new JButton("Build"));
        toolBar.add(checkBox);
        return toolBar;
    }

    class LaunchRamdomTestAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public LaunchRamdomTestAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml", "applicationContext-scenebuilder.xml" });
            BeanFactory factory = (BeanFactory) appContext;
            sceneManager = (SceneManager) factory.getBean("sceneManager");
            sceneManager.createScene();
            Graph<ModelComponent, ModelComponent> modelGraph = sceneManager.getModelScene().getSceneGraph();
            final JaspictRenderer renderer = new JMonkeyRendererDefault();
            graphicScene = new GraphicScene(new RandomLayout(), modelGraph, renderer);
            AbstractRendererFactory rendererFactory = new JMonkeyRendererFactory();
            graphicScene.setRendererFactory(rendererFactory);
            graphicScene.buildSceneGraph();
            graphicScene.draw();
        }
    }
}
