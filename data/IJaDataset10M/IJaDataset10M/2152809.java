package org.opennms.netmgt.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.springframework.core.style.ToStringCreator;

/**
 * @author min zhang
 *@hibernate.class table="nodegroup"
 */
@Entity
@Table(name = "nodegroup")
public class RdbmsNodeGroup implements Serializable {

    private static final long serialVersionUID = 3846398168228820087L;

    /** identifier field */
    private Integer m_nodegroupid;

    /** nullable persistent field */
    private Integer m_fkroleid;

    /** nullable persistent field */
    private Integer m_fknodeid;

    /** persistent field */
    private RdbmsRole m_rdbmsRole;

    /** persistent field */
    private OnmsNode m_OnmsNode;

    public RdbmsNodeGroup() {
    }

    public RdbmsNodeGroup(Integer m_nodegroupid, Integer m_roleid, Integer m_nodeid, RdbmsRole role, OnmsNode onmsNode) {
        this.m_nodegroupid = m_nodegroupid;
        this.m_fkroleid = m_roleid;
        this.m_fknodeid = m_nodeid;
        m_rdbmsRole = role;
        m_OnmsNode = onmsNode;
    }

    /**
	 * @return the m_nodegroupid
	 */
    @Id
    @Column(name = "nodegroupid")
    @SequenceGenerator(name = "rdbmsNodeGroupSequence", sequenceName = "rdbmsNodeGroupNxtId")
    @GeneratedValue(generator = "rdbmsNodeGroupSequence")
    public Integer getNodegroupid() {
        return m_nodegroupid;
    }

    /**
	 * @param m_nodegroupid the m_nodegroupid to set
	 */
    public void setNodegroupid(Integer m_nodegroupid) {
        this.m_nodegroupid = m_nodegroupid;
    }

    /**
	 * @return the m_fknodeid
	 */
    @Column(name = "fknodeid", nullable = false)
    public Integer getNodeid() {
        return m_fknodeid;
    }

    /**
	 * @param m_fknodeid the m_fknodeid to set
	 */
    public void setNodeid(Integer m_nodeid) {
        this.m_fknodeid = m_nodeid;
    }

    /**
	 * @return the m_fkroleid
	 */
    @Column(name = "fkroleid", nullable = false)
    public Integer getRoleid() {
        return m_fkroleid;
    }

    /**
	 * @param m_fkroleid the m_fkroleid to set
	 */
    public void setRoleid(Integer m_roleid) {
        this.m_fkroleid = m_roleid;
    }

    /**
	 * @return the m_rdbmsRole
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    public RdbmsRole getRdbmsRole() {
        return m_rdbmsRole;
    }

    /**
	 * @param role the m_rdbmsRole to set
	 */
    public void setRdbmsRole(RdbmsRole role) {
        m_rdbmsRole = role;
    }

    /**
	 * @return the m_OnmsNode
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    public OnmsNode getOnmsNode() {
        return m_OnmsNode;
    }

    /**
	 * @param onmsNode the m_OnmsNode to set
	 */
    public void setOnmsNode(OnmsNode onmsNode) {
        m_OnmsNode = onmsNode;
    }

    public String toString() {
        return new ToStringCreator(this).append("nodegroupid", getNodegroupid()).append("fkroleid", getRoleid()).append("fknodeid", getNodeid()).toString();
    }
}
