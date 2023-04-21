package ru.curs.showcase.app.client;

import ru.curs.showcase.app.client.api.BasicElementPanel;

/**
 * @author anlug
 * 
 */
public class UIDataPanelElement {

    UIDataPanelElement(final BasicElementPanel aobj) {
        this.setElementPanel(aobj);
    }

    /**
	 * 
	 */
    private BasicElementPanel elementPanel;

    /**
	 * @return the obj
	 */
    public BasicElementPanel getElementPanel() {
        return elementPanel;
    }

    /**
	 * @param aelementPanel
	 *            the obj to set
	 */
    public final void setElementPanel(final BasicElementPanel aelementPanel) {
        this.elementPanel = aelementPanel;
    }
}
