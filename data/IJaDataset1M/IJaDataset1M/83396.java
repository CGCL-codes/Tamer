package planilla.control;

import java.util.ArrayList;
import java.util.List;
import planilla.model.DataRepository;
import planilla.model.Horario;
import planilla.*;

public class AdmHorario {

    public static final String HORA_ENTRADA_DOCENTE = "09:00";

    public static final String HORA_SALIDA_DOCENTE = "18:00";

    private DataRepository mData;

    public AdmHorario(DataRepository mData) {
        this.mData = mData;
    }

    public DataRepository getData() {
        return mData;
    }

    public void setData(DataRepository val) {
        this.mData = val;
    }

    public void nuevo(Horario horario) {
        mData.getHorarios().add(horario);
    }

    public void eliminar(String nroDoc, String fecha, String horaInicio) {
        Horario obj = null;
        List<Horario> horarios = mData.getHorarios();
        for (int i = 0; i < horarios.size(); i++) {
            obj = horarios.get(i);
            if (obj.getEmpleado().getNroDocumento().equals(nroDoc) && obj.getFecha().equals(fecha) && obj.getHoraInicio().equals(horaInicio)) {
                horarios.remove(i);
                break;
            }
        }
    }

    public void actualizar(Horario horario) {
        Horario obj = null;
        List<Horario> horarios = mData.getHorarios();
        for (int i = 0; i < horarios.size(); i++) {
            obj = horarios.get(i);
            if (obj.getEmpleado().getNroDocumento().equals(horario.getEmpleado().getNroDocumento()) && obj.getFecha().equals(horario.getFecha()) && obj.getHoraInicio().equals(horario.getHoraInicio())) {
                horarios.set(i, obj);
                break;
            }
        }
    }

    public List listar(String nroDoc) {
        List<Horario> listahor = mData.getHorarios();
        List<Horario> listafiltro = new ArrayList<Horario>();
        Horario obj = null;
        for (int i = 0; i < listahor.size(); i++) {
            obj = listahor.get(i);
            if (obj.getEmpleado().getNroDocumento().equals(nroDoc)) {
                listafiltro.add(obj);
            }
        }
        return listafiltro;
    }

    public List listar(String nroDoc, String fecha) {
        List<Horario> listahor = mData.getHorarios();
        List<Horario> listafiltro = new ArrayList<Horario>();
        Horario obj = null;
        for (int i = 0; i < listahor.size(); i++) {
            obj = listahor.get(i);
            if (obj.getEmpleado().getNroDocumento().equals(nroDoc) && obj.getFecha().equals(fecha)) {
                listafiltro.add(obj);
            }
        }
        return listafiltro;
    }

    public Horario buscar(String nroDoc, String fecha, String horaInicio) {
        Horario find = null;
        Horario obj = null;
        List<Horario> horarios = mData.getHorarios();
        for (int i = 0; i < horarios.size(); i++) {
            obj = horarios.get(i);
            if (obj.getEmpleado().getNroDocumento().equals(nroDoc) && obj.getFecha().equals(fecha) && obj.getHoraInicio().equals(horaInicio)) {
                find = obj;
                break;
            }
        }
        return find;
    }
}
