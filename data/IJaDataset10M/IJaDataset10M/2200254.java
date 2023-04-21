package org.fao.geonet.kernel.schema;

import java.util.*;
import org.jdom.*;
import java.io.File;
import jeeves.utils.Xml;
import org.fao.geonet.constants.Edit;
import org.fao.geonet.kernel.SchemaOverrides;
import org.fao.geonet.kernel.SchemaSuggestions;

public class SchemaLoader {

    private Element elRoot;

    private Element elFirst = null;

    private final HashMap hmElements = new HashMap();

    private final HashMap hmTypes = new HashMap();

    private final HashMap hmAttrGrp = new HashMap();

    private final HashMap hmAttrGpEn = new HashMap();

    private final HashMap hmAbsElems = new HashMap();

    private final HashMap hmSubsGrp = new HashMap();

    private final HashMap hmSubsLink = new HashMap();

    private final HashMap hmSubsNames = new HashMap();

    private final HashMap hmAttribs = new HashMap();

    private final HashMap hmAllAttrs = new HashMap();

    private final HashMap hmGroups = new HashMap();

    private final HashMap hmNameSpaces = new HashMap();

    private SchemaSubstitutions ssOverRides;

    private String targetNS;

    private String targetNSPrefix;

    /** Restrictions for simple types (element restriction) */
    private final HashMap hmElemRestr = new HashMap();

    /** Restrictions for simple types (type restriction) */
    private final HashMap hmTypeRestr = new HashMap();

    public SchemaLoader() {
    }

