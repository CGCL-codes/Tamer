package org.monet.modelling.kernel.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "service-lock")
public class ServiceLockDeclaration extends WorklockDeclaration {

    public static class Use {

        @Attribute(name = "service-provider")
        protected String serviceProvider;

        public String getServiceProvider() {
            return serviceProvider;
        }
    }

    @Element(name = "use")
    protected Use use;

    public Use getUse() {
        return use;
    }
}
