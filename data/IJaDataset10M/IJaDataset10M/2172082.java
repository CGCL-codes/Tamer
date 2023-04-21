package br.com.nix.beans.obj;

import br.com.nix.beans.vertex.Vertex;
import br.com.nix.beans.vertex.VertexBeanInfo;

public class ObjBeanInfo extends VertexBeanInfo {

    public ObjBeanInfo(Class<? extends Vertex> classe) {
        super(classe);
        addPropertyDescriptors(new ProtoObjBeanInfo().getPropertyDescriptors());
    }

    public ObjBeanInfo() {
        this(Obj.class);
    }
}
