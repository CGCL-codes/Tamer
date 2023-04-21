package lechuga.core.exceptions;

import lechuga.core.contextelems.LechugaContextStruct;

/**
 * Advices for a wrong IdBean requested to the Lechuga Context, and
 * offers a possibly correct alternative, using the Levenshtein algorithm.
 * 
 * @author mhoms
 */
public class LechugaWrongIdBeanException extends LechugaException {

    private static final long serialVersionUID = 1L;

    public LechugaWrongIdBeanException(final String wrongIdBean, final LechugaContextStruct context) {
        super("supplied idBean not declared: " + wrongIdBean + (new PossiblyCorrectIdBeanFinder()).find(wrongIdBean, context));
    }
}
