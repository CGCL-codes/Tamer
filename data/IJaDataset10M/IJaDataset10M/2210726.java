package org.vardb.analysis.dao;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import org.vardb.analysis.IAnalysis;
import org.vardb.util.CStringHelper;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ANALYSIS")
@Table(name = "analyses")
public abstract class CAbstractAnalysis implements IAnalysis {

    protected String id;

    protected String user_id;

    protected IAnalysis.Type dtype;

    protected Date date;

    public CAbstractAnalysis() {
        this.id = CStringHelper.generateID();
        this.date = new Date();
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

    @Column(insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    public IAnalysis.Type getDtype() {
        return this.dtype;
    }

    public void setDtype(final IAnalysis.Type dtype) {
        this.dtype = dtype;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }
}
