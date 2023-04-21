package com.googlecode.hyperrecord.view;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.googlecode.hyperrecord.dao.hibernate.View;

@Entity
@Table(name = "XXCO_V_ALIASES")
public class PersistentAlias {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "VIEW_ID")
    private View view;

    private String associationName;

    private String aliasName;

    public PersistentAlias() {
        super();
    }

    public PersistentAlias(View view, String associationName, String aliasName) {
        this.view = view;
        this.associationName = associationName;
        this.aliasName = aliasName;
    }

    public PersistentAlias clone() {
        return new PersistentAlias(null, associationName, aliasName);
    }

    public void render(MetaCriteria crit) {
        crit.createAlias(associationName, aliasName);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Long getId() {
        return id;
    }

    public String getAssociationName() {
        return associationName;
    }

    public void setAssociationName(String associationName) {
        this.associationName = associationName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
}
