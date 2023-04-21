package org.t2framework.lucy.config.meta;

import org.t2framework.commons.meta.Config;
import org.t2framework.commons.meta.MethodDesc;

public interface OrderedConfig extends Iterable<OrderedConfig.OrderedPack>, Config {

    void addConfig(MethodDesc md, Config config);

    MethodDesc getTargetMethodDesc();

    public class OrderedPack {

        public MethodDesc methodDesc;

        public Config config;
    }
}
