package pub.servlets.update.propagate_annotation;

import pub.beans.*;
import pub.db.*;
import pub.servlets.*;
import pub.utils.*;
import pub.servlets.update.*;
import java.io.*;
import java.util.*;

public class PropagateAnnotationsToTerms {

    /**
     Constructs a new instance of this class
  */
    public PropagateAnnotationsToTerms() {
        annotations = new ArrayList();
    }

    public void setAnnotations(List annotations) {
        this.annotations = annotations;
    }

    /**
     Returns the list of annotations
     @return the list of annotations
  */
    public List getAnnotations() {
        return annotations;
    }

    /**
     Sets the connection to the pub database
     @param pc the connection to the pub database
  */
    public void setPubConnection(PubConnection pc) {
        conn = pc;
    }

    /**
     Sets the ID of the user making the change.
     @param uid ID of the user making the change.
  */
    public void setUserId(int uid) {
        userId = uid;
    }

    /**
     Sets the list of terms to propagate the annotation(s) to
     @param terms the List<String> of terms
  */
    public void setTerms(List terms) {
        this.terms = terms;
    }

    /**
     Returns the list of terms.
     @return the list of terms.
  */
    public List getTerms() {
        return terms;
    }

    /**
    Propagates annotations to the list of terms
  */
    public List propagate() {
        if (terms == null || terms.size() < 1 || annotations == null || annotations.size() < 1) {
            throw new RuntimeException("propagate annotation error: " + "the term and annotation lists cannot be null or empty.");
        }
        ArrayList annotation_ids = new ArrayList();
        for (int i = 0; i < annotations.size(); i++) {
            for (int j = 0; j < terms.size(); j++) {
                int annotation_id = Integer.parseInt((String) annotations.get(i));
                int term_id = Integer.parseInt((String) terms.get(j));
                String new_annotation = propagateAnnotationtoTerm(annotation_id, term_id, userId);
                if (!StringUtils.isEmpty(new_annotation)) {
                    annotation_ids.add(new_annotation);
                }
            }
        }
        return annotation_ids;
    }

    /**
     Propagates an annotation to a term.
     @param annotion_id the ID of the annotation
     @param term_id the ID of the term to propagate to
     @param user_id the ID of the user making the change
  */
    private String propagateAnnotationtoTerm(int annotation_id, int term_id, int user_id) {
        AnnotationBean annotation = pub.beans.BeanFactory.getAnnotationBean(conn, "" + annotation_id);
        pub.db.command.AddAnnotation add = CloneAnnotation.makeAddAnnotationCommand(conn, annotation_id, user_id);
        new pub.db.command.AddAnnotation(conn);
        add.setSubjectTermId(Integer.parseInt(annotation.getSubjectTermId()));
        add.setObjectTermId(term_id);
        add.initialize();
        add.execute();
        return "" + add.getAnnotationId();
    }

    private List annotations;

    private List terms;

    private int userId;

    private PubConnection conn;
}
