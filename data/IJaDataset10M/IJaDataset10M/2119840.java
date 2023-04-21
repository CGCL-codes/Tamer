package com.metanology.mde.core.xmlserializers;

import java.util.*;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.metanology.mde.core.metaModel.*;
import com.metanology.mde.utils.*;
import com.metanology.mde.core.metaModel.commonbehavior.*;
import com.metanology.mde.core.metaModel.datatypes.*;
import com.metanology.mde.core.metaModel.statemachines.*;

/**
 * Serialize an instanceof CreateAction into XML file
 *
 * @since 2.0
 */
public class CreateActionCollectionSerializer implements Serializer {

    private CreateActionCollection obj;

    private String oid;

    private Element element;

    public CreateActionCollectionSerializer(CreateActionCollection obj) {
        this.obj = obj;
        this.element = null;
        this.oid = null;
    }

    public CreateActionCollectionSerializer(CreateActionCollection obj, Element element, String oid) {
        this.obj = obj;
        this.element = element;
        this.oid = oid;
    }

    public void setObject(Object o) {
        if (o instanceof CreateActionCollection) {
            obj = (CreateActionCollection) o;
        }
    }

    public void addToObjectRepository(XmlWriteObjectRepository objRep) {
        if (objRep.hasObject(obj)) return;
        Element objData = objRep.createElement("object");
        if (objData != null) {
            objRep.addObject(obj, objData);
            objData.setAttribute("oid", objRep.getOid(obj));
            objData.setAttribute("classname", "CreateActionCollection");
            for (java.util.Iterator i = obj.iterator(); i.hasNext(); ) {
                CreateAction item = (CreateAction) i.next();
                CreateActionSerializer s = new CreateActionSerializer(item);
                s.addToObjectRepository(objRep);
                Element itemEl = objRep.createElement("objectref");
                Text val = objRep.createTextNode(String.valueOf(objRep.getOid(item)));
                itemEl.appendChild(val);
                objData.appendChild(itemEl);
            }
        }
    }

    public Object createFromObjectRepository(XmlReadObjectRepository objRep) {
        if (oid != null) obj = (CreateActionCollection) objRep.findObject(oid);
        if (obj != null) return obj;
        if (element == null) element = objRep.findElement(oid);
        if (element != null) {
            obj = new CreateActionCollection();
            if (obj instanceof Identifiable) {
                Identifiable idObj = (Identifiable) obj;
                idObj.setObjId(oid);
            }
            objRep.addObject(oid, obj);
            objRep.getSerializers().add(this);
        }
        return obj;
    }

    public void loadFromElement(XmlReadObjectRepository objRep) {
        if (element == null && oid != null) {
            element = objRep.findElement(oid);
        }
        if (element != null) {
            NodeList nodes = element.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                if (!(n instanceof Element)) continue;
                Element el = (Element) n;
                if (el.getTagName().equalsIgnoreCase("objectref")) {
                    String itemOid = (String) el.getFirstChild().getNodeValue();
                    CreateActionSerializer s = new CreateActionSerializer(null, null, itemOid);
                    Object o = s.createFromObjectRepository(objRep);
                    if (o != null) {
                        ((Collection) obj).add(o);
                    }
                }
            }
        }
    }
}