    public MetadataSchema load(String xmlSchemaFile, String schemaId, String xmlSubstitutionsFile) throws Exception {
        ssOverRides = new SchemaSubstitutions(xmlSubstitutionsFile);
        if (xmlSchemaFile.startsWith("_")) return new MetadataSchema(new Element("root"));
        ArrayList alElementFiles = loadFile(xmlSchemaFile, new HashSet());
        parseElements(alElementFiles);
        for (Iterator i = hmSubsGrp.keySet().iterator(); i.hasNext(); ) {
            String elem = (String) i.next();
            ArrayList elements = (ArrayList) hmSubsGrp.get(elem);
            ArrayList subsNames = new ArrayList();
            hmSubsNames.put(elem, subsNames);
            for (int j = 0; j < elements.size(); j++) {
                ElementEntry ee = (ElementEntry) elements.get(j);
                if (ee.type == null) {
                    ee.type = (String) hmAbsElems.get(elem);
                    if (ee.type == null) {
                        Logger.log("Type is null for 'element' : " + ee.name + " which is part of substitution group with head element " + elem);
                    }
                }
                hmElements.put(ee.name, ee.type);
                subsNames.add(ee.name);
            }
        }
        MetadataSchema mds = new MetadataSchema(elRoot);
        mds.setPrimeNS(elFirst.getAttributeValue("targetNamespace"));
        for (int j = 0; j < alElementFiles.size(); j++) {
            ElementInfo ei = (ElementInfo) alElementFiles.get(j);
            mds.addNS(ei.targetNSPrefix, ei.targetNS);
        }
        for (Iterator i = hmElements.keySet().iterator(); i.hasNext(); ) {
            String elem = (String) i.next();
            String type = (String) hmElements.get(elem);
            if (type == null) {
                Logger.log("Searching for type for element " + elem);
                type = recurseOnSubstitutionLinks(elem);
                if (type == null) {
                    System.out.println("WARNING: Cannot find type for " + elem + ": assuming string");
                    type = "string";
                } else {
                    Logger.log("-- Recursive search returned " + type + " for element " + elem);
                }
            }
            ArrayList elemRestr = (ArrayList) hmElemRestr.get(elem);
            ArrayList typeRestr = (ArrayList) hmTypeRestr.get(type);
            if (elemRestr == null) elemRestr = new ArrayList();
            if (typeRestr != null) elemRestr.addAll(typeRestr);
            ArrayList elemSubs = (ArrayList) hmSubsNames.get(elem);
            if (elemSubs == null) elemSubs = new ArrayList();
            String elemSubsLink = (String) hmSubsLink.get(elem);
            if (elemSubsLink == null) elemSubsLink = "";
            mds.addElement(elem, type, elemRestr, elemSubs, elemSubsLink);
        }
        for (Iterator i = hmAttrGpEn.values().iterator(); i.hasNext(); ) {
            AttributeGroupEntry age = (AttributeGroupEntry) i.next();
            for (int k = 0; k < age.alAttrs.size(); k++) {
                AttributeEntry attr = (AttributeEntry) age.alAttrs.get(k);
                if (attr.name != null) hmAllAttrs.put(attr.name, attr);
            }
            ArrayList attrs = resolveNestedAttributeGroups(age);
            hmAttrGrp.put(age.name, attrs);
        }
        HashMap hmAttrChk = new HashMap();
        for (Iterator i = hmAllAttrs.values().iterator(); i.hasNext(); ) {
            AttributeEntry attr = (AttributeEntry) i.next();
            AttributeEntry attrPrev = (AttributeEntry) hmAttrChk.get(attr.unqualifiedName);
            if (attrPrev != null) {
                attr.form = "qualified";
                attrPrev.form = "qualified";
            } else {
                hmAttrChk.put(attr.unqualifiedName, attr);
            }
        }
        ArrayList alTypes = new ArrayList(hmTypes.values());
        for (ListIterator i = alTypes.listIterator(); i.hasNext(); ) {
            ComplexTypeEntry cte = (ComplexTypeEntry) i.next();
            MetadataType mdt = new MetadataType();
            mdt.setOrType(cte.isOrType);
            if (cte.complexContent != null) {
                if (cte.complexContent.base != null) {
                    cte.alElements = resolveInheritance(cte);
                    ArrayList complexContentAttribs = resolveAttributeInheritance(cte);
                    for (int j = 0; j < complexContentAttribs.size(); j++) {
                        AttributeEntry ae = (AttributeEntry) complexContentAttribs.get(j);
                        mdt.addAttribute(buildMetadataAttrib(ae));
                    }
                    ComplexTypeEntry baseCTE = (ComplexTypeEntry) hmTypes.get(cte.complexContent.base);
                    if (baseCTE.isOrType) {
                        cte.isOrType = true;
                        mdt.setOrType(true);
                        Logger.log("Setting " + cte.name + " to isOrType");
                    }
                } else {
                    throw new IllegalArgumentException("base not defined for complexContent in " + cte.name);
                }
            } else if (cte.simpleContent != null) {
                ArrayList simpleContentAttribs = resolveAttributeInheritanceFromSimpleContent(cte);
                for (int j = 0; j < simpleContentAttribs.size(); j++) {
                    AttributeEntry ae = (AttributeEntry) simpleContentAttribs.get(j);
                    mdt.addAttribute(buildMetadataAttrib(ae));
                }
            } else {
                for (int j = 0; j < cte.alAttribs.size(); j++) {
                    AttributeEntry ae = (AttributeEntry) cte.alAttribs.get(j);
                    mdt.addAttribute(buildMetadataAttrib(ae));
                }
                for (int k = 0; k < cte.alAttribGroups.size(); k++) {
                    String attribGroup = (String) cte.alAttribGroups.get(k);
                    ArrayList al = (ArrayList) hmAttrGrp.get(attribGroup);
                    if (al == null) throw new IllegalArgumentException("Attribute group not found : " + attribGroup);
                    for (int j = 0; j < al.size(); j++) {
                        AttributeEntry ae = (AttributeEntry) al.get(j);
                        mdt.addAttribute(buildMetadataAttrib(ae));
                    }
                }
            }
            for (int j = 0; j < cte.alElements.size(); j++) {
                ElementEntry ee = (ElementEntry) cte.alElements.get(j);
                if (ee.groupElem || ee.choiceElem || ee.sequenceElem) {
                    Integer baseNr = j;
                    String baseName = cte.name;
                    String extension;
                    ArrayList elements;
                    if (ee.choiceElem) {
                        extension = Edit.RootChild.CHOICE;
                        elements = ee.alContainerElems;
                    } else if (ee.groupElem) {
                        extension = Edit.RootChild.GROUP;
                        GroupEntry group = (GroupEntry) hmGroups.get(ee.ref);
                        elements = group.alElements;
                    } else {
                        extension = Edit.RootChild.SEQUENCE;
                        elements = ee.alContainerElems;
                    }
                    String type = ee.name = baseName + extension + baseNr;
                    ArrayList newCtes = createTypeAndResolveNestedContainers(schemaId, mds, elements, baseName, extension, baseNr);
                    if (newCtes.size() != 0) {
                        for (int ctCntr = 0; ctCntr < newCtes.size(); ctCntr++) {
                            ComplexTypeEntry newCte = (ComplexTypeEntry) newCtes.get(ctCntr);
                            i.add(newCte);
                            i.previous();
                        }
                    }
                    mds.addElement(ee.name, type, new ArrayList(), new ArrayList(), "");
                    mdt.addElementWithType(ee.name, type, ee.min, ee.max);
                } else if (ee.ref != null) {
                    boolean choiceType = (cte.alElements.size() == 1);
                    handleRefElement(j, schemaId, cte.name, choiceType, ee, mdt, mds);
                } else if (ee.name != null) {
                    ComplexTypeEntry newCte = handleLocalElement(j, schemaId, cte.name, ee, mdt, mds);
                    if (newCte != null) {
                        i.add(newCte);
                        i.previous();
                    }
                } else {
                    throw new IllegalArgumentException("Unknown element type at position " + j + " in complexType " + cte.name);
                }
            }
            mds.addType(cte.name, mdt);
        }
        return mds;
    }

