package net.andycole.xmiwriter;

import org.netbeans.api.xmi.XMIReferenceProvider;
import org.netbeans.lib.jmi.xmi.OutputConfig;
import org.netbeans.lib.jmi.xmi.XmiConstants;
import org.netbeans.lib.jmi.xmi.WriterBase;
import javax.jmi.reflect.*;
import javax.jmi.model.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.lib.jmi.util.Logger;

/**
 * Overrides writing of XMI version 1.x files to comply with the format
 * Netbeans expects for its .etd files.
 * 
 * @author Andrew Cole
 */
public class NetbeansXmiWriter extends WriterBase {

    public NetbeansXmiWriter(OutputConfig config) {
        super(config);
    }

    protected void writeIfNonAggregatedReference(RefObject obj, Reference ref) {
        AggregationKind kind = ref.getExposedEnd().getAggregation();
        boolean isComposite = AggregationKindEnum.COMPOSITE.equals(kind);
        if (isComposite) return;
        Object temp = obj.refGetValue(ref);
        if (temp == null) return;
        Collection values;
        if (isMultivalued(ref)) values = (Collection) temp; else {
            values = new LinkedList();
            values.add(temp);
        }
        Iterator iter;
        if (collectionWriting) {
            Collection cValues = new LinkedList();
            iter = values.iterator();
            while (iter.hasNext()) {
                RefObject referencedObject = (RefObject) iter.next();
                if (isInClosure(referencedObject)) {
                    cValues.add(referencedObject);
                }
            }
            values = cValues;
        }
        if (values.isEmpty()) return;
        String refName = elementName(ref);
        writeInlineInstanceRefs(refName, values);
    }

    protected void writeIfAggregatedReference(RefObject obj, Reference ref) {
        AggregationKind kind = ref.getReferencedEnd().getAggregation();
        if (AggregationKindEnum.COMPOSITE.equals(kind)) return;
        kind = ref.getExposedEnd().getAggregation();
        boolean isComposite = AggregationKindEnum.COMPOSITE.equals(kind);
        if (!isComposite) return;
        Object temp = obj.refGetValue(ref);
        if (temp == null) return;
        Collection values;
        if (isMultivalued(ref)) values = (Collection) temp; else {
            values = new LinkedList();
            values.add(temp);
        }
        Iterator iter;
        if (collectionWriting) {
            Collection cValues = new LinkedList();
            iter = values.iterator();
            while (iter.hasNext()) {
                RefObject referencedObject = (RefObject) iter.next();
                if (isInClosure(referencedObject)) {
                    cValues.add(referencedObject);
                }
            }
            values = cValues;
        }
        if (values.isEmpty()) return;
        String name = qualifiedName(ref);
        startElement(name);
        iter = values.iterator();
        while (iter.hasNext()) {
            RefObject endValue = (RefObject) iter.next();
            writeInstance(endValue, false);
        }
        endElement(name);
    }

    private void writeInlineInstanceRefs(String refName, Collection values) {
        String refList = "";
        Iterator iter = values.iterator();
        RefObject obj = null;
        StringBuilder buffer = new StringBuilder();
        if (iter.hasNext()) {
            obj = (RefObject) iter.next();
            buffer.append(getAttributeValFromRef(obj));
        }
        while (iter.hasNext()) {
            obj = (RefObject) iter.next();
            buffer.append(" ");
            buffer.append(getAttributeValFromRef(obj));
        }
        refList = buffer.toString();
        addAttribute(refName, refList);
    }

    private String getAttributeValFromRef(RefObject obj) {
        XMIReferenceProvider.XMIReference xmiRef = provider.getReference(obj);
        String xmiId = xmiRef.getXmiId();
        String systemId = xmiRef.getSystemId();
        if ((systemId != null) && (thisSystemId != null) && (thisSystemId.equals(systemId))) systemId = null;
        if (systemId == null) return xmiId;
        return systemId + HREF_DELIMITER + xmiId;
    }

    @Override
    protected void writeNamespaces() {
        addAttribute("xmlns:UML", "omg.org/UML/1.4");
    }

    @Override
    protected void writeInstance(RefObject obj, boolean isTop) {
        RefClass proxy = obj.refClass();
        ModelElement element = (ModelElement) obj.refMetaObject();
        String name = qualifiedName(element);
        XMIReferenceProvider.XMIReference xmiRef = provider.getReference(obj);
        String xmiId = xmiRef.getXmiId();
        String systemId = xmiRef.getSystemId();
        if ((systemId != null) && (thisSystemId != null) && (thisSystemId.equals(systemId))) systemId = null;
        markWritten(obj);
        if (systemId != null) {
            if (!isTop) {
                startElement(name);
                addAttribute(XmiConstants.XMI_HREF, systemId + HREF_DELIMITER + xmiId);
                endElement(name);
            }
            collectLightOutermosts(obj, proxy);
            return;
        }
        startElement(name);
        addAttribute(XmiConstants.XMI_ID, xmiId);
        Iterator refs = references((MofClass) proxy.refMetaObject()).iterator();
        while (refs.hasNext()) {
            Reference ref = (Reference) refs.next();
            writeIfNonAggregatedReference(obj, ref);
        }
        Iterator attrs = instanceAttributes((MofClass) proxy.refMetaObject()).iterator();
        List attrsInContent = new LinkedList();
        while (attrs.hasNext()) {
            Attribute attr = (Attribute) attrs.next();
            if (!VisibilityKindEnum.PUBLIC_VIS.equals(attr.getVisibility())) continue;
            boolean isMultivalued = isMultivalued(attr);
            Object value;
            try {
                value = obj.refGetValue(attr);
            } catch (Exception e) {
                Logger.getDefault().annotate(e, ((ModelElement) obj.refMetaObject()).getName() + " " + attr.getName());
                Logger.getDefault().notify(e);
                value = Boolean.FALSE;
            }
            Object valueToWrite = value;
            if (value == null) continue;
            if (isMultivalued) {
                Collection col = (Collection) value;
                if (col.size() > 0) {
                    attrsInContent.add(attr);
                }
                continue;
            }
            Classifier type = getType(attr);
            if (!(type instanceof PrimitiveType) && !(type instanceof EnumerationType)) {
                attrsInContent.add(attr);
            } else writeValueInAttr(attr, valueToWrite);
        }
        Iterator iter = attrsInContent.iterator();
        while (iter.hasNext()) {
            Attribute attr = (Attribute) iter.next();
            writeValueInContent(attr, obj.refGetValue(attr));
        }
        refs = references((MofClass) proxy.refMetaObject()).iterator();
        while (refs.hasNext()) {
            Reference ref = (Reference) refs.next();
            writeIfAggregatedReference(obj, ref);
        }
        endElement(name);
    }
}
