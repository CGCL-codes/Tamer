package org.hmaciel.descop.otros.Puntaje;

import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.hmaciel.descop.otros.Log.ILogCUso;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

@Stateful
@Name("modificarPuntaje")
public class ModificarPuntaje implements IModificarPuntaje {

    @EJB
    ICalcularPuntaje logica;

    @EJB
    ILogCUso loguser;

    @Logger
    private Log log;

    String tipo;

    String categoria;

    String oportunidad;

    double valor;

    String mens;

    boolean cat;

    List<PuntajeASSEBean> puntos;

    String usuario = "";

    @In
    org.jboss.seam.security.Credentials credentials;

    @Create
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void nuevo() {
        tipo = "Cirujano";
        categoria = "3";
        oportunidad = "Coordinacion";
        cat = false;
        puntos = logica.listar();
        usuario = credentials.getUsername();
    }

    public String alta() {
        logica.modificarPuntaje(tipo, categoria, oportunidad, valor);
        puntos = logica.listar();
        log.info("Se ha guardado la informacion exitosamente");
        mens = "Se ha ingresado la informaci�n exitosamente.";
        loguser.registrarLog(new Date(), usuario, "5.d:Modificacion Puntaje: " + tipo + "," + categoria + "," + oportunidad + "," + valor);
        return "success";
    }

    public void seteo() {
    }

    public void seteo2() {
        if (tipo.equals("Anestesista")) {
            cat = true;
            categoria = "A";
        } else {
            cat = false;
            categoria = "3";
        }
    }

    @Destroy
    @Remove
    public void destroy() {
        log.info("se elimino");
        mens = "";
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getOportunidad() {
        return oportunidad;
    }

    public void setOportunidad(String oportunidad) {
        this.oportunidad = oportunidad;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getMens() {
        return mens;
    }

    public void setMens(String mens) {
        this.mens = mens;
    }

    public boolean isCat() {
        return cat;
    }

    public void setCat(boolean cat) {
        this.cat = cat;
    }

    public List<PuntajeASSEBean> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<PuntajeASSEBean> puntos) {
        this.puntos = puntos;
    }
}
