package org.vramework.mvc.web.compiletimetags;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.vramework.commons.collections.ArrayHelper;
import org.vramework.commons.datatypes.VStrBuilder;
import org.vramework.commons.utils.VSert;
import org.vramework.mvc.IMarkupTag;
import org.vramework.mvc.exceptions.MvcErrors;
import org.vramework.mvc.exceptions.MvcException;
import org.vramework.mvc.exceptions.TagErrors;
import org.vramework.mvc.exceptions.TagException;
import org.vramework.mvc.markuptags.Attribute;

/**
 * Container for {@link Attribute}s. The grammer is described in {@link IMarkupTag}<br />
 * In order to retain the order of the attributes, we use a List instead of a Map.
 * 
 * @author thomas.mahringer
 */
public class AttributeList {

    private List<Attribute<?>> _attributes = new ArrayList<Attribute<?>>(50);

    private IMarkupTag _owningTag;

    public AttributeList(IMarkupTag owningTag) {
        setOwningTag(owningTag);
    }

    @Override
    public String toString() {
        String str = getAttributes().toString();
        return str;
    }

    /**
   * @param id
   * @param ignoreCase
   * @param throwExIfNotFound
   * @return The attribute for the given id or null if not found.
   */
    public Attribute<?> getAttribute(String id, boolean ignoreCase, boolean throwExIfNotFound) {
        for (Attribute<?> attribute : getAttributes()) {
            if (ignoreCase) {
                if (attribute.getId().equalsIgnoreCase(id)) {
                    return attribute;
                }
            } else {
                if (attribute.getId().equals(id)) {
                    return attribute;
                }
            }
        }
        return null;
    }

    /**
   * @param id
   * @param ignoreCase
   * @param defaultValue
   * @param throwExIfNotFound
   * @return The value of the attribute or null if not found or throws exception if throwExIfNotFound == true.
   */
    private Object getAttributeValue(String id, boolean ignoreCase, Object defaultValue, boolean throwExIfNotFound) {
        Attribute<?> attribute = getAttribute(id, ignoreCase, throwExIfNotFound);
        if (attribute != null) {
            return attribute.getValue();
        }
        if (throwExIfNotFound) {
            throw new MvcException(MvcErrors.TagAttributeNotFound, new Object[] { id, getOwningTag().getName(), getOwningTag().getViewName(), getOwningTag().getBeginPos(), getOwningTag().getContents() });
        } else {
            return defaultValue;
        }
    }

    /**
   * See {@link #getAttributeValue(String, boolean, Object, boolean)}.
   * 
   * @param id
   * @param throwExIfNotFound
   * @return See {@link #getAttributeValue(String, boolean, Object, boolean)}.
   */
    public String getAttributeStringValue(String id, boolean throwExIfNotFound) {
        return (String) getAttributeValue(id, true, null, throwExIfNotFound);
    }

    /**
   * See {@link #getAttributeValue(String, boolean, Object, boolean)}.
   * 
   * @param id
   * @param defaultValue
   * @param throwExIfNotFound
   * @return See {@link #getAttributeValue(String, boolean, Object, boolean)}.
   */
    public String getAttributeStringValue(String id, String defaultValue, boolean throwExIfNotFound) {
        return (String) getAttributeValue(id, true, defaultValue, throwExIfNotFound);
    }

    /**
   * See {@link #getAttributeValue(String, boolean, Object, boolean)}.
   * 
   * @param id
   * @param throwExIfNotFound
   * @return See {@link #getAttributeValue(String, boolean, Object, boolean)}.
   */
    @SuppressWarnings("unchecked")
    public List<AttributeList> getAttributeArrayValue(String id, boolean throwExIfNotFound) {
        return (List<AttributeList>) getAttributeValue(id, true, null, throwExIfNotFound);
    }

    /**
   * @param inx
   * @return The attribute at the given index.
   */
    public Attribute<?> getAttribute(int inx) {
        return getAttributes().get(inx);
    }

    /**
   * Adds a new String attribute.
   * 
   * @param id
   * @param value
   */
    public void addAttribute(String id, String value) {
        getAttributes().add(new StringAttribute(id, value));
    }

    /**
   * Adds a new array attribute.
   * 
   * @param id
   * @param arrayElements
   */
    public void addAttribute(String id, List<AttributeList> arrayElements) {
        getAttributes().add(new ArrayAttribute(id, arrayElements));
    }

    /**
   * Adds an attribute.
   * 
   * @param attribute
   */
    public void addAttribute(Attribute<?> attribute) {
        getAttributes().add(attribute);
    }

    /**
   * @return The number of attributes.
   */
    public int size() {
        return getAttributes().size();
    }

    /**
   * Translates the {@linkplain AttributeList} to a list of HTML attributes.
   * 
   * @param excludes
   *          Attributes not to be translated.
   * @return The HTML attributes.
   */
    public String toHtmlAttributeList(Set<String> excludes) {
        VStrBuilder htmlAttribList = new VStrBuilder(512);
        for (Attribute<?> attribute : getAttributes()) {
            if (excludes == null || !excludes.contains(attribute.getId())) {
                if (attribute instanceof ArrayAttribute) {
                    throw new TagException(TagErrors.CanOnlyConvertScalarAttributeToHtml, new Object[] { attribute.getId(), getOwningTag().getName(), getOwningTag().getBeginPos(), getOwningTag().getViewName() });
                }
                StringAttribute stringAttribute = (StringAttribute) attribute;
                if (htmlAttribList.length() > 0) {
                    htmlAttribList.append(" ");
                }
                String value = stringAttribute.getValue();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    htmlAttribList.append(stringAttribute.getId(), "=", value);
                } else {
                    htmlAttribList.append(stringAttribute.getId(), "=\"", value, "\"");
                }
            }
        }
        return htmlAttribList.toString();
    }

    /**
   * See {@link #toHtmlAttributeList(Set)}.
   * @param excludes
   * @return See {@link #toHtmlAttributeList(Set)}.
   */
    public String toHtmlAttributeList(String[] excludes) {
        if (excludes != null) {
            Set<String> excludesSet = ArrayHelper.toSet(excludes);
            return toHtmlAttributeList(excludesSet);
        } else {
            return toHtmlAttributeList((Set<String>) null);
        }
    }

    /**
   * See {@link #toHtmlAttributeList(Set)}.
   * @return See {@link #toHtmlAttributeList(Set)}.
   */
    public String toHtmlAttributeList() {
        return toHtmlAttributeList((Set<String>) null);
    }

    /**
   * @return the attributes
   */
    public final List<Attribute<?>> getAttributes() {
        return _attributes;
    }

    /**
   * @param attributes
   *          the attributes to set
   */
    public final void setAttributes(List<Attribute<?>> attributes) {
        VSert.argNotNull("attributes", attributes);
        _attributes = attributes;
    }

    /**
   * @return the owningTag
   */
    public final IMarkupTag getOwningTag() {
        return _owningTag;
    }

    /**
   * @param owningTag
   *          the owningTag to set
   */
    public final void setOwningTag(IMarkupTag owningTag) {
        VSert.argNotNull("owningTag", owningTag);
        _owningTag = owningTag;
    }
}
