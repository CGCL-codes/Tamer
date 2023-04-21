package de.jlab.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "snapshot")
public class SnapshotConfig {

    @XmlAttribute(required = true, name = "name")
    String name;

    @XmlElement(name = "snapshot-values")
    List<SnapshotValueConfig> snapshotValues = new ArrayList<SnapshotValueConfig>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SnapshotValueConfig> getSnapshotValues() {
        return snapshotValues;
    }

    public void setSnapshotValues(List<SnapshotValueConfig> values) {
        this.snapshotValues = values;
    }
}
