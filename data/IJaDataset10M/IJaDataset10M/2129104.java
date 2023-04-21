package net.laubenberger.bogatyr.misc.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * XML entry representation for a map entry.
 *
 * @author Stefan Laubenberger
 * @author Roman Wuersch
 * @version 0.9.2 (20100519)
 * @since 0.9.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "id", "key", "value" })
@XmlRootElement(name = "entry")
public class XmlEntry {

    @XmlElement(name = "id")
    private String id;

    @XmlElement(name = "key", required = true)
    private final String key;

    @XmlElement(name = "value", required = true)
    private final String value;

    public XmlEntry() {
        super();
        key = null;
        value = null;
    }

    public XmlEntry(final String key, final String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public XmlEntry(final String id, final String key, final String value) {
        super();
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
