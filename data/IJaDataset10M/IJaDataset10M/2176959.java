package planilla.control;

import java.util.List;
import planilla.model.DataRepository;
import planilla.model.Salario;
import planilla.*;
import planilla.model.Administrativo;
import planilla.model.Docente;
import planilla.model.Empleado;
import planilla.model.SalarioAdministrativo;
import planilla.model.SalarioCompleto;
import planilla.model.SalarioExclusivo;
import planilla.model.SalarioParcial;

public class AdmSalario {

    private static final double SUELDO_BASE_EXCLUSIVO = 5100.0;

    private static final double SUELDO_BASE_DOCENTE_MN5 = 3000.0;

    private static final double SUELDO_BASE_DOCENTE_MYI5 = 3600.0;

    private static final double APORTE_AFP = 0.13;

    private DataRepository mData;

    public AdmSalario(DataRepository mData) {
        this.mData = mData;
    }

    public DataRepository getData() {
        return mData;
    }

    public void setData(DataRepository val) {
        this.mData = val;
    }

    public void nuevo(Empleado emp) {
        Salario sal = getSalario(emp);
        mData.getSalarios().add(sal);
    }

    public void actualizar(Salario sal) {
        Salario obj = null;
        List<Salario> salarios = mData.getSalarios();
        for (int i = 0; i < salarios.size(); i++) {
            obj = salarios.get(i);
            if (obj.getEmpleado().getNroDocumento().equalsIgnoreCase(sal.getEmpleado().getNroDocumento())) {
                salarios.set(i, sal);
                break;
            }
        }
    }

    public void eliminar(String nroDocumento) {
        Salario obj = null;
        List<Salario> salarios = mData.getSalarios();
        for (int i = 0; i < salarios.size(); i++) {
            obj = salarios.get(i);
            if (obj.getEmpleado().getNroDocumento().equalsIgnoreCase(nroDocumento)) {
                salarios.remove(i);
                break;
            }
        }
    }

    public Salario buscar(String nroDocumento) {
        Salario find = null;
        Salario obj = null;
        List<Salario> salarios = mData.getSalarios();
        for (int i = 0; i < salarios.size(); i++) {
            obj = salarios.get(i);
            if (obj.getEmpleado().getNroDocumento().equalsIgnoreCase(nroDocumento)) {
                find = obj;
                break;
            }
        }
        return find;
    }

    private Salario getSalario(Empleado emp) {
        String tipoContrato = emp.getTipoContrato();
        Salario sal = null;
        if (tipoContrato.equals(AdmEmpleado.TYPE_ADMINISTRATIVO)) {
            sal = new SalarioAdministrativo();
            double sueldo = ((Administrativo) emp).getSueldo();
            sal.setSueldoBase(sueldo);
        } else if (tipoContrato.equals(AdmEmpleado.TYPE_MEDIO_TIEMPO)) {
            sal = new SalarioParcial();
            sal.setSueldoBase(0);
        } else if (tipoContrato.equals(AdmEmpleado.TYPE_TIEMPO_COMPLETO)) {
            sal = new SalarioCompleto();
            int exp = ((Docente) emp).getExperiencia();
            if (exp < 5) {
                sal.setSueldoBase(SUELDO_BASE_DOCENTE_MN5);
            } else {
                sal.setSueldoBase(SUELDO_BASE_DOCENTE_MYI5);
            }
        } else if (tipoContrato.equals(AdmEmpleado.TYPE_EXCLUSIVO)) {
            sal = new SalarioExclusivo();
            sal.setSueldoBase(SUELDO_BASE_EXCLUSIVO);
        } else {
            sal = new Salario() {

                @Override
                public void calcularSueldo() {
                    this.sueldoFinal = 0;
                }
            };
        }
        sal.setAfpPorcentaje(APORTE_AFP);
        sal.setFechaIngreso(emp.getFechaIngreso());
        sal.setEmpleado(emp);
        return sal;
    }
}
