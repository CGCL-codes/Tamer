package org.hmaciel.sisingr.ejb.controladores;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Local;
import org.hmaciel.sisingr.ejb.querys.ParameterItemBean;
import org.hmaciel.sisingr.ejb.role.PatientBean;

@Local
public interface IBuscarPacientes {

    public abstract PatientBean buscarPaciente(List<ParameterItemBean> listpib);

    public void cancelarConsultaPaginada(String extensionQuery);

    public LinkedList<PatientBean> buscarPacientePaginado(List<ParameterItemBean> listpib, String extQuery, int pagina);

    public void contBuscarPacPaginado(String extQuery, List<PatientBean> listpatBean, int pagina);

    public boolean verificarExistencia(String cipat, String root);
}
