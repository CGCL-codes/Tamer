package com.germinus.xpression.cms.contents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.germinus.xpression.cms.CmsConfig;
import com.germinus.xpression.cms.categories.CategoryDefinition;
import com.germinus.xpression.cms.contents.binary.BinaryDataReference;
import com.germinus.xpression.i18n.I18NString;
import com.germinus.xpression.i18n.I18NUtils;

/**
 * This class defines the configuration of a Field
 * User: dpalmeira
 * Date: Ene 13, 2005
 * Time: 10:55:00 AM
 */
public class FieldDefinition {

    private static final String CHAPTER_TYPE = "chapter";

    private static final String GLOSSARY_TYPE = "glossary";

    private static final String PREVIEW_TYPE = "preview";

    private static final String HTML_TYPE_PREFIX = "html";

    private static final String TEXTAREA_TYPE = "textarea";

    private static final String TEXT_TYPE = "text";

    private static final String IMAGE_TYPE = "image";

    private static final String FILE_TYPE = "file";

    private static final String TAG_TYPE = "tags";

    private String id;

    private String name;

    private String type;

    private String maxlength;

    private String comments;

    private String width;

    private String height;

    private String depends;

    private String options;

    private Boolean internacionalizable = Boolean.FALSE;

    private List<ListItemDefinition> listItems;

    private CategoryDefinition category;

    private ContentTypeDefinition contentType;

    public FieldDefinition() {
        this.listItems = new ArrayList<ListItemDefinition>();
    }

    public String getId() {
        return id;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void createListItemRange(Integer start, Integer end, Integer step) {
        for (int i = start.intValue(); i <= end.intValue(); i += step.intValue()) {
            String key = Integer.toString(i);
            String label = key;
            addListItem(new ListItemDefinition(key, label));
        }
    }

    public void addListItem(ListItemDefinition listItem) {
        listItems.add(listItem);
    }

    @SuppressWarnings("unchecked")
    public Collection getListItems() {
        return listItems;
    }

    public void addCategory(CategoryDefinition category) {
        this.category = category;
    }

    public CategoryDefinition getCategory() {
        return category;
    }

    public ContentTypeDefinition getContentType() {
        return contentType;
    }

    public void addContentType(ContentTypeDefinition contentType) {
        this.contentType = contentType;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean equals(Object object) {
        if (!(object instanceof FieldDefinition)) {
            return false;
        }
        FieldDefinition def = (FieldDefinition) object;
        return new EqualsBuilder().append(id, def.id).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(217, 37).append(id).toHashCode();
    }

    /**
     * Fields that are considered categories and therefore populated in a special way
     * @return
     */
    public boolean isCategoryField() {
        return CmsConfig.getCategoryFields().contains(getType());
    }

    public boolean isClassificationCategoryField() {
        return CmsConfig.getClassificationCategories().contains(getCategory().getName());
    }

    public Boolean getInternacionalizable() {
        return internacionalizable;
    }

    public void setInternacionalizable(Boolean internacionalizable) {
        this.internacionalizable = internacionalizable;
    }

    public boolean isPopulable() {
        return !(isChapterType() || isGlossaryType());
    }

    private boolean isGlossaryType() {
        return getType().equals(GLOSSARY_TYPE);
    }

    public boolean isChapterType() {
        return getType().equals(CHAPTER_TYPE);
    }

    public boolean isEmpty(Object fieldValue) {
        if (fieldValue == null) return true;
        if (fieldValue instanceof String && StringUtils.isEmpty((String) fieldValue)) return true;
        if (fieldValue instanceof I18NString && I18NUtils.isEmptyCurrentLocale((I18NString) fieldValue)) {
            return true;
        }
        if (isBinaryField()) {
            BinaryDataReference binaryDataReference = (BinaryDataReference) fieldValue;
            return BinaryDataReference.NOT_CONFIGURED.equals(binaryDataReference.getURLPath());
        }
        return fieldValue == null;
    }

    boolean isBinaryField() {
        return isFileType() || isImageType() || getType().equals(PREVIEW_TYPE);
    }

    public boolean isImageType() {
        return IMAGE_TYPE.equals(getType());
    }

    public boolean isFileType() {
        return FILE_TYPE.equals(getType());
    }

    boolean isTextField() {
        return isTextType() || isTextAreaType() || isHtmlType();
    }

    public boolean isHtmlType() {
        return getType().startsWith(HTML_TYPE_PREFIX);
    }

    public boolean isTextAreaType() {
        return TEXTAREA_TYPE.equals(getType());
    }

    public boolean isTextType() {
        return TEXT_TYPE.equals(getType());
    }

    public boolean isTagField() {
        return TAG_TYPE.equals(getType());
    }
}
