package com.gusto.engine.colfil;

/**
 * <p>A {@link Prediction} is a special {@link Evaluation} 
 * that contains the prediction of evaluation for a certain Item.</p>
 * 
 * @author amokrane.belloui@gmail.com
 *
 */
public class Prediction extends Evaluation {

    public Prediction() {
        super();
    }

    public Prediction(Double value) {
        super(value);
    }
}
