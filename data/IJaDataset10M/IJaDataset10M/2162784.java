package edu.upmc.opi.caBIG.caTIES.server.ties.creole;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.AbstractLanguageAnalyser;
import gate.util.InvalidOffsetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Tags visible and invisible section headers
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id Exp $
 * @since 1.6.21
 */
public class CaTIES_SectionHeaderDetectorPR extends AbstractLanguageAnalyser {

    /**
	 * Field serialVersionUID. (value is "-1863322709982419803L ;")
	 */
    private static final long serialVersionUID = -1863322709982419803L;

    private static final String WILDCARD = "%";

    private static final String[][] transitionTable = { { "S1", "Token", "string", "[", "Cache", "S2" }, { "S1", WILDCARD, WILDCARD, WILDCARD, "No Action", "S1" }, { "S2", "Token", "string", "[", "Cache", "S4" }, { "S2", "Token", "kind", "word", "Cache", "S3" }, { "S2", WILDCARD, WILDCARD, WILDCARD, "Reset", "S1" }, { "S3", "Token", "string", "]", "AnnotateVisible", "S1" }, { "S3", "SpaceToken", "kind", "space", "Cache", "S3" }, { "S3", "Token", "kind", "word", "Cache", "S3" }, { "S3", WILDCARD, WILDCARD, WILDCARD, "Reset", "S1" }, { "S4", "Token", "kind", "word", "Cache", "S5" }, { "S4", WILDCARD, WILDCARD, WILDCARD, "Reset", "S1" }, { "S5", "Token", "string", "]", "Cache", "S6" }, { "S5", "SpaceToken", "kind", "space", "Cache", "S5" }, { "S5", "Token", "kind", "word", "Cache", "S5" }, { "S5", WILDCARD, WILDCARD, WILDCARD, "Reset", "S1" }, { "S6", "Token", "string", "]", "AnnotateHidden", "S1" }, { "S6", WILDCARD, WILDCARD, WILDCARD, "Reset", "S1" } };

    /**
	 * Field sectionsToTag.
	 */
    private List<String> sectionsToTag = null;

    /**
	 * Field debugging.
	 */
    private boolean debugging = false;

    private final ArrayList<Annotation> cache = new ArrayList<Annotation>();

    /**
	 * Constructor.
	 */
    public CaTIES_SectionHeaderDetectorPR() {
    }

    /**
	 * These getters and setters may be accessed through the entry in the GATE
	 * creole.xml file.
	 * 
	 * @return List
	 */
    public List<String> getSectionsToTag() {
        return this.sectionsToTag;
    }

