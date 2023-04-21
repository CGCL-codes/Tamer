package org.ops4j.peaberry.builders;

import org.ops4j.peaberry.Import;

/**
 * Provide runtime decoration/intercepting of imported services.
 * 
 * @author mcculls@gmail.com (Stuart McCulloch)
 */
public interface ImportDecorator<S> {

    /**
   * Decorate the given imported service.
   * 
   * @param service imported service handle
   * @return decorated service handle
   */
    <T extends S> Import<T> decorate(Import<T> service);
}