    private String recurseOnSubstitutionLinks(String elemName) {
        String elemLinkName = (String) hmSubsLink.get(elemName);
        if (elemLinkName != null) {
            String elemLinkType = (String) hmElements.get(elemLinkName);
            if (elemLinkType != null) return elemLinkType; else recurseOnSubstitutionLinks(elemLinkName);
        }
        return null;
    }

    private ComplexTypeEntry handleLocalElement(Integer elementNr, String schemaId, String baseName, ElementEntry ee, MetadataType mdt, MetadataSchema mds) {
        ComplexTypeEntry cteInt = null;
        ArrayList elemRestr = new ArrayList();
        if (ee.type == null) {
            if (ee.complexType != null) {
                cteInt = ee.complexType;
                ee.type = cteInt.name = ee.name + "HSI" + elementNr + getUnqualifiedName(baseName);
            } else if (ee.simpleType != null) {
                ee.type = "string";
                if (ee.simpleType.alEnum != null) elemRestr.addAll(ee.simpleType.alEnum);
            } else {
                System.out.println("WARNING: Could not find type for " + ee.name + " - assuming string");
                ee.type = "string";
            }
        }
        mds.addElement(ee.name, ee.type, elemRestr, new ArrayList(), "");
        mdt.addElementWithType(ee.name, ee.type, ee.min, ee.max);
        return (cteInt);
    }

    private ArrayList getOverRideSubstitutes(String elementName) {
        ArrayList subs = (ArrayList) hmSubsGrp.get(elementName);
        ArrayList ssOs = ssOverRides.getSubstitutes(elementName);
        if (ssOs != null && subs != null) {
            ArrayList results = new ArrayList();
            ArrayList validSubs = (ArrayList) hmSubsNames.get(elementName);
            for (int i = 0; i < ssOs.size(); i++) {
                String altSub = (String) ssOs.get(i);
                if (validSubs != null && !validSubs.contains(altSub)) {
                    System.out.println("WARNING: schema-substitutions.xml specified " + altSub + " for element " + elementName + " but the schema does not define this as a valid substitute");
                }
                for (int k = 0; k < subs.size(); k++) {
                    ElementEntry ee = (ElementEntry) subs.get(k);
                    if (ee.name.equals(altSub)) {
                        results.add(ee);
                    }
                }
            }
            if (results.size() == 0 && validSubs != null) {
                System.out.println("WARNING: schema-substitutions.xml has wiped out XSD substitution list for " + elementName);
            }
            return results;
        }
        return null;
    }

