package domain.simplex;

/**
 * Esta interfase es la que deben implementar los objetos que deben estar al tanto
 * de lo que pasa con el controlador de resolucionador (resolvedor) y tengan que realizar operaciones
 * en forma dependiente de este.
 * Por ejemplo: Actualizar una interfase gr�fica de usuario a medida que se resuelve el modelo.
 */
public interface ResolverObserver {

    /**
     * Notifica al observador que hubo un cambio en la estructura del simplex, es decir
     * se altero � bien la cantidad de variables � la cantidad de restricciones.
     */
    public void notifySizeChanged();

    /**
     * Notifica al observador que se realizo un pivoteo elegido por el resolucionador actual
     * y que los datos del modelo pueden estar alterados.
     */
    public void notifyPivot();

    /**
     * Notifica al observador que se llego a una condici�n de fin.
     */
    public void notifyEnded();
}
