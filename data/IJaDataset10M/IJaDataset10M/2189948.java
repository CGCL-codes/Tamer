package ru.pit.homemoney.ui.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ru.pit.homemoney.ui.components.AccountsSummaryTable;
import com.l2fprod.common.demo.TaskPaneMain;
import com.l2fprod.common.swing.JLinkButton;
import com.l2fprod.common.swing.JTaskPane;
import com.l2fprod.common.swing.JTaskPaneGroup;
import com.l2fprod.common.swing.plaf.LookAndFeelAddons;
import com.l2fprod.common.swing.plaf.aqua.AquaLookAndFeelAddons;

/**
 * 
 * 
 * @author P.Salnikov (p.salnikov@gmail.com)
 * @version $Revision: 121 $
 */
public class MainForm extends JFrame {

    private JComponent panelContent = null;

    private JToolBar panelTop = null;

    private JPanel panelTasks = null;

    private JPanel panelMain = null;

    private JPanel panelStatus = null;

    private JMenuBar menuBar = null;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainForm thisClass;
                try {
                    thisClass = new MainForm();
                    thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    thisClass.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * This is the default constructor
     */
    public MainForm() throws Exception {
        super();
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     * @throws IllegalAccessException
     * @throws Exception
     */
    private void initialize() throws Exception {
        this.setBounds(100, 100, 700, 500);
        this.setJMenuBar(getPanelMenu());
        this.setLayout(new BorderLayout());
        this.add(getPanelTop(), BorderLayout.NORTH);
        this.add(getPanelMain(), BorderLayout.CENTER);
        this.setTitle("JFrame");
    }

    private JMenuBar getPanelMenu() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            JMenu menuFile = new JMenu("File");
            menuFile.setMnemonic('F');
            JMenuItem menuFile_ItemOpen = new JMenuItem("Open");
            menuFile.add(menuFile_ItemOpen);
            menuBar.add(menuFile);
        }
        return menuBar;
    }

    /**
     * This method initializes panelTop
     * 
     * @return javax.swing.JPanel
     */
    private JComponent getPanelTop() {
        if (panelTop == null) {
            panelTop = new JToolBar(JToolBar.HORIZONTAL);
            panelTop.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            panelTop.setPreferredSize(new Dimension(700, 70));
            ButtonGroup group = new ButtonGroup();
            JToggleButton btn1 = new JToggleButton();
            btn1.setText("Счета");
            btn1.setIcon(new ImageIcon(getClass().getResource("/icons/lock32x32.png")));
            btn1.setPreferredSize(new Dimension(60, 60));
            btn1.setVerticalAlignment(SwingConstants.CENTER);
            JToggleButton btn2 = new JToggleButton();
            btn2.setText("Банки");
            btn2.setIcon(new ImageIcon(getClass().getResource("/icons/folder32x32.png")));
            btn2.setPreferredSize(new Dimension(60, 60));
            btn2.setVerticalAlignment(SwingConstants.CENTER);
            panelTop.add(btn1);
            group.add(btn1);
            panelTop.add(btn2);
            group.add(btn2);
            panelTop.addSeparator();
            JButton btn3 = new JButton();
            btn3.setText("Банки");
            btn3.setIcon(new ImageIcon(getClass().getResource("/icons/folder32x32.png")));
            btn3.setPreferredSize(new Dimension(60, 60));
            btn3.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn3.setHorizontalTextPosition(SwingConstants.CENTER);
            panelTop.add(btn3);
        }
        return panelTop;
    }

    /**
     * This method initializes panelTasks
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelTasks() {
        if (panelTasks == null) {
            panelTasks = new JPanel();
            panelTasks.setLayout(new BorderLayout());
            JTaskPane taskPane = new JTaskPane();
            taskPane.add(getTaskGroupInfo());
            taskPane.add(getTaskGroupAccounts());
            taskPane.add(getTaskGroupOperations());
            JScrollPane scroll = new JScrollPane(taskPane);
            panelTasks.add(scroll, BorderLayout.CENTER);
        }
        return panelTasks;
    }

    private Component getTaskGroupOperations() {
        JTaskPaneGroup group = new JTaskPaneGroup();
        group.setTitle("Info");
        group.add(makeAction("Word", "", "icons/tasks-writedoc.png"));
        group.setExpanded(false);
        group.setScrollOnExpand(true);
        return group;
    }

    private Component getTaskGroupAccounts() {
        JTaskPaneGroup group = new JTaskPaneGroup();
        group.setTitle("Info");
        group.add(makeAction("Word", "", "icons/tasks-writedoc.png"));
        group.setExpanded(false);
        group.setScrollOnExpand(true);
        return group;
    }

    private JTaskPaneGroup getTaskGroupInfo() {
        JTaskPaneGroup group = new JTaskPaneGroup();
        group.setTitle("Info");
        group.add(makeAction("Word", "", "icons/tasks-writedoc.png"));
        group.setExpanded(true);
        group.setScrollOnExpand(true);
        return group;
    }

    private Action makeAction(String title, String tooltiptext, String iconPath) {
        Action action = new AbstractAction(title) {

            public void actionPerformed(ActionEvent e) {
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon(TaskPaneMain.class.getResource(iconPath)));
        action.putValue(Action.SHORT_DESCRIPTION, tooltiptext);
        return action;
    }

    /**
     * This method initializes panelMain
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getPanelMain() {
        if (panelMain == null) {
            panelMain = new JPanel();
            panelMain.setLayout(new BorderLayout(5, 5));
            panelMain.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelMain.add(getPanelTasks(), BorderLayout.WEST);
            panelMain.add(getPanelContent(), BorderLayout.CENTER);
        }
        return panelMain;
    }

    private JComponent getPanelContent() {
        if (panelContent == null) {
            AccountsSummaryTable accountsSummaryTable = new AccountsSummaryTable();
            accountsSummaryTable.setSize(400, 0);
            JPanel innerContent = new JPanel(new BorderLayout());
            innerContent.add(accountsSummaryTable, BorderLayout.WEST);
            panelContent = new JScrollPane(innerContent, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            panelContent.setBackground(Color.LIGHT_GRAY);
            panelContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
        return panelContent;
    }
}
