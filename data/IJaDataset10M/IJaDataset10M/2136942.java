package net.sf.webwarp.modules.permission.impl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import net.sf.webwarp.modules.permission.ObjectType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Proxy;
import net.sf.webwarp.util.hibernate.dao.impl.AIDTypeImpl;

@Entity
@Table(name = "ObjectType")
@Proxy(lazy = false)
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@NamedQuery(name = "ObjectType.byName", query = "from ObjectTypeImpl p where p.name = :typeName")
public class ObjectTypeImpl extends AIDTypeImpl<Integer> implements ObjectType {

    private static final long serialVersionUID = 8137655482589064562L;

    private String name;

    private String description;

    @Override
    @Id
    @GeneratedValue(generator = "autoGen")
    @GenericGenerator(name = "autoGen", strategy = "native", parameters = { @Parameter(name = "sequence", value = "objectGroup_seq") })
    public Integer getId() {
        return id;
    }

    @Column(unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }
}
