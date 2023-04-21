package org.jboss.seam.example.Agenda;

import javax.ejb.Local;

@Local
public interface Register {

    public abstract String registerContacto();

    public abstract String ListarContactos();

    public String ModificarContacto1();

    public String ModificarContacto2();

    public String BuscarContacto();

    public String EliminarContacto();

    public abstract void destroy();

    public abstract String nuevo();

    public String cancelar();
}
