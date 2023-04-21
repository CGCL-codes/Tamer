package net.sf.opentranquera.spring.mf;

import net.sf.opentranquera.integration.mf.format.MessageFormatter;
import org.springframework.beans.factory.FactoryBean;

/**
 * 
 *
 * <br>Created Oct 4, 2006
 * @author Diego Campodonico (zz2x4b)
 */
public class MessageFormatterFactoryBean implements FactoryBean {

    private String fileName;

    public Object getObject() throws Exception {
        return MessageFormatter.getInstance(this.fileName);
    }

    public Class getObjectType() {
        return MessageFormatter.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
