package spidr.webapp;

import wdc.utils.*;
import java.io.*;

public class ElementBean implements Serializable {

    private String key;

    private String group;

    private String name;

    private String table;

    private String description;

    private String label;

    private String units;

    private boolean needStation;

    private boolean hasData;

    private DateInterval dateInterval;

    private String sampling;

    public ElementBean(String key, String group, String name, String table, String description, String label, String units, boolean needStation, boolean hasData, DateInterval dateInterval, String sampling) {
        this.key = key;
        this.group = group;
        this.name = name;
        this.table = table;
        this.description = description;
        this.label = label;
        this.units = units;
        this.needStation = needStation;
        this.hasData = hasData;
        this.dateInterval = dateInterval;
        this.sampling = sampling;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isNeedStation() {
        return needStation;
    }

    public void setNeedStation(boolean needStation) {
        this.needStation = needStation;
    }

    public boolean isHasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }

    public wdc.utils.DateInterval getDateInterval() {
        return dateInterval;
    }

    public void setDateInterval(wdc.utils.DateInterval dateInterval) {
        this.dateInterval = dateInterval;
    }

    public String getSampling() {
        return sampling;
    }

    public void setSampling(String sampling) {
        this.sampling = sampling;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }
}
