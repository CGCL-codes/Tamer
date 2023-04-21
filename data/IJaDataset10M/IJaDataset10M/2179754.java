package org.openremote.beehive.rest.service;

import javax.ws.rs.Path;
import org.openremote.beehive.SpringTestContext;
import org.openremote.beehive.rest.RemoteSectionRESTService;
import org.openremote.beehive.spring.ISpringContext;

@Path("/lirc/{vendor_name}/{model_name}")
public class RemoteSectionRESTTestService extends RemoteSectionRESTService {

    @Override
    protected Class<? extends ISpringContext> getSpringContextClass() {
        return SpringTestContext.class;
    }
}
