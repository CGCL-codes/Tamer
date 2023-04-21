package com.freality.programming.test;

import java.lang.reflect.Constructor;

/** 
 * This class represents a Constructor that generated a Throwable
 * during testing.
 *
 * @author <a href="mailto:pablo@webmind.com">Pablo Mayrgundter</a>
 * @version $Revision: 1.1.1.1 $
 * @since 1.0
 * @date $Date: 2001/03/07 19:21:05 $
 */
public class ProblemedConstructor extends ProblemedMember {

    Constructor constructor;

    Exception exception;

    Object[] parameters;

    ProblemedConstructor(Constructor constructor, Exception exception, Object[] parameters) {
        super(constructor, exception);
        this.parameters = parameters;
    }

    public String toString() {
        if (parameters == null) return super.toString() + "\n with a null parameter.\n";
        String returnString = super.toString() + "\nwith " + parameters.length + " parameters: \n";
        for (int i = 0; i < parameters.length; i++) returnString += "\t" + parameters[i] + "\n";
        return returnString;
    }
}
