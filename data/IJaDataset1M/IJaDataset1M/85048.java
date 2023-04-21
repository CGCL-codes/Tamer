package edu.gatech.cc.jcrasher;

import static edu.gatech.cc.jcrasher.Assertions.check;
import static edu.gatech.cc.jcrasher.Assertions.notNull;
import java.util.HashSet;
import java.util.Set;
import edu.gatech.cc.jcrasher.types.TypeGraph;
import edu.gatech.cc.jcrasher.types.TypeGraphImpl;

/**
 * Crawls classes to be tested. This populates our type graph.
 * Then uses the planner to pick and generate test cases.
 * 
 * @author csallner@gatech.edu (Christoph Csallner)
 */
public abstract class AbstractCrasher implements Crasher {

    /**
	 * Database holding the relation needed for planning how to obtain
	 * an object via combinations of functions in type-space
	 */
    protected final TypeGraph typeGraph = TypeGraphImpl.instance();

    /**
	 * Classes to crash.
	 */
    protected Class<?>[] classes;

    /**
	 * Constructor
	 */
    protected AbstractCrasher(Class<?>[] classes) {
        this.classes = notNull(classes);
        check(classes.length > 0);
        final Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> c : classes) {
            classSet.add(c);
        }
        typeGraph.crawl(classSet, Constants.VIS_USED);
    }
}
