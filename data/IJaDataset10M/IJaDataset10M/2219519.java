package pe.com.bn.sach.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Bnchf27GiroActividad entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Bnchf27GiroActividad implements java.io.Serializable {

    private Long f27IdGiro;

    private String f27DescGiro;

    private Long f27StdoGiro;

    private String f27IdUsuaCrea;

    private Date f27FeUsuaCrea;

    private String f27IdUsuaModi;

    private Date f27FeUsuaModi;

    private Set bnchf23ExpIngresoFamiliars = new HashSet(0);

    private Set bnchf23ExpIngresoTitulars = new HashSet(0);

    /** default constructor */
    public Bnchf27GiroActividad() {
    }

    /** minimal constructor */
    public Bnchf27GiroActividad(Long f27IdGiro, String f27DescGiro, Long f27StdoGiro, String f27IdUsuaCrea, Date f27FeUsuaCrea) {
        this.f27IdGiro = f27IdGiro;
        this.f27DescGiro = f27DescGiro;
        this.f27StdoGiro = f27StdoGiro;
        this.f27IdUsuaCrea = f27IdUsuaCrea;
        this.f27FeUsuaCrea = f27FeUsuaCrea;
    }

    /** full constructor */
    public Bnchf27GiroActividad(Long f27IdGiro, String f27DescGiro, Long f27StdoGiro, String f27IdUsuaCrea, Date f27FeUsuaCrea, String f27IdUsuaModi, Date f27FeUsuaModi, Set bnchf23ExpIngresoFamiliars, Set bnchf23ExpIngresoTitulars) {
        this.f27IdGiro = f27IdGiro;
        this.f27DescGiro = f27DescGiro;
        this.f27StdoGiro = f27StdoGiro;
        this.f27IdUsuaCrea = f27IdUsuaCrea;
        this.f27FeUsuaCrea = f27FeUsuaCrea;
        this.f27IdUsuaModi = f27IdUsuaModi;
        this.f27FeUsuaModi = f27FeUsuaModi;
        this.bnchf23ExpIngresoFamiliars = bnchf23ExpIngresoFamiliars;
        this.bnchf23ExpIngresoTitulars = bnchf23ExpIngresoTitulars;
    }

    public Long getF27IdGiro() {
        return this.f27IdGiro;
    }

    public void setF27IdGiro(Long f27IdGiro) {
        this.f27IdGiro = f27IdGiro;
    }

    public String getF27DescGiro() {
        return this.f27DescGiro;
    }

    public void setF27DescGiro(String f27DescGiro) {
        this.f27DescGiro = f27DescGiro;
    }

    public Long getF27StdoGiro() {
        return this.f27StdoGiro;
    }

    public void setF27StdoGiro(Long f27StdoGiro) {
        this.f27StdoGiro = f27StdoGiro;
    }

    public String getF27IdUsuaCrea() {
        return this.f27IdUsuaCrea;
    }

    public void setF27IdUsuaCrea(String f27IdUsuaCrea) {
        this.f27IdUsuaCrea = f27IdUsuaCrea;
    }

    public Date getF27FeUsuaCrea() {
        return this.f27FeUsuaCrea;
    }

    public void setF27FeUsuaCrea(Date f27FeUsuaCrea) {
        this.f27FeUsuaCrea = f27FeUsuaCrea;
    }

    public String getF27IdUsuaModi() {
        return this.f27IdUsuaModi;
    }

    public void setF27IdUsuaModi(String f27IdUsuaModi) {
        this.f27IdUsuaModi = f27IdUsuaModi;
    }

    public Date getF27FeUsuaModi() {
        return this.f27FeUsuaModi;
    }

    public void setF27FeUsuaModi(Date f27FeUsuaModi) {
        this.f27FeUsuaModi = f27FeUsuaModi;
    }

    public Set getBnchf23ExpIngresoFamiliars() {
        return this.bnchf23ExpIngresoFamiliars;
    }

    public void setBnchf23ExpIngresoFamiliars(Set bnchf23ExpIngresoFamiliars) {
        this.bnchf23ExpIngresoFamiliars = bnchf23ExpIngresoFamiliars;
    }

    public Set getBnchf23ExpIngresoTitulars() {
        return this.bnchf23ExpIngresoTitulars;
    }

    public void setBnchf23ExpIngresoTitulars(Set bnchf23ExpIngresoTitulars) {
        this.bnchf23ExpIngresoTitulars = bnchf23ExpIngresoTitulars;
    }
}
