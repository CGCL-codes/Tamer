package xmitools2.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import xmitools2.model.XmiElement;
import xmitools2.model.XmiFactory;

/**
 * Helpers for UML models.
 * 
 * @author rlechner
 */
public final class UmlHelpers {

    /**
     * Sets the UML name of a model element.
     * 
     * @param element the UML element
     * @param name the new name
     * @param factory an XMI factory
     */
    public static void setName(XmiElement element, String name, XmiFactory factory) {
        if (element.getAttribute("name") != null) {
            element.setAttribute("name", name);
            return;
        }
        XmiElement target = element.getFirstChild("UML:ModelElement.name");
        if (target == null) {
            target = element.getFirstChild("Foundation.Core.ModelElement.name");
        }
        if (target != null) {
            element.removeChild(target);
            target = factory.createElement(target.getXmlName());
            target.addChild(factory.createText(name));
            element.addChild(target);
            return;
        }
        element.setAttribute("name", name);
    }

    /**
     * Returns the name of an UML element.
     * 
     * @param element the UML element
     * @return the name (or null)
     */
    public static String getName(XmiElement element) {
        String result = element.getAttribute("name");
        if (result != null) {
            return result;
        }
        XmiElement target = element.getFirstChild("UML:ModelElement.name");
        if (target == null) {
            target = element.getFirstChild("Foundation.Core.ModelElement.name");
        }
        if (target != null) {
            return target.getText();
        }
        return null;
    }

