package playground.scnadine.MapMatching.detection;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Vector;
import playground.scnadine.MapMatching.global.ConnexionPostgresql;

/**
 * In process...
 * 
 * 
 * @author dboillee
 *
 */
public class DataFiltering {

    static int nbSatMinimum = 3;

    static int HDOPMinimum = 5;

    static String errorData = "(speed = 0 and direction = 0)";

    private ConnexionPostgresql con;

    private String table;

    Vector segments = new Vector();

    public DataFiltering(String base, String table) throws Exception {
        con = new ConnexionPostgresql();
        con.open(base);
        this.table = table;
    }

    /**
	 * Calcul de l'acceleration dans la table de points
	 * @throws Exception
	 */
    private void begin(int firstGid) throws Exception {
        con.execute("update " + table + " set acceleration=(select (t2.speed-t1.speed)/(3.6*EXTRACT(EPOCH FROM (t2.date-t1.date))) from " + table + " t1, " + table + " t2 where t1.gid=t2.gid-1 and t2.gid= " + table + ".gid) where " + table + ".gid>=" + firstGid + " and " + table + ".date<>(select t3.date from  " + table + "  t3 where t3.gid= " + table + ".gid-1)");
    }

    /**
	 * Met a jour la colonne 'quality' de la table de points avec les valeurs 'good' or 'bad'
	 * Les points ayant un nombre de satellites < nbSatMinimum (initialement 3), les points avec un hdop > HDOPMinimum (initialement 5) et les points tels que la vitesse
	 * et la direction sont nuls sont consid�r� comme "non-valide"
	 * @throws Exception
	 */
    private void deleteWrongData(int firstGid) throws Exception {
        con.execute("update " + table + " set quality='good' where gid>=" + firstGid);
        con.execute("update " + table + " set quality='bad' where gid>=" + firstGid + " and (hdop > " + HDOPMinimum + " or nbsat < " + nbSatMinimum + ")");
        con.execute("update " + table + " set quality='bad' where gid>=" + firstGid + "and " + errorData);
    }

    /**
	 * Detecte les activit�s (colonne "type_activity")
	 *  "Act1" outdoor activity (GPS records with zero speeds during more than 120 seconds)
		"Act2_1" indoor activity	(signal loss from 120 seconds to 600 seconds and the distance less than 50 meters)
		"Act2_2" underground or indoor travelling activities (signal loss more than 120 seconds and distance  more than 500 meters)
		"Act2_3" long-duration activity	(signal loss more than 600 seconds)
		"Act2_bis" Bad quality (signal loss duration between 120 and 600 seconds and distance from 50 to 500 meters)
	  	"-"	No activity
	 * @throws Exception
	 */
    private void detectActivity(int firstGid) throws Exception {
        long prevTime = 0;
        int prevGid = 0;
        int countNulSpeed = 0;
        int beginNulSpeed = 0;
        int endNulSpeed = 0;
        Vector act1 = new Vector();
        Vector act2_1 = new Vector();
        Vector act2_2 = new Vector();
        Vector act2_3 = new Vector();
        Vector act2_bis = new Vector();
        ResultSet r = con.executeR("SELECT t1.gid,t1.date,t1.speed, distance(t1.the_geom,t2.the_geom) FROM " + table + " t1," + table + " t2 where t1.gid>=" + firstGid + " and t1.quality='good' and t2.gid=(select max(gid) from " + table + " t3 where t3.gid<t1.gid and t3.quality='good') order by t1.gid");
        while (r.next()) {
            int gid = r.getInt(1);
            Timestamp t = Timestamp.valueOf(r.getString(2));
            long time = t.getTime() / 1000;
            double speed = r.getDouble(3);
            double distance = r.getDouble(4);
            if (r.isFirst()) {
                prevTime = time;
                prevGid = gid;
            }
            if (speed == 0) {
                countNulSpeed++;
            } else {
                if (countNulSpeed >= 120) {
                    for (int i = beginNulSpeed; i <= endNulSpeed; i++) act1.add(new Long(i));
                }
                countNulSpeed = 0;
            }
            if (countNulSpeed == 1) beginNulSpeed = gid;
            if (countNulSpeed >= 120) endNulSpeed = gid;
            if (time - prevTime >= 120 && time - prevTime < 600 && distance < 50) {
                act2_1.add(new Long(prevGid));
                act2_1.add(new Long(gid));
            }
            if (time - prevTime >= 120 && distance >= 500) {
                act2_2.add(new Long(prevGid));
                act2_2.add(new Long(gid));
            }
            if (time - prevTime >= 600) {
                act2_3.add(new Long(prevGid));
                act2_3.add(new Long(gid));
            }
            if (time - prevTime >= 120 && time - prevTime <= 600 && distance >= 50 && distance < 500) {
                act2_bis.add(new Long(prevGid));
                act2_bis.add(new Long(gid));
            }
            prevGid = gid;
            prevTime = time;
        }
        con.execute("update " + table + " set type_activity='-' where gid>=" + firstGid);
        executeUpdate(act1, "type_activity", "act1");
        executeUpdate(act2_1, "type_activity", "act2_1");
        executeUpdate(act2_2, "type_activity", "act2_2");
        executeUpdate(act2_3, "type_activity", "act2_3");
        executeUpdate(act2_bis, "type_activity", "act2_bis");
    }

