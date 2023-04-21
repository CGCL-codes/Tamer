package com.pjsofts.eurobudget.beans;

import com.pjsofts.eurobudget.EBConstants;
import java.io.Serializable;
import java.util.ResourceBundle;

/**
 * JavaBean
 * Could be a person, a company or everything that could do financial transactions.
 * (Company,Person or Tiers)
 * @author  PJ
 */
public class Entity extends Object implements java.lang.Comparable, Serializable {

    private static final long serialVersionUID = -1403528033407380856L;

    /** usefull for combo list */
    public static final transient Entity ENTITY_NULL = new Entity("", "", "", "", "");

    /** can't be null 
     * @serial
     */
    private String name;

    /** could be null 
     * @serial
     */
    private String surname;

    /** @serial */
    private String address;

    /** @serial */
    private String tel;

    /** @serial */
    private String email;

    private static final transient ResourceBundle i8n = EBConstants.i18n;

    public Entity() {
    }

    public Entity(String name, String surname, String address, String tel, String email) {
        this();
        setName(name);
        setSurname(surname);
        setAddress(address);
        setTel(tel);
        setEmail(email);
    }

    /** Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }

    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        if (name != null) name = name.trim();
        this.name = name;
    }

    /** Getter for property surname.
     * @return Value of property surname.
     */
    public java.lang.String getSurname() {
        return surname;
    }

    /** Setter for property surname.
     * @param surname New value of property surname.
     */
    public void setSurname(java.lang.String surname) {
        if (surname != null) surname = surname.trim();
        this.surname = surname;
    }

    /** Getter for property address.
     * @return Value of property address.
     */
    public java.lang.String getAddress() {
        return address;
    }

    /** Setter for property address.
     * @param address New value of property address.
     */
    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    /** Getter for property tel.
     * @return Value of property tel.
     */
    public java.lang.String getTel() {
        return tel;
    }

    /** Setter for property tel.
     * @param tel New value of property tel.
     */
    public void setTel(java.lang.String tel) {
        this.tel = tel;
    }

    /** Getter for property email.
     * @return Value of property email.
     */
    public java.lang.String getEmail() {
        return email;
    }

    /** Setter for property email.
     * @param email New value of property email.
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    /** */
    public String toString() {
        String retValue = getName();
        if (getSurname() != null && !getSurname().equals("")) retValue += " (" + getSurname() + ")";
        return retValue;
    }

    /** should be sort of reverse as toString method */
    public boolean isEqual(String s) {
        if (s == null) return false;
        s = s.trim();
        if (this.getName() == null) return false;
        if (this.getName().equalsIgnoreCase(s) || (this.getSurname() != null && this.getSurname().equalsIgnoreCase(s)) || this.toString().equalsIgnoreCase(s)) return true;
        return false;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.<p>
     *
     * In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of <i>expression</i>
     * is negative, zero or positive.
     *
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)<p>
     *
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.<p>
     *
     * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.<p>
     *
     * It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * @param   o the Object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     * 		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *        from being compared to this Object.
     */
    public int compareTo(Object o) {
        if (o instanceof Entity) {
            Entity obj = (Entity) o;
            if (obj == null || obj.getName() == null) return 1;
            if (this.getName() == null) return -1;
            return this.getName().compareToIgnoreCase(obj.getName());
        } else {
            throw new java.lang.IllegalArgumentException(i8n.getString("error_Compare_only_to_another_Entity"));
        }
    }
}
