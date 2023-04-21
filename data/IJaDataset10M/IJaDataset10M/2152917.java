package org.kablink.teaming.rest.v1.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jong
 *
 */
@XmlRootElement(name = "fileVersions")
public class FileVersionPropertiesCollection {

    @XmlElement(name = "fileVersion")
    private List<FileVersionProperties> list;

    private FileVersionPropertiesCollection() {
    }

    public FileVersionPropertiesCollection(List<FileVersionProperties> list) {
        this.list = list;
    }

    public List<FileVersionProperties> asList() {
        return list;
    }
}
