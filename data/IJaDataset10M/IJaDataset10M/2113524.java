package normal.engine.implementation.corba.triangulation;

import normal.engine.implementation.corba.Regina.Triangulation.*;
import normal.engine.implementation.corba.*;

public class NCORBAVertex extends CORBAShareableObject implements normal.engine.triangulation.NVertex {

    public NVertex data;

    public static final Class CORBAClass = NVertex.class;

    public static final Class helperClass = NVertexHelper.class;

    public NCORBAVertex(NVertex data) {
        super(data);
        this.data = data;
    }

    public static NCORBAVertex newWrapper(NVertex source) {
        return (source == null ? null : new NCORBAVertex(source));
    }

    public normal.engine.triangulation.NComponent getComponent() {
        return NCORBAComponent.newWrapper(data.getComponent());
    }

    public normal.engine.triangulation.NBoundaryComponent getBoundaryComponent() {
        return NCORBABoundaryComponent.newWrapper(data.getBoundaryComponent());
    }

    public int getLink() {
        return data.getLink();
    }

    public boolean isLinkClosed() {
        return data.isLinkClosed();
    }

    public boolean isIdeal() {
        return data.isIdeal();
    }

    public boolean isBoundary() {
        return data.isBoundary();
    }

    public boolean isStandard() {
        return data.isStandard();
    }

    public boolean isLinkOrientable() {
        return data.isLinkOrientable();
    }

    public long getLinkEulerCharacteristic() {
        return data.getLinkEulerCharacteristic();
    }

    public long getNumberOfEmbeddings() {
        return data.getNumberOfEmbeddings();
    }

    public normal.engine.triangulation.NVertexEmbedding getEmbedding(long index) {
        NTetrahedronHolder tet = new NTetrahedronHolder();
        org.omg.CORBA.IntHolder vertex = new org.omg.CORBA.IntHolder();
        data.getEmbedding(tet, vertex, (int) index);
        return new normal.engine.triangulation.NVertexEmbedding(NCORBATetrahedron.newWrapper(tet.value), vertex.value);
    }
}
