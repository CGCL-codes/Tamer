package normal.engine.implementation.corba.surfaces;

import java.math.BigInteger;
import normal.engine.implementation.corba.Regina.Surfaces.*;
import normal.engine.implementation.corba.triangulation.*;
import normal.engine.implementation.corba.*;

public class NCORBANormalSurface extends CORBAShareableObject implements normal.engine.surfaces.NNormalSurface {

    public NNormalSurface data;

    public static final Class CORBAClass = NNormalSurface.class;

    public static final Class helperClass = NNormalSurfaceHelper.class;

    protected NCORBANormalSurface(NNormalSurface data) {
        super(data);
        this.data = data;
    }

    public static NCORBANormalSurface newWrapper(NNormalSurface source) {
        return (source == null ? null : new NCORBANormalSurface(source));
    }

    public BigInteger getTriangleCoord(long tetIndex, int vertex) {
        return stringToLarge(data.getTriangleCoord((int) tetIndex, vertex));
    }

    public BigInteger getQuadCoord(long tetIndex, int quadType) {
        return stringToLarge(data.getQuadCoord((int) tetIndex, quadType));
    }

    public BigInteger getOctCoord(long tetIndex, int octType) {
        return stringToLarge(data.getOctCoord((int) tetIndex, octType));
    }

    public BigInteger getEdgeWeight(long edgeIndex) {
        return stringToLarge(data.getEdgeWeight((int) edgeIndex));
    }

    public BigInteger getFaceArcs(long faceIndex, int faceVertex) {
        return stringToLarge(data.getFaceArcs((int) faceIndex, faceVertex));
    }

    public int getNumberOfCoords() {
        return data.getNumberOfCoords();
    }

    public normal.engine.triangulation.NTriangulation getTriangulation() {
        return NCORBATriangulation.newWrapper(data.getTriangulation());
    }

    public void writeRawVector() {
    }

    public boolean isCompact() {
        return data.isCompact();
    }

    public BigInteger getEulerCharacteristic() {
        return stringToLarge(data.getEulerCharacteristic());
    }

    public int isOrientable() {
        return data.isOrientable();
    }

    public boolean hasRealBoundary() {
        return data.hasRealBoundary();
    }
}
