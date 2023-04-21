package uk.ac.lkl.expresser.client;

import com.google.gwt.user.client.ui.HTML;

/**
 * @author Ken Kahn
 *
 */
public class LockedTiedNumberValuePanel extends HTML {

    private TiedNumber tiedNumber;

    public LockedTiedNumberValuePanel(TiedNumber tiedNumber) {
        super();
        this.tiedNumber = tiedNumber;
        updateDisplay();
        setStylePrimaryName("expresser-locked-tied-number-value-panel");
        addStyleName("expresser-unselectable");
    }

    public void updateDisplay() {
        String html = tiedNumber.isValueDisplayed() ? Integer.toString(tiedNumber.evaluateToInt()) : "&nbsp;";
        setHTML(html);
    }
}
