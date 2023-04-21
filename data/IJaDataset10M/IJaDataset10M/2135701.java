package vmp.gate.tbl;

import gate.Annotation;
import gate.Resource;
import gate.creole.ResourceInstantiationException;
import vmp.gate.tbl.control.DocumentAnnotationManager;
import vmp.gate.tbl.entity.Lexicon;

/**
 *
 * @author Valentina Munoz Porras
 * @version 1.0 21 de enero de 2008
 */
public class InitialTagger extends TBLProcessingResource {

    String lowercaseTag;

    String uppInitialTag;

    /** Creates a new instance of InitialTagger */
    public InitialTagger() {
    }

    @Override
    public Resource init() throws ResourceInstantiationException {
        return this;
    }

    /**
     * Starts the process.
     */
    @Override
    public void execute() {
        Lexicon lexicon = this.lexiconLR.getLexicon();
        if (lexicon == null) throw new gate.util.GateRuntimeException("Empty lexicon" + " content in " + lexiconLR.getName());
        annotationManager = new DocumentAnnotationManager(document);
        int size = annotationManager.size();
        int currentIndex = 0;
        for (Object b : annotationManager) {
            this.fireProgressChanged((currentIndex++) * 100 / size);
            Annotation a = (Annotation) b;
            String currentFeature = (String) a.getFeatures().get(lexicon.getKey());
            if (lexicon.getBestClassification(currentFeature) != null) a.getFeatures().put(lexicon.getClassification(), lexicon.getBestClassification(currentFeature)); else {
                if (currentFeature.toUpperCase().substring(0, 1).equals(currentFeature.subSequence(0, 1))) a.getFeatures().put(lexicon.getClassification(), uppInitialTag); else a.getFeatures().put(lexicon.getClassification(), lowercaseTag);
            }
        }
    }

    public String getLowercaseTag() {
        return lowercaseTag;
    }

    public void setLowercaseTag(String lowercaseTag) {
        this.lowercaseTag = lowercaseTag;
    }

    public String getUppInitialTag() {
        return uppInitialTag;
    }

    public void setUppInitialTag(String uppInitialTag) {
        this.uppInitialTag = uppInitialTag;
    }
}
