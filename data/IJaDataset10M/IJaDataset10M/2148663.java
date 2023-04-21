package ru.cos.sim.visualizer.frame;

/**
 * Representation of the double-linked list's element.
 * Main goal of this class - containing information
 * about objects, that are on screen. The first element
 * in the list - is head element. Head element can be indicated
 * by <code>isHead</code> variable.
 * 
 * @author Dudinov Ivan
 *
 */
public class ViewableObjectInformation {

    /**
	 * ID of the element
	 */
    public Integer uid;

    public LinkFrameData linkData;

    /**
	 * Is element visible
	 */
    public boolean visible = false;

    /**
	 * Reference to the next element
	 */
    public ViewableObjectInformation next;

    /**
	 * Reference to the previous element
	 */
    public ViewableObjectInformation previous;

    /**
	 * Indicates whether element - Head element
	 */
    public boolean isHead = false;

    /**
	 * Simple constructor. 
	 * @param uid - id of the element
	 */
    public ViewableObjectInformation(int uid) {
        super();
        this.uid = new Integer(uid);
    }
}
