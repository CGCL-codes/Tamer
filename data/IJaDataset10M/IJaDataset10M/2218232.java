package net.sf.hsdd.conf;

import net.sf.hsdd.res.MessageResources;

/**
 *
 * @author Alexey.Kurgan
 */
public class MINetProxyNoProxyFor extends AbstractPropertyMetaInfo {

    private static final String defaultValue = "127.0.0.1;";

    public MINetProxyNoProxyFor(PropertyMetaInfo parent) {
        super(parent);
    }

    public String getPropertyName() {
        return "net.proxy.no-proxy-for";
    }

    public Class<?> getPropertyClass() {
        return String.class;
    }

    public String getPropertyDesc() {
        return MessageResources.getMessage("prmt_desc-net.proxy.no-proxy-for");
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void validate(Object obj) throws IllegalArgumentException {
    }
}