    /**
	 * D�tecte les segments. Un segment est limit� par les points SOW (start of walk), EOW(end of walk) et EOG(end of gap).
	 * Un EOG est d�termin� par une dur�e>=80 s entre 2 points
	 * Un SOW est d�termin� par une acc�l�ration comprise entre 0 et 0.1 m/s� de 3 points succ�ssif et l'absence de vitesse > 13km/h
	 * pendant 60 s.
	 * Un EOW est d�termin� par une acc�l�ration comprise entre -0.1 et 0 m/s� de 3 points succ�ssif. Si il y a une suite d'EOW, sans vitesse > 13 km/h,
	 * seul le dernier EOW est consid�r� comme tel.
	 * Les segments compris entre un SOW et un EOW, entre un EOG et un EOW ou entre un SOW et un EOG sont consid�r� comme �tant parcouru � pied.
	 */
    private void detectSegment(int firstGid) throws Exception {
        char position = 'd';
        char toFind = 'e';
        int lastSowGid = 0;
        long lastSowTime = 0;
        int lastEowGid = 0;
        long lastTime = Long.MAX_VALUE;
        long lastGid = Long.MAX_VALUE;
        int nbacc = 0;
        int nbdec = 0;
        ResultSet r = con.executeR("SELECT  gid,date,speed,acceleration FROM " + table + " where gid>=" + firstGid + " and quality='good' and type_activity<>'act1' order by gid");
        int gid = -1;
        int start = -1;
        int end = -1;
        boolean car = false;
        Segment s = new Segment();
        s.mode = "";
        while (r.next()) {
            gid = r.getInt(1);
            if (r.isFirst()) {
                s.begin = gid;
            }
            Timestamp t = Timestamp.valueOf(r.getString(2));
            long time = t.getTime() / 1000;
            double speed = r.getDouble(3);
            double acceleration = r.getDouble(4);
            if (time - lastTime > 80) {
                s.end = lastGid;
                if (!car) s.mode = "walk";
                segments.add(s);
                s = new Segment();
                s.mode = "";
                s.begin = gid;
                position = 'd';
                toFind = 'e';
                car = false;
                nbacc = 0;
                nbdec = 0;
                start = -1;
                end = -1;
            }
            if (toFind == 'e') {
                if (acceleration <= 0 && acceleration > -0.1) nbdec++; else {
                    nbdec = 0;
                }
                if (nbdec >= 3) end = gid; else end = -1;
            }
            if (toFind == 's') {
                if (acceleration >= 0 && acceleration < 0.1) nbacc++; else nbacc = 0;
                if (nbacc >= 3) start = gid; else start = -1;
            }
            if (speed > 13) car = true;
            if (position == 'd') {
                if (speed > 13 && (toFind == 'e')) {
                    toFind = 's';
                }
                if (toFind == 'e' && end != -1) {
                    lastEowGid = gid;
                    end = -1;
                    nbdec = 0;
                    position = 'e';
                    toFind = 'e';
                }
                if (toFind == 's' && start != -1) {
                    lastSowGid = start;
                    lastSowTime = time;
                    start = -1;
                    nbacc = 0;
                    position = 's';
                    toFind = '0';
                }
            } else if (position == 's') {
                if (speed > 13) {
                    position = 'd';
                    toFind = 's';
                }
                if (time - lastSowTime > 60 && toFind == '0') {
                    toFind = 'e';
                    car = false;
                }
                if (end != -1) {
                    lastEowGid = gid;
                    position = 'e';
                    end = -1;
                    nbdec = 0;
                    toFind = 'e';
                    s.end = lastSowGid;
                    s.mode = "";
                    segments.add(s);
                    s = new Segment();
                    s.begin = lastSowGid;
                    s.mode = "walk";
                }
            } else if (position == 'e') {
                if (speed > 13 && toFind == 'e') {
                    toFind = 's';
                    s.end = lastEowGid;
                    s.mode = "walk";
                    segments.add(s);
                    s = new Segment();
                    s.mode = "";
                    s.begin = gid;
                }
                if (toFind == 'e' && end != -1) {
                    lastEowGid = gid;
                }
                if (toFind == 's' && start != -1) {
                    lastSowGid = gid;
                    lastSowTime = time;
                    end = -1;
                    nbdec = 0;
                    position = 's';
                    toFind = '0';
                }
            }
            lastGid = gid;
            lastTime = time;
        }
        if (!car) s.mode = "walk";
        s.end = gid;
        segments.add(s);
    }

