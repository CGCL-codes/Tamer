package com.ail.core.configure.server;

import javax.ejb.EJBLocalObject;
import com.ail.core.VersionEffectiveDate;
import javax.ejb.EJBException;
import com.ail.core.Version;
import java.lang.String;
import com.ail.core.configure.server.GetNamespacesArg;
import com.ail.core.configure.server.GetConfigurationArg;
import com.ail.core.configure.server.SetConfigurationArg;
import com.ail.core.configure.server.GetCommandScriptArg;
import com.ail.core.configure.server.SetCommandScriptArg;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.finder.GetClassListArg;
import org.w3c.dom.Element;

public interface ServerLocal extends EJBLocalObject {

    VersionEffectiveDate getVersionEffectiveDate() throws EJBException;

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    void resetCoreConfiguration() throws EJBException;

    void resetNamedConfiguration(String name) throws EJBException;

    void clearConfigurationCache() throws EJBException;

    Version getVersion() throws EJBException;

    String invokeServiceXML(String xml) throws EJBException;

    Element[] invokeServiceSoap(Element[] xml) throws EJBException;

    GetNamespacesArg getNamespaces(GetNamespacesArg arg) throws EJBException;

    GetConfigurationArg getConfiguration(GetConfigurationArg arg) throws EJBException;

    SetConfigurationArg setConfiguration(SetConfigurationArg arg) throws EJBException;

    GetCommandScriptArg getCommandScript(GetCommandScriptArg arg) throws EJBException;

    SetCommandScriptArg setCommandScript(SetCommandScriptArg arg) throws EJBException;

    GetClassListArg getClassList(GetClassListArg arg) throws EJBException;

    DeployCarArg deployCar(DeployCarArg arg) throws EJBException;

    PackageCarArg packageCar(PackageCarArg arg) throws EJBException;

    CatalogCarArg catalogCar(CatalogCarArg arg) throws EJBException;
}
