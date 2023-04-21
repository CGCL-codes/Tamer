package genomemap.jobs.properties;

import commons.properties.PropertySet;
import commons.properties.PropertySetImpl;

/**
 * @since
 * @author Susanta Tewari
 */
public class GenomemapPropertySets {

    public static final PropertySet MODEL = new PropertySetImpl("model", "Model", "Model related inputs");

    public static final PropertySet DATA = new PropertySetImpl("data", "Data", "Data related inpus");

    public static final PropertySet COMPUTATIONS = new PropertySetImpl("computations", "Computations", "Pluggable computations");

    public static final PropertySet ALGORITHMS = new PropertySetImpl("algorithm", "Algorithm", "Pluggable algorithms");
}
