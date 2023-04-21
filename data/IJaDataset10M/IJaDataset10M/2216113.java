package com.w20e.socrates.data;

import java.util.Locale;
import com.w20e.socrates.expression.XObject;

/**
 * @author D.A.Dokter
 * Implement data type notion for Socrates, based on XForms data types.
 * Typing is implemented as a kind of constraint, that values can be matched
 * against, so as to be able to use datatypes in a flyweight pattern: only one
 * datatype of any type is needed.
 */
public interface DataType {

    /**
   * Evaluate an object against this type. The evaluation should traverse all
   * transformations and restrictions, and return an object only if all
   * restrictions are met. Otherwise an unchecked exception should be thrown.
   * The resulting value of <code>eval</code> should be such, that calling eval
   * again will still yield a valid result, or rather the same result. Design
   * your data type implementations with care!
   * The eval method should return the value space object for this type.
   *
   * In general, eval should be at least able to handle string representations
   * for a datatype, since the most common use will be questionnaires through
   * the web, where user input will largely have the form of a string.
   * @param value Object to test.
   * @return eval'ed object
   */
    XObject eval(Object value);

    /**
   * Get the lexical value for this type.
   * @param value Get the lexical value
   * @param l Use the locale to create the lexical value. This is mainly used
   * in, for instance, currency types and date types.
   * @return lexical value
   */
    String evalLexical(Object value, Locale l);

    /**
   * Add a restriction to this type.
   * @param restriction Restriction to add.
   */
    void addRestriction(Restriction restriction);

    /**
   * Add a transformation to this type.
   * @param trans Transformation to add.
   */
    void addTransformation(Transformation trans);

    /**
   * Set the transformation for converting to the lexical representation.
   * @param trans Transformation for lexical space.
   */
    void addLexicalTransformation(LexicalTransformation trans);
}
