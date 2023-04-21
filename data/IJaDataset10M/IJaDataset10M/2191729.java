package telefono;

import utilidad.clasesBase.*;

/**
 *
 * @author Sergio
 */
public class TelefonoVO extends BaseVO {

    private static final long serialVersionUID = 1L;

    private String modo;

    private int idEntidad;

    private String entidad;

    private String tipo;

    private String numero;

    private Boolean ppl;

    public TelefonoVO() {
        inicializarComponetes();
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public Integer getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        String texto = "";
        if (numero != null) {
            texto = numero;
        }
        return texto;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean esPpl() {
        return ppl;
    }

    public void setPpl(boolean ppl) {
        this.ppl = ppl;
    }

    @Override
    public void inicializarComponetes() {
        super.inicializarComponetes();
        this.modo = TelefonoUtil.MODO_TELEFONO;
        this.idEntidad = -1;
        this.entidad = "";
        this.tipo = "";
        this.numero = "";
        this.ppl = true;
    }
}
