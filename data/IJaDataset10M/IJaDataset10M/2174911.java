package com.uk.data.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity()
@Table(name = "BASHKI_KOMUNA")
public class BashkiKomuna implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4708811570641845831L;

    @Id
    private Integer id;

    private String tag;

    @ManyToOne
    private Rrethi rrethi;

    public BashkiKomuna() {
        super();
    }

    public BashkiKomuna(Integer id, String tag) {
        super();
        this.id = id;
        this.tag = tag;
        this.rrethi = rrethi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Rrethi getRrethi() {
        return rrethi;
    }

    public void setRrethi(Rrethi rrethi) {
        this.rrethi = rrethi;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((rrethi == null) ? 0 : rrethi.hashCode());
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BashkiKomuna other = (BashkiKomuna) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (rrethi == null) {
            if (other.rrethi != null) return false;
        } else if (!rrethi.equals(other.rrethi)) return false;
        if (tag == null) {
            if (other.tag != null) return false;
        } else if (!tag.equals(other.tag)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BashkiKomuna [id=");
        builder.append(id);
        builder.append(", tag=");
        builder.append(tag);
        builder.append(", rrethi=");
        builder.append(rrethi);
        builder.append("]");
        return builder.toString();
    }
}
