package burster.settings.model;

import javax.xml.bind.annotation.XmlElement;

public class Email {

    @XmlElement(name = "subject")
    public String subject;

    @XmlElement(name = "text")
    public String text;
}
