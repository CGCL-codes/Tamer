package com.ivata.mask.group;

import org.apache.log4j.Logger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import com.ivata.mask.field.Field;
import com.ivata.mask.filter.Filter;

/**
 * Implementation of {@link Group}defining a group of masks which share common
 * field definitions and other characteristics.
 *
 * @since ivata masks 0.1 (2004-02-26)
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 */
public class GroupImpl implements Group, Serializable {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(GroupImpl.class);

    /**
     * Serialization version (for <code>Serializable</code> interface).
     */
    private static final long serialVersionUID = 1L;

    /**
     * Implementation of <code>getAllExcludedFieldNames()</code>.
     *
     * @param instance
     *            instance for which to get the excluded fields.
     * @return list of all excluded field ids, as a <code>List</code> of
     *         <code>String</code> instances.
     * @see #getAllExcludedFieldNames()
     */
    protected static Set getAllExcludedFieldNames(final Group instance) {
        if (logger.isDebugEnabled()) {
            logger.debug("getAllExcludedFieldNames(Group instance = " + instance + ") - start");
        }
        Set excluded = new HashSet();
        Group parent = instance.getParent();
        if (parent != null) {
            excluded.addAll(getAllExcludedFieldNames(parent));
        }
        excluded.removeAll(instance.getIncludedFieldNames());
        excluded.addAll(instance.getExcludedFieldNames());
        if (logger.isDebugEnabled()) {
            logger.debug("getAllExcludedFieldNames - end - return value = " + excluded);
        }
        return excluded;
    }

    /**
     * Implementation of <code>getAllFirstFieldNames()</code>.
     *
     * @param instance
     *            instance for which to get the first fields.
     * @return list of all first field ids, as a <code>List</code> of
     *         <code>String</code> instances.
     * @see #getAllFirstFieldNames()
     */
    protected static List getAllFirstFieldNames(final Group instance) {
        if (logger.isDebugEnabled()) {
            logger.debug("getAllFirstFieldNames(Group instance = " + instance + ") - start");
        }
        List firstFieldNames = new ArrayList();
        Group parent = instance.getParent();
        if ((parent != null) && !instance.isParentFirstFieldNamesReplaced()) {
            firstFieldNames.addAll(getAllFirstFieldNames(parent));
        }
        firstFieldNames.addAll(instance.getFirstFieldNames());
        firstFieldNames.removeAll(instance.getExcludedFieldNames());
        if (logger.isDebugEnabled()) {
            logger.debug("getAllFirstFieldNames(Group) - end - return value = " + firstFieldNames);
        }
        return firstFieldNames;
    }

    /**
     * Implementation of <code>getAllLastFieldNames()</code>.
     *
     * @param instance
     *            instance for which to get the last fields.
     * @return list of all last field ids, as a <code>List</code> of
     *         <code>String</code> instances.
     * @see #getAllLastFieldNames()
     */
    protected static List getAllLastFieldNames(final Group instance) {
        if (logger.isDebugEnabled()) {
            logger.debug("getAllLastFieldNames(Group instance = " + instance + ") - start");
        }
        List lastFieldNames = new ArrayList();
        Group parent = instance.getParent();
        if ((parent != null) && !instance.isParentLastFieldNamesReplaced()) {
            lastFieldNames.addAll(getAllLastFieldNames(parent));
        }
        lastFieldNames.addAll(instance.getLastFieldNames());
        lastFieldNames.removeAll(instance.getExcludedFieldNames());
        if (logger.isDebugEnabled()) {
            logger.debug("getAllLastFieldNames(Group) - end - return value = " + lastFieldNames);
        }
        return lastFieldNames;
    }

    /**
     * <p>
     * If <code>true</code>, only the values in this mask will be displayed.
     * Otherwise, input fields are displayed.
     * </p>
     */
    private Boolean displayOnly = null;

    /**
     * Field names to be excluded from this group of definitions.
     */
    private Set excludedFieldNames = new HashSet();

    /**
     * Default field definitions, referenced by name. These can be
     * altered/overridden.
     */
    private Map fields = new HashMap();

    /**
     * <copyDoc>Refer to {@link Group#getFilters}.</copyDoc>
     */
    private List filters = new Vector();

