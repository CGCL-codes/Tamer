package mirrormonkey.rpc.annotations;

import mirrormonkey.rpc.member.RpcMethodDataIR;
import mirrormonkey.util.annotations.control.DefinePreset;
import mirrormonkey.util.annotations.control.IRClass;
import mirrormonkey.util.annotations.hfilter.ClassFilter;

public class RpcAnnotationPresets {

    @DefinePreset(RpcTarget.class)
    @IRClass(RpcMethodDataIR.class)
    @ReceiveRpcFrom(@ClassFilter())
    public void method() {
    }
}
