package org.jecars.support;

import java.util.*;
import javax.jcr.*;

/**
 * CARS_DefaultNodeIterator
 *
 * @version $Id: CARS_DefaultNodeIterator.java,v 1.1 2007/09/26 14:19:06 weertj Exp $
 */
public class CARS_DefaultNodeIterator extends CARS_DefaultRangeIterator implements NodeIterator {

    public Node nextNode() {
        try {
            return (Node) next();
        } catch (ArrayIndexOutOfBoundsException ae) {
            throw new NoSuchElementException();
        }
    }
}
