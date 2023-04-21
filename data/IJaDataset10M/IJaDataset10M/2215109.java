package edu.upmc.opi.caBIG.caTIES.lexbig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.ObjectToString;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.concepts.Entity;

public class CaTIES_LexBIGTester {

    private static LexBIGService lbSvc = null;

    private static String NCI_THESAURUS_SCHEME = "NCI_Thesaurus";

    private static boolean retrieveOnlyActiveConcepts = true;

    private static String RELATIONS_NAME = "relations";

    private static int TRAVERSE_FORWARD = 1;

    private static int TRAVERSE_BACKWARD = -1;

    private static SortOptionList NO_SORTING = null;

    private static CodedNodeSet.PropertyType[] IGNORE_PROPERTY_TYPES = null;

    private static NameAndValueList NO_ASSOCIATION_QUALIFIERS = null;

    private static LocalNameList USE_ALL_PROPERTIES = null;

    private static int UNLIMITED = -1;

    private static CodingSchemeVersionOrTag NCI_THESAURUS_VERSION = null;

    private static LocalNameList conceptProperties = ConvenienceMethods.createLocalNameList(new String[] { "CONCEPT_NAME", "SEMANTIC_TYPE", "UMLS_CUI", "NCI_META_CUI" });

    private static String[] domainRelationships = { "Disease_Has_Associated_Anatomic_Site", "Disease_Has_Finding", "Disease_Has_Metastatic_Anatomic_Site", "Disease_Has_Primary_Anatomic_Site", "Disease_Is_Grade", "Disease_Is_Stage", "Procedure_Has_Completely_Excised_Anatomy", "Procedure_Has_Excised_Anatomy", "Procedure_Has_Partially_Excised_Anatomy", "Procedure_Has_Target_Anatomy" };

    private static String[] breastCAPConcepts = { "C9245", "C0549184", "C12300", "C12301", "C12302", "C12303", "C12304", "C12971", "C16958", "C17649", "C18000", "C19157", "C25595", "C25626", "C2924", "C36180", "C38046", "C4741", "C47860", "C47861", "C48190", "C48616", "C48620", "C48621", "C48658", "C48661", "C48739", "C48880", "C48881", "C48882" };

    private static String breastDisorderID = "C26709";

    private static String biopsyOfBreastID = "C51698";

    private static String breastCarcinomaID = "C4872";

    private static String breastAdenocarcinomaID = "C5214";

    private static String breastCancerTreatmentID = "C15774";

    private static String metastaticNeoplasmToTheBone = "C3580";

    private static String metastaticMalignantNeoplasm = "C36263";

    private static String[] breastCancerFundamentals = { breastDisorderID, biopsyOfBreastID, breastCancerTreatmentID, breastCarcinomaID, breastAdenocarcinomaID, metastaticMalignantNeoplasm, metastaticNeoplasmToTheBone };

    private static CodedNodeSet conceptAccumulator = null;

    private static String propertyBundleFile;

    private static ResourceBundle resourceBundle;

