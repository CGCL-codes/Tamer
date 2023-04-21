package at.ofai.gate.virtualdocuments;

import gate.Controller;
import gate.DocumentContent;
import gate.FeatureMap;
import gate.LanguageAnalyser;
import gate.Resource;
import gate.corpora.DocumentContentImpl;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ControllerAwarePR;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.RunTime;
import gate.util.GateRuntimeException;
import gate.util.InvalidOffsetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.naming.InvalidNameException;

/**
 * This PR converts an existing, annotated document into a new document
 * where the document text is created in various possible ways from
 * the original text covered by specific annotation types, from the values
 * of features of specific annotations or optionally from the original document
 * text if no such specific annotation types are present at some location.
 * The processing is guided by specifying a list of individual annotation
 * specifications, see the documentation for {@link AnnotatedDocumentTransformer}
 * 
 *
 * @author Johann Petrak
 */
@CreoleResource(name = "Replace By Virtual Document PR", comment = "Create virtual document from an annotation document")
public class ReplaceByVirtualDocumentPR extends AbstractLanguageAnalyser implements LanguageAnalyser, ControllerAwarePR {

    public static final long serialVersionUID = 1L;

    /**
   * Documentation of the source annotation specifications: ... 	
   * @param ss
   */
    @RunTime
    @CreoleParameter(comment = "A list of source annotation specifications")
    public void setSourceSpecifications(List<String> ss) {
        this.sourceSpecifications = ss;
    }

    public List<String> getSourceSpecifications() {
        return sourceSpecifications;
    }

    private List<String> sourceSpecifications;

    @RunTime
    @CreoleParameter(comment = "A list or processing options", defaultValue = "separator=;takeAll=false;takeOverlapping=false;separatorSame=;separatorKeyValue=;terminator=")
    public void setProcessingOptions(FeatureMap po) {
        this.processingOptions = po;
    }

    public FeatureMap getProcessingOptions() {
        return processingOptions;
    }

    private FeatureMap processingOptions;

    @RunTime
    @CreoleParameter(comment = "The input annotation set", defaultValue = "")
    public void setInputAnnotationSetName(String ias) {
        this.inputAnnotationSetName = ias;
    }

    public String getInputAnnotationSetName() {
        return inputAnnotationSetName;
    }

    private String inputAnnotationSetName = "";

    AnnotatedDocumentTransformer stringFromDocumentWithAnnotations;

    @Override
    public Resource init() throws ResourceInstantiationException {
        super.init();
        return this;
    }

    @Override
    public void execute() {
        fireStatusChanged("CreateVirtualDocumentPR processing: " + getDocument().getName());
        String newText = stringFromDocumentWithAnnotations.getStringForDocument(getDocument(), inputAnnotationSetName);
        Set<String> asnames = new HashSet<String>();
        asnames.addAll(getDocument().getAnnotationSetNames());
        if (asnames != null) {
            for (String asname : asnames) {
                getDocument().removeAnnotationSet(asname);
            }
        }
        getDocument().getAnnotations().clear();
        try {
            DocumentContent newContent = new DocumentContentImpl(newText);
            document.edit(0L, document.getContent().size(), newContent);
        } catch (InvalidOffsetException ex) {
            throw new GateRuntimeException(ex);
        }
        fireStatusChanged("CreateVirutalDocumentPR completed");
    }

    @Override
    public void controllerExecutionAborted(Controller arg0, Throwable arg1) throws ExecutionException {
    }

    @Override
    public void controllerExecutionFinished(Controller arg0) throws ExecutionException {
    }

    @Override
    public void controllerExecutionStarted(Controller arg0) throws ExecutionException {
        startup();
    }

    public void startup() throws ExecutionException {
        try {
            stringFromDocumentWithAnnotations = new AnnotatedDocumentTransformer(getSourceSpecifications(), getProcessingOptions(), false, false);
        } catch (InvalidNameException ex) {
            throw new ExecutionException(ex);
        }
    }
}
