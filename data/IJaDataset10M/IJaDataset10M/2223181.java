package net.sf.juife.swing;

import javax.swing.JPanel;

/**
 * This class represents a page that can be displayed in <code>NavigationPane</code>.
 * @author Grigor Iliev
 */
public class NavigationPage extends JPanel {

    private String title;

    /** Creates a new instance of <code>NavigationPage</code> with empty title */
    public NavigationPage() {
        this("");
    }

    /**
	 * Creates a new instance of <code>NavigationPage</code> with the specified title.
	 * @param title	The title text.
	 */
    public NavigationPage(String title) {
        this.title = title;
    }

    /**
	 * Gets the title of this navigation page.
	 * @return The title of this navigation page.
	 */
    public String getTitle() {
        return title;
    }

    /**
	 * Sets the title of this navigation page.
	 * @param title Specifies the title text of this navigation page.
	 */
    public void setTitle(String title) {
        this.title = title;
    }
}
