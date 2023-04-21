package org.jaffa.modules.printing.components.printerdefinitionmaintenance.tx;

import org.apache.log4j.Logger;
import java.util.*;
import org.jaffa.persistence.UOW;
import org.jaffa.persistence.Criteria;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.ApplicationException;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.exceptions.DomainObjectNotFoundException;
import org.jaffa.exceptions.DuplicateKeyException;
import org.jaffa.exceptions.MultipleDomainObjectsFoundException;
import org.jaffa.exceptions.IncompleteKeySpecifiedException;
import org.jaffa.exceptions.RelatedDomainObjectFoundException;
import org.jaffa.datatypes.ValidationException;
import org.jaffa.exceptions.DomainObjectChangedException;
import org.jaffa.datatypes.DateTime;
import org.jaffa.datatypes.Formatter;
import org.jaffa.util.BeanHelper;
import org.jaffa.components.maint.MaintTx;
import org.jaffa.components.voucher.IVoucherGenerator;
import org.jaffa.components.voucher.VoucherGeneratorException;
import org.jaffa.datatypes.exceptions.InvalidForeignKeyException;
import org.jaffa.modules.printing.components.printerdefinitionmaintenance.IPrinterDefinitionMaintenance;
import org.jaffa.modules.printing.components.printerdefinitionmaintenance.dto.*;
import org.jaffa.modules.printing.domain.PrinterDefinition;
import org.jaffa.modules.printing.domain.PrinterDefinitionMeta;

/** Maintenance for PrinterDefinition objects.
*/
public class PrinterDefinitionMaintenanceTx extends MaintTx implements IPrinterDefinitionMaintenance {

    private static Logger log = Logger.getLogger(PrinterDefinitionMaintenanceTx.class);

    /**
     * This should be invoked, when done with the component.
     */
    public void destroy() {
    }

