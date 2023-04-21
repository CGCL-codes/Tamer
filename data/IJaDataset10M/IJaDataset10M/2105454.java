package wyq.tool.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.JTextComponent;
import wyq.cmd.CmdProcessor;
import wyq.cmd.CmdProcessorFactory;
import wyq.tool.util.Logger;

public class MainFrame {

    private JFrame frame;

    private JTextField cmdLineTextField;

    private final Action enterAction = new SwingAction();

    private JTextPane consoleTextPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    MainFrame window = new MainFrame();
                    JTextComponent console = window.getConsoleTextPane();
                    JTextComponent input = window.getCmdLineTextField();
                    PrintStreamWrapper wrapper = new PrintStreamWrapper(console);
                    System.setErr(wrapper);
                    System.setOut(wrapper);
                    System.setIn(new InputStreamWrapper(input));
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainFrame() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent arg0) {
                PluginLoader.load();
            }
        });
        frame.setBounds(100, 100, 630, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Console", null, panel_1, null);
        GridBagLayout gbl_panel_1 = new GridBagLayout();
        gbl_panel_1.columnWidths = new int[] { 364, 70, 0 };
        gbl_panel_1.rowHeights = new int[] { 221, 32, 0 };
        gbl_panel_1.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        gbl_panel_1.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        panel_1.setLayout(gbl_panel_1);
        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.gridwidth = 2;
        gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        panel_1.add(scrollPane, gbc_scrollPane);
        consoleTextPane = new JTextPane();
        consoleTextPane.setEditable(false);
        scrollPane.setViewportView(consoleTextPane);
        cmdLineTextField = new JTextField();
        cmdLineTextField.setAction(enterAction);
        GridBagConstraints gbc_cmdLineTextField = new GridBagConstraints();
        gbc_cmdLineTextField.insets = new Insets(0, 0, 0, 5);
        gbc_cmdLineTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_cmdLineTextField.gridx = 0;
        gbc_cmdLineTextField.gridy = 1;
        panel_1.add(cmdLineTextField, gbc_cmdLineTextField);
        cmdLineTextField.setColumns(10);
        JButton btnNewButton = new JButton("Enter");
        btnNewButton.setAction(enterAction);
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.gridx = 1;
        gbc_btnNewButton.gridy = 1;
        panel_1.add(btnNewButton, gbc_btnNewButton);
    }

    private class SwingAction extends AbstractAction {

        /**
	 * 
	 */
        private static final long serialVersionUID = 3630840312348520825L;

        public SwingAction() {
            putValue(NAME, "Enter");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent e) {
            CmdProcessor processor = CmdProcessorFactory.getCmdProcessor();
            Logger.log("Using CmdProcessor:" + processor.getClass());
            Logger.log(processor.getCmdProcessorHelp());
            String cmd = getCmdLineTextField().getText();
            processor.process(cmd);
            getCmdLineTextField().setText("");
        }
    }

    protected JTextPane getConsoleTextPane() {
        return consoleTextPane;
    }

    protected JTextField getCmdLineTextField() {
        return cmdLineTextField;
    }
}
