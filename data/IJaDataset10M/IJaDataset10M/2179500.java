package pub.servlets.update.propagate_annotation;

import pub.beans.*;
import pub.db.*;
import pub.servlets.*;
import pub.utils.*;
import pub.servlets.update.*;
import java.io.*;
import java.util.*;

/** this class propagates a list of annotations to a list of genes */
public class PropagateAnnotationsToGenes {

    private List annotations;

    private List genes;

    private int userId;

    private PubConnection conn;

    public PropagateAnnotationsToGenes() {
        annotations = new ArrayList();
        genes = new ArrayList();
    }

    public void setAnnotations(List annotations) {
        if (annotations != null && annotations.size() > 0) {
            this.annotations = annotations;
        }
    }

    public void setGenes(List genes) {
        if (genes != null && genes.size() > 0) {
            this.genes = genes;
        }
    }

    public void setUserId(int user) {
        this.userId = user;
    }

    public void setPubConnection(PubConnection conn) {
        this.conn = conn;
    }

    public List getAnnotations() {
        return annotations;
    }

    public List getGenes() {
        return genes;
    }

    /**
     * propagate the annotations to the list of genes
     * return the newly added OR updated annotation_ids as List of string
     */
    public List propagate() {
        ArrayList annotation_ids = new ArrayList();
        if (genes == null || genes.size() < 1 || annotations == null || annotations.size() < 1) {
            throw new RuntimeException("propagate annotation error: " + "the gene list or annotation list mustn't be null");
        }
        for (int annIndex = 0; annIndex < annotations.size(); annIndex++) {
            String annotation = (String) (annotations.get(annIndex));
            int annotation_id = Integer.parseInt(annotation);
            for (int genIndex = 0; genIndex < genes.size(); genIndex++) {
                String gene_term_id = (String) (genes.get(genIndex));
                int subject_term_id = Integer.parseInt(gene_term_id);
                String new_annotation = propagateAnnotationToGene(annotation_id, subject_term_id, userId);
                if (!StringUtils.isEmpty(new_annotation)) {
                    annotation_ids.add(new_annotation);
                }
            }
        }
        return annotation_ids;
    }

    /**
     * Propogate one annotation to another gene
     * return the newly added annotation_id 
     */
    private String propagateAnnotationToGene(int annotation_id, int subject_term_id, int user_id) {
        AnnotationBean annotation = pub.beans.BeanFactory.getAnnotationBean(conn, "" + annotation_id);
        pub.db.command.AddAnnotation add = CloneAnnotation.makeAddAnnotationCommand(conn, annotation_id, user_id);
        add.setSubjectTermId(subject_term_id);
        add.setObjectTermId(Integer.parseInt(annotation.getObjectTermId()));
        add.setMarkAnnotationTask(true);
        add.initialize();
        add.execute();
        return "" + add.getAnnotationId();
    }

    public String toString() {
        return "annotationsize" + annotations.size() + " gene size" + genes.size();
    }
}
