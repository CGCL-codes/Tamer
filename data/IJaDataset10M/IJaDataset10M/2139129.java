package javacream.xml.xsd;

/**
 * Declaration
 * 
 * @author Glenn Powell
 *
 */
public abstract class Declaration extends Reference {

    private String name = null;

    private Annotation annotation = null;

    public Declaration(String name) {
        super(null);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Annotation getAnnotation() {
        return annotation;
    }
}
