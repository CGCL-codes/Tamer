package utilitarios;

public interface Observable {

    public void agregarObservador(Observador o);

    public void eliminarObservador(Observador o);

    public void notificarObservadores();
}
