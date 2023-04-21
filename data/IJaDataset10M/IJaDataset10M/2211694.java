package monkey.gui;

import monkey.loader.old.Parser;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;

public class MonkeyFrame extends JFrame implements ChangeListener, ActionListener, Observer, TreeSelectionListener {

    private JTabbedPane tabbedPane;

    private MessagePanel messagePanel;

    private GrammarPanel grammarPanel;

    private static final String COMMAND_NEW = "NEW";

    private static final String COMMAND_OPEN = "OPEN";

    private static final String COMMAND_SAVE = "SAVE";

    private static final String COMMAND_CLOSE = "CLOSE";

    private static final String COMMAND_GENERATE = "GENERATE";

    private JFileChooser fileChooser;

    private MonkeyInvoker monkeyInvoker;

    public MonkeyFrame() {
        super("Monkey");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        initGUI(getContentPane());
        initFileChoser();
        monkeyInvoker = new MonkeyInvoker(this);
        addTab(new GrammarEditor(new File("src/monkey/gui/highlighter.mgr")));
    }

    private void initFileChoser() {
        fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setFileFilter(new FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory() || file.getName().endsWith(".mgr");
            }

            public String getDescription() {
                return "Grammar File (*.mgr)";
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private void initGUI(Container contentPane) {
        contentPane.setLayout(new BorderLayout());
        JToolBar toolBar = new JToolBar();
        toolBar.add(createToolBarButton("etc/new.png", COMMAND_NEW, "New"));
        toolBar.add(createToolBarButton("etc/open.png", COMMAND_OPEN, "Open"));
        toolBar.addSeparator();
        toolBar.add(createToolBarButton("etc/save.png", COMMAND_SAVE, "Save"));
        toolBar.addSeparator();
        toolBar.add(createToolBarButton("etc/close.png", COMMAND_CLOSE, "Close"));
        toolBar.addSeparator();
        toolBar.add(createToolBarButton("etc/monkey.png", COMMAND_GENERATE, "Generate"));
        contentPane.add(toolBar, BorderLayout.NORTH);
        tabbedPane = new JTabbedPane();
        tabbedPane.addChangeListener(this);
        grammarPanel = new GrammarPanel();
        grammarPanel.addTreeSelectionListener(this);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, grammarPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.80);
        messagePanel = new MessagePanel();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPane, messagePanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.85);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    private JButton createToolBarButton(String icon, String command, String tooltip) {
        JButton button;
        button = new JButton(new ImageIcon(icon));
        button.setActionCommand(command);
        button.addActionListener(this);
        button.setToolTipText(tooltip);
        return button;
    }

    public static void main(String[] args) {
        new MonkeyFrame().show();
    }

    public void stateChanged(ChangeEvent event) {
        tabChanged();
    }

    private void tabChanged() {
        GrammarEditor grammarEditor = (GrammarEditor) tabbedPane.getSelectedComponent();
        if (tabbedPane.getTabCount() > 0 && grammarEditor != null) {
            grammarEditor.requestFocus();
            messagePanel.setText(grammarEditor.getMessage().getText());
            grammarPanel.setGrammarTree(grammarEditor.getTree().getGrammarTree());
        }
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (COMMAND_NEW.equals(command)) {
            executeCommandNew();
        } else if (COMMAND_OPEN.equals(command)) {
            executeCommandOpen();
        } else if (COMMAND_SAVE.equals(command)) {
            executeCommandSave();
        } else if (COMMAND_CLOSE.equals(command)) {
            executeCommandClose();
        } else if (COMMAND_GENERATE.equals(command)) {
            executeCommandGenerate();
        }
    }

    private void executeCommandNew() {
        addTab(new GrammarEditor());
    }

    private void addTab(GrammarEditor grammarEditor) {
        if (grammarEditor != null) {
            tabbedPane.addTab(grammarEditor.getTabName(), grammarEditor);
            tabbedPane.setSelectedComponent(grammarEditor);
            grammarEditor.addMessageObserver(this);
            grammarEditor.addTreeObserver(this);
        }
    }

    private void executeCommandOpen() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            addTab(new GrammarEditor(fileChooser.getSelectedFile()));
        }
    }

    private boolean executeCommandSave() {
        GrammarEditor grammarEditor = (GrammarEditor) tabbedPane.getSelectedComponent();
        if (grammarEditor == null) {
            return false;
        }
        if (!grammarEditor.getFile().exists()) {
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                grammarEditor.setFile(fileChooser.getSelectedFile());
                tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), grammarEditor.getTabName());
            } else {
                return false;
            }
        }
        grammarEditor.save();
        return true;
    }

    private void executeCommandClose() {
        GrammarEditor grammarEditor = (GrammarEditor) tabbedPane.getSelectedComponent();
        if (true) {
            tabbedPane.removeTabAt(tabbedPane.getSelectedIndex());
            tabChanged();
        }
    }

    private void executeCommandGenerate() {
        if (executeCommandSave()) {
            GrammarEditor grammarEditor = (GrammarEditor) tabbedPane.getSelectedComponent();
            monkeyInvoker.invoke(grammarEditor.getFilePath());
        }
    }

    public void observe(Observable observable) {
        if (observable instanceof GrammarEditor.Message) {
            GrammarEditor.Message message = (GrammarEditor.Message) observable;
            if (message == ((GrammarEditor) tabbedPane.getSelectedComponent()).getMessage()) {
                messagePanel.setText(message.getText());
            }
        } else if (observable instanceof GrammarEditor.Tree) {
            GrammarEditor.Tree tree = (GrammarEditor.Tree) observable;
            if (tree == ((GrammarEditor) tabbedPane.getSelectedComponent()).getTree()) {
                grammarPanel.setGrammarTree(tree.getGrammarTree());
            }
        }
    }

    public void valueChanged(TreeSelectionEvent event) {
        TreePath path = event.getPath();
        GrammarEditor grammarEditor = (GrammarEditor) tabbedPane.getSelectedComponent();
        GrammarTree grammarTree = (GrammarTree) path.getLastPathComponent();
        grammarEditor.setCaretPosition(grammarTree.getOffset());
        grammarEditor.requestFocus();
    }

    private static class MonkeyInvoker {

        private Frame frame;

        private JTextArea textArea;

        private PrintStream out;

        private JScrollPane scrollPane;

        public MonkeyInvoker(Frame frame) {
            this.frame = frame;
            textArea = new JTextArea();
            out = new PrintStream(new OutputStream() {

                public void write(int b) {
                    textArea.append(String.valueOf((char) b));
                }
            }, true);
            scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
        }

        public void invoke(final String file) {
            PrintStream oldOut = System.out;
            textArea.setText("");
            System.setOut(out);
            new Thread(new Runnable() {

                public void run() {
                    try {
                        Parser.main(new String[] { file });
                    } catch (Exception e) {
                        e.printStackTrace(out);
                    }
                }
            }).start();
            JOptionPane.showMessageDialog(frame, scrollPane, "Generating...", JOptionPane.PLAIN_MESSAGE, null);
            System.setOut(oldOut);
        }
    }
}
