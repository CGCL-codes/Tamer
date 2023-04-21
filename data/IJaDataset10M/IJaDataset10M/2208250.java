package pe.com.bn.sach.domain;

/**
 * Bnchf78Comentario entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class BnchfxxAgencia implements java.io.Serializable {

    private String f01aoficina;

    private String coddepofim;

    private String f01coficina;

    private String f01toficina;

    private String coddep4;

    private String coddep4m;

    private String codtipo;

    private Long f14IdHipotecario;

    private String f14DesHipotecario;

    public Long getF14IdHipotecario() {
        return f14IdHipotecario;
    }

    public void setF14IdHipotecario(Long idHipotecario) {
        f14IdHipotecario = idHipotecario;
    }

    /** default constructor */
    public BnchfxxAgencia() {
    }

    /** full constructor */
    public BnchfxxAgencia(String f01aoficina, String coddepofim, String f01coficina, String f01toficina, String coddep4, String coddep4m, String codtipo) {
        this.f01aoficina = f01aoficina;
        this.coddepofim = coddepofim;
        this.f01coficina = f01coficina;
        this.f01toficina = f01toficina;
        this.coddep4 = coddep4;
        this.coddep4m = coddep4m;
        this.codtipo = codtipo;
    }

    /**
	 * @return Devuelve coddep4.
	 */
    public String getCoddep4() {
        return coddep4;
    }

    /**
	 * @param coddep4 El coddep4 a establecer.
	 */
    public void setCoddep4(String coddep4) {
        this.coddep4 = coddep4;
    }

    /**
	 * @return Devuelve coddep4m.
	 */
    public String getCoddep4m() {
        return coddep4m;
    }

    /**
	 * @param coddep4m El coddep4m a establecer.
	 */
    public void setCoddep4m(String coddep4m) {
        this.coddep4m = coddep4m;
    }

    /**
	 * @return Devuelve coddepofim.
	 */
    public String getCoddepofim() {
        return coddepofim;
    }

    /**
	 * @param coddepofim El coddepofim a establecer.
	 */
    public void setCoddepofim(String coddepofim) {
        this.coddepofim = coddepofim;
    }

    /**
	 * @return Devuelve codtipo.
	 */
    public String getCodtipo() {
        return codtipo;
    }

    /**
	 * @param codtipo El codtipo a establecer.
	 */
    public void setCodtipo(String codtipo) {
        this.codtipo = codtipo;
    }

    /**
	 * @return Devuelve f01aoficina.
	 */
    public String getF01aoficina() {
        return f01aoficina;
    }

    /**
	 * @param f01aoficina El f01aoficina a establecer.
	 */
    public void setF01aoficina(String f01aoficina) {
        this.f01aoficina = f01aoficina;
    }

    /**
	 * @return Devuelve f01coficina.
	 */
    public String getF01coficina() {
        return f01coficina;
    }

    /**
	 * @param f01coficina El f01coficina a establecer.
	 */
    public void setF01coficina(String f01coficina) {
        this.f01coficina = f01coficina;
    }

    /**
	 * @return Devuelve f01toficina.
	 */
    public String getF01toficina() {
        return f01toficina;
    }

    /**
	 * @param f01toficina El f01toficina a establecer.
	 */
    public void setF01toficina(String f01toficina) {
        this.f01toficina = f01toficina;
    }

    /**
	 * @return Devuelve f14DesHipotecario.
	 */
    public String getF14DesHipotecario() {
        return f14DesHipotecario;
    }

    /**
	 * @param desHipotecario El f14DesHipotecario a establecer.
	 */
    public void setF14DesHipotecario(String desHipotecario) {
        f14DesHipotecario = desHipotecario;
    }
}
