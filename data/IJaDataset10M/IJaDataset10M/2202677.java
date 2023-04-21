package jassEdit;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFileChooser;

public class Main extends JFrame implements ActionListener, CaretListener, KeyListener {

    static final long serialVersionUID = 0;

    SpringLayout mainLayout = new SpringLayout();

    DefaultMutableTreeNode blizzardTN = new DefaultMutableTreeNode("Blizzard");

    DefaultMutableTreeNode commonTN = new DefaultMutableTreeNode("Common");

    DefaultMutableTreeNode functionsTN = new DefaultMutableTreeNode("Functions");

    File opened;

    JTree blizzard = new JTree(blizzardTN);

    JTree functions = new JTree(functionsTN);

    JTree common = new JTree(commonTN);

    JTable errors = new JTable();

    JTextPane editor = new JTextPane();

    JMenuBar baseMenu = new JMenuBar();

    JMenu file = new JMenu("File");

    JMenu edit = new JMenu("Edit");

    JMenu help = new JMenu("Help");

    JMenuItem newScript = new JMenuItem("New");

    JMenuItem loadScript = new JMenuItem("Open");

    JMenuItem saveScript = new JMenuItem("Save");

    JMenuItem saveAsScript = new JMenuItem("Save As ...");

    JMenuItem settings = new JMenuItem("Settings");

    JMenuItem exit = new JMenuItem("Exit");

    JScrollPane editScroll = new JScrollPane(editor, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JScrollPane errorScroll = new JScrollPane(errors, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JScrollPane functionScroll = new JScrollPane(functions, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JScrollPane commonScroll = new JScrollPane(common, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JScrollPane blizzardScroll = new JScrollPane(blizzard, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    JTabbedPane functionTabs = new JTabbedPane();

    JTabbedPane errorTabs = new JTabbedPane();

    DefaultTableModel errModel = new DefaultTableModel();

    TableColumn errLine = new TableColumn();

    TableColumn errFunction = new TableColumn();

    TableColumn errExplain = new TableColumn();

    JFileChooser lScriptChooser = new JFileChooser(new File("j:\\Warcraft 3 Tools\\JassCraft"));

    Controller controller = new Controller(editor);

    public Main() {
        setControls();
        addControls();
        setSpringConstraints();
        getContentPane().setLayout(mainLayout);
        setFont(new Font("Monospaced", Font.PLAIN, 10));
        setTitle("JassEdit V1.0");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setJMenuBar(baseMenu);
        pack();
        setVisible(true);
        controller.startup();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new Main();
    }

    public void setControls() {
        editor.setContentType("text/plain");
        editor.addCaretListener(this);
        editScroll.setPreferredSize(new Dimension(700, 500));
        functionTabs.setPreferredSize(new Dimension(300, 500));
        errorTabs.setPreferredSize(new Dimension(1000, 200));
        setPreferredSize(new Dimension(1024, 768));
        errLine.setHeaderValue("Line");
        errFunction.setHeaderValue("Function");
        errExplain.setHeaderValue("Explanation");
        errLine.setCellRenderer(new DefaultTableCellRenderer());
        errFunction.setCellRenderer(new DefaultTableCellRenderer());
        errExplain.setCellRenderer(new DefaultTableCellRenderer());
        errModel.addColumn(errLine.getHeaderValue());
        errModel.addColumn(errFunction.getHeaderValue());
        errModel.addColumn(errExplain.getHeaderValue());
        errors.setModel(errModel);
        editor.addKeyListener(this);
    }

    public void setSpringConstraints() {
        mainLayout.putConstraint(SpringLayout.WEST, editScroll, 1, SpringLayout.WEST, getContentPane());
        mainLayout.putConstraint(SpringLayout.NORTH, editScroll, 1, SpringLayout.NORTH, getContentPane());
        mainLayout.putConstraint(SpringLayout.WEST, functionTabs, 1, SpringLayout.EAST, editScroll);
        mainLayout.putConstraint(SpringLayout.NORTH, functionTabs, 1, SpringLayout.NORTH, getContentPane());
        mainLayout.putConstraint(SpringLayout.WEST, errorTabs, 1, SpringLayout.WEST, getContentPane());
        mainLayout.putConstraint(SpringLayout.NORTH, errorTabs, 1, SpringLayout.SOUTH, editScroll);
    }

    public void addControls() {
        loadScript.addActionListener(this);
        exit.addActionListener(this);
        saveScript.addActionListener(this);
        saveAsScript.addActionListener(this);
        loadScript.setActionCommand("load");
        exit.setActionCommand("exit");
        saveScript.setActionCommand("save");
        saveAsScript.setActionCommand("saveAs");
        file.add(newScript);
        file.add(loadScript);
        file.add(saveScript);
        file.add(saveAsScript);
        file.add(settings);
        file.add(exit);
        baseMenu.add(file);
        baseMenu.add(edit);
        baseMenu.add(help);
        editScroll.getViewport().add(editor);
        errorScroll.getViewport().add(errors);
        errorTabs.add(errorScroll, 0);
        functionScroll.getViewport().add(functions);
        functionTabs.add(functionScroll, 0);
        functionTabs.add(commonScroll, 1);
        functionTabs.add(blizzardScroll, 2);
        errorTabs.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        errorTabs.setTitleAt(0, "Compile");
        functionTabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        functionTabs.setTitleAt(0, "Functions");
        functionTabs.setTitleAt(1, "Common.j");
        functionTabs.setTitleAt(2, "Blizzard.j");
        getContentPane().add(editScroll);
        getContentPane().add(functionTabs);
        getContentPane().add(errorTabs);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("load")) {
            int returnVal = lScriptChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                opened = lScriptChooser.getSelectedFile();
                functionTabs.setTitleAt(0, opened.getName());
                controller.loadJFile(opened);
            }
            repaint();
        } else if (e.getActionCommand().equals("exit")) {
            this.dispose();
        } else if (e.getActionCommand().equals("save")) {
            controller.save(opened);
        } else if (e.getActionCommand().equals("saveAs")) {
            controller.save(new File("d:\\test.j"));
        }
    }

    public void caretUpdate(CaretEvent e) {
        controller.setCurrentPos(e.getDot());
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
        if (e.getSource() == editor) {
            if (e.getKeyChar() == 13 || e.getKeyChar() == 32) {
                System.out.println("roept color aan");
                controller.cc.goColor();
            }
        }
    }
}
