package com.sri.emo.wizard.creation.model;

import java.io.*;
import com.jcorporate.expresso.core.controller.*;
import com.jcorporate.expresso.core.db.*;
import com.sri.emo.dbobj.*;
import org.apache.commons.lang.builder.*;
import org.apache.log4j.*;

/**
 * Metadata for each completion part.
 *
 * @author Michael Rimov
 * @version 1.0
 */
public class CreationPartsBean implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    /**
     * Field completion type. (wizard or fixed)
     */
    private FieldCompletion fieldCompletion;

    /**
     * Is this a free text entry.
     */
    private boolean freeTextEntry;

    /**
     * Minimum number of entries.
     */
    private Integer minEntries;

    /**
     * Maximum number of entries.
     */
    private Integer maxEntries;

    /**
     * The attached part.
     */
    private final Part attachedPart;

    /**
     * The owning completion bean.
     */
    private final CreationBean owner;

    /**
     * The step directive.
     */
    private String directive;

    /**
     * The step helptext.
     */
    private String helpText;

    /**
     * Is the step single value entry?
     */
    private boolean singleEntry;

    /**
     * added by rich
     * Allow for searching of existing nodes
     */
    private boolean search;

    private String searchText;

    /**
     * added by rich
     * Allow for creation of a brand new node
     */
    private boolean create;

    private String createText;

    /**
     * added by rich
     * Allow for browsing of existing nodes
     */
    private boolean browse;

    private String browseText;

    /**
     * added by rich
     * Allow the user to skip this choice
     */
    private boolean required;

    /**
     * Constructor that takes the parent bean and an associated <tt>Part</tt>
     * object.
     *
     * @param myOwner CompletionBean the parent bean.
     * @param myPart  Part the associated part.
     * @throws DBException upon Part query error.
     */
    public CreationPartsBean(final CreationBean myOwner, final Part myPart) throws DBException {
        attachedPart = myPart;
        owner = myOwner;
        freeTextEntry = !(myPart.hasPicklist() || myPart.isSharedNodeAttrib() || myPart.isHaveCustomHandler());
        if (!myPart.isSingleValued()) {
            minEntries = new Integer(1);
            maxEntries = new Integer(1);
            this.setSingleEntry(false);
        } else {
            minEntries = new Integer(1);
            maxEntries = new Integer(1);
            this.setSingleEntry(true);
        }
        setDefaultDirective(myPart);
        setDefaultCreateText(myPart);
        setDefaultBrowseText(myPart);
        setDefaultSearchText(myPart);
    }

    /**
     * Sets a default directive for the given part.  The text is varied
     * depending on the metadata of the part.
     *
     * @param myPart Part the part to extract the computed directive for.
     * @throws DBException upon error querying the Part.
     */
    private void setDefaultDirective(final Part myPart) throws DBException {
        String partLabel = myPart.getPartLabel();
        if (myPart.hasPicklist()) {
            if (myPart.isSingleValued()) {
                setDirective("Please select a value for <b>" + partLabel + "</b>");
            } else {
                setDirective("Please choose values for <b>" + partLabel + "</b>");
            }
        } else {
            if (myPart.isSingleValued()) {
                setDirective("Please enter a value for <b>" + partLabel + "</b>");
            } else {
                setDirective("Please set values for <b>" + partLabel + "</b>");
            }
        }
    }

    /**
     * Sets a default create text for the given part.  The text is varied
     * depending on the metadata of the part.
     *
     * @param myPart Part the part to extract the computed create text for.
     * @throws DBException upon error querying the Part.
     */
    private void setDefaultCreateText(final Part myPart) throws DBException {
        String partLabel = myPart.getPartLabel();
        setCreateText("<b>Create</b> " + partLabel + ":");
    }

    private void setDefaultBrowseText(final Part myPart) throws DBException {
        String partLabel = myPart.getPartLabel();
        setBrowseText("<b>Browse</b> for existing " + partLabel + ":");
    }

    private void setDefaultSearchText(final Part myPart) throws DBException {
        String partLabel = myPart.getPartLabel();
        setSearchText("<b>Find</b> existing " + partLabel + ":<p>The Search Engine can search for keywords found in several attributes within " + partLabel + ".</p>");
    }

    public CreationBean getOwner() {
        return owner;
    }

    public Part getPart() {
        return attachedPart;
    }

    public void setFieldCompletion(FieldCompletion fieldCompletion) {
        this.fieldCompletion = fieldCompletion;
    }

    public void setDirective(String directive) {
        this.directive = directive;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public void setSingleEntry(boolean singleEntry) {
        this.singleEntry = singleEntry;
    }

    public void setBrowse(boolean browse) {
        this.browse = browse;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

    public void setBrowseText(String browseText) {
        this.browseText = browseText;
    }

    public void setCreateText(String createText) {
        this.createText = createText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setMinEntries(int minEntries) {
        this.minEntries = new Integer(minEntries);
    }

    public void setMaxEntries(int maxEntries) {
        this.maxEntries = new Integer(maxEntries);
    }

    public FieldCompletion getFieldCompletion() {
        return fieldCompletion;
    }

    public boolean isFreeTextEntry() {
        return freeTextEntry;
    }

    public Integer getMinEntries() {
        return minEntries;
    }

    public Integer getMaxEntries() {
        return maxEntries;
    }

    public String getDirective() {
        return directive;
    }

    public String getHelpText() {
        return helpText;
    }

    public boolean isSingleEntry() {
        return singleEntry;
    }

    public boolean isBrowse() {
        return browse;
    }

    public boolean isCreate() {
        return create;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isSearch() {
        return search;
    }

    public String getBrowseText() {
        return browseText;
    }

    public String getCreateText() {
        return createText;
    }

    public String getSearchText() {
        return searchText;
    }

    public boolean isFreeTextAllowed() throws DBException {
        return !(this.getPart().hasPicklist() || getPart().isSharedNodeAttrib() || getPart().isHaveCustomHandler());
    }

    public boolean isMinMaxAllowed() throws DBException {
        return !this.getPart().isSingleValued();
    }

    /**
     * Compares this object with the specified object for order.  This implementation
     * relies on the underlying parts for comparison.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this
     *         object is less than, equal to, or greater than the specified object.
     */
    public int compareTo(Object o) {
        assert o instanceof CreationPartsBean;
        int myOrderNumber = 0;
        int otherOrderNumber = 0;
        try {
            CreationPartsBean other = (CreationPartsBean) o;
            if (attachedPart == null && other.attachedPart != null) {
                return -1;
            } else if (attachedPart != null && other.attachedPart == null) {
                return 1;
            }
            if ((attachedPart.getPartNum() == null || attachedPart.getPartNum().length() == 0) && !(other.attachedPart.getPartNum() == null || other.attachedPart.getPartNum().length() == 0)) {
                return -1;
            } else if (!(attachedPart.getPartNum() == null || attachedPart.getPartNum().length() == 0) && (other.attachedPart.getPartNum() == null || other.attachedPart.getPartNum().length() == 0)) {
                return 1;
            }
            myOrderNumber = attachedPart.getPartNumInt();
            otherOrderNumber = ((CreationPartsBean) o).attachedPart.getPartNumInt();
            if (myOrderNumber == otherOrderNumber) {
                return 0;
            } else if (myOrderNumber > otherOrderNumber) {
                return 1;
            } else {
                return -1;
            }
        } catch (DBException ex) {
            throw new RuntimeException("error querying part order for comparison");
        }
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        try {
            return "Creation bean for Part ID: " + attachedPart.getPartNum();
        } catch (DBException ex) {
            return "Creation bean for Part ID (Cannot get id due to exception)";
        }
    }

    /**
     * Validate the properties that have been set for this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.
     *
     * @param toPopulate The ErrorCollection we are using to populate with errors
     *                   upon validation errors.
     * @return ActionErrors
     * @todo This is not a side-effect free function.  Redesign so that 'fixing'
     * logic is centralized in the DBObject Converter.
     */
    public ErrorCollection validateAndAdjust(final ErrorCollection toPopulate) {
        if (this.getFieldCompletion() == FieldCompletion.NOT_INCLUDED) {
            return toPopulate;
        }
        try {
            if (this.getPart() == null) {
                toPopulate.addError("There is no attached part for this attribute: " + this.toString());
            }
            if (this.isMinMaxAllowed()) {
                if (minEntries != null) {
                    if (minEntries.intValue() < 0) {
                        toPopulate.addError("Min Entries Value for '" + this.getPart().getPartLabel() + "' must be greater than zero");
                    }
                }
                if (maxEntries != null) {
                    if (maxEntries.intValue() < 0) {
                        toPopulate.addError("Max Entries Value for '" + this.getPart().getPartLabel() + "' must be greater than zero");
                    }
                }
                if (maxEntries.intValue() < minEntries.intValue()) {
                    toPopulate.addError("Max Entries for: " + this.getPart().getPartLabel() + " must be greater than Min Entries.");
                }
            } else if (minEntries != null && minEntries.intValue() > 1) {
                toPopulate.addError("Min Entries for: " + this.getPart().getPartLabel() + " cannot be greater than '1'.  Automatically adjusting value.  Please verify and save the updated wizard.");
                minEntries = new Integer(1);
            } else if (maxEntries != null && maxEntries.intValue() > 1) {
                toPopulate.addError("Max Entries for: " + this.getPart().getPartLabel() + " cannot be greater than '1'.  Automatically adjusting value.   Please verify and save the updated wizard.");
                maxEntries = new Integer(1);
            }
            if (!this.isFreeTextAllowed() && this.isFreeTextEntry()) {
                toPopulate.addError("Free Text Entry is set, but free text entry is not permitted by node type definition." + " This has been corrected. Please Resubmit");
                this.freeTextEntry = false;
            }
        } catch (DBException ex) {
            Logger.getLogger(CreationPartsBean.class).error("Error validating part", ex);
            throw new RuntimeException("Error grabbing part label for: " + getPart().toString() + ". (Additional information logged)");
        }
        return toPopulate;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof CreationPartsBean)) {
            return false;
        }
        CreationPartsBean other = (CreationPartsBean) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder().append(attachedPart, other.attachedPart).append(directive, other.directive).append(fieldCompletion, other.fieldCompletion).append(freeTextEntry, other.freeTextEntry).append(helpText, other.helpText).append(maxEntries, other.maxEntries).append(minEntries, other.minEntries).append(singleEntry, other.singleEntry);
        return equalsBuilder.isEquals();
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        try {
            HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(481, 31).append(attachedPart.getId()).append(directive).append(fieldCompletion).append(freeTextEntry).append(helpText).append(maxEntries).append(minEntries).append(singleEntry);
            return hashCodeBuilder.toHashCode();
        } catch (DBException ex) {
            return 0;
        }
    }
}
