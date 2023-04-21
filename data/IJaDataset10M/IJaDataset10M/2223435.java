package EvaGUI;

import com.jme.math.Vector2f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;

/**
 *
 * @author DarkGaze
 */
public class BarraPorcentaje extends Boton {

    private Quad quadRelleno_;

    private Vector2f borde_ = new Vector2f(6, 6);

    private Vector2f tam_barra_ = null;

    public BarraPorcentaje(Widget padre) {
        super(padre);
        tam_barra_ = new Vector2f(tamano_.x - borde_.x * 2, tamano_.y - borde_.y * 2);
        quadRelleno_ = new Quad("quadReleno", tam_barra_.x, tam_barra_.y);
        quadRelleno_.setDefaultColor(ColorRGBA.white);
        este_.attachChild(quadRelleno_);
        este_.updateGeometricState(0, true);
        este_.updateRenderState();
        este_.swapChildren(1, 2);
    }

    public void setPorcentaje(float valor, float maximo) {
        tam_barra_ = new Vector2f(tamano_.x - borde_.x * 2, tamano_.y - borde_.y * 2);
        float ratio = valor / maximo;
        float tam_barra = ratio * tam_barra_.x;
        quadRelleno_.resize(tam_barra, tam_barra_.y);
        quadRelleno_.setLocalTranslation(-(tam_barra_.x - tam_barra) * 0.5f, 0, 0);
        this.setTexto((int) (ratio * 100) + "%");
    }

    Quad getBarra() {
        return quadRelleno_;
    }
}
