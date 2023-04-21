package net.sf.opentranquera.integration.mf.format.config;

import java.util.Map;

/**
 * @author Guillermo Meyer
 * @date 17/05/2005
 */
public class TargetField extends ArgumentalElement {

    private String id = null;

    private String source = null;

    private String type = null;

    private String defaultValue = null;

    /**
	 * @return Returns the type.
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type The type to set.
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return Returns the id.
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return Returns the source.
	 */
    public String getSource() {
        return source;
    }

    /**
	 * @param source The source to set.
	 */
    public void setSource(String source) {
        this.source = source;
    }

    /**
	 * Si tiene source property, toma del value object, sino del value.
	 * @param valueHolder
	 * @return
	 */
    public Object getTargetValue(Map valueHolder) {
        Object ret = this.getSource() != null ? valueHolder.get(this.getSource()) : null;
        ret = ret == null ? this.getDefaultValue() : ret;
        return ret;
    }

    /**
	 * @return Returns the defaultValue.
	 */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
	 * @param defaultValue The defaultValue to set.
	 */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setDefaultValueBody(String s) {
        if (s != null && !"".equals(s)) {
            this.setDefaultValue(s);
        }
    }
}
