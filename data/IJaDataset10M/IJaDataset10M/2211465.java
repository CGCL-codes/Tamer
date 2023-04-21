package com.apelon.dts.test.interactive;

import com.apelon.apelonserver.client.ServerConnection;
import com.apelon.dts.client.association.ConceptAssociation;
import com.apelon.dts.client.association.Synonym;
import com.apelon.dts.client.attribute.DTSProperty;
import com.apelon.dts.client.attribute.DTSQualifier;
import com.apelon.dts.client.attribute.DTSRole;
import com.apelon.dts.client.attribute.DTSRoleType;
import com.apelon.dts.client.concept.*;
import com.apelon.dts.test.interactive.config.DTSMenuInput;

/**
 * Menu test for OntylogConceptQuery and OntylogExtConceptQuery methods.
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Apelon, Inc.</p>
 * @version DTS 3.4.1
 * @since DTS 3.0
 */
public class OntylogConceptMenuTest extends MenuTest {

    private static final String[] mainItems = { "find concept by name", "find concept by id", "find concept by code", "find all role types", "find roleType by id", "find roleType by code", "find roleType by name", "find roleType by namespaceId", "add multiple concepts", "delete multiple concepts" };

    private static final String[] testMainItems = { "findConceptByName", "findConceptById", "findConceptByCode", "findAllRoleTypes", "findRoleTypeById", "findRoleTypeByCode", "findRoleTypeByName", "findRoleTypeByNamespaceId", "addMultipleConcepts", "deleteMultipleConcepts" };

    private static int choice;

    private OntylogConceptQuery server;

    private OntylogExtConceptQuery extServer;

    private DTSMenuInput di;

    public String[] getDisplayItems() {
        return mainItems;
    }

    public String[] getTestItems() {
        return testMainItems;
    }

    public OntylogConceptMenuTest() {
        super();
        di = DTSMenuTest.di;
    }

    public OntylogConceptMenuTest(ServerConnection conn) throws Exception {
        super();
        di = DTSMenuTest.di;
        server = OntylogConceptQuery.createInstance(conn);
        extServer = OntylogExtConceptQuery.createInstance(conn);
        init();
    }

    protected void init() {
    }

    public void findAllRoleTypes() throws Exception {
        DTSRoleType[] values = server.getAllRoleTypes();
        for (int i = 0; i < values.length; i++) {
            System.out.println("roleType: " + values[i] + " id=" + values[i].getId() + " code=" + values[i].getCode() + " namespaceId=" + values[i].getNamespaceId());
        }
    }

    public void findRoleTypeByNamespaceId() throws Exception {
        String namespaceIdStr = getInput("enter namespace id");
        DTSRoleType[] values = server.getRoleTypes(Integer.parseInt(namespaceIdStr));
        for (int i = 0; i < values.length; i++) {
            System.out.println("roleType: " + values[i] + " id=" + values[i].getId() + " code=" + values[i].getCode() + " namespaceId=" + values[i].getNamespaceId());
        }
    }

    public void findRoleTypeByName() throws Exception {
        String name = di.getName();
        int namespaceId = di.getNamespaceId();
        DTSRoleType value = server.findRoleTypeByName(name, namespaceId);
        System.out.println("roleType: " + value);
    }

    public void findRoleTypeById() throws Exception {
        int id = di.getId();
        int namespaceId = di.getNamespaceId();
        DTSRoleType value = server.findRoleTypeById(id, namespaceId);
        System.out.println("roleType: " + value);
    }

    public void findRoleTypeByCode() throws Exception {
        String code = di.getCode();
        int namespaceId = di.getNamespaceId();
        DTSRoleType value = server.findRoleTypeByCode(code, namespaceId);
        System.out.println("roleType: " + value);
    }

    public void findConceptByName() throws Exception {
        String name = di.getName();
        int namespaceId = di.getNamespaceId();
        OntylogConcept value = (OntylogConcept) server.findConceptByName(name, namespaceId, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES);
        printConcept(value);
    }

