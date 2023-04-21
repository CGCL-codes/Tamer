package ie.ucd.searchengine.javamodel;

import java.util.Vector;

/**
 * Abstract class for top-level Java elements
 * @author Brendan Maguire
 */
public abstract class JavaElement implements IJavaElement {

    private String name = "";

    private ITopLevelJavaElement parent = null;

    private Vector<JavaType> annotations = new Vector<JavaType>();

    private JavaComment comments = null;

    private JavaVisibility visibility = JavaVisibility.DEFAULT;

    /**
	 * Adds an annotations to the element
	 * @param annotation Annotation to be added to the element
	 */
    public void addAnnotation(JavaType annotation) {
        annotations.add(annotation);
    }

    /**
	 * Returns the annotations on the element
	 * @return Returns the annotations on the element
	 */
    public Vector<JavaType> getAnnotations() {
        return annotations;
    }

    /**
	 * Sets the annotations of the element
	 * @param annotations Annotations of the element
	 */
    public void setAnnotations(Vector<JavaType> annotations) {
        this.annotations = annotations;
    }

    /**
	 * Returns the comments of the element
	 * @return Returns the comments of the elements
	 */
    public JavaComment getComments() {
        return comments;
    }

    /**
	 * Sets the comments of the element
	 * @param comments Comments of the elements
	 */
    public void setComments(JavaComment comments) {
        this.comments = comments;
    }

    /**
	 * Gets the name of the element
	 * @return Returns the name of the element
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name of the element
	 * @param name Name of the element
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Returns the containing type of the element
	 * @return Returns the containing type of the element
	 */
    public ITopLevelJavaElement getParent() {
        return parent;
    }

    /**
	 * Sets the containing type of the element
	 * @param parent Containing type of the element
	 */
    public void setParent(ITopLevelJavaElement parent) {
        this.parent = parent;
    }

    /**
	 * Returns the visibility of the element
	 * @return Returns the visibility of the element
	 */
    public JavaVisibility getVisibility() {
        return visibility;
    }

    /**
	 * Sets the visibility of the element
	 * @param vis Visibility of the element
	 */
    public void setVisibility(JavaVisibility vis) {
        this.visibility = vis;
    }
}
