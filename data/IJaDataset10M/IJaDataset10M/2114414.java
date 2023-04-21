package org.apache.shindig.gadgets.preload;

import org.apache.shindig.gadgets.Gadget;
import java.util.Collection;
import java.util.concurrent.Callable;
import com.google.inject.ImplementedBy;

/**
 * Performs an individual preloading operation.
 */
@ImplementedBy(HttpPreloader.class)
public interface Preloader {

    /**
   * Create new preload tasks for the provided gadget.
   *
   * @param gadget The gadget that the operations will be performed for.
   * @return Preloading tasks that will be executed by
   *  {@link PreloaderService#}.
   */
    Collection<Callable<PreloadedData>> createPreloadTasks(Gadget gadget);
}
