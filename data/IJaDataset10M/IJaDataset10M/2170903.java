package normal.engine.implementation.corba.algebra;

import java.math.BigInteger;
import java.util.Enumeration;
import normal.engine.implementation.corba.Regina.Algebra.*;
import normal.engine.implementation.corba.maths.*;
import normal.engine.implementation.corba.*;

public class NCORBAAbelianGroup extends CORBAShareableObject implements normal.engine.algebra.NAbelianGroup {

    public NAbelianGroup data;

    public static final Class CORBAClass = NAbelianGroup.class;

    public static final Class helperClass = NAbelianGroupHelper.class;

    protected NCORBAAbelianGroup(NAbelianGroup data) {
        super(data);
        this.data = data;
    }

    public static NCORBAAbelianGroup newWrapper(NAbelianGroup source) {
        return (source == null ? null : new NCORBAAbelianGroup(source));
    }

    public void addRank() {
        addRank(1);
    }

    public void addRank(int extraRank) {
        data.addRank(extraRank);
    }

    public void addTorsionElement(BigInteger degree) {
        addTorsionElement(degree, 1);
    }

    public void addTorsionElement(BigInteger degree, int mult) {
        data.addTorsionElement_bigInt(degree.toString(), mult);
    }

    public void addTorsionElement(long degree) {
        addTorsionElement(degree, 1);
    }

    public void addTorsionElement(long degree, int mult) {
        data.addTorsionElement_long((int) degree, mult);
    }

    public void addTorsionElements(Enumeration torsion) {
        while (torsion.hasMoreElements()) addTorsionElement((BigInteger) torsion.nextElement(), 1);
    }

    public void addGroup(normal.engine.maths.NMatrixInt presentation) {
        data.addGroup_NMatrixInt(((NCORBAMatrixInt) presentation).data);
    }

    public void addGroup(normal.engine.algebra.NAbelianGroup group) {
        data.addGroup_NAbelianGroup(((NCORBAAbelianGroup) group).data);
    }

    public int getRank() {
        return data.getRank();
    }

    public int getTorsionRank(BigInteger degree) {
        return data.getTorsionRank_bigInt(degree.toString());
    }

    public int getTorsionRank(long degree) {
        return data.getTorsionRank_long((int) degree);
    }

    public long getNumberOfInvariantFactors() {
        return data.getNumberOfInvariantFactors();
    }

    public BigInteger getInvariantFactor(long index) {
        return stringToLarge(data.getInvariantFactor((int) index));
    }
}
