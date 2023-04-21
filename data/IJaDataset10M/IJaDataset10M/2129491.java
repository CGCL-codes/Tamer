package su.nsk.inp.roentgen.model.hierarchy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import su.nsk.inp.roentgen.model.HierarchicalObject;

@Entity
@Table(name = "examination_area")
public class ExaminationArea extends HierarchicalObject {

    private static final long serialVersionUID = 1L;

    private String _equivDICOM;

    private Float _zivertCoefficient = 1F;

    @Column(name = "isleaf", nullable = false)
    public boolean isLeaf() {
        return _parent == null;
    }

    @Column(name = "DICOM_equiv")
    public String getEquivDICOM() {
        return _equivDICOM;
    }

    @Column(name = "zivert_coeff")
    public Float getZivertCoefficient() {
        return _zivertCoefficient;
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return _id;
    }

    @Override
    @Column(length = 20, nullable = false)
    public String getName() {
        return _name;
    }

    public void setLeaf(boolean leaf) {
    }

    public void setEquivDICOM(String equivDICOM) {
        _equivDICOM = equivDICOM;
    }

    public void setZivertCoefficient(Float zivertCoefficient) {
        _zivertCoefficient = zivertCoefficient;
    }

    @Override
    @ManyToOne()
    @JoinColumn(name = "parent_id")
    public ExaminationArea getParent() {
        return (ExaminationArea) _parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExaminationArea)) {
            return false;
        }
        final ExaminationArea area = (ExaminationArea) o;
        return !(_name != null ? !_name.equals(area._name) : area._name != null) && !(_parent != null ? !_parent.equals(area._parent) : area._parent != null);
    }
}
