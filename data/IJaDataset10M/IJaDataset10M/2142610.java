package portal.presentation.cal;

import java.util.Calendar;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.Element;
import hambo.util.Device;
import hambo.app.util.DOMUtil;
import hambo.app.base.ProtectedPortalPage;

public class todo extends ProtectedPortalPage {

    public todo() {
        super("caladdtodo");
    }

    public void processPage() {
        if (getContext().getDevice().isHtmlDevice()) {
            if (getContext().getDevice().getGroup() != Device.HTML_COMPACT) {
                includeMarketMessage(language);
            }
        }
        if (getContext().getDevice().getGroup() == Device.HTML_COMPACT) calmainweek.fixNavigationPALM7(navigation); else calmainweek.fixNavigation(navigation, getContext());
        DOMUtil.setFirstMatchingAttribute(getElement("subject"), "value", getParameter("subject"));
        DOMUtil.setFirstNodeText(getElement("text"), notNull(getParameter("text")).trim());
        DOMUtil.setSelectedOption((HTMLSelectElement) getElement("status"), getParameter("status"));
        writeMonthBox();
        setRefererElement();
    }

    /**
	 * Get all the calendar objects for this month and make a box for selecting
	 * a day. If a day contains somthing or not is visually indicated.
	 */
    private void writeMonthBox() {
        Element bx = getElement("monthbox");
        if (bx != null) {
            monthbox box = new monthbox(comms, Calendar.DATE, user_id);
            DOMUtil.setFirstNodeText(bx, box.toString());
        }
    }

    /**
     * Draws a virtual page containing the marketing message.
     */
    protected void includeMarketMessage(String lang) {
        if (lang.equals("English") || lang.equals("Swedish") || lang.equals("English_us")) {
            mmsg_calmainweek mm = new mmsg_calmainweek(comms);
            helpComponent = mm;
        } else {
        }
    }
}
