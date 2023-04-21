package gov.nasa.jpf.network.cache;

import java.io.IOException;
import gov.nasa.jpf.jvm.MJIEnv;

public class JPF_gov_nasa_jpf_network_cache_CacheLayerInputStream {

    public static int native_read____I(MJIEnv env, int robj) throws IOException {
        byte[] buff = new byte[1];
        int id = env.getIntField(robj, "id");
        int num = CacheLayer.getInstance().read(id, buff);
        return num > 0 ? buff[0] : -1;
    }

    public static int native_read___3B__I(MJIEnv env, int robj, int b_ref) throws IOException {
        byte[] buff = env.getByteArrayObject(b_ref);
        int id = env.getIntField(robj, "id");
        int ret = CacheLayer.getInstance().read(id, buff);
        for (int i = 0; i < ret; i++) env.setByteArrayElement(b_ref, i, buff[i]);
        return ret;
    }

    public static boolean isNextReadBlock____Z(MJIEnv env, int robj) {
        int id = env.getIntField(robj, "id");
        return CacheLayer.getInstance().isNextReadBlocked(id);
    }
}
