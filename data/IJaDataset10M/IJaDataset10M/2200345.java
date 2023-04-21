package org.neurpheus.nlp.morphology.baseimpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.neurpheus.nlp.morphology.AnalysisResult;

/**
 * Represents a result which can be returned by morphological components.
 * <p>
 * Morphological components can return many possible results for a single analysed form. 
 * Each result item consist of a result form and an accuracy of the result. 
 * The accuracy is represented by a real number between 0.0 and 1.0. If this value 
 * is near 1.0, the produced result is correct with a high probability.
 * </p>
 * <p>
 * Morphological components can return many results having high accuracy.
 * This situation occurs when it is impossible to select single result analysing 
 * only the form of a word. The final selection should be performed by 
 * a context or semantic analysis.
 * </p>
 * 
 * @author Jakub Strychowski
 */
public class AnalysisResultImpl implements AnalysisResult {

    /** Defines an identifier of this class. */
    protected static final long serialVersionUID = -770608080315155027L;

    /** Holds result form. */
    protected String form;

    /** Holds an accuracy of an analysis. */
    protected double accuracy;

    /**
     * Constructs empty result.
     */
    public AnalysisResultImpl() {
        form = "";
        accuracy = 0;
    }

    /**
     * Constructs an analysis result from the given word form and accuracy value.
     * 
     * @param formValue The word form of the result.
     * @param accuracyValue The accuracy of the result.
     */
    public AnalysisResultImpl(final String formValue, final double accuracyValue) {
        if (formValue == null) {
            throw new NullPointerException("The [formValue] argument cannot be null.");
        }
        form = formValue;
        if (accuracyValue < 0 || accuracyValue > 1) {
            throw new IllegalArgumentException("The [accuracyValue] shoule be a value between 0.0 and 1.0 !");
        }
        accuracy = accuracyValue;
    }

    /**
     * Returns the result form of an analysed word form.
     *
     * @return The result form; for example a stem or lemma form.
     */
    public String getForm() {
        return form;
    }

    /**
     * Sets the result form of an analysed word form.
     *
     * @param formValue The result form; for example a stem or lemma form.
     */
    public void setForm(final String formValue) {
        if (formValue == null) {
            throw new NullPointerException("The [formValue] argument cannot be null.");
        }
        form = formValue;
    }

    /**
     * Returns the accuracy of the produced form. 
     *
     * @return  The accuracy of analysis as a real number between 0.0 (very low 
     *          accuracy) and 1.0 (very high accuracy).
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Sets the accuracy of the produced form. 
     *
     * @param accuracyValue The accuracy of analysis as a real number between 0.0 (very low 
     *          accuracy) and 1.0 (very high accuracy).
     */
    public void setAccuracy(final double accuracyValue) {
        if (accuracyValue < 0 || accuracyValue > 1) {
            throw new IllegalArgumentException("The [accuracyValue] shoule be a value between 0.0 and 1.0 !");
        }
        accuracy = accuracyValue;
    }

    /**
     * Compares this analysis result with another one.
     * An analysis result A is greater then analysis result B if an accuracy 
     * of A is greater then an accuracy of B.
     * 
     * @param o The object which have to be compared.
     * 
     * @return <code>value &gt; 0</code> if this analysis result is greater then the given analysis result,
     *  <code>0</code> if both analysis results are equal, and <code>value &lt; 0</code> otherwise.
     */
    public int compareTo(final Object o) {
        if (o == null) {
            return 1;
        } else {
            AnalysisResult ar = (AnalysisResult) o;
            if (accuracy == ar.getAccuracy()) {
                return form.compareTo(ar.getForm());
            } else {
                return -Double.compare(accuracy, ar.getAccuracy());
            }
        }
    }

    /** Value used by hash function. */
    private static final int HASH1 = 7;

    /** Value used by hash function. */
    private static final int HASH2 = 19;

    /** Value used by hash function. */
    private static final int HASH3 = 32;

    /**
     * Return a hash code of this object.
     * 
     * @return almost unique identifier of this analysis result.
     */
    public int hashCode() {
        int hash = HASH1;
        hash = HASH2 * hash + (this.form != null ? this.form.hashCode() : 0);
        hash = HASH2 * hash + (int) (Double.doubleToLongBits(this.accuracy) ^ (Double.doubleToLongBits(this.accuracy) >>> HASH3));
        return hash;
    }

    /**
     * Checks if the given object is equals to this analysis result.
     * Two analysis results are equal if their forms are equals.
     * 
     * @param o The object which have to be compare.
     * 
     * @return <code>true</code> if the given object is equal to this object.
     */
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else {
            AnalysisResult ar = (AnalysisResult) o;
            return form.equals(ar.getForm());
        }
    }

    /**
     * Serialises the object to the output stream.
     * 
     * @param oos The output stream.
     * @throws java.io.IOException if any i/o error occurred. 
     */
    protected void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.writeUTF(form);
        oos.writeDouble(accuracy);
    }

    /**
     * Deserialises this object from the input stream.
     * 
     * @param ois The input stream.
     * 
     * @throws java.lang.ClassNotFoundException if there is any class mapping problem.
     * @throws java.io.IOException if any i/o error occurred. 
     */
    protected void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        form = ois.readUTF();
        accuracy = ois.readDouble();
    }

    /** 
     * Returns string representing this analysis result.
     * 
     * @return Result in the form : $BASEFORM [$ACCURACY].
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append(getForm());
        res.append(" [");
        res.append(getAccuracy());
        res.append("] ");
        return res.toString();
    }

    /**
     * Checks if this result is certain.
     * 
     * @return <code>true</code> if this result is for sure correct.
     */
    public boolean isCertain() {
        return getAccuracy() >= 1.0;
    }

    /**
     * Writes this object to a data stream.
     *
     * @param out The data stream into which this object should be serialized.
     *
     * @throws java.io.IOException If any output error occurred.
     */
    public void write(DataOutputStream out) throws IOException {
        TagsetStreamPacker.writeString(form, out);
        if (accuracy == 1.0) {
            out.writeBoolean(true);
        } else {
            out.writeBoolean(false);
            out.writeFloat((float) accuracy);
        }
    }

    /**
     * Reads this object from a data stream.
     *
     * @param in The data stream from which this object should be serialized.
     *
     * @throws java.io.IOException If any input error occurred.
     */
    public void read(DataInputStream in) throws IOException {
        form = TagsetStreamPacker.readString(in);
        if (in.readBoolean()) {
            accuracy = 1.0;
        } else {
            accuracy = in.readFloat();
        }
    }
}
