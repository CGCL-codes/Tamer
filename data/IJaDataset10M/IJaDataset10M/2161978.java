package LRAC.bundlebuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.miginfocom.swing.MigLayout;
import AccordionLRACDrawer.bundles.Template;
import AccordionLRACDrawer.bundles.TemplateHandler;

/**
 * @author Peter McLachlan <spark343@cs.ubc.ca>
 *
 */
public class TemplateManager extends JFrame {

    static final int STARTWIDTH = 1000;

    static final int STARTHEIGHT = 800;

    static final Color BORDERCOLOR = Color.gray;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1043183053103080708L;

    private static final String TEMPLATELISTPANELNAME = "Template list";

    final JPanel templateListPanel = new JPanel();

    final TemplateEditorPanel templateEditorPanel = new TemplateEditorPanel(this);

    JLabel templateSelectionLabel = new JLabel("Select template");

    DefaultListModel templateListModel = new DefaultListModel();

    JList templateJList;

    JButton newButton = new JButton("New template");

    JButton delButton = new JButton("Delete template");

    TemplateHandler templateHandler;

    /**
	 * @param title
	 * @param f 
	 * @throws HeadlessException
	 */
    public TemplateManager(String title, File f) throws HeadlessException {
        super(title);
        this.setPreferredSize(new Dimension(STARTWIDTH, STARTHEIGHT));
        templateJList = new JList(templateListModel);
        loadTemplates(f);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                final String outFileName = "/tmp/outfile.xml";
                File f = new File(outFileName);
                System.out.println("Saving output xml to:" + outFileName);
                templateHandler.saveTemplates(f);
                System.out.println("Save complete");
                System.exit(0);
            }
        });
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, templateListPanel, templateEditorPanel);
        splitPane.setOneTouchExpandable(true);
        this.getContentPane().add(splitPane);
        addTemplateListPanelComponents();
        splitPane.setDividerLocation((int) templateListPanel.getPreferredSize().getWidth());
        templateEditorPanel.enableAllComponents(false);
        this.validate();
        this.pack();
        this.setVisible(true);
    }

    private void loadTemplates(File f) {
        final ClassLoader cl = this.getClass().getClassLoader();
        final String defaultFile = "LRAC/bundlebuilder/bundletester.xml";
        InputStream is;
        if (f == null || !f.canRead()) {
            is = cl.getResourceAsStream(defaultFile);
        } else {
            try {
                is = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                is = cl.getResourceAsStream(defaultFile);
            }
        }
        templateHandler = new TemplateHandler(is);
        HashMap<String, Template> templates = templateHandler.getTemplateMap();
        Iterator<Template> it = templates.values().iterator();
        while (it.hasNext()) {
            templateListModel.addElement(it.next());
        }
    }

    private void addTemplateListPanelComponents() {
        templateListPanel.setName(TEMPLATELISTPANELNAME);
        final String layoutConstraints = "wrap, fill";
        final String colConstraints = "[center]";
        final String rowConstraints = "[][fill,grow][][]";
        MigLayout layout = new MigLayout(layoutConstraints, colConstraints, rowConstraints);
        templateListPanel.setLayout(layout);
        templateJList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        templateJList.setLayoutOrientation(JList.VERTICAL);
        templateJList.setVisibleRowCount(-1);
        templateJList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                configTemplateEditorPanel((Template) templateJList.getSelectedValue());
                templateEditorPanel.enableAllComponents(true);
            }
        });
        newButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                templateListModel.addElement(templateHandler.newTemplate());
            }
        });
        delButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                templateHandler.delTemplate((Template) templateJList.getSelectedValue());
                templateListModel.remove(templateJList.getSelectedIndex());
            }
        });
        JScrollPane listScroller = new JScrollPane(templateJList);
        templateListPanel.add(templateSelectionLabel, "wrap, span, growx");
        templateListPanel.add(listScroller, "wrap, grow, span");
        templateListPanel.add(newButton, "grow, span, wrap");
        templateListPanel.add(delButton, "grow, span, wrap");
        templateListPanel.validate();
    }

    /**
	 * Configures the templateEditorPanel widgets
	 * @param string
	 */
    private void configTemplateEditorPanel(Template template) {
        templateEditorPanel.configWidgets(template);
    }

    public void disableAllComponents() {
        templateEditorPanel.enableAllComponents(false);
        templateJList.setEnabled(false);
        newButton.setEnabled(false);
    }

    public void enableAllComponents() {
        templateEditorPanel.enableAllComponents(true);
        templateJList.setEnabled(true);
        newButton.setEnabled(true);
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Unable to set look and feel");
        }
        File f = null;
        if (args.length > 0) {
            f = new File(args[0]);
            if (!f.canRead()) f = null;
        }
        TemplateManager b = new TemplateManager("Template manager", f);
    }

    public static final void addSeparator(JPanel panel, String text) {
        JLabel l = createLabel(text);
        panel.add(l, "gapbottom 1, span, split 2");
        JSeparator foo = new JSeparator();
        foo.setForeground(BORDERCOLOR);
        panel.add(foo, "gapleft rel, growx");
    }

    static final JLabel createLabel(String text) {
        return createLabel(text, SwingConstants.LEADING);
    }

    static final JLabel createLabel(String text, int align) {
        final JLabel label = new JLabel(text, align);
        return label;
    }
}
