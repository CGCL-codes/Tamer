package com.ail.core.configure.server;

import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;

/**
 * Arg interface for the GetConfiguration entry point. The entry point takes one
 * argument: a namespace's name, and returns one result: the Configuration object
 * for the namespace. 
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/GetConfigurationArg.java,v $
 */
public interface GetConfigurationArg extends CommandArg {

    void setConfigurationRet(Configuration configurationRet);

    Configuration getConfigurationRet();

    void setNamespaceArg(String namespace);

    String getNamespaceArg();
}
