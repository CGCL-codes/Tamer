package org.opcda2out;

import java.util.Map;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;

/**
 * Retrieves data from an OPC server
 * 
 * @author Joao Leal
 */
public interface OPCDataProvider {

    public Group getGroup();

    public Item[] getItems();

    /**
     * Initializes this OPC data provider
     *
     * @param samplingRate The output writer sampling rate (in milliseconds)
     * @throws java.lang.Exception
     */
    public void initialize() throws Exception;

    /**
     * Gets the current OPC items' data
     *
     * @return The current OPC items' data
     * @throws java.lang.Exception
     */
    public Map<String, ItemState> getOPCData() throws Exception;

    /**
     * Stops the data retrieval from the OPC server
     */
    public void stop();
}
