package edu.harvard.fas.rbrady.tpteam.tpbridge.model;

import java.io.Serializable;
import java.util.Hashtable;
import org.osgi.service.event.Event;

/*******************************************************************************
* File 			:  	TPEvent.java
* 
* Description 	: 	A serializable object that contains information about
* 					TPTeam CRUD and execution events
* 
* @author Bob Brady, rpbrady@gmail.com
* @version $Revision: 367 $
* @date $Date: 2007-06-16 16:20:45 -0400 (Sat, 16 Jun 2007) $ Copyright (c) 2007 Bob Brady
******************************************************************************/
public class TPEvent implements Serializable {

    private static final long serialVersionUID = 7318549536622346381L;

    public static final String PROJECT_KEY = "PROJECT";

    public static final String PROJECT_ID_KEY = "PROJ_ID";

    public static final String PROJ_PROD_XML_KEY = "PROD_PROJ_XML";

    public static final String TEST_XML_KEY = "TEST_XML";

    public static final String TEST_TREE_XML_KEY = "TEST_TREE_XML";

    public static final String TEST_EXEC_XML_KEY = "TEST_EXEC_XML";

    public static final String TEST_PROP_XML_KEY = "TEST_PROP_XML";

    public static final String CHART_DATASET_XML_KEY = "CHART_DATASET_XML_KEY";

    public static final String VERDICT_KEY = "VERDICT";

    public static final String TEST_NAME_KEY = "TEST_NAME";

    public static final String TEST_DESC_KEY = "TEST_DESC";

    public static final String TIMESTAMP_KEY = "TIMESTAMP";

    public static final String COMMENTS_KEY = "COMMENTS";

    /** The ID from the TPTeam Database */
    public static final String ID_KEY = "ID";

    /** The ID of the parent entity from the TPTeam database */
    public static final String PARENT_ID_KEY = "PARENT_ID";

    public static final String ECFID_KEY = "ECFID";

    public static final String STATUS_KEY = "STATUS";

    /** The ECF ID of the intended recipient */
    public static final String SEND_TO = "SEND_TO";

    /** The ECF ID of the sender */
    public static final String FROM = "FROM";

    /** The table of key=value pairs */
    private Hashtable<String, String> mDictionary;

    /** The topic of the event */
    private String mTopic;

    /**
	 * Constructor
	 * @param event The corresponding OSGi Event
	 */
    public TPEvent(Event event) {
        mDictionary = new Hashtable<String, String>();
        for (String propName : event.getPropertyNames()) mDictionary.put(propName, (String) event.getProperty(propName));
        mTopic = event.getTopic();
    }

    /**
	 * Constructor 
	 * @param topic the topic of the TPTeam event
	 * @param dictionary map of key=value pairs
	 */
    public TPEvent(String topic, Hashtable<String, String> dictionary) {
        mDictionary = dictionary;
        mTopic = topic;
    }

    public void setTopic(String topic) {
        mTopic = topic;
    }

    public String getTopic() {
        return mTopic;
    }

    public Hashtable<String, String> getDictionary() {
        return mDictionary;
    }

    public String getProject() {
        return (String) mDictionary.get(PROJECT_KEY);
    }

    public String getTestName() {
        return (String) mDictionary.get(TEST_NAME_KEY);
    }

    public String getID() {
        return (String) mDictionary.get(ID_KEY);
    }

    public String getStatus() {
        return (String) mDictionary.get(STATUS_KEY);
    }

    public void setStatus(String status) {
        mDictionary.put(STATUS_KEY, status);
    }
}
