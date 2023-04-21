package spacewars.graficos;

import java.net.URL;
import javax.media.opengl.GL;
import spacewars.graficos.arquivos.swg.SWGManager;
import spacewars.misc.Matriz;
import spacewars.principal.EngineJogo;

public class PropriedadesGraficasLaser extends PropriedadesGraficas {

    protected static PropriedadesGraficasLaser propLaser;

    public static void inicia() throws Exception {
        propLaser = new PropriedadesGraficasLaser();
    }

    private PropriedadesGraficasLaser() throws Exception {
        URL arqSWG_HP = EngineJogo.class.getResource("/modelos/laser/laser.swg");
        billboard = BillCili;
        displayList_HP = SWGManager.defaultSWGManager.getObjeto(arqSWG_HP);
        displayList_LP = displayList_HP;
    }

    public static void desenhaLaser(Matriz matriz, float constTamanho, float matiz) {
        propLaser.desenha(matriz, constTamanho, matiz);
    }

    public void desenha(Matriz matriz, float constTamanho, float matiz) {
        GL gl = EngineGrafico.gl;
        gl.glMatrixMode(GL.GL_TEXTURE);
        gl.glTranslatef(0, matiz, 0);
        super.desenha(matriz, constTamanho);
        gl.glMatrixMode(GL.GL_TEXTURE);
        gl.glLoadIdentity();
    }
}
