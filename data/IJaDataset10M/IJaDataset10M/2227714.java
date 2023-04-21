package org.eclipse.epsilon.fptc.test.unit;

import org.eclipse.epsilon.fptc.simulation.includes.crossproduct.CrossProduct;
import org.eclipse.epsilon.fptc.simulation.includes.crossproduct.CrossProductOf3Sets;
import org.eclipse.epsilon.fptc.simulation.includes.crossproduct.EmptyCrossProduct;
import org.eclipse.epsilon.fptc.simulation.includes.crossproduct.SingletonCrossProduct;
import org.eclipse.epsilon.fptc.simulation.includes.crossproduct.UnevenCrossProduct;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

@RunWith(Suite.class)
@SuiteClasses({ CrossProduct.class, UnevenCrossProduct.class, CrossProductOf3Sets.class, SingletonCrossProduct.class, EmptyCrossProduct.class })
public class CrossProductSuite {

    public static Test suite() {
        return new JUnit4TestAdapter(CrossProductSuite.class);
    }
}
