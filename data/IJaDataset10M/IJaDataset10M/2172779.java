package grammarbrowser.browser.components;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Tregex Pattern coloring pane
 * 
 * @author Bernard Bou
 * 
 */
public class TregexPatternCell extends JTextPane {

    private static final long serialVersionUID = 1L;

    /**
     * Font
     */
    private Font theFont = new Font("Dialog", Font.PLAIN, 12);

    /**
     * Operator pattern
     */
    private Pattern theOperatorPattern = Pattern.compile("([^A-Z]*)");

    /**
     * Grammatical category pattern
     */
    private Pattern theCategoryPattern = Pattern.compile("([A-Z]*)");

    /**
     * Extra operators in pretty printing
     */
    private Pattern thePrettyOperatorPattern = Pattern.compile("(\\bRoot\\b|\\band\\b|\\bor\\b)");

    /**
     * Regular expression
     */
    private Pattern theRegExpPattern = Pattern.compile("/[^/]*/");

    /**
     * 'target'
     */
    private Pattern theTargetPattern = Pattern.compile("\\btarget\\b");

    /**
     * Operator style
     */
    private SimpleAttributeSet theOperatorStyle = new SimpleAttributeSet();

    /**
     * Category style
     */
    private SimpleAttributeSet theCategoryStyle = new SimpleAttributeSet();

    /**
     * Regular expression style
     */
    private SimpleAttributeSet theRegExpStyle = new SimpleAttributeSet();

    /**
     * 'target' style
     */
    private SimpleAttributeSet theTargetStyle = new SimpleAttributeSet();

    private Pattern[] thePatterns = { theOperatorPattern, theCategoryPattern, thePrettyOperatorPattern, theRegExpPattern, theTargetPattern };

    private SimpleAttributeSet[] theStyles = { theOperatorStyle, theCategoryStyle, theOperatorStyle, theRegExpStyle, theTargetStyle };

    {
        StyleConstants.setForeground(theOperatorStyle, Color.RED);
        StyleConstants.setBold(theOperatorStyle, true);
        StyleConstants.setBold(theCategoryStyle, true);
        StyleConstants.setForeground(theRegExpStyle, Color.BLUE);
        StyleConstants.setBackground(theTargetStyle, Color.RED);
        StyleConstants.setForeground(theTargetStyle, Color.WHITE);
    }

    /**
     * Constructor
     */
    public TregexPatternCell() {
        setFont(theFont);
    }

    @Override
    public void setText(String thisString) {
        super.setText(thisString);
        for (int i = 0; i < thePatterns.length; i++) {
            Matcher thisMatcher = thePatterns[i].matcher(thisString);
            while (thisMatcher.find()) {
                int from = thisMatcher.start();
                int to = thisMatcher.end();
                getStyledDocument().setCharacterAttributes(from, to - from, theStyles[i], true);
            }
        }
    }
}
