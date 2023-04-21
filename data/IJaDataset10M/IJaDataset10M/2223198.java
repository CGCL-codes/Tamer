package eu.planets_project.pp.plato.evaluation.evaluators;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.logging.Log;
import org.xml.sax.SAXException;
import eu.planets_project.pp.plato.evaluation.EvaluatorException;
import eu.planets_project.pp.plato.evaluation.IObjectEvaluator;
import eu.planets_project.pp.plato.evaluation.IStatusListener;
import eu.planets_project.pp.plato.model.Alternative;
import eu.planets_project.pp.plato.model.DetailedExperimentInfo;
import eu.planets_project.pp.plato.model.DigitalObject;
import eu.planets_project.pp.plato.model.SampleObject;
import eu.planets_project.pp.plato.model.measurement.Measurement;
import eu.planets_project.pp.plato.model.scales.Scale;
import eu.planets_project.pp.plato.model.values.FreeStringValue;
import eu.planets_project.pp.plato.model.values.PositiveFloatValue;
import eu.planets_project.pp.plato.model.values.PositiveIntegerValue;
import eu.planets_project.pp.plato.model.values.Value;
import eu.planets_project.pp.plato.util.MeasurementInfoUri;
import eu.planets_project.pp.plato.util.PlatoLogger;

/**
 * This class analyses the metadata collected during experiment execution
 * and extracts measurements. Currently focussed on the metadata schema
 * found in minimee experiments.
 * @author cb
 * TODO add comment to value: which profiling tool was used, etc.
 */
public class ExperimentEvaluator extends EvaluatorBase implements IObjectEvaluator {

    private static final Log log = PlatoLogger.getLogger(ExperimentEvaluator.class);

    private static HashMap<String, String> propertyToMeasuredValues = new HashMap<String, String>();

    private static final String DESCRIPTOR_FILE = "data/evaluation/measurementsExperiment.xml";

    public ExperimentEvaluator() {
        loadMeasurementsDescription(DESCRIPTOR_FILE);
    }

    /**
     * all properties which can be evaluated have to be registered here
     * 
     * Don't forget to configure measurableProperties of the migration engines in miniMEE-tool-configs.xml properly !
     * 
     */
    static {
        propertyToMeasuredValues.put(OBJECT_ACTION_RUNTIME_PERFORMANCE_TIME_PERSAMPLE, "performance:time:used");
        propertyToMeasuredValues.put(OBJECT_ACTION_RUNTIME_PERFORMANCE_MEMORY_PERSAMPLE, "performance:memory:gross");
    }

    /**
     * 
     * @see IObjectEvaluator#evaluate(Alternative, SampleObject, DigitalObject, List, IStatusListener)
     */
    public HashMap<MeasurementInfoUri, Value> evaluate(Alternative alternative, SampleObject sample, DigitalObject result, List<MeasurementInfoUri> measurementInfoUris, IStatusListener listener) throws EvaluatorException {
        HashMap<MeasurementInfoUri, Value> results = new HashMap<MeasurementInfoUri, Value>();
        for (MeasurementInfoUri m : measurementInfoUris) {
            Value v = evaluate(alternative, sample, result, m);
            if (v != null) {
                results.put(m, v);
            }
        }
        return results;
    }

