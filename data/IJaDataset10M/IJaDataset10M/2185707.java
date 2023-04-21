package edu.upmc.opi.caBIG.caTIES.creole;

import java.io.File;
import java.net.URL;
import edu.upmc.opi.caBIG.caTIES.client.config.PropertyDefaults;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Constants;
import edu.upmc.opi.caBIG.caTIES.server.dispatcher.mmtx.CaTIES_EvsMMTx;

public class CaTIES_EvsMMTxControllerCoderPR extends CaTIES_EvsMMTxCoderPR {

    /**
     * Method openMMTX.
     */
    protected void openMMTX() {
        try {
            String property = System.getProperty(CaTIES_Constants.PROPERTY_KEY_MMTX_CONFIG_FILE);
            if (property == null) property = PropertyDefaults.getDefaultForProperty(CaTIES_Constants.PROPERTY_KEY_MMTX_CONFIG_FILE);
            URL mmTxRegistryCfgURL = getClass().getResource(property);
            File mmTxRegistryCfgFile = new File(mmTxRegistryCfgURL.getPath());
            String mmTxRegisistryCfgFilePath = mmTxRegistryCfgFile.getAbsolutePath().replaceAll("%20", " ");
            this.evsMMTx = CaTIES_EvsMMTx.newInstance(mmTxRegisistryCfgFilePath);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
