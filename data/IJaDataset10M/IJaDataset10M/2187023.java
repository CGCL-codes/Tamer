package pub.db;

import pub.beans.*;
import pub.utils.*;
import java.sql.*;
import java.util.*;

/**
 * Annotation table to write one annotation entry to DB table term_annotation
 */
public class AnnotationTable extends GeneralDB {

    public AnnotationTable(pub.db.PubConnection conn) {
        super(conn, "pub_termannotation", "id");
    }

    /**
     * Given an annotation_id, returns the description of the
     * relationship between subject and object terms.  */
    public String getRelationshipDescription(String annotation_id) throws SQLException {
        GeneralDB db = new GeneralDB(conn, "pub_relationshiptype", "id");
        String type_id = getAttribute(annotation_id, "relationship_type_id");
        return db.getAttribute(type_id, "description");
    }

    /**
     * Given an annotation id, returns the evidence code associated
     * with that evidence.*/
    public String getEvidenceCode(String annotation_id) throws SQLException {
        GeneralDB db = new GeneralDB(conn, "pub_evidencetype", "id");
        String type_id = getAttribute(annotation_id, "evidence_type_id");
        return db.getAttribute(type_id, "evidence_code");
    }

    /**
     * Returns a list of annotation ids with a given subject_id.  */
    public List getAnnotationIdsFromSubjectId(String subject_term_id) throws SQLException {
        return searchIds("subject_term_id=? " + " and is_obsolete ='n'", subject_term_id);
    }

    /**
     * Returns a list of annotation ids with a given object term id. */
    public List getAnnotationIdsFromObjectId(String object_term_id) throws SQLException {
        return searchIds("object_term_id=?" + " and is_obsolete ='n'", object_term_id);
    }

    /**
     * Returns a list of annotation ids with the given article_id. */
    public List getAnnotationIdsFromArticleId(String article_id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select annot.id " + " from pub_termannotation annot, pub_reference ref" + " where annot.pub_reference_id = ref.id" + " and ref.table_id = ?" + " and ref.table_name = 'pub_article' " + " and annot.is_obsolete= 'n'");
        try {
            stmt.setString(1, article_id);
            ResultSet rs = stmt.executeQuery();
            List ids = new ArrayList();
            while (rs.next()) {
                ids.add(rs.getString(1));
            }
            return ids;
        } finally {
            stmt.close();
        }
    }

    /** Obsoletes a given annotation */
    public void obsoleteAnnotation(String annotation_id, String obsoleted_by) {
        AnnotationBean annotBean = BeanFactory.getAnnotationBean(conn, annotation_id);
        annotBean.setIsObsolete(true, BeanFactory.getUserBean(conn, obsoleted_by));
        annotBean.store(conn);
    }

    /** Unobsoletes an annotation */
    public void unobsoleteAnnotation(String annotation_id, String user_id) {
        AnnotationBean annotBean = BeanFactory.getAnnotationBean(conn, annotation_id);
        annotBean.setIsObsolete(false, BeanFactory.getUserBean(conn, user_id));
        annotBean.store(conn);
    }

    /** 
     * Migrates non-obsolete annotations from this gene to the
     * replaced by, treated as subject term.  gene_id and replaced_by
     * are both gene ids.
     */
    public void migrateGeneAnnotationsToAnotherGene(int gene_id, int replaced_by, int user_id) {
        pub.beans.GeneBean oldGeneBean = BeanFactory.getGeneBean(conn, "" + gene_id);
        pub.beans.GeneBean newGeneBean = BeanFactory.getGeneBean(conn, "" + replaced_by);
        pub.beans.UserBean userBean = BeanFactory.getUserBean(conn, "" + user_id);
        List annotations = oldGeneBean.getAnnotationBeans();
        for (int i = 0; i < annotations.size(); i++) {
            AnnotationBean annotBean = (AnnotationBean) annotations.get(i);
            annotBean.setSubjectTerm(newGeneBean.getTermBean(), userBean);
            annotBean.store(conn);
        }
    }

    /**
     * Obsoletes all the annotations to this subject term.
     */
    public void obsoleteAnnotationsToSubjectTerm(int subject_term_id, int user_id) {
        TermBean termBean = BeanFactory.getTermBean(conn, "" + subject_term_id);
        List annotationBeans = termBean.getSubjectAnnotationBeans();
        UserBean userBean = BeanFactory.getUserBean(conn, "" + user_id);
        for (int i = 0; i < annotationBeans.size(); i++) {
            AnnotationBean annotBean = (AnnotationBean) annotationBeans.get(i);
            annotBean.setIsObsolete(true, userBean);
            annotBean.store(conn);
        }
    }

    /**
     * Obsoletes annotations to this object term.
     */
    public void obsoleteAnnotationsToObjectTerm(int object_term_id, int user_id) {
        TermBean termBean = BeanFactory.getTermBean(conn, "" + object_term_id);
        List annotationBeans = termBean.getObjectAnnotationBeans();
        UserBean userBean = BeanFactory.getUserBean(conn, "" + user_id);
        for (int i = 0; i < annotationBeans.size(); i++) {
            AnnotationBean annotBean = (AnnotationBean) annotationBeans.get(i);
            annotBean.setIsObsolete(true, userBean);
            annotBean.store(conn);
        }
    }
}
