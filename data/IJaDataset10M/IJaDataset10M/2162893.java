package icescrum2.dao.model.impl;

import icescrum2.dao.model.ITheme;
import org.hibernate.validator.NotNull;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "icescrum2_Theme")
@XmlRootElement(name = "theme")
public class Theme implements Serializable, ITheme {

    /**
     *
     */
    private static final long serialVersionUID = 7072515028109185168L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer idTheme;

    @ManyToOne
    @JoinColumn(name = "is2product_fk")
    private Product parentProduct;

    @OneToMany(mappedBy = "theme", cascade = { CascadeType.REFRESH })
    @OrderBy(value = "rank, label")
    private List<ProductBacklogItem> pbis = new ArrayList<ProductBacklogItem>();

    @NotNull
    private String name;

    @Column(length = 2000)
    private String description;

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    private String textColor = " ";

    private String color = "#FFFFFF";

    private Integer estimatedPoints = IMPOSSIBLE_POINT;

    public void setIdTheme(Integer idTheme) {
        this.idTheme = idTheme;
    }

    public Integer getIdTheme() {
        return idTheme;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parentProduct == null) ? 0 : parentProduct.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Theme other = (Theme) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (parentProduct == null) {
            if (other.parentProduct != null) return false;
        } else if (!parentProduct.equals(other.parentProduct)) return false;
        return true;
    }

    @XmlTransient
    public Product getParentProduct() {
        return parentProduct;
    }

    public void setParentProduct(Product parentProduct) {
        this.parentProduct = parentProduct;
    }

    public Serializable getId() {
        return this.idTheme;
    }

    public void setEstimatedPoints(Integer estimatedPoints) {
        this.estimatedPoints = estimatedPoints;
    }

    public Integer getEstimatedPoints() {
        return this.estimatedPoints;
    }

    /**
     * @return the pbis
     */
    @XmlTransient
    public List<ProductBacklogItem> getPbis() {
        return pbis;
    }

    @XmlID
    @XmlAttribute(name = "id")
    public String getStringId() {
        return "theme-" + idTheme;
    }

    /**
     * @param pbis the pbis to set
     */
    public void setPbis(List<ProductBacklogItem> pbis) {
        this.pbis = pbis;
    }
}
