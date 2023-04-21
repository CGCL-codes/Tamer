package edu.unibi.agbi.dawismd.entities.metadata;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "db_statistics")
@Table(name = "db_statistics")
public class DbStatistics implements java.io.Serializable {

    private static final long serialVersionUID = 8324392443588644351L;

    private String statisticsTable;

    private DbData dbData;

    private String dataContent;

    private String subgroup;

    private int entries;

    public DbStatistics() {
    }

    public DbStatistics(String statisticsTable, DbData dbData, int entries) {
        this.statisticsTable = statisticsTable;
        this.dbData = dbData;
        this.entries = entries;
    }

    public DbStatistics(String statisticsTable, DbData dbData, String dataContent, String subgroup, int entries) {
        this.statisticsTable = statisticsTable;
        this.dbData = dbData;
        this.dataContent = dataContent;
        this.subgroup = subgroup;
        this.entries = entries;
    }

    @Id
    @Column(name = "statistics_table", nullable = false, length = 64)
    public String getStatisticsTable() {
        return this.statisticsTable;
    }

    public void setStatisticsTable(String statisticsTable) {
        this.statisticsTable = statisticsTable;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dbname", nullable = false)
    public DbData getDbData() {
        return this.dbData;
    }

    public void setDbData(DbData dbData) {
        this.dbData = dbData;
    }

    @Column(name = "data_content", length = 32)
    public String getDataContent() {
        return this.dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    @Column(name = "subgroup", length = 32)
    public String getSubgroup() {
        return this.subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    @Column(name = "entries", nullable = false)
    public int getEntries() {
        return this.entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }
}
