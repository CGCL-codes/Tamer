package gruntspud.standalone;

import gruntspud.CVSCommandHandler;
import gruntspud.ColorUtil;
import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.GruntspudUtil;
import gruntspud.StringUtil;
import gruntspud.actions.AbstractClearAction;
import gruntspud.actions.AbstractCopyAction;
import gruntspud.actions.AbstractSaveAction;
import gruntspud.event.GruntspudCVSAdapter;
import gruntspud.style.TextStyle;
import gruntspud.ui.GruntspudLogo;
import gruntspud.ui.UIUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.netbeans.lib.cvsclient.command.Command;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 */
public class SimpleConsole extends JPanel implements ClipboardOwner, ActionListener {

    private static final int MAX_BUFFERED_LINES = 1000;

    private Action clearAction;

    private Action copyAction;

    private Action saveAction;

    private JTextPane textPane;

    private GruntspudLogo logo;

    private JLabel statusLabel;

    private JLabel infoLabel;

    private boolean commandRunning;

    private JProgressBar memoryGauge;

    private GruntspudContext context;

    private int maxLength;

    private long started;

    private ConsoleWriter consoleWriter;

    /**
   * Creates a new SimpleConsole object.
   * 
   * @param context
   *          DOCUMENT ME!
   */
    public SimpleConsole(GruntspudContext context) {
        super(new BorderLayout());
        consoleWriter = new ConsoleWriter();
        this.context = context;
        textPane = new JTextPane() {

            public boolean getScrollableTracksViewportWidth() {
                return false;
            }

            public void setSize(Dimension d) {
                if (d.width < getParent().getSize().width) {
                    d.width = getParent().getSize().width;
                }
                super.setSize(d);
            }
        };
        textPane.setEditable(false);
        JScrollPane textScroller = new JScrollPane(textPane) {

            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 140);
            }
        };
        JToolBar toolBar = new JToolBar("Console tools");
        toolBar.setOpaque(false);
        toolBar.setBorder(null);
        toolBar.setFloatable(false);
        boolean showSelectiveText = context.getHost().getBooleanProperty(Constants.TOOL_BAR_SHOW_SELECTIVE_TEXT, true);
        toolBar.add(UIUtil.createButton(clearAction = new ClearAction(), showSelectiveText, false));
        toolBar.add(UIUtil.createButton(copyAction = new CopyAction(), showSelectiveText, false));
        toolBar.add(UIUtil.createButton(saveAction = new SaveAction(), showSelectiveText, false));
        logo = new GruntspudLogo(context);
        statusLabel = new JLabel();
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        statusLabel.setFont(statusLabel.getFont().deriveFont(10f));
        statusLabel.setVerticalAlignment(JLabel.BOTTOM);
        infoLabel = new JLabel();
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        infoLabel.setFont(infoLabel.getFont().deriveFont(10f));
        infoLabel.setVerticalAlignment(JLabel.TOP);
        JPanel t = new JPanel(new GridLayout(2, 1, 2, 1));
        t.add(statusLabel);
        t.add(infoLabel);
        memoryGauge = new JProgressBar(0, 100) {

            public Dimension getMinimumSize() {
                return new Dimension(100, 16);
            }

            public Dimension getSize() {
                return getMinimumSize();
            }
        };
        memoryGauge.setBorder(BorderFactory.createLineBorder(Color.black));
        memoryGauge.setBackground(Color.white);
        memoryGauge.setForeground(Color.red);
        memoryGauge.setStringPainted(true);
        memoryGauge.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent evt) {
                Constants.SYSTEM_LOG.info("Scheduling garbage collection");
                System.gc();
            }
        });
        JPanel middlePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        UIUtil.jGridBagAdd(middlePanel, t, gbc, GridBagConstraints.RELATIVE);
        gbc.weighty = 0.0;
        gbc.weightx = 0.0;
        UIUtil.jGridBagAdd(middlePanel, memoryGauge, gbc, GridBagConstraints.REMAINDER);
        JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.add(toolBar, BorderLayout.WEST);
        toolPanel.add(middlePanel, BorderLayout.CENTER);
        toolPanel.add(logo, BorderLayout.EAST);
        JPanel south = new JPanel(new BorderLayout());
        south.add(new JSeparator(JSeparator.HORIZONTAL), BorderLayout.NORTH);
        south.add(toolPanel, BorderLayout.SOUTH);
        add(textScroller, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);
        CVSCommandHandler.getInstance().addGruntspudCVSListener(new SimpleConsoleCVSListener());
        init();
        javax.swing.Timer timer = new javax.swing.Timer(2000, this);
        timer.start();
    }

    /**
   * DOCUMENT ME!
   */
    public void init() {
        textPane.setBackground(ColorUtil.getColor(JDK13GruntspudHost.PROP_CONSOLE_BACKGROUND, UIManager.getColor("TextPane.background"), context));
        Font f = StringUtil.stringToFont(context.getHost().getProperty(JDK13GruntspudHost.PROP_CONSOLE_FONT, "monospaced,0,10"));
        if (f != null) {
            textPane.setFont(f);
        }
        maxLength = context.getHost().getIntegerProperty(JDK13GruntspudHost.PROP_CONSOLE_MAX_SIZE, 131072);
        checkMaxLength(0);
        memoryGauge.setVisible(context.getHost().getBooleanProperty(JDK13GruntspudHost.PROP_SHOW_MEMORY_MONITOR, false));
    }

    private void checkMaxLength(int toAdd) {
        try {
            int z = textPane.getDocument().getLength() + toAdd;
            if (z > maxLength) {
                textPane.getDocument().remove(0, z - maxLength);
            }
        } catch (Throwable ex) {
        }
    }

    /**
   * DOCUMENT ME!
   * 
   * @param evt
   *          DOCUMENT ME!
   */
    public void actionPerformed(ActionEvent evt) {
        long total = Runtime.getRuntime().totalMemory();
        long free = Runtime.getRuntime().freeMemory();
        StringBuffer buf = new StringBuffer();
        buf.append("Memory    Total=");
        buf.append(total);
        buf.append(" Free=");
        buf.append(free);
        long used = total - free;
        buf.append(" Used=");
        buf.append(used);
        buf.append(" [");
        buf.append(context.getViewManager().getCachedNodeCount());
        buf.append(" file nodes are cached");
        buf.append("] - Click to schedule gc.");
        int gauge = (int) (((double) used / (double) total) * 100d);
        memoryGauge.setValue(gauge);
        memoryGauge.setToolTipText(buf.toString());
        buf.setLength(0);
        buf.append(gauge);
        buf.append("% (");
        buf.append((int) (used / 1024d / 1024d));
        buf.append("M)");
        memoryGauge.setString(buf.toString());
    }

    /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
    public boolean isCommandRunning() {
        return commandRunning;
    }

    /**
   * DOCUMENT ME!
   * 
   * @param c
   *          DOCUMENT ME!
   * @param message
   *          DOCUMENT ME!
   * @param icon
   *          DOCUMENT ME!
   */
    public void writeToStatusLine(Color c, String message, Icon icon) {
        statusLabel.setText(message);
        statusLabel.setForeground((c == null) ? UIManager.getColor("Label.foreground") : c);
        statusLabel.setIcon((icon == null) ? UIUtil.EMPTY_SMALL_ICON : icon);
    }

    /**
   * DOCUMENT ME!
   * 
   * @param c
   *          DOCUMENT ME!
   * @param message
   *          DOCUMENT ME!
   * @param icon
   *          DOCUMENT ME!
   */
    public void writeToInfoLine(Color c, String message, Icon icon) {
        infoLabel.setText(message);
        infoLabel.setForeground((c == null) ? UIManager.getColor("Label.foreground") : c);
        infoLabel.setIcon((icon == null) ? UIUtil.EMPTY_SMALL_ICON : icon);
    }

    /**
   * DOCUMENT ME!
   */
    public void saveConsole() {
        JFileChooser chooser = new JFileChooser();
        String dir = context.getHost().getProperty(JDK13GruntspudHost.PROP_CONSOLE_LAST_SAVE_LOCATION);
        if (dir != null) {
            chooser.setCurrentDirectory(new File(dir));
        }
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".log");
            }

            public String getDescription() {
                return "Gruntspud console log files";
            }
        });
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Save console log as ...");
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            context.getHost().setProperty(JDK13GruntspudHost.PROP_CONSOLE_LAST_SAVE_LOCATION, f.getParentFile().getAbsolutePath());
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                out.write(textPane.getText().getBytes());
                out.flush();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            } finally {
                GruntspudUtil.closeStream(out);
            }
        }
    }

    /**
   * DOCUMENT ME!
   * 
   * @param clipboard
   *          DOCUMENT ME!
   * @param contents
   *          DOCUMENT ME!
   */
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    /**
   * DOCUMENT ME!
   * 
   * @param c
   *          DOCUMENT ME!
   * @param text
   *          DOCUMENT ME!
   */
    public void writeMessage(TextStyle style, final String text) {
        consoleWriter.addMessage(style, text);
    }

    /**
   * DOCUMENT ME!
   * 
   * @param commandRunning
   *          DOCUMENT ME!
   */
    public void setCommandRunning(boolean commandRunning) {
        boolean was = this.commandRunning;
        if (was != commandRunning) {
            if (commandRunning) {
                started = System.currentTimeMillis();
            } else {
                int took = (int) (System.currentTimeMillis() - started);
                int h = took / 3600000;
                took -= (h * 3600000);
                int m = took / 60000;
                took -= (m * 60000);
                int s = took / 1000;
                took -= (s * 1000);
                int ms = took;
                writeMessage(context.getTextStyleModel().getStyle(Constants.OPTIONS_STYLE_GRUNTSPUD), "Command took " + h + "h" + m + "m" + s + "s" + ms + "ms");
            }
            this.commandRunning = commandRunning;
            logo.setAnimate(commandRunning);
        }
    }

    class ClearAction extends AbstractClearAction {

        /**
     * Constructor for the DeleteAction object
     */
        ClearAction() {
            super();
        }

        public void clear() {
            textPane.setText("");
        }
    }

    class SaveAction extends AbstractSaveAction {

        public void save() {
            saveConsole();
        }
    }

    class CopyAction extends AbstractCopyAction {

        /**
     * Constructor for the CopyAction object
     */
        CopyAction() {
            super();
        }

        public void copy() {
            try {
                Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
                c.setContents(new StringSelection(textPane.getText().substring(textPane.getSelectionStart(), textPane.getSelectionEnd())), SimpleConsole.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SimpleConsoleCVSListener extends GruntspudCVSAdapter {

        public String getShortName() {
            return "SimpleConsole";
        }

        public void commandGroupStarted(Command[] command) {
            logo.setAnimate(true);
        }

        public void commandGroupFinished() {
            logo.setAnimate(false);
        }
    }

    static class WriteLock {
    }

    class ConsoleWriter extends Thread {

        private List consoleBuffer;

        ConsoleWriter() {
            super("ConsoleWriter");
            setDaemon(true);
            consoleBuffer = new ArrayList();
            start();
        }

        public void addMessage(TextStyle style, String text) {
            synchronized (consoleBuffer) {
                if (consoleBuffer.size() > MAX_BUFFERED_LINES) {
                    try {
                        consoleBuffer.wait();
                    } catch (InterruptedException e) {
                        Constants.UI_LOG.error(e);
                    }
                }
                consoleBuffer.add(new ConsoleWriterWrapper(style, text));
            }
            synchronized (this) {
                notifyAll();
            }
        }

        public void run() {
            StringBuffer writeBuffer = new StringBuffer();
            TextStyle lastStyle = null;
            SimpleAttributeSet attr = null;
            int l = 0;
            while (true) {
                synchronized (this) {
                    try {
                        wait(2000);
                    } catch (InterruptedException ie) {
                    }
                }
                while (consoleBuffer.size() > 0) {
                    ConsoleWriterWrapper w = (ConsoleWriterWrapper) consoleBuffer.get(0);
                    String txt = w.text;
                    if (!txt.endsWith("\n")) {
                        txt = txt + "\n";
                    }
                    if (lastStyle == null || lastStyle != w.style) {
                        if (writeBuffer.length() != 0) {
                            writeText(writeBuffer.toString(), attr);
                            writeBuffer.setLength(0);
                            l = 0;
                        }
                        attr = new SimpleAttributeSet();
                        if (w.style.getForeground() != null) StyleConstants.setForeground(attr, w.style.getForeground()); else StyleConstants.setForeground(attr, textPane.getForeground());
                        if (w.style.getBackground() != null) StyleConstants.setBackground(attr, w.style.getBackground()); else StyleConstants.setBackground(attr, textPane.getBackground());
                        StyleConstants.setBold(attr, w.style.isBold());
                        StyleConstants.setItalic(attr, w.style.isItalic());
                        lastStyle = w.style;
                    }
                    writeBuffer.append(txt);
                    l++;
                    if (l == 5) {
                        writeText(writeBuffer.toString(), attr);
                        writeBuffer.setLength(0);
                        l = 0;
                    }
                    synchronized (consoleBuffer) {
                        consoleBuffer.remove(0);
                        if (consoleBuffer.size() < (MAX_BUFFERED_LINES / 2)) {
                            consoleBuffer.notifyAll();
                        }
                    }
                }
                if (writeBuffer.length() > 0) {
                    writeText(writeBuffer.toString(), attr);
                    writeBuffer.setLength(0);
                    l = 0;
                }
            }
        }

        public void writeText(final String writeTxt, final AttributeSet attr) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        try {
                            checkMaxLength(0);
                            textPane.getDocument().insertString(textPane.getDocument().getLength(), writeTxt, attr);
                            textPane.scrollRectToVisible(textPane.getVisibleRect());
                            textPane.setCaretPosition(textPane.getDocument().getLength());
                        } catch (Throwable ex) {
                        }
                    }
                });
            } catch (Exception e) {
                Constants.UI_LOG.error(e);
            }
        }

        class ConsoleWriterWrapper {

            TextStyle style;

            String text;

            ConsoleWriterWrapper(TextStyle style, String text) {
                this.style = style;
                this.text = text;
            }
        }
    }
}
