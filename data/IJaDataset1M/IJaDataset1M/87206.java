package normal.engine.implementation.corba.angle;

import java.math.BigInteger;
import normal.engine.implementation.corba.Regina.Angle.*;
import normal.engine.implementation.corba.triangulation.*;
import normal.engine.implementation.corba.*;

public class NCORBAAngleStructure extends CORBAShareableObject implements normal.engine.angle.NAngleStructure {

    public NAngleStructure data;

    public static final Class CORBAClass = NAngleStructure.class;

    public static final Class helperClass = NAngleStructureHelper.class;

    protected NCORBAAngleStructure(NAngleStructure data) {
        super(data);
        this.data = data;
    }

    public static NCORBAAngleStructure newWrapper(NAngleStructure source) {
        return (source == null ? null : new NCORBAAngleStructure(source));
    }

    public BigInteger getAngleNum(long tetIndex, int edgePair) {
        return stringToLarge(data.getAngleNum((int) tetIndex, edgePair));
    }

    public BigInteger getAngleDen(long tetIndex, int edgePair) {
        return stringToLarge(data.getAngleDen((int) tetIndex, edgePair));
    }

    public normal.engine.triangulation.NTriangulation getTriangulation() {
        return NCORBATriangulation.newWrapper(data.getTriangulation());
    }

    public boolean isStrict() {
        return data.isStrict();
    }

    public boolean isTaut() {
        return data.isTaut();
    }
}
