package gate.creole;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.InvalidOffsetException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@CreoleResource(name = "Schema Cleaner", interfaceName = "gate.ProcessingResource", icon = "sweep.png", comment = "Produces an annotation set whose content is restricted by the specified set of schemas")
public class SchemaCleaner extends AbstractLanguageAnalyser {

    private String inputASName = null;

    private String outputASName = null;

    private List<AnnotationSchema> schemas = new ArrayList<AnnotationSchema>();

    @Override
    public void execute() throws ExecutionException {
        if (schemas.isEmpty()) return;
        AnnotationSet outputAS = getDocument().getAnnotations(outputASName);
        if (!outputAS.isEmpty()) throw new ExecutionException("Output AnnotationSet must be empty");
        AnnotationSet inputAS = getDocument().getAnnotations(inputASName);
        for (AnnotationSchema schema : schemas) {
            AnnotationSet annots = inputAS.get(schema.getAnnotationName());
            if (annots == null) continue;
            for (Annotation a : annots) {
                boolean valid = true;
                FeatureMap params = Factory.newFeatureMap();
                if (schema.getFeatureSchemaSet() != null) {
                    FeatureMap current = a.getFeatures();
                    for (FeatureSchema fs : schema.getFeatureSchemaSet()) {
                        String fn = fs.getFeatureName();
                        Object fv = current.get(fn);
                        if (fv != null) {
                            if (fs.getFeatureValueClass().isAssignableFrom(fv.getClass())) {
                                if (!fs.isEnumeration() || fs.getPermittedValues().contains(fv)) {
                                    params.put(fn, fv);
                                }
                            }
                        }
                        if (fs.isRequired() && !params.containsKey(fn)) {
                            valid = false;
                            break;
                        }
                    }
                }
                if (valid) {
                    try {
                        outputAS.add(a.getId(), a.getStartNode().getOffset(), a.getEndNode().getOffset(), schema.getAnnotationName(), params);
                    } catch (InvalidOffsetException e) {
                        throw new ExecutionException(e);
                    }
                }
            }
        }
    }

    @RunTime
    @Optional
    @CreoleParameter(comment = "the annotation set used as input to this PR")
    public void setInputASName(String name) {
        inputASName = name;
    }

    public String getInputASName() {
        return inputASName;
    }

    @RunTime
    @Optional
    @CreoleParameter(comment = "the annotation set used to store output from this PR", defaultValue = "safe.preprocessing")
    public void setOutputASName(String name) {
        outputASName = name;
    }

    public String getOutputASName() {
        return outputASName;
    }

    @RunTime
    @Optional
    @CreoleParameter(comment = "the list of schemas that define the annotations to move from the input to the output annotation set")
    public void setSchema(List<AnnotationSchema> schemas) {
        this.schemas = schemas;
    }

    public List<AnnotationSchema> getSchema() {
        return schemas;
    }
}
