package com.tirsen.hanoi.engine;

/**
 * Interface for plugging in loaders of process definitions from for example a java-package, a database,
 * or a version-control system.
 *
 * <!-- $Id: ProcessDefinitionLoader.java,v 1.2 2002/08/26 14:03:17 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&eacute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.2 $
 */
public interface ProcessDefinitionLoader {

    ProcessDefinition load(String definitionID);

    ProcessDefinition[] getDefinitions();
}
