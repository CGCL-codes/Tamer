package org.kwantu.m2.model.ui;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author siviwe
 */
@Entity
public class KwantuFilterRestriction extends AbstractFilterRestriction {

    private KwantuTable owningKwantuTable;

    public KwantuFilterRestriction() {
    }

    @Override
    @ManyToOne
    public KwantuTable getOwningKwantuTable() {
        return owningKwantuTable;
    }

    public void setOwningKwantuTable(KwantuTable owningKwantuTable) {
        this.owningKwantuTable = owningKwantuTable;
    }
}
