package pe.com.bn.sach.domain;

import java.util.Date;

/**
 * Bnchf69Mensaje entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Bnchf69Mensaje implements java.io.Serializable {

    private Long f69IdMensaje;

    private String f69DescMen;

    private String f69AsntoMen;

    private String f69CuerpoMen;

    private Long f69Stdo;

    private String f69IdUsuaCrea;

    private Date f69FeUsuaCrea;

    private String f69IdUsuaModi;

    private Date f69FeUsuaModi;

    private String f69Item;

    /** default constructor */
    public Bnchf69Mensaje() {
    }

    /** minimal constructor */
    public Bnchf69Mensaje(Long f69IdMensaje, String f69AsntoMen, String f69CuerpoMen, String f69IdUsuaCrea, Date f69FeUsuaCrea) {
        this.f69IdMensaje = f69IdMensaje;
        this.f69AsntoMen = f69AsntoMen;
        this.f69CuerpoMen = f69CuerpoMen;
        this.f69IdUsuaCrea = f69IdUsuaCrea;
        this.f69FeUsuaCrea = f69FeUsuaCrea;
    }

    /** full constructor */
    public Bnchf69Mensaje(Long f69IdMensaje, String f69DescMen, String f69AsntoMen, String f69CuerpoMen, Long f69Stdo, String f69IdUsuaCrea, Date f69FeUsuaCrea, String f69IdUsuaModi, Date f69FeUsuaModi) {
        this.f69IdMensaje = f69IdMensaje;
        this.f69DescMen = f69DescMen;
        this.f69AsntoMen = f69AsntoMen;
        this.f69CuerpoMen = f69CuerpoMen;
        this.f69Stdo = f69Stdo;
        this.f69IdUsuaCrea = f69IdUsuaCrea;
        this.f69FeUsuaCrea = f69FeUsuaCrea;
        this.f69IdUsuaModi = f69IdUsuaModi;
        this.f69FeUsuaModi = f69FeUsuaModi;
    }

    public Long getF69IdMensaje() {
        return this.f69IdMensaje;
    }

    public void setF69IdMensaje(Long f69IdMensaje) {
        this.f69IdMensaje = f69IdMensaje;
    }

    public String getF69DescMen() {
        return this.f69DescMen;
    }

    public void setF69DescMen(String f69DescMen) {
        this.f69DescMen = f69DescMen;
    }

    public String getF69AsntoMen() {
        return this.f69AsntoMen;
    }

    public void setF69AsntoMen(String f69AsntoMen) {
        this.f69AsntoMen = f69AsntoMen;
    }

    public String getF69CuerpoMen() {
        return this.f69CuerpoMen;
    }

    public void setF69CuerpoMen(String f69CuerpoMen) {
        this.f69CuerpoMen = f69CuerpoMen;
    }

    public Long getF69Stdo() {
        return this.f69Stdo;
    }

    public void setF69Stdo(Long f69Stdo) {
        this.f69Stdo = f69Stdo;
    }

    public String getF69IdUsuaCrea() {
        return this.f69IdUsuaCrea;
    }

    public void setF69IdUsuaCrea(String f69IdUsuaCrea) {
        this.f69IdUsuaCrea = f69IdUsuaCrea;
    }

    public Date getF69FeUsuaCrea() {
        return this.f69FeUsuaCrea;
    }

    public void setF69FeUsuaCrea(Date f69FeUsuaCrea) {
        this.f69FeUsuaCrea = f69FeUsuaCrea;
    }

    public String getF69IdUsuaModi() {
        return this.f69IdUsuaModi;
    }

    public void setF69IdUsuaModi(String f69IdUsuaModi) {
        this.f69IdUsuaModi = f69IdUsuaModi;
    }

    public Date getF69FeUsuaModi() {
        return this.f69FeUsuaModi;
    }

    public void setF69FeUsuaModi(Date f69FeUsuaModi) {
        this.f69FeUsuaModi = f69FeUsuaModi;
    }

    /**
	 * @return Devuelve f69Item.
	 */
    public String getF69Item() {
        return f69Item;
    }

    /**
	 * @param item El f69Item a establecer.
	 */
    public void setF69Item(String item) {
        f69Item = item;
    }
}
