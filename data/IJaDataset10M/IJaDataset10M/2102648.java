package verisoft.BL;

import java.util.ArrayList;
import java.util.List;
import verisoft.BE.*;
import verisoft.DA.PerfilDA;

/**
 *
 * @author gginet
 */
public class PerfilBL {

    public List<Perfil> buscarPerfiles() {
        return new PerfilDA().findAll();
    }

    public List<Perfil> buscarPerfilesAtributos(Perfil perfilBusq) {
        List<Perfil> list = new ArrayList<Perfil>();
        List<Perfil> resultado = new ArrayList<Perfil>();
        Perfil perfil = null;
        if (perfilBusq.getIdperfil() != -1) {
            perfil = new PerfilDA().findById(perfilBusq.getIdperfil());
            list.add(perfil);
        } else {
            list = new PerfilDA().findByAtributos(perfilBusq.getNombre(), perfilBusq.getEstado());
        }
        for (Perfil per : list) {
            if (per.getEstado().getIdestado().equals(perfilBusq.getEstado().getIdestado())) {
                resultado.add(per);
            }
        }
        return resultado;
    }

    public Perfil nuevoPerfil(String nombre, String estado) {
        try {
            List<Estado> listaEstado = new EstadoBL().buscarEstados();
            estado = "G_" + estado;
            Estado estdoB = null;
            for (Estado est : listaEstado) {
                if (est.getIdestado().equals(estado)) {
                    estdoB = est;
                }
            }
            Perfil perfil = new Perfil();
            perfil.setDescripcion(nombre);
            perfil.setNombre(nombre);
            perfil.setEstado(estdoB);
            if (estdoB != null) return perfil; else return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean guardarPerfil(Perfil perfil) {
        return new PerfilDA().save(perfil);
    }

    public Perfil obtenerRegistro() {
        return new PerfilDA().findUltimo();
    }

    public boolean actualizarPerfil(Perfil perfil) {
        return new PerfilDA().update(perfil);
    }
}
