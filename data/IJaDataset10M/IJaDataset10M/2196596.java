package org.ncgr.cmtv.serialization;

import org.ncgr.cmtv.datamodel.*;
import org.ncgr.cmtv.datamodel.impl.ConsensusMap;
import java.io.*;
import java.util.*;
import org.apache.xml.serialize.*;
import org.xml.sax.helpers.*;
import org.ncgr.isys.objectmodel.LinearObject;
import org.ncgr.isys.objectmodel.SequenceText;
import org.ncgr.isys.objectmodel.OffsetLinearObject;
import org.ncgr.isys.objectmodel.LinearObjectPosition;
import org.ncgr.isys.objectmodel.ComplementStrandPosition;
import org.ncgr.isys.objectmodel.LinearIntervalPosition;
import org.ncgr.isys.objectmodel.LinearPointPosition;
import org.ncgr.isys.objectmodel.ComplexLinearObjectPosition;
import org.ncgr.isys.objectmodel.LinearlyLocatedObject;
import org.ncgr.isys.objectmodel.PairwiseAlignment;
import org.ncgr.isys.objectmodel.LinearObjectDistribution;
import org.ncgr.isys.system.*;
import org.ncgr.util.MultiMap;
import org.ncgr.util.MultiHashMapHashSet;

public class MapModelXMLWriter {

    private XMLSerializer serializer;

    public MapModelXMLWriter() {
        OutputFormat of = new OutputFormat();
        of.setIndenting(true);
        of.setPreserveSpace(true);
        serializer = new XMLSerializer(of);
    }

    static final AttributesImpl EMPTY_ATTRIBUTES = new AttributesImpl();

    public void setWriter(Writer w) throws IOException {
        serializer.setOutputCharStream(new BufferedWriter(w));
        serializer.asContentHandler();
    }

    public void startDocument() throws org.xml.sax.SAXException {
        referencedObjs.clear();
        objects2Ids.clear();
        lastIdAssigned = 0;
        serializedObjs.clear();
        serializer.startElement(null, null, "root", EMPTY_ATTRIBUTES);
    }

    public void endDocument() throws org.xml.sax.SAXException {
        while (referencedObjs.size() > 0) {
            MultiHashMapHashSet ros = new MultiHashMapHashSet(referencedObjs.size());
            for (Iterator itr = referencedObjs.keySet().iterator(); itr.hasNext(); ) {
                Object key = itr.next();
                ros.putAll(key, referencedObjs.getAll(key));
            }
            for (Iterator itr = ros.keySet().iterator(); itr.hasNext(); ) {
                Object refd = itr.next();
                for (Iterator itr2 = ros.getAll(refd).iterator(); itr2.hasNext(); ) {
                    Class c = (Class) itr2.next();
                    if (c != null) {
                        if (c.equals(IsysObject.class)) writeIsysObject((IsysObject) refd); else if (c.equals(IsysAttribute.class)) writeIsysAttribute((IsysAttribute) refd); else if (c.equals(LinearObject.class)) writeMap((LinearObject) refd, null); else if (c.equals(LinearlyLocatedObject.class)) writeMappedObject((LinearlyLocatedObject) refd, null);
                    } else {
                        if (refd instanceof Annotation) writeAnnotation((Annotation) refd); else if (refd instanceof String) writeMappable(refd);
                    }
                }
            }
        }
        serializer.endElement(null, null, "root");
    }

    void objectSerialized(Object o) {
        objectSerialized(o, null);
    }

    void objectSerialized(Object o, Class c) {
        serializedObjs.put(o, c);
        referencedObjs.remove(o, c);
        Collection col = referencedObjs.getAll(o);
        if (col == null || col.size() == 0) referencedObjs.remove(o);
    }

    boolean isObjectSerialized(Object o, Class c) {
        Collection col = serializedObjs.getAll(o);
        if (col == null) return false;
        return col.contains(c);
    }

