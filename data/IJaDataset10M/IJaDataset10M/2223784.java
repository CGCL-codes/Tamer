package pl.edu.icm.pnpca.ca;

import java.io.IOException;
import pl.edu.icm.pnpca.storage.StorageException;

/**
 * An interface which allows to store or update certificate information.
 * This is used by CA only and shall not be exposed to clients.
 * @author Aleksander Nowinski
 */
public interface CertificateStorage {

    void storeCertificate(CertificateInfo ci) throws CAException, IOException, StorageException;
}
