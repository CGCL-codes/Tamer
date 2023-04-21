package br.unb.unbiquitous.ubiquitos.driver.pc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author      Lucas Almeida <lucas.augusto.almeida@gmail.com>
 * @version     1.0
 * @since       2011.0718
 */
public class KeyboardDriverGUI implements KeyListener, ActionListener {

    private KeyboardDriver keyboardDriver;

    int guiWidth;

    int guiHeight;

    JFrame mainFrame;

    JPanel mainPanel;

    JLabel numberOfListenersLabel;

    JTextArea textArea;

    /**
	 * Sets up the screen to get keyboard events.
	 *
	 * @param keyboardDriver Reference to the keyboard driver.
	 * @since 2011.0718
	 */
    public KeyboardDriverGUI(KeyboardDriver keyboardDriver) {
        this.keyboardDriver = keyboardDriver;
        mainFrame = new JFrame("PC Keyboard Driver");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        guiWidth = screenSize.height - 200;
        guiHeight = screenSize.height - 200;
        mainFrame.setSize(guiWidth, guiHeight);
        mainFrame.setLocation(screenSize.width / 2 - guiWidth / 2, screenSize.height / 2 - guiHeight / 2);
        mainPanel = setUpGUI();
        mainFrame.setContentPane(mainPanel);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
	 * Fills a panel with all visual componentes
	 *
	 * @return The panel with all visual components.
	 * @since 2011.0718
	 */
    private JPanel setUpGUI() {
        JPanel panel = new JPanel();
        textArea = new JTextArea();
        textArea.addKeyListener(this);
        textArea.setFocusTraversalKeysEnabled(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(guiWidth - 50, guiHeight - 200));
        panel.add(scrollPane);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        numberOfListenersLabel = new JLabel("Listening to this: 0");
        numberOfListenersLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        numberOfListenersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(numberOfListenersLabel);
        JButton clearButton = new JButton("Clear Text");
        clearButton.setActionCommand("clearTextArea");
        clearButton.addActionListener(this);
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(clearButton);
        JLabel warningLabel = new JLabel("<html>All keys typed on the text area will send messages,<br>" + "even if it doesn't show here.<br> " + "Also, remember that you're mapping the target keyboard,<br>" + "so some keys may have changed places or may not exist.</html>");
        warningLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        warningLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(warningLabel);
        panel.add(infoPanel);
        return panel;
    }

    /**
	 * Changes the number of listeners that is displayed on the screen.
	 *
	 * @param numberOfListeners New value to be displayed on the screen;
	 * @since 2011.0718
	 */
    public void changeNumberOfListeners(int numberOfListeners) {
        numberOfListenersLabel.setText("Listening to this: " + numberOfListeners);
    }

    /**
	 * Clears the text area.
	 *
	 * @param event Action event performed by a button on this class.
	 * @since 2011.0709
	 */
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("clearTextArea")) {
            textArea.setText("");
        }
    }

    /**
	 * Sorts and notifies the listeners about each key event.
	 *
	 * First it sorts the key into one of the following cathegories:
	 * - Letter (a..z);
	 * - Number (0..9);
	 * - Function keys (F1..F12);
	 * - Arrows (Up, down, left and right);
	 * - Text Control (Page up, Page down, Home and End);
	 * - Remaining keys, which can be:
	 *         - characters;
	 *         - commands.
	 *
	 * Then, sends the event with the correct message.
	 *
	 * @param event The key that generated the event.
	 * @since 2011.0719
	 */
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) {
            keyboardDriver.notifyKeyboardEvent("character", String.valueOf((char) (keyCode + 32)));
        } else if (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) {
            keyboardDriver.notifyKeyboardEvent("character", String.valueOf((char) keyCode));
        } else if (keyCode >= KeyEvent.VK_NUMPAD0 && keyCode <= KeyEvent.VK_DIVIDE) {
            switch(keyCode) {
                case KeyEvent.VK_NUMPAD0:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad0");
                    break;
                case KeyEvent.VK_NUMPAD1:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad1");
                    break;
                case KeyEvent.VK_NUMPAD2:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad2");
                    break;
                case KeyEvent.VK_NUMPAD3:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad3");
                    break;
                case KeyEvent.VK_NUMPAD4:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad4");
                    break;
                case KeyEvent.VK_NUMPAD5:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad5");
                    break;
                case KeyEvent.VK_NUMPAD6:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad6");
                    break;
                case KeyEvent.VK_NUMPAD7:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad7");
                    break;
                case KeyEvent.VK_NUMPAD8:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad8");
                    break;
                case KeyEvent.VK_NUMPAD9:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad9");
                    break;
                case KeyEvent.VK_MULTIPLY:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad*");
                    break;
                case KeyEvent.VK_ADD:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad+");
                    break;
                case KeyEvent.VK_SEPARATOR:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad,");
                    break;
                case KeyEvent.VK_SUBTRACT:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad-");
                    break;
                case KeyEvent.VK_DECIMAL:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad.");
                    break;
                case KeyEvent.VK_DIVIDE:
                    keyboardDriver.notifyKeyboardEvent("command", "NumPad/");
                    break;
            }
        } else if (keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12) {
            switch(keyCode) {
                case KeyEvent.VK_F1:
                    keyboardDriver.notifyKeyboardEvent("command", "F1");
                    break;
                case KeyEvent.VK_F2:
                    keyboardDriver.notifyKeyboardEvent("command", "F2");
                    break;
                case KeyEvent.VK_F3:
                    keyboardDriver.notifyKeyboardEvent("command", "F3");
                    break;
                case KeyEvent.VK_F4:
                    keyboardDriver.notifyKeyboardEvent("command", "F4");
                    break;
                case KeyEvent.VK_F5:
                    keyboardDriver.notifyKeyboardEvent("command", "F5");
                    break;
                case KeyEvent.VK_F6:
                    keyboardDriver.notifyKeyboardEvent("command", "F6");
                    break;
                case KeyEvent.VK_F7:
                    keyboardDriver.notifyKeyboardEvent("command", "F7");
                    break;
                case KeyEvent.VK_F8:
                    keyboardDriver.notifyKeyboardEvent("command", "F8");
                    break;
                case KeyEvent.VK_F9:
                    keyboardDriver.notifyKeyboardEvent("command", "F9");
                    break;
                case KeyEvent.VK_F10:
                    keyboardDriver.notifyKeyboardEvent("command", "F10");
                    break;
                case KeyEvent.VK_F11:
                    keyboardDriver.notifyKeyboardEvent("command", "F11");
                    break;
                case KeyEvent.VK_F12:
                    keyboardDriver.notifyKeyboardEvent("command", "F12");
                    break;
            }
        } else if (keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_DOWN) {
            switch(keyCode) {
                case KeyEvent.VK_LEFT:
                    keyboardDriver.notifyKeyboardEvent("command", "Left");
                    break;
                case KeyEvent.VK_UP:
                    keyboardDriver.notifyKeyboardEvent("command", "Up");
                    break;
                case KeyEvent.VK_RIGHT:
                    keyboardDriver.notifyKeyboardEvent("command", "Right");
                    break;
                case KeyEvent.VK_DOWN:
                    keyboardDriver.notifyKeyboardEvent("command", "Down");
                    break;
            }
        } else if (keyCode >= KeyEvent.VK_PAGE_UP && keyCode <= KeyEvent.VK_HOME) {
            switch(keyCode) {
                case KeyEvent.VK_PAGE_UP:
                    keyboardDriver.notifyKeyboardEvent("command", "Page_Up");
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    keyboardDriver.notifyKeyboardEvent("command", "Page_Down");
                    break;
                case KeyEvent.VK_END:
                    keyboardDriver.notifyKeyboardEvent("command", "End");
                    break;
                case KeyEvent.VK_HOME:
                    keyboardDriver.notifyKeyboardEvent("command", "Home");
                    break;
            }
        } else {
            switch(keyCode) {
                case KeyEvent.VK_SPACE:
                    keyboardDriver.notifyKeyboardEvent("character", " ");
                    break;
                case KeyEvent.VK_COMMA:
                    keyboardDriver.notifyKeyboardEvent("character", ",");
                    break;
                case KeyEvent.VK_MINUS:
                    keyboardDriver.notifyKeyboardEvent("character", "-");
                    break;
                case KeyEvent.VK_PERIOD:
                    keyboardDriver.notifyKeyboardEvent("character", ".");
                    break;
                case KeyEvent.VK_SLASH:
                    keyboardDriver.notifyKeyboardEvent("character", "/");
                    break;
                case KeyEvent.VK_SEMICOLON:
                    keyboardDriver.notifyKeyboardEvent("character", ";");
                    break;
                case KeyEvent.VK_EQUALS:
                    keyboardDriver.notifyKeyboardEvent("character", "=");
                    break;
                case KeyEvent.VK_OPEN_BRACKET:
                    keyboardDriver.notifyKeyboardEvent("character", "[");
                    break;
                case KeyEvent.VK_BACK_SLASH:
                    keyboardDriver.notifyKeyboardEvent("character", "\\");
                    break;
                case KeyEvent.VK_CLOSE_BRACKET:
                    keyboardDriver.notifyKeyboardEvent("character", "]");
                    break;
                case KeyEvent.VK_DEAD_GRAVE:
                    keyboardDriver.notifyKeyboardEvent("character", "`");
                    break;
                case KeyEvent.VK_DEAD_ACUTE:
                    keyboardDriver.notifyKeyboardEvent("character", "'");
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    keyboardDriver.notifyKeyboardEvent("command", "Backspace");
                    break;
                case KeyEvent.VK_TAB:
                    keyboardDriver.notifyKeyboardEvent("command", "Tab");
                    break;
                case KeyEvent.VK_ENTER:
                    keyboardDriver.notifyKeyboardEvent("command", "Enter");
                    break;
                case KeyEvent.VK_SHIFT:
                    keyboardDriver.notifyKeyboardEvent("command", "Shift_pressed");
                    break;
                case KeyEvent.VK_CONTROL:
                    keyboardDriver.notifyKeyboardEvent("command", "Control_pressed");
                    break;
                case KeyEvent.VK_ALT:
                    keyboardDriver.notifyKeyboardEvent("command", "Alt_pressed");
                    break;
                case KeyEvent.VK_CAPS_LOCK:
                    keyboardDriver.notifyKeyboardEvent("command", "Caps_Lock");
                    break;
                case KeyEvent.VK_ESCAPE:
                    keyboardDriver.notifyKeyboardEvent("command", "Escape");
                    break;
                case KeyEvent.VK_DELETE:
                    keyboardDriver.notifyKeyboardEvent("command", "Delete");
                    break;
                case KeyEvent.VK_NUM_LOCK:
                    keyboardDriver.notifyKeyboardEvent("command", "Num_Lock");
                    break;
                case KeyEvent.VK_PRINTSCREEN:
                    keyboardDriver.notifyKeyboardEvent("command", "Print_Screen");
                    break;
                case KeyEvent.VK_INSERT:
                    keyboardDriver.notifyKeyboardEvent("command", "Insert");
                    break;
                case KeyEvent.VK_F13:
                    keyboardDriver.notifyKeyboardEvent("command", "F13");
                    break;
                case KeyEvent.VK_F14:
                    keyboardDriver.notifyKeyboardEvent("command", "F14");
                    break;
                case KeyEvent.VK_F15:
                    keyboardDriver.notifyKeyboardEvent("command", "F15");
                    break;
                case KeyEvent.VK_F16:
                    keyboardDriver.notifyKeyboardEvent("command", "F16");
                    break;
                case KeyEvent.VK_F17:
                    keyboardDriver.notifyKeyboardEvent("command", "F17");
                    break;
                case KeyEvent.VK_F18:
                    keyboardDriver.notifyKeyboardEvent("command", "F18");
                    break;
                case KeyEvent.VK_F19:
                    keyboardDriver.notifyKeyboardEvent("command", "F19");
                    break;
                case KeyEvent.VK_F20:
                    keyboardDriver.notifyKeyboardEvent("command", "F20");
                    break;
                case KeyEvent.VK_F21:
                    keyboardDriver.notifyKeyboardEvent("command", "F21");
                    break;
                case KeyEvent.VK_F22:
                    keyboardDriver.notifyKeyboardEvent("command", "F22");
                    break;
                case KeyEvent.VK_F23:
                    keyboardDriver.notifyKeyboardEvent("command", "F23");
                    break;
                case KeyEvent.VK_F24:
                    keyboardDriver.notifyKeyboardEvent("command", "F24");
                    break;
            }
        }
    }

    /**
	 * Only sends message if the key is 'Shift', 'Control' or 'Alt'.
	 *
	 * @param event The key that generated the event.
	 * @since 2011.0719
	 */
    public void keyReleased(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch(keyCode) {
            case KeyEvent.VK_SHIFT:
                keyboardDriver.notifyKeyboardEvent("command", "Shift_released");
                break;
            case KeyEvent.VK_CONTROL:
                keyboardDriver.notifyKeyboardEvent("command", "Control_released");
                break;
            case KeyEvent.VK_ALT:
                keyboardDriver.notifyKeyboardEvent("command", "Alt_released");
                break;
        }
    }

    public void keyTyped(KeyEvent arg0) {
    }
}
