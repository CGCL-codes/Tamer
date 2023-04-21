package org.libreplan.business.resources.daos;

import java.util.Date;
import java.util.List;
import org.libreplan.business.common.daos.IIntegrationEntityDAO;
import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.business.resources.entities.Worker;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO interface for the <code>Worker</code> entity.
 *
 * @author Fernando Bellas Permuy <fbellas@udc.es>
 * @author Manuel Rego Casasnovas <mrego@igalia.com>
 * @author Diego Pino Garcia <dpino@igalia.com>
 *
 */
public interface IWorkerDAO extends IIntegrationEntityDAO<Worker> {

    /**
     * Returns workers which name/NIF partially matches with name
     *
     * @param name
     *            search worker by name(firstname or surname)/NIF
     *
     */
    List<Worker> findByNameSubpartOrNifCaseInsensitive(String name, boolean limitingResource);

    /**
     * Finds a {@link Worker} with the NIF param that should be unique.
     *
     * @param nif
     *            The NIF to search the {@link Worker}
     * @return The {@link Worker} with this NIF
     * @throws InstanceNotFoundException
     *             If there're more than one {@link Worker} with this NIF or
     *             there isn't any {@link Worker} with this NIF
     */
    @Transactional(readOnly = true)
    Worker findUniqueByNif(String nif) throws InstanceNotFoundException;

    /**
     * Return list of workers and virtual workers
     *
     * @return
     */
    List<Worker> getAll();

    /**
     * Return list of workers
     *
     * @return
     */
    @Transactional(readOnly = true)
    List<Worker> getWorkers();

    /**
     * Return list of workers with a particular firstName
     * @param name
     *            The string with the name searched
     * @return The list of {@link Worker} entities found
     */
    List<Worker> findByFirstNameCaseInsensitive(String name);

    /**
     * Return list of workers with a particular firstName when called from
     * inside an external transaction
     * @param name
     *            The string with the name searched
     * @return The list of {@link Worker} entities found
     */
    List<Worker> findByFirstNameAnotherTransactionCaseInsensitive(String name);

    /**
     * Return list of workers with a particular set of firstName, surname and
     * nif values
     * @param firstname
     *            String value for firstname
     * @param surname
     *            String value for surname
     * @param nif
     *            String value for nif
     * @return The list of {@link Worker} entities found
     */
    List<Worker> findByFirstNameSecondNameAndNif(String firstname, String surname, String nif);

    List<Worker> findByFirstNameSecondNameAndNifAnotherTransaction(String firstname, String surname, String nif);

    List<Object[]> getWorkingHoursGroupedPerWorker(List<String> workerNifs, Date startingDate, Date endingDate);

    Worker findByNifAnotherTransaction(String nif) throws InstanceNotFoundException;

    public List<Worker> findByFirstNameSecondName(String firstname, String secondname);

    public List<Worker> findByFirstNameSecondNameAnotherTransaction(String firstname, String secondname);
}
