package org.webguitoolkit.ui.controls.util.style.selector;

/**
 * creates styles in format
 * <code>
 * style="width: 100%; color: red;"
 * </code>
 *  
 * @author Benjamin Klug
 *
 */
public class TagSelector extends AStyleSelector implements IStyleSelector {

    /**
	 * Private initialization. define behavoiur of selector here
	 */
    {
        super.setSelector("style=");
        super.setBrackets(false);
        super.setLineBreaks(false);
        super.setQuotes(true);
    }

    public String getOutput() {
        return this.getSelector();
    }
}
