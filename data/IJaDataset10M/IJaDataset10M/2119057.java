package mirrormonkey.rpc.tools.singlecall.entities;

import mirrormonkey.rpc.annotations.ReceiveRpcFrom;
import mirrormonkey.util.annotations.hfilter.ClassFilter;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class ClientLocalInvokeRotatableBox extends LocalInvokeRotatableBox {

    public ClientLocalInvokeRotatableBox(Vector3f pos) {
        super(pos);
    }

    @Override
    @ReceiveRpcFrom(@ClassFilter(classes = { ServerLocalInvokeRotatableBox.class }))
    public void setRotation(Quaternion rot) {
        setLocalRotation(rot);
    }
}
