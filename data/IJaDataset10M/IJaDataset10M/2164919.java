package com.abiquo.framework.services;

import com.abiquo.framework.domain.InstanceName;
import com.abiquo.framework.xml.events.messages.Data;

/**
 * The Interface provides the methods needed to work with FileSystemResources to
 * success retrieve and deploy files remotly
 * 
 * TODO: throws exceptions on fail
 */
public interface IFileSystemService {

    /**
	 * Deploy the content of the file located on filePath to the middleware named nodeName
	 * 
	 * @param nodeName
	 *            the node name where to put the file
	 * @param filePath
	 *            the path to local file to deploy on nodeName
	 * 
	 * @return the data reference associated to deployed data 
	 */
    InstanceName deploy(String nodeName, String filePath);

    /**
	 * Gets the status of the data
	 * 
	 * @param inData
	 *            the data reference want to obtain its status
	 * 
	 * @return the status: ready, blocked, exception
	 */
    String getStatus(InstanceName inData);

    /**
	 * Retrieve the Data associated to given data reference
	 * 
	 * @param inData
	 *            the data reference want to obtain
	 * 
	 * @return the data named inData
	 */
    Data retrieve(InstanceName inData);
}
