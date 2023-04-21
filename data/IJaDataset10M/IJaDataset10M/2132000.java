package nz.org.venice.analyser;

import javax.swing.JComponent;

/**
 * An analysis tool configuration page. Each analysis tool consists of a series of 
 * configuration pages. Each page is represented by a tab pane. The pages group various 
 * parameters, such as buy/sell rules, portfolio information, trade costs, etc. together. 
 * By separating these pages into different classes, they can be shared between tools.
 * 
 * @author Andrew Leppard
 * @see PaperTradeModule
 * @see GPModule
 */
public interface AnalyserPage {

    /**
     * Save the user interface values to preferences.
     *
     * @param key a key which uniquely identifies the analysis tool using this page
     */
    public void save(String key);

    /**
     * Load the user interface values from preferences.
     *
     * @param key a key which uniquely identifies the analysis tool using this page
     */
    public void load(String key);

    /**
     * Parse the values the user has entered into the GUI. If any of the
     * values are invalid, the page will display an option pane describing
     * the error, and the function will return <code>false</code>.
     *
     * @return <code>true</code> if the user's values are valid, <code>false</code> otherwise.
     */
    public boolean parse();

    /**
     * Return the page's graphical user interface.
     *
     * @return the GUI
     */
    public JComponent getComponent();

    /**
     * Return the title of this page.
     *
     * @return the page's title
     */
    public String getTitle();
}