    private static BufferedOutputStream outputStream;

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        new CaTIES_LexBIGTester();
    }

    public CaTIES_LexBIGTester() {
        setPropertyBundleFile("CaTIES_LexBIGTester");
        setProperties();
        try {
            String outputFileName = resourceBundle.getString("outputFileName");
            File outputFile = new File(outputFileName);
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
        } catch (Exception x) {
            System.err.println("[CaTIES_ThesaurusBuilder] Failed opening output file.");
        }
        try {
            lbSvc = new LexBIGServiceImpl();
            reportCodingSchemes();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            outputStream.flush();
            outputStream.close();
        } catch (Exception x) {
            System.err.println("[CaTIES_ThesaurusBuilder] Failed opening output file.");
        }
    }

    private static void printCls(CaTIES_LexBIGConcept ontClass) {
        String jessCommand = "(bind ?" + ontClass.getConceptName();
        jessCommand += " (add-owl-cls ";
        jessCommand += "\"" + ontClass.getConceptName() + "\"" + " ";
        jessCommand += "\"" + ontClass.getConceptID() + "\"" + " ";
        jessCommand += "\"" + ontClass.getConceptCUI() + "\"" + " ";
        jessCommand += "\"" + ontClass.getConceptTUI() + "\"" + " ";
        jessCommand += "))\n";
        printJessCommand(jessCommand);
    }

    private static void reportCodingSchemes() {
        try {
            CodingSchemeRenderingList schemes = lbSvc.getSupportedCodingSchemes();
            System.out.println("There are " + schemes.getCodingSchemeRenderingCount() + " coding schemes.");
            for (int idx = 0; idx < schemes.getCodingSchemeRenderingCount(); idx++) {
                CodingSchemeRendering csr = schemes.getCodingSchemeRendering(idx);
                System.out.println(ObjectToString.toString(csr.getCodingSchemeSummary()));
            }
            for (int idx = 0; idx < schemes.getCodingSchemeRenderingCount(); idx++) {
                CodingSchemeRendering csr = schemes.getCodingSchemeRendering(idx);
                System.out.println(ObjectToString.toString(csr.getCodingSchemeSummary()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createRelationships() {
        for (int idx = 0; idx < domainRelationships.length; idx++) {
            String objectPropertyName = domainRelationships[idx];
            String jessCommand = "(bind ?" + objectPropertyName;
            jessCommand += " (add-owl-relationship ";
            jessCommand += "\"" + objectPropertyName + "\"))\n";
            printJessCommand(jessCommand);
        }
    }

    private static void printJessCommand(String jessCommand) {
        try {
            byte[] jessCommandAsBytes = jessCommand.getBytes();
            outputStream.write(jessCommandAsBytes);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public static String getPropertyBundleFile() {
        return propertyBundleFile;
    }

    public static void setPropertyBundleFile(String propertyBundleFileInput) {
        propertyBundleFile = propertyBundleFileInput;
    }

    protected static void setProperties() {
        try {
            resourceBundle = PropertyResourceBundle.getBundle(propertyBundleFile);
            System.out.println("Properties are: ");
            for (Enumeration keys = resourceBundle.getKeys(); keys.hasMoreElements(); ) {
                String key = (String) keys.nextElement();
                String value = String.valueOf(resourceBundle.getObject(key));
                System.out.println("\t" + key + " ==> " + value);
                System.getProperties().setProperty(key, value);
            }
        } catch (Exception x) {
            String errorMessage = "[CaTIES_LexBIGTester] " + propertyBundleFile + " NOT FOUND!";
            System.out.println(errorMessage);
        }
    }

    private static void reportCapProtocolConcepts() throws Exception {
        System.out.println("There are " + breastCAPConcepts.length + " concepts in the breast caDSR");
        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(breastCAPConcepts, NCI_THESAURUS_SCHEME);
        CodedNodeSet nodes = lbSvc.getCodingSchemeConcepts(NCI_THESAURUS_SCHEME, NCI_THESAURUS_VERSION, retrieveOnlyActiveConcepts).restrictToCodes(crefs);
        ResolvedConceptReferenceList matches = resolvedMatches(nodes, UNLIMITED);
        getChildrenForMatches(matches);
        System.out.println("\n\nAfter getChildrenForMatches.");
        reportCodedNodeSetIteratively(conceptAccumulator);
        getAncestorsForMatches(matches);
        System.out.println("\n\nAfter getAncestorsForMatches.");
        reportCodedNodeSetIteratively(conceptAccumulator);
    }

    private static void reportCodedNodeSetIteratively(CodedNodeSet conceptAccumulator) throws Exception {
        ResolvedConceptReferencesIterator iterator = conceptAccumulator.resolve(NO_SORTING, conceptProperties, null);
        int idx = 0;
        while (iterator.hasNext()) {
            ResolvedConceptReference ref = (ResolvedConceptReference) iterator.next();
            Entity entry = ref.getReferencedEntry();
            reportCodedEntry(idx++ + ") ", entry);
        }
    }

    private static void cacheCodedNodeSet(CodedNodeSet codedNodeSet) throws Exception {
        ResolvedConceptReferencesIterator iterator = codedNodeSet.resolve(NO_SORTING, null, null);
        while (iterator.hasNext()) {
            ResolvedConceptReference ref = (ResolvedConceptReference) iterator.next();
            CaTIES_LexBIGConcept conceptToCache = new CaTIES_LexBIGConcept();
            conceptToCache.setResolvedConceptReference(ref);
            conceptToCache.setBackChainable(true);
            conceptToCache.setForwardChainable(true);
            conceptToCache.setDomainRelationChainable(true);
            CaTIES_LexBIGCacher.getInstance().cacheConcept(conceptToCache);
        }
    }

    private static void getChildrenForMatches(ResolvedConceptReferenceList matches) throws Exception {
        if (matches.getResolvedConceptReferenceCount() > 0) {
            for (int idx = 0; idx < matches.getResolvedConceptReferenceCount(); idx++) {
                ResolvedConceptReference ref = matches.getResolvedConceptReference(idx);
                getChildrenQuery(ref);
            }
        } else {
            System.out.println("No match found!");
        }
    }

    private static void getChildrenQuery(ResolvedConceptReference graphFocus) throws Exception {
        System.out.println("Getting children of " + graphFocus.getConceptCode());
        CodedNodeGraph conceptNodeGraph = lbSvc.getNodeGraph(NCI_THESAURUS_SCHEME, NCI_THESAURUS_VERSION, RELATIONS_NAME);
        conceptNodeGraph.restrictToAssociations(getHasSubtypeAssociationList(), NO_ASSOCIATION_QUALIFIERS);
        boolean resolveForward = true;
        boolean resolveBackward = false;
        int resolveCodedEntryDepth = 10;
        int resolveAssociationDepth = 1000;
        int maxToReturn = 25;
        CodedNodeSet codedNodeSet = conceptNodeGraph.toNodeList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth);
        accumulateCodedNodeSet(codedNodeSet);
        ResolvedConceptReferenceList list = conceptNodeGraph.resolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth, conceptProperties, IGNORE_PROPERTY_TYPES, NO_SORTING, maxToReturn);
        reportResolvedConceptReferenceAssociations(list, TRAVERSE_FORWARD);
    }

    private static void getAncestorsForMatches(ResolvedConceptReferenceList matches) throws Exception {
        if (matches.getResolvedConceptReferenceCount() > 0) {
            for (int idx = 0; idx < matches.getResolvedConceptReferenceCount(); idx++) {
                ResolvedConceptReference ref = matches.getResolvedConceptReference(idx);
                getAncestorQuery(ref);
            }
        } else {
            System.out.println("No match found!");
        }
    }

    private static void getAncestorQuery(ConceptReference graphFocus) throws Exception {
        System.out.println("Getting ancestors of " + graphFocus.getConceptCode());
        CodedNodeGraph conceptNodeGraph = lbSvc.getNodeGraph(NCI_THESAURUS_SCHEME, NCI_THESAURUS_VERSION, RELATIONS_NAME);
        conceptNodeGraph.restrictToAssociations(getHasSubtypeAssociationList(), NO_ASSOCIATION_QUALIFIERS);
        boolean resolveForward = false;
        boolean resolveBackward = true;
        int resolveCodedEntryDepth = 10;
        int resolveAssociationDepth = UNLIMITED;
        int maxToReturn = UNLIMITED;
        CodedNodeSet codedNodeSet = conceptNodeGraph.toNodeList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth);
        accumulateCodedNodeSet(codedNodeSet);
        ResolvedConceptReferenceList list = conceptNodeGraph.resolveAsList(graphFocus, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth, conceptProperties, IGNORE_PROPERTY_TYPES, NO_SORTING, maxToReturn);
        reportResolvedConceptReferenceAssociations(list, TRAVERSE_BACKWARD);
    }

    private static void accumulateCodedNodeSet(CodedNodeSet codedNodeSet) throws Exception {
        if (conceptAccumulator == null) {
            conceptAccumulator = codedNodeSet;
        } else {
            conceptAccumulator = conceptAccumulator.union(codedNodeSet);
        }
    }

    private static NameAndValueList getHasSubtypeAssociationList() {
        NameAndValue associationNameAndValue = new NameAndValue();
        associationNameAndValue.setName("hasSubtype");
        NameAndValueList associationNameAndValueList = new NameAndValueList();
        associationNameAndValueList.addNameAndValue(associationNameAndValue);
        return associationNameAndValueList;
    }

    private static NameAndValueList getDomainRelationsList() {
        NameAndValueList associationNameAndValueList = new NameAndValueList();
        for (int idx = 0; idx < domainRelationships.length; idx++) {
            String domainRelation = domainRelationships[idx];
            NameAndValue associationNameAndValue = new NameAndValue();
            associationNameAndValue.setName(domainRelation);
            associationNameAndValueList.addNameAndValue(associationNameAndValue);
        }
        return associationNameAndValueList;
    }

    private static void reportResolvedConceptReferenceAssociations(ResolvedConceptReferenceList list, int direction) {
        ResolvedConceptReference[] resolvedConceptReferenceArray = list.getResolvedConceptReference();
        if (resolvedConceptReferenceArray.length == 0) {
            System.out.println("No concepts found.");
        } else {
            for (int idx = 0; idx < resolvedConceptReferenceArray.length; idx++) {
                ResolvedConceptReference resolvedConcept = resolvedConceptReferenceArray[idx];
                Entity entry = resolvedConcept.getReferencedEntry();
                reportCodedEntry("", entry);
                AssociationList associationList = null;
                if (direction < 0) {
                    associationList = resolvedConcept.getTargetOf();
                } else {
                    associationList = resolvedConcept.getSourceOf();
                }
                reportAssociationList("", associationList, direction);
            }
        }
    }

    private static void reportAssociationList(String prefix, AssociationList associationList, int direction) {
        if (associationList != null) {
            for (int idx = 0; idx < associationList.getAssociationCount(); idx++) {
                Association association = associationList.getAssociation(idx);
                AssociatedConceptList associatedConceptList = association.getAssociatedConcepts();
                for (int jdx = 0; jdx < associatedConceptList.getAssociatedConceptCount(); jdx++) {
                    AssociatedConcept associatedConcept = associatedConceptList.getAssociatedConcept(jdx);
                    reportCodedEntry(prefix, associatedConcept.getReferencedEntry());
                    AssociationList childAssociationList = null;
                    if (direction < 0) {
                        childAssociationList = associatedConcept.getTargetOf();
                    } else {
                        childAssociationList = associatedConcept.getSourceOf();
                    }
                    reportAssociationList(prefix + "\t", childAssociationList, direction);
                }
            }
        }
    }

    private static void reportCodedEntry(String prefix, Entity entry) {
        System.out.print(prefix);
        for (int jdx = 0; jdx < entry.getPropertyCount(); jdx++) {
            System.out.print(" " + entry.getProperty(jdx).getValue().getContent());
        }
        System.out.println();
    }

    private static ResolvedConceptReferenceList resolvedMatches(CodedNodeSet nodes, int maxToReturn) throws Exception {
        ResolvedConceptReferenceList matches = nodes.resolveToList(NO_SORTING, conceptProperties, IGNORE_PROPERTY_TYPES, maxToReturn);
        return matches;
    }

    private static void reportResolvedMatches(ResolvedConceptReferenceList matches) throws Exception {
        if (matches.getResolvedConceptReferenceCount() > 0) {
            for (int idx = 0; idx < matches.getResolvedConceptReferenceCount(); idx++) {
                ResolvedConceptReference ref = matches.getResolvedConceptReference(idx);
                Entity entry = ref.getReferencedEntry();
                reportCodedEntry(idx + ") ", entry);
            }
        } else {
            System.out.println("No match found!");
        }
    }

    private static void generateFactBase() throws Exception {
        printJessCommand(";/***********************************/\n");
        printJessCommand(";/**          Relationships        **/\n");
        printJessCommand(";/***********************************/\n");
        printJessCommand("(bind ?ancestor-of (add-owl-relationship \"ancestor-of\"))\n");
        printJessCommand("(bind ?descendant-of (add-owl-relationship \"descendant-of\"))\n");
        printJessCommand("(bind ?ancestorRelationship ?ancestor-of)\n");
        printJessCommand("(bind ?descendantRelationship ?descendant-of)\n");
        createRelationships();
        ConceptReferenceList crefs = ConvenienceMethods.createConceptReferenceList(breastCancerFundamentals, NCI_THESAURUS_SCHEME);
        CodedNodeSet kernelNodes = lbSvc.getCodingSchemeConcepts(NCI_THESAURUS_SCHEME, NCI_THESAURUS_VERSION, retrieveOnlyActiveConcepts).restrictToCodes(crefs);
        cacheCodedNodeSet(kernelNodes);
        int iterations = 0;
        int conceptBatchSize = 10000;
        while (iterations < conceptBatchSize) {
            iterations++;
            CaTIES_LexBIGConcept conceptCodeToVisit = CaTIES_LexBIGCacher.getInstance().getNextUnprocessedConcept();
            if (conceptCodeToVisit == null) {
                System.out.println("No more unprocessed concepts.");
                System.out.println("Size of the concept table is " + CaTIES_LexBIGCacher.getInstance().getConceptTable().size());
                break;
            }
            if (conceptCodeToVisit.isBackChainable()) {
                queueParents(conceptCodeToVisit);
            }
            if (conceptCodeToVisit.isForwardChainable()) {
                queueChildren(conceptCodeToVisit);
            }
            if (conceptCodeToVisit.isDomainRelationChainable()) {
                queueDomainRestrictions(conceptCodeToVisit);
            }
            conceptCodeToVisit.setProcessed(true);
            if (CaTIES_LexBIGCacher.getInstance().getConceptTable().size() % 100 == 0) {
                System.out.println("Size of the concept table is " + CaTIES_LexBIGCacher.getInstance().getConceptTable().size());
            }
        }
        CaTIES_LexBIGCacher.getInstance().inheritRestrictions();
        Hashtable conceptsTable = CaTIES_LexBIGCacher.getInstance().getConceptTable();
        printJessCommand(";/***********************************/\n");
        printJessCommand(";/**          Classes              **/\n");
        printJessCommand(";/***********************************/\n");
        for (Iterator keysIterator = conceptsTable.keySet().iterator(); keysIterator.hasNext(); ) {
            String conceptCodeName = (String) keysIterator.next();
            CaTIES_LexBIGConcept conceptCodeToVisit = (CaTIES_LexBIGConcept) conceptsTable.get(conceptCodeName);
            printJessCommand(conceptCodeToVisit.getJessCommand());
        }
        printJessCommand(";/***********************************/\n");
        printJessCommand(";/**            Isa                **/\n");
        printJessCommand(";/***********************************/\n");
        ArrayList isaTable = CaTIES_LexBIGCacher.getInstance().getIsaTable();
        for (Iterator iterator = isaTable.iterator(); iterator.hasNext(); ) {
            CaTIES_LexBIGIsa isaRelation = (CaTIES_LexBIGIsa) iterator.next();
            printJessCommand(isaRelation.getJessCommand());
        }
        printJessCommand(";/***********************************/\n");
        printJessCommand(";/**          Relations            **/\n");
        printJessCommand(";/***********************************/\n");
        ArrayList relationsTable = CaTIES_LexBIGCacher.getInstance().getDomainRelationsTable();
        for (Iterator iterator = relationsTable.iterator(); iterator.hasNext(); ) {
            CaTIES_LexBIGRestriction domainRelation = (CaTIES_LexBIGRestriction) iterator.next();
            printJessCommand(domainRelation.getJessCommand());
        }
        System.out.println(CaTIES_LexBIGCacher.getInstance().toString());
    }

    private static void printIsaRelation(CaTIES_LexBIGConcept parentConcept) {
        ResolvedConceptReference resolvedparentConcept = parentConcept.getResolvedConceptReference();
        AssociationList associationList = resolvedparentConcept.getSourceOf();
        if (associationList != null) {
            for (int idx = 0; idx < associationList.getAssociationCount(); idx++) {
                Association association = associationList.getAssociation(idx);
                String associationName = association.getAssociationName();
                if (!associationName.equals("hasSubtype")) {
                    continue;
                }
                AssociatedConceptList associatedConceptList = association.getAssociatedConcepts();
                for (int jdx = 0; jdx < associatedConceptList.getAssociatedConceptCount(); jdx++) {
                    AssociatedConcept associatedConcept = associatedConceptList.getAssociatedConcept(jdx);
                    CaTIES_LexBIGConcept childConcept = (CaTIES_LexBIGConcept) CaTIES_LexBIGCacher.getInstance().getConceptTable().get(associatedConcept.getConceptCode());
                    printIsaRelation(childConcept.getConceptName(), parentConcept.getConceptName());
                }
            }
        }
    }

    private static void printRelations(CaTIES_LexBIGConcept sourceConcept) {
        ResolvedConceptReference resolvedSourceConcept = sourceConcept.getResolvedConceptReference();
        AssociationList associationList = resolvedSourceConcept.getSourceOf();
        if (associationList != null) {
            for (int idx = 0; idx < associationList.getAssociationCount(); idx++) {
                Association association = associationList.getAssociation(idx);
                String associationName = association.getAssociationName();
                if (associationName.equals("hasSubtype")) {
                    continue;
                }
                AssociatedConceptList associatedConceptList = association.getAssociatedConcepts();
                for (int jdx = 0; jdx < associatedConceptList.getAssociatedConceptCount(); jdx++) {
                    AssociatedConcept associatedConcept = associatedConceptList.getAssociatedConcept(jdx);
                    CaTIES_LexBIGConcept targetConcept = (CaTIES_LexBIGConcept) CaTIES_LexBIGCacher.getInstance().getConceptTable().get(associatedConcept.getConceptCode());
                    printRelation(sourceConcept.getConceptName(), associationName, targetConcept.getConceptName());
                }
            }
        }
    }

    private static void printIsaRelation(String parentConceptName, String childConceptName) {
        String jessCommand = "";
        jessCommand += " (add-owl-isa ";
        jessCommand += "?" + childConceptName + " ";
        jessCommand += "?" + parentConceptName + " ";
        jessCommand += "?ancestorRelationship ?descendantRelationship";
        jessCommand += ")\n";
        printJessCommand(jessCommand);
    }

    private static void printRelation(String sourceConceptName, String relationName, String targetConceptName) {
        String jessCommand = "";
        jessCommand += " (add-owl-relation ";
        jessCommand += "?" + sourceConceptName + " ";
        jessCommand += "?" + relationName + " ";
        jessCommand += "?" + targetConceptName + " ";
        jessCommand += ")\n";
        printJessCommand(jessCommand);
    }

    private static void queueDomainRestrictions(CaTIES_LexBIGConcept sourceCode) throws Exception {
        boolean resolveForward = true;
        boolean resolveBackward = false;
        int resolveCodedEntryDepth = 2;
        int resolveAssociationDepth = UNLIMITED;
        System.out.println("Queueing domain restrictions for " + sourceCode.getConceptName() + " " + sourceCode.getConceptID());
        CodedNodeGraph codedNodeGraph = lbSvc.getNodeGraph(NCI_THESAURUS_SCHEME, NCI_THESAURUS_VERSION, RELATIONS_NAME);
        codedNodeGraph.restrictToAssociations(getDomainRelationsList(), NO_ASSOCIATION_QUALIFIERS);
        ResolvedConceptReference sourceResolvedCode = sourceCode.getResolvedConceptReference();
        ResolvedConceptReferenceList sourceList = codedNodeGraph.resolveAsList(sourceResolvedCode, resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth, USE_ALL_PROPERTIES, IGNORE_PROPERTY_TYPES, NO_SORTING, 20);
        for (int idx = 0; idx < sourceList.getResolvedConceptReferenceCount(); idx++) {
            sourceResolvedCode = sourceList.getResolvedConceptReference(idx);
            System.out.println(sourceResolvedCode.getConceptCode());
            AssociationList associationList = sourceResolvedCode.getSourceOf();
            if (associationList != null) {
                for (int jdx = 0; jdx < associationList.getAssociationCount(); jdx++) {
                    Association association = associationList.getAssociation(jdx);
                    String associationName = association.getAssociationName();
                    AssociatedConceptList associatedConceptList = association.getAssociatedConcepts();
                    for (int kdx = 0; kdx < associatedConceptList.getAssociatedConceptCount(); kdx++) {
                        AssociatedConcept associatedConcept = associatedConceptList.getAssociatedConcept(kdx);
                        CaTIES_LexBIGConcept targetCode = new CaTIES_LexBIGConcept();
                        targetCode.setResolvedConceptReference(associatedConcept);
                        targetCode.setBackChainable(true);
                        targetCode.setForwardChainable(false);
                        targetCode.setDomainRelationChainable(true);
                        targetCode = CaTIES_LexBIGCacher.getInstance().cacheConcept(targetCode);
                        CaTIES_LexBIGRestriction domainRestriction = new CaTIES_LexBIGRestriction();
                        domainRestriction.setSourceConcept(sourceCode);
                        domainRestriction.setRelationName(associationName);
                        domainRestriction.setTargetConcept(targetCode);
                        sourceCode.addDomainForRestriction(domainRestriction);
                        targetCode.addRangeForRestriction(domainRestriction);
                        CaTIES_LexBIGCacher.getInstance().cacheRestriction(domainRestriction);
                    }
                }
            }
        }
    }

    private static void queueParents(CaTIES_LexBIGConcept childCode) throws Exception {
        boolean resolveForward = false;
        boolean resolveBackward = true;
        int resolveCodedEntryDepth = 1;
        int resolveAssociationDepth = UNLIMITED;
        CodedNodeGraph codedNodeGraph = lbSvc.getNodeGraph(NCI_THESAURUS_SCHEME, NCI_THESAURUS_VERSION, RELATIONS_NAME);
        codedNodeGraph.restrictToAssociations(getHasSubtypeAssociationList(), NO_ASSOCIATION_QUALIFIERS);
        CodedNodeSet parents = codedNodeGraph.toNodeList(childCode.getResolvedConceptReference(), resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth);
        ResolvedConceptReferencesIterator iterator = parents.resolve(NO_SORTING, null, null);
        while (iterator.hasNext()) {
            ResolvedConceptReference parentResolvedCode = (ResolvedConceptReference) iterator.next();
            if (!parentResolvedCode.getConceptCode().equals(childCode.getConceptID())) {
                CaTIES_LexBIGConcept parentCode = new CaTIES_LexBIGConcept();
                parentCode.setResolvedConceptReference(parentResolvedCode);
                parentCode.setBackChainable(true);
                parentCode.setForwardChainable(false);
                parentCode.setDomainRelationChainable(true);
                parentCode = CaTIES_LexBIGCacher.getInstance().cacheConcept(parentCode);
                CaTIES_LexBIGIsa isaRelation = new CaTIES_LexBIGIsa();
                isaRelation.setChildConcept(childCode);
                isaRelation.setParentConcept(parentCode);
                parentCode.addParentRoleRelation(isaRelation);
                childCode.addChildRoleRelation(isaRelation);
                CaTIES_LexBIGCacher.getInstance().cacheIsa(isaRelation);
            }
        }
    }

    private static void queueChildren(CaTIES_LexBIGConcept parentCode) throws Exception {
        boolean resolveForward = true;
        boolean resolveBackward = false;
        int resolveCodedEntryDepth = 1;
        int resolveAssociationDepth = UNLIMITED;
        CodedNodeGraph codedNodeGraph = lbSvc.getNodeGraph(NCI_THESAURUS_SCHEME, NCI_THESAURUS_VERSION, RELATIONS_NAME);
        codedNodeGraph.restrictToAssociations(getHasSubtypeAssociationList(), NO_ASSOCIATION_QUALIFIERS);
        CodedNodeSet children = codedNodeGraph.toNodeList(parentCode.getResolvedConceptReference(), resolveForward, resolveBackward, resolveCodedEntryDepth, resolveAssociationDepth);
        ResolvedConceptReferencesIterator iterator = children.resolve(NO_SORTING, USE_ALL_PROPERTIES, null);
        while (iterator.hasNext()) {
            ResolvedConceptReference childResolvedNode = (ResolvedConceptReference) iterator.next();
            if (!childResolvedNode.getConceptCode().equals(parentCode.getConceptID())) {
                CaTIES_LexBIGConcept childCode = new CaTIES_LexBIGConcept();
                childCode.setResolvedConceptReference(childResolvedNode);
                childCode.setBackChainable(false);
                childCode.setForwardChainable(true);
                childCode.setDomainRelationChainable(true);
                childCode = CaTIES_LexBIGCacher.getInstance().cacheConcept(childCode);
                CaTIES_LexBIGIsa isaRelation = new CaTIES_LexBIGIsa();
                isaRelation.setChildConcept(childCode);
                isaRelation.setParentConcept(parentCode);
                parentCode.addParentRoleRelation(isaRelation);
                childCode.addChildRoleRelation(isaRelation);
                CaTIES_LexBIGCacher.getInstance().cacheIsa(isaRelation);
            }
        }
    }
}
