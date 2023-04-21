package la.data.provider;

import commons.provider.BaseProvider;
import commons.provider.ProviderException;
import java.io.File;
import la.data.KSA00Data;

/**
 * @since
 * @author Susanta Tewari
 */
public interface KSA00DataProvider extends BaseProvider<KSA00Data> {

    /**
     * Sets the data file for this provider.
     * @param dataFile the data file for this provider. Assumes the file exists
     * @throws ProviderException if the data file is invalid
     */
    void setDataFile(File dataFile) throws ProviderException;
}
