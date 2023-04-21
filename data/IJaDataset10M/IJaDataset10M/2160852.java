package org.easygen.core.config;

import org.easygen.core.generators.Generator;
import org.easygen.core.generators.common.CommonGenerator;

/**
 * @author eveno
 * Created on 20 d�c. 06
 *
 */
public class CommonConfig extends ModuleConfig {

    public static final String NATURE = "common";

    /**
	 * 
	 */
    public CommonConfig() {
        super();
        setNature(NATURE);
    }

    /**
	 * @see org.easygen.core.config.ModuleConfig#getGeneratorClass()
	 */
    @Override
    public Generator getGenerator() {
        return new CommonGenerator();
    }
}
