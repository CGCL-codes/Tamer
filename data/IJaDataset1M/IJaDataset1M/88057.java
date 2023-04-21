package net.sourceforge.ondex.parser.kegg2.data;

import java.io.Serializable;
import com.sleepycat.persist.model.Persistent;

/**
 * @author taubertj
 *
 */
@Persistent
public class Subtype implements Serializable {

    /**
	 * Default serial version unique id
	 */
    private static final long serialVersionUID = 1L;

    private String name;

    private String value;

    public Subtype(String name, String value) {
        if (name == null) throw new NullPointerException("Name is null");
        if (value == null) throw new NullPointerException("Value is null");
        this.name = name;
        this.value = value.intern();
    }

    @SuppressWarnings("unused")
    private Subtype() {
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