    /**
     * Names of fields which should come last, in the order in which they should
     * appear.
     */
    private List firstFieldNames = new ArrayList();

    /**
     * Field names to be included from this group of definitions. This is used
     * to explicitly override a field which was excluded higher up the group
     * hierarchy.
     */
    private Set includedFieldNames = new HashSet();

    /**
     * Names of fields which should come last, in the order in which they should
     * appear.
     */
    private List lastFieldNames = new ArrayList();

    /**
     * Identifier for this group. This should be unique as it is used to
     * identify the group.
     */
    private String name;

    /**
     * Group which this group of masks/fields extends.
     */
    private Group parent;

    /**
     * If <code>false</code>, then the parent first field name list is
     * extended by the names in this mask, otherwise it is relaced.
     */
    private boolean parentFirstFieldNamesReplaced = false;

    /**
     * If <code>false</code>, then the parent last field name list is
     * extended by the names in this mask, otherwise it is relaced.
     */
    private boolean parentLastFieldNamesReplaced = false;

    /**
     * Create a group with no parent.
     *
     * @param nameParam
     *            unique identifier for this group.
     */
    public GroupImpl(final String nameParam) {
        this.name = nameParam;
    }

    /**
     * Create a mask with the specified parent.
     *
     * @param nameParam
     *            unique identifier for this group.
     * @param parentParam
     *            group which this group extends.
     */
    public GroupImpl(final String nameParam, final Group parentParam) {
        this.name = nameParam;
        this.parent = parentParam;
    }

