package com.bluemarsh.jswat.command.view;

import com.bluemarsh.jswat.command.CommandParser;
import com.bluemarsh.jswat.command.CommandProvider;
import com.bluemarsh.jswat.ui.actions.Actions;
import com.bluemarsh.jswat.ui.actions.ClearAction;
import com.bluemarsh.jswat.ui.actions.CopyAction;
import com.bluemarsh.jswat.ui.actions.CutAction;
import com.bluemarsh.jswat.ui.actions.PasteAction;
import com.bluemarsh.jswat.core.util.Threads;
import java.awt.EventQueue;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.concurrent.Future;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import org.openide.ErrorManager;

/**
 * Displays an input field for receiving commands and a text area for
 * showing the results of the command processing.
 *
 * @author  Nathan Fiedler
 */
public class CommandPanel extends JPanel implements Runnable {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /** The future reading from the reader and printing to the text area. */
    private transient Future readerFuture;

    /**
     * Creates new form CommandPanel.
     */
    public CommandPanel() {
        initComponents();
        DefaultCaret caret = new DefaultCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        outputTextArea.setCaret(caret);
        new TextEnhancer(outputScrollPane, outputTextArea);
        Action[] actions = new Action[] { new CutAction(), new CopyAction(), new PasteAction(), new ClearAction() };
        Actions.attachActions(actions, outputTextArea, inputTextField);
        Actions.attachShortcuts(actions, this);
        EventQueue.invokeLater(this);
    }

    /**
     * This component is being closed.
     */
    void closing() {
        readerFuture.cancel(true);
    }

    @Override
    public void run() {
        CommandParser parser = CommandProvider.getCommandParser();
        if (parser != null) {
            PipedWriter pwriter = new PipedWriter();
            PipedReader preader = new PipedReader();
            try {
                preader.connect(pwriter);
            } catch (IOException ioe) {
                ErrorManager.getDefault().notify(ioe);
                return;
            }
            PrintWriter printWriter = new PrintWriter(pwriter);
            parser.setOutput(printWriter);
            new CommandInputAdapter(inputTextField, parser, printWriter);
            OutputReader or = new OutputReader(preader);
            readerFuture = Threads.getThreadPool().submit(or);
        } else {
            ErrorManager.getDefault().log(ErrorManager.ERROR, "No CommandParser defined!");
        }
    }

    /**
     * Adds a few additional features to a plain JTextArea.
     *
     * @author  Nathan Fiedler
     */
    private static class TextEnhancer implements ComponentListener {

        /** Vertical scrollbar for the text area for auto-scrolling. */
        private JScrollBar verticalScrollBar;

        /** Horizontal scrollbar for the text area for auto-scrolling. */
        private JScrollBar horizontalScrollBar;

        /** Runnable to scroll the text area down. */
        private Runnable downScroller;

        /**
         * Creates a new instance of TextEnhancer.
         *
         * @param  scrollPane  scroll pane to manage.
         * @param  textArea    text area to watch.
         */
        public TextEnhancer(JScrollPane scrollPane, JTextArea textArea) {
            super();
            verticalScrollBar = scrollPane.getVerticalScrollBar();
            horizontalScrollBar = scrollPane.getHorizontalScrollBar();
            textArea.addComponentListener(this);
            downScroller = new Runnable() {

                @Override
                public void run() {
                    if (verticalScrollBar != null) {
                        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                    }
                    if (horizontalScrollBar != null) {
                        horizontalScrollBar.setValue(horizontalScrollBar.getMinimum());
                    }
                }
            };
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentResized(ComponentEvent e) {
            EventQueue.invokeLater(downScroller);
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }
    }

    /**
     * Reads from a Reader and writes to a text area.
     *
     * @author  Nathan Fiedler
     */
    private class OutputReader implements Runnable {

        /** Reader from whence output is read. */
        private Reader reader;

        /**
         * Creates a new instance of OutputReader.
         *
         * @param  reader  from which output is read.
         */
        public OutputReader(Reader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            char[] buf = new char[8192];
            while (true) {
                try {
                    int len = reader.read(buf);
                    if (len == -1) {
                        break;
                    } else if (len > 0) {
                        String str = new String(buf, 0, len);
                        outputTextArea.append(str);
                    }
                } catch (IOException ioe) {
                    break;
                }
            }
        }
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        outputScrollPane = new javax.swing.JScrollPane();
        outputTextArea = new javax.swing.JTextArea();
        inputLabel = new javax.swing.JLabel();
        inputTextField = new javax.swing.JTextField();
        setLayout(new java.awt.GridBagLayout());
        outputScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        outputTextArea.setEditable(false);
        outputTextArea.setFont(new java.awt.Font("Monospaced", 0, 11));
        outputScrollPane.setViewportView(outputTextArea);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        add(outputScrollPane, gridBagConstraints);
        inputLabel.setLabelFor(inputTextField);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/command/view/Form");
        inputLabel.setText(bundle.getString("LBL_Command_Input"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 12);
        add(inputLabel, gridBagConstraints);
        inputTextField.setFont(new java.awt.Font("Monospaced", 0, 11));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        add(inputTextField, gridBagConstraints);
    }

    private javax.swing.JLabel inputLabel;

    private javax.swing.JTextField inputTextField;

    private javax.swing.JScrollPane outputScrollPane;

    private javax.swing.JTextArea outputTextArea;
}
