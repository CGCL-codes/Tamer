package net.sf.remilama.creator;

import org.seasar.framework.container.creator.ComponentCreatorImpl;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

public class ConfigCreator extends ComponentCreatorImpl {

    private static final String NAME_SUFFIX = "Config";

    public ConfigCreator(NamingConvention namingConvention) {
        super(namingConvention);
        this.setNameSuffix(NAME_SUFFIX);
        this.setInstanceDef(InstanceDefFactory.SINGLETON);
        this.setExternalBinding(false);
        this.setEnableAbstract(false);
        this.setEnableInterface(false);
    }
}
