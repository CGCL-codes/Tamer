package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v1_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for numerical_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="numerical_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="TimeSteps" type="{}time_steps_type" minOccurs="0"/>
 *         &lt;element name="KSPTolerances" type="{}ksp_tolerances_type" minOccurs="0"/>
 *         &lt;element name="KSPSolver" type="{}ksp_solver_type" minOccurs="0"/>
 *         &lt;element name="KSPPreconditioner" type="{}ksp_preconditioner_type" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "numerical_type", propOrder = {  })
public class NumericalType {

    @XmlElement(name = "TimeSteps")
    protected TimeStepsType timeSteps;

    @XmlElement(name = "KSPTolerances")
    protected KspTolerancesType kspTolerances;

    @XmlElement(name = "KSPSolver")
    protected KspSolverType kspSolver;

    @XmlElement(name = "KSPPreconditioner")
    protected KspPreconditionerType kspPreconditioner;

    /**
     * Gets the value of the timeSteps property.
     * 
     * @return
     *     possible object is
     *     {@link TimeStepsType }
     *     
     */
    public TimeStepsType getTimeSteps() {
        return timeSteps;
    }

    /**
     * Sets the value of the timeSteps property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeStepsType }
     *     
     */
    public void setTimeSteps(TimeStepsType value) {
        this.timeSteps = value;
    }

    /**
     * Gets the value of the kspTolerances property.
     * 
     * @return
     *     possible object is
     *     {@link KspTolerancesType }
     *     
     */
    public KspTolerancesType getKSPTolerances() {
        return kspTolerances;
    }

    /**
     * Sets the value of the kspTolerances property.
     * 
     * @param value
     *     allowed object is
     *     {@link KspTolerancesType }
     *     
     */
    public void setKSPTolerances(KspTolerancesType value) {
        this.kspTolerances = value;
    }

    /**
     * Gets the value of the kspSolver property.
     * 
     * @return
     *     possible object is
     *     {@link KspSolverType }
     *     
     */
    public KspSolverType getKSPSolver() {
        return kspSolver;
    }

    /**
     * Sets the value of the kspSolver property.
     * 
     * @param value
     *     allowed object is
     *     {@link KspSolverType }
     *     
     */
    public void setKSPSolver(KspSolverType value) {
        this.kspSolver = value;
    }

    /**
     * Gets the value of the kspPreconditioner property.
     * 
     * @return
     *     possible object is
     *     {@link KspPreconditionerType }
     *     
     */
    public KspPreconditionerType getKSPPreconditioner() {
        return kspPreconditioner;
    }

    /**
     * Sets the value of the kspPreconditioner property.
     * 
     * @param value
     *     allowed object is
     *     {@link KspPreconditionerType }
     *     
     */
    public void setKSPPreconditioner(KspPreconditionerType value) {
        this.kspPreconditioner = value;
    }
}
