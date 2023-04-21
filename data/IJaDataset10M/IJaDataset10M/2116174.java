package net.solarnetwork.node.settings;

/**
 * An individual setting value.
 * 
 * @author matt
 * @version $Revision: 2073 $
 */
public class SettingValueBean {

    private String providerKey;

    private String instanceKey;

    private String key;

    private String value;

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getInstanceKey() {
        return instanceKey;
    }

    public void setInstanceKey(String instanceKey) {
        this.instanceKey = instanceKey;
    }
}
