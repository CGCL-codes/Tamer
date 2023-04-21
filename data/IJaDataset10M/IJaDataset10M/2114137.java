package org.japano.pagenode.jstl.core;

import org.japano.Buffer;
import org.japano.PageNode;

/**
 Subtag of <choose> that includes its body if its condition evalutes to 'true' 
 
 @author Sven Helmberger ( sven dot helmberger at gmx dot de )
 @version $Id: When.java,v 1.3 2005/09/27 21:30:51 fforw Exp $
 #SFLOGO# 
 
 @japano.tag library="/japano/jstl/core" 
 */
public class When extends PageNode {

    private boolean test;

    /**
   Condition.
   
   @japano.attribute
   */
    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean isTest() {
        return test;
    }
}
