package fr.n7.khome.view;

public class ResultField {

    private Object field;

    private Long id;

    private float firstRowGLikelihood;

    private float likelihood;

    private float gLikelihood;

    private float sumLikelihood;

    private float firstRowGNecessity;

    private float necessity;

    private float gNecessity;

    private float sumNecessity;

    public ResultField(Object o, Long id, float likelihood, float gLikelihood, float sumLikelihood, float firstRowGLikelihood, float necessity, float gNecessity, float sumNecessity, float firstRowGNecessity) {
        this.field = o;
        this.id = id;
        this.likelihood = likelihood;
        this.gLikelihood = gLikelihood;
        this.sumLikelihood = sumLikelihood;
        this.firstRowGLikelihood = firstRowGLikelihood;
        this.necessity = necessity;
        this.gNecessity = gNecessity;
        this.sumNecessity = sumNecessity;
        this.firstRowGNecessity = firstRowGNecessity;
    }

    public float getLikelihood() {
        return likelihood;
    }

    public float getGLikelihood() {
        return gLikelihood;
    }

    public float getSumLikelihood() {
        return sumLikelihood;
    }

    public float getFirstRowGLikelihood() {
        return firstRowGLikelihood;
    }

    public float getNecessity() {
        return necessity;
    }

    public float getGNecessity() {
        return gNecessity;
    }

    public float getSumNecessity() {
        return sumNecessity;
    }

    public float getFirstRowGNecessity() {
        return firstRowGNecessity;
    }

    public Object getField() {
        return field;
    }

    public Long getID() {
        return id;
    }
}
