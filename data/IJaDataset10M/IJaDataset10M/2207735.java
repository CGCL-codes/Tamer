package nakayo.gameserver.model.templates.siege;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xitanium
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SiegeReward")
public class SiegeReward {

    @XmlAttribute(name = "grade")
    protected int grade;

    @XmlAttribute(name = "top")
    protected int top;

    @XmlAttribute(name = "itemid")
    protected int itemId;

    @XmlAttribute(name = "count")
    protected int itemCount;

    public int getGrade() {
        return grade;
    }

    public int getTop() {
        return top;
    }

    public int getItemId() {
        return itemId;
    }

    public int getItemCount() {
        return itemCount;
    }
}
