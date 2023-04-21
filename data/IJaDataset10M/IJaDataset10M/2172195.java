package edu.upmc.opi.caBIG.caTIES.installer.pipes.creole;

import edu.upmc.opi.caBIG.caTIES.server.index.CaTIES_SectionNameMangler;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Constructs a heirarchical dependency between report elements.
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id: CaTIES_PhysicalModelDeducerPR.java,v 1.1 2005/12/06 18:51:45
 * mitchellkj Exp $
 * @since 1.4.2_04
 */
public class CaTIES_PhysicalModelDeducerPR extends AbstractLanguageAnalyser {

    /**
	 * Field serialVersionUID. (value is "-1863322709982419803L ;")
	 */
    private static final long serialVersionUID = -1863322709982419803L;

    /**
     * Field categoryThenDocumentOrder.
     */
    protected Comparator categoryThenDocumentOrder = new CaTIES_CategoryThenDocumentOrder();

    /**
     * Field documentOrder.
     */
    protected Comparator documentOrder = new CaTIES_Comparator();

    /**
     * Field reportParts.
     */
    protected TreeSet reportParts = new TreeSet(new Comparator() {

        public int compare(Object o1, Object o2) {
            Integer partNumber1 = (Integer) o1;
            Integer partNumber2 = (Integer) o2;
            return partNumber1.intValue() - partNumber2.intValue();
        }
    });

    /**
     * Field sortedSpecimens.
     */
    protected CaTIES_SortedAnnotationSet sortedSpecimens = null;

    /**
     * Field defaultAnnotations.
     */
    protected AnnotationSet defaultAnnotations = null;

    /**
     * Field classifierAnnotations.
     */
    protected AnnotationSet classifierAnnotations = null;

    /**
     * Field debugging.
     */
    protected boolean debugging = false;

    /**
     * Field FINAL_DIAGNOSIS. (value is ""FINAL DIAGNOSIS:" ")
     */
    protected static final String FINAL_DIAGNOSIS = CaTIES_SectionNameMangler.getInstance().convertFromPrettyFormatToLegacyFormat("Final Diagnosis");

    /**
     * Field GROSS_DESCRIPTION. (value is ""GROSS DESCRIPTION:" ")
     */
    protected static final String GROSS_DESCRIPTION = CaTIES_SectionNameMangler.getInstance().convertFromPrettyFormatToLegacyFormat("Gross Description");

    /**
     * Field ANY_DIAGNOSIS. (value is ""DIAGNOSIS:" ")
     */
    protected static final String ANY_DIAGNOSIS = CaTIES_SectionNameMangler.getInstance().convertFromPrettyFormatToLegacyFormat("Diagnosis");

    /**
     * Field ANY_GROSS. (value is ""GROSS" ")
     */
    protected static final String ANY_GROSS = "GROSS";

    /**
     * Field CATEGORY_ORGAN. (value is ""ORGAN" ")
     */
    protected static final String CATEGORY_ORGAN = "ORGAN";

    /**
     * Field COMPLEMENT. (value is false)
     */
    protected static final boolean COMPLEMENT = true;

    /**
     * Constructor for CaTIES_PhysicalModelDeducerPR.
     */
    public CaTIES_PhysicalModelDeducerPR() {
    }

    /**
     * Method getDebugging.
     * 
     * @return Boolean
     */
    public Boolean getDebugging() {
        return new Boolean(this.debugging);
    }

    /**
     * Method setDebugging.
     * 
     * @param debugging Boolean
     */
    public void setDebugging(Boolean debugging) {
        this.debugging = debugging.booleanValue();
    }

    /**
     * Method reInit.
     * 
     * @throws ResourceInstantiationException the resource instantiation exception
     */
    public void reInit() throws ResourceInstantiationException {
        super.reInit();
    }

