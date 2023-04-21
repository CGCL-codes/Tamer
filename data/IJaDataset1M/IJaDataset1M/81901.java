package org.apache.jasper.runtime;

import java.lang.reflect.InvocationTargetException;
import javax.naming.NamingException;
import org.apache.AnnotationProcessor;

/**
 * Verify the annotation and Process it.
 *
 * @author Fabien Carrion
 * @author Remy Maucherat
 * @version $Revision: 467222 $, $Date: 2006-10-24 05:17:11 +0200 (Tue, 24 Oct 2006) $
 */
public class AnnotationHelper {

    /**
     * Call postConstruct method on the specified instance. Note: In Jasper, this
     * calls naming resources injection as well.
     */
    public static void postConstruct(AnnotationProcessor processor, Object instance) throws IllegalAccessException, InvocationTargetException, NamingException {
        if (processor != null) {
            processor.processAnnotations(instance);
            processor.postConstruct(instance);
        }
    }

    /**
     * Call preDestroy method on the specified instance.
     */
    public static void preDestroy(AnnotationProcessor processor, Object instance) throws IllegalAccessException, InvocationTargetException {
        if (processor != null) {
            processor.preDestroy(instance);
        }
    }
}
