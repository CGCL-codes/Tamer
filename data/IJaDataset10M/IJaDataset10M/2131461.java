package com.ibm.tuningfork.systemtap;

import java.util.ArrayList;
import java.util.Collections;
import com.ibm.tuningfork.core.configuration.FigureConfiguration;
import com.ibm.tuningfork.core.figure.CategoryDescriptor;
import com.ibm.tuningfork.core.graphics.ICategoryMapper;
import com.ibm.tuningfork.core.graphics.RGBColor;
import com.ibm.tuningfork.infra.data.IStringMapRelation;
import com.ibm.tuningfork.infra.data.TimeInterval;
import com.ibm.tuningfork.infra.event.IEvent;
import com.ibm.tuningfork.infra.stream.core.Stream;
import com.ibm.tuningfork.infra.streambundle.StreamBundle;

public class ThreadColorMapper implements ICategoryMapper {

    protected String processName;

    protected int[] pidList = new int[PID_MAP_SIZE];

    protected CategoryDescriptor[] catList = new CategoryDescriptor[PID_MAP_SIZE];

    int entries;

    protected CategoryDescriptor irqCategory = new CategoryDescriptor("Interrupts", RGBColor.lookup("black"));

    protected CategoryDescriptor otherCategory = new CategoryDescriptor("Other", RGBColor.lookup("lightGray"));

    protected final RGBColor[] colors;

    protected int nextColor;

    protected static final int PID_MAP_SIZE = 1024;

    protected static final int NO_PID = Integer.MIN_VALUE;

    public ThreadColorMapper(String command) {
        this.processName = command;
        this.colors = FigureConfiguration.COLOR_DEFAULTS;
        clear();
    }

    public String getName() {
        return "Color by thread for " + processName + " processes";
    }

    public void changeMapping(String categoryName, CategoryDescriptor descriptor) {
        if (categoryName.equals(irqCategory.name)) {
            irqCategory = descriptor;
        } else if (categoryName.equals(otherCategory.name)) {
            otherCategory = descriptor;
        } else {
            catList[getIndexByName(categoryName)] = descriptor;
        }
    }

    public void clear() {
        nextColor = 0;
        for (int i = 0; i < PID_MAP_SIZE; i++) {
            pidList[i] = NO_PID;
        }
        entries = 0;
    }

    public int getCategoryCount() {
        return entries + 2;
    }

    public CategoryDescriptor[] getCategoryDescriptors() {
        ArrayList<CategoryDescriptor> descriptors = new ArrayList<CategoryDescriptor>();
        for (int i = 0; i < PID_MAP_SIZE; i++) {
            if (pidList[i] != NO_PID) {
                descriptors.add(catList[i]);
            }
        }
        Collections.sort(descriptors);
        descriptors.add(0, irqCategory);
        descriptors.add(otherCategory);
        return descriptors.toArray(new CategoryDescriptor[descriptors.size()]);
    }

    protected final int getIndex(int pid) {
        int start = pid % PID_MAP_SIZE;
        int i = start;
        do {
            if (pidList[i] == pid) {
                return i;
            }
            i = (i + 1) % PID_MAP_SIZE;
        } while (i != start);
        return -1;
    }

    protected int getIndexForInsert(int pid) {
        int start = pid % PID_MAP_SIZE;
        for (int i = start; i != (start - 1) % PID_MAP_SIZE; i = (i + 1) % PID_MAP_SIZE) {
            if (pidList[i] == NO_PID || pidList[i] == pid) {
                return i;
            }
        }
        return -1;
    }

    protected int getIndexByName(String name) {
        for (int i = 0; i < PID_MAP_SIZE; i++) {
            if (pidList[i] != NO_PID && catList[i].name.equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public CategoryDescriptor mapToCategory(IEvent event, Stream source) {
        if (event instanceof TimeInterval) {
            TimeInterval interval = (TimeInterval) event;
            IStringMapRelation info = interval.getAnnotation();
            if (info instanceof PidInfo) {
                PidInfo pidinfo = (PidInfo) info;
                int pid = pidinfo.pid;
                if (pid == PidInfo.IRQ_PID) {
                    return irqCategory;
                } else if (pid < 0) {
                    return otherCategory;
                }
                int index = getIndex(pid);
                if (index >= 0) {
                    return catList[index];
                }
                if (pidinfo.command.contains("irq")) {
                    return irqCategory;
                } else if (pidinfo.command.equals(processName)) {
                    return makeNewDescriptor(pidinfo);
                }
            }
        }
        return otherCategory;
    }

    protected CategoryDescriptor makeNewDescriptor(PidInfo info) {
        String threadName = info.threadName;
        int pid = info.pid;
        String name = threadName != null && threadName.length() > 0 ? threadName + " (" + pid + ")" : "TID " + pid;
        CategoryDescriptor descriptor = new CategoryDescriptor(name, colors[nextColor % colors.length]);
        nextColor++;
        int index = getIndexForInsert(pid);
        if (pidList[index] == NO_PID) {
            entries++;
        }
        pidList[index] = pid;
        catList[index] = descriptor;
        return descriptor;
    }

    public RGBColor mapToColor(IEvent event, Stream source) {
        return mapToCategory(event, source).toRGBColor();
    }

    public void addStream(Stream stream) {
    }

    public void addStreams(StreamBundle streams) {
    }
}
