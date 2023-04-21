package uk.co.drpj.rep;

public class Maps {

    public static ArrayMap unit;

    public static MapSequence unitSequence;

    static {
        int map[] = new int[128];
        for (int i = 0; i < 128; i++) map[i] = i;
        unit = new ArrayMap(map);
        unitSequence = new TreeMapSequence();
        ((TreeMapSequence) unitSequence).put(0.0, Maps.unit);
    }
}
