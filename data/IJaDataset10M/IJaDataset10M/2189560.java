package edu.unibi.agbi.dawismd.entities.biodwh.jaspar;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Index;

@Entity(name = "jaspar_matrix")
@Table(name = "jaspar_matrix")
public class JasparMatrix implements java.io.Serializable {

    private static final long serialVersionUID = -6411533627068281104L;

    private int jasparId;

    private String collection;

    private String baseId;

    private int versionId;

    private String name;

    private Set<JasparMatrixData> jasparMatrixDatas = new HashSet<JasparMatrixData>(0);

    private Set<JasparMatrixAnnotation> jasparMatrixAnnotations = new HashSet<JasparMatrixAnnotation>(0);

    private Set<JasparMatrixSpecies> jasparMatrixSpecieses = new HashSet<JasparMatrixSpecies>(0);

    private Set<JasparMatrixProtein> jasparMatrixProteins = new HashSet<JasparMatrixProtein>(0);

    public JasparMatrix() {
    }

    public JasparMatrix(int jasparId, String collection, String baseId, int versionId, String name) {
        this.jasparId = jasparId;
        this.collection = collection;
        this.baseId = baseId;
        this.versionId = versionId;
        this.name = name;
    }

    public JasparMatrix(int jasparId, String collection, String baseId, int versionId, String name, Set<JasparMatrixData> jasparMatrixDatas, Set<JasparMatrixAnnotation> jasparMatrixAnnotations, Set<JasparMatrixSpecies> jasparMatrixSpecieses, Set<JasparMatrixProtein> jasparMatrixProteins) {
        this.jasparId = jasparId;
        this.collection = collection;
        this.baseId = baseId;
        this.versionId = versionId;
        this.name = name;
        this.jasparMatrixDatas = jasparMatrixDatas;
        this.jasparMatrixAnnotations = jasparMatrixAnnotations;
        this.jasparMatrixSpecieses = jasparMatrixSpecieses;
        this.jasparMatrixProteins = jasparMatrixProteins;
    }

    @Id
    @Column(name = "jaspar_id", nullable = false)
    public int getJasparId() {
        return this.jasparId;
    }

    public void setJasparId(int jasparId) {
        this.jasparId = jasparId;
    }

    @Column(name = "collection", nullable = false, length = 16)
    public String getCollection() {
        return this.collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    @Column(name = "base_id", nullable = false, length = 16)
    public String getBaseId() {
        return this.baseId;
    }

    public void setBaseId(String baseId) {
        this.baseId = baseId;
    }

    @Column(name = "version_id", nullable = false)
    public int getVersionId() {
        return this.versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    @Column(name = "name", nullable = false)
    @Index(name = "nameIdx")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jasparMatrix")
    public Set<JasparMatrixData> getJasparMatrixDatas() {
        return this.jasparMatrixDatas;
    }

    public void setJasparMatrixDatas(Set<JasparMatrixData> jasparMatrixDatas) {
        this.jasparMatrixDatas = jasparMatrixDatas;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jasparMatrix")
    public Set<JasparMatrixAnnotation> getJasparMatrixAnnotations() {
        return this.jasparMatrixAnnotations;
    }

    public void setJasparMatrixAnnotations(Set<JasparMatrixAnnotation> jasparMatrixAnnotations) {
        this.jasparMatrixAnnotations = jasparMatrixAnnotations;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jasparMatrix")
    public Set<JasparMatrixSpecies> getJasparMatrixSpecieses() {
        return this.jasparMatrixSpecieses;
    }

    public void setJasparMatrixSpecieses(Set<JasparMatrixSpecies> jasparMatrixSpecieses) {
        this.jasparMatrixSpecieses = jasparMatrixSpecieses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jasparMatrix")
    public Set<JasparMatrixProtein> getJasparMatrixProteins() {
        return this.jasparMatrixProteins;
    }

    public void setJasparMatrixProteins(Set<JasparMatrixProtein> jasparMatrixProteins) {
        this.jasparMatrixProteins = jasparMatrixProteins;
    }
}
