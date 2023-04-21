package net.sf.hsdd.conf;

import net.sf.hsdd.res.MessageResources;

/**
 *
 * @author Alexey.Kurgan
 */
public class MINetProxyHTTPPort extends AbstractPropertyMetaInfo {

    public MINetProxyHTTPPort(PropertyMetaInfo parent) {
        super(parent);
    }

    public String getPropertyName() {
        return "net.proxy.http.port";
    }

    public Class<?> getPropertyClass() {
        return Integer.class;
    }

    public String getPropertyDesc() {
        return MessageResources.getMessage("prmt_desc-net.proxy.http.port");
    }

    public Object getDefaultValue() {
        return null;
    }

    public void validate(Object obj) throws IllegalArgumentException {
        if (obj == null) return;
        if (obj instanceof Number) {
            int port = ((Number) obj).intValue();
            if (port < 1 || port > 65535) throw new IllegalArgumentException(MessageResources.getMessage("err-port-must-in-the-range"));
            return;
        }
        throw new IllegalArgumentException(MessageResources.getMessage("err-port-must-be-number"));
    }
}
