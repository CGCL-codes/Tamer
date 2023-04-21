package hu.ihash.common.gui;

import hu.ihash.common.parser.IParser;
import hu.ihash.common.parser.ParseException;
import hu.ihash.common.parser.ParserFactory;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * A grammar-based text field.
 *
 * @author Gergely Kiss
 *
 */
public class JGrammarField extends JTextArea {

    private static final Color correctBackground = new Color(250, 250, 250);

    private static final Color failedBackground = new Color(255, 128, 128);

    /** The timer for parsing the text. */
    protected final Timer parseTimeout;

    /** The parser class to use for parsing the text. */
    protected Class<? extends IParser<?>> parserType;

    /** The time to wait in msecs after a keystroke for parsing the text. */
    protected int parseDelay = 500;

    /** True, if the current text is a valid expression. */
    private boolean correct = true;

    public JGrammarField() {
        setCorrect(true);
        parseTimeout = new Timer(0, new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        tryParse(getText());
                    }
                });
            }
        });
        parseTimeout.setRepeats(false);
        parseTimeout.setInitialDelay(parseDelay);
        parseTimeout.setDelay(parseDelay);
        parseTimeout.start();
        addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                parseTimeout.restart();
            }
        });
    }

    protected void tryParse(String text) {
        if (parserType == null) return;
        IParser<?> parser = ParserFactory.createParser(parserType, text);
        try {
            parser.parse();
            setCorrect(true);
        } catch (ParseException e) {
            setCorrect(false);
            setToolTipText(e.getLocalizedMessage());
        }
    }

    private void setCorrect(boolean correct) {
        this.correct = correct;
        if (correct) {
            setBackground(correctBackground);
        } else {
            setBackground(failedBackground);
        }
    }

    public Class<? extends IParser<?>> getParserType() {
        return parserType;
    }

    public void setParserType(Class<? extends IParser<?>> parserType) {
        if ((this.parserType != null) && !this.parserType.equals(parserType)) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    tryParse(getText());
                }
            });
        }
        this.parserType = parserType;
    }

    public int getParseDelay() {
        return parseDelay;
    }

    public void setParseDelay(int parseDelay) {
        this.parseDelay = parseDelay;
    }

    public boolean isCorrect() {
        return correct;
    }
}
