package edu.upmc.opi.caBIG.caTIES.installer.pipes.ties.creole;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Linked near concepts based on input categories.
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id: CaTIES_NearestNeighborLinker.java,v 1.1 2005/12/06 18:51:45
 *          mitchellkj Exp $
 * @since 1.4.2_04
 */
public class CaTIES_NearestNeighborLinker {

    /**
	 * Field document.
	 */
    protected Document document = null;

    /**
	 * Field annotations.
	 */
    protected AnnotationSet annotations = null;

    /**
	 * Field reportElementsToRemove.
	 */
    protected Vector reportElementsToRemove = new Vector();

    /**
	 * Field sortedAnnotations.
	 */
    protected CaTIES_SortedAnnotationSet sortedAnnotations = null;

    /**
	 * Field sourceSet.
	 */
    protected HashSet sourceSet = null;

    /**
	 * Field targetSet.
	 */
    protected HashSet targetSet = null;

    /**
	 * Field oneLinkPerTarget.
	 */
    protected boolean oneLinkPerTarget = false;

    /**
	 * Field targetLinkFeatureName.
	 */
    protected String targetLinkFeatureName = "constituents";

    /**
	 * Field comparator.
	 */
    protected Comparator comparator = null;

    /**
	 * Field section.
	 */
    protected String section = null;

    /**
	 * Field sourceCn.
	 */
    protected Vector sourceCn = null;

    /**
	 * Field useSourceCnComplement.
	 */
    protected boolean useSourceCnComplement = false;

    /**
	 * Field debugging.
	 */
    protected boolean debugging = false;

    /**
	 * Constructor for CaTIES_NearestNeighborLinker.
	 */
    public CaTIES_NearestNeighborLinker() {
    }

