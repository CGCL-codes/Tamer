package org.blueoxygen.brigade.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.blueoxygen.cimande.DefaultPersistence;

/**
 * @author kurniawan
 *
 */
@Entity()
@Table(name = "brigade_material")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Material extends DefaultPersistence {

    private Module module;

    private Material parent;

    private String indexMaterial;

    private String url;

    private String name;

    private String objective;

    private String release;

    private String version;

    private String edition;

    private List<ExamForm> examForms = new ArrayList<ExamForm>();

    private List<GradeBook> gradeBooks = new ArrayList<GradeBook>();

    @OneToMany(mappedBy = "material")
    public List<GradeBook> getGradeBooks() {
        return gradeBooks;
    }

    public void setGradeBooks(List<GradeBook> gradeBooks) {
        this.gradeBooks = gradeBooks;
    }

    @OneToMany(mappedBy = "material")
    public List<ExamForm> getExamForms() {
        return examForms;
    }

    public void setExamForms(List<ExamForm> examForms) {
        this.examForms = examForms;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    @ManyToOne()
    @JoinColumn(name = "module_id")
    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @ManyToOne()
    @JoinColumn(name = "parent_id")
    public Material getParent() {
        return parent;
    }

    public void setParent(Material parent) {
        this.parent = parent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "objective", length = 1000)
    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    @Column(name = "release_date")
    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIndexMaterial() {
        return indexMaterial;
    }

    public void setIndexMaterial(String indexMaterial) {
        this.indexMaterial = indexMaterial;
    }
}
