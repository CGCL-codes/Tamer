package Vista3D.EstadoJuego;

import Vista3D.Vista3D;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.RenderState;
import java.io.File;
import criaturas.CriaturaFisica;
import criaturas.CriatureContainer;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**@TODO: Nos estamos olvidando de los patrones Singleton, y aqui hay muchos. Deberíamos usarlos. No queremos encontrarnos con que alguien hace dos Motor3D, por ejemplo.*/
public class Creador {

    public Creador() {
    }

    public Spatial crearComida(int x, int y, int z, int indice) {
        Box comida = new Box("Comida" + indice, new Vector3f(), 2, 2, 2);
        comida.setModelBound(new BoundingBox());
        comida.updateModelBound();
        comida.setRenderState((RenderState) Vista3D.getGestorRecursos().getRecurso("comp_luchar"));
        Spatial s1 = new SharedMesh("comida" + indice, comida);
        s1.setLocalTranslation(new Vector3f(x, y, z));
        s1.updateRenderState();
        s1.updateModelBound();
        return s1;
    }

    public CriaturaFisica crearCriatura(int x, int y, int z, int indice) {
        try {
            CriatureContainer cc = CriatureContainer.fromXML(new File("./src/XML/ArchAdv.xml"));
            CriaturaFisica cf = cc.getNewRandomInstance();
            return cf;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EstadoJuego.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