    MultiMap serializedObjs = new MultiHashMapHashSet();

    MultiMap referencedObjs = new MultiHashMapHashSet();

    HashMap objects2Ids = new HashMap();

    int lastIdAssigned = 0;

    String getIdForReferencedObject(Object o) {
        return getIdForReferencedObject(o, null);
    }

    String getIdForReferencedObject(Object o, Class c) {
        Collection col = serializedObjs.getAll(o);
        if (col == null || !col.contains(c)) referencedObjs.put(o, c);
        return getIdForObject(o, c);
    }

    String getIdForObject(Object o) {
        return getIdForObject(o, null);
    }

    String getIdForObject(Object o, Class c) {
        Object retval = objects2Ids.get(o);
        if (retval == null) {
            retval = new Integer(lastIdAssigned++);
            objects2Ids.put(o, retval);
        }
        if (c == null) return retval.toString();
        return c.getName() + ":" + retval.toString();
    }

    public void writeMapModel(MapModel mapModel) throws org.xml.sax.SAXException {
        writeMapsForMapModel(mapModel.getMapsWithMappedObjects().keySet(), mapModel);
    }

    public void writeMapsForMapModel(Collection maps, MapModel mapModel) throws org.xml.sax.SAXException {
        serializer.startElement(null, null, "MapModel", EMPTY_ATTRIBUTES);
        for (Iterator itr = maps.iterator(); itr.hasNext(); ) writeMap((LinearObject) itr.next(), mapModel);
        serializer.endElement(null, null, "MapModel");
    }

    void writeMap(LinearObject map, MapModel mapModel) throws org.xml.sax.SAXException {
        String id = getIdForObject(map, LinearObject.class);
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute(null, null, "id", "ID", id);
        attrs.addAttribute(null, null, "length", "CDATA", map.getLength().toString());
        attrs.addAttribute(null, null, "units", "CDATA", map.getUnits());
        if (map instanceof OffsetLinearObject) attrs.addAttribute(null, null, "offset", "CDATA", ((OffsetLinearObject) map).getOffset().toString());
        if (map instanceof SequenceText) attrs.addAttribute(null, null, "seq_text", "CDATA", ((SequenceText) map).getSequenceText());
        serializer.startElement("", null, "Map", attrs);
        if (mapModel != null) {
            for (Iterator itr = mapModel.getMappedObjectsForMap(map).iterator(); itr.hasNext(); ) writeMappedObject((LinearlyLocatedObject) itr.next(), mapModel);
            Collection annos = mapModel.getAnnotationsForMap(map);
            if (annos != null) {
                for (Iterator itr = annos.iterator(); itr.hasNext(); ) {
                    Annotation anno = (Annotation) itr.next();
                    String annoId = getIdForReferencedObject(anno);
                    attrs.clear();
                    attrs.addAttribute(null, null, "anno_id", "IDREF", annoId);
                    serializer.startElement(null, null, "AnnotationLink", attrs);
                    serializer.endElement(null, null, "AnnotationLink");
                }
            }
            Collection objReps = mapModel.getObjectRepresentationsForMap(map);
            if (objReps != null) {
                for (Iterator itr = objReps.iterator(); itr.hasNext(); ) {
                    Object objRep = itr.next();
                    Class c = null;
                    if (objRep instanceof IsysObject) c = IsysObject.class;
                    String objRepId = getIdForReferencedObject(objRep, c);
                    attrs.clear();
                    attrs.addAttribute(null, null, "objrep_id", "IDREF", objRepId);
                    serializer.startElement(null, null, "ObjectLink", attrs);
                    serializer.endElement(null, null, "ObjectLink");
                }
            }
            String mapString = null;
            for (Iterator itr = mapModel.getObjectRepresentationsForMap(map).iterator(); itr.hasNext(); ) {
                Object o = itr.next();
                if (o instanceof String) {
                    mapString = (String) o;
                    break;
                }
            }
            if (mapString != null) {
                char[] charData = mapString.toCharArray();
                serializer.characters(charData, 0, charData.length);
            }
        }
        serializer.endElement(null, null, "Map");
        objectSerialized(map, LinearObject.class);
        if (map instanceof OffsetLinearObject) objectSerialized(map, OffsetLinearObject.class);
        if (map instanceof SequenceText) objectSerialized(map, SequenceText.class);
    }

