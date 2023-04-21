package org.gridtrust.UcsService.AttributeManager.DataType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * implements the Reputation of the user
 * @author maurizio colombo
 *
 */
public class Reputation_Data extends GenericAttributeData {

    private int data;

    static final Log logger = LogFactory.getLog(Reputation_Data.class);

    public Reputation_Data() {
    }

    public void setData(Object data) {
        logger.info("DATA RECEIVED " + data);
        try {
            float aux = (Float) data;
            aux = aux * 100;
            Float f = new Float(aux);
            if (this.getTimeStamp() == null) this.setTimeStamp();
            if (f.intValue() != this.data) {
                this.setTimeStamp();
                this.data = f.intValue();
            }
        } catch (Exception e) {
            logger.error("some ERROR in reputation data format: " + e.getMessage());
            this.setTimeStamp();
            this.data = 0;
        }
    }

    public boolean isgreater(String value) {
        int aux = new Integer(value).intValue();
        return (this.data >= aux);
    }

    public Object getData() {
        return this.data;
    }
}