    public void findConceptById() throws Exception {
        int id = di.getId();
        int namespaceId = di.getNamespaceId();
        OntylogConcept value = (OntylogConcept) server.findConceptById(id, namespaceId, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES);
        printConcept(value);
    }

    public void findConceptByCode() throws Exception {
        String code = di.getCode();
        int namespaceId = di.getNamespaceId();
        OntylogConcept value = (OntylogConcept) server.findConceptByCode(code, namespaceId, ConceptAttributeSetDescriptor.ALL_ATTRIBUTES);
        printConcept(value);
    }

    static void printConcept(OntylogConcept value) {
        System.out.println(value);
        DTSRole[] roles = value.getFetchedRoles();
        for (int i = 0; i < roles.length; i++) {
            System.out.println("role: name " + roles[i].getName() + " concept: " + roles[i].getValueConcept());
        }
        DTSProperty[] props = value.getFetchedProperties();
        for (int i = 0; i < props.length; i++) {
            System.out.println("property: " + props[i] + " value: " + props[i].getValue());
            DTSQualifier[] qualifiers = props[i].getFetchedQualifiers();
            for (int j = 0; j < qualifiers.length; j++) {
                System.out.println("qualifier: " + qualifiers[j].getQualifierType() + " qualifierValue: " + qualifiers[j].getValue());
            }
        }
        Synonym[] syn = value.getFetchedSynonyms();
        for (int i = 0; i < syn.length; i++) {
            System.out.println("synonym: " + syn[i]);
        }
        ConceptAssociation[] assoc = value.getFetchedConceptAssociations();
        for (int i = 0; i < assoc.length; i++) {
            System.out.println("conceptAssociation: " + assoc[i]);
        }
        ConceptAssociation[] inverseAssoc = value.getFetchedInverseConceptAssociations();
        for (int i = 0; i < inverseAssoc.length; i++) {
            System.out.println("inverseConceptAssociation: " + inverseAssoc[i]);
        }
        DTSRole[] inverseRoles = value.getFetchedInverseRoles();
        for (int i = 0; i < inverseRoles.length; i++) {
            System.out.println("inverseRole: name " + inverseRoles[i].getName() + " concept: " + inverseRoles[i].getValueConcept());
        }
    }

    public void addMultipleConcepts() throws Exception {
        int namespaceId = di.getNamespaceId();
        String name = di.getPrependName();
        int startNum = di.getStartNum();
        int endNum = di.getEndNum();
        int i = startNum;
        while (i < endNum) {
            try {
                DTSConcept value = new DTSConcept(name + "-" + i, namespaceId);
                value = extServer.addConcept(value);
                System.out.println(value.getName() + " [" + value.getId() + ": " + value.getCode() + "]");
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteMultipleConcepts() throws Exception {
        int namespaceId = di.getNamespaceId();
        String name = di.getPrependName();
        int startNum = di.getStartNum();
        int endNum = di.getEndNum();
        int i = startNum;
        while (i < endNum) {
            try {
                String conName = name + "-" + i;
                DTSConcept concept = server.findConceptByName(conName, namespaceId, ConceptAttributeSetDescriptor.NO_ATTRIBUTES);
                if (concept != null) {
                    boolean deleted = extServer.deleteConcept(concept);
                    if (deleted) {
                        System.out.println("Deleted: " + concept.getName() + " [" + concept.getId() + ": " + concept.getCode() + "]");
                    } else {
                        System.out.println("NOT deleted: " + concept.getName() + " [" + concept.getId() + ": " + concept.getCode() + "]");
                    }
                } else {
                    System.out.println("Concept not found: " + conName);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void printResult(boolean result) {
        if (result) {
            System.out.println("updated");
        } else {
            System.out.println("NO CHANGE");
        }
    }
}
