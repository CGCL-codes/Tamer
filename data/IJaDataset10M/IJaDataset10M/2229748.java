package org.ist_spice.mdcs.beans;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import org.ist_spice.mdcs.interfaces.FusionBean_IF;

@Stateless
@Remote(FusionBean_IF.class)
@Local(FusionBean_IF.class)
public class FusionBean implements FusionBean_IF {

    public void announceBoundRenderer(long user_id, String renderer_id, String content_id) {
    }

    public void announceUnboundRenderer(long user_id, String renderer_id, String content_id) {
    }
}
