package ontologizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author grossman
 *
 */
public class PopulationSet extends StudySet {

    /**
	 * Constructs the population set.
	 */
    public PopulationSet() {
        super();
    }

    /**
	 * constructs an empty PopulationSet with the given name
	 * 
	 * @param name the name of the PopulationSet to construct
	 */
    public PopulationSet(String name) {
        super();
        setName(name);
    }
}
