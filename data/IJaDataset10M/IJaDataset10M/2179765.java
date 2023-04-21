package criaturas.fisica.formas;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Pyramid;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *Clase para especificar un cubo
 * @author Gabriel
 */
@XStreamAlias("Piramide")
public class Piramide extends Forma {

    private float largo, alto;

    /**
     * Crea un nuevo cubo
     * @param nombre Nombre
     * @param largo 
     * @param alto 
     * @param tamz Tamaño Z del cubo
     */
    public Piramide(String nombre, float largo, float alto) {
        this.largo = largo;
        this.alto = alto;
        this.nombre = nombre;
        tri = new Pyramid(nombre, largo, alto);
        tri.setModelBound(new BoundingBox());
        tri.updateModelBound();
    }

    /**
     * Tamaño Z de la forma
     * @return Tamaño Z de la forma
     */
    public float sizeX() {
        return largo;
    }

    /**
     * Tamaño Y de la forma
     * @return Tamaño Y de la forma
     */
    public float sizeY() {
        return alto;
    }

    /**
     * Tamaño Z de la forma
     * @return Tamaño Z de la forma
     */
    public float sizeZ() {
        return largo;
    }

    @Override
    public Forma clone() {
        return new Piramide(getNombre(), largo, alto);
    }

    @Override
    public void setSizeX(float x) {
        largo = x;
    }

    @Override
    public void setSizeY(float y) {
        alto = y;
    }

    @Override
    public void setSizeZ(float z) {
        largo = z;
    }
}
