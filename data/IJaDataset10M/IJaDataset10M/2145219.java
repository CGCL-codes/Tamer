package net.sf.hsdd.conf;

import net.sf.hsdd.res.MessageResources;

/**
 *
 * @author Alexey.Kurgan
 */
public class MINetRestartChunkAfter extends AbstractPropertyMetaInfo {

    private static final Integer defaultValue = Integer.valueOf(60);

    public MINetRestartChunkAfter(PropertyMetaInfo parent) {
        super(parent);
    }

    public String getPropertyName() {
        return "net.restart-chunk-after";
    }

    public Class<?> getPropertyClass() {
        return Integer.class;
    }

    public String getPropertyDesc() {
        return MessageResources.getMessage("prmt_desc-net.restart-chunk-after");
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void validate(Object obj) throws IllegalArgumentException {
        if (obj == null) return;
        if (obj instanceof Number) {
            int num = ((Number) obj).intValue();
            if (num >= 5) return;
        }
        throw new IllegalArgumentException(MessageResources.getMessage("err-wait-restart-chunk-after-must-be-number"));
    }
}
