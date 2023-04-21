package com.angel.architecture.event.actor;

/**
 * <p>Abstracci�n que me puede informar, como un string, qui�n es aquel que
 * produjo un evento determinado del sistema. Sirve como punto de flexibilidad y
 * para facilitar el testeo.</p>
 *
 * @user Juan Isern
 */
public interface CurrentActorResolver {

    public String getCurrentActor();
}
