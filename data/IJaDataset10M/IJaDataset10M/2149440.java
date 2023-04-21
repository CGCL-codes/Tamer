package org.jaffa.tools.patternmetaengine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.log4j.Logger;
import org.jaffa.util.StringHelper;
import org.jaffa.datatypes.Defaults;
import org.jaffa.tools.patternmetaengine.domain.ApplicationBuilder;
import org.jaffa.tools.patternmetaengine.domain.Module;
import org.jaffa.patterns.library.domain_creator_1_1.domain.Field;
import org.jaffa.patterns.library.domain_creator_1_1.domain.Relationship;
import org.jaffa.patterns.library.domain_creator_1_1.domain.RelationshipField;
import org.jaffa.patterns.library.object_viewer_meta_1_0.domain.*;

/** Create the meta data for an Object Viewer Pattern, based on a specified
 * Domain Object.
 *
 * @author  PaulE
 * @version 1.0
 */
public class BuildObjectViewer_1 implements IBuilder {

    /** Set up Logging for Log4J */
    private static Logger log = Logger.getLogger(BuildObjectViewer.class);

    private static final String SCHEMA = "patterns/library/object_viewer_1_0/objectViewerMeta_1_0.xsd";

    private ApplicationBuilder m_app = null;

    private Module m_module = null;

    private org.jaffa.patterns.library.domain_creator_1_1.domain.Root m_domain = null;

    private DomainObjectHelper m_doList;

    private ObjectViewerMeta m_viewer = null;

    private List m_upDomain = null;

    private List m_upRel = null;

    private Properties m_labels = null;

    private ComponentRegistry m_compReg = null;

    /** Creates a new instance of BuildObjectViewer */
    public BuildObjectViewer_1(ApplicationBuilder app, ComponentRegistry comps, Module module, org.jaffa.patterns.library.domain_creator_1_1.domain.Root domain, DomainObjectHelper doList, Properties labels) {
        log.debug("Create Viewer For - " + domain.getDomainObject());
        m_compReg = comps;
        m_app = app;
        m_module = module;
        m_domain = domain;
        m_doList = doList;
        m_labels = labels;
    }

