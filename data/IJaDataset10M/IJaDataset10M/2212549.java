package org.jquantlib.methods.finitedifferences;

import org.jquantlib.lang.exceptions.LibraryException;
import org.jquantlib.processes.GeneralizedBlackScholesProcess;

/**
 * @author Srinivas Hasti
 *
 */
public final class PdeTypeTokenUtil {

    public static <T extends Object> T getPdeInstance(final Class<T> clazz, final GeneralizedBlackScholesProcess process) {
        try {
            return clazz.getConstructor(process.getClass()).newInstance(process);
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
    }
}
