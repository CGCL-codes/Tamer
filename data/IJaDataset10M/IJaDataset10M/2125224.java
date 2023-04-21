package org.escapek.gui.core.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.escapek.core.tools.StringUtil;
import org.escapek.domain.registry.RegistryNode;
import org.escapek.domain.registry.RegistryNodeData;
import org.escapek.server.core.services.IRepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tools that deals with registry management
 * @author nicolasjouanin
 *
 */
public class RegistryTools {

    private static Logger logger = LoggerFactory.getLogger(RegistryTools.class);

    public static RegistryNodeData addRegistryNodeData(RegistryNode folder, RegistryNodeData data) throws SecurityException {
        RegistryNode reg = null;
        IRepositoryService service = (IRepositoryService) ServiceUtils.getService(IRepositoryService.ID);
        Assert.isNotNull(service);
        return service.addRegistryNodeData(folder.getId(), data);
    }

    /**
	 * Creates a Registry node and adds it to the registry
	 * @param service the service to use to create the value
	 * @param folder parent folder in the registry
	 * @param name new registry node name
	 * @param shortDesc registry node short description
	 * @param longDesc registry node long description
	 * @return the registry node created
	 */
    public static RegistryNode addRegistryNode(RegistryNode folder, String name, String desc) throws SecurityException {
        RegistryNode reg = null;
        IRepositoryService service = (IRepositoryService) ServiceUtils.getService(IRepositoryService.ID);
        Assert.isNotNull(service);
        RegistryNode newNode = new RegistryNode();
        newNode.setKeyName(name);
        newNode.setDescription(desc);
        newNode.setParentNode(folder);
        return service.createRegistryNode(newNode);
    }

    /**
	 * Gets a registry node by Id 
	 * @param Id Id to look for
	 * @return the registry value found, or null.
	 */
    public static RegistryNode getRegistryNodeById(String Id) {
        RegistryNode reg = null;
        IRepositoryService service = (IRepositoryService) ServiceUtils.getService(IRepositoryService.ID);
        Assert.isNotNull(service);
        reg = service.getRegistryNodeById(Id);
        return reg;
    }

    /**
	 * Gets a registry node by path 
	 * @param path path to look for
	 * @return the registry folder found, or null.
	 */
    public static RegistryNode getRegistryNodeByPath(String path) {
        RegistryNode reg = null;
        IRepositoryService service = (IRepositoryService) ServiceUtils.getService(IRepositoryService.ID);
        Assert.isNotNull(service);
        reg = service.getRegistryNodeByPath(path);
        return reg;
    }

    /**
	 * Loads CSV file into a registry folder 
	 * @param folder Registry folder to load properties to
	 * @param fileName file to read properties from
	 * For each line if the given file , this methods creates a registryNode object in the given registry folder.
	 * Expected format are:
	 *  path;desc;value -> creates a registryNodeValue into an existing folder
	 *  path;value -> creates a registryNode into an existing folder
	 */
    public static void loadToRegistry(IProgressMonitor monitor, String taskName, RegistryNode folder, String fileName) throws SecurityException {
        RegistryNode baseFolder = folder;
        String name, desc, val;
        int i, nbLines = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            while (in.readLine() != null) nbLines++;
            monitor.beginTask(taskName, nbLines);
            in.close();
            in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            i = 0;
            String line = in.readLine();
            while (line != null) {
                if (line.startsWith("#") || line.startsWith(" ") || line.equals("")) {
                    line = in.readLine();
                    continue;
                }
                String vals[] = line.split(";");
                if (vals.length < 2) {
                    line = in.readLine();
                    continue;
                }
                if (vals.length == 2) {
                    name = vals[0];
                    desc = vals[1];
                    String fullPath = StringUtil.getFullPath(baseFolder.getPath(), name);
                    RegistryNode f = getRegistryNodeByPath(fullPath);
                    if (f == null) {
                        RegistryNode parent = getRegistryNodeByPath(StringUtil.dirName(fullPath));
                        if (parent == null) {
                            logger.error("parent folder '{}' not found for '{}' =>SKIPPING", parent, name);
                        } else addRegistryNode(parent, StringUtil.baseName(name), desc);
                    }
                } else {
                    String fullPath, dirName;
                    RegistryNode parent;
                    name = vals[0];
                    desc = vals[1];
                    val = vals[2];
                    fullPath = StringUtil.getFullPath(baseFolder.getPath(), name);
                    dirName = StringUtil.dirName(fullPath);
                    if (!dirName.equals(baseFolder.getPath())) {
                        parent = getRegistryNodeByPath(StringUtil.dirName(fullPath));
                        if (parent == null) parent = baseFolder;
                    } else parent = baseFolder;
                    RegistryNode newReg = addRegistryNode(parent, StringUtil.baseName(name), desc);
                    RegistryNodeData data = new RegistryNodeData();
                    data.setValue(val);
                    addRegistryNodeData(newReg, data);
                }
                if (i % 10 == 0) monitor.worked(10);
                i++;
                line = in.readLine();
            }
        } catch (ClassCastException cce) {
            logger.error("Unexpected data type", cce);
        } catch (FileNotFoundException e) {
            logger.error("file '{}' not found", fileName, e);
        } catch (IOException e) {
            logger.error("Error while reading '{}", fileName, e);
        } finally {
            monitor.done();
        }
    }
}
