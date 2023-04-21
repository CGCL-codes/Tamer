package org.comtor.imcomplete;

import com.sun.javadoc.*;

/**
 * The <code>LengthFeatureDoclet</code> class is a tool to
 * measure a simple feature: the average length of all comments
 * in a class source file.
 *
 * @author Michael Locasto
 */
public final class LengthFeatureDoclet {

    /**
    * Entry point.
    *
    * @param rootDoc  the root of the documentation tree
    * @returns some boolean value
    */
    public static boolean start(RootDoc rootDoc) {
        long commentLengthSum = 0L;
        long avgCommentLength = 0L;
        long commentLength = 0L;
        ClassDoc[] classes = rootDoc.classes();
        MethodDoc[] methods = new MethodDoc[0];
        for (int i = 0; i < classes.length; i++) {
            avgCommentLength = 0L;
            commentLengthSum = 0L;
            commentLength = 0L;
            methods = classes[i].methods();
            for (int j = 0; j < methods.length; j++) {
                commentLength = methods[j].getRawCommentText().length();
                commentLengthSum += commentLength;
                System.out.println("method: " + methods[j].name() + " (" + commentLength + " chars)");
            }
            if (0 != methods.length) avgCommentLength = (commentLengthSum / methods.length); else avgCommentLength = 0;
            System.out.println(classes[i] + ": " + avgCommentLength + " characters.");
        }
        return true;
    }
}
