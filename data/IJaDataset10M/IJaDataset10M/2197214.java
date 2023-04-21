package a4;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Run {

    ArrayList<Smurf> zwerge = new ArrayList<Smurf>();

    Saftmaschinen saft = new Saftmaschinen();

    Tisch tisch = new Tisch();

    int nos = 5;

    void ausgabe() {
        System.out.print("\n");
        for (int i = 0; i < zwerge.size(); i++) {
            System.out.print(" " + zwerge.get(i).name + "(" + zwerge.get(i).tut + ")");
            if (zwerge.get(i).rgabel.isused) {
                if (zwerge.get(i).rgabel.usedby == zwerge.get(i)) {
                    System.out.print(" G       ");
                } else System.out.print("       G ");
            } else System.out.print(" g       ");
        }
    }

    public static void main(String[] args) {
        Run run = new Run();
        for (int i = 0; i < run.nos; i++) {
            run.zwerge.add(new Smurf(new Gabel(), String.format("Smurf%d", i + 1), run.saft, run.tisch));
            if (i > 0) run.zwerge.get(i).lgabel = run.zwerge.get(i - 1).rgabel;
        }
        run.zwerge.get(0).lgabel = run.zwerge.get(run.nos - 1).rgabel;
        for (int i = 0; i < run.nos; i++) {
            run.zwerge.get(i).start();
        }
        while (true) {
            run.ausgabe();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }
}
