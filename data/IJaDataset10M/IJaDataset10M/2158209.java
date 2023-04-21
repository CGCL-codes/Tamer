package modelVylegzhaninVV;

import annotationsVylegzhaninVV.VisibleVylegzhaninVV;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Table;

/**
 *
 * @author Вылегжанин Владимир Валерьевич
 */
public class MetaRowVylegzhaninVV<T extends Object> {

    private Class<T> entity;

    public MetaRowVylegzhaninVV(Class<T> e) {
        entity = e;
    }

    public Class<T> entity() {
        return entity;
    }

    public String table() {
        Table t = entity.getAnnotation(Table.class);
        return t.name();
    }

    public String visibleName() {
        VisibleVylegzhaninVV t = entity.getAnnotation(VisibleVylegzhaninVV.class);
        return t.name();
    }

    public List<Field> fields() {
        List<Field> flds = new ArrayList<Field>();
        for (Field f : entity.getDeclaredFields()) {
            VisibleVylegzhaninVV v = f.getAnnotation(VisibleVylegzhaninVV.class);
            if (v != null) {
                flds.add(f);
            }
        }
        return flds;
    }

    public List<String> headers() {
        List<String> hdr = new ArrayList<String>();
        for (Field f : entity.getDeclaredFields()) {
            VisibleVylegzhaninVV v = f.getAnnotation(VisibleVylegzhaninVV.class);
            if (v != null) {
                hdr.add(v.name());
            }
        }
        return hdr;
    }

    public List<String> names() {
        List<String> hdr = new ArrayList<String>();
        for (Field f : entity.getDeclaredFields()) {
            VisibleVylegzhaninVV v = f.getType().getAnnotation(VisibleVylegzhaninVV.class);
            if (v != null) {
                hdr.add(f.getName());
                continue;
            }
            v = f.getAnnotation(VisibleVylegzhaninVV.class);
            if (v != null) {
                hdr.add(f.getName());
            }
        }
        return hdr;
    }

    public List<Object> values(Object o) {
        List<Object> l = new ArrayList<Object>();
        for (String name : names()) {
            l.add(getFieldValue(name, o));
        }
        return l;
    }

    public Object getFieldValue(String name, Object o) {
        try {
            Field f = entity.getDeclaredField(name);
            return getFieldValue(f, o);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setFieldValue(Field f, Object o, Object value) {
        try {
            java.beans.PropertyDescriptor p = new PropertyDescriptor(f.getName(), entity);
            p.getWriteMethod().invoke(o, value);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntrospectionException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected Object getFieldValue(Field f, Object o) {
        try {
            java.beans.PropertyDescriptor p = new PropertyDescriptor(f.getName(), entity);
            return p.getReadMethod().invoke(o);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IntrospectionException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MetaRowVylegzhaninVV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public DataModelVylegzhaninVV.RowData row(T obj) {
        Integer id = (Integer) getFieldValue("id", obj);
        List<String> row = new ArrayList<String>();
        for (Field f : entity.getDeclaredFields()) {
            VisibleVylegzhaninVV v = f.getAnnotation(VisibleVylegzhaninVV.class);
            if (v != null) {
                Object o = getFieldValue(f, obj);
                row.add(convert(o));
            }
        }
        return new DataModelVylegzhaninVV.RowData(id, row);
    }

    private String convert(Object o) {
        String s = "";
        if (o instanceof Date) {
            Date d = (Date) o;
            return new SimpleDateFormat("dd.MM.yyyy").format(d);
        } else if (o != null) {
            s = o.toString();
        }
        return s;
    }
}
