package net.sf.doolin.app.sc.security.core;

import net.sf.doolin.app.sc.engine.ClientResponse;
import net.sf.doolin.app.sc.engine.ClientState;
import net.sf.doolin.app.sc.engine.Engine;
import net.sf.doolin.app.sc.engine.InstanceGenerator;
import net.sf.doolin.app.sc.engine.InstanceID;

public interface SecuredEngine<G extends InstanceGenerator, T extends ClientState, P extends ClientResponse> extends Engine<G, T, P> {

    String META_CREATOR = "SecuredEngine.Creator";

    boolean isCreator(InstanceID iid, String name);
}
