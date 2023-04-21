package javax.management.j2ee.statistics;

/**
 * Specifies the statistics provided by session beans of both stateful and
 * stateless types.
 */
public interface SessionBeanStats extends EJBStats {

    /**
	 * Number of beans in the method-ready state.
	 */
    RangeStatistic getMethodReadyCount();
}
