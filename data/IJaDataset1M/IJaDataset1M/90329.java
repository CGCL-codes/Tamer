package org.chernovia.net.games.sports.football.server;

import java.util.List;
import java.util.StringTokenizer;
import org.chessworks.common.javatools.io.FileHelper;

public class OffPlay implements Comparable<OffPlay> {

    public static String FS = System.getProperty("file.separator");

    public static String PLAYDIR = FootBot.PLAYDIR;

    protected List<String> DataVec;

    public String name, filename;

    public float[] pcurve;

    public float sumcurve;

    public int datalen, offset;

    public boolean special;

    public static int PUNT = 0, FG = 1, MAXNAME = 12;

    public float probout = 0, probcmp = 0, probint = 0;

    public boolean pass;

    public OffPlay(String fn) {
        filename = PLAYDIR + FS + fn;
        DataVec = FileHelper.readFile(filename);
        if (DataVec == null || DataVec.size() < 6) return;
        name = DataVec.get(0);
        offset = Integer.parseInt(DataVec.get(1));
        StringTokenizer ST = new StringTokenizer(DataVec.get(2));
        datalen = ST.countTokens();
        pcurve = new float[datalen];
        sumcurve = 0;
        for (int i = 0; i < datalen; i++) {
            pcurve[i] = Float.parseFloat(ST.nextToken());
            sumcurve += pcurve[i];
        }
        probcmp = Float.parseFloat(DataVec.get(3));
        if (probcmp > 0) pass = true; else pass = false;
        probint = Float.parseFloat(DataVec.get(4));
        probout = Integer.parseInt(DataVec.get(5));
        if (name.equalsIgnoreCase("Field Goal") || name.equalsIgnoreCase("Punt")) special = true; else special = false;
    }

    public int runPlay() {
        int y;
        float pt = 0;
        float r = (float) (Math.random() * sumcurve);
        for (y = 0; y < datalen; y++) {
            pt += pcurve[y];
            if (pt > r) break;
        }
        return y - offset;
    }

    public int compareTo(OffPlay o) {
        if (o.getClass() != this.getClass()) return -1;
        if (o.probcmp > probcmp) return 1; else if (o.probcmp < probcmp) return -1; else return 0;
    }
}
