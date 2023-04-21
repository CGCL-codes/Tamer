package org.wings.conf;

import java.net.URL;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.wings.adapter.IntegrationAdapter;

/**
 * <code>Integration<code>.
 * <p/>
 * User: rrd
 * Date: 08.08.2007
 * Time: 08:52:06
 *
 * @author rrd
 * @version $Id
 */
@XmlRootElement(name = "integration")
@XmlAccessorType(XmlAccessType.NONE)
public class Integration {

    public static final String DEFAULT_PROTOCOL = "http";

    public static final String DEFAULT_SERVER = "localhost";

    public static final int DEFAULT_PORT = 80;

    @XmlAttribute(name = "adapter", required = true)
    @XmlJavaTypeAdapter(ClassAdapter.class)
    private Class<? extends IntegrationAdapter> adapter;

    public Class<? extends IntegrationAdapter> getAdapter() {
        return adapter;
    }

    public void setAdapter(Class<? extends IntegrationAdapter> adapter) {
        this.adapter = adapter;
    }

    @XmlElement(name = "base-url", required = true)
    @XmlJavaTypeAdapter(UrlAdapter.class)
    private URL baseUrl;

    /**
	 * @return the baseUrl
	 */
    public URL getBaseUrl() {
        return baseUrl;
    }

    /**
	 * @param baseUrl the baseUrl to set
	 */
    public void setBaseUrl(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    @XmlElement(name = "resource", required = true)
    private Resource resource;

    /**
	 * @return the resource
	 */
    public Resource getResource() {
        return resource;
    }

    /**
	 * @param resource the resource to set
	 */
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
