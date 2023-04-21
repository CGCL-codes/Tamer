package com.monad.homerun.pkg.system;

import java.util.Properties;
import com.monad.homerun.core.SystemAgent;
import com.monad.homerun.core.SystemListener;
import com.monad.homerun.objmgt.RuntimeContext;
import com.monad.homerun.objmgt.impl.SimpleObject;

/**
 * AgentObjects represent agents that listen for, and
 * react to system events
 */
public class AgentObject extends SimpleObject implements SystemListener {

    SystemAgent agent = null;

    public AgentObject() {
        super();
    }

    public void init(Properties props, RuntimeContext context) {
        super.init(props, context);
        String agentClass = props.getProperty("agentClass");
        try {
            agent = (SystemAgent) Class.forName(agentClass).newInstance();
        } catch (Exception e) {
            ;
        }
        agent.setProperty("assignName", props.getProperty("assignName"));
        Activator.objectSvc.addListener(this);
    }

    public void onEvent(String eventType, Object event) {
        agent.process(eventType, event);
    }
}