    private void handleRefElement(Integer elementNr, String schemaId, String baseName, boolean choiceType, ElementEntry ee, MetadataType mdt, MetadataSchema mds) {
        String type = (String) hmElements.get(ee.ref);
        boolean isAbstract = hmAbsElems.containsKey(ee.ref);
        boolean doSubs = true;
        ArrayList al = getOverRideSubstitutes(ee.ref);
        if (al == null) al = (ArrayList) hmSubsGrp.get(ee.ref); else doSubs = false;
        if ((al != null && al.size() > 0) || isAbstract) {
            if (choiceType) {
                Integer elementsAdded = assembleChoiceElements(mdt, al, doSubs);
                if (!isAbstract && doSubs) {
                    mdt.addRefElementWithType(ee.ref, type, ee.min, ee.max);
                    elementsAdded++;
                }
                mdt.setOrType(elementsAdded > 1);
            } else {
                MetadataType mdtc = new MetadataType();
                Integer elementsAdded = assembleChoiceElements(mdtc, al, doSubs);
                if (!isAbstract && doSubs) {
                    mdtc.addRefElementWithType(ee.ref, ee.type, ee.min, ee.max);
                    elementsAdded++;
                }
                mdtc.setOrType(elementsAdded > 1);
                type = ee.ref + Edit.RootChild.CHOICE + elementNr;
                String name = type;
                mds.addType(type, mdtc);
                mds.addElement(name, type, new ArrayList(), new ArrayList(), "");
                mdt.addElementWithType(name, type, ee.min, ee.max);
            }
        } else if (!isAbstract) {
            mdt.addRefElementWithType(ee.ref, type, ee.min, ee.max);
        } else {
            System.out.println("WARNING: element " + ee.ref + " from " + baseName + " has fallen through the logic (abstract: " + isAbstract + ") - ignoring");
        }
    }

    private ArrayList resolveNestedAttributeGroups(AttributeGroupEntry age) {
        ArrayList attrs = new ArrayList();
        if (age.alAttrGrps.size() > 0) {
            for (int i = 0; i < age.alAttrGrps.size(); i++) {
                AttributeGroupEntry ageInternal = (AttributeGroupEntry) age.alAttrGrps.get(i);
                AttributeGroupEntry ageRef = (AttributeGroupEntry) hmAttrGpEn.get(ageInternal.ref);
                if (ageRef == null) throw new IllegalArgumentException("ERROR: cannot find attributeGroup with ref " + ageInternal.ref);
                attrs.addAll(resolveNestedAttributeGroups(ageRef));
            }
        }
        attrs.addAll(age.alAttrs);
        return attrs;
    }