    /** This method is used to perform prevalidations before creating a new instance of PrinterDefinition.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public PrinterDefinitionMaintenancePrevalidateOutDto prevalidateCreate(PrinterDefinitionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) log.debug("Input: " + (input != null ? input.toString() : null));
            uow = new UOW();
            preprocess(uow, input);
            duplicateCheck(uow, input);
            validateForeignObjects(uow, input);
            PrinterDefinition domain = createDomain(uow, input, true);
            postCreate(uow, input, domain, true);
            PrinterDefinitionMaintenancePrevalidateOutDto output = createPrevalidateOutDto(uow, domain, input);
            if (log.isDebugEnabled()) log.debug("Output: " + (output != null ? output.toString() : null));
            return output;
        } catch (FrameworkException e) {
            if (e.getCause() != null && e.getCause() instanceof ApplicationExceptions) {
                throw (ApplicationExceptions) e.getCause();
            } else if (e.getCause() != null && e.getCause() instanceof ApplicationException) {
                ApplicationExceptions appExps = new ApplicationExceptions();
                appExps.add((ApplicationException) e.getCause());
                throw appExps;
            } else throw e;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Persists a new instance of PrinterDefinition.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public PrinterDefinitionMaintenanceRetrieveOutDto create(PrinterDefinitionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) log.debug("Input: " + (input != null ? input.toString() : null));
            uow = new UOW();
            preprocess(uow, input);
            duplicateCheck(uow, input);
            validateForeignObjects(uow, input);
            PrinterDefinition domain = createDomain(uow, input, false);
            uow.add(domain);
            postCreate(uow, input, domain, false);
            uow.commit();
            if (log.isDebugEnabled()) log.debug("Successfully created the domain object. Now retrieving the object details.");
            PrinterDefinitionMaintenanceRetrieveInDto retrieveInDto = new PrinterDefinitionMaintenanceRetrieveInDto();
            retrieveInDto.setHeaderDto(input.getHeaderDto());
            retrieveInDto.setPrinterId(input.getPrinterId());
            PrinterDefinitionMaintenanceRetrieveOutDto output = retrieve(retrieveInDto);
            return output;
        } catch (FrameworkException e) {
            if (e.getCause() != null && e.getCause() instanceof ApplicationExceptions) {
                throw (ApplicationExceptions) e.getCause();
            } else if (e.getCause() != null && e.getCause() instanceof ApplicationException) {
                ApplicationExceptions appExps = new ApplicationExceptions();
                appExps.add((ApplicationException) e.getCause());
                throw appExps;
            } else throw e;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Returns the details for PrinterDefinition.
     * @param input The criteria based on which an object will be retrieved.
     * @throws ApplicationExceptions This will be thrown if the criteria contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details. A null indicates, the object was not found.
     */
    public PrinterDefinitionMaintenanceRetrieveOutDto retrieve(PrinterDefinitionMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) log.debug("Input: " + (input != null ? input.toString() : null));
            uow = new UOW();
            preprocess(uow, input);
            PrinterDefinition domain = load(uow, input);
            PrinterDefinitionMaintenanceRetrieveOutDto output = buildRetrieveOutDto(uow, input, domain);
            if (log.isDebugEnabled()) log.debug("Output: " + (output != null ? output.toString() : null));
            return output;
        } catch (FrameworkException e) {
            if (e.getCause() != null && e.getCause() instanceof ApplicationExceptions) {
                throw (ApplicationExceptions) e.getCause();
            } else if (e.getCause() != null && e.getCause() instanceof ApplicationException) {
                ApplicationExceptions appExps = new ApplicationExceptions();
                appExps.add((ApplicationException) e.getCause());
                throw appExps;
            } else throw e;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** This method is used to perform prevalidations before updating an existing instance of PrinterDefinition.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public PrinterDefinitionMaintenancePrevalidateOutDto prevalidateUpdate(PrinterDefinitionMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) log.debug("Input: " + (input != null ? input.toString() : null));
            uow = new UOW();
            preprocess(uow, input);
            PrinterDefinition domain = load(uow, input);
            validateForeignObjects(uow, input);
            updateDomain(uow, input, domain, true);
            PrinterDefinitionMaintenancePrevalidateOutDto output = createPrevalidateOutDto(uow, domain, input);
            if (log.isDebugEnabled()) log.debug("Output: " + (output != null ? output.toString() : null));
            return output;
        } catch (FrameworkException e) {
            if (e.getCause() != null && e.getCause() instanceof ApplicationExceptions) {
                throw (ApplicationExceptions) e.getCause();
            } else if (e.getCause() != null && e.getCause() instanceof ApplicationException) {
                ApplicationExceptions appExps = new ApplicationExceptions();
                appExps.add((ApplicationException) e.getCause());
                throw appExps;
            } else throw e;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Updates an existing instance of PrinterDefinition.
     * @param input The new values for the domain object.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     * @return The object details.
     */
    public PrinterDefinitionMaintenanceRetrieveOutDto update(PrinterDefinitionMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            if (log.isDebugEnabled()) log.debug("Input: " + (input != null ? input.toString() : null));
            uow = new UOW();
            preprocess(uow, input);
            PrinterDefinition domain = load(uow, input);
            validateForeignObjects(uow, input);
            updateDomain(uow, input, domain, false);
            uow.update(domain);
            uow.commit();
            if (log.isDebugEnabled()) log.debug("Successfully updated the domain object. Now retrieving the object details.");
            PrinterDefinitionMaintenanceRetrieveInDto retrieveInDto = new PrinterDefinitionMaintenanceRetrieveInDto();
            retrieveInDto.setHeaderDto(input.getHeaderDto());
            retrieveInDto.setPrinterId(input.getPrinterId());
            PrinterDefinitionMaintenanceRetrieveOutDto output = retrieve(retrieveInDto);
            return output;
        } catch (FrameworkException e) {
            if (e.getCause() != null && e.getCause() instanceof ApplicationExceptions) {
                throw (ApplicationExceptions) e.getCause();
            } else if (e.getCause() != null && e.getCause() instanceof ApplicationException) {
                ApplicationExceptions appExps = new ApplicationExceptions();
                appExps.add((ApplicationException) e.getCause());
                throw appExps;
            } else throw e;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Deletes an existing instance of PrinterDefinition.
     * @param input The key values for the domain object to be deleted.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     */
    public void delete(PrinterDefinitionMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions {
        UOW uow = null;
        try {
            uow = new UOW();
            delete(input, uow);
            uow.commit();
            if (log.isDebugEnabled()) log.debug("Successfully deleted the domain object");
        } catch (FrameworkException e) {
            if (e.getCause() != null && e.getCause() instanceof ApplicationExceptions) {
                throw (ApplicationExceptions) e.getCause();
            } else if (e.getCause() != null && e.getCause() instanceof ApplicationException) {
                ApplicationExceptions appExps = new ApplicationExceptions();
                appExps.add((ApplicationException) e.getCause());
                throw appExps;
            } else throw e;
        } finally {
            if (uow != null) uow.rollback();
        }
    }

    /** Deletes an existing instance of PrinterDefinition.
     * @param input The key values for the domain object to be deleted.
     * @param uow The delete will be performed using the input UOW.
     * @throws ApplicationExceptions This will be thrown if the input contains invalid data.
     * @throws FrameworkException Indicates some system error.
     */
    public void delete(PrinterDefinitionMaintenanceDeleteInDto input, UOW uow) throws FrameworkException, ApplicationExceptions {
        if (log.isDebugEnabled()) log.debug("Input: " + (input != null ? input.toString() : null));
        preprocess(uow, input);
        PrinterDefinition domain = load(uow, input);
        deleteDomain(uow, input, domain);
        if (log.isDebugEnabled()) log.debug("The domain object has been marked for deletion. It will be deleted when the UOW is committed");
    }

    /** Preprocess the input for the create method. */
    private void preprocess(UOW uow, PrinterDefinitionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Ensure that a duplicate record is not created. */
    private void duplicateCheck(UOW uow, PrinterDefinitionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        if (input.getPrinterId() == null) return;
        Criteria criteria = new Criteria();
        criteria.setTable(PrinterDefinitionMeta.getName());
        criteria.addCriteria(PrinterDefinitionMeta.PRINTER_ID, input.getPrinterId());
        Collection col = uow.query(criteria);
        if (col != null && !col.isEmpty()) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DuplicateKeyException(PrinterDefinitionMeta.getLabelToken()));
            throw appExps;
        }
    }

    /** Create the domain object. */
    private PrinterDefinition createDomain(UOW uow, PrinterDefinitionMaintenanceCreateInDto input, boolean fromPrevalidate) throws FrameworkException, ApplicationExceptions {
        PrinterDefinition domain = new PrinterDefinition();
        ApplicationExceptions appExps = null;
        try {
            domain.updatePrinterId(input.getPrinterId());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateDescription(input.getDescription());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateSiteCode(input.getSiteCode());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateLocationCode(input.getLocationCode());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateRemote(input.getRemote());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateRealPrinterName(input.getRealPrinterName());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updatePrinterOption1(input.getPrinterOption1());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updatePrinterOption2(input.getPrinterOption2());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateOutputType(input.getOutputType());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        if (appExps != null && appExps.size() > 0) throw appExps;
        return domain;
    }

    /** This method is invoked after the domain object has been created.*/
    private void postCreate(UOW uow, PrinterDefinitionMaintenanceCreateInDto input, PrinterDefinition domain, boolean fromPrevalidate) throws FrameworkException, ApplicationExceptions {
    }

    /** Preprocess the input for the retrieve method. */
    private void preprocess(UOW uow, PrinterDefinitionMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Retrieve the domain object. */
    private PrinterDefinition load(UOW uow, PrinterDefinitionMaintenanceRetrieveInDto input) throws FrameworkException, ApplicationExceptions {
        PrinterDefinition domain = null;
        Criteria criteria = new Criteria();
        criteria.setTable(PrinterDefinitionMeta.getName());
        criteria.addCriteria(PrinterDefinitionMeta.PRINTER_ID, input.getPrinterId());
        Iterator itr = uow.query(criteria).iterator();
        if (itr.hasNext()) domain = (PrinterDefinition) itr.next();
        if (domain == null) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DomainObjectNotFoundException(PrinterDefinitionMeta.getLabelToken()));
            throw appExps;
        }
        return domain;
    }

    /** Create the RetrieveOutDto. */
    private PrinterDefinitionMaintenanceRetrieveOutDto buildRetrieveOutDto(UOW uow, PrinterDefinitionMaintenanceRetrieveInDto input, PrinterDefinition domain) throws FrameworkException, ApplicationExceptions {
        PrinterDefinitionMaintenanceRetrieveOutDto output = new PrinterDefinitionMaintenanceRetrieveOutDto();
        output.setPrinterId(domain.getPrinterId());
        output.setDescription(domain.getDescription());
        output.setSiteCode(domain.getSiteCode());
        output.setLocationCode(domain.getLocationCode());
        output.setRemote(domain.getRemote());
        output.setRealPrinterName(domain.getRealPrinterName());
        output.setPrinterOption1(domain.getPrinterOption1());
        output.setPrinterOption2(domain.getPrinterOption2());
        output.setOutputType(domain.getOutputType());
        addForeignObjectsToRetrieveOut(uow, domain, output);
        addRelatedDtosToRetrieveOut(uow, domain, output);
        return output;
    }

    /** Preprocess the input for the update method. */
    private void preprocess(UOW uow, PrinterDefinitionMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Retrieve the domain object. */
    private PrinterDefinition load(UOW uow, PrinterDefinitionMaintenanceUpdateInDto input) throws FrameworkException, ApplicationExceptions {
        PrinterDefinition domain = null;
        Criteria criteria = new Criteria();
        criteria.setTable(PrinterDefinitionMeta.getName());
        criteria.addCriteria(PrinterDefinitionMeta.PRINTER_ID, input.getPrinterId());
        criteria.setLocking(Criteria.LOCKING_PARANOID);
        Iterator itr = uow.query(criteria).iterator();
        if (itr.hasNext()) domain = (PrinterDefinition) itr.next();
        if (domain == null) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DomainObjectNotFoundException(PrinterDefinitionMeta.getLabelToken()));
            throw appExps;
        }
        return domain;
    }

    /** Update the domain object and add it to the UOW. */
    private void updateDomain(UOW uow, PrinterDefinitionMaintenanceUpdateInDto input, PrinterDefinition domain, boolean fromPrevalidate) throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = null;
        try {
            domain.updateDescription(input.getDescription());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateSiteCode(input.getSiteCode());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateLocationCode(input.getLocationCode());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateRemote(input.getRemote());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateRealPrinterName(input.getRealPrinterName());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updatePrinterOption1(input.getPrinterOption1());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updatePrinterOption2(input.getPrinterOption2());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        try {
            domain.updateOutputType(input.getOutputType());
        } catch (ValidationException e) {
            if (appExps == null) appExps = new ApplicationExceptions();
            appExps.add(e);
        }
        if (appExps != null && appExps.size() > 0) throw appExps;
    }

    /** Preprocess the input for the delete method. */
    private void preprocess(UOW uow, PrinterDefinitionMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions {
    }

    /** Retrieve the domain object. */
    private PrinterDefinition load(UOW uow, PrinterDefinitionMaintenanceDeleteInDto input) throws FrameworkException, ApplicationExceptions {
        PrinterDefinition domain = null;
        Criteria criteria = new Criteria();
        criteria.setTable(PrinterDefinitionMeta.getName());
        criteria.addCriteria(PrinterDefinitionMeta.PRINTER_ID, input.getPrinterId());
        criteria.setLocking(Criteria.LOCKING_PARANOID);
        Iterator itr = uow.query(criteria).iterator();
        if (itr.hasNext()) domain = (PrinterDefinition) itr.next();
        if (domain == null) {
            ApplicationExceptions appExps = new ApplicationExceptions();
            appExps.add(new DomainObjectNotFoundException(PrinterDefinitionMeta.getLabelToken()));
            throw appExps;
        }
        return domain;
    }

    /** Delete the domain object from the domain. */
    private void deleteDomain(UOW uow, PrinterDefinitionMaintenanceDeleteInDto input, PrinterDefinition domain) throws FrameworkException, ApplicationExceptions {
        deleteRelatedObjects(uow, input, domain);
        uow.delete(domain);
    }

    /** Add foreign objects to PrinterDefinitionMaintenanceRetrieveOutDto */
    private void addForeignObjectsToRetrieveOut(UOW uow, PrinterDefinition domain, PrinterDefinitionMaintenanceRetrieveOutDto output) throws FrameworkException, ApplicationExceptions {
    }

    /** This will validate the Foreign Objects. */
    private void validateForeignObjects(UOW uow, PrinterDefinitionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = null;
        if (appExps != null) throw appExps;
    }

    private PrinterDefinitionMaintenancePrevalidateOutDto createPrevalidateOutDto(UOW uow, PrinterDefinition domain, PrinterDefinitionMaintenanceCreateInDto input) throws FrameworkException, ApplicationExceptions {
        PrinterDefinitionMaintenancePrevalidateOutDto output = new PrinterDefinitionMaintenancePrevalidateOutDto();
        output.setPrinterId(domain.getPrinterId());
        output.setDescription(domain.getDescription());
        output.setSiteCode(domain.getSiteCode());
        output.setLocationCode(domain.getLocationCode());
        output.setRemote(domain.getRemote());
        output.setRealPrinterName(domain.getRealPrinterName());
        output.setPrinterOption1(domain.getPrinterOption1());
        output.setPrinterOption2(domain.getPrinterOption2());
        output.setOutputType(domain.getOutputType());
        addForeignObjectsToRetrieveOut(uow, domain, output);
        addRelatedDtosToRetrieveOut(uow, domain, output);
        return output;
    }

    /** Add related objects to PrinterDefinitionMaintenanceRetrieveOutDto */
    private void addRelatedDtosToRetrieveOut(UOW uow, PrinterDefinition printerDefinition, PrinterDefinitionMaintenanceRetrieveOutDto output) throws FrameworkException, ApplicationExceptions {
    }

    /** Delete the related domain objects if the 'Cascading' constraint is specified. Throw an exception in case 'Restricted' constraint is specified. */
    private void deleteRelatedObjects(UOW uow, PrinterDefinitionMaintenanceDeleteInDto input, PrinterDefinition printerDefinition) throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = null;
        if (appExps != null) throw appExps;
    }
}
