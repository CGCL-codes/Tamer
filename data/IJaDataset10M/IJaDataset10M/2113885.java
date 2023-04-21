package rjws.objects.composite;

import rjws.objects.Tag;

/**
 * A Link
 * 
 * @author Thomas Rudin
 *
 */
public class A extends Tag {

    /**
	 * Constructs a Link with Reference
	 * @param href The Reference
	 */
    public A(String href) {
        super("A");
        this.setAttribute("href", href);
    }

    /**
	 * Constructs a link with reference and Text
	 * @param href
	 * @param desc
	 */
    public A(String href, String desc) {
        super("A");
        this.setAttribute("href", href);
        this.add(desc);
    }

    /**
	 * Sets the Target
	 * @param target the Target
	 */
    public A setTarget(String target) {
        this.setAttribute("target", target);
        return this;
    }
}