    /**
     * Method execute.
     */
    public void execute() {
        try {
            this.defaultAnnotations = this.document.getAnnotations();
            this.classifierAnnotations = this.document.getAnnotations("Classifier");
            categorizeAnnotations();
            preprocessFindings();
            convertToReportElements();
            identifyKeyElements(FINAL_DIAGNOSIS, ANY_DIAGNOSIS);
            identifyKeyElements(GROSS_DESCRIPTION, ANY_GROSS);
            connectFindings();
            adjustUnitsOfMeasure();
            createSpecimens();
            linkModifiers();
            findAcquisitionProcedure();
            linkOrgansToDiagnosis();
            linkDiagnosisToSpecimen();
            if (this.debugging) {
                System.out.println("\n\n[CaTIES_PhysicalModelDeducerPR] AFTER DIAGNOSIS LINKING....");
                sortSpecimens();
                printSpecimenTree();
            }
            linkGrossMeasurements();
            if (this.debugging) {
                System.out.println("\n\n[CaTIES_PhysicalModelDeducerPR] AFTER GROSS MEASUREMENTS LINKING....");
                sortSpecimens();
                printSpecimenTree();
            }
            linkGrossOrgansToSpecimens();
            if (this.debugging) {
                System.out.println("\n\n[CaTIES_PhysicalModelDeducerPR] AFTER GROSS ORGAN LINKING....");
                printSpecimenTree();
            }
            if (this.debugging) {
                sortSpecimens();
                printSpecimenTree();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method printSpecimenTree.
     */
    protected void printSpecimenTree() {
        if (this.sortedSpecimens != null) {
            for (Iterator specimenIterator = this.sortedSpecimens.iterator(); specimenIterator.hasNext(); ) {
                Annotation specimenAnnotation = (Annotation) specimenIterator.next();
                printRecursively(specimenAnnotation, "");
            }
        }
    }

    /**
     * Method printRecursively.
     * 
     * @param annotation Annotation
     * @param indentationControl String
     */
    protected void printRecursively(Annotation annotation, String indentationControl) {
        FeatureMap annotationFeatures = annotation.getFeatures();
        String category = (String) annotationFeatures.get("category");
        String term = (String) annotationFeatures.get("term");
        String part = (String) annotationFeatures.get("part");
        if (category != null) {
            System.out.println(indentationControl + category + "<" + part + ">" + " ==> " + term);
        }
        Annotation acquisitionProcedure = (Annotation) annotationFeatures.get("acquisition-procedure");
        if (acquisitionProcedure != null) {
            printRecursively(acquisitionProcedure, indentationControl + "\t");
        }
        TreeSet grossDescriptions = (TreeSet) annotationFeatures.get("gross-description");
        if (grossDescriptions != null) {
            for (Iterator iterator = grossDescriptions.iterator(); iterator.hasNext(); ) {
                Annotation grossDescription = (Annotation) iterator.next();
                printRecursively(grossDescription, indentationControl + "\t");
            }
        }
        TreeSet constituents = (TreeSet) annotationFeatures.get("constituents");
        if (constituents != null) {
            for (Iterator iterator = constituents.iterator(); iterator.hasNext(); ) {
                Annotation constituent = (Annotation) iterator.next();
                printRecursively(constituent, indentationControl + "\t");
            }
        }
    }

    /**
     * Method categorizeAnnotations.
     */
    protected void categorizeAnnotations() {
        AnnotationSet defaultAnnotSet = this.document.getAnnotations();
        HashSet filter = new HashSet();
        filter.add("Finding");
        filter.add("Attribute");
        filter.add("Value");
        AnnotationSet annotations = defaultAnnotSet.get(filter);
        if (annotations != null) {
            for (Iterator iterator = annotations.iterator(); iterator.hasNext(); ) {
                Annotation annotation = (Annotation) iterator.next();
                annotation.getFeatures().put("category", annotation.getType().toUpperCase());
                annotation.getFeatures().put("negated", "0");
            }
        }
        filter.clear();
        filter.add("NegatedConcept");
        annotations = defaultAnnotSet.get(filter);
        if (annotations != null) {
            for (Iterator iterator = annotations.iterator(); iterator.hasNext(); ) {
                Annotation annotation = (Annotation) iterator.next();
                if (annotation.getFeatures().get("category") == null) {
                    annotation.getFeatures().put("category", "Modifier");
                }
                annotation.getFeatures().put("negated", "1");
            }
        }
        filter.clear();
        filter.add("Concept");
        annotations = defaultAnnotSet.get(filter);
        if (annotations != null) {
            for (Iterator iterator = annotations.iterator(); iterator.hasNext(); ) {
                Annotation annotation = (Annotation) iterator.next();
                if (annotation.getFeatures().get("category") == null) {
                    annotation.getFeatures().put("category", "Modifier");
                }
                annotation.getFeatures().put("negated", "0");
            }
        }
    }

    /**
     * Method preprocessFindings.
     */
    protected void preprocessFindings() {
        try {
            AnnotationSet defaultAnnotSet = this.document.getAnnotations();
            AnnotationSet findingAnnotSet = defaultAnnotSet.get("Finding");
            for (Iterator iterator = findingAnnotSet.iterator(); iterator.hasNext(); ) {
                Annotation finding = (Annotation) iterator.next();
                codeFinding(finding);
                propogateFeaturesToConstituents(finding);
            }
        } catch (Exception x) {
            ;
        }
        try {
            HashSet filter = new HashSet();
            filter.add("Finding");
            filter.add("Attribute");
            filter.add("Value");
            AnnotationSet defaultAnnotSet = this.document.getAnnotations();
            AnnotationSet findingAnnotSet = defaultAnnotSet.get(filter);
            for (Iterator iterator = findingAnnotSet.iterator(); iterator.hasNext(); ) {
                Annotation finding = (Annotation) iterator.next();
                finding.getFeatures().put("term", CaTIES_Utilities.spanStrings(this.document, finding));
            }
        } catch (Exception x) {
            ;
        }
    }

    /**
     * Method codeFinding.
     * 
     * @param finding Annotation
     */
    protected void codeFinding(Annotation finding) {
        FeatureMap features = finding.getFeatures();
        features.put("category", "FINDING");
        String cn = (String) features.get("cn");
        if (cn.toLowerCase().startsWith("gleason")) {
            features.put("cui", "C0332326");
            features.put("cn", "GLEASON GRADING FOR PROSTATIC CANCER");
            features.put("sty", "Intellectual Product");
            features.put("tui", "T170");
            features.put("term", "GLEASON GRADING FOR PROSTATIC CANCER");
            features.put("concept", "0");
        } else if (cn.toLowerCase().startsWith("tnm")) {
            features.put("cui", "C0809869");
            features.put("cn", "TNM CLASSIFICATION");
            features.put("sty", "Classification");
            features.put("tui", "T185");
            features.put("term", "TNM CLASSIFICATION");
            features.put("concept", "0");
        } else if (cn.toLowerCase().startsWith("size")) {
            features.put("cui", "C0702146");
            features.put("cn", "Size");
            features.put("sty", "Quantitative Concept");
            features.put("tui", "T081");
            features.put("term", "Size");
            features.put("concept", "0");
        } else if (cn.toLowerCase().startsWith("weight")) {
            features.put("cui", "C0043100");
            features.put("cn", "Weight");
            features.put("sty", "Quantitative Concept");
            features.put("tui", "T081");
            features.put("term", "Weight");
            features.put("concept", "0");
        } else {
            features.put("cui", "UNKNOWN");
            features.put("cn", "UNKNOWN");
            features.put("sty", "UNKNOWN");
            features.put("tui", "UNKNOWN");
            features.put("term", "UNKNOWN");
            features.put("concept", "0");
        }
    }

    /**
     * Method propogateFeaturesToConstituents.
     * 
     * @param finding Annotation
     */
    protected void propogateFeaturesToConstituents(Annotation finding) {
        FeatureMap findingFeatures = finding.getFeatures();
        String constituentsIds = (String) findingFeatures.get("constituentIds");
        constituentsIds = constituentsIds.substring(1, constituentsIds.length() - 1);
        StringTokenizer st = new StringTokenizer(constituentsIds, ",");
        while (st.hasMoreTokens()) {
            String idAsString = st.nextToken();
            idAsString = idAsString.trim();
            Integer constituentAnnotationId = new Integer(idAsString);
            Annotation constituentAnnotation = (Annotation) this.document.getAnnotations().get(constituentAnnotationId);
            for (Iterator keyIterator = findingFeatures.keySet().iterator(); keyIterator.hasNext(); ) {
                String key = (String) keyIterator.next();
                if (!key.startsWith("constituent") && !key.startsWith("category") && !key.equals("cn")) {
                    constituentAnnotation.getFeatures().put(key, findingFeatures.get(key));
                }
            }
        }
    }

    /**
     * Method convertToReportElements.
     * 
     * @throws InvalidOffsetException the invalid offset exception
     */
    protected void convertToReportElements() throws InvalidOffsetException {
        HashSet filter = new HashSet();
        filter.add("Concept");
        filter.add("NegatedConcept");
        filter.add("Finding");
        filter.add("Attribute");
        filter.add("Value");
        AnnotationSet conceptAnnotSet = this.defaultAnnotations.get(filter);
        if (conceptAnnotSet != null) {
            for (Iterator iterator = conceptAnnotSet.iterator(); iterator.hasNext(); ) {
                Annotation conceptAnnot = (Annotation) iterator.next();
                FeatureMap conceptFeatures = conceptAnnot.getFeatures();
                FeatureMap reportElementFeatures = Factory.newFeatureMap();
                for (Iterator iter = conceptFeatures.keySet().iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    reportElementFeatures.put(key, conceptFeatures.get(key));
                }
                reportElementFeatures.put("annotationId", (conceptAnnot.getId()).intValue() + "");
                Integer annotationId = this.classifierAnnotations.add(conceptAnnot.getStartNode().getOffset(), conceptAnnot.getEndNode().getOffset(), "ReportElement", reportElementFeatures);
            }
        }
    }

    /**
     * Method identifyKeyElements.
     * 
     * @param specificSection String
     * @param generalSection String
     */
    protected void identifyKeyElements(String specificSection, String generalSection) {
        try {
            if (existsSpecificSection(specificSection)) {
                eliminateGeneralAnnotations(specificSection, generalSection);
            } else {
                adjustGeneralAnnotations(specificSection, generalSection);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method existsSpecificSection.
     * 
     * @param specificSection String
     * 
     * @return boolean
     */
    protected boolean existsSpecificSection(String specificSection) {
        boolean returnValue = false;
        try {
            FeatureMap filterFeatureMap = Factory.newFeatureMap();
            filterFeatureMap.put("section", specificSection);
            AnnotationSet specificSectionAnnotations = this.classifierAnnotations.get("ReportElement", filterFeatureMap);
            if (specificSectionAnnotations != null && specificSectionAnnotations.size() > 0) {
                returnValue = true;
            }
        } catch (Exception x) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Method eliminateGeneralAnnotations.
     * 
     * @param specificSection String
     * @param generalSection String
     */
    protected void eliminateGeneralAnnotations(String specificSection, String generalSection) {
        Vector annotationsToRemove = new Vector();
        try {
            for (Iterator iterator = this.classifierAnnotations.iterator(); iterator.hasNext(); ) {
                Annotation annotation = (Annotation) iterator.next();
                FeatureMap features = annotation.getFeatures();
                String sectionName = (String) features.get("section");
                if (sectionName != null && !sectionName.equals(specificSection) && sectionName.indexOf(generalSection) != -1) {
                    annotationsToRemove.add(annotation);
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        try {
            CaTIES_Utilities.removeAnnotations(this.classifierAnnotations, annotationsToRemove);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method adjustGeneralAnnotations.
     * 
     * @param specificSection String
     * @param generalSection String
     */
    protected void adjustGeneralAnnotations(String specificSection, String generalSection) {
        try {
            for (Iterator iterator = this.classifierAnnotations.iterator(); iterator.hasNext(); ) {
                Annotation annotation = (Annotation) iterator.next();
                FeatureMap features = annotation.getFeatures();
                String sectionName = (String) features.get("section");
                if (sectionName != null && sectionName.indexOf(generalSection) != -1) {
                    features.put("section", specificSection);
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method connectFindings.
     */
    protected void connectFindings() {
        try {
            FeatureMap filterFeatures = Factory.newFeatureMap();
            filterFeatures.put("category", "FINDING");
            AnnotationSet findingAnnotations = this.classifierAnnotations.get("ReportElement", filterFeatures);
            Iterator findingIterator = findingAnnotations.iterator();
            Vector reportElementsToRemove = new Vector();
            while (findingIterator.hasNext()) {
                Annotation finding = (Annotation) findingIterator.next();
                FeatureMap findingFeatures = (FeatureMap) finding.getFeatures();
                String constituentIdsString = (String) findingFeatures.get("constituentIds");
                if (constituentIdsString == null) {
                    continue;
                }
                int constituentIdsStringLength = constituentIdsString.length();
                constituentIdsString = constituentIdsString.substring(1, constituentIdsStringLength - 1);
                StringTokenizer tokenizer = new StringTokenizer(constituentIdsString, ",");
                TreeSet constituents = new TreeSet(this.documentOrder);
                while (tokenizer.hasMoreTokens()) {
                    String constituentAsString = tokenizer.nextToken();
                    constituentAsString = constituentAsString.trim();
                    FeatureMap annotationIdFilter = Factory.newFeatureMap();
                    annotationIdFilter.put("annotationId", constituentAsString);
                    AnnotationSet constituentAnnotations = this.classifierAnnotations.get("ReportElement", annotationIdFilter);
                    if (constituentAnnotations != null) {
                        Annotation constituentAnnotation = (Annotation) constituentAnnotations.iterator().next();
                        constituents.add(constituentAnnotation);
                        reportElementsToRemove.add(constituentAnnotation);
                    } else if (this.debugging) {
                        System.err.println("[CaTIES_PhysicalModelDeducerPR] Dangling reference " + "to annotationId " + constituentAsString);
                    }
                }
                findingFeatures.put("constituents", constituents);
            }
            removeReportElements(reportElementsToRemove);
        } catch (Exception x) {
            ;
        }
    }

    /**
     * Method removeReportElements.
     * 
     * @param reportElementsToRemove Vector
     */
    protected void removeReportElements(Vector reportElementsToRemove) {
        try {
            CaTIES_Utilities.removeAnnotations(this.classifierAnnotations, reportElementsToRemove);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method adjustUnitsOfMeasure.
     */
    protected void adjustUnitsOfMeasure() {
        try {
            FeatureMap filterFeatures = Factory.newFeatureMap();
            filterFeatures.put("category", "FINDING");
            AnnotationSet findingAnnotations = this.classifierAnnotations.get("ReportElement", filterFeatures);
            for (Iterator findingIterator = findingAnnotations.iterator(); findingIterator.hasNext(); ) {
                Annotation findingAnnotation = (Annotation) findingIterator.next();
                FeatureMap findingFeatures = findingAnnotation.getFeatures();
                String findingName = (String) findingFeatures.get("cn");
                TreeSet constituentsTreeSet = (TreeSet) findingFeatures.get("constituents");
                LinkedList constituents = new LinkedList(constituentsTreeSet);
                int constituentsLength = constituents.size();
                Annotation unitsOfMeasureAnnotation = (Annotation) constituents.get(constituentsLength - 1);
                if (findingName == null || unitsOfMeasureAnnotation == null) {
                    ;
                } else if (findingName.startsWith("Size")) {
                    adjustSize(unitsOfMeasureAnnotation);
                } else if (findingName.startsWith("Weight")) {
                    adjustWeight(unitsOfMeasureAnnotation);
                }
            }
        } catch (Exception x) {
            ;
        }
    }

    /**
     * Method adjustSize.
     * 
     * @param unitsOfMeasureAnnotation Annotation
     */
    protected void adjustSize(Annotation unitsOfMeasureAnnotation) {
        FeatureMap unitsOfMeasureFeatures = unitsOfMeasureAnnotation.getFeatures();
        String unitsOfMeasure = (String) unitsOfMeasureFeatures.get("term");
        if (unitsOfMeasure == null || !unitsOfMeasure.toLowerCase().startsWith("m")) {
            unitsOfMeasureFeatures.put("term", "cm");
        } else {
            unitsOfMeasureFeatures.put("term", "mm");
        }
    }

    /**
     * Method adjustWeight.
     * 
     * @param unitsOfMeasureAnnotation Annotation
     */
    protected void adjustWeight(Annotation unitsOfMeasureAnnotation) {
        FeatureMap unitsOfMeasureFeatures = unitsOfMeasureAnnotation.getFeatures();
        String unitsOfMeasure = (String) unitsOfMeasureFeatures.get("term");
        if (unitsOfMeasure == null || !unitsOfMeasure.toLowerCase().startsWith("k")) {
            unitsOfMeasureFeatures.put("term", "gm");
        } else {
            unitsOfMeasureFeatures.put("term", "kg");
        }
    }

    /**
     * Method createSpecimens.
     */
    protected void createSpecimens() {
        determineParts();
        try {
            for (Iterator partsIterator = this.reportParts.iterator(); partsIterator.hasNext(); ) {
                Integer partNumber = (Integer) partsIterator.next();
                Integer specimenAnnotationId = createSpecimen(partNumber, FINAL_DIAGNOSIS, CATEGORY_ORGAN);
                if (specimenAnnotationId == null) {
                    specimenAnnotationId = createSpecimen(partNumber, null, CATEGORY_ORGAN);
                }
                if (specimenAnnotationId == null) {
                    specimenAnnotationId = createSpecimen(partNumber, null, null);
                }
            }
            FeatureMap specimenFilter = Factory.newFeatureMap();
            specimenFilter.put("category", "Specimen");
            if (this.classifierAnnotations.get("ReportElement", specimenFilter) == null) {
                forceSpecimenCreation();
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method determineParts.
     */
    protected void determineParts() {
        try {
            this.reportParts = new TreeSet(new Comparator() {

                public int compare(Object o1, Object o2) {
                    Integer partNumber1 = (Integer) o1;
                    Integer partNumber2 = (Integer) o2;
                    return partNumber1.intValue() - partNumber2.intValue();
                }
            });
            for (Iterator annotationIterator = this.classifierAnnotations.iterator(); annotationIterator.hasNext(); ) {
                Annotation annotation = (Annotation) annotationIterator.next();
                FeatureMap annotationFeatures = annotation.getFeatures();
                String partAsString = (String) annotationFeatures.get("part");
                if (partAsString != null) {
                    this.reportParts.add(new Integer(partAsString));
                }
            }
            if (this.debugging) {
                for (Iterator iterator = this.reportParts.iterator(); iterator.hasNext(); ) {
                    Integer partNumber = (Integer) iterator.next();
                    System.out.println("[CaTIES_PhysicalModelDeducerPR] Found part " + partNumber);
                }
            }
        } catch (Exception x) {
            ;
        }
    }

    /**
     * Method createSpecimen.
     * 
     * @param sectionName String
     * @param categoryName String
     * @param thePart Integer
     * 
     * @return Integer
     */
    protected Integer createSpecimen(Integer thePart, String sectionName, String categoryName) {
        Integer specimenAnnotationId = null;
        try {
            FeatureMap filterFeatures = Factory.newFeatureMap();
            filterFeatures.put("part", thePart.intValue() + "");
            if (sectionName != null) {
                filterFeatures.put("section", sectionName);
            }
            if (categoryName != null) {
                filterFeatures.put("category", categoryName);
            }
            AnnotationSet candidateAnnotations = this.classifierAnnotations.get("ReportElement", filterFeatures);
            if (candidateAnnotations != null) {
                CaTIES_SortedAnnotationSet sortedCandidateAnnotations = new CaTIES_SortedAnnotationSet(candidateAnnotations);
                Annotation candidateAnnotation = null;
                for (Iterator candidateIterator = sortedCandidateAnnotations.iterator(); candidateIterator.hasNext(); ) {
                    candidateAnnotation = (Annotation) candidateIterator.next();
                    if (((String) candidateAnnotation.getFeatures().get("category")).equals("FINDING")) {
                        candidateAnnotation = null;
                    } else {
                        break;
                    }
                }
                if (candidateAnnotation != null) {
                    FeatureMap candidateFeatures = candidateAnnotation.getFeatures();
                    Long candidateStartOffset = candidateAnnotation.getStartNode().getOffset();
                    Long candidateEndOffset = candidateAnnotation.getEndNode().getOffset();
                    FeatureMap specimenFeatures = Factory.newFeatureMap();
                    for (Iterator iter = candidateFeatures.keySet().iterator(); iter.hasNext(); ) {
                        String key = (String) iter.next();
                        specimenFeatures.put(key, candidateFeatures.get(key));
                    }
                    specimenFeatures.put("category", "Specimen");
                    specimenFeatures.put("constituents", null);
                    specimenAnnotationId = this.classifierAnnotations.add(candidateStartOffset, candidateEndOffset, "ReportElement", specimenFeatures);
                }
            }
        } catch (Exception x) {
            specimenAnnotationId = null;
        }
        return specimenAnnotationId;
    }

    /**
     * Method setSpecimenSection.
     * 
     * @param section String
     */
    protected void setSpecimenSection(String section) {
        try {
            FeatureMap featureFilter = Factory.newFeatureMap();
            featureFilter.put("category", "Specimen");
            AnnotationSet specimenAnnotations = this.classifierAnnotations.get("ReportElement", featureFilter);
            for (Iterator iterator = specimenAnnotations.iterator(); iterator.hasNext(); ) {
                Annotation specimenAnnotation = (Annotation) iterator.next();
                specimenAnnotation.getFeatures().put("section", section);
            }
        } catch (Exception x) {
        }
    }

    /**
     * Method forceSpecimenCreation.
     */
    protected void forceSpecimenCreation() {
        try {
            FeatureMap features = Factory.newFeatureMap();
            features.put("category", "Specimen");
            features.put("section", FINAL_DIAGNOSIS);
            features.put("part", "0");
            features.put("cn", "UNKNOWN");
            Integer specimenAnnotationId = this.classifierAnnotations.add(new Long(0L), new Long(0L), "ReportElement", features);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method linkModifiers.
     */
    protected void linkModifiers() {
        try {
            HashSet sourceSet = new HashSet();
            sourceSet.add("Modifier");
            HashSet targetSet = new HashSet();
            targetSet.add("ORGAN");
            targetSet.add("DIAGNOSIS");
            targetSet.add("PROCEDURE");
            CaTIES_NearestNeighborLinker linker = new CaTIES_NearestNeighborLinker();
            linker.setDocument(this.document);
            linker.setSourceSet(sourceSet);
            linker.setTargetSet(targetSet);
            linker.setComparator(categoryThenDocumentOrder);
            linker.setDebugging(new Boolean(this.debugging));
            linker.execute();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method findAcquisitionProcedure.
     */
    protected void findAcquisitionProcedure() {
        try {
            FeatureMap featureFilter = Factory.newFeatureMap();
            featureFilter.put("category", "Specimen");
            AnnotationSet specimenAnnotations = this.classifierAnnotations.get("ReportElement", featureFilter);
            for (Iterator iterator = specimenAnnotations.iterator(); iterator.hasNext(); ) {
                Annotation specimenAnnotation = (Annotation) iterator.next();
                boolean success = findAcquisitionProcedureFromFinalDiagnosis(specimenAnnotation);
                if (!success) {
                    success = findAcquisitionProcedureFromReport(specimenAnnotation);
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method findAcquisitionProcedureFromFinalDiagnosis.
     * 
     * @param specimenAnnotation Annotation
     * 
     * @return boolean
     */
    protected boolean findAcquisitionProcedureFromFinalDiagnosis(Annotation specimenAnnotation) {
        boolean returnValue = false;
        try {
            FeatureMap specimenFeatures = specimenAnnotation.getFeatures();
            String partNumberAsString = (String) specimenFeatures.get("part");
            FeatureMap filterFeatures = Factory.newFeatureMap();
            filterFeatures.put("part", partNumberAsString);
            filterFeatures.put("section", FINAL_DIAGNOSIS);
            filterFeatures.put("category", "PROCEDURE");
            AnnotationSet procedureAnnotations = this.classifierAnnotations.get("ReportElement", filterFeatures);
            if (procedureAnnotations != null) {
                CaTIES_SortedAnnotationSet sortedProcedureAnnotations = new CaTIES_SortedAnnotationSet(procedureAnnotations);
                Annotation acquisitionProcedureAnnotation = (Annotation) sortedProcedureAnnotations.first();
                specimenFeatures.put("acquisition-procedure", acquisitionProcedureAnnotation);
                returnValue = true;
            }
        } catch (Exception x) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Method findAcquisitionProcedureFromReport.
     * 
     * @param specimenAnnotation Annotation
     * 
     * @return boolean
     */
    protected boolean findAcquisitionProcedureFromReport(Annotation specimenAnnotation) {
        boolean returnValue = false;
        try {
            FeatureMap specimenFeatures = specimenAnnotation.getFeatures();
            FeatureMap filterFeatures = Factory.newFeatureMap();
            filterFeatures.put("category", "PROCEDURE");
            AnnotationSet procedureAnnotations = this.classifierAnnotations.get("ReportElement", filterFeatures);
            if (procedureAnnotations != null) {
                CaTIES_SortedAnnotationSet sortedProcedureAnnotations = new CaTIES_SortedAnnotationSet(procedureAnnotations);
                Annotation acquisitionProcedureAnnotation = (Annotation) sortedProcedureAnnotations.first();
                specimenFeatures.put("acquisition-procedure", acquisitionProcedureAnnotation);
                returnValue = true;
            }
        } catch (Exception x) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Method linkOrgansToDiagnosis.
     */
    protected void linkOrgansToDiagnosis() {
        try {
            HashSet sourceSet = new HashSet();
            sourceSet.add("ORGAN");
            HashSet targetSet = new HashSet();
            targetSet.add("DIAGNOSIS");
            CaTIES_NearestNeighborLinker linker = new CaTIES_NearestNeighborLinker();
            linker.setDocument(this.document);
            linker.setSection(FINAL_DIAGNOSIS);
            linker.setSourceSet(sourceSet);
            linker.setTargetSet(targetSet);
            linker.setComparator(categoryThenDocumentOrder);
            linker.setDebugging(new Boolean(this.debugging));
            linker.execute();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method linkDiagnosisToSpecimen.
     */
    protected void linkDiagnosisToSpecimen() {
        try {
            mergeDuplicatedDiagnosisConcepts();
        } catch (Exception x) {
            x.printStackTrace();
        }
        try {
            associateDiagnosis();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method mergeDuplicatedDiagnosisConcepts.
     */
    protected void mergeDuplicatedDiagnosisConcepts() {
        try {
            FeatureMap specimenFilter = Factory.newFeatureMap();
            specimenFilter.put("category", "Specimen");
            AnnotationSet specimenAnnotations = this.classifierAnnotations.get("ReportElement", specimenFilter);
            for (Iterator iterator = specimenAnnotations.iterator(); iterator.hasNext(); ) {
                Annotation specimenAnnotation = (Annotation) iterator.next();
                String partAsString = (String) specimenAnnotation.getFeatures().get("part");
                FeatureMap filterFeatures = Factory.newFeatureMap();
                filterFeatures.put("section", FINAL_DIAGNOSIS);
                filterFeatures.put("part", partAsString);
                filterFeatures.put("category", "DIAGNOSIS");
                AnnotationSet partDiagnosisAnnotations = this.classifierAnnotations.get("ReportElement", filterFeatures);
                if (partDiagnosisAnnotations != null) {
                    CaTIES_SortedAnnotationSet sortedPartDiagnosisAnnotations = new CaTIES_SortedAnnotationSet(partDiagnosisAnnotations);
                    mergeDuplicatedDiagnosisConceptsForPart(sortedPartDiagnosisAnnotations);
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method mergeDuplicatedDiagnosisConceptsForPart.
     * 
     * @param sortedPartDiagnosisAnnotations CaTIES_SortedAnnotationSet
     */
    protected void mergeDuplicatedDiagnosisConceptsForPart(CaTIES_SortedAnnotationSet sortedPartDiagnosisAnnotations) {
        try {
            Vector reportElementsToRemove = new Vector();
            Hashtable diagnosisHashedByCui = new Hashtable();
            for (Iterator iterator = sortedPartDiagnosisAnnotations.iterator(); iterator.hasNext(); ) {
                Annotation diagnosisAnnotation = (Annotation) iterator.next();
                FeatureMap diagnosisFeatures = diagnosisAnnotation.getFeatures();
                String diagnosisCui = (String) diagnosisFeatures.get("cui");
                String diagnosisAssertion = (String) diagnosisFeatures.get("negated");
                String key = diagnosisCui + "<" + diagnosisAssertion + ">";
                Annotation hashedAnnotation = (Annotation) diagnosisHashedByCui.get(key);
                if (hashedAnnotation == null) {
                    diagnosisHashedByCui.put(key, diagnosisAnnotation);
                } else {
                    mergeDiagnosisAnnotations(diagnosisAnnotation, hashedAnnotation);
                    reportElementsToRemove.add(diagnosisAnnotation);
                }
            }
            CaTIES_Utilities.removeAnnotations(this.classifierAnnotations, reportElementsToRemove);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method mergeDiagnosisAnnotations.
     * 
     * @param targetAnnotation Annotation
     * @param sourceAnnotation Annotation
     */
    protected void mergeDiagnosisAnnotations(Annotation sourceAnnotation, Annotation targetAnnotation) {
        try {
            TreeSet constituents = new TreeSet(categoryThenDocumentOrder);
            TreeSet targetConstituents = (TreeSet) targetAnnotation.getFeatures().get("constituents");
            TreeSet sourceConstituents = (TreeSet) sourceAnnotation.getFeatures().get("constituents");
            if (targetConstituents != null) {
                constituents.addAll(targetConstituents);
            }
            if (sourceConstituents != null) {
                constituents.addAll(sourceConstituents);
            }
            targetAnnotation.getFeatures().put("constituents", constituents);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method associateDiagnosis.
     */
    protected void associateDiagnosis() {
        try {
            setSpecimenSection(FINAL_DIAGNOSIS);
            HashSet sourceSet = new HashSet();
            sourceSet.add("DIAGNOSIS");
            sourceSet.add("FINDING");
            HashSet targetSet = new HashSet();
            targetSet.add("Specimen");
            Vector matchConceptNames = new Vector();
            matchConceptNames.add("Size");
            matchConceptNames.add("Weight");
            CaTIES_NearestNeighborLinker linker = new CaTIES_NearestNeighborLinker();
            linker.setDocument(this.document);
            linker.setSection(FINAL_DIAGNOSIS);
            linker.setSourceCn(matchConceptNames);
            linker.setUseSourceCnComplement(true);
            linker.setSourceSet(sourceSet);
            linker.setTargetSet(targetSet);
            linker.setComparator(documentOrder);
            linker.setDebugging(new Boolean(this.debugging));
            linker.execute();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method linkGrossMeasurements.
     */
    protected void linkGrossMeasurements() {
        try {
            HashSet sourceSet = new HashSet();
            sourceSet.add("FINDING");
            HashSet targetSet = new HashSet();
            targetSet.add("ORGAN");
            Vector matchConceptNames = new Vector();
            matchConceptNames.add("Size");
            CaTIES_NearestNeighborLinker linker = new CaTIES_NearestNeighborLinker();
            linker.setDocument(this.document);
            linker.setSection(GROSS_DESCRIPTION);
            linker.setSourceCn(matchConceptNames);
            linker.setOneLinkPerTarget(true);
            linker.setSourceSet(sourceSet);
            linker.setTargetSet(targetSet);
            linker.setComparator(categoryThenDocumentOrder);
            linker.setDebugging(new Boolean(this.debugging));
            linker.execute();
            matchConceptNames.clear();
            matchConceptNames.add("Weight");
            linker = new CaTIES_NearestNeighborLinker();
            linker.setDocument(this.document);
            linker.setSection(GROSS_DESCRIPTION);
            linker.setSourceCn(matchConceptNames);
            linker.setOneLinkPerTarget(true);
            linker.setSourceSet(sourceSet);
            linker.setTargetSet(targetSet);
            linker.setComparator(categoryThenDocumentOrder);
            linker.setDebugging(new Boolean(this.debugging));
            linker.execute();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method linkGrossOrgansToSpecimens.
     */
    protected void linkGrossOrgansToSpecimens() {
        try {
            setSpecimenSection(GROSS_DESCRIPTION);
            HashSet sourceSet = new HashSet();
            sourceSet.add("ORGAN");
            HashSet targetSet = new HashSet();
            targetSet.add("Specimen");
            CaTIES_NearestNeighborLinker linker = new CaTIES_NearestNeighborLinker();
            linker.setDocument(this.document);
            linker.setSection(GROSS_DESCRIPTION);
            linker.setTargetLinkFeatureName("gross-description");
            linker.setSourceSet(sourceSet);
            linker.setTargetSet(targetSet);
            linker.setComparator(categoryThenDocumentOrder);
            linker.setDebugging(new Boolean(this.debugging));
            linker.execute();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Method sortSpecimens.
     */
    protected void sortSpecimens() {
        try {
            this.sortedSpecimens = null;
            FeatureMap featureFilter = Factory.newFeatureMap();
            featureFilter.put("category", "Specimen");
            AnnotationSet specimenAnnotations = this.classifierAnnotations.get("ReportElement", featureFilter);
            if (specimenAnnotations != null) {
                this.sortedSpecimens = new CaTIES_SortedAnnotationSet(specimenAnnotations);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
