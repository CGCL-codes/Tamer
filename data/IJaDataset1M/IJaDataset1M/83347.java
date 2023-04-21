package com.ericsson.sip;

import java.io.Serializable;
import java.util.Map;
import javax.servlet.sip.SipURI;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * TODO Add comments (class description)  
 *  
 * 
 * @author ekrigro
 * @since 2005-apr-08
 *
 */
public class TimerContent implements Runnable, Serializable {

    /**
    * 
    */
    private static final long serialVersionUID = 1L;

    public TimerContent(SipURI key, Map target, String info) {
        _key = key;
        _target = target;
        _info = info;
    }

    private Logger logger = Logger.getLogger("CallSetup");

    private SipURI _key;

    private Map _target;

    private String _info;

    public SipURI getKey() {
        return _key;
    }

    public void setKey(SipURI key) {
        _key = key;
    }

    public Map getTarget() {
        return _target;
    }

    public void setTarget(Map target) {
        _target = target;
    }

    public String getInfo() {
        return _info;
    }

    public void setInfo(String info) {
        _info = info;
    }

    public void run() {
        logger.log(Level.INFO, "Timer timeout - removing key = " + _key + " info = " + _info);
        _target.remove(_key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Key=").append(_key).append(" : ");
        sb.append("Target=").append(_target).append(" : ");
        sb.append("Info=").append(_info);
        return sb.toString();
    }
}
