package com.windsor.node.plugin;

import java.io.File;
import java.util.List;
import com.windsor.node.common.domain.DataFlow;
import com.windsor.node.common.domain.NodeTransaction;
import com.windsor.node.common.domain.NodeVisit;
import com.windsor.node.common.domain.ProcessContentResult;

public interface PluginHelper {

    /**
     * Saves plugin content to the flow plugin dir.
     * 
     * @param flowName
     * @param content
     */
    void savePluginContent(DataFlow flow, NodeVisit visit, byte[] content);

    /**
     * Returns the directory containing the plugin .jar file associated with the
     * given DataFlow.
     * 
     * @param flow
     * @return
     */
    File getPluginContentDir(DataFlow flow);

    /**
     * Processes Submits and Notifies
     */
    ProcessContentResult processTransaction(NodeTransaction transaction);

    /**
     * Gets instance of the plugin for info only; does not configure the plugin.
     * 
     * @param service
     * @return
     */
    BaseWnosPlugin getWnosPlugin(DataFlow flow, String implementingClassName);

    /**
     * Gets List of names of concrete implementations of BaseWnosPlugin
     * associated with the given DataFlow.
     * 
     * @param flow
     * @return
     */
    List<String> getWnosPluginImplementors(DataFlow flow);

    WnosClassLoader getClassLoader();
}
