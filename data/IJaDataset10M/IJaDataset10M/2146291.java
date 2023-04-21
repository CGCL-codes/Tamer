package net.sf.jgamelibrary.options.option;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlAccessType;

/**
 * Base class for all options.
 * @author vlad
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Option {

    @XmlAttribute(required = true)
    private String name;

    protected Option() {
    }

    protected Option(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
	 * @return This option's value.
	 */
    public abstract Object getValue();
}
