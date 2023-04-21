package oso.data;

import gap.data.BlobOutputStream;
import gap.servlet.Templates;
import hapax.TemplateDictionary;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 */
public final class Image extends gap.data.BigTable {

    private static final long serialVersionUID = 4L;

    public static enum Field implements gap.data.Field {

        KEY("key"), ID("id"), LAST_UPDATED("updated"), MIME_TYPE("mimeType"), FILE_NAME("fileName"), BINARY("binary"), USER_ID("userId");

        private static final Map<String, Field> FieldName = new java.util.HashMap<String, Field>();

        public static final Field[] AllFields = Field.values();

        public static final String[] AllNames;

        static {
            int count = AllFields.length;
            String[] names = new String[count];
            for (int cc = 0; cc < count; cc++) {
                Field field = AllFields[cc];
                String fieldName = field.getFieldName();
                names[cc] = fieldName;
                FieldName.put(fieldName, field);
            }
            AllNames = names;
        }

        public static Field getField(String name) {
            return FieldName.get(name);
        }

        public static Object Get(Field field, Image instance) {
            switch(field) {
                case KEY:
                    return instance.getKey();
                case ID:
                    return instance.getId();
                case LAST_UPDATED:
                    return instance.getUpdated();
                case MIME_TYPE:
                    return instance.getMimeType();
                case FILE_NAME:
                    return instance.getFileName();
                case BINARY:
                    return instance.getBinary();
                case USER_ID:
                    return instance.getUserId();
                default:
                    throw new IllegalArgumentException(field.toString() + " in Image");
            }
        }

        public static void Set(Field field, Image instance, Object value) {
            switch(field) {
                case KEY:
                    instance.setKey((Key) value);
                    return;
                case ID:
                    instance.setId((String) value);
                    return;
                case LAST_UPDATED:
                    instance.setUpdated((Date) value);
                    return;
                case MIME_TYPE:
                    instance.setMimeType((String) value);
                    return;
                case FILE_NAME:
                    instance.setFileName((String) value);
                    return;
                case BINARY:
                    instance.setBinary((Blob) value);
                    return;
                case USER_ID:
                    instance.setUserId((String) value);
                    return;
                default:
                    throw new IllegalArgumentException(field.toString() + " in Image");
            }
        }

        private final String fieldName;

        Field(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public String toString() {
            return this.fieldName;
        }
    }

    private volatile Key key;

    private volatile String id;

    private volatile Date updated;

    private volatile String mimeType;

    private volatile String fileName;

    private volatile Blob binary;

    private volatile String userId;

    public Image() {
        super();
    }

    public Image(String id, String userId) {
        super();
        this.id = id;
        this.userId = userId;
    }

    public void init() {
    }

    public boolean hasKey() {
        return (null != this.key);
    }

    public boolean hasNotKey() {
        return (null == this.key);
    }

    public Key getKey() {
        return this.key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getUpdated() {
        if (this.updated == null) return null; else return new Date(this.updated.getTime());
    }

    public void setUpdated(Date updated) {
        if (updated == null) this.updated = null; else this.updated = new Date(updated.getTime());
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Blob getBinary() {
        return this.binary;
    }

    public void setBinary(Blob blob) {
        this.binary = blob;
    }

    public int lengthOfBinary() {
        Blob binary = this.binary;
        if (null == binary) return 0; else {
            byte[] bytes = binary.getBytes();
            if (null == bytes) return 0; else return bytes.length;
        }
    }

    public BlobOutputStream writeBinary() {
        return new BlobOutputStream(this, Field.BINARY);
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClassKind() {
        return "Image";
    }

    public String getClassFieldUnique() {
        return "id";
    }

    public String getClassFieldKeyName() {
        return "key";
    }

    public List<gap.data.Field> getClassFields() {
        List<gap.data.Field> list = new java.util.ArrayList<gap.data.Field>();
        for (gap.data.Field field : Field.values()) list.add(field);
        return list;
    }

    public gap.data.Field getClassFieldByName(String name) {
        return Field.getField(name);
    }

    public java.io.Serializable valueOf(gap.data.Field field) {
        return (java.io.Serializable) Field.Get((Field) field, this);
    }

    public void define(gap.data.Field field, java.io.Serializable value) {
        Field.Set((Field) field, this, value);
    }

    public int updateFrom(gap.servlet.Query query, Image request) {
        return 0;
    }

    public TemplateDictionary dictionaryFor() {
        return this.dictionaryInto(Templates.CreateDictionary());
    }

    public TemplateDictionary dictionaryInto(TemplateDictionary dict) {
        for (Field field : Field.AllFields) {
            java.lang.Object value = Field.Get(field, this);
            if (null != value) {
                dict.putVariable(field.toString(), value.toString());
            }
        }
        return dict;
    }

    public TemplateDictionary dictionaryFor(gap.servlet.Query query) {
        TemplateDictionary dict = Templates.CreateDictionary();
        if (null != query && query.hasFields()) {
            for (String name : query.getFields()) {
                Field field = Field.getField(name);
                if (null != field) {
                    java.lang.Object value = Field.Get(field, this);
                    if (null != value) {
                        dict.putVariable(field.toString(), value.toString());
                    }
                }
            }
            return dict;
        } else return this.dictionaryInto(dict);
    }
}
