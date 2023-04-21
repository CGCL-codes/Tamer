package org.universalmvc.config;

import java.util.List;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author vj
 *
 */
@XStreamAlias("model")
public class Model {

    private String name;

    @XStreamImplicit(itemFieldName = "field")
    private List<Field> fields;

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
	 * @return the fields
	 */
    public List<Field> getFields() {
        return fields;
    }

    /**
	 * @param fields the fields to set
	 */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
