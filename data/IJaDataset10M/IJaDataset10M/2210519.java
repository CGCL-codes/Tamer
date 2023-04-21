package org.opensih.gdq.ControladoresCU;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import org.opensih.gdq.Modelo.Paciente;

@Local
public interface IPacienteCtrl {

    public abstract Paciente buscarPaciente(String ci, String root);

    public abstract Map<String, Paciente> buscarPacientes(List<String> idents);

    public abstract void altaPaciente(Paciente pac);

    public void unificarToXMLPIX(Paciente uno, Paciente dos);

    public void revertirUnificacion(String id_asse);
}
