package org.moyoman.util;

import org.moyoman.framework.ServerConfig;
import org.moyoman.log.*;
import java.io.*;

public class Zobrist implements Serializable {

    /** The random numbers used for the black stones.*/
    private static long[][] blackRandom;

    /** The random numbers used for the white stones.*/
    private static long[][] whiteRandom;

    static {
        try {
            blackRandom = new long[19][19];
            whiteRandom = new long[19][19];
            ServerConfig sc = ServerConfig.getServerConfig();
            String topdir = sc.getTopDirectory();
            FileReader fr = new FileReader(topdir + "data" + File.separator + "org" + File.separator + "moyoman" + File.separator + "util" + File.separator + "zobrist");
            BufferedReader br = new BufferedReader(fr);
            String s = br.readLine();
            if (!s.equals("Black values")) {
                throw new Exception("Unknown file format for zobrist");
            }
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    s = br.readLine();
                    long val = Long.parseLong(s);
                    blackRandom[i][j] = val;
                }
            }
            s = br.readLine();
            if (!s.equals("White values")) {
                throw new Exception("Unknown file format for zobrist");
            }
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {
                    s = br.readLine();
                    long val = Long.parseLong(s);
                    whiteRandom[i][j] = val;
                }
            }
            br.close();
        } catch (Exception e) {
            SystemLog.error(e);
        }
    }

    /** Get the Zobrist hash value for the given board position.
	  * @param stones - An array of Stone objects which describes the board.
	  * @return A Long object.
	  */
    public static Long getValue(Stone[] stones) {
        long hashval = 0;
        for (int i = 0; i < stones.length; i++) {
            int x = stones[i].getX();
            int y = stones[i].getY();
            if (stones[i].getColor().equals(Color.BLACK)) {
                hashval ^= blackRandom[x][y];
            } else if (stones[i].getColor().equals(Color.WHITE)) {
                hashval ^= whiteRandom[x][y];
            }
        }
        Long l = new Long(hashval);
        return l;
    }
}
