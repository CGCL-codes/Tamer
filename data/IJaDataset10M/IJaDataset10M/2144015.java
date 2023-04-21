package org.isurf.spmiddleware.management.jmx;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.isurf.spmiddleware.dao.ReaderProfileDAO;
import org.isurf.spmiddleware.management.IdentityReaderProfileManager;
import org.isurf.spmiddleware.management.ReaderProfileManager;
import org.isurf.spmiddleware.reader.Reader;
import org.isurf.spmiddleware.reader.ReaderProfile;
import org.isurf.spmiddleware.reader.identity.BusinessTransactionType;
import org.isurf.spmiddleware.reader.identity.IdentityReaderProfile;
import org.isurf.spmiddleware.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * Manages identity readers in the SPMiddleware.
 */
@ManagedResource(objectName = "SPMiddleware Management:name=Identity Reader Manager", description = "Management console for administration of Identity Readers.", log = false)
public class IdentityReaderProfileManagerImpl extends ReaderProfileManager implements IdentityReaderProfileManager {

    private Logger logger = Logger.getLogger(IdentityReaderProfileManagerImpl.class);

    /**
	 * Constructs a IdentityReaderProfileManagerImpl.
	 *
	 * @param readerProfileDAO
	 *            The {@link ReaderProfileDAO}
	 */
    @Autowired
    public IdentityReaderProfileManagerImpl(ReaderProfileDAO readerProfileDAO) {
        super(readerProfileDAO);
    }

    @ManagedOperation(description = "Adds a new Identity Reader to the Smart Product Middleware", currencyTimeLimit = 25)
    @ManagedOperationParameters({ @ManagedOperationParameter(name = "name", description = "The logical reader name"), @ManagedOperationParameter(name = "readerClass", description = "The fully qualified class name of the Reader"), @ManagedOperationParameter(name = "metadata", description = "Optional metadata"), @ManagedOperationParameter(name = "userName", description = "The user name of the Reader, if supported."), @ManagedOperationParameter(name = "password", description = "The password of the Reader, if supported."), @ManagedOperationParameter(name = "ipAddress", description = "The IP address of the Reader, if supported."), @ManagedOperationParameter(name = "port", description = "The network port of the Reader, if supported.") })
    public void addIdentityReader(String name, String readerClass, String metadata, String userName, String password, String ipAddress, int port) throws ClassNotFoundException {
        try {
            logger.debug("addIdentitylReader: name = " + name + "; readerClass = " + readerClass);
            ValidationUtil.validateNotNull(name, "name");
            ValidationUtil.validateNotNull(readerClass, "readerClass");
            ValidationUtil.validateClass(readerClass, Reader.class);
            logger.debug("addIdentitylReader: validated input...");
            IdentityReaderProfile profile = new IdentityReaderProfile();
            profile.setName(name);
            profile.setReaderClass(Class.forName(readerClass));
            profile.setMetadata(metadata);
            profile.setUserName(userName);
            profile.setPassword(password);
            profile.setIpAddress(ipAddress);
            profile.setPort(port);
            profile.setState(ReaderProfile.State.ACTIVE);
            readerProfileDAO.saveOrUpdate(profile);
            logger.debug("addIdentitylReader: saved profile = " + profile);
        } catch (Exception e) {
            logger.error("addIdentitylReader: error occured adding reader", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @ManagedOperation(description = "Gets the logical names of all Identity Readers", currencyTimeLimit = 25)
    public Collection<String> getLogicalReaders() {
        Collection<String> readers = readerProfileDAO.findReaderNames(IdentityReaderProfile.class);
        logger.debug("getLogicalReaders: readers = " + readers);
        return readers;
    }

    @ManagedOperation(description = "Associates a business transaction type (sales, deliveries etc.) with an Identity Reader", currencyTimeLimit = 25)
    @ManagedOperationParameters({ @ManagedOperationParameter(name = "logicalReader", description = "The logical reader name"), @ManagedOperationParameter(name = "businessTransactionType", description = "The business transaction type, e.g deliveries") })
    public void addBusinessTransactionTypes(String logicalReader, String businessTransactionType) {
        ReaderProfile profile = readerProfileDAO.findByName(logicalReader);
        if (profile instanceof IdentityReaderProfile) {
            IdentityReaderProfile epcReaderProfile = (IdentityReaderProfile) profile;
            BusinessTransactionType type = new BusinessTransactionType();
            type.setBusinessTransactionType(businessTransactionType);
            if (epcReaderProfile.getBusinessTransactionTypes() != null && !epcReaderProfile.getBusinessTransactionTypes().contains(type)) {
                epcReaderProfile.getBusinessTransactionTypes().add(type);
                readerProfileDAO.saveOrUpdate(epcReaderProfile);
            }
        }
    }

    @ManagedOperation(description = "Disassociates a business transaction type (sales, deliveries etc.) from an Identity Reader", currencyTimeLimit = 25)
    @ManagedOperationParameters({ @ManagedOperationParameter(name = "logicalReader", description = "The logical reader name"), @ManagedOperationParameter(name = "businessTransactionType", description = "The business transaction type, e.g deliveries") })
    public void removeBusinessTransactionType(String logicalReader, String businessTransactionType) {
        ReaderProfile profile = readerProfileDAO.findByName(logicalReader);
        if (profile instanceof IdentityReaderProfile) {
            IdentityReaderProfile epcReaderProfile = (IdentityReaderProfile) profile;
            BusinessTransactionType type = new BusinessTransactionType();
            type.setBusinessTransactionType(businessTransactionType);
            if (epcReaderProfile.getBusinessTransactionTypes() != null && epcReaderProfile.getBusinessTransactionTypes().contains(type)) {
                epcReaderProfile.getBusinessTransactionTypes().remove(type);
                readerProfileDAO.saveOrUpdate(epcReaderProfile);
            }
        }
    }
}
