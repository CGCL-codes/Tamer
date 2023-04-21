package net.sf.brico.cmd.support.spring;

import net.sf.ezmorph.MorphUtils;
import net.sf.ezmorph.Morpher;
import net.sf.ezmorph.MorpherRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class MorpherRegistryFactory implements FactoryBean, InitializingBean {

    private MorpherRegistry morpherRegistry;

    private Morpher[] morphers;

    public void afterPropertiesSet() throws Exception {
        morpherRegistry = new MorpherRegistry();
        MorphUtils.registerStandardMorphers(morpherRegistry);
        if (morphers != null) {
            for (int i = 0; i < morphers.length; i++) {
                morpherRegistry.registerMorpher(morphers[i]);
            }
        }
    }

    public Object getObject() throws Exception {
        return morpherRegistry;
    }

    public Class getObjectType() {
        return MorpherRegistry.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setMorphers(Morpher[] morphers) {
        this.morphers = morphers;
    }
}
