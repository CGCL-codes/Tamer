package orbe.model.element;

/**
 * Flags appliable for elements.
 * 
 * @author Damien Coraboeuf
 * @version $Id: ElementFlag.java,v 1.1 2006/11/17 16:56:00 guinnessman Exp $
 */
public interface ElementFlag {

    /**
	 * Edited element
	 */
    int FLAG_EDITED = 0;

    /**
	 * Created element
	 */
    int FLAG_NEW = 1;
}
