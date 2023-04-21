package org.vardb.analysis.dao;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.vardb.analysis.CCodonUsageByColumn;
import org.vardb.analysis.CCodonUsageTable;
import org.vardb.sequences.CSequenceFileParser;
import org.vardb.sequences.CSequenceType;

@Entity
@DiscriminatorValue("CODONUSAGE")
public class CCodonUsageAnalysis extends CAbstractAnalysis {

    protected Params params = new Params();

    protected Results results = new Results();

    public CCodonUsageAnalysis() {
    }

    public CCodonUsageAnalysis(Map<String, String> alignment) {
        super();
        this.params.setFastaAlignment(CSequenceFileParser.writeFasta(alignment));
        this.alignment = alignment;
    }

    @Embedded
    public Params getParams() {
        return this.params;
    }

    public void setParams(final Params params) {
        this.params = params;
    }

    @Embedded
    public Results getResults() {
        return this.results;
    }

    public void setResults(final Results results) {
        this.results = results;
    }

    @Embeddable
    public static class Params {

        protected String fastaAlignment;

        @Column(name = "codonusage_params_fastaalignment")
        public String getFastaAlignment() {
            return this.fastaAlignment;
        }

        public void setFastaAlignment(final String fastaAlignment) {
            this.fastaAlignment = fastaAlignment;
        }
    }

    @Embeddable
    public static class Results {
    }

    protected Map<String, String> alignment;

    protected CCodonUsageTable codonUsageTable;

    protected CCodonUsageByColumn codonUsageByColumn;

    @Transient
    public Map<String, String> getAlignment() {
        if (this.alignment == null) this.alignment = CSequenceFileParser.readFastaAlignment(this.params.getFastaAlignment(), CSequenceType.NT);
        return this.alignment;
    }

    @Transient
    public CCodonUsageTable getCodonUsageTable() {
        if (this.codonUsageTable == null) this.codonUsageTable = new CCodonUsageTable(getAlignment().values());
        return this.codonUsageTable;
    }

    @Transient
    public CCodonUsageByColumn getCodonUsageByColumn() {
        if (this.codonUsageByColumn == null) this.codonUsageByColumn = new CCodonUsageByColumn(getAlignment().values());
        return this.codonUsageByColumn;
    }
}
