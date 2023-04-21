package it.infodea.tapestrydea.filter.demodea;

import it.infodea.tapestrydea.services.UserSecurityLevelProvider;
import it.infodea.tapestrydea.support.enumerations.SecurityGroup;
import java.util.Collections;
import java.util.Set;
import org.apache.tapestry5.ioc.ObjectCreator;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBuilderResources;
import org.apache.tapestry5.ioc.def.ServiceDef;

public class FakeServiceDef implements ServiceDef {

    public ObjectCreator createServiceCreator(ServiceBuilderResources resources) {
        return new ObjectCreator() {

            public Object createObject() {
                return new UserSecurityLevelProvider() {

                    public int provideUserSecurityLevel() {
                        SecurityGroup group = SecurityGroupForTesting.getGroup();
                        return group != null ? group.getLevel() : SecurityGroup.ANONYMOUS.getLevel();
                    }
                };
            }
        };
    }

    public Set<Class> getMarkers() {
        return Collections.emptySet();
    }

    public String getServiceId() {
        return "UserSecurityLevelProvider";
    }

    public Class getServiceInterface() {
        return UserSecurityLevelProvider.class;
    }

    public String getServiceScope() {
        return ScopeConstants.DEFAULT;
    }

    public boolean isEagerLoad() {
        return false;
    }
}

;
