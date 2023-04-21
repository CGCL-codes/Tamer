package edu.upmc.opi.caBIG.caTIES.security;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.security.authorization.AuthorizationException;
import org.globus.security.authorization.ChainConfig;
import org.globus.security.gridmap.GridMap;
import org.globus.wsrf.config.ConfigException;
import org.globus.wsrf.impl.security.authorization.GridMapPDP;
import org.globus.wsrf.impl.security.descriptor.ContainerSecurityDescriptor;
import org.globus.wsrf.impl.security.util.AuthzUtil;
import org.globus.wsrf.security.SecureContainerConfig;
import org.globus.wsrf.security.authorization.PDPConstants;
import org.globus.security.authorization.InitializeException;

public class CaTIES_GridMapPDP extends GridMapPDP {

    private static final long serialVersionUID = -8062266140094275489L;

    private ChainConfig mChainConfig = null;

    private static Log logger = LogFactory.getLog(CaTIES_GridMapPDP.class.getName());

    public void initialize(String chainName, String prefix_, ChainConfig config) throws InitializeException {
        super.initialize(chainName, prefix_, config);
        this.mChainConfig = config;
    }

    protected GridMap getGridMap() throws AuthorizationException {
        String gridMapFilename = null;
        GridMap gridMap = (GridMap) this.mChainConfig.getProperty(this.prefix, PDPConstants.GRIDMAP_OBJECT);
        if (gridMap == null) {
            try {
                File currentWorkingDirectory = new File(".");
                String currentWorkingDirectoryPath = currentWorkingDirectory.getAbsolutePath();
                currentWorkingDirectoryPath = currentWorkingDirectoryPath.substring(0, currentWorkingDirectoryPath.lastIndexOf("bin" + File.separator + "."));
                currentWorkingDirectoryPath += "webapps" + File.separator;
                currentWorkingDirectoryPath += "wsrf" + File.separator;
                currentWorkingDirectoryPath += "WEB-INF" + File.separator;
                logger.debug("The current working directory is " + currentWorkingDirectoryPath);
                ContainerSecurityDescriptor desc = SecureContainerConfig.getSecurityDescriptor();
                gridMapFilename = (String) desc.getDefaultAuthzParamValue(this.prefix, PDPConstants.GRIDMAP_FILE);
                gridMapFilename = currentWorkingDirectoryPath + gridMapFilename;
                logger.debug("Container gridmap filename " + gridMapFilename);
                gridMapFilename = (String) this.mChainConfig.getProperty(this.prefix, PDPConstants.GRIDMAP_FILE);
                gridMapFilename = currentWorkingDirectoryPath + gridMapFilename;
                logger.debug("Service gridmap filename " + gridMapFilename);
                gridMap = AuthzUtil.getGridMap(gridMapFilename, true);
                this.mChainConfig.setProperty(this.prefix, PDPConstants.GRIDMAP_OBJECT, gridMap);
            } catch (ConfigException exp) {
                exp.printStackTrace();
            }
        }
        if (gridMap == null) {
            gridMap = super.getGridMap();
        }
        return gridMap;
    }
}
