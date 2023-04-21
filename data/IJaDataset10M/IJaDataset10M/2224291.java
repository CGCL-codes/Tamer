package com.cosmos.acacia.crm.data.assembling;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Miro
 */
@Entity
@Table(name = "assembling_algorithms")
@NamedQueries({ @NamedQuery(name = "AssemblingAlgorithm.findByAlgorithmCode", query = "select aa from AssemblingAlgorithm aa where aa.algorithmCode = :algorithmCode") })
public class AssemblingAlgorithm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "AssemblingAlgorithmsSequenceGenerator", sequenceName = "assembling_algorithms_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AssemblingAlgorithmsSequenceGenerator")
    @Column(name = "algorithm_id", nullable = false)
    private Integer algorithmId;

    @Column(name = "algorithm_code", nullable = false)
    private String algorithmCode;

    @Column(name = "algorithm_name", nullable = false)
    private String algorithmName;

    @Column(name = "description")
    private String description;

    public AssemblingAlgorithm() {
    }

    public AssemblingAlgorithm(Integer algorithmId) {
        this.algorithmId = algorithmId;
    }

    public Integer getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(Integer algorithmId) {
        this.algorithmId = algorithmId;
    }

    public String getAlgorithmCode() {
        return algorithmCode;
    }

    public void setAlgorithmCode(String algorithmCode) {
        this.algorithmCode = algorithmCode;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (algorithmId != null ? algorithmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AssemblingAlgorithm)) {
            return false;
        }
        AssemblingAlgorithm other = (AssemblingAlgorithm) object;
        if ((this.algorithmId == null && other.algorithmId != null) || (this.algorithmId != null && !this.algorithmId.equals(other.algorithmId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cosmos.acacia.crm.data.AssemblingAlgorithm[algorithmId=" + algorithmId + "]";
    }
}
