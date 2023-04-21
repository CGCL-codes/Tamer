package uk.ac.essex.common.action.impl;

import uk.ac.essex.common.action.xml.ActionGroup;
import uk.ac.essex.common.action.ActionGroupDescriptor;
import uk.ac.essex.common.action.ActionDescriptor;
import uk.ac.essex.common.action.SeparatorDescriptor;
import uk.ac.essex.common.menu.MenuList;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * Created Date: 01-Nov-2003<br>
 * <p/>
 * You should have received a copy of Lesser GNU public license with this code.
 * If not please visit <a href="http://www.gnu.org/copyleft/lesser.html">this site </a>
 *
 * @author Laurence Smith
 */
public class ActionGroupDescriptorImpl implements ActionGroupDescriptor {

    ActionGroup actionGroup;

    List childList = new ArrayList();

    List swingList = new ArrayList(10);

    public ActionGroupDescriptorImpl(ActionGroup actionGroup) {
        this.actionGroup = actionGroup;
    }

    /**
     * @return
     */
    public String getLabel() {
        return actionGroup.getLabel();
    }

    public String getMenu() {
        return actionGroup.getMenu();
    }

    public String getMnemonic() {
        String mnemonic = actionGroup.getMnemonic();
        return mnemonic;
    }

    public String getName() {
        return actionGroup.getName();
    }

    public String getMenuLabel() {
        return getLabel();
    }

    public void addChildGroup(ActionGroupDescriptor childGroupDescriptor) {
        childList.add(childGroupDescriptor);
    }

    /**
     * @param toolBarId
     * @return
     */
    public ActionDescriptor[] getToolBarActions(String toolBarId) {
        return new ActionDescriptor[0];
    }

    /**
     * Do I need this??
     *
     * @return
     */
    public ActionDescriptor[] getActionsWithIcons() {
        return new ActionDescriptor[0];
    }

    /**
     * @param actionDescriptor
     */
    public void addAction(ActionDescriptor actionDescriptor) {
        swingList.add(actionDescriptor);
    }

    /**
     * @param separator
     */
    public void addSeperator(SeparatorDescriptor separator) {
        swingList.add(separator);
    }

    public boolean showIcons() {
        return false;
    }

    /**
     *
     * @return
     */
    public List getSwingList() {
        return swingList;
    }
}
