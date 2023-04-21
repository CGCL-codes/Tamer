package utilidades;

import java.util.ArrayList;

/**
 *
 * @author carlos
 */
public class SubjectHelper implements ISubject {

    private ArrayList listaObservadores = new ArrayList();

    /** Creates a new instance of SubjectHelper */
    public SubjectHelper() {
    }

    public void addObserver(IObserver observador) {
        listaObservadores.add(observador);
    }

    public void removeObserver(IObserver observador) {
        listaObservadores.remove(observador);
    }

    public void notify(Class clase, Object argumento, String mensaje) {
        for (int i = 0; i < listaObservadores.size(); i++) {
            IObserver observador = (IObserver) listaObservadores.get(i);
            observador.update(clase, argumento, mensaje);
        }
    }
}
