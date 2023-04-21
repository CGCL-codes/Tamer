package org.libreplan.business.resources.entities;

import java.util.List;
import org.hibernate.validator.AssertTrue;
import org.libreplan.business.common.Registry;

/**
 * This class models a VirtualWorker.
 *
 * @author Lorenzo Tilve Álvaro <ltilve@igalia.com>
 * @author Fernando Bellas Permuy <fbellas@udc.es>
 */
public class VirtualWorker extends Worker {

    public static VirtualWorker create() {
        VirtualWorker virtualWorker = new VirtualWorker();
        virtualWorker.setNewObject(true);
        virtualWorker.setNif("(Virtual)");
        virtualWorker.setSurname("---");
        virtualWorker.getCalendar();
        return create(virtualWorker);
    }

    public static VirtualWorker create(String code) {
        VirtualWorker virtualWorker = new VirtualWorker();
        virtualWorker.setNewObject(true);
        virtualWorker.setNif("(Virtual)");
        virtualWorker.setSurname("---");
        virtualWorker.getCalendar();
        return create(virtualWorker, code);
    }

    private String observations;

    /**
     * Constructor for hibernate. Do not use!
     */
    public VirtualWorker() {
    }

    @Override
    public String getDescription() {
        return getFirstName();
    }

    @Override
    public String getShortDescription() {
        return getFirstName() + " " + getNif();
    }

    public String getName() {
        return getFirstName();
    }

    @Override
    public boolean isVirtual() {
        return true;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    @AssertTrue
    @Override
    public boolean checkConstraintUniqueFiscalCode() {
        return true;
    }

    @AssertTrue(message = "Virtual worker group name must be unique")
    public boolean checkConstraintUniqueVirtualGroupName() {
        List<Worker> list = Registry.getWorkerDAO().findByFirstNameAnotherTransactionCaseInsensitive(this.getFirstName());
        if ((isNewObject() && list.isEmpty()) || list.isEmpty()) {
            return true;
        } else {
            return list.get(0).getId().equals(getId());
        }
    }

    @Override
    public Boolean isLimitingResource() {
        return false;
    }

    @Override
    public String getHumanId() {
        return getFirstName();
    }
}