    void writeMappedObject(LinearlyLocatedObject mappedObject, MapModel mapModel) throws org.xml.sax.SAXException {
        String id = getIdForObject(mappedObject, LinearlyLocatedObject.class);
        Object mappable = mappedObject.getLocatedObject();
        boolean isAnchor = false;
        LinearObject map = mappedObject.getPositionOnLinearObject().getLinearObject();
        if (map instanceof ConsensusMap) {
            ConsensusMap con = (ConsensusMap) map;
            if (con.isAnchorMember(mappedObject)) {
                isAnchor = true;
            }
        }
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute(null, null, "id", "ID", id);
        if (isAnchor) {
            attrs.addAttribute(null, null, "anchor", "CDATA", "1");
        }
        String elementName = "MappedObject";
        if (mappedObject instanceof PairwiseAlignment) {
            elementName = "Alignment";
            attrs.addAttribute(null, null, "aligned_id", "CDATA", getIdForReferencedObject(((PairwiseAlignment) mappedObject).getLocatedObject(), LinearObject.class));
        } else if (mappedObject instanceof LinearObjectDistribution) {
            elementName = "Distribution";
            attrs.addAttribute(null, null, "min_range_value", "CDATA", ((LinearObjectDistribution) mappedObject).getMinimumValueInRange().toString());
            attrs.addAttribute(null, null, "max_range_value", "CDATA", ((LinearObjectDistribution) mappedObject).getMaximumValueInRange().toString());
        }
        serializer.startElement(null, null, elementName, attrs);
        writeMappedObjectPosition(mappedObject.getPositionOnLinearObject());
        if (mappedObject instanceof PairwiseAlignment) writeMappedObjectPosition(((PairwiseAlignment) mappedObject).getPositionOnLocatedObject(), true); else if (mappedObject instanceof LinearObjectDistribution) {
            Map dist = ((LinearObjectDistribution) mappedObject).getDistributionValuesByPosition();
            for (Iterator itr = dist.entrySet().iterator(); itr.hasNext(); ) {
                Map.Entry me = (Map.Entry) itr.next();
                LinearPointPosition p = (LinearPointPosition) me.getKey();
                Number v = (Number) me.getValue();
                attrs.clear();
                attrs.addAttribute(null, null, "coordinate", "CDATA", p.getCoordinate().toString());
                attrs.addAttribute(null, null, "value", "CDATA", v.toString());
                serializer.startElement(null, null, "DistributionValue", attrs);
                serializer.endElement(null, null, "DistributionValue");
            }
        }
        if (mapModel != null) {
            Collection annos = mapModel.getAnnotationsForMappedObject(mappedObject);
            if (annos != null) {
                for (Iterator itr = annos.iterator(); itr.hasNext(); ) {
                    Annotation anno = (Annotation) itr.next();
                    String annoId = getIdForReferencedObject(anno);
                    attrs.clear();
                    attrs.addAttribute(null, null, "anno_id", "IDREF", annoId);
                    serializer.startElement(null, null, "AnnotationLink", attrs);
                    serializer.endElement(null, null, "AnnotationLink");
                }
            }
            Collection objReps = mapModel.getObjectRepresentationsForMappedObject(mappedObject);
            if (objReps != null) {
                for (Iterator itr = objReps.iterator(); itr.hasNext(); ) {
                    Object objRep = itr.next();
                    Class c = null;
                    if (objRep instanceof IsysObject) c = IsysObject.class;
                    String objRepId = getIdForReferencedObject(objRep, c);
                    attrs.clear();
                    attrs.addAttribute(null, null, "objrep_id", "IDREF", objRepId);
                    serializer.startElement(null, null, "ObjectLink", attrs);
                    serializer.endElement(null, null, "ObjectLink");
                }
            }
        }
        serializer.endElement(null, null, elementName);
        objectSerialized(mappedObject, LinearlyLocatedObject.class);
        if (mappedObject instanceof PairwiseAlignment) objectSerialized(mappedObject, PairwiseAlignment.class);
        if (mappedObject instanceof LinearObjectDistribution) objectSerialized(mappedObject, LinearObjectDistribution.class);
    }

