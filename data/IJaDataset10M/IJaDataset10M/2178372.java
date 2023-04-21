package org.elf.businesslayer;

import org.elf.businesslayer.kernel.services.lock.KernelLock;
import org.elf.datalayer.DLSession;

/**
 * Clase para gestionar los bloqueos entre Threads
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class Lock {

    KernelLock _kernelLock;

    Lock(KernelLock kernelLock) {
        if (kernelLock == null) {
            throw new RuntimeException("El par�metro 'kernelLock' no puede ser null");
        }
        _kernelLock = kernelLock;
    }

    /**
     * Realiza el bloqueo de un "objeto".<br>
     * Si el "objeto" no estaba bloqueado lo bloqueara y si lo estaba se esperar�
     * un tiempo y si pasado ese tiempo sigue bloqueado lanzar� una excepci�n.<br>
     * Los objeto no son nada "real" de Java
     * sino para que haya un espacio de nombres para que
     * no se solapen los nombre de los bloqueos se han
     * separado en tres nombres.<br>
     * Ej:<br>
     * Para bloquear una tabla se podr�a hacer lo siguiente:<br>
     * &nbsp;&nbsp;&nbsp;BLSession.getLock().lock("TABLE","MYTABLE","34",0);<br>
     * Por lo que la categor�a ser�a "TABLE", el tipo ser�a el nombre de la tabla a bloquear y
     * la instancia ser�a la clave primaria de la tabla.
     * @param category Categor�a del "objeto" a bloquear
     * @param type Tipo del objeto a bloquear
     * @param instance instancia del objeto a bloquear.
     * @throws LockException Si no se ha podido conseguir el bloqueo pq ya esta de antes,
     *  se lanzar� la excepci�n y no se habr� conseguido el bloqueo.
     */
    public void lock(String category, String type, String instance) throws LockException {
        _kernelLock.lock(category, type, instance, 0);
    }

    /**
     * Realiza el bloqueo de un "objeto".<br>
     * Si el "objeto" no estaba bloqueado lo bloqueara y si lo estaba se esperar�
     * un tiempo y si pasado ese tiempo sigue bloqueado lanzar� una excepci�n.<br>
     * Los objeto no son nada "real" de Java
     * sino para que haya un espacio de nombres para que
     * no se solapen los nombre de los bloqueos se han
     * separado en tres nombres.<br>
     * Ej:<br>
     * Para bloquear una tabla se podr�a hacer lo siguiente:<br>
     * &nbsp;&nbsp;&nbsp;BLSession.getLock().lock("TABLE","MYTABLE","34",0);<br>
     * Por lo que la categor�a ser�a "TABLE", el tipo ser�a el nombre de la tabla a bloquear y
     * la instancia ser�a la clave primaria de la tabla.
     * @param category Categor�a del "objeto" a bloquear
     * @param type Tipo del objeto a bloquear
     * @param instance instancia del objeto a bloquear.
     * @param timeout Cuanto tiempo esperar en milisegundos si ya est� bloqueado antes de lanzar la excepci�n.
     * sino se ha podido acceder al bloqueo.Si vale 0 no se esperar� nada.
     * @throws LockException Si no se ha podido conseguir el bloqueo pq ya esta de antes,
     *  se lanzar� la excepci�n y no se habr� conseguido el bloqueo.
     */
    public void lock(String category, String type, String instance, long timeout) throws LockException {
        _kernelLock.lock(category, type, instance, timeout);
    }

    /**
     * Libera el bloqueo de un "objeto".<br>
     * Si el "objeto" no estaba bloqueado o no es la misma sesi�n que lo bloqueo, lanzar� una excepci�n.<br>
     * Los objeto no son nada "real" de Java
     * sino para que haya un espacio de nombres para que
     * no se solapen los nombre de los bloqueos se han
     * separado en tres nombres.<br>
     * Ej:<br>
     * Para bloquear una tabla se podr�a hacer lo siguiente:<br>
     * &nbsp;&nbsp;&nbsp;BLSession.getLock().unlock("TABLE","MYTABLE","34");<br>
     * Por lo que la categor�a ser�a "TABLE", el tipo ser�a el nombre de la tabla a bloquear y
     * la instancia ser�a la clave primaria de la tabla.
     * @param category Categor�a del "objeto" a bloquear
     * @param type Tipo del objeto a bloquear
     * @param instance instancia del objeto a bloquear.
     * @throws LockException Si no se ha podido desbloquear pq 
     * no lo hab�a bloqueado este usuario en esta sesi�n.
     */
    public void unlock(String category, String type, String instance) throws LockException {
        _kernelLock.unlock(category, type, instance);
    }

    /**
     * Elimina TODOS los bloqueos que hay en el sistema, pero permite filtrando seg�n distintos par�metros.<br><br>
     * Si se llama de la siguiente forma se borrar�n todos los bloqueos:<br>
     * {@code BLSession.getLock().unlockFiltering(false,false,0)}<br>
     * Para borrar solo los que llevan m�s de 10 segundos se usar�:<br>
     * {@code BLSession.getLock().unlockFiltering(false,false,10000)}<br>
     * Para borrar los creados por el usuario actual:<br>
     * {@code BLSession.getLock().unlockFiltering(false,true,0)}<br>
     * Para borrar los creados por esta sesi�n de la DataLayer:<br>
     * {@code BLSession.getLock().unlockFiltering(true,false,0)}<br>
     * @param filterByCurrentSystem Si vale true se borran solo los bloqueos creados
     * por esta instancia de la DataLayer.Si vale false no se filtra por este valor
     * @param filterByCurrentSession Si vale true se borran solo los bloqueos creados
     * por la sesi�n actual del usuario de la DataLayer.Si vale false no se filtra por este valor
     * @param filterByLockDuration Si es distinto de 0 borra todos aquellos objetos que llevan,
     *  m�s de 'filterByLockDuration' misilegundos bloqueados.Si vale 0 no se filtrar� por este valor. 
     */
    public void unlockFiltering(boolean filterByCurrentSystem, boolean filterByCurrentSession, int filterByLockDuration) {
        String dlsystemUID;
        String dlsessionUID;
        if (filterByCurrentSystem == true) {
            dlsystemUID = DLSession.getDataLayerUID();
        } else {
            dlsystemUID = null;
        }
        if (filterByCurrentSession == true) {
            dlsessionUID = DLSession.getUID();
        } else {
            dlsessionUID = null;
        }
        _kernelLock.unlockFiltering(dlsystemUID, dlsessionUID, filterByLockDuration);
    }
}