    private ArrayList createTypeAndResolveNestedContainers(String schemaId, MetadataSchema mds, ArrayList al, String baseName, String extension, Integer baseNr) {
        ArrayList complexTypes = new ArrayList();
        Integer oldBaseNr = baseNr;
        if (al == null) return complexTypes;
        MetadataType mdt = new MetadataType();
        if (extension.contains(Edit.RootChild.CHOICE)) mdt.setOrType(true);
        for (int k = 0; k < al.size(); k++) {
            ElementEntry ee = (ElementEntry) al.get(k);
            baseNr++;
            if (ee.choiceElem) {
                String newExtension = Edit.RootChild.CHOICE;
                ArrayList newCtes = createTypeAndResolveNestedContainers(schemaId, mds, ee.alContainerElems, baseName, newExtension, baseNr);
                if (newCtes.size() > 0) complexTypes.addAll(newCtes);
                ee.name = ee.type = baseName + newExtension + baseNr;
                mds.addElement(ee.name, ee.type, new ArrayList(), new ArrayList(), "");
                mdt.addElementWithType(ee.name, ee.type, ee.min, ee.max);
            } else if (ee.groupElem) {
                String newExtension = Edit.RootChild.GROUP;
                if (ee.ref != null) {
                    GroupEntry group = (GroupEntry) hmGroups.get(ee.ref);
                    ArrayList alGroupElements = group.alElements;
                    ArrayList newCtes = createTypeAndResolveNestedContainers(schemaId, mds, alGroupElements, baseName, newExtension, baseNr);
                    if (newCtes.size() > 0) complexTypes.addAll(newCtes);
                    ee.name = ee.type = baseName + newExtension + baseNr;
                    mds.addElement(ee.name, ee.type, new ArrayList(), new ArrayList(), "");
                    mdt.addElementWithType(ee.name, ee.type, ee.min, ee.max);
                } else {
                    System.out.println("WARNING: group element ref is NULL in " + baseName + extension + baseNr);
                }
            } else if (ee.sequenceElem) {
                String newExtension = Edit.RootChild.SEQUENCE;
                ArrayList newCtes = createTypeAndResolveNestedContainers(schemaId, mds, ee.alContainerElems, baseName, newExtension, baseNr);
                if (newCtes.size() > 0) complexTypes.addAll(newCtes);
                ee.name = ee.type = baseName + newExtension + baseNr;
                mds.addElement(ee.name, ee.type, new ArrayList(), new ArrayList(), "");
                mdt.addElementWithType(ee.name, ee.type, ee.min, ee.max);
            } else {
                if (ee.name != null) {
                    ComplexTypeEntry newCte = handleLocalElement(k, schemaId, baseName, ee, mdt, mds);
                    if (newCte != null) complexTypes.add(newCte);
                } else {
                    handleRefElement(k, schemaId, baseName, false, ee, mdt, mds);
                }
            }
        }
        mds.addType(baseName + extension + oldBaseNr, mdt);
        return complexTypes;
    }

    private int assembleChoiceElements(MetadataType mdt, ArrayList al, boolean doSubs) {
        int number = 0;
        if (al == null) return number;
        for (int k = 0; k < al.size(); k++) {
            ElementEntry ee = (ElementEntry) al.get(k);
            if (ee.abstrElem) {
                Integer numberRecursed = assembleChoiceElements(mdt, (ArrayList) hmSubsGrp.get(ee.name), doSubs);
                number = number + numberRecursed;
            } else {
                number++;
                mdt.addElementWithType(ee.name, ee.type, ee.min, ee.max);
                if (doSubs) {
                    ArrayList elemSubs = (ArrayList) hmSubsGrp.get(ee.name);
                    if (elemSubs != null) {
                        for (int j = 0; j < elemSubs.size(); j++) {
                            ElementEntry eeSub = (ElementEntry) elemSubs.get(j);
                            mdt.addElementWithType(eeSub.name, eeSub.type, eeSub.min, eeSub.max);
                            number++;
                        }
                    }
                }
            }
        }
        return number;
    }

    /** Loads the xml-schema file, removes annotations and resolve imports/includes */
    private ArrayList loadFile(String xmlSchemaFile, HashSet loadedFiles) throws Exception {
        loadedFiles.add(new File(xmlSchemaFile).getCanonicalPath());
        String path = new File(xmlSchemaFile).getParent() + "/";
        elRoot = Xml.loadFile(xmlSchemaFile);
        if (elFirst == null) elFirst = elRoot;
        String oldtargetNS = targetNS;
        String oldtargetNSPrefix = targetNSPrefix;
        targetNS = elRoot.getAttributeValue("targetNamespace");
        targetNSPrefix = null;
        if (targetNS != null) {
            for (Iterator i = elRoot.getAdditionalNamespaces().iterator(); i.hasNext(); ) {
                Namespace ns = (Namespace) i.next();
                if (targetNS.equals(ns.getURI())) {
                    targetNSPrefix = ns.getPrefix();
                    break;
                }
            }
            if ("".equals(targetNSPrefix)) targetNSPrefix = null;
        }
        if ((xmlSchemaFile.contains("xml.xsd") || xmlSchemaFile.contains("xml-mod.xsd")) && targetNS.equals("http://www.w3.org/XML/1998/namespace")) targetNSPrefix = "xml";
        List children = elRoot.getChildren();
        ArrayList alElementFiles = new ArrayList();
        for (int i = 0; i < children.size(); i++) {
            Element elChild = (Element) children.get(i);
            String name = elChild.getName();
            if (name.equals("annotation")) ; else if (name.equals("import") || name.equals("include")) {
                String schemaLoc = elChild.getAttributeValue("schemaLocation");
                if (schemaLoc.startsWith("http:")) {
                    int lastSlash = schemaLoc.lastIndexOf("/");
                    schemaLoc = schemaLoc.substring(lastSlash + 1);
                }
                if (!loadedFiles.contains(new File(path + schemaLoc).getCanonicalPath())) alElementFiles.addAll(loadFile(path + schemaLoc, loadedFiles));
            } else alElementFiles.add(new ElementInfo(elChild, xmlSchemaFile, targetNS, targetNSPrefix));
        }
        targetNS = oldtargetNS;
        targetNSPrefix = oldtargetNSPrefix;
        return alElementFiles;
    }

