package SessionBean;

import Entidades.Consultas;
import Entidades.Procedimientos;
import Entidades.Vacas;
import SessionBeans.ConsultasFacadeLocal;
import SessionBeans.ProcedimientosFacadeLocal;
import SessionBeans.VacasFacade;
import SessionBeans.VacasFacadeLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Felipe
 */
@Stateless
public class logica implements logicaLocal {

    @EJB
    private VacasFacadeLocal vacasFacade;

    @EJB
    private ProcedimientosFacadeLocal procedimientosFacade;

    @EJB
    private ConsultasFacadeLocal consultasFacade;

    @Override
    public void insertarVaca(String codigo) {
        Vacas nueva = new Vacas();
        nueva.setCodigo(codigo);
        vacasFacade.create(nueva);
    }

    @Override
    public void insertarConsulta(String codigoVaca, String procedimiento, String descripcion, int dia, int mes, int anno) {
        Consultas nueva = new Consultas();
        nueva.setAnno(anno);
        nueva.setDia(dia);
        nueva.setMes(mes);
        nueva.setDescripcion(descripcion);
        List<Vacas> vacas = vacasFacade.findAll();
        for (int i = 0; i < vacas.size(); i++) {
            if (vacas.get(i).getCodigo().equals(codigoVaca)) {
                nueva.setIdVacas(vacas.get(i));
            }
        }
        List<Procedimientos> procedimientos = procedimientosFacade.findAll();
        for (int i = 0; i < procedimientos.size(); i++) {
            if (procedimientos.get(i).getProcedimiento().equals(procedimiento)) {
                nueva.setIdProcedimientos(procedimientos.get(i));
            }
        }
        consultasFacade.create(nueva);
    }

    @Override
    public java.awt.List sacaInfo(String codigo) {
        List<Vacas> vacas = vacasFacade.findAll();
        List<Consultas> consultas = consultasFacade.findAll();
        List lasQueSon = null;
        for (int i = 0; i < vacas.size(); i++) {
            if (vacas.get(i).getCodigo().equals(codigo)) {
                for (int j = 0; j < consultas.size(); j++) {
                    if (consultas.get(j).getIdVacas().equals(vacas.get(i))) {
                        lasQueSon.add(consultas.get(j));
                    }
                }
                break;
            }
        }
        return (java.awt.List) lasQueSon;
    }
}