    static final Class[] POSITION_TYPES = new Class[] { LinearPointPosition.class, LinearIntervalPosition.class, ComplexLinearObjectPosition.class };

    void writeMappedObjectPosition(LinearObjectPosition position) throws org.xml.sax.SAXException {
        writeMappedObjectPosition(position, false);
    }

    void writeMappedObjectPosition(LinearObjectPosition position, boolean alignedPosition) throws org.xml.sax.SAXException {
        AttributesImpl attrs = new AttributesImpl();
        if (alignedPosition == true) serializer.startElement(null, null, "Aligned", attrs);
        if (position instanceof ComplementStrandPosition) attrs.addAttribute(null, null, "complement", "CDATA", "1");
        if (position instanceof LinearPointPosition) attrs.addAttribute(null, null, "coordinate", "CDATA", ((LinearPointPosition) position).getCoordinate().toString());
        if (position instanceof LinearIntervalPosition) {
            LinearIntervalPosition lip = (LinearIntervalPosition) position;
            attrs.addAttribute(null, null, "start", "CDATA", lip.getStart().toString());
            attrs.addAttribute(null, null, "end", "CDATA", lip.getEnd().toString());
        }
        serializer.startElement(null, null, "Position", attrs);
        if (position instanceof ComplexLinearObjectPosition) {
            LinearObjectPosition[] positions = ((ComplexLinearObjectPosition) position).getComponentPositions();
            for (int i = 0; i < positions.length; i++) writeMappedObjectPosition(positions[i]);
        }
        serializer.endElement(null, null, "Position");
        if (alignedPosition == true) serializer.endElement(null, null, "Aligned");
        objectSerialized(position);
    }

    void writeMappable(Object mappable) throws org.xml.sax.SAXException {
        AttributesImpl attrs = new AttributesImpl();
        String id = getIdForObject(mappable);
        attrs.addAttribute(null, null, "id", "ID", id);
        serializer.startElement(null, null, "Mappable", attrs);
        char[] charData = mappable.toString().toCharArray();
        serializer.characters(charData, 0, charData.length);
        serializer.endElement(null, null, "Mappable");
        objectSerialized(mappable);
    }

