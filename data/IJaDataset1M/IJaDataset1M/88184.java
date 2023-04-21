package org.tolven.gen.bean;

/**
 * Command object directing an MDB to generate patients for an account. Create a number of families and add them to 
 * the specified account. This does not cause the direct creation of the families but instead creates
 * the virtual family and then passes that to the data generator.
 * @author John Churin
 *
 */
public abstract class GenControlAccount extends GenControlBase {

    private static final long serialVersionUID = 1L;
}
