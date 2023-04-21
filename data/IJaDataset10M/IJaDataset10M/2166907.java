package de.janbusch.jhashpassword.xml.simple.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * <p>
 * Java class for Synchronization complex type.
 * 
 */
@Root(name = "Synchronization", strict = false)
public class Synchronization implements Serializable {

    /**
	 *  SerialVersionUID
	 */
    private static final long serialVersionUID = 31617185311440347L;

    @ElementList(entry = "Hosts", inline = true, required = false)
    private List<Host> hosts;

    public List<Host> getHosts() {
        if (hosts == null) {
            hosts = new ArrayList<Host>();
        }
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }
}
