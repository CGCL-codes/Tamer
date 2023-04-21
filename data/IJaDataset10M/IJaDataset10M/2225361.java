package de.lema.bo;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import de.lema.bo.idtext.IdTextEnum;

@Entity
@NamedQueries({ @NamedQuery(name = "EventLabelHistoryText.alle", query = "from de.lema.bo.EventLabelHistoryText c"), @NamedQuery(name = "EventLabelHistoryText.byIdText", query = "from de.lema.bo.EventLabelHistoryText c where c.labelId=:labelId and c.idText=:idText and c.value=:value"), @NamedQuery(name = "EventLabelHistoryText.byLabel", query = "from de.lema.bo.EventLabelHistoryText c where c.labelId=:labelId order by c.idText, c.value") })
@PersistenceUnits(@PersistenceUnit(unitName = "lema"))
public class EventLabelHistoryText implements Serializable {

    private static final String SEQ_NAME = "EventLabelHistoryText_SEQ";

    private static final long serialVersionUID = 1L;

    private int count;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, initialValue = 1, allocationSize = 1)
    private Integer id;

    private int idText;

    private long labelId;

    private long value;

    @Transient
    private transient int countAktive;

    public int getCountAktive() {
        return countAktive;
    }

    public void setCountAktive(int countAktive) {
        this.countAktive = countAktive;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + count;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + idText;
        result = prime * result + (int) (labelId ^ (labelId >>> 32));
        result = prime * result + (int) (value ^ (value >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EventLabelHistoryText other = (EventLabelHistoryText) obj;
        if (count != other.count) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (idText != other.idText) return false;
        if (labelId != other.labelId) return false;
        if (value != other.value) return false;
        return true;
    }

    public int getCount() {
        return count;
    }

    public Integer getId() {
        return id;
    }

    public int getIdText() {
        return idText;
    }

    public IdTextEnum getIdTextEnum() {
        return IdTextEnum.get(idText);
    }

    public long getLabelId() {
        return labelId;
    }

    public long getValue() {
        return value;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdText(int idText) {
        this.idText = idText;
    }

    public void setIdTextEnum(IdTextEnum e) {
        idText = e.convert();
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EventLabelHistoryText [id=" + id + ", labelId=" + labelId + ", idText=" + idText + ", value=" + value + ", count=" + count + "]";
    }
}
