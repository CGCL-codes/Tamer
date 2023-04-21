package pe.com.bn.sach.domain;

/**
 * Bnchf01Persona entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class BnchfTasaInteres implements java.io.Serializable {

    private String f07CodCentroHip;

    private String f07CodTarifa;

    private String f07Timestamp;

    private Long f07Plazo;

    private Double f07TasaFija;

    private Double f07TasaVar;

    private Double f07TasaCom;

    private String f07CodUsuario;

    private String f07Estado;

    /**
	 * @return Devuelve f07Estado.
	 */
    public String getF07Estado() {
        return f07Estado;
    }

    /**
	 * @param estado El f07Estado a establecer.
	 */
    public void setF07Estado(String estado) {
        f07Estado = estado;
    }

    /**
	 * @return Devuelve f07CodCentroHip.
	 */
    public String getF07CodCentroHip() {
        return f07CodCentroHip;
    }

    /**
	 * @param codCentroHip El f07CodCentroHip a establecer.
	 */
    public void setF07CodCentroHip(String codCentroHip) {
        f07CodCentroHip = codCentroHip;
    }

    /**
	 * @return Devuelve f07CodTarifa.
	 */
    public String getF07CodTarifa() {
        return f07CodTarifa;
    }

    /**
	 * @param codTarifa El f07CodTarifa a establecer.
	 */
    public void setF07CodTarifa(String codTarifa) {
        f07CodTarifa = codTarifa;
    }

    /**
	 * @return Devuelve f07CodUsuario.
	 */
    public String getF07CodUsuario() {
        return f07CodUsuario;
    }

    /**
	 * @param codUsuario El f07CodUsuario a establecer.
	 */
    public void setF07CodUsuario(String codUsuario) {
        f07CodUsuario = codUsuario;
    }

    /**
	 * @return Devuelve f07Plazo.
	 */
    public Long getF07Plazo() {
        return f07Plazo;
    }

    /**
	 * @param plazo El f07Plazo a establecer.
	 */
    public void setF07Plazo(Long plazo) {
        f07Plazo = plazo;
    }

    /**
	 * @return Devuelve f07TasaFija.
	 */
    public Double getF07TasaFija() {
        return f07TasaFija;
    }

    /**
	 * @param tasaFija El f07TasaFija a establecer.
	 */
    public void setF07TasaFija(Double tasaFija) {
        f07TasaFija = tasaFija;
    }

    /**
	 * @return Devuelve f07TasaVar.
	 */
    public Double getF07TasaVar() {
        return f07TasaVar;
    }

    /**
	 * @param tasaVar El f07TasaVar a establecer.
	 */
    public void setF07TasaVar(Double tasaVar) {
        f07TasaVar = tasaVar;
    }

    /**
	 * @return Devuelve f07Timestamp.
	 */
    public String getF07Timestamp() {
        return f07Timestamp;
    }

    /**
	 * @param timestamp El f07Timestamp a establecer.
	 */
    public void setF07Timestamp(String timestamp) {
        f07Timestamp = timestamp;
    }

    public Double getF07TasaCom() {
        return f07TasaCom;
    }

    public void setF07TasaCom(Double tasaCom) {
        f07TasaCom = tasaCom;
    }
}
