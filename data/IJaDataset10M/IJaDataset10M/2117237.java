package org.hip.vifapp.fixtures;

import java.util.List;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

/**
 *
 * @author Luthiger
 * Created 17.02.2009 
 */
public class VifOpenDiscussionAction extends VifAdminFixture {

    private static final String XPATH = "//form[@name='%s']/table/tbody/tr";

    private static final String FIELD_LBL = "State";

    private static final String COMMAND_NAME = "cmdOpen";

    public VifOpenDiscussionAction(String inPort) {
        super(inPort);
    }

    @Override
    protected String getRequestType() {
        return null;
    }

    @SuppressWarnings("unchecked")
    public String groupState() throws Exception {
        List<HtmlTableRow> lRows = (List<HtmlTableRow>) getSuccess().getByXPath(String.format(XPATH, FORM_NAME));
        for (HtmlTableRow lRow : lRows) {
            if (FIELD_LBL.equals(lRow.getCell(0).asText())) {
                return lRow.getCell(1).asText();
            }
        }
        return "";
    }

    public String groupTransition() throws Exception {
        HtmlForm lForm = getSuccess().getFormByName(FORM_NAME);
        return lForm.getInputByName(COMMAND_NAME).asText();
    }

    public boolean makeGroupOpen() throws Exception {
        HtmlForm lForm = getSuccess().getFormByName(FORM_NAME);
        lForm.getInputByName(COMMAND_NAME).click();
        return true;
    }

    public String getGroupId(String inGroupName) throws Exception {
        return getGroupId(inGroupName, 1, getTableRows(String.format(XPATH_TR, "odd"), String.format(XPATH_TR, "even")));
    }
}