    private void parseElements(ArrayList alElementFiles) throws JDOMException {
        hmElements.clear();
        hmTypes.clear();
        hmAttrGrp.clear();
        hmAbsElems.clear();
        hmSubsGrp.clear();
        hmSubsLink.clear();
        hmElemRestr.clear();
        hmTypeRestr.clear();
        hmAttribs.clear();
        hmAllAttrs.clear();
        hmGroups.clear();
        for (int i = 0; i < alElementFiles.size(); i++) {
            ElementInfo ei = (ElementInfo) alElementFiles.get(i);
            Element elChild = ei.element;
            String name = elChild.getName();
            if (name.equals("element")) buildGlobalElement(ei); else if (name.equals("complexType")) buildComplexType(ei); else if (name.equals("simpleType")) buildSimpleType(ei); else if (name.equals("attribute")) buildGlobalAttrib(ei); else if (name.equals("group")) buildGlobalGroup(ei); else if (name.equals("attributeGroup")) buildGlobalAttributeGroup(ei); else Logger.log("Unknown global element : " + elChild.getName(), ei);
        }
    }

    private void buildGlobalElement(ElementInfo ei) {
        ElementEntry ee = new ElementEntry(ei);
        if (ee.name == null) throw new IllegalArgumentException("Name is null for element : " + ee.name);
        if (ee.substGroup != null) {
            ArrayList al = (ArrayList) hmSubsGrp.get(ee.substGroup);
            if (al == null) {
                al = new ArrayList();
                hmSubsGrp.put(ee.substGroup, al);
            }
            al.add(ee);
            if (hmSubsLink.get(ee.name) != null) {
                throw new IllegalArgumentException("Substitution link collision for : " + ee.name + " link to " + ee.substGroup);
            } else {
                hmSubsLink.put(ee.name, ee.substGroup);
            }
        }
        if (ee.abstrElem) {
            if (hmAbsElems.containsKey(ee.name)) throw new IllegalArgumentException("Namespace collision for : " + ee.name);
            hmAbsElems.put(ee.name, ee.type);
            return;
        }
        if (ee.complexType != null) {
            String type = ee.name + "HSI";
            ee.complexType.name = type;
            ee.type = type;
            if (hmElements.containsKey(ee.name)) throw new IllegalArgumentException("Namespace collision for : " + ee.name);
            hmElements.put(ee.name, type);
            hmTypes.put(type, ee.complexType);
        } else if (ee.simpleType != null) {
            String type = ee.name;
            if (hmElements.containsKey(ee.name)) throw new IllegalArgumentException("Namespace collision for : " + ee.name);
            ee.type = "string";
            hmElements.put(ee.name, ee.type);
            hmElemRestr.put(ee.name, ee.simpleType.alEnum);
        } else {
            if (ee.type == null && ee.substGroup == null) {
                System.out.println("WARNING: " + ee.name + " is a global element without a type - assuming a string");
                ee.type = "string";
            }
            hmElements.put(ee.name, ee.type);
        }
        if (ee.name.contains("SensorML")) {
            Logger.log("SensorML element detected " + ee.name + " " + ee.complexType.name);
        }
    }

