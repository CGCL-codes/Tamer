package pl.edu.wat.wcy.jit.engine;

import java.util.Comparator;

/**
 * @author: Robert Zal, Dariusz Pierzchala
 */
public class ActivityCompare implements Comparator {

    public int compare(Object Obj1, Object Obj2) {
        SimActivity Sim1, Sim2;
        int pom;
        Sim1 = (SimActivity) Obj1;
        Sim2 = (SimActivity) Obj2;
        double X = Sim1.getTimeOfResume() - Sim2.getTimeOfResume();
        if (X < 0) pom = -1; else if (X > 0) pom = 1; else if (Sim1.getMyState() == 'I') pom = -1; else if (Sim2.getMyState() == 'I') pom = 1; else pom = 0;
        return pom;
    }
}
