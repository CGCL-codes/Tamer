package workday.module.common.console;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import workday.common.factory.product.i_common_Logger;
import workday.module.i_module_Console;

/**
 *
 */
public final class ConsoleArea extends JTextArea {

    private static final long serialVersionUID = 1L;

    private String INVITATION_MESSAGE = "Workday console welcomes you.";

    private String INVITATION = "->";

    private i_common_Logger logger;

    private i_module_Console console;

    private Document document;

    private enum LINE_STATUS {

        EMPTY_LINE, FILLED_LINE_CARET_IN_THE_END, FILLED_LINE_CARET_AT_THE_END
    }

    private enum CARET_STATUS {

        BESIDE_INVITATION, FAR_INVITATION
    }

    private boolean lock_moveLeft = true;

    private boolean lock_waitAnswer = false;

    private volatile ArrayList<String> lastAnswer = new ArrayList<String>();

    private Integer ID;

    private boolean history_update = false;

    private int history_index = 0;

    private ArrayList<String> commandHistory = new ArrayList<String>();

    private String restoredCommand;

    enum COMMAND_DIRECTION {

        UP, DOWN
    }

    public ConsoleArea(String _Invitation, String _InvitationMessage, i_common_Logger _logger, i_module_Console _console) {
        super();
        logger = _logger;
        console = _console;
        document = this.getDocument();
        this.setFont(new java.awt.Font("Tahoma", 0, 12));
        this.setBackground(Color.BLACK);
        this.setForeground(Color.WHITE);
        this.setCaretColor(Color.WHITE);
        this.setSize(350, 200);
        setInvitation(_Invitation);
        setInvitationMessage(_InvitationMessage);
        this.append(INVITATION_MESSAGE);
        this.append("");
        this.append("");
        this.append(INVITATION);
        MouseListener[] mouseListeners = getMouseListeners();
        for (int i = 0; i < mouseListeners.length; i++) {
            removeMouseListener(mouseListeners[i]);
        }
        MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
        for (int i = 0; i < mouseMotionListeners.length; i++) {
            removeMouseMotionListener(mouseMotionListeners[i]);
        }
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
            }
        });
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        showLastCommand(COMMAND_DIRECTION.UP);
                        e.setKeyCode(0);
                        break;
                    case KeyEvent.VK_DOWN:
                        showLastCommand(COMMAND_DIRECTION.DOWN);
                        e.setKeyCode(0);
                        break;
                    case KeyEvent.VK_LEFT:
                        analyzeCaret();
                        actionWhenMoveLeft(e);
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        analyzeCaret();
                        actionWhenMoveLeft(e);
                        break;
                    case KeyEvent.VK_ENTER:
                        actionWhenEnterPressed(analyzeLine(), e);
                        e.setKeyCode(0);
                        break;
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        analyzeCaret();
                        actionWhenMoveLeft(e);
                        break;
                    case KeyEvent.VK_BACK_SPACE:
                        analyzeCaret();
                        actionWhenMoveLeft(e);
                        break;
                }
            }
        });
    }

    private LINE_STATUS analyzeLine() {
        try {
            int lastLineEndOffset = getLineEndOffset(getLineCount() - 1);
            int lastLineStartOffset = getLineStartOffset(getLineCount() - 1);
            if (lastLineStartOffset + INVITATION.length() == lastLineEndOffset) {
                return LINE_STATUS.EMPTY_LINE;
            } else if (getCaretPosition() == lastLineEndOffset) {
                return LINE_STATUS.FILLED_LINE_CARET_IN_THE_END;
            } else {
                return LINE_STATUS.FILLED_LINE_CARET_AT_THE_END;
            }
        } catch (BadLocationException exception) {
            logger.error(exception.getLocalizedMessage(), exception);
        }
        return LINE_STATUS.FILLED_LINE_CARET_AT_THE_END;
    }

    private CARET_STATUS analyzeCaret() {
        try {
            int lastLineStartOffset = getLineStartOffset(getLineCount() - 1);
            if (getCaretPosition() - lastLineStartOffset <= INVITATION.length()) {
                lock_moveLeft = true;
                return CARET_STATUS.BESIDE_INVITATION;
            } else {
                lock_moveLeft = false;
                return CARET_STATUS.FAR_INVITATION;
            }
        } catch (BadLocationException exception) {
            logger.error(exception.getLocalizedMessage(), exception);
        }
        return CARET_STATUS.BESIDE_INVITATION;
    }

    private void actionWhenMoveLeft(KeyEvent keyEvent) {
        if (lock_waitAnswer) {
            keyEvent.setKeyCode(0);
            return;
        }
        if (lock_moveLeft) {
            keyEvent.setKeyCode(0);
        }
    }

    private void actionWhenEnterPressed(LINE_STATUS line_status, KeyEvent keyEvent) {
        if (lock_waitAnswer) {
            keyEvent.setKeyCode(0);
            return;
        }
        switch(line_status) {
            case EMPTY_LINE:
                append("");
                append(INVITATION);
                break;
            case FILLED_LINE_CARET_AT_THE_END:
                readCommand();
                break;
            case FILLED_LINE_CARET_IN_THE_END:
                readCommand();
                break;
        }
    }

    private void showLastCommand(COMMAND_DIRECTION last_command) {
        restoredCommand = restoreLastCommand(last_command);
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    int currentLineEndOffset = getLineEndOffset(getLineCount() - 1);
                    int currentLineStartOffset = getLineStartOffset(getLineCount() - 1);
                    document.remove(currentLineStartOffset, currentLineEndOffset - currentLineStartOffset);
                    document.insertString(currentLineStartOffset, INVITATION, null);
                    append(restoredCommand);
                } catch (BadLocationException exception) {
                    logger.error(exception.getLocalizedMessage(), exception);
                }
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    private String restoreLastCommand(COMMAND_DIRECTION last_command) {
        if (history_index < commandHistory.size() && history_index > -1) {
            if (history_update) {
                history_update = false;
                restoredCommand = commandHistory.get(history_index);
            } else {
                restoredCommand = commandHistory.get(history_index);
            }
            switch(last_command) {
                case UP:
                    if (history_index > 0) {
                        history_index--;
                    }
                    break;
                case DOWN:
                    if (history_index < commandHistory.size() - 1) {
                        history_index++;
                    }
                    break;
            }
        }
        logger.info(history_index);
        return restoredCommand;
    }

    @Override
    public void append(String string) {
        if (string == null) {
            return;
        }
        try {
            if (string.isEmpty()) {
                setCaretPosition(getLineEndOffset(getLineCount() - 1));
                super.append("\n");
            } else {
                super.append(string);
                setCaretPosition(getLineEndOffset(getLineCount() - 1));
            }
        } catch (BadLocationException exception) {
            logger.error(exception.getLocalizedMessage(), exception);
        }
    }

    public void appendAnswer(String _string) {
        if (SwingUtilities.isEventDispatchThread()) {
            append(_string);
            append("");
        } else {
            lastAnswer.add(_string);
            Runnable runnable = new Runnable() {

                public void run() {
                    append(lastAnswer.size() > 0 ? lastAnswer.remove(0) : "answer stack is empty.");
                    append("");
                }
            };
            SwingUtilities.invokeLater(runnable);
        }
    }

    public void finishAnswer() {
        if (SwingUtilities.isEventDispatchThread()) {
            setEditable(true);
            lock_waitAnswer = false;
            append(INVITATION);
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    setEditable(true);
                    lock_waitAnswer = false;
                    append(INVITATION);
                }
            });
        }
    }

    public void readCommand() {
        final ArrayList<String> returnCommand = new ArrayList<String>();
        try {
            int commandEndOffset = getLineEndOffset(getLineCount() - 1);
            int commandStartOffset = getLineStartOffset(getLineCount() - 1) + INVITATION.length();
            String commandLine = this.getDocument().getText(commandStartOffset, commandEndOffset - commandStartOffset);
            commandHistory.add(commandLine);
            String parameter[] = commandLine.split(" ");
            returnCommand.addAll(Arrays.asList(parameter));
            history_update = true;
            history_index = commandHistory.size() - 1;
        } catch (BadLocationException exception) {
            logger.error("getCommand(): bad location.", exception);
        }
        super.setEditable(false);
        lock_waitAnswer = true;
        logger.info("waiting answer...");
        append("");
        console.sendCommand(returnCommand, ID);
    }

    public void setID(Integer id) {
        if (id != null) {
            ID = id;
        }
    }

    /**
     * Возврат текущего приглашения командной строки
     * @return INVITATION - текущее приглашение
     */
    public String getInvitation() {
        return INVITATION;
    }

    /**
     * Установка приглашения командной строки
     * @param INVITATION - the INVITATION to set
     */
    public void setInvitation(String _INVITATION) {
        if (_INVITATION == null || _INVITATION.isEmpty()) {
            logger.warn("Invitation string is null or empty. Default used.");
            return;
        }
        this.INVITATION = _INVITATION + ">";
    }

    /**
     * Возврат текущего приветственного сообщения командной строки
     * @return INVITATION_MESSAGE - текущее приветствие
     */
    public String getInvitationMessage() {
        return INVITATION_MESSAGE;
    }

    /**
     * Установка приветственного сообщения командной строки
     * @param INVITATION_MESSAGE - the INVITATION_MESSAGE to set
     */
    public void setInvitationMessage(String _INVITATION_MESSAGE) {
        if (_INVITATION_MESSAGE == null || _INVITATION_MESSAGE.isEmpty()) {
            logger.warn("Invitation message string is null or empty. Default used.");
            return;
        }
        this.INVITATION_MESSAGE = _INVITATION_MESSAGE;
    }

    public void close() {
    }
}
