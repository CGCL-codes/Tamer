package plankton.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PatchCreator {

    public static void main(String[] args) {
        new PatchCreator().writeRoute();
    }

    private void writeRoute() {
        netRoutes();
        System.out.println(patch.toString());
        try {
            File f = new File("test.pd");
            FileWriter fW = new FileWriter(f);
            fW.write(patch.toString());
            fW.flush();
            fW.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void standardCanvas() {
        patch.append("#N canvas 0 0 454 304 12;\n");
    }

    public void netRoutes() {
        standardCanvas();
        addObj("netreceive", "netreceive", "3000", 0, 0);
        addObj("route", "route", "rect1 rect2 rect3 rect4", 100, 0);
        addConnection("netreceive", 0, "route", 0);
        for (int i = 0; i < 4; i++) {
            netRoute("rect" + (i + 1), i);
        }
    }

    public void netRoute(String name, int pos) {
        int w = pos * 400;
        createRouteV("route-3" + name, new String[] { pos + "rot0", pos + "trans", pos + "rot1" }, 3, w, 80);
        createRouteV("route-2" + name, new String[] { pos + "size", pos + "pixOff", pos + "film" }, 2, w, 300);
        createRouteV("route-6" + name, new String[] { pos + "effects" }, 6, w, 500);
        createRouteV("route-1" + name, new String[] { pos + "scale" }, 1, w, 800);
        addConnection("route", pos, "route-2" + name, 0);
        addConnection("route", pos, "route-1" + name, 0);
        addConnection("route", pos, "route-3" + name, 0);
        addConnection("route", pos, "route-6" + name, 0);
    }

    public void createRouteV(String name, String[] names, int num, int x, int y) {
        StringBuffer param = new StringBuffer();
        for (int i = 0; i < num; i++) {
            param.append(" v");
            param.append(i);
        }
        StringBuffer parameters = new StringBuffer();
        int bX = x;
        for (int i = 0; i < names.length; i++) {
            addRoute(names[i], param.toString(), x, y);
            x = x + 120;
            parameters.append(names[i]);
            parameters.append(" ");
        }
        y = y - 40;
        x = bX;
        addRoute(name, parameters.toString(), x, y);
        y = y + 80;
        for (int i = 0; i < names.length; i++) {
            addConnection(name, i, names[i], 0);
            addSender(names[i] + "-v", num, names[i], x, y);
            x = x + 120;
        }
    }

    public void addSender(String name, int number, String source, int x, int y) {
        for (int i = 0; i < number; i++) {
            addObj(name + i, "s", name + i, x, y);
            y = y + 40;
            addConnection(source, i, name + i, 0);
        }
    }

    public void addSend(String name, String parameters, int x, int y) {
        addObj(name, "s", parameters, x, y);
    }

    public void addRoute(String name, String parameters, int x, int y) {
        addObj(name, "route", filterParameters(parameters), x, y);
    }

    private String filterParameters(String input) {
        String[] inputs = input.split(" ");
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < inputs.length; i++) {
            try {
                Integer.parseInt(inputs[i].substring(0, 1));
                inputs[i] = inputs[i].substring(1);
            } catch (Exception e) {
            }
            ret.append(inputs[i]);
            ret.append(" ");
        }
        return ret.toString();
    }

    public void addConnection(String from, int fromNr, String to, int toNr) {
        patch.append("#X connect ");
        try {
            patch.append(index.get(from));
            patch.append(" ");
            patch.append(fromNr);
            patch.append(" ");
            patch.append(index.get(to));
            patch.append(" ");
            patch.append(toNr);
            patch.append(";\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int num = 0;

    private TreeMap index = new TreeMap();

    StringBuffer patch = new StringBuffer();

    public void addObj(String name, String objName, String parameters, int x, int y) {
        patch.append("#X obj ");
        patch.append(x);
        patch.append(" ");
        patch.append(y);
        patch.append(" ");
        patch.append(objName);
        patch.append(" ");
        patch.append(parameters);
        patch.append(";\n");
        index.put(name, new Integer(num));
        num++;
    }
}
