package visualizaciones;

import java.awt.Point;

/**
 * @author Fernando
 *
 */
public interface Nodo {

    public int getIdNodo();

    public void setLocalizacion(int x, int y);

    public Point getLocalizacion();
}
