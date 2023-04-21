package org.waveprotocol.wave.model.testing;

/**
 * Generic factory interface.  The intended use within this test package is
 * to allow black-box tests, which only test an interface, to be decoupled from
 * the construction of the particular instance of that interface to test.
 *
 * @param <T> type of created instances
 */
public interface Factory<T> {

    /**
   * Creates an instance.
   */
    T create();
}
