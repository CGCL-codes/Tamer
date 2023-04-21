package org.fao.fenix.web.client.services;

import java.util.Vector;
import com.google.gwt.user.client.rpc.RemoteService;

public interface FenixService extends RemoteService {

    /** gwt.typeArgs <org.fao.fenix.web.ui.client.domain4.perspective.Project> */
    public Vector getAllProjects();

    /** gwt.typeArgs <org.fao.fenix.web.ui.client.domain4.perspective.MapView> */
    public Vector getMapsByProjectId(int id);

    /** gwt.typeArgs <org.fao.fenix.web.ui.client.domain4.layer.Layer> */
    public Vector getLayersByMapId(int id);
}
