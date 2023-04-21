package org.opendeco.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.opendeco.DiveParameters;
import org.opendeco.base.Composite;
import org.opendeco.base.Profil;
import org.opendeco.base.Segment;
import org.opendeco.gaz.GazMix;
import org.opendeco.gaz.GazMixUsage;
import org.opendeco.tools.ToolBox;

public class SingleLevelTableModel extends TableModel {

    private List<Category> categories = null;

    private double depth = 0f;

    private double lastDepth = 0f;

    private int timeInSeconds = 0;

    private double stopSpeed = -6f;

    private double ascentSpeed = -15f;

    private double descentSpeed = 30f;

    /**
     * 
     * @param name
     * @throws IOException
     */
    public SingleLevelTableModel(String name) {
        super(name);
        String path = "/" + this.getClass().getPackage().getName().replace('.', '/');
        path += "/tables/" + name + "/table.csv";
        InputStream stream = this.getClass().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        try {
            line = reader.readLine();
            Master master = new Master(line.split(","));
            Map<Double, Category> allentries = new Hashtable<Double, Category>();
            if (master.isValid()) {
                while ((line = reader.readLine()) != null) {
                    String array[] = line.split(",");
                    Entry tmpEntry = new Entry(master, array);
                    Category entries = allentries.get(tmpEntry.depth);
                    if (entries == null) {
                        entries = new Category(tmpEntry.depth);
                        allentries.put(tmpEntry.depth, entries);
                    }
                    entries.entries.add(tmpEntry);
                }
            }
            for (Category entries : allentries.values()) {
                java.util.Collections.sort(entries.entries, new Comparator<Entry>() {

                    @Override
                    public int compare(Entry arg0, Entry arg1) {
                        return ((arg0.timeInSeconds > arg1.timeInSeconds) ? 1 : ((arg0.timeInSeconds < arg1.timeInSeconds) ? -1 : 0));
                    }
                });
            }
            categories = new ArrayList<Category>(allentries.values());
            java.util.Collections.sort(categories, new Comparator<Category>() {

                @Override
                public int compare(Category arg0, Category arg1) {
                    return ((arg0.depth > arg1.depth) ? 1 : ((arg0.depth < arg1.depth) ? -1 : 0));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDetails() {
        return ToolBox.formatUnit(descentSpeed, "m/min") + ", " + ToolBox.formatUnit(ascentSpeed, "m/min") + ", " + ToolBox.formatUnit(stopSpeed, "m/min");
    }

    @Override
    public void add(Profil profil) {
        if (profil instanceof Segment) {
            this.depth = Math.max(this.depth, profil.getStartDepth());
            this.depth = Math.max(this.depth, profil.getEndDepth());
            lastDepth = profil.getEndDepth();
            this.timeInSeconds += profil.getTimeInSeconds();
        } else if (profil instanceof Composite) {
            for (Profil segment : (Composite) profil) {
                this.add(segment);
            }
        }
    }

    @Override
    public void reset() {
        this.depth = 0f;
        this.timeInSeconds = 0;
    }

    @Override
    protected TableModelDeco getDeco() throws DecompressionCalculationException {
        Category cat = ToolBox.findImmediateHigher(categories, this.depth);
        Entry entry = null;
        if (cat != null) {
            entry = ToolBox.findImmediateHigher(cat.entries, this.timeInSeconds);
        }
        if (entry == null) {
            throw new DecompressionCalculationException("No entry in table for " + this.depth + "m " + this.timeInSeconds / 60 + "'");
        }
        return entry.deco;
    }

    @Override
    protected double getLastDepth() {
        return this.lastDepth;
    }

    /**
     * 
     * @author pmelet
     * 
     */
    private static class Category implements Comparable<Double> {

        private double depth = 0f;

        private List<Entry> entries = new ArrayList<Entry>();

        public Category(double depth) {
            this.depth = depth;
        }

        public String toString() {
            return depth + ":" + super.toString();
        }

        @Override
        public int compareTo(Double o) {
            return (depth > o ? 1 : (depth < o ? -1 : 0));
        }
    }

    /**
     * 
     * @author pmelet
     * 
     */
    private static class Entry implements Comparable<Integer> {

        protected double depth = 0f;

        protected int timeInSeconds = 0;

        private TableModelDeco deco = null;

        public Entry(Master master, String[] array) {
            this.depth = Double.parseDouble(array[master.depth]);
            this.timeInSeconds = ToolBox.parseTime(array[master.time]);
            int index = 0;
            int firstStop = -1;
            for (int stopidx : master.stops) {
                double depth = master.stopDepth.get(index);
                int stopTime = ToolBox.parseTime(array[stopidx]);
                if (stopTime > 0) {
                    if (firstStop < 0) {
                        firstStop = index;
                        deco = new TableModelDeco(master.stops.size() - firstStop);
                    }
                    deco.set(index - firstStop, depth, stopTime);
                }
                index++;
            }
        }

        public String toString() {
            return depth + "/" + timeInSeconds;
        }

        @Override
        public int compareTo(Integer o) {
            return (timeInSeconds > o ? 1 : (timeInSeconds < o ? -1 : 0));
        }
    }

    /**
     * 
     * @author pmelet
     * 
     */
    private static class Master {

        public int time = -1;

        public int depth = -1;

        public List<Integer> stops = new ArrayList<Integer>();

        public List<Double> stopDepth = new ArrayList<Double>();

        public Master(String[] array) {
            for (int idx = 0; idx < array.length; idx++) {
                if (array[idx].compareTo("depth") == 0) {
                    depth = idx;
                } else if (array[idx].compareTo("time") == 0) {
                    time = idx;
                } else {
                    try {
                        stopDepth.add(Double.parseDouble(array[idx]));
                        stops.add(idx);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

        public boolean isValid() {
            return time >= 0 && depth >= 0;
        }
    }

    @Override
    public double getSpeed(double fromd, double tod, boolean interstops) {
        if (fromd > tod) {
            return (interstops ? this.stopSpeed : this.ascentSpeed) * DiveParameters.getDefault().getAtmPression();
        } else {
            return this.descentSpeed * DiveParameters.getDefault().getAtmPression();
        }
    }

    @Override
    protected List<GazMixUsage> getGazMixes() {
        List<GazMixUsage> list = new LinkedList<GazMixUsage>();
        list.add(new GazMixUsage(GazMix.AIR_20));
        return list;
    }
}
