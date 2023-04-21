package com.sharkysoft.printf;

import java.util.ArrayList;
import java.util.List;

/**
 * Syntactic sugar for variable argument lists.
 * 
 * <p><b>Details:</b>  <code>PrintfData</code> offers an alternative method for
 * defining the data vector in Printf For Java.  If you don't like the idea of
 * wrapping primitive types in their object counterparts and referencing them in 
 * an <code>Object</code> array, then perhaps you'll like the syntactic sugar 
 * afforded by this helper class.  <em>Use of this class is entirely 
 * optional.</em></p>
 * 
 * <p>To build a printf data vector using this class, first instantiate an 
 * instance, and then chain together calls to its overloaded <code>add</code> 
 * method, furnishing the printf input data one element at a time.  (Each 
 * <code>add</code> call returns the same instance of this class, to facilitate 
 * chaining.)  After the last element has been added, the result can then be 
 * supplied directly to most {@link Printf} methods.</p>
 *
 * <blockquote class="example">
 *  
 *   <p><b>Example:</b> The two {@link Printf} in this code are equivalent:</p>
 * 
    *<blockquote><pre>
      *String format = "My dog %s is %d years old.";
      *String name = "Spot";
      *int age = 3;
      *<i>// With Object array:</i>
      *Printf.out(format, <b>new Object[]{name, new Integer(age)}</b>);
      *<i>// With PrintfData:</i>
      *Printf.out(format, <b>new PrintfData().add(name).add(age)</b>);
    *</pre></blockquote>
    
 * </blockquote>
 *
 * <p>In the first {@link Printf} call above, an <code>Object</code> array is 
 * explicitely created, and the primitive type is explicitely wrapped.  In the 
 * second call, no <code>Object</code> array is created, and no primitive type 
 * is wrapped.  <code>Printf</code> accepts the data vector returned by the last 
 * <code>add</code> call.</p>
 * 
 * <p>Behind the scenes, <code>Printf</code> simply converts the 
 * <code>PrintfData</code> to an <code>Object[]</code>.  Therefore, straight 
 * <code>Object</code> arrays are always more efficient.  The choice is 
 * yours.</p>
 * 
 * @since before 1998.02.19
 * @version 1998.12.23
 * 
 * @author Sharky
 */
public class PrintfData {

    private final List mpList;

    /**
   * Initializes empty vector with undetermined length.
   * 
   * <p><b>Details:</b> This constructor initializes an empty data vector.  Use
   * this constructor when the final length of the data vector is unknown at
   * compile time.</p>
   */
    public PrintfData() {
        mpList = new ArrayList();
    }

    /**
   * Initializes empty vector with final length.
   * 
   * <p><b>Details:</b> This constructor initializes an empty data vector whose 
   * final length has already been determined.  Use this constructor whenever 
   * possible to create more efficient code.  If the predicted final length of 
   * this data vector ultimately differs from the value passed into this 
   * constructor, no harm is done.</p>
   * 
   * @param inLength the estimated length 
   */
    public PrintfData(int inLength) {
        mpList = new ArrayList(inLength);
    }

    /**
   * Appends Object data.  
   * 
   * <p><b>Details:</b> Use this method to append any type of object, including 
   * <code>String</code>s, to this data vector.  The argument will not be 
   * wrapped.</p>
   * 
   * @param ipO the object
   * @return this instance
   */
    public PrintfData add(Object ipO) {
        mpList.add(ipO);
        return this;
    }

    /**
   * Appends char data.
   * 
   * <p><b>Details:</b> Use this method to append a <code>char</code> to this 
   * data vector.  The argument will be wrapped in a <code>Character</code>.</p>
   * 
   * @param icC the char
   * @return this instance
   */
    public PrintfData add(char icC) {
        return add(new Character(icC));
    }

    /**
	 * Appends short data.
	 * 
	 * <p><b>Details:</b> Use this method to append a <code>short</code> to this 
	 * data vector.  The argument will be wrapped in a <code>Short</code>.</p>
	 * 
	 * @param iwS the short
   * @return this instance
	 */
    public PrintfData add(short iwS) {
        return add(new Short(iwS));
    }

    /**
	 * Appends int data.
	 * 
	 * <p><b>Details:</b> Use this method to append an <code>int</code> to this 
	 * data vector.  The argument will be wrapped in a <code>Integer</code>.</p>
	 * 
	 * @param inI the int
   * @return this instance
	 */
    public PrintfData add(int inI) {
        return add(new Integer(inI));
    }

    /**
	 * Appends long data.
	 * 
	 * <p><b>Details:</b> Use this method to append a <code>long</code> to this 
	 * data vector.  The argument will be wrapped in a <code>Long</code>.</p>
	 * 
	 * @param ilL the long
   * @return this instance
	 */
    public PrintfData add(long ilL) {
        return add(new Long(ilL));
    }

    /**
	 * Appends float data.
	 * 
	 * <p><b>Details:</b> Use this method to append a <code>float</code> to this 
	 * data vector.  The argument will be wrapped in a <code>Float</code>.</p>
	 * 
	 * @param ifF the float
   * @return this instance
	 */
    public PrintfData add(float ifF) {
        return add(new Float(ifF));
    }

    /**
	 * Appends double data.
	 * 
	 * <p><b>Details:</b> Use this method to append a <code>double</code> to this 
	 * data vector.  The argument will be wrapped in a <code>Double</code>.</p>
	 * 
	 * @param idD the double
   * @return this instance
	 */
    public PrintfData add(double idD) {
        return add(new Double(idD));
    }

    /**
	 * Appends byte data.
	 * 
	 * <p><b>Details:</b> Use this method to append a <code>byte</code> to this 
	 * data vector.  The argument will be wrapped in a <code>Byte</code>.</p>
	 * 
	 * @param ibB the byte
   * @return this instance
	 */
    public PrintfData add(final byte ibB) {
        return add(new Byte(ibB));
    }

    /**
	 * Appends boolean data.
	 * 
	 * <p><b>Details:</b> Use this method to append a <code>boolean</code> to this 
	 * data vector.  The argument will be wrapped in a <code>Boolean</code>.</p>
	 * 
	 * @param izB the boolean
   * @return this instance
	 */
    public PrintfData add(final boolean izB) {
        return add(new Boolean(izB));
    }

    /**
	 * Converts to Object array.
	 * 
	 * <p><b>Details:</b> Although it is not necessary in most cases, this method
	 * can be used to convert this data vector into an <code>Object</code> 
	 * array.</p>
	 * 
	 * @return the Object array
	 */
    public Object[] done() {
        return mpList.toArray();
    }
}

;
