package org.openacs;

import javax.ejb.EJBLocalObject;

/**
 *
 * @author Administrator
 */
public interface ScriptLocal extends EJBLocalObject {

    String getName();

    byte[] getScript();

    void setScript(byte[] script);

    String getDescription();

    void setDescription(String description);
}
