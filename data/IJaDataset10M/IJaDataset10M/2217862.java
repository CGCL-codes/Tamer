package eu.more.jotau.objects.business;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import net.jxta.document.MimeMediaType;
import net.jxta.endpoint.MessageElement;

/**
 * @author seraphim
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetStatusRequest", propOrder = { "filename" })
public class GetStatusRequest {

    @XmlElement(name = "filename", required = true)
    private String filename;

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
