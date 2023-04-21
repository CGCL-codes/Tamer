package forms;

import interfaces.IOutputInput;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import osloader.Constants;

@SuppressWarnings("serial")
public class Console extends JFrame implements IOutputInput {

    private static String consoleName = "VM Console " + Constants.VERSION;

    private int consoleWidth = 0;

    private int consoleHeight = 0;

    private JScrollPane consoleScroll = null;

    public static JTextArea consoleTextArea = null;

    private LoginPanel loginPanel = null;

    /**
	 * buffer[0] == '1' means that buffer[1] is special key code else it is
	 * standard character.
	 */
    private final char[] buffer = new char[2];

    private boolean waitInput = false;

    private final ArrayList<String> commands = new ArrayList<String>();

    int position = 0;

    /**************************************************************************
	 * Constructor of class ShellForm. There are set variables of instance.
	 */
    public Console() {
        super(consoleName);
        consoleWidth = 800;
        consoleHeight = 600;
        createForm();
    }

    /**************************************************************************
	 * Method sets loginPanel visible.
	 */
    public void setLoginPanelVisible() {
        invalidate();
        add(loginPanel, BorderLayout.CENTER);
        loginPanel.setVisible(true);
        validate();
    }

    /**************************************************************************
	 * Method sets loginPanel invisible.
	 */
    public void setLoginPanelInvisible() {
        invalidate();
        loginPanel.setVisible(false);
        remove(loginPanel);
        validate();
    }

    /**************************************************************************
	 * Method returns variable loginPanel.
	 * 
	 * @return - variable loginPanel
	 */
    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    /**************************************************************************
	 * Method sets text area of console visible.
	 */
    public void setConsoleTextAreaVisible() {
        invalidate();
        consoleTextArea.setText("");
        add(consoleScroll, BorderLayout.CENTER);
        consoleScroll.setVisible(true);
        consoleTextArea.requestFocus();
        validate();
    }

    /**************************************************************************
	 * Method sets text area of console invisible.
	 * 
	 * @param clear
	 *            - sign of erasure of text area of console.
	 */
    public void setConsoleTextAreaInvisible() {
        invalidate();
        consoleScroll.setVisible(false);
        remove(consoleScroll);
        validate();
    }

    @Override
    public void close() {
    }

    @Override
    public String readInput() {
        waitInput = true;
        String localBuffer = "";
        consoleTextArea.getCaret().setVisible(true);
        consoleTextArea.repaint();
        while (true) {
            synchronized (consoleTextArea) {
                try {
                    consoleTextArea.setCaretPosition(consoleTextArea.getText().length());
                    consoleTextArea.wait();
                } catch (final InterruptedException e) {
                }
                if (buffer[1] == KeyEvent.VK_ENTER) {
                    commands.add(localBuffer);
                    position = commands.size() - 1;
                    printOutput(Constants.EOL);
                    localBuffer += Constants.EOL;
                    break;
                } else if ((buffer[0] == '1') && (buffer[1] == KeyEvent.VK_DOWN)) {
                    if ((commands.size() > 0) && (position < commands.size())) {
                        consoleTextArea.setText(consoleTextArea.getText().substring(0, consoleTextArea.getText().length() - localBuffer.length()));
                        localBuffer = commands.get(position);
                        consoleTextArea.append(localBuffer);
                        position++;
                    }
                    if (position > commands.size() - 1) {
                        position = commands.size() - 1;
                    }
                } else if ((buffer[0] == '1') && (buffer[1] == KeyEvent.VK_UP)) {
                    if ((commands.size() > 0) && (position < commands.size()) && (position >= 0)) {
                        consoleTextArea.setText(consoleTextArea.getText().substring(0, consoleTextArea.getText().length() - localBuffer.length()));
                        localBuffer = commands.get(position);
                        position--;
                        consoleTextArea.append(localBuffer);
                    }
                    if (position < 0) {
                        position = 0;
                    }
                } else if (buffer[1] == KeyEvent.VK_BACK_SPACE) {
                    if (!localBuffer.isEmpty()) {
                        consoleTextArea.setText(consoleTextArea.getText().substring(0, consoleTextArea.getText().length() - 1));
                        localBuffer = localBuffer.substring(0, localBuffer.length() - 1);
                    }
                } else if ((buffer[1] != KeyEvent.VK_ESCAPE) && (buffer[1] != KeyEvent.VK_DELETE)) {
                    localBuffer += buffer[1];
                    if (buffer[1] == Constants.EOF) {
                        break;
                    }
                    printOutput(Character.toString(buffer[1]));
                }
            }
        }
        waitInput = false;
        consoleTextArea.getCaret().setVisible(false);
        return localBuffer;
    }

