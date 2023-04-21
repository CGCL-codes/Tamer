package net.sf.csutils.groovy.policy;

import groovy.lang.Closure;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.RegistryObject;
import net.sf.csutils.groovy.QueryEngine;
import com.softwareag.centrasite.policy.model.GroovyEntity;

/**
 * Helper class for use within an action template.
 */
public class ActionTemplates {

    public static void run(Closure pClosure, GroovyEntity pEntity) throws JAXRException {
        final RegistryObject ro = GroovyEntityMetaClass.getRegistryObject(pEntity);
        QueryEngine.run(pClosure, ro);
    }
}
