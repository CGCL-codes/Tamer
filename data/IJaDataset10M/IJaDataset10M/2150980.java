package org.rapla.storage.xml;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import org.apache.avalon.framework.logger.Logger;
import org.rapla.components.util.SerializableDateTimeFormat;
import org.rapla.entities.Category;
import org.rapla.entities.EntityNotFoundException;
import org.rapla.entities.Ownable;
import org.rapla.entities.RaplaObject;
import org.rapla.entities.RaplaType;
import org.rapla.entities.User;
import org.rapla.entities.domain.Permission;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.internal.AttributeImpl;
import org.rapla.entities.internal.CategoryImpl;
import org.rapla.entities.storage.EntityResolver;
import org.rapla.entities.storage.RefEntity;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.storage.IdTable;
import org.rapla.storage.LocalCache;
import org.rapla.storage.impl.EntityStore;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class RaplaXMLReader extends DelegationHandler implements Namespaces {

    EntityStore resolver;

    Logger logger;

    IdTable idTable;

    RaplaContext sm;

    Map localnameMap;

    Map readerMap;

    SerializableDateTimeFormat dateTimeFormat;

    public RaplaXMLReader(RaplaContext context) throws RaplaException {
        logger = (Logger) context.lookup(Logger.class.getName());
        this.sm = context;
        this.resolver = (EntityStore) context.lookup(EntityStore.class.getName());
        this.idTable = (IdTable) context.lookup(IdTable.class.getName());
        RaplaLocale raplaLocale = (RaplaLocale) context.lookup(RaplaLocale.ROLE);
        dateTimeFormat = new SerializableDateTimeFormat(raplaLocale.createCalendar());
        this.localnameMap = (Map) context.lookup(PreferenceReader.LOCALNAMEMAPENTRY);
        this.readerMap = (Map) context.lookup(PreferenceReader.READERMAP);
    }

    public RaplaType getTypeForLocalName(String localName) throws SAXParseException {
        RaplaType type = (RaplaType) localnameMap.get(localName);
        if (type == null) throw createSAXParseException("No type declared for localname " + localName);
        return type;
    }

    /**
     * @param raplaType
     * @throws SAXParseException
     */
    protected RaplaXMLReader getChildHandlerForType(RaplaType raplaType) throws SAXParseException {
        RaplaXMLReader childReader = (RaplaXMLReader) readerMap.get(raplaType);
        if (childReader == null) {
            throw createSAXParseException("No Reader declared for type " + raplaType);
        }
        childReader.setDocumentLocator(getLocator());
        addChildHandler(childReader);
        return childReader;
    }

    protected Logger getLogger() {
        return logger;
    }

    protected boolean isContentCategoryId(String content) {
        if (content == null) {
            return false;
        }
        content = content.trim();
        String KEY_START = Category.TYPE.getLocalName() + "_";
        boolean idContent = (content.indexOf(KEY_START) < 2 && content.length() < KEY_START.length() + 5 && content.length() > 0);
        return idContent;
    }

    public Long parseLong(String text) throws SAXException {
        try {
            return new Long(text);
        } catch (NumberFormatException ex) {
            throw createSAXParseException("No valid number format: " + text);
        }
    }

    public Date parseDate(String date, boolean fillDate) throws SAXException {
        try {
            return dateTimeFormat.parseDate(date, fillDate);
        } catch (ParseException ex) {
            throw createSAXParseException(ex.getMessage());
        }
    }

    public Date parseDateTime(String date, String time) throws SAXException {
        try {
            return dateTimeFormat.parseDateTime(date, time);
        } catch (ParseException ex) {
            throw createSAXParseException(ex.getMessage());
        }
    }

    protected String getString(Attributes atts, String key, String defaultString) {
        String str = atts.getValue("", key);
        return (str != null) ? str : defaultString;
    }

    protected String getString(Attributes atts, String key) throws SAXParseException {
        String str = atts.getValue("", key);
        if (str == null) throw createSAXParseException("Attribute " + key + " not found!");
        return str;
    }

    /** return the new id */
    protected Object setId(RefEntity entity, Attributes atts) throws SAXException {
        String idString = atts.getValue("id");
        Object id = getId(entity.getRaplaType(), idString);
        entity.setId(id);
        return id;
    }

    protected void setVersionIfThere(RefEntity entity, Attributes atts) throws SAXException {
        String version = atts.getValue("version");
        if (version != null) {
            try {
                entity.setVersion(Long.parseLong(version));
            } catch (NumberFormatException ex) {
                createSAXParseException("Error parsing version-string '" + version + "'");
            }
        }
    }

    /** return the new id */
    protected Object setNewId(RefEntity entity) throws SAXException {
        try {
            Object id = idTable.createId(entity.getRaplaType());
            entity.setId(id);
            return id;
        } catch (RaplaException ex) {
            throw createSAXParseException(ex.getMessage());
        }
    }

    protected void setOwner(Ownable ownable, Attributes atts) throws SAXException {
        String ownerString = atts.getValue("owner");
        if (ownerString != null) {
            ownable.setOwner((User) resolve(User.TYPE, ownerString));
        }
    }

    protected Object getId(RaplaType type, String str) throws SAXException {
        try {
            Object id = LocalCache.getId(type, str);
            return id;
        } catch (ParseException ex) {
            ex.printStackTrace();
            throw createSAXParseException(ex.getMessage());
        }
    }

    void throwEntityNotFound(String type, Integer id) throws SAXException {
        throw createSAXParseException(type + " with id '" + id + "' not found.");
    }

    public RaplaObject getType() throws SAXException {
        throw createSAXParseException("Method getType() not implemented by subclass " + this.getClass().getName());
    }

    protected CategoryImpl getSuperCategory() {
        return resolver.getSuperCategory();
    }

    public DynamicType getDynamicType(String keyref) {
        return resolver.getDynamicType(keyref);
    }

    protected Object resolve(RaplaType type, String str) throws SAXException {
        try {
            return resolver.resolve(getId(type, str));
        } catch (EntityNotFoundException ex) {
            throw createSAXParseException(ex.getMessage());
        }
    }

    protected Object parseAttributeValue(Attribute attribute, String text) throws SAXException {
        try {
            EntityResolver resolver = null;
            if (isContentCategoryId(text)) resolver = this.resolver;
            return AttributeImpl.parseAttributeValue(attribute, text, resolver);
        } catch (ParseException ex) {
            throw createSAXParseException(ex.getMessage());
        }
    }

    public void add(RefEntity entity) {
        resolver.put(entity);
    }

    public void remove(String localname, String id) throws SAXException {
        RaplaType type = getTypeForLocalName(localname);
        Object idObject = getId(type, id);
        resolver.addRemoveId(idObject);
    }

    protected Category getCategoryFromPath(String path) throws SAXParseException {
        try {
            return getSuperCategory().getCategoryFromPath(path);
        } catch (Exception ex) {
            throw createSAXParseException(ex.getMessage());
        }
    }

    protected Category getGroup(String groupKey) throws SAXParseException {
        CategoryImpl groupCategory = (CategoryImpl) getSuperCategory().getCategory(Permission.GROUP_CATEGORY_KEY);
        if (groupCategory == null) {
            throw createSAXParseException(Permission.GROUP_CATEGORY_KEY + " category not found");
        }
        try {
            return groupCategory.getCategoryFromPath(groupKey);
        } catch (Exception ex) {
            throw createSAXParseException(ex);
        }
    }

    protected void putPassword(Object userid, String password) {
        resolver.putPassword(userid, password);
    }
}
