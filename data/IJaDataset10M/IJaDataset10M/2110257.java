package svc.ui;

import java.util.StringTokenizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import svc.core.Constants;
import svc.ui.event.TishListener;

/**
 * Stands for Tiny Interactive SHell. This shell allows users to type in
 * commands to execute them and outputs the possible results.
 * 
 * @author Allen Charlton
 */
public class Tish {

    public static void main(String[] args) {
        Tish shell = new Tish();
        shell.setTitle("Tish");
        shell.setPrompt("$");
        shell.setTishListener(new TishListener() {

            public void commandTyped(String command) {
                System.out.println(command);
            }
        });
        shell.open();
    }

    private Display display;

    private Shell shell;

    private StyledText inputArea;

    /**
	 * The font used.
	 */
    private Font promptFont;

    private String prompt;

    private int commandStart;

    private boolean typing = true;

    private TishListener tishListener;

    public Tish() {
        prompt = ">";
        initContents();
    }

    private void initContents() {
        display = new Display();
        shell = new Shell(display, SWT.TITLE | SWT.MIN | SWT.MAX);
        display = shell.getDisplay();
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        shell.setLayout(layout);
        inputArea = new StyledText(shell, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
        GridData layoutData = new GridData(GridData.FILL_BOTH);
        inputArea.setLayoutData(layoutData);
        inputArea.setDoubleClickEnabled(false);
        setBackground(display.getSystemColor(SWT.COLOR_BLACK));
        setForeground(display.getSystemColor(SWT.COLOR_GREEN));
        promptFont = new Font(display, new FontData("Courier New", 10, SWT.NORMAL));
        setFont(promptFont);
        attachListeners();
        adjustCaret();
    }

    private void attachListeners() {
        inputArea.addVerifyListener(new VerifyListener() {

            public void verifyText(VerifyEvent e) {
                if (inputArea.getCaretOffset() < commandStart) {
                    e.doit = false;
                    display.beep();
                    return;
                } else if (e.start < commandStart) {
                    e.doit = false;
                    display.beep();
                    return;
                }
                String input = e.text;
                if (typing) {
                    if (input.equals(Constants.LINE_SEPARATOR)) {
                        if (tishListener != null) {
                            try {
                                tishListener.commandTyped(inputArea.getText(commandStart, inputArea.getCharCount() - 1));
                            } catch (IllegalArgumentException iae) {
                                tishListener.commandTyped("");
                            }
                        }
                        prompt();
                        e.doit = false;
                        return;
                    }
                    if (input.indexOf(Constants.LINE_SEPARATOR) != -1) {
                        StringTokenizer tokenizer = new StringTokenizer(input, "\n");
                        while (tokenizer.hasMoreTokens()) {
                            if (tishListener != null) {
                                String text = tokenizer.nextToken();
                                text = text.replaceAll("\\\r", "");
                                if (text.length() > 0) {
                                    append(text + Constants.LINE_SEPARATOR);
                                    if (tishListener != null) {
                                        tishListener.commandTyped(text);
                                    }
                                    prompt();
                                    e.doit = false;
                                }
                            }
                        }
                    }
                }
            }
        });
        inputArea.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                adjustCaret();
            }

            public void mouseDown(MouseEvent e) {
                adjustCaret();
            }

            public void mouseUp(MouseEvent e) {
                adjustCaret();
            }
        });
        inputArea.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                adjustCaret();
            }

            public void keyReleased(KeyEvent e) {
                adjustCaret();
            }
        });
    }

    /**
	 * Displays a prompt symbol in front of a new line and prompts the user to
	 * type in.
	 * 
	 */
    private void prompt() {
        int total = inputArea.getCharCount();
        if (total != 0 && inputArea.getLineAtOffset(total) - inputArea.getLineAtOffset(total - 1) == 0) {
            append(Constants.LINE_SEPARATOR);
        }
        append(prompt);
        inputArea.setCaretOffset(inputArea.getCharCount());
        adjustCaret();
        commandStart = inputArea.getCharCount();
    }

    public void dispose() {
        display.asyncExec(new Runnable() {

            public void run() {
                shell.setVisible(false);
            }
        });
    }

    /**
	 * Appends text to the end of this shell. This appending
	 * will not interfere the processing of the internal listener.
	 * @param text the text to append
	 */
    public void append(String text) {
        typing = false;
        inputArea.append(text);
        typing = true;
    }

    public void adjustCaret() {
        Caret caret = inputArea.getCaret();
        caret.setSize(6, caret.getSize().y);
    }

    public void open() {
        shell.open();
        prompt();
        Display display = shell.getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        promptFont.dispose();
        if (!shell.isDisposed()) {
            shell.dispose();
        }
        display.dispose();
    }

    public void setTitle(String title) {
        shell.setText(title);
    }

    /**
	 * Sets the prompt message of this shell.
	 * 
	 * @param prompt
	 *            the message
	 */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setFont(Font font) {
        inputArea.setFont(font);
    }

    public void setForeground(Color color) {
        inputArea.setForeground(color);
    }

    public void setBackground(Color color) {
        inputArea.setBackground(color);
    }

    public void setTishListener(TishListener l) {
        this.tishListener = l;
    }

    public void setSize(int width, int height) {
        shell.setSize(width, height);
    }

    public void setSize(Point p) {
        shell.setSize(p);
    }
}
