package fr.gfi.foundation.core.email;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

/**
 * AttachmentInfo.
 * 
 * @author Tiago Fernandez
 * @since 0.1
 */
public class AttachmentInfo {

    private static Log logger = LogFactory.getLog(AttachmentInfo.class);

    private String name;

    private URL path;

    private byte[] body;

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the path
	 */
    public URL getPath() {
        return path;
    }

    /**
	 * @param url the url path to set
	 */
    public void setPath(URL url) {
        if (url != null) {
            this.path = url;
        }
    }

    /**
	 * @param file the file path to set
	 */
    public void setPath(File file) {
        if (file != null) {
            URL url = toURL(file);
            setPath(url);
        }
    }

    /**
	 * @return the body copy
	 */
    public byte[] getBody() {
        byte[] bodyCopy = null;
        if (body != null) {
            bodyCopy = new byte[body.length];
            System.arraycopy(body, 0, bodyCopy, 0, body.length);
        }
        return bodyCopy;
    }

    /**
	 * @param body the body to set
	 */
    public void setBody(byte[] body) {
        if (body != null) {
            this.body = new byte[body.length];
            System.arraycopy(body, 0, this.body, 0, body.length);
        }
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("name", getName()).append("path", getPath()).toString();
    }

    /**
	 * @see java.lang.Object#equals(Object)
	 */
    @Override
    public boolean equals(Object other) {
        boolean equals = false;
        if (other instanceof AttachmentInfo) {
            AttachmentInfo castOther = (AttachmentInfo) other;
            equals = new EqualsBuilder().append(this.getName(), castOther.getName()).append(this.getPath(), castOther.getPath()).isEquals();
        }
        return equals;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).append(getPath()).toHashCode();
    }

    private URL toURL(File file) {
        URL url = null;
        try {
            Assert.notNull(file, "File must not be null");
            url = file.toURI().toURL();
        } catch (MalformedURLException ex) {
            logger.error("Error while generating URL from file: " + file.getPath(), ex);
        }
        return url;
    }
}
