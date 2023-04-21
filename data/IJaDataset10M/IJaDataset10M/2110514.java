package de.folt.rpc.messages;

import java.util.Hashtable;
import java.util.Vector;
import com.araya.eaglememex.util.EMXProperties;
import com.araya.tmx.Interface;
import de.folt.rpc.services.RPCMessage;
import de.folt.util.OpenTMSProperties;

/**
 * @author Klemens Waldh�r
 * 
 */
public class DeleteOpenTMSDataSource implements RPCMessage {

    /** DeleteOpenTMSDataSource deletes an OpenTMS Datasource. Parameters are provided through a hash table. The hashtable contains the following keys:<p>
     * dataSourceName - the name of the data source<br>
     * dataSourceType - the type of the data source; any defined database, e.g. MySQl<br>
     * dataSourceServer - the name of the server, e.g. localhost or IP address<br>
     * dataSourcePort - the port of the data source, e.g. 1433<br>
     * dataSourceUser - the user of the data source, e.g. sa<br>
     * dataSourcePassword - the name of the data source, e.g. folt<br>
     * dataModel  - either TMX or TBX depending on the data source<p>
     * It returns:<p>
     * vec.add(de.folt.constants.OpenTMSConstants.OpenTMS_ID_SUCCESS +"");<br>
     * vec.add(dataSourceName + " successfully deleted!");<p>
     * or<p>
     *  vec.add(de.folt.constants.OpenTMSConstants.OpenTMS_ID_FAILURE +"");<br>
     *  vec.add(ex.getMessage());<br>
     * @see de.folt.rpc.services.RPCMessage#execute(java.util.Hashtable)
     */
    @SuppressWarnings("unchecked")
    public Vector execute(Hashtable message) {
        Vector vec = new Vector();
        try {
            String dataSourceName = (String) message.get("dataSourceName");
            String dataSourceType = (String) message.get("dataSourceType");
            String dataSourceServer = (String) message.get("dataSourceServer");
            String dataSourcePort = (String) message.get("dataSourcePort");
            String dataSourceUser = (String) message.get("dataSourceUser");
            @SuppressWarnings("unused") String dataSourcePassword = (String) message.get("dataSourcePassword");
            String dataModel = (String) message.get("dataModel");
            if ((dataModel == null) || dataModel.equalsIgnoreCase("")) dataModel = "TMX"; else if (dataModel.equalsIgnoreCase("TMX")) ; else if (dataModel.equalsIgnoreCase("TBX")) ; else dataModel = "TMX";
            @SuppressWarnings("unused") Interface inter = new Interface();
            System.out.println("dataSourceName=    \"" + dataSourceName + "\"");
            System.out.println("dataSourceUser=    \"" + dataSourceUser + "\"");
            System.out.println("dataSourceType=    \"" + dataSourceType + "\"");
            System.out.println("dataSourceServer=  \"" + dataSourceServer + "\"");
            System.out.println("dataSourcePort=    \"" + dataSourcePort + "\"");
            System.out.println("dataModel=         \"" + dataModel + "\"");
            String propFile = OpenTMSProperties.getInstance().getOpenTMSProperty("ArayaPropertiesFile");
            EMXProperties.getInstance(propFile);
            @SuppressWarnings("unused") String filePath = EMXProperties.getInstance().getEMXProperty("database.path");
            String listLocation = EMXProperties.getInstance().getEMXProperty("database.list");
            System.out.println("listLocation       \"" + listLocation + "\"");
            System.out.println("ArayaPropertiesFile=\"" + propFile + "\"");
            @SuppressWarnings("unused") Vector retVec = com.araya.OpenTMS.Interface.runDeleteDB(message);
            System.out.println("DeleteOpenTMSDataSource " + dataSourceName + " finished!");
            vec.add(de.folt.constants.OpenTMSConstants.OpenTMS_ID_SUCCESS + "");
            vec.add(dataSourceName + " successfully deleted!");
            return vec;
        } catch (Exception ex) {
            vec.add(de.folt.constants.OpenTMSConstants.OpenTMS_ID_FAILURE + "");
            vec.add(ex.getMessage());
            ex.printStackTrace();
            return vec;
        }
    }
}
