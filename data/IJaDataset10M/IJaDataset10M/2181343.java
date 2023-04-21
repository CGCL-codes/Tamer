package ch.skyguide.tools.requirement.data;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;

public class AbstractVersion implements Serializable {

    private static final long serialVersionUID = 6091485357502388645L;

    private String name;

    private Date plannedDate;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    public Date getPlannedDate() {
        return plannedDate;
    }

    public void setPlannedDate(Date _plannedDate) {
        plannedDate = _plannedDate;
    }

    @Override
    public String toString() {
        return name;
    }

    @SuppressWarnings("unused")
    @XmlID
    @XmlAttribute
    private String getXmlId() {
        return XmlIdHelper.getXmlId(this);
    }
}
