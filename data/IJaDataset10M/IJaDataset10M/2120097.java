package org.pojonode.ant;

import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.pojonode.codegen.config.AlfrescoShareConfigGenerator;
import org.pojonode.codegen.config.AlfrescoWebClientConfigGenerator;
import org.pojonode.codegen.config.IAlfrescoConfigGenerator;
import org.pojonode.service.PojonodeNamespacePrefixResolver;

/**
 * @author Cosmin Marginean, 22/02/12
 */
public class GeneratShareConfigCustomTask extends ConfigGeneratorTask {

    @Override
    protected IAlfrescoConfigGenerator createConfigGenerator(PojonodeNamespacePrefixResolver namespacePrefixResolver) {
        return new AlfrescoShareConfigGenerator();
    }
}
