package it.novabyte.idb1;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;

public class FrmWorkspace extends JFrame {

    private static final long serialVersionUID = 3930957403046705253L;

    private final Action actNewStatement;

    private final JTabbedPane ctlStatements;

    public FrmWorkspace() {
        super("iDB");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationByPlatform(true);
        actNewStatement = new AbstractAction("New statement") {

            private static final long serialVersionUID = 7536522436723327274L;

            @Override
            public void actionPerformed(ActionEvent e) {
                onNewStatement();
            }
        };
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(actNewStatement);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        ctlStatements = new JTabbedPane();
        setLayout(new BorderLayout());
        add(ctlStatements);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                onNewStatement();
            }
        });
    }

    protected void onNewStatement() {
        CtlStatement ctlStatement = new CtlStatement();
        ctlStatements.add(ctlStatement);
        ctlStatements.setSelectedComponent(ctlStatement);
    }
}
