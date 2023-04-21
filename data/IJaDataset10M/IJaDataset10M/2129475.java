package org.opensih.gdq.WServices.Rphm.ejb;

import java.util.List;

public interface InterfaceInvocador {

    public abstract String consultaRepositorio(String Fulano);

    public abstract void altaRepositorio(String datosAlta);

    public abstract String consultaContinuada(String Fulano);

    public abstract void cancelarConsulta(String Fulano);

    public abstract String listaDePacientes(List<String> identificadores);

    public abstract void unificarPacientes(String mensaje);

    public abstract void revertirUnificacion(String id_asse);
}
