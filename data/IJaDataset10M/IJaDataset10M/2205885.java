package course;

import org.modelibra.persistency.PersistentDomain;

/**
 * Creates the persistent domain.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-12-03
 */
public class PersistentCourse extends PersistentDomain {

    /**
	 * Constructs the persistent domain.
	 */
    public PersistentCourse(Course course) {
        super(course);
    }
}
