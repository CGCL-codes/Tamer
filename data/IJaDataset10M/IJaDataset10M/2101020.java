package pl.n3fr0.n3talk.mirror;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pl.n3fr0.n3talk.message.XML;
import pl.n3fr0.n3talk.mirror.entity.MirrorBase;
import pl.n3fr0.n3talk.mirror.entity.MirrorListModificator;
import pl.n3fr0.n3talk.mirror.entity.MirrorMapModificator;
import pl.n3fr0.n3talk.mirror.entity.MirrorMapTransferer;
import pl.n3fr0.n3talk.mirror.entity.MirrorObject;
import pl.n3fr0.n3talk.mirror.entity.MirrorProperty;

public class MirrorMarshaller implements XML {

    public Element toDOM(Object object) {
        throw new UnsupportedOperationException("NYI");
    }

    @SuppressWarnings("unchecked")
    public Element toDOM(MirrorProperty property) {
        Object propValue = property.getValue();
        final Element prop = document.createElement(TAG_PROPERTY);
        prop.setAttribute(ATT_NAME, property.getName());
        prop.setAttribute(ATT_CLASS_NAME, propValue.getClass().getName());
        if (MirrorProperty.Tool.isList(propValue.getClass())) {
            final Element list = document.createElement(TAG_LIST);
            for (Object o : ((List<Object>) propValue)) {
                final Element item = document.createElement(TAG_OBJECT);
                if (o instanceof MirrorBase) {
                    final MirrorBase mb = (MirrorBase) o;
                    if (mb.isMirrored()) {
                        item.setAttribute(ATT_OBJECT_UID, mb.getObjectUID());
                    }
                }
                list.appendChild(item);
            }
            prop.appendChild(list);
        } else if (property.isMap()) {
            final Element map = document.createElement(TAG_MAP);
            final Element key = document.createElement(TAG_KEY);
            final Element val = document.createElement(TAG_VALUE);
            final MirrorMapTransferer mmt = (MirrorMapTransferer) propValue;
            prop.setAttribute(ATT_CLASS_NAME, mmt.getMapClassName());
            final MirrorMapTransferer.MapEntry[] entries = mmt.getEntries();
            for (MirrorMapTransferer.MapEntry e : entries) {
                final Element k = document.createElement(TAG_OBJECT);
                final Element v = document.createElement(TAG_OBJECT);
                k.setAttribute(ATT_OBJECT_UID, e.key.getObjectUID());
                v.setAttribute(ATT_OBJECT_UID, e.value.getObjectUID());
                key.appendChild(k);
                val.appendChild(v);
            }
            if (entries.length > 0) {
                map.appendChild(key);
                map.appendChild(val);
                prop.appendChild(map);
            } else {
                return null;
            }
        } else {
            prop.setAttribute(ATT_VALUE, property.getValue().toString());
        }
        return prop;
    }

    public Element toDOM(MirrorListModificator mlm) {
        final Element list = document.createElement(TAG_LIST);
        list.setAttribute(ATT_OBJECT_UID, mlm.getObjectUID());
        list.setAttribute(ATT_NAME, mlm.getName());
        list.setAttribute(ATT_TYPE, mlm.getKind().toString());
        final Element obj = document.createElement(TAG_OBJECT);
        obj.setAttribute(ATT_OBJECT_UID, ((MirrorObject) mlm.getObject()).getObjectUID());
        list.appendChild(obj);
        return list;
    }

    public Element toDOM(MirrorMapModificator mmm) {
        final Element map = document.createElement(TAG_MAP);
        map.setAttribute(ATT_NAME, mmm.getName());
        map.setAttribute(ATT_OBJECT_UID, mmm.getObjectUID());
        map.setAttribute(ATT_TYPE, mmm.getKind().toString());
        final Element key = document.createElement(TAG_KEY);
        key.setAttribute(ATT_OBJECT_UID, ((MirrorObject) mmm.getKey()).getObjectUID());
        map.appendChild(key);
        final Element value = document.createElement(TAG_VALUE);
        value.setAttribute(ATT_OBJECT_UID, ((MirrorObject) mmm.getValue()).getObjectUID());
        map.appendChild(value);
        return map;
    }

    public Object toObject(Element element) {
        Object o = null;
        switch(JavaClass.type(element.getAttribute(ATT_CLASS_NAME))) {
            case INTEGER:
                o = new Integer(element.getAttribute(ATT_VALUE));
                break;
            case LONG:
                o = new Long(element.getAttribute(ATT_VALUE));
                break;
            case BOOLEAN:
                o = new Boolean(element.getAttribute(ATT_VALUE));
                break;
            case STRING:
            case UNKNOWN:
            default:
                o = element.getAttribute(ATT_VALUE);
                break;
        }
        return o;
    }

    public void setDOMDocument(Document document) {
        this.document = document;
    }

    public enum JavaClass {

        INTEGER(Integer.class.getName()), LONG(Long.class.getName()), STRING(String.class.getName()), BOOLEAN(Boolean.class.getName()), UNKNOWN("");

        private JavaClass(String clazz) {
            this.clazz = clazz;
        }

        public String value() {
            return clazz;
        }

        private String clazz;

        public static JavaClass type(String name) {
            for (JavaClass cl : values()) {
                if (cl.value().equals(name)) {
                    return cl;
                }
            }
            return JavaClass.UNKNOWN;
        }
    }

    private Document document;
}