    /**
     * Returns all tagged values of a model element.
     * 
     * @param element the model element
     * @return a list of tagged values
     */
    public static List getAllTaggedValues(XmiElement element) {
        Vector result = new Vector();
        Iterator it = element.getChildren().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof XmiElement) {
                XmiElement child = (XmiElement) o;
                String tag = child.getXmlName();
                if (tag.equals("UML:ModelElement.taggedValue") || tag.equals("Foundation.Core.ModelElement.taggedValue")) {
                    Iterator tvs = child.getChildren().iterator();
                    while (tvs.hasNext()) {
                        o = tvs.next();
                        if (o instanceof XmiElement) {
                            result.add(o);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns a tagged value from a model element.
     * 
     * @param element the model element
     * @param tvName the name of the tagged value
     * @return the tagged value or null
     */
    public static XmiElement getTaggedValue(XmiElement element, String tvName) {
        Iterator tvs = getAllTaggedValues(element).iterator();
        while (tvs.hasNext()) {
            XmiElement tv = (XmiElement) tvs.next();
            String name = getName(tv);
            if (name != null && name.equals(tvName)) {
                return tv;
            }
        }
        return null;
    }

    /**
     * Returns the data value of a tagged value.
     * 
     * @param tv the tagged value
     * @return the data value or null
     */
    public static String getTaggedValueData(XmiElement tv) {
        String data = tv.getAttribute("dataValue");
        if (data != null) {
            return data;
        }
        XmiElement child = tv.getFirstChild("UML:TaggedValue.dataValue");
        if (child == null) {
            child = tv.getFirstChild("Foundation.Extension_Mechanisms.TaggedValue.dataValue");
        }
        if (child != null) {
            return child.getText();
        }
        return null;
    }

    private static void removeFromStereotype(XmiElement st, String idref) {
        XmiElement ext = st.getFirstChild("UML:Stereotype.extendedElement");
        if (ext == null) {
            ext = st.getFirstChild("Foundation.Extension_Mechanisms.Stereotype.extendedElement");
            if (ext == null) return;
        }
        Iterator it = ext.getChildren().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof XmiElement) {
                XmiElement ref = (XmiElement) o;
                String x = ref.getXmiIdref();
                if (x != null && x.equals(idref)) {
                    ext.removeChild(ref);
                    if (ext.getChildren().isEmpty()) {
                        ext.getParent().removeChild(ext);
                    }
                    return;
                }
            }
        }
    }

    /**
     * Removes a stereotype from a model element.
     * 
     * @param element the model element
     * @param stName the name of the stereotype
     * @paran map map from xmi.id to element
     */
    public static void removeStereotype(XmiElement element, String stName, Map map) {
        String id = element.getXmiId();
        String st_list = element.getAttribute("stereotype");
        String new_list = null;
        if (st_list != null) {
            XmiElement the_stereotype = null;
            StringTokenizer st = new StringTokenizer(st_list);
            while (st.hasMoreTokens()) {
                String idref = st.nextToken();
                XmiElement target = (XmiElement) map.get(idref);
                if (target == null) {
                    throw new NullPointerException("wrong xmi.idref: " + idref);
                }
                String st_name = getName(target);
                if (st_name != null && st_name.equals(stName)) {
                    the_stereotype = target;
                } else {
                    if (new_list == null) new_list = idref; else new_list += (" " + idref);
                }
            }
            if (the_stereotype != null) {
                if (new_list == null) element.removeAttribute("stereotype"); else element.setAttribute("stereotype", new_list);
                if (id != null) {
                    removeFromStereotype(the_stereotype, id);
                }
                return;
            }
        }
        Iterator children = element.getChildren().iterator();
        while (children.hasNext()) {
            Object o = children.next();
            if (o instanceof XmiElement) {
                XmiElement child = (XmiElement) o;
                String tagName = child.getXmlName();
                if (tagName.equals("UML:ModelElement.stereotype") || tagName.equals("Foundation.Core.ModelElement.stereotype")) {
                    Iterator it = child.getChildren().iterator();
                    while (it.hasNext()) {
                        o = it.next();
                        if (o instanceof XmiElement) {
                            XmiElement stRef = (XmiElement) o;
                            String idref = stRef.getXmiIdref();
                            if (idref != null) {
                                XmiElement target = (XmiElement) map.get(idref);
                                if (target == null) {
                                    throw new NullPointerException("wrong xmi.idref: " + idref);
                                }
                                String st_name = getName(target);
                                if (st_name != null && st_name.equals(stName)) {
                                    child.removeChild(stRef);
                                    if (child.getChildren().isEmpty()) {
                                        element.removeChild(child);
                                    }
                                    if (id != null) {
                                        removeFromStereotype(target, id);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Retruns a list with the names of all stereotypes of a model element.
     * 
     * @param element the model element
     * @param map map from xmi.id to element
     * @return the list of stereotype names
     */
    public static List getAllStereotypeNames(XmiElement element, Map map) {
        Vector result = new Vector();
        String st_list = element.getAttribute("stereotype");
        if (st_list != null) {
            StringTokenizer st = new StringTokenizer(st_list);
            while (st.hasMoreTokens()) {
                String idref = st.nextToken();
                XmiElement target = (XmiElement) map.get(idref);
                if (target == null) {
                    throw new NullPointerException("wrong xmi.idref: " + idref);
                }
                result.add(getName(target));
            }
        }
        Iterator children = element.getChildren().iterator();
        while (children.hasNext()) {
            Object o = children.next();
            if (o instanceof XmiElement) {
                XmiElement child = (XmiElement) o;
                String tagName = child.getXmlName();
                if (tagName.equals("UML:ModelElement.stereotype") || tagName.equals("Foundation.Core.ModelElement.stereotype")) {
                    Iterator it = child.getChildren().iterator();
                    while (it.hasNext()) {
                        o = it.next();
                        if (o instanceof XmiElement) {
                            String idref = ((XmiElement) o).getXmiIdref();
                            if (idref != null) {
                                XmiElement target = (XmiElement) map.get(idref);
                                if (target == null) {
                                    throw new NullPointerException("wrong xmi.idref: " + idref);
                                }
                                result.add(getName(target));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Checks if a model element has the given stereotype.
     * 
     * @param element the model element
     * @param stName the name of the stereotype
     * @paran map map from xmi.id to element
     */
    public static boolean hasStereotype(XmiElement element, String stName, Map map) {
        String st_list = element.getAttribute("stereotype");
        if (st_list != null) {
            StringTokenizer st = new StringTokenizer(st_list);
            while (st.hasMoreTokens()) {
                String idref = st.nextToken();
                XmiElement target = (XmiElement) map.get(idref);
                if (target == null) {
                    throw new NullPointerException("wrong xmi.idref: " + idref);
                }
                if (getName(target).equals(stName)) {
                    return true;
                }
            }
        }
        Iterator children = element.getChildren().iterator();
        while (children.hasNext()) {
            Object o = children.next();
            if (o instanceof XmiElement) {
                XmiElement child = (XmiElement) o;
                String tagName = child.getXmlName();
                if (tagName.equals("UML:ModelElement.stereotype") || tagName.equals("Foundation.Core.ModelElement.stereotype")) {
                    Iterator it = child.getChildren().iterator();
                    while (it.hasNext()) {
                        o = it.next();
                        if (o instanceof XmiElement) {
                            String idref = ((XmiElement) o).getXmiIdref();
                            if (idref != null) {
                                XmiElement target = (XmiElement) map.get(idref);
                                if (target == null) {
                                    throw new NullPointerException("wrong xmi.idref: " + idref);
                                }
                                if (getName(target).equals(stName)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
