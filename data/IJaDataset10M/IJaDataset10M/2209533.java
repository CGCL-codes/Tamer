package entensim.main;

import java.util.ArrayList;
import java.util.Iterator;
import entensim.ducks.AbstractDuck;
import entensim.ducks.impl.GebrateneEnte;
import entensim.ducks.impl.GummiEnte;
import entensim.ducks.impl.LaufEnte;
import entensim.ducks.impl.MoorEnte;
import entensim.ducks.impl.StockEnte;

public class EntenSim {

    private static ArrayList<AbstractDuck> ducks;

    static {
        ducks = new ArrayList<AbstractDuck>();
        fillDucks();
    }

    private static void fillDucks() {
        ducks.add(new GebrateneEnte());
        ducks.add(new StockEnte());
        ducks.add(new LaufEnte());
        ducks.add(new MoorEnte());
        ducks.add(new GummiEnte());
    }

    public static void main(String[] args) {
        Iterator<AbstractDuck> it = ducks.iterator();
        AbstractDuck tmp;
        while (it.hasNext()) {
            tmp = it.next();
            tmp.identifyMe();
            tmp.doFly();
            tmp.doQuack();
            tmp.doSwim();
        }
    }
}
