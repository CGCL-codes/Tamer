package net.beasainwireless.guregipuzkoa.api.xml;

import java.net.URL;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "rsp")
public class GGLicenses {

    @XmlElementWrapper(name = "licenses")
    @XmlElement(name = "license")
    public List<GGLicense> licenses;

    public static class GGLicense {

        @XmlAttribute
        public String id, name;

        @XmlAttribute
        public URL url;
    }
}
