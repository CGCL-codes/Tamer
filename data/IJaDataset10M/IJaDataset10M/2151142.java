package Modelo;

import java.util.List;

public interface InterfazModeloControlador {

    public int crearObjeto(int idTipoObjeto, double posX, double posY, List<Object> parametros);

    public void lanzar(int idObjeto, double velocidad, double angulo);
}
