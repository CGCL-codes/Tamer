package edu.unibi.agbi.biodwh.entity.mint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Jan_Fuessmann
 * @version 1.0_2010
 */
@Entity(name = "mint_parti_hostorganism_exp_ref")
@Table(name = "mint_parti_hostorganism_exp_ref")
public class MintPartiHostorganismExpRef implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -201728655013094968L;

    private Integer id;

    private MintExp mintExp;

    private MintPartiHostorganism mintPartiHostorganism;

    public MintPartiHostorganismExpRef() {
    }

    public MintPartiHostorganismExpRef(Integer id) {
        this.id = id;
    }

    public MintPartiHostorganismExpRef(Integer id, MintExp mintExp, MintPartiHostorganism mintPartiHostorganism) {
        this.id = id;
        this.mintExp = mintExp;
        this.mintPartiHostorganism = mintPartiHostorganism;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_xml_internal_id", referencedColumnName = "xml_internal_id", nullable = false)
    public MintExp getMintExp() {
        return this.mintExp;
    }

    public void setMintExp(MintExp mintExp) {
        this.mintExp = mintExp;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "participant_id", referencedColumnName = "participant_id", nullable = false), @JoinColumn(name = "biosource_id", referencedColumnName = "biosource_id", nullable = false) })
    public MintPartiHostorganism getMintPartiHostorganism() {
        return this.mintPartiHostorganism;
    }

    public void setMintPartiHostorganism(MintPartiHostorganism mintPartiHostorganism) {
        this.mintPartiHostorganism = mintPartiHostorganism;
    }
}
