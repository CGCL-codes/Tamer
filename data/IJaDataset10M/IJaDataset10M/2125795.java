package pe.com.bn.sach.domain;

import java.util.Date;
import java.util.List;

/**
 * Bnchf36ExpVendedor entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class Bnchf36ExpVendedor implements java.io.Serializable {

    private List listVendedores;

    private Bnchf36ExpVendedorId id;

    private Long f36UsrReg;

    private Date f36FeUsrReg;

    private Long f36UsrAct;

    private Date f36FeUsrAct;

    private Long f36Stdo;

    private Long f36EstdoCheq;

    private String f36IdUsuaCrea;

    private Date f36FeUsuaCrea;

    private String f36IdUsuaModi;

    private Date f36FeUsuaModi;

    private Long f36FlagCheq;

    private Double f36MtoCheqGeren;

    private String f36MtoCheqGerens;

    private String f36NroControl;

    private String f36NroCheq;

    private Date f36FeEmiCheq;

    private String f36IdPagare;

    private String f36AgencCheq;

    private Long f36CondVend;

    private String f36DescComent;

    private int index;

    /** default constructor */
    public Bnchf36ExpVendedor() {
        id = new Bnchf36ExpVendedorId();
    }

    /** minimal constructor */
    public Bnchf36ExpVendedor(Bnchf36ExpVendedorId id, Long f36UsrReg, Date f36FeUsrReg, Long f36Stdo, String f36IdUsuaCrea, Date f36FeUsuaCrea) {
        this.id = id;
        this.f36UsrReg = f36UsrReg;
        this.f36FeUsrReg = f36FeUsrReg;
        this.f36Stdo = f36Stdo;
        this.f36IdUsuaCrea = f36IdUsuaCrea;
        this.f36FeUsuaCrea = f36FeUsuaCrea;
    }

    /** full constructor */
    public Bnchf36ExpVendedor(Bnchf36ExpVendedorId id, Long f36UsrReg, Date f36FeUsrReg, Long f36UsrAct, Date f36FeUsrAct, Long f36Stdo, String f36IdUsuaCrea, Date f36FeUsuaCrea, String f36IdUsuaModi, Date f36FeUsuaModi) {
        this.id = id;
        this.f36UsrReg = f36UsrReg;
        this.f36FeUsrReg = f36FeUsrReg;
        this.f36UsrAct = f36UsrAct;
        this.f36FeUsrAct = f36FeUsrAct;
        this.f36Stdo = f36Stdo;
        this.f36IdUsuaCrea = f36IdUsuaCrea;
        this.f36FeUsuaCrea = f36FeUsuaCrea;
        this.f36IdUsuaModi = f36IdUsuaModi;
        this.f36FeUsuaModi = f36FeUsuaModi;
    }

    public Bnchf36ExpVendedorId getId() {
        return this.id;
    }

    public void setId(Bnchf36ExpVendedorId id) {
        this.id = id;
    }

    public Long getF36UsrReg() {
        return this.f36UsrReg;
    }

    public void setF36UsrReg(Long f36UsrReg) {
        this.f36UsrReg = f36UsrReg;
    }

    public Date getF36FeUsrReg() {
        return this.f36FeUsrReg;
    }

    public void setF36FeUsrReg(Date f36FeUsrReg) {
        this.f36FeUsrReg = f36FeUsrReg;
    }

    public Long getF36UsrAct() {
        return this.f36UsrAct;
    }

    public void setF36UsrAct(Long f36UsrAct) {
        this.f36UsrAct = f36UsrAct;
    }

    public Date getF36FeUsrAct() {
        return this.f36FeUsrAct;
    }

    public void setF36FeUsrAct(Date f36FeUsrAct) {
        this.f36FeUsrAct = f36FeUsrAct;
    }

    public Long getF36Stdo() {
        return this.f36Stdo;
    }

    public void setF36Stdo(Long f36Stdo) {
        this.f36Stdo = f36Stdo;
    }

    public String getF36IdUsuaCrea() {
        return this.f36IdUsuaCrea;
    }

    public void setF36IdUsuaCrea(String f36IdUsuaCrea) {
        this.f36IdUsuaCrea = f36IdUsuaCrea;
    }

    public Date getF36FeUsuaCrea() {
        return this.f36FeUsuaCrea;
    }

    public void setF36FeUsuaCrea(Date f36FeUsuaCrea) {
        this.f36FeUsuaCrea = f36FeUsuaCrea;
    }

    public String getF36IdUsuaModi() {
        return this.f36IdUsuaModi;
    }

    public void setF36IdUsuaModi(String f36IdUsuaModi) {
        this.f36IdUsuaModi = f36IdUsuaModi;
    }

    public Date getF36FeUsuaModi() {
        return this.f36FeUsuaModi;
    }

    public void setF36FeUsuaModi(Date f36FeUsuaModi) {
        this.f36FeUsuaModi = f36FeUsuaModi;
    }

    public Double getF36MtoCheqGeren() {
        return f36MtoCheqGeren;
    }

    public void setF36MtoCheqGeren(Double mtoCheqGeren) {
        f36MtoCheqGeren = mtoCheqGeren;
    }

    /**
	 * @return Devuelve index.
	 */
    public int getIndex() {
        return index;
    }

    /**
	 * @param index El index a establecer.
	 */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
	 * @return Devuelve f36AgencCheq.
	 */
    public String getF36AgencCheq() {
        return f36AgencCheq;
    }

    /**
	 * @param agencCheq El f36AgencCheq a establecer.
	 */
    public void setF36AgencCheq(String agencCheq) {
        f36AgencCheq = agencCheq;
    }

    /**
	 * @return Devuelve f36FeEmiCheq.
	 */
    public Date getF36FeEmiCheq() {
        return f36FeEmiCheq;
    }

    /**
	 * @param feEmiCheq El f36FeEmiCheq a establecer.
	 */
    public void setF36FeEmiCheq(Date feEmiCheq) {
        f36FeEmiCheq = feEmiCheq;
    }

    /**
	 * @return Devuelve f36NroControl.
	 */
    public String getF36NroControl() {
        return f36NroControl;
    }

    /**
	 * @param nroControl El f36NroControl a establecer.
	 */
    public void setF36NroControl(String nroControl) {
        f36NroControl = nroControl;
    }

    /**
	 * @return Devuelve f36EstdoCheq.
	 */
    public Long getF36EstdoCheq() {
        return f36EstdoCheq;
    }

    /**
	 * @param estdoCheq El f36EstdoCheq a establecer.
	 */
    public void setF36EstdoCheq(Long estdoCheq) {
        f36EstdoCheq = estdoCheq;
    }

    /**
	 * @return Devuelve f36CondVend.
	 */
    public Long getF36CondVend() {
        return f36CondVend;
    }

    /**
	 * @param condVend El f36CondVend a establecer.
	 */
    public void setF36CondVend(Long condVend) {
        f36CondVend = condVend;
    }

    /**
	 * @return Devuelve f36DescComent.
	 */
    public String getF36DescComent() {
        return f36DescComent;
    }

    /**
	 * @param descComent El f36DescComent a establecer.
	 */
    public void setF36DescComent(String descComent) {
        f36DescComent = descComent;
    }

    /**
	 * @return Devuelve f36NroCheq.
	 */
    public String getF36NroCheq() {
        return f36NroCheq;
    }

    /**
	 * @param nroCheq El f36NroCheq a establecer.
	 */
    public void setF36NroCheq(String nroCheq) {
        f36NroCheq = nroCheq;
    }

    /**
	 * @return Devuelve listVendedores.
	 */
    public List getListVendedores() {
        return listVendedores;
    }

    /**
	 * @param listVendedores El listVendedores a establecer.
	 */
    public void setListVendedores(List listVendedores) {
        this.listVendedores = listVendedores;
    }

    /**
	 * @param flagCheq El f36FlagCheq a establecer.
	 */
    public void setF36FlagCheq(Long flagCheq) {
        f36FlagCheq = flagCheq;
    }

    /**
	 * @return Devuelve f36FlagCheq.
	 */
    public Long getF36FlagCheq() {
        return f36FlagCheq;
    }

    /**
	 * @return Devuelve f36MtoCheqGerens.
	 */
    public String getF36MtoCheqGerens() {
        return f36MtoCheqGerens;
    }

    /**
	 * @param mtoCheqGerens El f36MtoCheqGerens a establecer.
	 */
    public void setF36MtoCheqGerens(String mtoCheqGerens) {
        f36MtoCheqGerens = mtoCheqGerens;
    }

    /**
	 * @return Devuelve f36IdPagare.
	 */
    public String getF36IdPagare() {
        return f36IdPagare;
    }

    /**
	 * @param idPagare El f36IdPagare a establecer.
	 */
    public void setF36IdPagare(String idPagare) {
        f36IdPagare = idPagare;
    }
}
