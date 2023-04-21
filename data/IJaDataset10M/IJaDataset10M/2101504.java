package com.hitao.codegen.configs.systemconfig;

import com.hitao.codegen.configs.basic.AbstractSetConfig;

/***
 * The configuration set which can contains many child configuration.
 *
 * @author  zhangjun.ht
 * @created 2011-2-18
 * @version $Id: ConifgsConfigSet.java 12 2011-02-20 10:50:23Z guest $
 */
public class ConifgsConfigSet extends AbstractSetConfig<ConfigConfig> {

    private static final long serialVersionUID = -7487493349025252226L;

    public static final String MAIN_TAG = "configs";

    @Override
    public String getChildTag() {
        return ConfigConfig.MAIN_TAG;
    }
}
