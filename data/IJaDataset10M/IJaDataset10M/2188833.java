package net.sourceforge.javautil.common.exception;

/**
 * The base for most thread level managers.
 *
 * @author elponderador
 * @author $Author: ponderator $
 * @version $Id: ThrowableManagerThreadLevelAbstract.java 1536 2009-12-03 22:51:08Z ponderator $
 */
public abstract class ThrowableManagerThreadLevelAbstract extends ThrowableManagerAbstract {

    /**
	 * Since this is a thread level manager, this will be thread safe.
	 */
    protected RuntimeException lastException;

    public RuntimeException getLastException() {
        return lastException;
    }

    public RuntimeException setLastException(RuntimeException lastException) {
        this.lastException = lastException;
        return this.lastException;
    }
}
