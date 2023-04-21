package ru.jasatoo;

import ru.jasatoo.diff.rp.NoCenterOfGravityRP;
import ru.jasatoo.diff.rp.NoCenterOfGravityRPData;
import ru.jnano.bal.models.planet.PlanetBean;
import ru.jnano.math.calc.diff.diff.DiffStatus;
import ru.jnano.math.calc.diff.diff.FactoryDiff;
import ru.jnano.math.calc.diff.diff.IDiffSolve;
import ru.jnano.math.calc.diff.rp.IRightPart;
import ru.jnano.math.calc.diff.rp.ListDataRightPart;
import ru.jnano.math.geom.OrbitUtils;
import ru.jnano.math.utils.PrintUtils;

public class NoCenterOfGravityTest {

    private static final double RADIUS = 6800000;

    public static void main(String[] args) throws InterruptedException {
        PlanetBean planetBean = new PlanetBean(0.00007292115, 6378140, 6356755, 398600440000000.0, -0.0010826274, -0.00000002371);
        AtmGOST25645Bean atmGOST25645Bean = new AtmGOST25645Bean(9.80665, -18.70561, 0.59676, 116.56314, 9.80665, -18.70041, 0.57145, 110.48925, 9.80665, -25.8666, 0.35861, 368.99154);
        SputnikBean sputnikBean = new SputnikBean(0.000345);
        NoCenterOfGravityRPData model = new NoCenterOfGravityRPData(planetBean, atmGOST25645Bean, sputnikBean);
        IRightPart rightPart = new NoCenterOfGravityRP(model);
        ListDataRightPart data = new ListDataRightPart();
        double velocity = OrbitUtils.CircularVelocity(planetBean.getMu(), RADIUS);
        double[] initY = { RADIUS, 0.0, 0.0, 0.0, 0.0, velocity };
        double initTime = 0;
        double endTime = 6000;
        double step = 2;
        IDiffSolve diff = FactoryDiff.RungeKutt4(rightPart, data, initY, initTime, endTime, step);
        DiffStatus status = diff.call();
        System.out.println("������ - " + status);
        PrintUtils.onCosoleData(data.getList(), "%.2f %.0f ", "time", "altitude");
    }
}
