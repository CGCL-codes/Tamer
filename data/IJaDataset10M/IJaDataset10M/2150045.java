package at.campus02.datapit.core.classifier;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import at.campus02.datapit.core.algorithm.dataset.DataSet;
import at.campus02.datapit.core.algorithm.evaluation.EvaluationType;
import at.campus02.datapit.core.algorithm.parameter.Parameter;

/**
 * 
 * @author Gerhard Schlager
 */
@XmlType(name = "BuildClassifierParameter", propOrder = { "algorithmName", "modelName", "dataSet", "classAttributeName", "classifierParameters", "evaluationType", "evaluationParameters" })
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildClassifierParameter {

    @XmlElement(name = "AlgorithmName", nillable = false, required = true)
    @XmlSchemaType(name = "token")
    private String algorithmName;

    @XmlElement(name = "ModelName", nillable = false, required = true)
    @XmlSchemaType(name = "token")
    private String modelName;

    @XmlElement(name = "DataSet", nillable = false, required = true)
    private DataSet dataSet;

    @XmlElement(name = "ClassAttributeName", nillable = false, required = true)
    @XmlSchemaType(name = "token")
    private String classAttributeName;

    @XmlElement(name = "ClassifierParameters", nillable = true, required = true)
    private List<Parameter> classifierParameters;

    @XmlElement(name = "EvaluationType", nillable = false, required = true)
    private EvaluationType evaluationType;

    @XmlElement(name = "EvaluationParameters", nillable = true, required = true)
    private List<Parameter> evaluationParameters;

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getModelName() {
        return modelName;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public String getClassAttributeName() {
        return classAttributeName;
    }

    public List<Parameter> getClassifierParameters() {
        return classifierParameters;
    }

    public EvaluationType getEvaluationType() {
        return evaluationType;
    }

    public List<Parameter> getEvaluationParameters() {
        return evaluationParameters;
    }
}