    private void buildComplexType(ElementInfo ei) {
        ComplexTypeEntry ct = new ComplexTypeEntry(ei);
        if (hmTypes.containsKey(ct.name)) throw new IllegalArgumentException("Namespace collision for : " + ct.name);
        hmTypes.put(ct.name, ct);
    }

    private void buildSimpleType(ElementInfo ei) {
        SimpleTypeEntry st = new SimpleTypeEntry(ei);
        if (hmTypeRestr.containsKey(st.name)) throw new IllegalArgumentException("Namespace collision for : " + st.name);
        hmTypeRestr.put(st.name, st.alEnum);
    }

    private void buildGlobalAttrib(ElementInfo ei) {
        AttributeEntry at = new AttributeEntry(ei);
        if (hmAttribs.containsKey(at.name)) throw new IllegalArgumentException("Namespace collision for : " + at.name);
        hmAttribs.put(at.name, at);
        hmAllAttrs.put(at.name, at);
    }

    private void buildGlobalGroup(ElementInfo ei) {
        GroupEntry ge = new GroupEntry(ei);
        if (hmGroups.containsKey(ge.name)) throw new IllegalArgumentException("Namespace collision for : " + ge.name);
        hmGroups.put(ge.name, ge);
    }

    private void buildGlobalAttributeGroup(ElementInfo ei) {
        AttributeGroupEntry age = new AttributeGroupEntry(ei);
        if (hmAttrGpEn.containsKey(age.name)) throw new IllegalArgumentException("Namespace collision for : " + age.name);
        hmAttrGpEn.put(age.name, age);
    }

    private ArrayList resolveAttributeInheritanceFromSimpleContent(ComplexTypeEntry cte) {
        ArrayList result = new ArrayList();
        if (cte.simpleContent == null) {
            throw new IllegalArgumentException("SimpleContent must be present in base type of the SimpleContent in " + cte.name);
        } else {
            String baseType = cte.simpleContent.base;
            ComplexTypeEntry baseCTE = (ComplexTypeEntry) hmTypes.get(baseType);
            if (baseCTE != null) result = new ArrayList(resolveAttributeInheritanceFromSimpleContent(baseCTE));
            if (cte.simpleContent.restriction) {
                ArrayList adds = (ArrayList) cte.simpleContent.alAttribs.clone();
                for (int i = 0; i < result.size(); i++) {
                    AttributeEntry attrib = (AttributeEntry) result.get(i);
                    for (int j = 0; j < adds.size(); j++) {
                        AttributeEntry attribOther = (AttributeEntry) adds.get(j);
                        boolean eqAttrib = eqAttribs(attribOther, attrib);
                        if (eqAttrib) {
                            result.set(i, attribOther);
                        }
                    }
                }
            } else result.addAll((ArrayList) cte.simpleContent.alAttribs.clone());
            if (cte.simpleContent.alAttribGroups != null) {
                for (int k = 0; k < cte.simpleContent.alAttribGroups.size(); k++) {
                    String attribGroup = (String) cte.simpleContent.alAttribGroups.get(k);
                    ArrayList al = (ArrayList) hmAttrGrp.get(attribGroup);
                    if (al == null) throw new IllegalArgumentException("Attribute group not found : " + attribGroup);
                    for (int j = 0; j < al.size(); j++) result.add(al.get(j));
                }
            }
        }
        return result;
    }

    /** function to test whether two AttributeEntry objects have the same name
	 */
    boolean eqAttribs(AttributeEntry attribOther, AttributeEntry attrib) {
        if (attribOther.name != null) {
            if (attrib.name != null) {
                if (attribOther.name.equals(attrib.name)) return true;
            } else {
                if (attribOther.name.equals(attrib.reference)) return true;
            }
        } else {
            if (attrib.name != null) {
                if (attribOther.reference.equals(attrib.name)) return true;
            } else {
                if (attribOther.reference.equals(attrib.reference)) return true;
            }
        }
        return false;
    }

