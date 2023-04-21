package org.nexopenframework.samples.simple.management;

import java.io.Serializable;
import javax.management.MBeanInfo;
import javax.management.ObjectName;

/**
 * <p>simple using NexOpen</p>
 * 
 * <p>Holder of JMX information</p>
 * 
 * @author Bosco Curtu
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class MBeanData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**The JMX {@link ObjectName} related to given service*/
    private final ObjectName objectName;

    /**The JMX {@link MBeanInfo} related to given service*/
    private final MBeanInfo metaData;

    /**
	 * @param objectName
	 * @param metaData
	 */
    public MBeanData(final ObjectName objectName, final MBeanInfo metaData) {
        super();
        this.objectName = objectName;
        this.metaData = metaData;
    }

    public MBeanInfo getMetaData() {
        return metaData;
    }

    public ObjectName getObjectName() {
        return objectName;
    }

    /**
    * @return The canonical key properties string
    */
    public String getNameProperties() {
        return objectName.getCanonicalKeyPropertyListString();
    }

    /**
    * @return The MBeanInfo.getClassName() value
    */
    public String getClassName() {
        return metaData.getClassName();
    }
}
