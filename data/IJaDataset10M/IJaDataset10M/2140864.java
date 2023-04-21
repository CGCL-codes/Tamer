package org.xmi.infoset;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * a element extension, contains the model-extension content of the xmi-extension
 */
public interface ExtensionElement extends Serializable {

    /**
	 * get the name of the ModelElement
	 * @return
	 */
    public abstract String getName();

    /**
	 * set The name of the ModelElement
	 * @param name
	 */
    public abstract void setName(String name);

    /**
	 * get the elements namespace
	 * @return
	 */
    public abstract String getNamespace();

    /**
	 * set the elements namespace
	 * @param namespace
	 */
    public abstract void setNamespace(String namespace);

    /**
	 * get the values
	 * @return
	 */
    public abstract Map<String, List<String>> getValues();

    /**
	 * set the values
	 * @param values
	 */
    public abstract void setValues(Map<String, List<String>> values);

    /**
	 * add a single value
	 * @param key
	 * @param value
	 */
    public abstract void addValue(String key, List<String> value);

    /**
	 * get a single value
	 * @param key
	 * @return
	 */
    public abstract List<String> getValue(String key);

    /**
	 * set the children
	 * @param children
	 */
    public abstract void setChildren(List<ExtensionElement> children);

    /**
	 * add a single child
	 * @param value
	 */
    public abstract void addChild(ExtensionElement value);

    /**
	 * get all children
	 * @return
	 */
    public abstract List<ExtensionElement> getChildren();
}
