package mipt.math.fuzzy.op.impl;

import mipt.math.fuzzy.FuzzyNumber;
import mipt.math.fuzzy.op.*;
import mipt.math.fuzzy.GaussianNumber;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class GMult extends Mult {

    public final FuzzyNumber calc(FuzzyNumber fa, FuzzyNumber fb) {
        System.out.println("mult:" + fa + "," + fb);
        double adata[] = fa.data();
        double bdata[] = fb.data();
        double a = adata[0], sa = adata[1];
        double b = bdata[0], sb = bdata[1];
        double val = a * b;
        double err = Math.sqrt(a * a * sb * sb + b * b * sa * sa + sa * sa * sb * sb);
        return new GaussianNumber(val, err);
    }
}
