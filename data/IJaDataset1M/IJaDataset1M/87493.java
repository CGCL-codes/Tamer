package uk.ac.shef.wit.trex.validation;

/**
 * Splits the input data randomly into one train fold and one test fold for a combinersingle run.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public class ValidationOneRun extends ValidationNRandom {

    public ValidationOneRun() {
        super(1, 50);
    }

    public String toString() {
        return "one run";
    }
}
