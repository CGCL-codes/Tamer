package spacewars.graficos.camadas;

import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL;
import spacewars.graficos.arquivos.swg.SWGManager;
import spacewars.misc.Matriz;
import spacewars.principal.EngineJogo;

public class CamadaLargada extends CamadaPontoDeVista {

    protected List<Matriz> Portais = new LinkedList<Matriz>();

    int idLargada = SWGManager.defaultSWGManager.getObjeto(EngineJogo.class.getResource("/modelos/largada/largada.swg"));

    public void desenhaUmaTela(Matriz matrizCamera, float constTamanho) {
        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadMatrixf(matrizCamera.toArray(), 0);
        gl.glCallList(idLargada);
        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE);
        gl.glDepthMask(false);
        Matriz pos = new Matriz();
        pos.addTranslation(0, 120, 0);
        gl.glColor3f(1, 0, 0);
        for (int i = 0; i < 5; i++) {
            pos.addTranslation(0, 25, 0);
            CamadaPortais.desenhaPortal(gl, pos, matrizCamera, constTamanho, 230);
        }
        gl.glDepthMask(true);
    }
}
