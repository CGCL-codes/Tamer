package cbr.agent;

import java.awt.Image;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import cbr.gui.SearchController;
import cbr.gui.SearchView;
import cbr.service.PicsFinder;
import cbr.service.PictureEntry;
import cbr.util.Images;
import de.fhg.igd.semoa.agent.RoundtripAgent;
import de.fhg.igd.semoa.security.AgentStructure;
import de.fhg.igd.semoa.server.Environment;
import de.fhg.igd.semoa.server.Mobility;
import de.fhg.igd.util.Resource;

public class CCVQueryAgent extends RoundtripAgent {

    public String getProperty(String key) {
        if (vars_ == null) {
            return null;
        }
        return vars_.get(key);
    }

    protected void perform() {
        ObjectOutputStream out;
        PictureEntry[] results;
        InputStream in;
        PicsFinder finder;
        Resource res;
        String name;
        Image image;
        int i;
        try {
            name = getProperty(AgentStructure.PROP_AGENT_ALIAS);
            System.out.print("[" + name + "] Arrived. " + " Checking for service " + getProperty("index.path") + "...");
            finder = (PicsFinder) Environment.getEnvironment().lookup(getProperty("index.path"));
            System.out.println("OK");
            res = Mobility.getContext().getResource();
            in = res.getInputStream("/" + AgentStructure.PATH_STATIC + "/image.data");
            image = Images.newImage(in);
            in.close();
            out = new ObjectOutputStream(res.getOutputStream("/" + AgentStructure.PATH_MUTABLE + "/target" + getHopNumber() + "/results"));
            results = finder.find(image, Float.parseFloat(getProperty("max.distance")), Integer.parseInt(getProperty("max.number")));
            for (i = 0; i < results.length; i++) {
                System.out.println("[" + name + "] Found " + results[i].name);
                out.writeObject(results[i]);
            }
            out.writeObject("EOF");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void finish() {
        ObjectInputStream in;
        SearchController controller;
        PictureEntry[] picEntries;
        ArrayList entries;
        String name;
        String path;
        Object o;
        int i;
        try {
            name = getProperty(AgentStructure.PROP_AGENT_ALIAS);
            entries = new ArrayList();
            for (i = 0; i < getHopNumber(); i++) {
                System.out.println("Reading pictures from target " + i);
                in = new ObjectInputStream(Mobility.getContext().getResource().getInputStream("/" + AgentStructure.PATH_MUTABLE + "/target" + i + "/results"));
                while ((o = in.readObject()) instanceof PictureEntry) {
                    entries.add(o);
                }
            }
            System.out.print("[" + name + "] Checking for service " + SearchView.PATH + "...");
            controller = (SearchController) Environment.getEnvironment().lookup(SearchView.PATH);
            System.out.println("OK");
            picEntries = new PictureEntry[entries.size()];
            for (i = 0; i < entries.size(); i++) {
                picEntries[i] = (PictureEntry) entries.get(i);
            }
            controller.put(picEntries, getProperty("searchID"));
            System.out.println("Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
