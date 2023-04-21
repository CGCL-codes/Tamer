package net.sf.hsdd.conf;

import net.sf.hsdd.res.MessageResources;

/**
 *
 * @author Alexey.Kurgan
 */
public class MINetAdminTelnetPort extends AbstractPropertyMetaInfo {

    private static final Integer defaultValue = Integer.valueOf(7781);

    public MINetAdminTelnetPort(PropertyMetaInfo parent) {
        super(parent);
    }

    public String getPropertyName() {
        return "net.admin.telnet.port";
    }

    public Class<?> getPropertyClass() {
        return Integer.class;
    }

    public String getPropertyDesc() {
        return MessageResources.getMessage("prmt_desc-net.admin.telnet.port");
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void validate(Object obj) throws IllegalArgumentException {
        if (obj == null) return;
        if (obj instanceof Number) {
            int port = ((Number) obj).intValue();
            if (port >= 1 || port <= 65535) return;
        }
        throw new IllegalArgumentException(MessageResources.getMessage("err-port-must-be-number-in-range"));
    }
}
