package eu.popeye.networkabstraction.communication.basic.util;

/**
 * Exception thrown when there is an attempt to create a workgroup with a
 * name of an existing workgroup.
 * @author Gerard Paris Aixala
 *
 */
public class WorkgroupAlreadyCreatedException extends PopeyeException {

    private static final long serialVersionUID = 7508012605358766266L;

    public WorkgroupAlreadyCreatedException(Throwable t) {
        super(t);
    }
}
