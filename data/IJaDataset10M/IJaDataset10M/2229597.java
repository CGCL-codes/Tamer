package edu.upmc.opi.caBIG.caTIES.cadsr;

import gov.nih.nci.cadsr.domain.ObjectClass;
import gov.nih.nci.cadsr.domain.PermissibleValue;
import gov.nih.nci.evs.domain.Association;
import gov.nih.nci.evs.domain.Atom;
import gov.nih.nci.evs.domain.AttributeSetDescriptor;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.domain.EdgeProperties;
import gov.nih.nci.evs.domain.MetaThesaurusConcept;
import gov.nih.nci.evs.domain.Property;
import gov.nih.nci.evs.domain.Qualifier;
import gov.nih.nci.evs.domain.Role;
import gov.nih.nci.evs.domain.SemanticType;
import gov.nih.nci.evs.domain.Source;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class CaTIES_ThesaurusTest {

    private static final int MATCH_BY_NAME = 0;

    private static final int MATCH_BY_ROLE = 1;

    private static final int MATCH_BY_PROPERTY = 2;

    private static final int MATCH_BY_SILO = 3;

    private static final int MATCH_BY_ASSOCIATION = 4;

    private static final int MATCH_BY_SYNONYM = 5;

    private static final int MATCH_BY_INVERSE_ROLE = 6;

    private static final int MATCH_BY_INVERSE_ASSOCIATION = 7;

    private static final int MATCH_BY_ROLE_PROPERTY = 8;

    private static final int WITH_NO_ATTRIBUTES = 0;

    private static final int WITH_ALL_ATTRIBUTES = 1;

    private static final int WITH_ALL_ROLES = 2;

    private static final int WITH_ALL_PROPERTIES = 3;

    private static final Hashtable conceptsTable = new Hashtable();

    private static final TreeSet ncitCodesFromCaDSR = new TreeSet(new Comparator() {

        public int compare(Object obj1, Object obj2) {
            CaTIES_ConceptWrapper concept1 = (CaTIES_ConceptWrapper) obj1;
            CaTIES_ConceptWrapper concept2 = (CaTIES_ConceptWrapper) obj2;
            return concept1.getId().compareTo(concept2.getId());
        }

        public boolean equals(Object obj) {
            return false;
        }
    });

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_ThesaurusTest.class);

    private String[] pertinentCategoryArray = { "Anatomy_Kind", "Findings_and_Disorders_Kind", "Procedure" };

    private String[] pertinentCategoryByNCIConceptArray = { "C43652", "C43661", "C43656", "C25218" };

    private String[][] pertinentCategoryByCUIArray = { { "CL337124", "NCI_META_CUI" }, { "CL337141", "NCI_META_CUI" }, { "C0184661", "UMLS_CUI" } };

    private ApplicationService appService;

    public CaTIES_ThesaurusTest() {
        this.appService = ApplicationServiceProvider.getApplicationService();
        testCaDSR();
    }

    private void runOtherTests() {
        getMetaSourcesTest();
        runTest("blood*", 5);
        generateTreeTest("Procedure", 1);
        getVocabularyNamesTest();
        getFrameNameTest();
        runAllCategoriesByConceptCode();
    }

    private void testCaDSR() {
        CaTIES_CaDSR cadsr = new CaTIES_CaDSR();
        cadsr.setNodeAccumulator(new CaTIES_CaDSRTreeNode());
        cadsr.classifyDataElements();
        CaTIES_CaDSRTreeNode rootNode = cadsr.getNodeAccumulator();
        gatherNCITCodesForClassificationScheme(rootNode);
        reportAcquiredConcepts();
    }

    private void reportAcquiredConcepts() {
        logger.debug("reportAcquiredConcepts.");
        for (Iterator iterator = ncitCodesFromCaDSR.iterator(); iterator.hasNext(); ) {
            logger.debug(iterator.next());
        }
    }

    private void gatherNCITCodesForClassificationScheme(CaTIES_CaDSRTreeNode parentNode) {
        for (Iterator iterator = parentNode.getChildren().iterator(); iterator.hasNext(); ) {
            CaTIES_CaDSRTreeNode childNode = (CaTIES_CaDSRTreeNode) iterator.next();
            CaTIES_CaDSRResourceData resourceData = (CaTIES_CaDSRResourceData) childNode.getUserImplObject();
            String resourceDataName = resourceData.getName();
            if (resourceDataName.endsWith("cap.domain.breast")) {
                gatherNCITCodes(childNode);
            }
        }
    }

    private void gatherNCITCodes(CaTIES_CaDSRTreeNode parentNode) {
        for (Iterator iterator = parentNode.getChildren().iterator(); iterator.hasNext(); ) {
            CaTIES_CaDSRTreeNode childNode = (CaTIES_CaDSRTreeNode) iterator.next();
            CaTIES_CaDSRResourceData resourceData = (CaTIES_CaDSRResourceData) childNode.getUserImplObject();
            if (resourceData.getDataClassName().equals(ObjectClass.class.getName()) || resourceData.getDataClassName().equals(Property.class.getName()) || resourceData.getDataClassName().equals(PermissibleValue.class.getName())) {
                gatherLeafNCITCode(childNode);
            } else {
                gatherNCITCodes(childNode);
            }
        }
    }

    private void gatherLeafNCITCode(CaTIES_CaDSRTreeNode parentNode) {
        int numberOfChildren = parentNode.getChildCount();
        for (int idx = 0; idx < numberOfChildren; idx++) {
            CaTIES_CaDSRTreeNode childNode = (CaTIES_CaDSRTreeNode) parentNode.getChildAt(idx);
            CaTIES_CaDSRResourceData resourceData = (CaTIES_CaDSRResourceData) childNode.getUserImplObject();
            String resourceClassName = resourceData.getDataClassName();
            if (resourceClassName.equals(String.class.getName()) && resourceData.getName().toUpperCase().startsWith("C")) {
                String ncitCode = resourceData.getName();
                CaTIES_ConceptWrapper conceptWrapper = new CaTIES_ConceptWrapper();
                conceptWrapper.setId(ncitCode);
                conceptWrapper.setCui(ncitCode);
                conceptWrapper.setNciMetaCui(ncitCode);
                this.ncitCodesFromCaDSR.add(conceptWrapper);
            }
        }
    }

    private CaTIES_CaDSRResourceData fetchDescLogicConcept(String ncitCode) {
        DescLogicConcept ncitConcept = null;
        logger.debug("Fetching ncitCode " + ncitCode);
        CaTIES_CaDSRResourceData resourceData = (CaTIES_CaDSRResourceData) conceptsTable.get(ncitCode);
        if (resourceData == null) {
            try {
                EVSQuery evsQuery = new EVSQueryImpl();
                evsQuery.searchSourceByCode(ncitCode, "NCI2006_06E");
                List evsResults;
                evsResults = (List) appService.evsSearch(evsQuery);
                if (evsResults.size() == 0) {
                    logger.debug("Not Found in NCIM.");
                } else {
                    MetaThesaurusConcept ncimConcept = (MetaThesaurusConcept) evsResults.get(0);
                    String cui = ncimConcept.getCui();
                    logger.debug("Found NCIM umls cui called " + cui);
                    String cuiPropertyName = "UMLS_CUI";
                    if (cui.startsWith("CL")) {
                        cuiPropertyName = "NCI_META_CUI";
                    }
                    evsQuery = new EVSQueryImpl();
                    int limit = 1;
                    evsQuery.searchDescLogicConcepts("NCI_Thesaurus", cui, limit, MATCH_BY_PROPERTY, cuiPropertyName, WITH_ALL_ATTRIBUTES);
                    evsResults = (List) appService.evsSearch(evsQuery);
                    if (evsResults.size() == 0) {
                        logger.debug("Cui " + cui + " not found in NCIT.");
                    } else {
                        logger.debug("Cui " + cui + " found in NCIT.");
                        ncitConcept = (DescLogicConcept) evsResults.get(0);
                        CaTIES_ConceptWrapper conceptWrapper = new CaTIES_ConceptWrapper();
                        conceptWrapper.setUserImplObject(ncitConcept);
                        conceptsTable.put(ncitCode, conceptWrapper);
                    }
                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ncitConcept != null) {
            }
        }
        return resourceData;
    }

    private void searchByCodeTest(String searchCode) {
        try {
            logger.debug("\n***********\nsearchByCodeTest\n***********\n\n");
            EVSQuery evsQuery = new EVSQueryImpl();
            int limit = 1;
            evsQuery.searchSourceByCode(searchCode, "NCI2006_06E");
            List evsResults = (List) appService.evsSearch(evsQuery);
            logger.debug("Search for code = " + searchCode + " has " + evsResults.size() + " results.");
            for (int i = 0; i < evsResults.size(); i++) {
                MetaThesaurusConcept concept = (MetaThesaurusConcept) evsResults.get(i);
                logger.debug("\n" + (i + 1) + ".Concept: " + concept.getName() + "\t" + concept.getCui());
                processMetaThesaurusConcept(concept);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void processMetaThesaurusConcept(MetaThesaurusConcept concept) {
        List sList = concept.getSourceCollection();
        logger.debug("\tSource-->" + sList.size());
        for (int y = 0; y < sList.size(); y++) {
            Source s = (Source) sList.get(y);
            logger.debug("\t - " + s.getAbbreviation());
        }
        List semanticList = concept.getSemanticTypeCollection();
        logger.debug("\tSemanticType---> count =" + semanticList.size());
        for (int z = 0; z < semanticList.size(); z++) {
            SemanticType sType = (SemanticType) semanticList.get(z);
            logger.debug("\t- Id: " + sType.getId() + "\n\t- Name : " + sType.getName());
        }
        List atomList = concept.getAtomCollection();
        logger.debug("\tAtoms -----> count = " + atomList.size());
        for (int i = 0; i < atomList.size(); i++) {
            Atom at = (Atom) atomList.get(i);
            logger.debug("\t -Code: " + at.getCode() + " -Name: " + at.getName() + " -LUI: " + at.getLui() + " -Source: " + at.getSource().getAbbreviation());
        }
        List synList = concept.getSynonymCollection();
        logger.debug("\tSynonyms -----> count = " + synList.size());
        for (int i = 0; i < synList.size(); i++) {
            logger.debug("\t - " + (String) synList.get(i));
        }
    }

    private void getMetaSourcesTest() {
        try {
            logger.debug("\n***********\n\tgetMetaTest\n***********\n\n");
            EVSQuery evsQuery = new EVSQueryImpl();
            evsQuery.getMetaSources();
            List evsResults = (List) appService.evsSearch(evsQuery);
            logger.debug("Search for MetaSources has " + evsResults.size() + " results.");
            for (int i = 0; i < evsResults.size(); i++) {
                Source metaSource = (Source) evsResults.get(i);
                String metaSourceDescription = metaSource.getDescription();
                String metaSourceAbbrev = metaSource.getAbbreviation();
                logger.debug("\n" + (i + 1) + ".MetaSource: " + metaSourceAbbrev + "\n\t" + metaSourceDescription);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void getVocabularyNamesTest() {
        try {
            EVSQuery evsQuery = new EVSQueryImpl();
            evsQuery.getVocabularyNames();
            List evsResults = (List) appService.evsSearch(evsQuery);
            logger.debug("Search for vocabularies has " + evsResults.size() + " results.");
            for (int i = 0; i < evsResults.size(); i++) {
                String vocabularyName = (String) evsResults.get(i);
                logger.debug("\n" + (i + 1) + ".Vocabulary: " + vocabularyName);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void getFrameNameTest() {
        logger.debug("\n***********\n\tgetFrameNameTest\n***********\n\n");
        for (int idx = 0; idx < pertinentCategoryArray.length; idx++) {
            String categoryName = pertinentCategoryArray[idx];
            runTest(categoryName, 1);
        }
    }

    private void runAllCategoriesByConceptCode() {
        for (int idx = 0; idx < pertinentCategoryByNCIConceptArray.length; idx++) {
            String categoryCode = pertinentCategoryByNCIConceptArray[idx];
            searchByCodeTest(categoryCode);
        }
        for (int idx = 0; idx < pertinentCategoryByCUIArray.length; idx++) {
            String categoryCode = pertinentCategoryByCUIArray[idx][0];
            String categoryProperty = pertinentCategoryByCUIArray[idx][1];
            searchByConceptTest(categoryCode, categoryProperty);
        }
    }

    private void searchByConceptTest(String searchConcept, String propertyName) {
        try {
            EVSQuery evsQuery = new EVSQueryImpl();
            int limit = 1;
            evsQuery.searchDescLogicConcepts("NCI_Thesaurus", searchConcept, limit, MATCH_BY_PROPERTY, propertyName, WITH_ALL_ATTRIBUTES);
            List evsResults = (List) appService.evsSearch(evsQuery);
            logger.debug("Search for searchTerm = " + searchConcept + " has " + evsResults.size() + " results.");
            for (int i = 0; i < evsResults.size(); i++) {
                DescLogicConcept concept = (DescLogicConcept) evsResults.get(i);
                logger.debug("\n" + (i + 1) + ".Concept: " + concept.getName() + "\t" + concept.getCode());
                processDescLogicConcept(concept);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void runTest(String searchTerm, int limit) {
        try {
            EVSQuery evsQuery = new EVSQueryImpl();
            evsQuery.searchDescLogicConcepts("NCI_Thesaurus", searchTerm, limit);
            List evsResults = (List) appService.evsSearch(evsQuery);
            for (int i = 0; i < evsResults.size(); i++) {
                DescLogicConcept concept = (DescLogicConcept) evsResults.get(i);
                logger.debug("\n" + (i + 1) + ".Concept: " + concept.getName() + "\t" + concept.getCode());
                processDescLogicConcept(concept);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void generateTreeTest(String className, int level) {
        try {
            EVSQuery evsQuery = new EVSQueryImpl();
            boolean traverseDown = true;
            boolean isaFlag = true;
            evsQuery.getTree("NCI_Thesaurus", className, traverseDown, isaFlag, AttributeSetDescriptor.WITH_ALL_ATTRIBUTES, level, new Vector());
            List evsResults = (List) appService.evsSearch(evsQuery);
            for (int i = 0; i < evsResults.size(); i++) {
                Object resultObj = evsResults.get(i);
                logger.debug("Got result object of class " + resultObj.getClass().getName());
                DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) resultObj;
                for (Enumeration nodes = rootNode.breadthFirstEnumeration(); nodes.hasMoreElements(); ) {
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) nodes.nextElement();
                    DescLogicConcept concept = (DescLogicConcept) currentNode.getUserImplObject();
                    processDescLogicConcept(concept);
                }
            }
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    private void processDescLogicConcept(DescLogicConcept concept) {
        processDescLogicConceptProperties(concept);
        EdgeProperties conceptEdgeProperties = concept.getEdgeProperties();
    }

    private void processAssociationsAndRoles(DescLogicConcept concept) {
        processDescLogicConceptAssociations(concept);
        processDescLogicConceptInverseAssociations(concept);
        processDescLogicConceptRoles(concept);
        processDescLogicConceptInverseRoles(concept);
    }

    private void processDescLogicConceptProperties(DescLogicConcept concept) {
        List pList = new ArrayList();
        pList = concept.getPropertyCollection();
        for (int x = 0; x < pList.size(); x++) {
            Property prop = (Property) pList.get(x);
            logger.debug("\tProperty :" + prop.getName() + "\t" + prop.getValue());
            List qList = prop.getQualifierCollection();
            for (int q = 0; q < qList.size(); q++) {
                Qualifier qual = (Qualifier) qList.get(q);
                logger.debug("\t\tQualifer " + qual.getName() + "\t" + qual.getValue());
            }
        }
    }

    private String getDescLogicProperty(String propertyName, DescLogicConcept concept) {
        String propertyValue = null;
        List pList = new ArrayList();
        pList = concept.getPropertyCollection();
        for (int x = 0; x < pList.size(); x++) {
            Property prop = (Property) pList.get(x);
            if (prop.getName().equals(propertyName)) {
                propertyValue = prop.getValue();
                break;
            }
        }
        return propertyValue;
    }

    private void processDescLogicConceptAssociations(DescLogicConcept concept) {
        List pList = new ArrayList();
        pList = concept.getAssociationCollection();
        logger.debug("Number of Associations: " + pList.size());
        for (int x = 0; x < pList.size(); x++) {
            Association ass = (Association) pList.get(x);
            logger.debug("\tAssociation :" + ass.getName() + "\t" + ass.getValue());
            List qList = ass.getQualifierCollection();
            for (int q = 0; q < qList.size(); q++) {
                Qualifier qual = (Qualifier) qList.get(q);
                logger.debug("\t\tQualifer " + qual.getName() + "\t" + qual.getValue());
            }
        }
    }

    private void processDescLogicConceptInverseAssociations(DescLogicConcept concept) {
        List pList = new ArrayList();
        pList = concept.getInverseAssociationCollection();
        for (int x = 0; x < pList.size(); x++) {
            Association ass = (Association) pList.get(x);
            logger.debug("\tAssociation :" + ass.getName() + "\t" + ass.getValue());
            List qList = ass.getQualifierCollection();
            for (int q = 0; q < qList.size(); q++) {
                Qualifier qual = (Qualifier) qList.get(q);
                logger.debug("\t\tQualifer " + qual.getName() + "\t" + qual.getValue());
            }
        }
    }

    private void processDescLogicConceptRoles(DescLogicConcept concept) {
        List pList = new ArrayList();
        pList = concept.getRoleCollection();
        logger.debug("Number of roles: " + pList.size());
        for (int x = 0; x < pList.size(); x++) {
            Role role = (Role) pList.get(x);
            logger.debug("\tRole :" + role.getName() + "\t" + role.getValue());
        }
    }

    private void processDescLogicConceptInverseRoles(DescLogicConcept concept) {
        List pList = new ArrayList();
        pList = concept.getInverseRoleCollection();
        for (int x = 0; x < pList.size(); x++) {
            Role role = (Role) pList.get(x);
            logger.debug("\tRole :" + role.getName() + "\t" + role.getValue());
        }
    }

    public static void main(String[] args) {
        new CaTIES_ThesaurusTest();
    }
}
