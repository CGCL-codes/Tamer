package at.campus02.datapit.core.algorithm.evaluation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import at.campus02.datapit.core.algorithm.parameter.ParameterSpecification;

/**
 * 
 * @author Gerhard Schlager
 */
@XmlType(name = "EvaluationMode", propOrder = { "name", "type", "parameterSpecifications" })
@XmlAccessorType(XmlAccessType.FIELD)
public class EvaluationMode {

    @XmlElement(name = "Name", nillable = false, required = true)
    private String name;

    @XmlElement(name = "Type", nillable = false, required = true)
    private EvaluationType type;

    @XmlElement(name = "ParameterSpecifications", nillable = true, required = true)
    private List<ParameterSpecification> parameterSpecifications = new ArrayList<ParameterSpecification>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EvaluationType getType() {
        return type;
    }

    public void setType(EvaluationType type) {
        this.type = type;
    }

    public List<ParameterSpecification> getParameterSpecifications() {
        return Collections.unmodifiableList(parameterSpecifications);
    }

    public boolean addParameterSpecification(ParameterSpecification specification) {
        return parameterSpecifications.add(specification);
    }

    public boolean removeParameterSpecification(ParameterSpecification specification) {
        return parameterSpecifications.remove(specification);
    }
}
