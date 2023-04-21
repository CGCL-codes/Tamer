package atunit.core;

import java.lang.annotation.Annotation;

@SuppressWarnings("serial")
public class IllegalAnnotationException extends Exception {

    public IllegalAnnotationException(Class<? extends Annotation> annotation, String reason) {
        super("The annotation " + annotation + " cannot be used in this test: " + reason);
    }
}
