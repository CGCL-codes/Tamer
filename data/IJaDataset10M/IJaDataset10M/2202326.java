package com.ibm.safe.accesspath;

import com.ibm.wala.classLoader.IField;

/**
 * A field in an access-path string.
 * 
 * @author Eran Yahav (yahave)
 * @author Stephen Fink
 */
public abstract class FieldPathElement implements PathElement {

    /**
   * A unique identifier of a field in a class
   */
    private IField fld;

    /**
   * create a new field-element
   * 
   * @param fld - field element to serve as part of a path
   */
    public FieldPathElement(IField fld) {
        assert fld != null : "cannot create null element";
        this.fld = fld;
    }

    @Override
    public String toString() {
        return fld.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && getClass().equals(o.getClass())) {
            FieldPathElement other = (FieldPathElement) o;
            return (fld.equals(other.fld));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 5153 * fld.hashCode();
    }

    public IField getField() {
        return fld;
    }
}