    public void writeMapComparisonForMaps(MapComparisonModel mcm, Collection maps, MapModel mapModel) throws org.xml.sax.SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute(null, null, "desc", "CDATA", mcm.getDescription());
        serializer.startElement(null, null, "MapComparison", attrs);
        LinearlyLocatedObject[][] correspondences = mcm.getMapCorrespondencesForMaps((LinearObject[]) maps.toArray(new LinearObject[0]));
        for (int i = 0; i < correspondences.length; i++) writeCorrespondence(correspondences[i]);
        serializer.endElement(null, null, "MapComparison");
    }

    void writeCorrespondence(LinearlyLocatedObject[] correspondence) throws org.xml.sax.SAXException {
        AttributesImpl attrs = new AttributesImpl();
        serializer.startElement(null, null, "Correspondence", attrs);
        for (int i = 0; i < correspondence.length; i++) {
            LinearlyLocatedObject mo = correspondence[i];
            attrs.clear();
            String id = getIdForReferencedObject(mo, LinearlyLocatedObject.class);
            attrs.addAttribute(null, null, "corr_obj_id", "IDREF", id);
            serializer.startElement(null, null, "CorrespondenceElement", attrs);
            serializer.endElement(null, null, "CorrespondenceElement");
        }
        serializer.endElement(null, null, "Correspondence");
    }

    void writeAnnotation(Annotation anno) throws org.xml.sax.SAXException {
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute(null, null, "id", "ID", getIdForObject(anno));
        attrs.addAttribute(null, null, "author", "CDATA", anno.getAnnotationAuthor());
        attrs.addAttribute(null, null, "time", "CDATA", "" + anno.getAnnotationTime());
        serializer.startElement(null, null, "Annotation", attrs);
        char[] charData = anno.getAnnotationText().toCharArray();
        serializer.characters(charData, 0, charData.length);
        serializer.endElement(null, null, "Annotation");
        objectSerialized(anno);
    }

    void writeIsysObject(IsysObject io) throws org.xml.sax.SAXException {
        Collection c = serializedObjs.getAll(io);
        if (c != null && c.contains(IsysObject.class)) {
            objectSerialized(io, IsysObject.class);
            return;
        }
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute(null, null, "id", "ID", getIdForObject(io, IsysObject.class));
        serializer.startElement(null, null, "IsysObject", attrs);
        for (java.util.Iterator itr = io.getAttribute(IsysAttribute.class).iterator(); itr.hasNext(); ) {
            attrs.clear();
            IsysAttribute ia = (IsysAttribute) itr.next();
            if (ia == null) continue;
            attrs.addAttribute(null, null, "ia_id", "IDREF", getIdForReferencedObject(ia, IsysAttribute.class));
            serializer.startElement(null, null, "IsysAttributeLink", attrs);
            serializer.endElement(null, null, "IsysAttributeLink");
        }
        attrs.clear();
        Class[] equiAttrs = io.getEquivalenceAttributes();
        for (int i = 0; i < equiAttrs.length; i++) {
            serializer.startElement(null, null, "EquiAttr", attrs);
            char[] charData = equiAttrs[i].getName().toCharArray();
            serializer.characters(charData, 0, charData.length);
            serializer.endElement(null, null, "EquiAttr");
        }
        serializer.endElement(null, null, "IsysObject");
        objectSerialized(io, IsysObject.class);
    }

    HashMap mthd2result = new HashMap();

    void writeIsysAttribute(IsysAttribute ia) throws org.xml.sax.SAXException {
        AttributesImpl attrs = new AttributesImpl();
        mthd2result.clear();
        attrs.addAttribute(null, null, "id", "ID", getIdForObject(ia, IsysAttribute.class));
        Class[] intfs = org.ncgr.isys.system.DataModelRegistry.getAttrsForClass(ia.getClass());
        for (int i = 0; i < intfs.length; i++) {
            Class intf = intfs[i];
            if (intf.equals(org.ncgr.isys.service.AnnotatedLinearObject.class) || intf.equals(org.ncgr.isys.service.Annotation.class)) continue;
            if (!IsysAttribute.class.isAssignableFrom(intf)) continue;
            if (isObjectSerialized(ia, intf)) continue;
            java.lang.reflect.Method[] mthds = intf.getMethods();
            for (int j = 0; j < mthds.length; j++) {
                String name = mthds[j].getName();
                if (name.startsWith("get")) name = name.substring(3);
                try {
                    mthd2result.put(name, mthds[j].invoke(ia, new Object[0]).toString());
                } catch (Exception e) {
                }
            }
        }
        for (Iterator itr = mthd2result.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry me = (Map.Entry) itr.next();
            attrs.addAttribute(null, null, (String) me.getKey(), "CDATA", (String) me.getValue());
        }
        serializer.startElement(null, null, "IsysAttribute", attrs);
        serializer.endElement(null, null, "IsysAttribute");
        objectSerialized(ia, IsysAttribute.class);
    }
}
