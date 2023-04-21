package cn.liangent.travlib;

import static cn.liangent.travlib.util.Utilities.*;
import HTTPClient.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Village {

    private static final Logger logger = Logger.getLogger(Village.class.getCanonicalName());

    private final Client client;

    private final User user;

    private final int newdid;

    private CompleteResourceData cachedResourceData;

    private CompleteResourceData cachedProduction;

    private ResourceData cachedWarehouseSize;

    private long resourceCachingTime;

    public Village(Client client, int newdid) {
        if (client == null) {
            throw new NullPointerException();
        }
        if (newdid < 1 || newdid > (801 * 801)) {
            throw new IllegalArgumentException();
        }
        this.client = client;
        this.user = client.getUser();
        this.newdid = newdid;
    }

    public Client getClient() {
        return client;
    }

    public int getNewdid() {
        return newdid;
    }

    private void updateResourceCache() throws IOException, HttpException, AuthenticationException, BadPageException {
        updateResourceCache(System.currentTimeMillis(), httpGet("/dorf1.php"));
    }

    private void updateResourceCache(String pgsrc) throws BadPageException {
        updateResourceCache(System.currentTimeMillis(), pgsrc);
    }

    private void updateResourceCache(long setTime, String pgsrc) {
        if (setTime < 0) setTime = System.currentTimeMillis();
        CompleteResourceData cr = cachedResourceData;
        CompleteResourceData cp = cachedProduction;
        ResourceData cws = cachedWarehouseSize;
        long cct = resourceCachingTime;
        try {
            updateResourceCacheWork(pgsrc);
            resourceCachingTime = setTime;
        } catch (BadPageException ex) {
            cachedResourceData = cr;
            cachedProduction = cp;
            cachedWarehouseSize = cws;
            resourceCachingTime = cct;
        }
    }

    private void updateResourceCacheWork(String pgsrc) throws BadPageException {
        pgsrc = removeLineSeparator(pgsrc);
        {
            String slumber = find(pgsrc, "\\Qtitle=\"" + user.getString("Lumber") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E(\\d+)\\Q/\\E\\d+\\Q</td>\\E");
            String sclay = find(pgsrc, "\\Qtitle=\"" + user.getString("Clay") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E(\\d+)\\Q/\\E\\d+\\Q</td>\\E");
            String siron = find(pgsrc, "\\Qtitle=\"" + user.getString("Iron") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E(\\d+)\\Q/\\E\\d+\\Q</td>\\E");
            String scrop = find(pgsrc, "\\Qtitle=\"" + user.getString("Crop") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E(\\d+)\\Q/\\E\\d+\\Q</td>\\E");
            String scc = find(pgsrc, "\\Qtitle=\"" + user.getString("Crop consumption") + "\">&nbsp;\\E(\\d+)\\Q/\\E\\d+\\Q</td>\\E");
            int lumber, clay, iron, crop, cc;
            try {
                lumber = Integer.parseInt(slumber);
                clay = Integer.parseInt(sclay);
                iron = Integer.parseInt(siron);
                crop = Integer.parseInt(scrop);
                cc = Integer.parseInt(scc);
            } catch (Exception ex) {
                throw new BadPageException(ex);
            }
            if (lumber < 0 || clay < 0 || iron < 0 || crop < 0 || cc < 0) {
                throw new BadPageException("resource amount < 0");
            }
            cachedResourceData = new CompleteResourceData(lumber, clay, iron, crop, cc);
        }
        {
            String slumber = find(pgsrc, "\\Qtitle=\"" + user.getString("Lumber") + "\"></td><td id=l\\E[1-4]\\Q title=\\E(\\d+)\\Q>\\E\\d+\\Q/\\E\\d+\\Q</td>\\E");
            String sclay = find(pgsrc, "\\Qtitle=\"" + user.getString("Clay") + "\"></td><td id=l\\E[1-4]\\Q title=\\E(\\d+)\\Q>\\E\\d+\\Q/\\E\\d+\\Q</td>\\E");
            String siron = find(pgsrc, "\\Qtitle=\"" + user.getString("Iron") + "\"></td><td id=l\\E[1-4]\\Q title=\\E(\\d+)\\Q>\\E\\d+\\Q/\\E\\d+\\Q</td>\\E");
            String scrop = find(pgsrc, "\\Qtitle=\"" + user.getString("Crop consumption") + "\">&nbsp;\\E\\d+\\Q/\\E(\\d+)\\Q</td>\\E");
            String scc = find(pgsrc, "\\Qtitle=\"" + user.getString("Crop consumption") + "\">&nbsp;\\E(\\d+)\\Q/\\E\\d+\\Q</td>\\E");
            int lumber, clay, iron, crop, cc;
            try {
                lumber = Integer.parseInt(slumber);
                clay = Integer.parseInt(sclay);
                iron = Integer.parseInt(siron);
                crop = Integer.parseInt(scrop);
                cc = Integer.parseInt(scc);
            } catch (Exception ex) {
                throw new BadPageException(ex);
            }
            if (lumber < 0 || clay < 0 || iron < 0 || crop < 0 || cc < 0) {
                throw new BadPageException("resource amount < 0");
            }
            cachedProduction = new CompleteResourceData(lumber, clay, iron, crop, cc);
        }
        {
            String slumber = find(pgsrc, "\\Qtitle=\"" + user.getString("Lumber") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E\\d+\\Q/\\E(\\d+)\\Q</td>\\E");
            String sclay = find(pgsrc, "\\Qtitle=\"" + user.getString("Clay") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E\\d+\\Q/\\E(\\d+)\\Q</td>\\E");
            String siron = find(pgsrc, "\\Qtitle=\"" + user.getString("Iron") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E\\d+\\Q/\\E(\\d+)\\Q</td>\\E");
            String scrop = find(pgsrc, "\\Qtitle=\"" + user.getString("Crop") + "\"></td><td id=l\\E[1-4]\\Q title=\\E\\d+\\Q>\\E\\d+\\Q/\\E(\\d+)\\Q</td>\\E");
            int lumber, clay, iron, crop;
            try {
                lumber = Integer.parseInt(slumber);
                clay = Integer.parseInt(sclay);
                iron = Integer.parseInt(siron);
                crop = Integer.parseInt(scrop);
            } catch (Exception ex) {
                throw new BadPageException(ex);
            }
            if (lumber < 0 || clay < 0 || iron < 0 || crop < 0) {
                throw new BadPageException("resource amount < 0");
            }
            cachedWarehouseSize = new ResourceData(lumber, clay, iron, crop);
        }
    }

    /**
	 * this method will throw only if update == true
	 */
    public CompleteResourceData getResourceData(boolean update) throws IOException, HttpException, AuthenticationException, BadPageException {
        if (update || cachedResourceData == null) {
            updateResourceCache();
            return cachedResourceData;
        } else {
            long time = System.currentTimeMillis();
            return cachedResourceData.calculateProduct(time - resourceCachingTime, cachedProduction, cachedWarehouseSize);
        }
    }

    public CompleteResourceData getProduction(boolean update) throws IOException, HttpException, AuthenticationException, BadPageException {
        if (update || cachedProduction == null) updateResourceCache();
        return cachedProduction;
    }

    public ResourceData getWarehouseSize(boolean update) throws IOException, HttpException, AuthenticationException, BadPageException {
        if (update || cachedWarehouseSize == null) updateResourceCache();
        return cachedWarehouseSize;
    }

    public Field getField(int id) {
        if (id < 1 || id > 40) {
            throw new IllegalArgumentException();
        }
        return new Field(this, id);
    }

    public Field[] getFields() {
        Field[] fld = new Field[18];
        for (int i = 0; i < 18; i++) {
            fld[i] = new Field(this, i + 1);
        }
        return fld;
    }

    public StaticField[] getStaticFields() throws IOException, HttpException, AuthenticationException, BadPageException {
        String pgsrc = httpGet("/dorf1.php?newdid=" + newdid);
        logger.fine("get fields: dorf1.php:");
        logger.fine(pgsrc);
        StaticField[] fields = new StaticField[18];
        for (int i = 1; i <= 18; i++) {
            String name = find(pgsrc, "\\Q<area href=\"build.php?id=" + i + "\" coords=\"\\E.*?\\Q\" shape=\"circle\" title=\"\\E(.*?)\\Q\">");
            logger.info("got " + name + " in id " + i);
            String[] part = name.split(" ");
            if (part.length < 3) throw new BadPageException("no enough parts in '" + name + "'");
            if (!part[part.length - 2].equals(user.getString("level"))) throw new BadPageException("not 'level' in mid-'" + name + "'");
            int level;
            try {
                level = Integer.parseInt(part[part.length - 1]);
            } catch (Exception ex) {
                throw new BadPageException(ex);
            }
            if (level < 0) throw new BadPageException("level < 0");
            for (int i = 1; i < part.length - 2; i++) {
                part[0] += " " + part[i];
            }
            if (part[0].equals(user.getString("Woodcutter"))) {
                fields[i - 1] = new StaticField(this, i, Building.Type.WOODCUTTER, level);
            } else if (part[0].equals(user.getString("Clay Pit"))) {
                fields[i - 1] = new StaticField(this, i, Building.Type.CLAY_PIT, level);
            } else if (part[0].equals(user.getString("Iron Mine"))) {
                fields[i - 1] = new StaticField(this, i, Building.Type.IRON_MINE, level);
            } else if (part[0].equals(user.getString("Cropland"))) {
                fields[i - 1] = new StaticField(this, i, Building.Type.CROPLAND, level);
            } else {
                throw new BadPageException(part[0] + " is not a name of fields");
            }
        }
        return fields;
    }

    String httpGet(String path) throws IOException, HttpException, AuthenticationException, BadPageException {
        int idx = path.indexOf("#");
        if (idx != -1) {
            path = path.substring(0, idx);
        }
        if (newdid != 0 && !path.matches(".*[?&]newdid=\\d{1,6}($|&)")) {
            if (path.contains("?")) {
                path += "&newdid=" + newdid;
            } else {
                path += "?newdid=" + newdid;
            }
        }
        String reply = client.httpGet(path);
        updateResourceCache(reply);
        return reply;
    }

    String httpPost(String path, NVPair[] form) throws IOException, HttpException, AuthenticationException, BadPageException {
        int idx = path.indexOf("#");
        if (idx != -1) {
            path = path.substring(0, idx);
        }
        if (newdid != 0 && !path.matches(".*[?&]newdid=\\d{1,6}($|&)")) {
            if (path.contains("?")) {
                path += "&newdid=" + newdid;
            } else {
                path += "?newdid=" + newdid;
            }
        }
        String reply = client.httpPost(path, form);
        updateResourceCache(reply);
        return reply;
    }
}
