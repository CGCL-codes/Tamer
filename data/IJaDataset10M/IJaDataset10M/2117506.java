package net.sourceforge.ajaxtags.tags;

import javax.servlet.jsp.JspException;
import net.sourceforge.ajaxtags.helpers.DIVElement;
import static org.apache.commons.lang.StringUtils.trimToNull;

/**
 * Tag handler for the toggle (on/off, true/false) AJAX tag.
 * 
 * @author Darren Spurgeon
 * @author Jens Kapitza
 * @version $Revision: 86 $ $Date: 2007/06/20 20:55:56 $ $Author: jenskapitza $
 */
public class AjaxToggleTag extends BaseAjaxTag {

    private static final long serialVersionUID = 6877730352175914711L;

    private static final String AVOID_URL_START = "<a href=\"" + AJAX_VOID_URL + "\" title=\"";

    private static final String AVOID_URL_END = "\"></a>";

    private String ratings;

    private String defaultRating;

    private String state;

    private boolean onOff;

    private String containerClass;

    private String messageClass;

    private String selectedClass;

    private String selectedOverClass;

    private String selectedLessClass;

    private String overClass;

    private String updateFunction;

    public String getUpdateFunction() {
        return updateFunction;
    }

    public void setUpdateFunction(final String updateFunction) {
        this.updateFunction = updateFunction;
    }

    public String getContainerClass() {
        return containerClass;
    }

    public void setContainerClass(final String containerClass) {
        this.containerClass = containerClass;
    }

    public String getDefaultRating() {
        return defaultRating;
    }

    public void setDefaultRating(final String defaultRating) {
        this.defaultRating = trimToNull(defaultRating);
    }

    public String getMessageClass() {
        return messageClass;
    }

    public void setMessageClass(final String messageClass) {
        this.messageClass = messageClass;
    }

    public String getOnOff() {
        return Boolean.toString(onOff);
    }

    public void setOnOff(final String onOff) {
        this.onOff = Boolean.parseBoolean(onOff);
    }

    public String getOverClass() {
        return overClass;
    }

    public void setOverClass(final String overClass) {
        this.overClass = overClass;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(final String ratings) {
        this.ratings = trimToNull(ratings);
    }

    public String getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(final String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public String getSelectedLessClass() {
        return selectedLessClass;
    }

    public void setSelectedLessClass(final String selectedLessClass) {
        this.selectedLessClass = selectedLessClass;
    }

    public String getSelectedOverClass() {
        return selectedOverClass;
    }

    public void setSelectedOverClass(final String selectedOverClass) {
        this.selectedOverClass = selectedOverClass;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    @Override
    protected String getJsClass() {
        return JSCLASS_BASE + "Toggle";
    }

    @Override
    protected OptionsBuilder getOptions() {
        final OptionsBuilder options = getOptionsBuilder();
        options.add("ratings", this.ratings, true);
        options.add("containerClass", this.containerClass, true);
        options.add("selectedClass", this.selectedClass, true);
        options.add("selectedOverClass", this.selectedOverClass, true);
        options.add("selectedLessClass", this.selectedLessClass, true);
        options.add("overClass", this.overClass, true);
        options.add("messageClass", this.messageClass, true);
        options.add("state", this.state, true);
        options.add("defaultRating", this.defaultRating, true);
        options.add("updateFunction", this.updateFunction, false);
        return options;
    }

    @Override
    public int doEndTag() throws JspException {
        final DIVElement div = new DIVElement(getSource());
        div.setClassName(getRatingDivClass(onOff, getContainerClass()));
        final String[] ratingValues = getRatingValues();
        if (onOff) {
            div.append(AVOID_URL_START);
            if (ratingValues.length > 0) {
                final String val = ratingValues[0];
                if (defaultRating != null && defaultRating.equalsIgnoreCase(val)) {
                    div.append(val).append("\" class=\"").append(selectedClass);
                } else {
                    div.append(ratingValues[1]);
                }
            }
            div.append(AVOID_URL_END);
        } else {
            for (String val : ratingValues) {
                div.append(AVOID_URL_START).append(val);
                if (val.equalsIgnoreCase(defaultRating)) {
                    div.append("\" class=\"").append(selectedClass);
                }
                div.append(AVOID_URL_END);
            }
        }
        div.append(buildScript());
        out(div);
        return EVAL_PAGE;
    }

    private String[] getRatingValues() {
        return ratings == null ? new String[0] : ratings.split(",");
    }

    private String getRatingDivClass(final boolean onOff, final String containerClass) {
        return onOff ? containerClass + " onoff" : containerClass;
    }

    @Override
    public void releaseTag() {
        this.ratings = null;
        this.defaultRating = null;
        this.state = null;
        this.onOff = false;
        this.containerClass = null;
        this.messageClass = null;
        this.selectedClass = null;
        this.selectedOverClass = null;
        this.selectedLessClass = null;
        this.overClass = null;
    }
}
