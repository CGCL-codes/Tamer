package org.jxpfw.jsp.tag.form;

import java.util.Collection;
import java.util.List;
import org.apache.log4j.Logger;
import org.josef.util.CDebug;
import org.josef.util.CReflection;
import org.josef.util.CodeDescription;
import org.josef.web.html.HtmlUtil;
import org.jxpfw.util.CReflectionException;

/**
 * Custom JSP tag that corresponds to an HTML select element.
 * Usage:<pre>
 * &lt;jxpfw:select collection="expression" [size="number"] [multiple=boolean]
 * [id="id"] [html="html code"]/&gt;</pre>
 * The collection should contain elements of type {@link CodeDescription}.<br>
 * For non mandatory select items an HTML option will be added. This is
 * necessary since when a user has made a choice she can't undo it. The value
 * for this added option is take from {@link #OPTIONAL_VALUE} and the
 * description is fetched from the resource bundle of the JSP bean. The key used
 * is formed by appending {@link #KEY_OPTIONAL} to the property name. When no
 * description is found the standard key {@link #KEY_OPTIONAL_SELECT_OPTION}
 * will be tried. If this is still without result than an empty description will
 * be added.<br>
 * Supply a size of 1 (one) to create a combo box. Supply a value larger than
 * 1 (one) to create a list.<br>
 * Supplying multiple="true" allows for selection of multiple items in the list.
*  <br>In the absence of an id attribute an id equal to the name of the property
 * will be generated.<br>
 * Note: This tag can be used as an indexed property. When "multiple=false" it
 * works out of the box but when "multiple=true" is used things get complicated.
 * You have to know that a setter: set...(String []) is called where the
 * parameter consist of one big array containing all the selected values of all
 * properties. The getter expects to find an array of selected values per
 * property. In order to map the big array to the array per property you need
 * unique option values for <b>all</b> items that can be selected.<br>
 * Note: This tag must be nested within a {@link PropertyTag}.<br>
 * @author Kees Schotanus
 * @version 1.0 $Revision: 1.43 $
 * @see PropertyTag
 */
public class SelectTag extends AbstractInputTag {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 5373187419095634605L;

    /**
     * Value that is appended to a property name to locate the corresponding
     * HTML option for non mandatory items.
     * <br>When your select property is called city than the key used to
     * retrieve the option would be cityOptional.
     */
    public static final String KEY_OPTIONAL = "Optional";

    /**
     * Key to the select option that should be added to non mandatory select
     * items.
     */
    public static final String KEY_OPTIONAL_SELECT_OPTION = "optionalSelectOption";

    /**
     * Value of the optional item.
     */
    public static final String OPTIONAL_VALUE = "-";

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger(SelectTag.class);

    /**
     * Collection containing the data that should be displayed in the select
     * element.
     */
    private Collection<? extends CodeDescription> collection;

    /**
     * Determines how many rows of data are displayed in the select element.
     * <br>Contains null when no size has been supplied.
     */
    private Integer size;

    /**
     * Determines whether the select item allows for selection of multiple
     * items (true) or not (false).
     * <br>Contains false when no size has been supplied.
     */
    private Boolean multiple = Boolean.FALSE;

    /**
     * Gets the collection that contains the data that needs to be displayed.
     * @return Collection containing data that needs to be displayed.
     */
    public Collection<? extends CodeDescription> getCollection() {
        return collection;
    }

    /**
     * Sets the collection containing the data to be displayed.
     * <br>Each element in the collection must be of type
     * {@link CodeDescription}.
     * @param collection Collection containing the data to be displayed.
     * @throws NullPointerException When the supplied collection is null.
     */
    public void setCollection(final Collection<? extends CodeDescription> collection) {
        CDebug.checkParameterNotNull(collection, "collection");
        this.collection = collection;
    }

    /**
     * Gets the size of the select element.
     * @return The size of the select element or null when no size has been
     *  supplied.
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the size for the select element.
     * @param size Number of rows the select element should show.
     *  <br>Effectively a size of more than one displays the select component as
     *  a list and a size of 1 shows this select element as a combobox.<br>
     *  A size < 1 is silently converted to 1 without warning.<br>
     *  When null is supplied, the browser will determine the size of the
     *  select element.
     */
    public void setSize(final Integer size) {
        this.size = size == null ? null : (size.intValue() < 1 ? Integer.valueOf(1) : size);
    }

    /**
     * Determines whether multiple items from the list can be selected (true)
     * or not (false).
     * @return True when the user can select more than one item from the list,
     *  false otherwise.
     */
    public Boolean isMultiple() {
        return multiple;
    }

    /**
     * Sets the select element to accept only one item or multiple items
     * simultaneously.
     * @param multiple Supply true to allow for selection of multiple items,
     *  supply null or false when the user is only allowed to select one item.
     */
    public void setMultiple(final Boolean multiple) {
        this.multiple = multiple == null ? Boolean.FALSE : multiple;
    }

