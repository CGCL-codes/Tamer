package nl.huub.van.amelsvoort.render;

import nl.huub.van.amelsvoort.client.refexport_t;

/**
 * Ref
 * 
 * @author cwei
 */
public interface Ref {

    refexport_t GetRefAPI(RenderAPI renderer);

    String getName();
}