    /**
	 * Method execute.
	 */
    public void execute() {
        try {
            this.reportElementsToRemove.clear();
            FeatureMap featureFilter = Factory.newFeatureMap();
            featureFilter.put("category", "Specimen");
            AnnotationSet specimenAnnotations = this.annotations.get("ReportElement", featureFilter);
            for (Iterator specimenIterator = specimenAnnotations.iterator(); specimenIterator.hasNext(); ) {
                Annotation specimenAnnotation = (Annotation) specimenIterator.next();
                String partAsString = (String) specimenAnnotation.getFeatures().get("part");
                featureFilter.clear();
                featureFilter.put("part", partAsString);
                if (this.section != null) {
                    featureFilter.put("section", this.section);
                }
                AnnotationSet annotationsForPart = this.annotations.get("ReportElement", featureFilter);
                if (annotationsForPart != null) {
                    executePerPart(annotationsForPart);
                }
            }
            removeReportElements(this.reportElementsToRemove);
            enableReportElements();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
	 * Method removeReportElements.
	 * 
	 * @param reportElementsToRemove
	 *            Vector
	 */
    protected void removeReportElements(Vector reportElementsToRemove) {
        try {
            CaTIES_Utilities.removeAnnotations(this.annotations, reportElementsToRemove);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
	 * Method executePerPart.
	 * 
	 * @param partAnnotations
	 *            AnnotationSet
	 */
    protected void executePerPart(AnnotationSet partAnnotations) {
        CaTIES_SortedAnnotationSet sortedAnnotations = new CaTIES_SortedAnnotationSet(partAnnotations);
        CaTIES_CategoryScanner sourceScanner = new CaTIES_CategoryScanner();
        sourceScanner.setCategorySet(sourceSet);
        sourceScanner.setCn(this.sourceCn);
        sourceScanner.setUseCnComplement(this.useSourceCnComplement);
        sourceScanner.setSortedAnnotations(new LinkedList(sortedAnnotations));
        while (sourceScanner.scan()) {
            Annotation sourceAnnotation = sourceScanner.getCurrentAnnotation();
            CaTIES_CategoryScanner backwardScanner = new CaTIES_CategoryScanner();
            backwardScanner.setSortedAnnotations(new LinkedList(sortedAnnotations));
            backwardScanner.setCategorySet(this.targetSet);
            backwardScanner.setSearchDirection(CaTIES_CategoryScanner.BACKWARD);
            backwardScanner.setCurrentPosition(sourceScanner.getCurrentPosition());
            boolean backwardFound = backwardScanner.scan();
            CaTIES_CategoryScanner forewardScanner = new CaTIES_CategoryScanner();
            forewardScanner.setSortedAnnotations(new LinkedList(sortedAnnotations));
            forewardScanner.setCategorySet(this.targetSet);
            forewardScanner.setSearchDirection(CaTIES_CategoryScanner.FOREWARD);
            forewardScanner.setCurrentPosition(sourceScanner.getCurrentPosition());
            boolean forewardFound = forewardScanner.scan();
            Annotation closestAnnotation = null;
            Annotation previousAnnotation = null;
            Annotation nextAnnotation = null;
            if (backwardFound && forewardFound) {
                previousAnnotation = backwardScanner.getCurrentAnnotation();
                nextAnnotation = forewardScanner.getCurrentAnnotation();
                if (isCloser(sourceAnnotation, previousAnnotation, nextAnnotation)) {
                    closestAnnotation = previousAnnotation;
                } else {
                    closestAnnotation = nextAnnotation;
                }
            } else if (backwardFound) {
                previousAnnotation = backwardScanner.getCurrentAnnotation();
                closestAnnotation = previousAnnotation;
            } else if (forewardFound) {
                nextAnnotation = forewardScanner.getCurrentAnnotation();
                closestAnnotation = nextAnnotation;
            }
            if (closestAnnotation != null) {
                linkAnnotation(closestAnnotation, sourceAnnotation);
            }
        }
    }

    /**
	 * Method linkAnnotation.
	 * 
	 * @param targetAnnotation
	 *            Annotation
	 * @param sourceAnnotation
	 *            Annotation
	 */
    protected void linkAnnotation(Annotation targetAnnotation, Annotation sourceAnnotation) {
        try {
            FeatureMap targetFeatures = targetAnnotation.getFeatures();
            TreeSet constituents = null;
            Object constituentsAsObject = targetFeatures.get(this.targetLinkFeatureName);
            if (constituentsAsObject == null) {
                constituents = new TreeSet(this.comparator);
                targetFeatures.put(this.targetLinkFeatureName, constituents);
            } else {
                constituents = (TreeSet) constituentsAsObject;
            }
            constituents.add(sourceAnnotation);
            if (oneLinkPerTarget) {
                targetFeatures.put("disabled", new Boolean(true));
            }
            this.reportElementsToRemove.add(sourceAnnotation);
        } catch (Exception x) {
            ;
        }
    }

    /**
	 * Method getDocument.
	 * 
	 * @return Document
	 */
    public Document getDocument() {
        return this.document;
    }

    /**
	 * Method getSourceSet.
	 * 
	 * @return HashSet
	 */
    public HashSet getSourceSet() {
        return this.sourceSet;
    }

    /**
	 * Method getTargetSet.
	 * 
	 * @return HashSet
	 */
    public HashSet getTargetSet() {
        return this.targetSet;
    }

    /**
	 * Method getComparator.
	 * 
	 * @return Comparator
	 */
    public Comparator getComparator() {
        return this.comparator;
    }

    /**
	 * Method getSection.
	 * 
	 * @return String
	 */
    public String getSection() {
        return this.section;
    }

    /**
	 * Method getSourceCn.
	 * 
	 * @return Vector
	 */
    public Vector getSourceCn() {
        return this.sourceCn;
    }

    /**
	 * Method getUseSourceCnComplement.
	 * 
	 * @return boolean
	 */
    public boolean getUseSourceCnComplement() {
        return this.useSourceCnComplement;
    }

    /**
	 * Method getOneLinkPerTarget.
	 * 
	 * @return boolean
	 */
    public boolean getOneLinkPerTarget() {
        return oneLinkPerTarget;
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
	 * Method setDocument.
	 * 
	 * @param document
	 *            Document
	 */
    public void setDocument(Document document) {
        this.document = document;
        this.annotations = document.getAnnotations("Classifier");
    }

    /**
	 * Method setSourceSet.
	 * 
	 * @param sourceSet
	 *            HashSet
	 */
    public void setSourceSet(HashSet sourceSet) {
        this.sourceSet = sourceSet;
    }

    /**
	 * Method setTargetSet.
	 * 
	 * @param targetSet
	 *            HashSet
	 */
    public void setTargetSet(HashSet targetSet) {
        this.targetSet = targetSet;
    }

    /**
	 * Method setComparator.
	 * 
	 * @param comparator
	 *            Comparator
	 */
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    /**
	 * Method setSection.
	 * 
	 * @param section
	 *            String
	 */
    public void setSection(String section) {
        this.section = section;
    }

    /**
	 * Method setSourceCn.
	 * 
	 * @param sourceCn
	 *            Vector
	 */
    public void setSourceCn(Vector sourceCn) {
        this.sourceCn = sourceCn;
    }

    /**
	 * Method setUseSourceCnComplement.
	 * 
	 * @param useSourceCnComplement
	 *            boolean
	 */
    public void setUseSourceCnComplement(boolean useSourceCnComplement) {
        this.useSourceCnComplement = useSourceCnComplement;
    }

    /**
	 * Method setOneLinkPerTarget.
	 * 
	 * @param oneLinkPerTarget
	 *            boolean
	 */
    public void setOneLinkPerTarget(boolean oneLinkPerTarget) {
        this.oneLinkPerTarget = oneLinkPerTarget;
    }

    /**
	 * Method setTargetLinkFeatureName.
	 * 
	 * @param targetLinkFeatureName
	 *            String
	 */
    public void setTargetLinkFeatureName(String targetLinkFeatureName) {
        this.targetLinkFeatureName = targetLinkFeatureName;
    }

    /**
	 * Method setDebugging.
	 * 
	 * @param debugging
	 *            Boolean
	 */
    public void setDebugging(Boolean debugging) {
        this.debugging = debugging.booleanValue();
    }

    /**
	 * Method neighborDistance.
	 * 
	 * @param annotation1
	 *            Annotation
	 * @param annotation2
	 *            Annotation
	 * 
	 * @return int
	 */
    protected int neighborDistance(Annotation annotation1, Annotation annotation2) {
        int returnValue = 0;
        try {
            FeatureMap features1 = annotation1.getFeatures();
            String sentence1AsString = ((String) features1.get("sentence"));
            long sentence1 = (new Long(sentence1AsString)).longValue();
            String phrase1AsString = ((String) features1.get("phrase"));
            long phrase1 = (new Long(phrase1AsString)).longValue();
            long startOffset1 = annotation1.getStartNode().getOffset().longValue();
            FeatureMap features2 = annotation2.getFeatures();
            String sentence2AsString = ((String) features2.get("sentence"));
            long sentence2 = (new Long(sentence2AsString)).longValue();
            String phrase2AsString = ((String) features2.get("phrase"));
            long phrase2 = (new Long(phrase2AsString)).longValue();
            long startOffset2 = annotation2.getStartNode().getOffset().longValue();
            returnValue += 1000 * Math.abs(sentence1 - sentence2);
            returnValue += 100 * Math.abs(phrase1 - phrase2);
            returnValue += 10 * Math.abs(startOffset1 - startOffset2);
            if (sentence1 == sentence2 && phrase1 == phrase2 && startOffset1 < startOffset2) {
                returnValue += 5;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return returnValue;
    }

    /**
	 * Method isCloser.
	 * 
	 * @param annotation1
	 *            Annotation
	 * @param annotation3
	 *            Annotation
	 * @param annotation2
	 *            Annotation
	 * 
	 * @return boolean
	 */
    protected boolean isCloser(Annotation annotation1, Annotation annotation2, Annotation annotation3) {
        boolean returnValue = false;
        try {
            int distance1 = neighborDistance(annotation1, annotation2);
            int distance2 = neighborDistance(annotation1, annotation3);
            returnValue = distance1 < distance2;
        } catch (Exception x) {
            returnValue = false;
        }
        return returnValue;
    }

    /**
	 * Method enableReportElements.
	 */
    protected void enableReportElements() {
        try {
            for (Iterator iterator = this.annotations.iterator(); iterator.hasNext(); ) {
                Annotation annotation = (Annotation) iterator.next();
                annotation.getFeatures().remove("disabled");
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