    /**
     * Add the name of a field to exclude from this mask group.
     *
     * @param nameParam
     *            unique identifier for the field which should be excluded.
     * @see #getExcludedFieldNames
     */
    public final void addExcludedFieldName(final String nameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("addExcludedFieldName(String nameParam = " + nameParam + ") - start");
        }
        excludedFieldNames.add(nameParam);
        if (logger.isDebugEnabled()) {
            logger.debug("addExcludedFieldName(String) - end");
        }
    }

    /**
     * Add a field definition to this group.
     *
     * @param fieldParam
     *            field definition to add to this mask group.
     */
    public final void addField(final Field fieldParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("addField(Field fieldParam = " + fieldParam + ") - start");
        }
        fields.put(fieldParam.getName(), fieldParam);
        if (logger.isDebugEnabled()) {
            logger.debug("addField(Field) - end");
        }
    }

    /**
     * Add a filter to the list.
     *
     * @param filterParam filter to be added.
     * @see Group#getFilters.
     */
    public final void addFilter(final Filter filterParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("addFilter(Filter filterParam = " + filterParam + ") - start");
        }
        filters.add(filterParam);
        if (logger.isDebugEnabled()) {
            logger.debug("addFilter(Filter) - end");
        }
    }

    /**
     * Add the identifier for one of the first fields in the mask.
     *
     * @param nameParam
     *            unique identifier for the field which should appear at the
     *            start of the mask group.
     * @see #getFirstFieldNames
     */
    public final void addFirstFieldName(final String nameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("addFirstFieldName(String nameParam = " + nameParam + ") - start");
        }
        firstFieldNames.add(nameParam);
        if (logger.isDebugEnabled()) {
            logger.debug("addFirstFieldName(String) - end");
        }
    }

    /**
     * Add the name of an included field.
     *
     * @param nameParam
     *            unique identifier for the field which should appear in the
     *            mask group, overriding parent excluded fields.
     * @see #getIncludedFieldNames()
     */
    public final void addIncludedFieldName(final String nameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("addIncludedFieldName(String nameParam = " + nameParam + ") - start");
        }
        includedFieldNames.add(nameParam);
        if (logger.isDebugEnabled()) {
            logger.debug("addIncludedFieldName(String) - end");
        }
    }

    /**
     * Add the identifier for one of the last fields in the mask.
     *
     * @param nameParam
     *            unique identifier for the field which should appear at the end
     *            of the mask group.
     * @see #getLastFieldNames
     */
    public final void addLastFieldName(final String nameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("addLastFieldName(String nameParam = " + nameParam + ") - start");
        }
        lastFieldNames.add(nameParam);
        if (logger.isDebugEnabled()) {
            logger.debug("addLastFieldName(String) - end");
        }
    }

    /**
     * Get the field names of all fields which have been excluded from this and
     * its parents.
     * <p>
     * <b>Note: </b> This will include fields which were explicitly included,
     * even if a parent class excluded them before.
     * </p>
     *
     * @return list of all excluded field names, as a <code>Set</code> of
     *         <code>String</code> instances.
     */
    public final Set getAllExcludedFieldNames() {
        if (logger.isDebugEnabled()) {
            logger.debug("getAllExcludedFieldNames() - start");
        }
        Set returnSet = Collections.unmodifiableSet(getAllExcludedFieldNames(this));
        if (logger.isDebugEnabled()) {
            logger.debug("getAllExcludedFieldNames() - end - return value = " + returnSet);
        }
        return returnSet;
    }

    /**
     * <p>
     * Get the field identifiers of all fields which should appear at the start
     * of the group/mask, including those defined by its parent.
     * </p>
     *
     * @return list of all first field ids, as a <code>List</code> of
     *         <code>String</code> instances.
     * @see #getFirstFieldNames
     */
    public final List getAllFirstFieldNames() {
        if (logger.isDebugEnabled()) {
            logger.debug("getAllFirstFieldNames() - start");
        }
        List returnList = Collections.unmodifiableList(getAllFirstFieldNames(this));
        if (logger.isDebugEnabled()) {
            logger.debug("getAllFirstFieldNames() - end - return value = " + returnList);
        }
        return returnList;
    }

    /**
     * <p>
     * Get the field identifiers of all fields which should appear at the end of
     * the group/mask, including those defined by its parent.
     * </p>
     *
     * @return list of all last field ids, as a <code>List</code> of
     *         <code>String</code> instances.
     * @see #getFirstFieldNames
     */
    public final List getAllLastFieldNames() {
        if (logger.isDebugEnabled()) {
            logger.debug("getAllLastFieldNames() - start");
        }
        List returnList = Collections.unmodifiableList(getAllLastFieldNames(this));
        if (logger.isDebugEnabled()) {
            logger.debug("getAllLastFieldNames() - end - return value = " + returnList);
        }
        return returnList;
    }

    /**
     * <p>
     * Get the field ids which have been explicitly excluded from this group.
     * </p>
     *
     * @return the field ids which have been explicitly excluded from this group
     *         (not including those excluded by parent groups).
     */
    public final Set getExcludedFieldNames() {
        if (logger.isDebugEnabled()) {
            logger.debug("getExcludedFieldNames() - start");
        }
        Set returnSet = Collections.unmodifiableSet(excludedFieldNames);
        if (logger.isDebugEnabled()) {
            logger.debug("getExcludedFieldNames() - end - return value = " + returnSet);
        }
        return returnSet;
    }

    /**
     * <p>
     * Default field definitions. These can be altered/overridden.
     * </p>
     *
     * @param nameParam
     *            name of the field to be returned.
     * @return read-only copy of the fields.
     */
    public final Field getField(final String nameParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("getField(String nameParam = " + nameParam + ") - start");
        }
        Field field = (Field) fields.get(nameParam);
        if ((field == null) && (parent != null)) {
            Field returnField = parent.getField(nameParam);
            if (logger.isDebugEnabled()) {
                logger.debug("getField(String) - end - return value = " + returnField);
            }
            return returnField;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getField(String) - end - return value = " + field);
        }
        return field;
    }

    /**
     * <copyDoc>Refer to {@link Group#getFilters}.</copyDoc>
     * @return <copyDoc>Refer to {@link Group#getFilters}.</copyDoc>
     */
    public List getFilters() {
        if (logger.isDebugEnabled()) {
            logger.debug("getFilters() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getFilters() - end - return value = " + filters);
        }
        return filters;
    }

    /**
     * <p>
     * Get the ids of all fields which should appear at the start of masks in
     * this group. For an input mask this usually means the fields will appear
     * at the top of the page; for a list the fields will appear at the left of
     * the list.
     * </p>
     * <p>
     * <b>Note </b> that all these fields do not need to be present in all masks
     * of this group (some value objects may not have all the fields listed).
     * Those fields which are listed and present in the value object will appear
     * at the start, in the order given.
     * </p>
     *
     * @return list containing string identifiers of fields which should appear
     *         first in the mask.
     */
    public final List getFirstFieldNames() {
        if (logger.isDebugEnabled()) {
            logger.debug("getFirstFieldNames() - start");
        }
        List returnList = Collections.unmodifiableList(firstFieldNames);
        if (logger.isDebugEnabled()) {
            logger.debug("getFirstFieldNames() - end - return value = " + returnList);
        }
        return returnList;
    }

    /**
     * By explicitly including fields in a mask, you can override fields
     * excluded by one of its parents.
     *
     * @return fields explicitly included (overridden) in this mask.
     */
    public final Set getIncludedFieldNames() {
        if (logger.isDebugEnabled()) {
            logger.debug("getIncludedFieldNames() - start");
        }
        Set returnSet = Collections.unmodifiableSet(includedFieldNames);
        if (logger.isDebugEnabled()) {
            logger.debug("getIncludedFieldNames() - end - return value = " + returnSet);
        }
        return returnSet;
    }

    /**
     * <p>
     * Get the ids of all fields which should appear at the end of masks in this
     * group. For an input mask this usually means the fields will appear at the
     * bottom of the page; for a list the fields will appear at the right of the
     * list.
     * </p>
     * <p>
     * <b>Note </b> that all these fields do not need to be present in all masks
     * of this group (some value objects may not have all the fields listed).
     * Those fields which are listed and present in the value object will appear
     * at the end, in the order given.
     * </p>
     *
     * @return list containing string identifiers of fields which should appear
     *         last in the mask.
     */
    public final List getLastFieldNames() {
        if (logger.isDebugEnabled()) {
            logger.debug("getLastFieldNames() - start");
        }
        List returnList = Collections.unmodifiableList(lastFieldNames);
        if (logger.isDebugEnabled()) {
            logger.debug("getLastFieldNames() - end - return value = " + returnList);
        }
        return returnList;
    }

    /**
     * <copyDoc>Refer to {@link Group#getName}.</copyDoc>
     *
     * @return unique identifier of this group.
     */
    public final String getName() {
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getName() - end - return value = " + name);
        }
        return name;
    }

    /**
     * <p>
     * Get the parent of this group, if any.
     * </p>
     *
     * <p>
     * Each group or mask can define a parent, from which it can inherit field
     * definitions and group/mask properties.
     * </p>
     *
     * @return parent of this group, or <code>null</code> if this is a
     *         top-level group.
     */
    public final Group getParent() {
        if (logger.isDebugEnabled()) {
            logger.debug("getParent() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("getParent() - end - return value = " + parent);
        }
        return parent;
    }

    /**
     * <p>
     * If <code>true</code>, only the values in this mask will be displayed.
     * Otherwise, input fields are displayed.
     * </p>
     *
     * <p>
     * This setting is inherited. If it is not set in a group directly, the
     * value for the group's parent is taken.
     * </p>
     *
     * @return whether or not the mask should only display field values.
     */
    public final boolean isDisplayOnly() {
        if (logger.isDebugEnabled()) {
            logger.debug("isDisplayOnly() - start");
        }
        if (displayOnly != null) {
            boolean returnboolean = displayOnly.booleanValue();
            if (logger.isDebugEnabled()) {
                logger.debug("isDisplayOnly() - end - return value = " + returnboolean);
            }
            return returnboolean;
        }
        if (parent != null) {
            boolean returnboolean = parent.isDisplayOnly();
            if (logger.isDebugEnabled()) {
                logger.debug("isDisplayOnly() - end - return value = " + returnboolean);
            }
            return returnboolean;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("isDisplayOnly() - end - return value = " + false);
        }
        return false;
    }

    /**
     * <p>
     * When first field identifiers are defined for a group, normally these are
     * appended to the list of all parent group first field identifiers, i.e.
     * the list returned by calling
     * {@link #getFirstFieldNames getFirstFieldNames} on the parent group.
     * </p>
     * <p>
     * This is the standard, default behavior when this method returns
     * <code>false</code>. However, if this method returns <code>true</code>,
     * then the first field identifiers in this group override (replace) the
     * list returned by the group's parents.
     * </p>
     *
     * @return <code>true</code> if this group replaces the list of first
     *         field ids defined by parent groups, otherwise <code>false</code>
     *         if this group's list will be appended to that of its parents.
     * @see #getFirstFieldNames
     */
    public final boolean isParentFirstFieldNamesReplaced() {
        if (logger.isDebugEnabled()) {
            logger.debug("isParentFirstFieldNamesReplaced() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("isParentFirstFieldNamesReplaced - end - return value = " + parentFirstFieldNamesReplaced);
        }
        return parentFirstFieldNamesReplaced;
    }

    /**
     * <p>
     * When last field identifiers are defined for a group, normally these are
     * appended to the list of all parent group last field identifiers, i.e. the
     * list returned by calling
     * {@link #getLastFieldNames getLastFieldNames}on the parent group.
     * </p>
     * <p>
     * This is the standard, default behavior when this method returns
     * <code>false</code>. However, if this method returns <code>true</code>,
     * then the last field identifiers in this group override (replace) the list
     * returned by the group's parents.
     * </p>
     *
     * @return <code>true</code> if this group replaces the list of last field
     *         ids defined by parent groups, otherwise <code>false</code> if
     *         this group's list will be appended to that of its parents.
     * @see #getLastFieldNames
     */
    public final boolean isParentLastFieldNamesReplaced() {
        if (logger.isDebugEnabled()) {
            logger.debug("isParentLastFieldNamesReplaced() - start");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("isParentLastFieldNamesReplaced() - end - return value = " + parentLastFieldNamesReplaced);
        }
        return parentLastFieldNamesReplaced;
    }

    /**
     * <p>
     * If <code>true</code>, only the values in this mask will be displayed.
     * Otherwise, input fields are displayed.
     * </p>
     *
     * <p>
     * This setting is inherited. If it is not set in a group directly, the
     * value for the group's parent is taken.
     * </p>
     *
     * @param b
     *            if <code>true</code> field values are just displayed and
     *            cannot be changed.
     * @see #isDisplayOnly
     */
    public final void setDisplayOnly(final boolean b) {
        if (logger.isDebugEnabled()) {
            logger.debug("setDisplayOnly(boolean b = " + b + ") - start");
        }
        displayOnly = new Boolean(b);
        if (logger.isDebugEnabled()) {
            logger.debug("setDisplayOnly(boolean) - end");
        }
    }

    /**
     * Set whether or not the first field name list of parent mask groups should
     * be replaced.
     *
     * @param parentFirstFieldNamesReplacedParam
     *            <code>true</code> if parent first field ids should be
     *            replaced (ignored), otherwise <code>false</code>.
     * @see #isParentFirstFieldNamesReplaced()
     */
    public final void setParentFirstFieldNamesReplaced(final boolean parentFirstFieldNamesReplacedParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setParentFirstFieldNamesReplaced(" + "boolean parentFirstFieldNamesReplacedParam = " + parentFirstFieldNamesReplacedParam + ") - start");
        }
        parentFirstFieldNamesReplaced = parentFirstFieldNamesReplacedParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setParentFirstFieldNamesReplaced(boolean) - end");
        }
    }

    /**
     * Set whether or not the last field name list of parent mask groups should
     * be replaced.
     *
     * @param parentLastFieldNamesReplacedParam
     *            <code>true</code> if parent last field ids should be
     *            replaced (ignored), otherwise <code>false</code>.
     * @see #isParentLastFieldNamesReplaced()
     */
    public final void setParentLastFieldNamesReplaced(final boolean parentLastFieldNamesReplacedParam) {
        if (logger.isDebugEnabled()) {
            logger.debug("setParentLastFieldNamesReplaced(" + "boolean parentLastFieldNamesReplacedParam = " + parentLastFieldNamesReplacedParam + ") - start");
        }
        parentLastFieldNamesReplaced = parentLastFieldNamesReplacedParam;
        if (logger.isDebugEnabled()) {
            logger.debug("setParentLastFieldNamesReplaced(boolean) - end");
        }
    }
}
