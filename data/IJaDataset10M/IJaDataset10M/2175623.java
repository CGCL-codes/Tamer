package edu.gsbme.msource.MathModel.Element;

/**
 * Weak term math object
 * @author David
 *
 */
public class WeakTerm extends MathObject {

    public String constr;

    public String dweak;

    public String weak;

    public WeakTerm(String id, String model_id) {
        super(id, model_id);
    }
}
