package freelands.maps.def;

import freelands.Main;
import freelands.maps.Map;
import freelands.maps.MapObject3D;
import freelands.maps.area.TeleportUseArea;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author michael
 */
public class Use_areaDefTag extends DefTag {

    @Override
    public void treatTag(Map map, BufferedReader in) throws IOException {
        try {
            String line = in.readLine();
            String[] linearray;
            String key;
            String value;
            int index;
            short minx = -1, miny = -1, maxx = -1, maxy = -1, tpx = -1, tpy = -1, objectid = -1, invobjectid = -1, bookid = -1;
            short tpmap = -1;
            boolean sendsparks = false;
            String toofar = "Hey don't try from the moon, go near", wrongobject = "Hihi, try again", use = null;
            while (!line.startsWith("[/use_area]")) {
                if (line.startsWith(comment + "")) {
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                if (line.charAt(0) == '[') {
                    throw new Error("def file of Map" + map.mapfilename + " malformed at line : \n" + line);
                }
                linearray = line.split(":");
                if (linearray.length == 2) {
                    key = linearray[0].trim();
                    value = linearray[1].trim();
                    index = value.indexOf(comment);
                    if (index > -1) {
                        value = value.substring(0, index);
                    }
                    try {
                        Options option = Options.valueOf(key.toLowerCase().trim());
                        switch(option) {
                            case min_x:
                                minx = shortValue(value);
                                break;
                            case min_y:
                                miny = shortValue(value);
                                break;
                            case max_x:
                                maxx = shortValue(value);
                                break;
                            case max_y:
                                maxy = shortValue(value);
                                break;
                            case teleport_x:
                                tpx = shortValue(value);
                                break;
                            case teleport_y:
                                tpy = shortValue(value);
                                break;
                            case teleport_map:
                                tpmap = mapValue(value);
                                break;
                            case map_object_id:
                                objectid = shortValue(value);
                                break;
                            case inv_object_id:
                                invobjectid = shortValue(value);
                                break;
                            case send_sparks:
                                sendsparks = booleanValue(value);
                                break;
                            case too_far_text:
                                toofar = value;
                                break;
                            case wrong_object_text:
                                wrongobject = value;
                                break;
                            case use_text:
                                use = value;
                                break;
                            case open_book:
                                bookid = shortValue(value);
                                break;
                            default:
                                Main.preferences.LOGGER.fine("key " + option.name() + " not yet implemented");
                        }
                    } catch (IllegalArgumentException e) {
                        Main.preferences.LOGGER.warning("unknow key :" + key + " for map " + map.mapfilename);
                    }
                } else {
                    throw new Error("def file of Map " + map.mapfilename + " malformed at line : \n" + line);
                }
                line = in.readLine();
            }
            if (minx == -1 || miny == -1 || maxx == -1 || maxy == -1) {
                throw new Error("def file of Map " + map.mapfilename + " malformed\n missing area positions parameters in use_area");
            }
            if (objectid == -1) {
                throw new Error("def file of Map " + map.mapfilename + " malformed\n missing mapobject parameter in use_area");
            }
            if (bookid != -1) {
                if ((tpx != -1 || tpy != -1 || tpmap != -1 || sendsparks)) {
                    throw new Error("def file of Map" + map.mapfilename + " malformed\n you can't use open_book and teleportation in same effect");
                }
            } else {
                if (tpx == -1 || tpy == -1 || tpmap == -1) {
                    throw new Error("def file of Map " + map.mapfilename + " malformed\n teleportation information is malformed");
                }
                MapObject3D mo = map.getObject3d(objectid);
                if (mo == null) {
                    Main.preferences.LOGGER.warning("id of map object is for a blended object");
                } else {
                    mo.setArea(new TeleportUseArea(minx, miny, maxx, maxy, invobjectid, toofar, wrongobject, use, tpx, tpy, tpmap, sendsparks));
                }
            }
        } catch (NullPointerException npe) {
            throw new Error("def file of Map " + map.mapfilename + " malformed");
        }
    }

    private enum Options {

        min_x, min_y, max_x, max_y, teleport_x, teleport_y, teleport_map, map_object_id, inv_object_id, send_sparks, too_far_text, wrong_object_text, use_text, open_book
    }
}
