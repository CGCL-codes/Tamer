package mx.ipn.to;

public class FiltroEmpleadoTO extends TransferObject {

    private EmpleadoTO[] arrEmpleado1TO;

    private EmpleadoTO[] arrEmpAOperarTO;

    private EmpleadoTO empleadoTO;

    public EmpleadoTO getEmpleadoTO() {
        return empleadoTO;
    }

    public void setEmpleadoTO(EmpleadoTO empleadoTO) {
        this.empleadoTO = empleadoTO;
    }

    public EmpleadoTO[] getArrEmpleado1TO() {
        return arrEmpleado1TO;
    }

    public void setArrEmpleado1TO(EmpleadoTO[] arrEmpleado1TO) {
        this.arrEmpleado1TO = arrEmpleado1TO;
    }

    public EmpleadoTO[] getArrEmpAOperarTO() {
        return arrEmpAOperarTO;
    }

    public void setArrEmpAOperarTO(EmpleadoTO[] arrEmpAOperarTO) {
        this.arrEmpAOperarTO = arrEmpAOperarTO;
    }

    public FiltroEmpleadoTO(EmpleadoTO[] arrEmpleado1TO, EmpleadoTO[] arrEmpAOperarTO, EmpleadoTO empleadoTO) {
        super();
        this.arrEmpleado1TO = arrEmpleado1TO;
        this.arrEmpAOperarTO = arrEmpAOperarTO;
        this.empleadoTO = empleadoTO;
    }

    public FiltroEmpleadoTO() {
        super();
        this.arrEmpleado1TO = null;
        this.arrEmpAOperarTO = null;
        this.empleadoTO = null;
    }
}
