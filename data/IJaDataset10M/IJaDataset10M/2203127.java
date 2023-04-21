package pl.edu.amu.xtr.cors;

/**
 * Defines correspondences beetwean two elements in mapped XSD sxhemas. Corespondences are defined as <code>String</code> to
 * allow functions.
 * 
 * @author Jakub Marciniak
 */
public class Correspondence {

    private String sourceElement;

    private String targetElement;

    private Boolean isFunctional;

    private AggregateFunction aggregateFunction = AggregateFunction.NONE;

    public Correspondence(String sourceElement, String targetElement, boolean isFunctional) {
        this.sourceElement = sourceElement.trim();
        this.targetElement = targetElement.trim();
        this.isFunctional = isFunctional;
    }

    public Correspondence() {
        isFunctional = Boolean.FALSE;
    }

    public String getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(String sourceElement) {
        this.sourceElement = sourceElement.trim();
    }

    public String getTargetElement() {
        return targetElement;
    }

    public void setTargetElement(String targetElement) {
        this.targetElement = targetElement.trim();
    }

    public Boolean getIsFunctional() {
        return isFunctional;
    }

    public void setIsFunctional(Boolean isFunctional) {
        this.isFunctional = isFunctional;
    }

    public AggregateFunction getAggregateFunction() {
        return aggregateFunction;
    }

    public void setAggregateFunction(AggregateFunction aggregateFunction) {
        this.aggregateFunction = aggregateFunction;
    }
}