    /** Causes the meta data object to be build ready for saving.
     * @param fullPackage If true then the .applications. and .modules. package names
     * are used. If this is false, these are ommited for a much more condensed package
     * naming convention
     */
    public void buildPhase1(boolean fullPackage) throws Exception {
        initUpEntities();
        ObjectFactory objFactory = new ObjectFactory();
        try {
            m_viewer = objFactory.createObjectViewerMeta();
            m_viewer.setPatternTemplate("patterns/library/object_viewer_1_0/ObjectViewerPattern.xml");
            m_viewer.setApplication(m_app.getApplicationName());
            m_viewer.setModule(m_module.getName());
            m_viewer.setComponent(m_domain.getDomainObject() + "Viewer");
            StringBuffer sb = new StringBuffer();
            sb.append(m_app.getPackagePrefix());
            sb.append(".");
            if (fullPackage) {
                sb.append("applications.");
            }
            sb.append(m_app.getApplicationName());
            sb.append(".");
            if (fullPackage) {
                sb.append("modules.");
            }
            sb.append(m_module.getName());
            m_viewer.setBasePackage(sb.toString().toLowerCase());
            m_viewer.setDomainObject(m_domain.getDomainObject());
            m_viewer.setDomainPackage(m_domain.getDomainPackage());
            String labelDomain = "label." + StringHelper.getUpper1(m_app.getApplicationName()) + "." + StringHelper.getUpper1(m_module.getName()) + "." + StringHelper.getUpper1(m_domain.getDomainObject());
            String labelId = "title." + StringHelper.getUpper1(m_app.getApplicationName()) + "." + StringHelper.getUpper1(m_module.getName()) + "." + StringHelper.getUpper1(m_domain.getDomainObject()) + "Viewer.";
            m_viewer.setTitle(labelId + "view");
            m_labels.put(m_viewer.getTitle(), "[" + labelDomain + "] Details");
            List fields = m_domain.getFields().getField();
            if (fields == null || fields.isEmpty()) {
                log.error("Domain Object " + m_domain.getDomainObject() + " has no fields, this is needed to build a valid viewer");
            } else {
                CriteriaFields cfields = objFactory.createCriteriaFields();
                m_viewer.setCriteriaFields(cfields);
                for (Iterator it1 = fields.iterator(); it1.hasNext(); ) {
                    Field fld = (Field) it1.next();
                    if (fld.getPrimaryKey() == org.jaffa.patterns.library.domain_creator_1_1.domain.Boolean.T) {
                        CriteriaField cfld = objFactory.createCriteriaField();
                        cfields.getCriteriaField().add(cfld);
                        cfld.setName(reservedName(fld.getName()));
                        String dt = Defaults.getDataType(fld.getDataType());
                        if (dt == null) {
                            throw new Exception("Can't Translate Java Class " + fld.getDataType() + " to a supported Data Type");
                        }
                        cfld.setDataType(dt);
                        cfld.setDomainField(fld.getName());
                    }
                }
                ResultsFields rfields = objFactory.createResultsFields();
                m_viewer.setResultsFields(rfields);
                for (Iterator it1 = fields.iterator(); it1.hasNext(); ) {
                    Field fld = (Field) it1.next();
                    ResultsField rfld = objFactory.createResultsField();
                    rfields.getResultsField().add(rfld);
                    rfld.setName(reservedName(fld.getName()));
                    String dt = Defaults.getDataType(fld.getDataType());
                    if (dt == null) {
                        throw new Exception("Can't Translate Java Class " + fld.getDataType() + " to a supported Data Type");
                    }
                    rfld.setDataType(dt);
                    rfld.setDisplay(true);
                    rfld.setLabel("[" + labelDomain + "." + fld.getName() + "]");
                    rfld.setWidth("300px");
                    rfld.setDomainField(fld.getName());
                }
                if (m_domain.getRelationships() != null) {
                    List rels = m_domain.getRelationships().getRelationship();
                    if (rels != null) {
                        RelatedObjects relObjs = objFactory.createRelatedObjects();
                        m_viewer.setRelatedObjects(relObjs);
                        List relList = relObjs.getRelatedObject();
                        for (Iterator it2 = rels.iterator(); it2.hasNext(); ) {
                            Relationship rel = (Relationship) it2.next();
                            org.jaffa.patterns.library.domain_creator_1_1.domain.Root innerDomain = m_doList.getByDomain(rel.getToDomainObject());
                            RelatedObject relObj = objFactory.createRelatedObject();
                            relList.add(relObj);
                            relObj.setObjectName(innerDomain.getDomainObject());
                            relObj.setPackage(innerDomain.getDomainPackage());
                            RelatedObjectJoinFields rojf = objFactory.createRelatedObjectJoinFields();
                            relObj.setRelatedObjectJoinFields(rojf);
                            List rojbList = rojf.getRelatedObjectJoinBetween();
                            List domainKeys = rel.getFromFields().getRelationshipField();
                            List foreignKeys = rel.getToFields().getRelationshipField();
                            for (int i = 0; i < domainKeys.size(); i++) {
                                RelatedObjectJoinBetween rojb = objFactory.createRelatedObjectJoinBetween();
                                rojbList.add(rojb);
                                rojb.setDomainObjectField(((RelationshipField) domainKeys.get(i)).getName());
                                rojb.setRelatedObjectFieldName(((RelationshipField) foreignKeys.get(i)).getName());
                            }
                            List innerFlds = innerDomain.getFields().getField();
                            RelatedObjectFields rofs = objFactory.createRelatedObjectFields();
                            relObj.setRelatedObjectFields(rofs);
                            List relFields = rofs.getRelatedObjectField();
                            KeyFields kfs = objFactory.createKeyFields();
                            relObj.setKeyFields(kfs);
                            List relKeys = kfs.getKeyField();
                            OrderByFields obfs = objFactory.createOrderByFields();
                            relObj.setOrderByFields(obfs);
                            List orderFlds = obfs.getOrderByField();
                            for (Iterator it3 = innerFlds.iterator(); it3.hasNext(); ) {
                                Field innerFld = (Field) it3.next();
                                List toflds = rel.getToFields().getRelationshipField();
                                boolean inRel = false;
                                for (Iterator it4 = toflds.iterator(); it4.hasNext(); ) {
                                    RelationshipField rf = (RelationshipField) it4.next();
                                    if (rf.getName().equals(innerFld.getName())) {
                                        inRel = true;
                                        break;
                                    }
                                }
                                String dt = Defaults.getDataType(innerFld.getDataType());
                                if (dt == null) {
                                    throw new Exception("Can't Translate Java Class " + innerFld.getDataType() + " to a supported Data Type");
                                }
                                RelatedObjectField rof = objFactory.createRelatedObjectField();
                                relFields.add(rof);
                                rof.setName(innerFld.getName());
                                rof.setDataType(dt);
                                rof.setDisplay(!inRel);
                                rof.setDisplayAsKey(innerFld.getPrimaryKey() == org.jaffa.patterns.library.domain_creator_1_1.domain.Boolean.T);
                                rof.setLabel(innerFld.getLabelToken());
                                rof.setWidth("300px");
                                rof.setDomainField(innerFld.getName());
                                if (innerFld.getPrimaryKey() == org.jaffa.patterns.library.domain_creator_1_1.domain.Boolean.T) {
                                    KeyField kf = objFactory.createKeyField();
                                    relKeys.add(kf);
                                    kf.setDataType(dt);
                                    kf.setRelatedObjectFieldName(innerFld.getName());
                                    kf.setFieldNameInTargetComponent(innerFld.getName());
                                }
                                if (rof.isDisplayAsKey()) {
                                    OrderByField obf = objFactory.createOrderByField();
                                    orderFlds.add(obf);
                                    obf.setDomainFieldName(innerFld.getName());
                                    obf.setSortAscending("true");
                                }
                            }
                        }
                    }
                }
            }
        } catch (JAXBException e) {
            log.error("Failed to create Viewer Object");
        }
    }