    @Override
    public void printOutput(final String outputString) {
        synchronized (consoleTextArea) {
            consoleTextArea.append(outputString);
            consoleTextArea.setCaretPosition(consoleTextArea.getText().length());
        }
    }

    /**************************************************************************
	 * 
	 * @throws InterruptedException
	 */
    public void waitForLogin() throws InterruptedException {
        setTitle(consoleName);
        setTitle(consoleName + " - " + loginPanel.waitForLogin());
    }

    /**************************************************************************
	 * Method sets font for text area of console.
	 * 
	 * @param font
	 */
    public void setConsoleFont(final Font font) {
        consoleTextArea.setFont(font);
    }

    /**************************************************************************
	 * Method sets parametrs of console's form and append appropriate
	 * components.
	 */
    private void createForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(consoleWidth, consoleHeight);
        getContentPane().setBackground(Color.BLACK);
        setMinimumSize(new Dimension(640, 480));
        final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenDimensions.width / 2 - consoleWidth / 2, screenDimensions.height / 2 - consoleHeight / 2);
        addTextArea();
        setVisible(true);
    }

    /**************************************************************************
	 * Method adds text area into console form and sets parametrs of text area.
	 * Method creates listeners for form and for keys.
	 */
    private void addTextArea() {
        consoleTextArea = new JTextArea();
        loginPanel = new LoginPanel(consoleWidth, consoleHeight);
        consoleScroll = new JScrollPane(consoleTextArea);
        consoleTextArea.setLineWrap(true);
        consoleTextArea.setWrapStyleWord(false);
        consoleTextArea.setAutoscrolls(true);
        consoleTextArea.setBackground(new Color(0, 0, 0));
        consoleTextArea.setForeground(new Color(210, 210, 210));
        consoleTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        consoleTextArea.setEditable(false);
        consoleTextArea.setDoubleBuffered(true);
        consoleTextArea.setCaretColor(consoleTextArea.getForeground());
        consoleTextArea.getCaret().setVisible(false);
        addComponentListener(new ComponentListener() {

            @Override
            public void componentHidden(final ComponentEvent e) {
            }

            @Override
            public void componentMoved(final ComponentEvent e) {
            }

            @Override
            public void componentResized(final ComponentEvent e) {
                final Component c = (Component) e.getSource();
                final Dimension d = c.getSize();
                loginPanel.recomputeBounds(d.width, d.height);
            }

            @Override
            public void componentShown(final ComponentEvent e) {
            }
        });
        final KeyListener keyListener = new KeyListener() {

            public void keyPressed(final KeyEvent keyEvent) {
                if (waitInput) {
                    synchronized (consoleTextArea) {
                        switch(keyEvent.getKeyCode()) {
                            case KeyEvent.VK_UP:
                            case KeyEvent.VK_DOWN:
                                buffer[0] = '1';
                                buffer[1] = (char) keyEvent.getKeyCode();
                                consoleTextArea.notifyAll();
                                break;
                            default:
                        }
                        keyEvent.consume();
                    }
                }
            }

            public void keyReleased(final KeyEvent keyEvent) {
                keyEvent.consume();
            }

            public void keyTyped(final KeyEvent keyEvent) {
                if (waitInput) {
                    synchronized (consoleTextArea) {
                        buffer[0] = '0';
                        buffer[1] = keyEvent.getKeyChar();
                        consoleTextArea.notifyAll();
                    }
                }
            }
        };
        final FocusListener focusListener = new FocusListener() {

            @Override
            public void focusGained(final FocusEvent arg0) {
                consoleTextArea.getCaret().setVisible(true);
            }

            @Override
            public void focusLost(final FocusEvent arg0) {
                consoleTextArea.getCaret().setVisible(false);
            }
        };
        consoleTextArea.addKeyListener(keyListener);
        consoleTextArea.addFocusListener(focusListener);
        setConsoleTextAreaInvisible();
        setLoginPanelInvisible();
    }

    /**************************************************************************
	 * Sets console background and foreground color.
	 * 
	 * @param back
	 *            - color of background
	 * @param fore
	 *            - color of foreground
	 */
    public void setColor(final Color back, final Color fore) {
        consoleTextArea.setBackground(back);
        consoleTextArea.setForeground(fore);
    }

    /**************************************************************************
	 * Rendering method. Overrides the paint() method on the superclass to add
	 * antialiasing to the output screen.
	 */
    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paint(g);
    }

    /**
	 * Clears text area.
	 */
    public void clear() {
        synchronized (consoleTextArea) {
            consoleTextArea.setText("");
        }
    }
}
