package pub.beans;

import pub.beans.factories.*;
import pub.db.*;
import pub.utils.*;
import java.sql.*;
import java.util.*;

public class AnalysisReferenceBean extends SimpleBeanSubfactory implements BeanMakerI, PubBeanI {

    private java.util.HashMap attributes;

    public AnalysisReferenceBean() {
        attributes = new java.util.HashMap();
    }

    public AnalysisReferenceBean(java.util.Map attributes) {
        this();
        this.attributes.putAll(attributes);
    }

    public String toString() {
        return getName();
    }

    public boolean isNull() {
        return false;
    }

    public Class getBeanClass() {
        return AnalysisReferenceBean.class;
    }

    protected PubBeanI getBeanHelper(pub.db.PubConnection conn, String id) {
        String query = "SELECT pub_analysisreference.*, pub_reference.id as pub_reference_id " + " FROM pub_analysisreference, pub_reference " + " WHERE pub_analysisreference.id=? " + " AND pub_analysisreference.id = pub_reference.table_id " + " AND pub_reference.table_name = 'pub_analysisreference' ";
        try {
            java.sql.PreparedStatement statement = conn.prepareStatement(query);
            try {
                statement.setString(1, id);
                java.sql.ResultSet rs = statement.executeQuery();
                if (rs.next() == false) {
                    return new NullAnalysisReferenceBean();
                }
                Map map = MapUtils.fetchRow(rs);
                return new AnalysisReferenceBean(map);
            } finally {
                statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return (String) (this.attributes.get("name"));
    }

    public String getPubAccession() {
        return "REFERENCE:" + getPubReferenceId();
    }

    public void store(PubConnection conn) {
    }

    public String getShortName() {
        String name = (String) (this.attributes.get("name"));
        if (name.length() > 15) {
            return name.substring(0, 15) + "...";
        } else {
            return name;
        }
    }

    public String getId() {
        return (String) (this.attributes.get("pub_analysisreference.id"));
    }

    public String getParameters() {
        return (String) (this.attributes.get("parameters"));
    }

    public String getPubReferenceId() {
        return (String) (this.attributes.get("pub_reference_id"));
    }

    public String getOutput_def_criteria() {
        return (String) (this.attributes.get("output_def_criteria"));
    }

    public String getIs_nucleotide() {
        return (String) (this.attributes.get("is_nucleotide"));
    }

    public String getIs_peptide() {
        return (String) (this.attributes.get("is_peptide"));
    }

    public void setAttribute(String attr, String value) {
        this.attributes.put(attr, value);
    }

    public class NullAnalysisReferenceBean extends AnalysisReferenceBean {

        public boolean isNull() {
            return true;
        }

        public String getName() {
            return "NullAnalysisReferenceBean";
        }

        public String getShortName() {
            return getName();
        }

        public String getId() {
            return "0";
        }

        public String getParameters() {
            return "";
        }

        public String getOutput_def_criteria() {
            return "";
        }

        public String getIs_nucleotide() {
            return "n";
        }

        public String getIs_peptide() {
            return "n";
        }
    }
}