    /**
	 * Method setSectionsToTag.
	 * 
	 * @param sectionsToTag
	 *            List
	 */
    public void setSectionsToTag(List<String> sectionsToTag) {
        this.sectionsToTag = sectionsToTag;
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
	 * @param debugging
	 *            Boolean
	 */
    public void setDebugging(Boolean debugging) {
        this.debugging = debugging.booleanValue();
    }

    /**
	 * Method execute.
	 */
    public void execute() {
        processFiniteStateMachine();
    }

    @SuppressWarnings("unchecked")
    private void processFiniteStateMachine() {
        try {
            if (this.debugging) {
                System.out.println("[CaTIES_SectionHeaderDetectorPR] Executing...");
            }
            HashSet<String> tokenAndSpaceAnnots = new HashSet<String>();
            tokenAndSpaceAnnots.add("Token");
            tokenAndSpaceAnnots.add("SpaceToken");
            AnnotationSet tokenAndSpaceAnnotationSet = this.document.getAnnotations().get(tokenAndSpaceAnnots);
            CaTIES_SortedAnnotationSet sortedTokenAnnotationSet = new CaTIES_SortedAnnotationSet(tokenAndSpaceAnnotationSet);
            String currentState = "S1";
            this.cache.clear();
            Iterator<Annotation> tokenAndSpaceIterator = sortedTokenAnnotationSet.iterator();
            while (tokenAndSpaceIterator.hasNext()) {
                Annotation currentAnnotation = tokenAndSpaceIterator.next();
                for (int sdx = 0; sdx < transitionTable.length; sdx++) {
                    String tblState = transitionTable[sdx][0];
                    String tblAnnotationType = transitionTable[sdx][1];
                    String tblFeatureName = transitionTable[sdx][2];
                    String tblFeatureValue = transitionTable[sdx][3];
                    String tblAction = transitionTable[sdx][4];
                    String tblNextState = transitionTable[sdx][5];
                    boolean isFired = true;
                    isFired = isFired && isEqualState(currentState, tblState);
                    isFired = isFired && isEqualCurrentAnnotationType(currentAnnotation, tblAnnotationType);
                    isFired = isFired && hasFeatureWithName(currentAnnotation, tblFeatureName);
                    isFired = isFired && hasFeatureWithValue(currentAnnotation, tblFeatureName, tblFeatureValue);
                    if (isFired) {
                        if (this.debugging) {
                            System.out.println("FIRING - " + currentState + "\n\t" + tblAnnotationType + "\n\t" + tblFeatureName + "\n\t" + tblFeatureValue + "\n\t" + tblAction + "\n\t" + tblNextState);
                        }
                        if (tblAction.equals("Reset")) {
                            this.cache.clear();
                        } else if (tblAction.equals("Cache")) {
                            this.cache.add(currentAnnotation);
                        } else if (tblAction.equals("AnnotateVisible")) {
                            this.cache.add(currentAnnotation);
                            annotateVisible(this.cache);
                            this.cache.clear();
                        } else if (tblAction.equals("AnnotateHidden")) {
                            this.cache.add(currentAnnotation);
                            annotateHidden(this.cache);
                            this.cache.clear();
                        } else if (tblAction.equals("No Action")) {
                            ;
                        }
                        currentState = tblNextState;
                        break;
                    }
                }
            }
            if (this.debugging) {
                System.out.println("[CaTIES_SectionHeaderDetectorPR] Finished...");
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    private void annotateVisible(ArrayList<Annotation> cache) {
        if (cache.size() > 0 && cacheMatchesSectionHeader(cache)) {
            Annotation startAnnot = (Annotation) cache.get(0);
            Annotation endAnnot = (Annotation) cache.get(cache.size() - 1);
            Long positionStart = startAnnot.getStartNode().getOffset();
            Long positionEnd = endAnnot.getEndNode().getOffset();
            FeatureMap params = Factory.newFeatureMap();
            params.put("majorType", "heading");
            params.put("minorType", "section");
            try {
                this.document.getAnnotations().add(positionStart, positionEnd, "Lookup", params);
            } catch (InvalidOffsetException e) {
                e.printStackTrace();
            }
        }
    }

    private void annotateHidden(ArrayList<Annotation> cache) {
        if (cache.size() > 0 && cacheMatchesSectionHeader(cache)) {
            Annotation startAnnot = (Annotation) cache.get(0);
            Annotation endAnnot = (Annotation) cache.get(cache.size() - 1);
            Long positionStart = startAnnot.getStartNode().getOffset();
            Long positionEnd = endAnnot.getEndNode().getOffset();
            FeatureMap params = Factory.newFeatureMap();
            params.put("majorType", "heading");
            params.put("minorType", "section");
            try {
                this.document.getAnnotations().add(positionStart, positionEnd, "Lookup", params);
            } catch (InvalidOffsetException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean cacheMatchesSectionHeader(ArrayList<Annotation> cache) {
        boolean result = false;
        StringBuffer sb = new StringBuffer();
        for (Annotation annotation : cache) {
            String underLyingText = CaTIES_Utilities.spanStrings(this.document, annotation);
            if (underLyingText.equals("[") || underLyingText.equals("[[") || underLyingText.equals("]") || underLyingText.equals("]]")) {
                continue;
            }
            sb.append(underLyingText);
        }
        String candidateSectionHeading = sb.toString();
        result = this.sectionsToTag.contains(candidateSectionHeading);
        return result;
    }

    private boolean hasFeatureWithValue(Annotation currentAnnotation, String tblFeatureName, String tblFeatureValue) {
        boolean result = tblFeatureValue.equals(WILDCARD);
        if (!result) {
            String featureValue = (String) currentAnnotation.getFeatures().get(tblFeatureName);
            result = featureValue.equals(tblFeatureValue);
        }
        return result;
    }

    private boolean hasFeatureWithName(Annotation currentAnnotation, String tblFeatureName) {
        boolean result = tblFeatureName.equals(WILDCARD);
        if (!result) {
            result = currentAnnotation.getFeatures().get(tblFeatureName) != null;
        }
        return result;
    }

    private boolean isEqualCurrentAnnotationType(Annotation currentAnnotation, String tblAnnotationType) {
        return tblAnnotationType.equals(WILDCARD) || currentAnnotation.getType().equals(tblAnnotationType);
    }

    private boolean isEqualState(String currentState, String tblState) {
        return currentState.equals(tblState);
    }
}
