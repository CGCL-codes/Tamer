package net.sf.campusip.web.webwork.action.especialidad;

public class RemoveEspecialidad extends ViewEspecialidad {

    private String confirm;

    public RemoveEspecialidad() {
        super();
    }

    public String execute() throws Exception {
        String result = super.execute();
        if (hasErrors()) {
            return result;
        }
        if (confirm != null) {
            jcolegio.remove(especialidad);
            return SUCCESS;
        } else {
            return "cancel";
        }
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
