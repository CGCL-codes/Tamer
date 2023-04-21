package pos.domain;

public class ProvinciaImpl implements Provincia {

    private String id;

    private String descripcion;

    public ProvinciaImpl() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
