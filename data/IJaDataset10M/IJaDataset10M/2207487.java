package edu.upmc.opi.caBIG.caTIES.installer.pipes.ties.creole;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.corpora.DocumentContentImpl;
import gate.util.InvalidOffsetException;

/**
 * Utilities for RegEx mechanism.
 * 
 * @author mitchellkj@upmc.edu
 * @author SOO IL KIM (skim@dsg.bwh.harvard.edu)
 * @version $Id: CaTIES_RegExUtilities.java,v 1.2 2006/05/31 18:42:46 mitchellkj
 * Exp $
 * @since 1.4.2_04
 */
public class CaTIES_RegExUtilities {

    /**
     * Method getStringForAnnotation.
     * 
     * @param annotation Annotation
     * @param document Document
     * 
     * @return String
     */
    static String getStringForAnnotation(Document document, Annotation annotation) {
        String annotationString = null;
        try {
            Long startOffset = annotation.getStartNode().getOffset();
            Long endOffset = annotation.getEndNode().getOffset();
            DocumentContentImpl doc = (DocumentContentImpl) document.getContent().getContent(startOffset, endOffset);
            annotationString = doc.getOriginalContent();
        } catch (InvalidOffsetException ioe) {
            annotationString = null;
        }
        return annotationString;
    }

    /**
     * Method createSentenceAnnotation.
     * 
     * @param sentenceStartOffset Long
     * @param targetAS AnnotationSet
     * @param sentenceString String
     * @param sentenceEndOffset Long
     */
    static void createSentenceAnnotation(AnnotationSet targetAS, Long sentenceStartOffset, Long sentenceEndOffset, String sentenceString) {
        FeatureMap fm_sent = Factory.newFeatureMap();
        fm_sent.put("string", sentenceString);
        addAnnotationToSet(targetAS, sentenceStartOffset, sentenceEndOffset, "Sent", fm_sent);
    }

    /**
     * Method addAnnotationToSet.
     * 
     * @param annotationType String
     * @param features FeatureMap
     * @param startOffset Long
     * @param targetAS AnnotationSet
     * @param endOffset Long
     */
    static void addAnnotationToSet(AnnotationSet targetAS, Long startOffset, Long endOffset, String annotationType, FeatureMap features) {
        try {
            targetAS.add(startOffset, endOffset, annotationType, features);
        } catch (InvalidOffsetException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Method getSentenceAnnotations.
     * 
     * @param sourceAS AnnotationSet
     * 
     * @return AnnotationSet
     */
    static AnnotationSet getSentenceAnnotations(AnnotationSet sourceAS) {
        AnnotationSet sentence_annotations = null;
        try {
            sentence_annotations = sourceAS.get("Sentence_sub");
            if (sentence_annotations == null || sentence_annotations.isEmpty()) {
                sentence_annotations = sourceAS.get("diagnosistext");
            }
            if (sentence_annotations == null || sentence_annotations.isEmpty()) {
                sentence_annotations = sourceAS.get("XSentence");
            }
            if (sentence_annotations == null || sentence_annotations.isEmpty()) {
                sentence_annotations = sourceAS.get("Sentence");
            }
        } catch (Exception x) {
            ;
        }
        return sentence_annotations;
    }

    /**
     * Method getDocumentFileName.
     * 
     * @param document Document
     * 
     * @return String
     */
    static String getDocumentFileName(Document document) {
        String filename = null;
        try {
            filename = document.getSourceUrl().getFile();
        } catch (Exception x) {
            ;
        }
        try {
            if (filename == null) {
                filename = (String) document.getFeatures().get("FILE_NAME");
            }
        } catch (Exception x) {
            ;
        }
        return filename;
    }
}
