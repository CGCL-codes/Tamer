package com.genia.toolbox.projects.tests.uml2model_test.plugin;

import com.genia.toolbox.spring.provider.plugin.impl.AbstractSimpleSpringProviderPlugin;

/**
 * this class declare hibernate configuration spring file.
 */
public class Uml2ModelSpringProviderPlugin extends AbstractSimpleSpringProviderPlugin {

    /**
   * the hibernate config file.
   */
    private static final String HIBERNATE_CONFIG_FILE_RESOURCE = "/com/genia/toolbox/projects/tests/uml2model_test/plugin/hibernate-spring-config.xml";

    /**
   * the main config file.
   */
    private static final String TEST_MAIN_DAO_CONFIG_FILE_RESOURCE = "/com/genia/toolbox/projects/tests/uml2model_test/plugin/test-main-dao-spring-config.xml";

    /**
   * constructors.
   */
    public Uml2ModelSpringProviderPlugin() {
        super(HIBERNATE_CONFIG_FILE_RESOURCE, TEST_MAIN_DAO_CONFIG_FILE_RESOURCE);
    }
}