    private ArrayList resolveAttributeInheritance(ComplexTypeEntry cte) {
        if (cte.complexContent == null) return cte.alAttribs;
        String baseType = cte.complexContent.base;
        ComplexTypeEntry baseCTE = (ComplexTypeEntry) hmTypes.get(baseType);
        if (baseCTE == null) throw new IllegalArgumentException("Base type not found for : " + baseType);
        ArrayList result = new ArrayList(resolveAttributeInheritance(baseCTE));
        if (cte.complexContent.restriction) {
            ArrayList adds = cte.complexContent.alAttribs;
            for (int i = 0; i < result.size(); i++) {
                AttributeEntry attrib = (AttributeEntry) result.get(i);
                for (int j = 0; j < adds.size(); j++) {
                    AttributeEntry attribOther = (AttributeEntry) adds.get(j);
                    boolean eqAttrib = eqAttribs(attribOther, attrib);
                    if (eqAttrib) {
                        result.set(i, attribOther);
                    }
                }
            }
        } else {
            result.addAll(cte.complexContent.alAttribs);
            if (cte.complexContent.alAttribGroups != null) {
                for (int k = 0; k < cte.complexContent.alAttribGroups.size(); k++) {
                    String attribGroup = (String) cte.complexContent.alAttribGroups.get(k);
                    ArrayList al = (ArrayList) hmAttrGrp.get(attribGroup);
                    if (al == null) throw new IllegalArgumentException("Attribute group not found : " + attribGroup);
                    for (int j = 0; j < al.size(); j++) result.add(al.get(j));
                }
            }
        }
        if (baseCTE.alAttribGroups != null) {
            for (int k = 0; k < baseCTE.alAttribGroups.size(); k++) {
                String attribGroup = (String) baseCTE.alAttribGroups.get(k);
                ArrayList al = (ArrayList) hmAttrGrp.get(attribGroup);
                if (al == null) throw new IllegalArgumentException("Attribute group not found : " + attribGroup);
                for (int j = 0; j < al.size(); j++) result.add(al.get(j));
            }
        }
        return result;
    }

    private ArrayList resolveInheritance(ComplexTypeEntry cte) {
        if (cte == null || cte.complexContent == null) return cte.alElements;
        String baseType = cte.complexContent.base;
        ComplexTypeEntry baseCTE = (ComplexTypeEntry) hmTypes.get(baseType);
        if (baseCTE == null) throw new IllegalArgumentException("Base type not found for : " + baseType);
        ArrayList result = new ArrayList();
        if (!cte.complexContent.restriction) result = new ArrayList(resolveInheritance(baseCTE));
        result.addAll(cte.complexContent.alElements);
        return result;
    }

    private MetadataAttribute buildMetadataAttrib(AttributeEntry ae) {
        String name = ae.name;
        String ref = ae.reference;
        String value = ae.defValue;
        boolean overRequired = ae.required;
        MetadataAttribute ma = new MetadataAttribute();
        if (ref != null) {
            ae = (AttributeEntry) hmAttribs.get(ref);
            if (ae == null) throw new IllegalArgumentException("Reference '" + ref + "' not found for attrib : " + name + ":" + ref);
        }
        if (ref != null && ref.contains(":")) ma.name = ref; else ma.name = ae.unqualifiedName;
        if (value != null) ma.defValue = value; else ma.defValue = ae.defValue;
        ma.required = overRequired;
        for (int k = 0; k < ae.alValues.size(); k++) ma.values.add(ae.alValues.get(k));
        return ma;
    }

    private String getPrefix(String qname) {
        int pos = qname.indexOf(":");
        if (pos < 0) return ""; else return qname.substring(0, pos);
    }

    public String getUnqualifiedName(String qname) {
        int pos = qname.indexOf(":");
        if (pos < 0) return qname; else return qname.substring(pos + 1);
    }

    private String getProfile(String name) {
        int pos = name.indexOf(".");
        if (pos < 0) return ""; else return name.substring(pos + 1);
    }
}

class ElementInfo {

    public Element element;

    public String file;

    public String targetNS;

    public String targetNSPrefix;

    public ElementInfo(Element e, String f, String tns, String tnsp) {
        element = e;
        file = f;
        targetNS = tns;
        targetNSPrefix = tnsp;
    }
}
