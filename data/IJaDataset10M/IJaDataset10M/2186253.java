package edu.unibi.agbi.biodwh.entity.jaspar;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "jaspar_matrix_annotation")
@Table(name = "jaspar_matrix_annotation")
public class JasparMatrixAnnotation implements java.io.Serializable {

    private static final long serialVersionUID = 7521927933677231897L;

    private JasparMatrixAnnotationId id;

    private JasparMatrix jasparMatrix;

    private String val;

    public JasparMatrixAnnotation() {
    }

    public JasparMatrixAnnotation(JasparMatrixAnnotationId id, JasparMatrix jasparMatrix, String val) {
        this.id = id;
        this.jasparMatrix = jasparMatrix;
        this.val = val;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "jasparId", column = @Column(name = "jaspar_id", nullable = false)), @AttributeOverride(name = "tag", column = @Column(name = "tag", nullable = false)) })
    public JasparMatrixAnnotationId getId() {
        return this.id;
    }

    public void setId(JasparMatrixAnnotationId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jaspar_id", nullable = false, insertable = false, updatable = false)
    public JasparMatrix getJasparMatrix() {
        return this.jasparMatrix;
    }

    public void setJasparMatrix(JasparMatrix jasparMatrix) {
        this.jasparMatrix = jasparMatrix;
    }

    @Column(name = "val", nullable = false)
    public String getVal() {
        return this.val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
