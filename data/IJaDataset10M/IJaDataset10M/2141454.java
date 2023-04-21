package de.knowwe.kdom.defaultMarkup;

import java.util.regex.Pattern;
import de.knowwe.core.kdom.AbstractType;
import de.knowwe.core.kdom.rendering.Renderer;

public class AnnotationType extends AbstractType {

    private final DefaultMarkup.Annotation annotation;

    private static final String REGEX = "$LINESTART$\\s*(@$NAME$.*?)\\s*?(?=$LINESTART$\\s*@|^/?%|\\z)";

    private static final int FLAGS = Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL;

    public AnnotationType(DefaultMarkup.Annotation annotation) {
        this.annotation = annotation;
        this.setSectionFinder(new AdaptiveMarkupFinder(annotation.getName(), REGEX, FLAGS, 1));
        this.addChildType(new AnnotationNameType(annotation));
        this.addChildType(new AnnotationContentType(annotation));
        Renderer renderer = annotation.getRenderer();
        if (renderer != null) {
            this.setRenderer(renderer);
        }
    }

    /**
	 * Returns the name of the underlying annotation defined.
	 * 
	 * @return the annotation's name
	 */
    @Override
    public String getName() {
        return this.annotation.getName();
    }

    /**
	 * Returns the underlying annotation.
	 * 
	 * @return the underlying annotation
	 */
    public DefaultMarkup.Annotation getAnnotation() {
        return annotation;
    }
}
