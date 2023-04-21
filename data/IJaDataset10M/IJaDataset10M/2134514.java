package edu.uta.futureye.test;

import edu.uta.futureye.algebra.SpaceVector;
import edu.uta.futureye.algebra.SparseBlockVector;
import edu.uta.futureye.algebra.SparseVectorHashMap;
import edu.uta.futureye.algebra.intf.SparseVector;
import edu.uta.futureye.function.VarPair;
import edu.uta.futureye.function.Variable;
import edu.uta.futureye.function.basic.FAxpb;
import edu.uta.futureye.function.basic.SpaceVectorFunction;
import edu.uta.futureye.function.intf.Function;
import edu.uta.futureye.function.operator.FMath;

public class TestVector {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        SpaceVector a = new SpaceVector(1.0, 0.0);
        SpaceVector b = new SpaceVector(0.0, 1.0);
        System.out.println(a.dot(b));
        SpaceVector a3 = new SpaceVector(1, 0, 0);
        SpaceVector b3 = new SpaceVector(0, 1, 0);
        a3.crossProduct(b3).print();
        SparseVector sv = new SparseVectorHashMap(10, 1);
        sv.set(3, 100);
        sv.add(2, 10);
        sv.print();
        Function f1 = new FAxpb("x", 1.0, 1.0);
        Function f2 = new FAxpb("y", 2.0, 2.0);
        Function f3 = new FAxpb("z", 3.0, 3.0);
        SpaceVectorFunction svf = new SpaceVectorFunction(f1, f2, f3);
        System.out.println("svf(x,y,z)=" + svf);
        System.out.println("svf(1,1,1)=" + svf.value(new Variable().set("x", 1).set("y", 1).set("z", 1)));
        System.out.println(svf.dot(new SpaceVector(1, 2, 3)));
        Function dot2 = svf.dot(svf);
        System.out.println(dot2);
        System.out.println(dot2.value(new Variable(new VarPair("x", 1), new VarPair("y", 1), new VarPair("z", 1))));
        System.out.println("div(svf)=" + FMath.div(svf));
        SparseBlockVector sbv = new SparseBlockVector(2);
        SparseVector v1 = new SparseVectorHashMap(1.0, 2.0, 3.0);
        SparseVector v2 = new SparseVectorHashMap(4.0, 5.0);
        sbv.setBlock(1, v1);
        sbv.setBlock(2, v2);
        sbv.print();
        SparseBlockVector sbv2 = new SparseBlockVector(2);
        SparseVector v3 = new SparseVectorHashMap(10.0, 20.0, 30.0);
        SparseVector v4 = new SparseVectorHashMap(40.0, 50.0);
        sbv2.setBlock(1, v3);
        sbv2.setBlock(2, v4);
        sbv2.print();
        sbv.set(sbv2);
        sbv.print();
    }
}
