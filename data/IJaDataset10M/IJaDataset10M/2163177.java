package org.tolven.ctom.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * An abnormal tissue growth resulted from uncontrolled cell proliferation. Benign
 * neoplastic cells resemble normal cells without exhibiting significant cytologic
 * atypia, while malignant ones exhibit overt signs such as dysplastic features,
 * atypical mitotic figures, necrosis, nuclear pleomorphism, and anaplasia.
 * Representative examples of benign neoplasms include papillomas, cystadenomas,
 * and lipomas; malignant neoplasms include carcinomas, sarcomas, lymphomas, and
 * leukemias.
 * @version 1.0
 * @created 27-Sep-2006 9:55:14 AM
 */
@Entity
@Table
public class Neoplasm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * The type of cancer cells found.
	 */
    @Column
    private String cellType;

    /**
	 * The system generated unique identifier.
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CTOM_SEQ_GEN")
    private long id;

    @ManyToOne
    private Histopathology histopathology;

    public Neoplasm() {
    }

    public void finalize() throws Throwable {
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public Histopathology getHistopathology() {
        return histopathology;
    }

    public void setHistopathology(Histopathology histopathology) {
        this.histopathology = histopathology;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
