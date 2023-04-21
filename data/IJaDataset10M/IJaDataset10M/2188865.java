package net.i4q.sqlved.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import net.i4q.sqlved.db.DBConnection;

public class MainWindow extends JFrame implements ActionListener {

    SQLEditor sqlEditor = new SQLEditor();

    SQLResult sqlResult = new SQLResult();

    JMenuBar menuBar = new JMenuBar();

    DBConnection dbConnection = null;

    private NewConnectionDialog newConnectionDialog = new NewConnectionDialog(this, true);

    ;

    MainWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("SQLVed");
        this.setSize(800, 600);
        this.center();
        this.initMenu();
        newConnectionDialog.addActionListener(this);
    }

    private void center() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenDimension = toolkit.getScreenSize();
        int width = this.getWidth();
        int height = this.getHeight();
        int x = (int) ((screenDimension.getWidth() - width) / 2);
        int y = (int) ((screenDimension.getHeight() - height) / 2);
        this.setBounds(x, y, width, height);
    }

    public void initComponents() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(sqlEditor);
        splitPane.add(sqlResult);
        splitPane.setDividerLocation(this.getHeight() / 3);
        this.add(splitPane);
        this.setVisible(true);
    }

    public void initMenu() {
        JMenuItem newConnectionMenuItem = new JMenuItem("New Connection", KeyEvent.VK_N);
        newConnectionMenuItem.setActionCommand("newConnection");
        newConnectionMenuItem.addActionListener(this);
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(newConnectionMenuItem);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem executeMenuItem = new JMenuItem("Execute", KeyEvent.VK_X);
        executeMenuItem.setActionCommand("execute");
        executeMenuItem.addActionListener(this);
        JMenu actionMenu = new JMenu("Action");
        actionMenu.add(executeMenuItem);
        actionMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(fileMenu);
        menuBar.add(actionMenu);
        this.add(menuBar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionCommand = ae.getActionCommand();
        System.out.println("Action Command: " + actionCommand);
        if (actionCommand.equals("newConnection")) {
            newConnectionDialog.setVisible(true);
        } else if (actionCommand.equals("execute")) {
            execute();
        } else if (actionCommand.equals("cancelNewConnectionDialog")) {
            newConnectionDialog.setVisible(false);
        } else if (actionCommand.equals("connect")) {
            Properties props = newConnectionDialog.getParameters();
            try {
                sqlResult.init(props);
                newConnectionDialog.setVisible(false);
                initComponents();
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Library containing the JDBC Driver class (com.mysql.jdbc.Driver) is missing", "Missing Library", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "You have specified an invalid parameter. Please try again", "Invalid Parameters", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void execute() {
        sqlResult.execute(sqlEditor.getText());
    }
}