    /**
	 * Met a jour la colonne 'colomnName' de la table avec la valeur 'columndata' quand le gid est dans la liste 'list' 
	 * @param list
	 * @param columnName
	 * @param columnData
	 * @throws Exception
	 */
    private void executeUpdate(Vector list, String columnName, String columnData) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            con.execute("update " + table + " set " + columnName + "='" + columnData + "' where gid =" + list.elementAt(i));
        }
    }

    /**
	 * Ajout du mode dans la table de points (colonne "mode")
	 * Sans mode: "-"
	 * @throws Exception
	 */
    private void addMode(int firstGid) throws Exception {
        con.execute("update " + table + " set mode='-' where gid>=" + firstGid);
        for (int i = 0; i < segments.size(); i++) {
            Segment s = (Segment) segments.elementAt(i);
            if (s.mode.equals("walk")) con.execute("update " + table + " set mode='" + s.mode + "' where quality='good' and gid>=" + s.begin + " and gid<=" + s.end);
        }
    }

    private void detectMode() throws Exception {
        for (int i = 0; i < segments.size(); i++) {
            Segment s = (Segment) segments.elementAt(i);
            if (!s.mode.equals("walk")) {
                ResultSet r = con.executeR("select avg(speed),max(speed),avg(abs(acceleration)),count(*) from " + table + " where quality='good' and gid>=" + s.begin + " and gid<=" + s.end);
                r.next();
                s.AS = r.getDouble(1);
                s.S = r.getDouble(2) * 0.95;
                s.A = r.getDouble(3);
                s.DQ = r.getDouble(4) / (1 + s.end - s.begin);
                System.out.println("begin " + s.begin + "  end" + s.end + "  A: " + s.A + "  AS " + s.AS + "  S " + s.S + "  DQ " + s.DQ);
            }
        }
    }

    /**Effectue toutes les op�rations de pr�processing: cr�ation des nouvelles colonnes, d�tection des donn�es "non-valides",
	 * d�tection des activit�s, d�tection des segments � map-match�, d�tection du mode de transport de ces segments.
	 * @throws Exception
	 */
    public void process() throws Exception {
        process_tiny();
        int firstGid = 0;
        ResultSet r = con.executeR("select count(gid),min(gid) from " + table + " where mode is null ");
        r.next();
        if (r.getInt(1) != 0) {
            firstGid = r.getInt(2);
            detectActivity(firstGid);
            System.out.println("Done activity");
            detectSegment(firstGid);
            addMode(firstGid);
        }
    }

    public void process_tiny() throws Exception {
        int firstGid = 0;
        ResultSet r = con.executeR("select count(gid),min(gid) from " + table + " where quality is null ");
        r.next();
        if (r.getInt(1) != 0) {
            firstGid = r.getInt(2);
            begin(firstGid);
            deleteWrongData(firstGid);
        }
    }
}
