package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.forms.AbstractFormFragment;
import com.volantis.mcs.protocols.forms.Link;
import com.volantis.mcs.runtime.URLConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the state associated with a FormFragment in a particular
 * MarinerPageContext.
 *
 * @mock.generate base="FormatInstance"
 */
public class FormFragmentInstance extends FormatInstance implements AbstractFormFragment {

    /**
     * Property to store the text to use for the link to the fragment
     */
    private String previousLinkText = null;

    /**
     * Property to store the text to use for the link from the fragment
     */
    private String nextLinkText = null;

    /** 
     * Style class property 
     */
    private String previousLinkStyleClass = null;

    private String nextLinkStyleClass = null;

    /**
     * Whether fragment has a reset button
     */
    private String resetFlag = null;

    /** 
     * Whether fragment links appear above or below the form
     */
    private String prevLinkBefore = null;

    private String nextLinkBefore = null;

    /**
     * Create a new <code>FormFragmentInstance</code>.
     */
    public FormFragmentInstance(NDimensionalIndex index) {
        super(index);
    }

    /**
     * Retrieve the link to text.
     * @return the text
     */
    public String getNextLinkText() {
        return nextLinkText;
    }

    /**
     * Set the value of the link to text
     */
    public void setNextLinkText(String txt) {
        nextLinkText = txt;
    }

    /**
     * Retrieve the link from text.
     */
    public String getPreviousLinkText() {
        return previousLinkText;
    }

    /**
     * Set the value of the link from text
     */
    public void setPreviousLinkText(String txt) {
        previousLinkText = txt;
    }

    /**
     * Set the name of the style class
     */
    public void setNextLinkStyleClass(String styleClass) {
        nextLinkStyleClass = styleClass;
    }

    /**
     * Set the name of the style class
     */
    public void setPreviousLinkStyleClass(String styleClass) {
        previousLinkStyleClass = styleClass;
    }

    /**
     * Get the value of the style class
     */
    public String getPreviousLinkStyleClass() {
        return previousLinkStyleClass;
    }

    /**
     * Get the value of the style class
     */
    public String getNextLinkStyleClass() {
        return nextLinkStyleClass;
    }

    /**
     * Get whether or not the link is rendered before the form contents
     */
    public String isPreviousLinkBefore() {
        return prevLinkBefore;
    }

    /**
     * Set whether or not the link is rendered before the form contents
     */
    public void setPreviousLinkBefore(String before) {
        prevLinkBefore = before;
    }

    /**
     * Get whether or not the link is rendered before the form contents
     */
    public String isNextLinkBefore() {
        return nextLinkBefore;
    }

    /**
     * Set whether or not the link is rendered before the form contents
     */
    public void setNextLinkBefore(String before) {
        nextLinkBefore = before;
    }

    /**
     * Get whether or not the fragment has a reset button
     */
    public String hasReset() {
        return resetFlag;
    }

    /**
     * Set whether or not the fragment has a reset button
     */
    public void setReset(String flag) {
        resetFlag = flag;
    }

    /**
     * This is called after the format and the context have been set and allows
     * the sub class to do any initialisation which depends on those values.
     */
    public void initialise() {
        FormFragment fragment = (FormFragment) format;
        previousLinkText = fragment.getPreviousLinkText();
        nextLinkText = fragment.getNextLinkText();
        previousLinkStyleClass = fragment.getPreviousLinkStyleClass();
        nextLinkStyleClass = fragment.getNextLinkStyleClass();
        prevLinkBefore = fragment.isPreviousLinkBefore() ? "true" : "false";
        nextLinkBefore = fragment.isNextLinkBefore() ? "true" : "false";
        resetFlag = fragment.isResetEnabled() ? "true" : "false";
        super.initialise();
    }

    protected boolean isEmptyImpl() {
        FormFragment currentFragment = context.getCurrentFormFragment();
        FormFragment fragment = (FormFragment) format;
        Format child = fragment.getChildAt(0);
        if (child != null) {
            if (currentFragment == null || fragment == currentFragment) {
                return context.isFormatEmpty(child);
            } else {
                return false;
            }
        }
        return true;
    }

    public List getBeforeFragmentLinks(AbstractFormFragment previous, AbstractFormFragment next) {
        List beforeLinks = new ArrayList();
        getLink(previous, true, true, beforeLinks);
        getLink(next, false, true, beforeLinks);
        return beforeLinks;
    }

    public List getAfterFragmentLinks(AbstractFormFragment previous, AbstractFormFragment next) {
        List afterLinks = new ArrayList();
        getLink(previous, true, false, afterLinks);
        getLink(next, false, false, afterLinks);
        if (((FormFragment) this.getFormat()).isResetEnabled()) {
            afterLinks.add(new Link(RESET_TEXT, URLConstants.RESET_FORM_FRAGMENT));
        }
        return afterLinks;
    }

    /**
     * Gets link.
     * 
     * @param fragment form fragment wrapper
     * @param prev determines if previous form fragment
     * @param before determines if we get before frgament links
     * @param linkList list of links
     */
    private void getLink(AbstractFormFragment fragment, boolean prev, boolean before, List linkList) {
        if (fragment == null) {
            return;
        }
        FormFragmentInstance instance = (FormFragmentInstance) fragment;
        FormFragment formFragment = (FormFragment) instance.getFormat();
        boolean isLink = prev ? formFragment.isPreviousLinkBefore() : formFragment.isNextLinkBefore();
        if ((before && isLink) || (!before && !isLink)) {
            addLink(instance, formFragment, prev, linkList);
        }
    }

    /**
     * Helper method that adds links to the list.
     * 
     * @param fragmentInstance fragment instance 
     * @param formFragment form fragment
     * @param prev determines if previous form fragment
     * @param linkList list of links
     */
    private void addLink(FormFragmentInstance fragmentInstance, FormFragment formFragment, boolean prev, List linkList) {
        String linkName = prev ? URLConstants.PREV_FORM_FRAGMENT : URLConstants.NEXT_FORM_FRAGMENT;
        String linkText = prev ? fragmentInstance.getPreviousLinkText() : fragmentInstance.getNextLinkText();
        if (linkText == null) {
            linkText = prev ? formFragment.getPreviousLinkText() : formFragment.getNextLinkText();
        }
        Link link = new Link(linkText, linkName);
        link.setFormFragment(fragmentInstance);
        linkList.add(link);
    }

    public String getLabel() {
        return previousLinkText;
    }

    public String getName() {
        return format.getName();
    }
}
