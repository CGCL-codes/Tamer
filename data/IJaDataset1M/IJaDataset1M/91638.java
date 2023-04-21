package pub.beans.factories;

import pub.beans.*;
import java.sql.*;
import pub.utils.Log;
import pub.utils.MapUtils;
import java.util.*;

public class GeneAlleleLinkingBeanFactory implements BeanMakerI {

    public PubBeanI getBean(pub.db.PubConnection conn, String primary_id) {
        String query = " SELECT pub_gene_allele.*, pub_gene.name as pub_gene_name, " + " pub_allele.name as pub_allele_name " + " from pub_gene_allele, pub_gene, pub_allele  " + " where pub_gene_allele.id = ? and " + " pub_gene_allele.pub_gene_id = pub_gene.id  and " + " pub_gene_allele.pub_allele_id = pub_allele.id  ";
        try {
            java.sql.PreparedStatement statement = conn.prepareStatement(query);
            try {
                statement.setString(1, primary_id);
                java.sql.ResultSet rs = statement.executeQuery();
                if (rs.next() == false) {
                    return new NullGeneAlleleLinkingBean();
                }
                Map map = MapUtils.fetchRow(rs);
                return new GeneAlleleLinkingBeanImpl(map, conn);
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Class getBeanClass() {
        return pub.beans.GeneAlleleLinkingBean.class;
    }
}
