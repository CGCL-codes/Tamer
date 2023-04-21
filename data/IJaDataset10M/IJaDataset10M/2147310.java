package vpro.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import vpro.client.VProClient;
import vpro.client.exceptions.SettingsFileNotFoundException;

public class VProFrame extends JFrame {

    private VProClient client;

    Action exitAction = new AbstractAction() {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    };

    Action newAction = new AbstractAction() {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1775487499873572369L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(new JFrame(), "Are you sure you want to override current dictionary?") == JOptionPane.OK_OPTION) {
                client.reconnectDB();
            }
        }
    };

    private void reloadTree() {
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 7896510510090975529L;

    public VProFrame() throws SettingsFileNotFoundException {
        client = new VProClient();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setTitle("VPro - Develop Your Vocabulary");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(700 + 100, 500 * 297 / 210);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setJMenuBar(createJMenuBar());
        JTabbedPane tabbed = new JTabbedPane();
        tabbed.add("Google Translate Web Page", new GooglePanel());
        tabbed.add("Learning Mode", new LearningModePanel());
        tabbed.add("My Dictionary", new DictionaryPanel());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createJPanel(), tabbed);
        add(splitPane);
    }

    private JPanel createJPanel() {
        JPanel panel = new JPanel();
        class TopPanel extends JPanel {

            /**
			 * 
			 */
            private static final long serialVersionUID = -2684738966932824633L;

            TopPanel() {
                JTextField word = new JTextField(20);
                JTextArea explanation = new JTextArea(6, 30);
                JButton button = new JButton("Remember");
                setLayout(new GridBagLayout());
                GridBagConstraints c1 = new GridBagConstraints();
                c1.gridy = 0;
                c1.anchor = GridBagConstraints.LINE_START;
                add(new JLabel("word: "), c1);
                GridBagConstraints c2 = new GridBagConstraints();
                c2.gridy = 0;
                c2.anchor = GridBagConstraints.LINE_START;
                add(new JLabel("explanation: "), c2);
                GridBagConstraints c3 = new GridBagConstraints();
                c3.gridy = 1;
                c3.fill = GridBagConstraints.BOTH;
                word.setFont(new Font("Helvetica", Font.BOLD, 16));
                add(word, c3);
                GridBagConstraints c4 = new GridBagConstraints();
                c4.gridy = 1;
                c4.fill = GridBagConstraints.BOTH;
                add(explanation, c4);
                GridBagConstraints c5 = new GridBagConstraints();
                c5.gridy = 2;
                c5.gridx = 1;
                c5.insets = new Insets(10, 0, 20, 0);
                c5.anchor = GridBagConstraints.LINE_END;
                button.setPreferredSize(new Dimension(100, 25));
                add(button, c5);
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                });
            }
        }
        panel.setLayout(new BorderLayout());
        panel.add(new TopPanel(), BorderLayout.NORTH);
        return panel;
    }

    private JMenuBar createJMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        exitAction.putValue(AbstractAction.NAME, "Exit");
        newAction.putValue(AbstractAction.NAME, "New");
        file.add(newAction);
        file.add(exitAction);
        bar.add(file);
        return bar;
    }
}