    /**
     * Creates the HTML code to add an HTML select tag to a JSP page.
     * @return HTML code that needs to be added to a JSP page in order to add a
     *  select tag.
     * @throws CReflectionException When the value of a property could not be
     *  obtained.
     */
    @Override
    protected String getOutput() throws CReflectionException {
        final StringBuffer output = new StringBuffer(255);
        output.append("<select");
        addNameAttribute(output);
        addIdAttribute(output);
        addClassAttribute("select", null, output);
        if (Boolean.TRUE.equals(getPropertyTag().isReadonly())) {
            output.append(" disabled");
        }
        if (size != null) {
            output.append(" size=\"");
            output.append(size.intValue());
            output.append('\"');
        }
        if (Boolean.TRUE.equals(multiple)) {
            output.append(" multiple");
        }
        addHtml(output);
        output.append(">\n");
        addOptions(output);
        output.append("\t\t</select>");
        return output.toString();
    }

    /**
     * Adds the collection to the supplied buffer in the form of HTML option
     * elements.
     * @param buffer StringBuffer to which the HTML option elements will be
     *  added.
     * @throws CReflectionException When the value of the corresponding property
     *  could not be obtained.
     * @throws NullPointerException When the supplied buffer is null.
     */
    protected void addOptions(final StringBuffer buffer) throws CReflectionException {
        CDebug.checkParameterNotNull(buffer, "buffer");
        String[] propertyValues;
        try {
            Object returnedObject;
            final Integer propertyIndex = getPropertyTag().getIndexNumber();
            if (propertyIndex == null) {
                returnedObject = CReflection.invokeGetMethod(getBeanTag().getJSPBean(), getPropertyTag().getName());
            } else {
                returnedObject = CReflection.invokeGetMethod(getBeanTag().getJSPBean(), getPropertyTag().getName(), propertyIndex.intValue());
            }
            if (Boolean.TRUE.equals(multiple)) {
                propertyValues = (String[]) returnedObject;
            } else {
                propertyValues = new String[] { returnedObject == null ? null : returnedObject.toString() };
            }
        } catch (final Exception exception) {
            LOGGER.fatal(exception.getMessage(), exception);
            throw new CReflectionException(exception);
        }
        if (Boolean.FALSE.equals(getPropertyTag().isMandatory())) {
            String description = getBeanTag().getJSPBean().getStringResource(getPropertyTag().getName() + KEY_OPTIONAL, null, null);
            if (description == null) {
                description = getBeanTag().getJSPBean().getStringResource(KEY_OPTIONAL_SELECT_OPTION, "&lt;?&gt;", null);
            }
            addOption(buffer, OPTIONAL_VALUE, description, isItemSelected(OPTIONAL_VALUE, propertyValues));
        }
        for (CodeDescription codeDescription : collection) {
            addOption(buffer, codeDescription.getCode().toString(), codeDescription.getDescription(), isItemSelected(codeDescription.getCode().toString(), propertyValues));
        }
    }

    /**
     * Determines whether the supplied item is a selected item or not.
     * @param item Item to check whether it has been selected or not.
     * @param selectedItems All items that have been selected.
     * @return True when the supplied item has been selected, false when it has
     *  not been selected.
     */
    private static boolean isItemSelected(final String item, final String[] selectedItems) {
        assert item != null;
        assert selectedItems != null;
        for (int i = 0; i < selectedItems.length; ++i) {
            if (item != null && item.equals(selectedItems[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds one option the supplied buffer.
     * @param buffer Buffer to which the HTML option will be added.
     * @param value Value to add to the HTML option statement.
     * @param description Describes the supplied value.
     * @param selected True when this option should be selected, false
     *  otherwise.
     */
    private static void addOption(final StringBuffer buffer, final String value, final String description, final boolean selected) {
        assert buffer != null;
        assert value != null;
        assert description != null;
        buffer.append("\t\t\t<option value=\"").append(HtmlUtil.quoteHtml(value)).append('\"');
        if (selected) {
            buffer.append(" selected");
        }
        buffer.append('>');
        buffer.append(HtmlUtil.quoteHtml(description));
        buffer.append("</option>\n");
    }

    /**
     * Clears attributes so the next tag won't re-use them incorrectly.
     */
    @Override
    public void release() {
        super.release();
        collection = null;
        size = null;
        multiple = Boolean.FALSE;
    }

    /**
     * Convenience method to move selected items in one input item of type
     * select to another input item of type select.
     * @param items The selected items.
     *  <br>Each element should store the object ID of an item to move.
     * @param source The list containing the selected items.
     * @param destination The list to which the selected items are moved.
     * @throws NullPointerException When either the supplied items, source or
     *  destination is null.
     *  <br>This exception is also thrown when an element in the supplied items
     *  is null or when a CodeDescription within the supplied source list
     *  contains a null code.
     */
    public static void moveSelectedItems(final String[] items, final List<? extends CodeDescription> source, final List<CodeDescription> destination) {
        CDebug.checkParameterNotNull(items, "items");
        CDebug.checkParameterNotNull(source, "source");
        CDebug.checkParameterNotNull(destination, "destination");
        for (String item : items) {
            final Long objectId = Long.valueOf(item);
            for (CodeDescription codeDescription : source) {
                if (codeDescription.getCode().equals(objectId)) {
                    source.remove(codeDescription);
                    destination.add(codeDescription);
                    break;
                }
            }
        }
    }
}
