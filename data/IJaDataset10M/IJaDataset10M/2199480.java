package org.gjt.universe;

import java.util.Vector;

/** This class represents a polynomial.  */
class Polynomial {

    private Vector elements;

    Polynomial(String representation) {
        elements = new Vector();
    }

    float evaluate(float value) {
        float returnvalue = (float) 0.0;
        int size = elements.size();
        for (int cnt = 0; cnt < size; cnt++) {
            Element thisElement = (Element) elements.elementAt(cnt);
            returnvalue += thisElement.evaluate(value);
        }
        return returnvalue;
    }

    private class Element {

        private float coefficient;

        private float exponent;

        Element(float coefficient, float exponent) {
            this.coefficient = coefficient;
            this.exponent = exponent;
        }

        float evaluate(float value) {
            return coefficient * (float) Math.pow(value, exponent);
        }
    }
}