    /** Saves the generated meta data to the prespecified location as an XML file
     * NOTE: assumes that the build(..) method has been called!
     */
    public boolean save() {
        String filename = m_app.getOutputRoot() + m_app.getOutputViewers() + m_domain.getDomainObject() + "Viewer.xml";
        File file = new File(filename);
        File path = new File(file.getParent());
        if (!path.exists()) {
            path.mkdirs();
        }
        FileOutputStream out = null;
        try {
            try {
                out = new FileOutputStream(file.getPath());
            } catch (FileNotFoundException e) {
                log.error("Failed to open output stream !", e);
                return false;
            }
            try {
                JAXBContext jc = JAXBContext.newInstance("org.jaffa.patterns.library.object_viewer_meta_1_0.domain");
                Marshaller m = jc.createMarshaller();
                m.setSchema(org.jaffa.util.JAXBHelper.createSchema(SCHEMA));
                JAXBHelper.marshalWithDocType(m, m_viewer, out, "Root", "-//JAFFA//DTD Object Viewer Meta 1.0//EN", "http://jaffa.sourceforge.net/DTD/object-viewer-meta_1_0.dtd");
            } catch (Exception e) {
                log.error("Failed to marshal xml to output stream !", e);
                return false;
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }

    private static List reservedAttr = null;

    static {
        reservedAttr = new ArrayList();
        reservedAttr.add("component");
    }

    private String reservedName(String name) {
        if (reservedAttr.contains(name.toLowerCase())) {
            return name + "1";
        } else {
            return name;
        }
    }

    private void initUpEntities() {
        m_upDomain = new ArrayList();
        m_upRel = new ArrayList();
        for (Iterator it = m_doList.iterator(); it.hasNext(); ) {
            org.jaffa.patterns.library.domain_creator_1_1.domain.Root domain = (org.jaffa.patterns.library.domain_creator_1_1.domain.Root) it.next();
            if (domain.getRelationships() != null) {
                List rels = domain.getRelationships().getRelationship();
                if (rels != null && !rels.isEmpty()) {
                    for (Iterator it2 = rels.iterator(); it2.hasNext(); ) {
                        org.jaffa.patterns.library.domain_creator_1_1.domain.Relationship rel = (org.jaffa.patterns.library.domain_creator_1_1.domain.Relationship) it2.next();
                        if (m_domain.getDomainObject().equals(rel.getToDomainObject()) && m_domain.getDomainPackage().equals(rel.getToDomainPackage())) {
                            m_upDomain.add(domain);
                            m_upRel.add(rel);
                            break;
                        }
                    }
                }
            }
        }
    }

    private int getUpDomain(String fieldname) {
        for (int i = 0; i < m_upDomain.size(); i++) {
            org.jaffa.patterns.library.domain_creator_1_1.domain.Root domain = (org.jaffa.patterns.library.domain_creator_1_1.domain.Root) m_upDomain.get(i);
            org.jaffa.patterns.library.domain_creator_1_1.domain.Relationship rel = (org.jaffa.patterns.library.domain_creator_1_1.domain.Relationship) m_upRel.get(i);
            List toFields = rel.getToFields().getRelationshipField();
            if (toFields.size() == 1) {
                org.jaffa.patterns.library.domain_creator_1_1.domain.RelationshipField toFld = (org.jaffa.patterns.library.domain_creator_1_1.domain.RelationshipField) toFields.get(0);
                if (toFld.getName().equals(fieldname)) {
                    log.debug("...Found - " + domain.getDomainObject());
                    return i;
                }
            }
        }
        return -1;
    }

    public String getApplication() {
        return m_viewer.getApplication();
    }

    public String getComponentControllerClass() {
        return m_viewer.getComponent() + "Component";
    }

    public String getComponentControllerPackage() {
        return m_viewer.getBasePackage() + ".components." + m_viewer.getComponent().toLowerCase() + ".ui";
    }

    public String getComponentName() {
        return getModule() + "." + StringHelper.getUpper1(m_viewer.getComponent());
    }

    public String getComponentType() {
        return "Viewer";
    }

    public String getDomain() {
        return m_viewer.getDomainPackage() + "." + m_viewer.getDomainObject();
    }

    public String getModule() {
        return StringHelper.getUpper1(m_viewer.getModule());
    }

    public String getName() {
        return StringHelper.getUpper1(m_viewer.getComponent());
    }

    /** Causes the meta data object to be build ready for saving.
     * @param fullPackage If true then the .applications. and .modules. package names
     * are used. If this is false, these are ommited for a much more condensed package
     * naming convention
     */
    public void buildPhase2(boolean fullPackage) throws Exception {
        ObjectFactory objFactory = new ObjectFactory();
        List results = m_viewer.getResultsFields().getResultsField();
        for (Iterator it = results.iterator(); it.hasNext(); ) {
            ResultsField rfld = (ResultsField) it.next();
            int upDomainIdx = getUpDomain(rfld.getName());
            if (upDomainIdx != -1) {
                org.jaffa.patterns.library.domain_creator_1_1.domain.Root upDomain = (org.jaffa.patterns.library.domain_creator_1_1.domain.Root) m_upDomain.get(upDomainIdx);
                org.jaffa.patterns.library.domain_creator_1_1.domain.Relationship upRel = (org.jaffa.patterns.library.domain_creator_1_1.domain.Relationship) m_upRel.get(upDomainIdx);
                log.debug("Linking 'Up' Viewer " + upDomain.getDomainObject() + " to " + m_domain.getDomainObject());
                IBuilder vcomp = m_compReg.findComponent(upDomain.getDomainPackage() + "." + upDomain.getDomainObject(), "Viewer");
                if (vcomp != null) {
                    Viewer viewer = objFactory.createViewer();
                    rfld.setViewer(viewer);
                    viewer.setClassName(vcomp.getComponentControllerClass());
                    viewer.setComponentName(vcomp.getComponentName());
                    viewer.setPackage(vcomp.getComponentControllerPackage());
                    viewer.setFieldNameInTargetComponent(reservedName(((org.jaffa.patterns.library.domain_creator_1_1.domain.RelationshipField) upRel.getFromFields().getRelationshipField().get(0)).getName()));
                } else {
                    log.info("No related viewer found in " + getComponentName() + " for " + upDomain.getDomainObject());
                }
            }
        }
        if (m_viewer.getRelatedObjects() != null) {
            List related = m_viewer.getRelatedObjects().getRelatedObject();
            for (Iterator it = related.iterator(); it.hasNext(); ) {
                RelatedObject robj = (RelatedObject) it.next();
                log.debug("Linking 'Down' Viewer " + robj.getObjectName() + " to " + m_domain.getDomainObject());
                IBuilder vcomp = m_compReg.findComponent(robj.getPackage() + "." + robj.getObjectName(), "Viewer");
                if (vcomp != null) {
                    RelatedObjectViewer rviewer = objFactory.createRelatedObjectViewer();
                    robj.setRelatedObjectViewer(rviewer);
                    rviewer.setClassName(vcomp.getComponentControllerClass());
                    rviewer.setComponentName(vcomp.getComponentName());
                    rviewer.setPackage(vcomp.getComponentControllerPackage());
                } else {
                    log.info("No related viewer in component " + getComponentName() + " for domain " + robj.getObjectName());
                }
            }
        }
    }
}
