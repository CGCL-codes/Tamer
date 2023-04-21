package de.offis.semanticmm4u.user_profiles_connector.xml.repository;

import java.io.Reader;
import java.util.TreeMap;
import java.util.Vector;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotDeleteUserProfileException;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotStoreUserProfileException;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UUserProfileNotFoundException;
import de.offis.semanticmm4u.user_profiles_connector.xml.repository.upromRepositoryFailures.UProMStylesheetNotFoundException;

public abstract class IOToolkit {

    public abstract Reader loadProfile(String id, boolean idIsAbsolut) throws MM4UUserProfileNotFoundException;

    public abstract void saveProfile(String data, String id, boolean idIsAbsolut) throws MM4UCannotStoreUserProfileException;

    public abstract String getCoreModelID();

    public abstract String getExtensionModelID();

    public abstract Reader getXSLTStylesheet(String id) throws UProMStylesheetNotFoundException;

    public abstract void deleteProfile(String id) throws MM4UCannotDeleteUserProfileException;

    public abstract boolean idIsInUse(String id);

    public static final IOToolkit getFactory(IOToolkitLocator ioToolkitLocator) {
        return ioToolkitLocator.getIOToolkit();
    }

    public abstract void saveExtensionModel(String extensionModel);

    public abstract Vector getAllProfiles();

    public abstract TreeMap getTransformations();

    public abstract String getID(String id);
}
