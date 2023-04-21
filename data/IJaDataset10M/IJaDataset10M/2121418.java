package pub.db.command;

import pub.beans.*;
import pub.db.*;
import java.util.*;
import java.sql.*;

public class AddGeneAlleleLinking {

    private PubConnection conn;

    private GeneBean geneBean;

    private AlleleBean alleleBean;

    private UserBean updatedBy;

    private String relationship_type;

    private String gene_feature_site;

    private String id;

    public AddGeneAlleleLinking(PubConnection conn) {
        this.conn = conn;
        this.geneBean = new NullGeneBean();
        this.alleleBean = null;
        this.updatedBy = new NullUserBean();
        this.relationship_type = "is_an_allele_of";
        this.gene_feature_site = "unknown";
    }

    public String getGeneAlleleLinkingId() {
        return id;
    }

    public void setGeneBean(GeneBean geneBean) {
        this.geneBean = geneBean;
    }

    public void setAlleleBean(AlleleBean alleleBean) {
        this.alleleBean = alleleBean;
    }

    public void setUpdatedBy(UserBean updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationship_type = relationshipType;
    }

    public void setGeneFeatureSite(String geneFeatureSite) {
        this.gene_feature_site = geneFeatureSite;
    }

    public void execute() {
        try {
            checkValidState();
            if (isDuplicated() == false) {
                doSqlInsertion();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkValidState() {
        if (geneBean.isNull()) {
            throw new IllegalStateException("geneBean cannot be null");
        }
        if (alleleBean.isNull()) {
            throw new IllegalStateException("alleleBean cannot be null");
        }
        if (updatedBy.isNull()) {
            throw new IllegalStateException("updatedBy cannot be null");
        }
    }

    private boolean isDuplicated() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select pub_gene_allele.id from pub_gene_allele " + " where pub_gene_id = ? and pub_allele_id = ? " + " and is_obsolete ='n' ");
        try {
            stmt.setString(1, geneBean.getPub_gene_id());
            stmt.setString(2, alleleBean.getPub_allele_id());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } finally {
            stmt.close();
        }
    }

    private void doSqlInsertion() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("insert into pub_gene_allele (" + " pub_gene_id, pub_allele_id, relationship_type, " + " gene_feature_site, entered_by, updated_by, " + " date_entered, date_updated) " + " values (?, ?, ?, ?, ?, ?, CURDATE(), CURDATE()) ");
        try {
            stmt.setString(1, geneBean.getPub_gene_id());
            stmt.setString(2, alleleBean.getPub_allele_id());
            stmt.setString(3, relationship_type);
            stmt.setString(4, gene_feature_site);
            stmt.setString(5, updatedBy.getUser_id());
            stmt.setString(6, updatedBy.getUser_id());
            System.out.println("new entry inserted " + geneBean.getPub_gene_id() + " and " + alleleBean.getPub_allele_id());
            stmt.executeUpdate();
        } finally {
            stmt.close();
        }
        stmt = conn.prepareStatement("select last_insert_id() from  pub_gene_allele");
        try {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getString(1);
            }
        } finally {
            stmt.close();
        }
    }
}
