package juego;

import exceptions.LaserException;
import graphic.ImageUtils;
import java.awt.Color;
import java.awt.Image;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * La Clase Celda.
 * @author Daniel Goldberg, Maria Eugenia Cura
 */
public class Celda {

    private Set<Laser> lasers;

    private Elemento elemento;

    /**
	 * Crea una nueva celda.
	 */
    public Celda() {
        this.lasers = new TreeSet<Laser>(new Comparator<Laser>() {

            public int compare(Laser p1, Laser p2) {
                return 1;
            }
        });
    }

    /**
	 * Si hay un laser en la celda con el mismo color que recibe.
	 * 
	 * @param color un color
	 * 
	 * @return true, si hay un laser de ese color.
	 */
    public boolean hayLaserColor(Color color) {
        for (Laser unLaser : lasers) {
            if (unLaser.getColor().equals(color)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Es origen.
	 * 
	 * @return true, si es origen.
	 */
    public boolean esOrigen() {
        return elemento.getClass() == Origen.class;
    }

    /**
	 * Agregar laser.
	 * 
	 * @param elem un laser
	 * 
	 * @return true, si pudo agregar el laser al set.
	 */
    public boolean agregarLaser(Laser elem) {
        boolean flag;
        for (Laser laserViejo : lasers) {
            if (elem.getClass() == HalfLaser.class) flag = elem.getRotacion() == laserViejo.getRotacion(); else flag = (elem.getRotacion() % 2) == (laserViejo.getRotacion() % 2);
            if (flag) {
                Color colorNuevo = ImageUtils.mix(elem.getColor(), laserViejo.getColor());
                try {
                    lasers.add(laserViejo.crearNuevo(elem.rotacion, colorNuevo.getBlue(), colorNuevo.getGreen(), colorNuevo.getRed()));
                    return true;
                } catch (LaserException e) {
                    System.err.println("error en la creacion del laser");
                }
            }
        }
        lasers.add(elem);
        return true;
    }

    /**
	 * Borra los lasers del set.
	 */
    public void quitarLasers() {
        lasers.clear();
    }

    /**
	 * Setea el elemento de la celda.
	 * 
	 * @param elem el elemento a ser seteado
	 * 
	 * @return true, Si se setea correctamente.
	 */
    public boolean agregarElemento(Elemento elem) {
        this.elemento = elem;
        return true;
    }

    /**
	 * Copia el elemento a otra celda.
	 * 
	 * @param celdaDestino La celda destino
	 * 
	 * @return true, si se copia correctamente
	 */
    public boolean copiarElemento(Celda celdaDestino) {
        celdaDestino.elemento = elemento;
        elemento = null;
        return true;
    }

    /**
	 * Cantidad de lasers
	 * 
	 * @return la cantidad de lasers
	 */
    public int cantLasers() {
        if (this.lasers != null) return lasers.size();
        return 0;
    }

    /**
	 * Devuelve el elemento
	 * 
	 * @return el elemento
	 */
    public Elemento getElemento() {
        return this.elemento;
    }

    public String toString() {
        return this.elemento == null ? null : this.elemento.toString();
    }

    public String toStringGuardar(int i, int j) {
        return i + "," + j + "," + getElemento().toStringGuardar() + "\n";
    }

    /**
	 * Dibujar lasers.
	 * 
	 * @return un set con las imagenes de los lasers
	 */
    public Set<Image> dibujarLasers() {
        Set<Image> imagenes = new TreeSet<Image>(new Comparator<Image>() {

            @Override
            public int compare(Image arg0, Image arg1) {
                return 1;
            }
        });
        for (Laser unLaser : lasers) imagenes.add(unLaser.dibujar());
        return imagenes;
    }

    /**
	 * Dibujar.
	 * 
	 * @return una imagen del elemento.
	 */
    public Image dibujar() {
        return elemento.dibujar();
    }
}
