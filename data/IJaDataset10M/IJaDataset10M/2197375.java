package dinamica;

import motor3d.Objeto3d;
import motor3d.TransformacionEje;

public class ObjetoDinamicaRotInt extends ObjetoDinamica implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private boolean flagRotacionInterpolada;

    public RotacionInterpolada rotacionInterpolada;

    public ObjetoDinamicaRotInt() {
        rotacionInterpolada = new RotacionInterpolada();
    }

    public void setFlagRotacionInterpolada(boolean flag) {
        flagRotacionInterpolada = flag;
        rotacionInterpolada.activado = false;
    }

    public void setObjeto3d(Objeto3d obj) {
        super.setObjeto3d(obj);
        if (objeto3d != null && obj.transformacion != null && obj.transformacion.getClass().isAssignableFrom(TransformacionEje.class)) {
            rotacionInterpolada.transformacion = (TransformacionEje) obj.transformacion;
        } else {
            rotacionInterpolada.transformacion = null;
        }
    }

    public void dinamicaRotacion(double tiempo, double dt) {
        if (flagRotacionInterpolada) {
            rotacionInterpolada.tick(tiempo, dt);
        } else {
            super.dinamicaRotacion(tiempo, dt);
        }
    }
}
