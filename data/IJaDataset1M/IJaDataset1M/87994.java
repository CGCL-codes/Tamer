package eu.mpower.framework.fsa.j2me.types;

/**
 * <p>Java class for str complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="str">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.mpower-project.eu/types}Value">
 *       &lt;sequence>
 *         &lt;element name="Min" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Max" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
public class Str extends Value {

    protected int min;

    protected int max;

    /**
     * Gets the value of the min property.
     * 
     */
    public int getMin() {
        return min;
    }

    /**
     * Sets the value of the min property.
     * 
     */
    public void setMin(int value) {
        this.min = value;
    }

    /**
     * Gets the value of the max property.
     * 
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     * 
     */
    public void setMax(int value) {
        this.max = value;
    }
}
