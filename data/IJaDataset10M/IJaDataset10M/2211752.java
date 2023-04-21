package pruebaweb.autenticacion;

public class Estado {

    private boolean ok;

    private String error;

    public Estado(String error) {
        this.ok = error == null;
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public String getError() {
        return error;
    }
}