    public Value evaluate(Alternative alternative, SampleObject sample, DigitalObject result, MeasurementInfoUri measurementInfoUri) {
        String propertyURI = measurementInfoUri.getAsURI();
        Scale scale = descriptor.getMeasurementScale(measurementInfoUri);
        if (scale == null) {
            return null;
        }
        double sampleSize = sample.getData().getSize() * (1024 * 1024);
        if (OBJECT_ACTION_ACTIVITYLOGGING_AMOUNT.equals(propertyURI)) {
            Map<SampleObject, DetailedExperimentInfo> detailedInfo = alternative.getExperiment().getDetailedInfo();
            DetailedExperimentInfo detailedExperimentInfo = detailedInfo.get(sample);
            if ((detailedExperimentInfo != null) && (detailedExperimentInfo.getProgramOutput() != null)) {
                PositiveIntegerValue v = (PositiveIntegerValue) scale.createValue();
                v.setValue(detailedExperimentInfo.getProgramOutput().length());
                v.setComment("extracted from experiment details");
                return v;
            }
            return null;
        } else if (OBJECT_ACTION_ACTIVITYLOGGING_FORMAT.equals(propertyURI)) {
            Map<SampleObject, DetailedExperimentInfo> detailedInfo = alternative.getExperiment().getDetailedInfo();
            DetailedExperimentInfo detailedExperimentInfo = detailedInfo.get(sample);
            if ((detailedExperimentInfo != null) && (detailedExperimentInfo.getProgramOutput() != null)) {
                FreeStringValue v = (FreeStringValue) scale.createValue();
                v.setValue(evaluateLogging(detailedExperimentInfo.getProgramOutput()));
                v.setComment("extracted from experiments details");
                return v;
            }
            return null;
        } else if (OBJECT_ACTION_RUNTIME_PERFORMANCE_THROUGHPUT.equals(propertyURI)) {
            Value extracted = extractMeasuredValue(alternative, sample, OBJECT_ACTION_RUNTIME_PERFORMANCE_TIME_PERSAMPLE);
            if (extracted instanceof PositiveFloatValue) {
                PositiveFloatValue value = new PositiveFloatValue();
                double floatVal = ((PositiveFloatValue) extracted).getValue();
                if (Double.compare(floatVal, 0.0) != 0) {
                    floatVal = floatVal / sampleSize;
                    value.setValue((1.0 / (floatVal / 1000.0)));
                }
                value.setComment("extracted from experiment details");
                return value;
            }
        } else if (OBJECT_ACTION_RUNTIME_PERFORMANCE_TIME_PERMB.equals(propertyURI)) {
            Value extracted = extractMeasuredValue(alternative, sample, OBJECT_ACTION_RUNTIME_PERFORMANCE_TIME_PERSAMPLE);
            if (extracted instanceof PositiveFloatValue) {
                PositiveFloatValue value = new PositiveFloatValue();
                double floatVal = ((PositiveFloatValue) extracted).getValue();
                if (Double.compare(floatVal, 0.0) != 0) {
                    floatVal = floatVal / sampleSize;
                    value.setValue(floatVal);
                }
                value.setComment("extracted from experiment details");
                return value;
            }
        } else if (OBJECT_ACTION_RUNTIME_PERFORMANCE_MEMORY_PERMB.equals(propertyURI)) {
            Value extracted = extractMeasuredValue(alternative, sample, OBJECT_ACTION_RUNTIME_PERFORMANCE_MEMORY_PERSAMPLE);
            if (extracted instanceof PositiveFloatValue) {
                PositiveFloatValue value = new PositiveFloatValue();
                double floatVal = ((PositiveFloatValue) extracted).getValue();
                value.setValue(floatVal / sampleSize);
                value.setComment("extracted from experiment details");
                return value;
            }
        }
        Value extracted = extractMeasuredValue(alternative, sample, propertyURI);
        if (extracted != null) {
            extracted.setComment("extracted from experiment details");
        }
        return extracted;
    }

    /**
     * extracts a measured value from detailed experimentInfo
     * (which is populated atm by minimee services)
     * 
     * @param alternative
     * @param sample
     * @param key
     * @return
     */
    private Value extractMeasuredValue(Alternative alternative, SampleObject sample, String propertyURI) {
        Map<SampleObject, DetailedExperimentInfo> detailedInfo = alternative.getExperiment().getDetailedInfo();
        DetailedExperimentInfo detailedExperimentInfo = detailedInfo.get(sample);
        if (detailedExperimentInfo != null) {
            String measuredProperty = propertyToMeasuredValues.get(propertyURI);
            Measurement m = detailedExperimentInfo.getMeasurements().get(measuredProperty);
            if (m != null) {
                return m.getValue();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String evaluateLogging(String logOutput) {
        if ((logOutput == null) || "".equals(logOutput)) {
            return "none";
        } else {
            String result = "text";
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            try {
                Schema schema = factory.newSchema();
                Validator validator = schema.newValidator();
                validator.validate(new StreamSource(new StringReader(logOutput)));
                result = "XML";
            } catch (SAXException e) {
            } catch (IOException e) {
                log.error("logoutput-evaluator is not properly configured: ", e);
            }
            return result;
        }
    }
}
