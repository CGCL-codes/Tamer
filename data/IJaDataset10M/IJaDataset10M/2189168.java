package org.nakedobjects.plugins.headless.applib;

/**
 * Implemented by all objects that have been viewed as per
 * {@link HeadlessViewer#view(Object)}.
 */
public interface ViewObject<T> {

    /**
	 * Programmatic equivalent of invoking save for a transient object (that is,
	 * like hitting the <i>save</i> button that the DnD viewer automatically
	 * renders.
	 */
    void save();

    /**
	 * Provide access to the underlying object.
	 * 
	 * <p>
	 * Used to unwrap objects used as arguments to actions (otherwise, end up
	 * creating a {@link NakedObjectSpecification} for the CGLib-enhanced class,
	 * not the original class).
	 */
    Object underlying();
}
