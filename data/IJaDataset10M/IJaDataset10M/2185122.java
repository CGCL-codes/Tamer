package spacewars.graficos;

import java.net.URL;
import javax.media.opengl.GL;
import spacewars.graficos.arquivos.swg.SWGManager;
import spacewars.misc.Matriz;
import spacewars.misc.io.URLUtil;
import spacewars.principal.TipoPeca;

public class PropriedadesGraficas {

    protected static final float tam_LP = new Float(EngineGrafico.ConfiguracoesGraficas.getProperty("TamanhoLP", "10"));

    public static final int BillNada = 0;

    public static final int BillEsf = 1;

    public static final int BillCili = 2;

    protected int displayList_HP;

    protected int displayList_LP;

    protected int billboard = BillNada;

    protected float raio = 1;

    public PropriedadesGraficas() {
    }

    public PropriedadesGraficas(TipoPeca tipo) throws Exception {
        URL baseDir = tipo.getBaseDir();
        URL arqSWG_HP = URLUtil.compose(baseDir, tipo.getProperty("GraficoHP", null));
        URL arqSWG_LP = (tipo.getProperty("GraficoLP", null) == null) ? arqSWG_HP : URLUtil.compose(baseDir, tipo.getProperty("GraficoLP", null));
        String bill = tipo.getProperty("Billboarding", "Nada");
        if (bill.equalsIgnoreCase("Esferico")) billboard = BillEsf; else if (bill.equalsIgnoreCase("Cilindrico")) billboard = BillCili;
        displayList_HP = SWGManager.defaultSWGManager.getObjeto(arqSWG_HP);
        displayList_LP = SWGManager.defaultSWGManager.getObjeto(arqSWG_LP);
        raio = tipo.getRaio();
    }

    public void desenha(Matriz matriz, float constTamanho) {
        GL gl = EngineGrafico.gl;
        if (billboard == BillEsf) matriz.BillBoardEsferico();
        if (billboard == BillCili) matriz.BillBoardCilindrico();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadMatrixf(matriz.toArray(), 0);
        if (matriz.isFlip()) gl.glFrontFace(GL.GL_CW); else gl.glFrontFace(GL.GL_CCW);
        float tamanho = -constTamanho * raio / matriz.getTranslacao().z;
        if ((tamanho > tam_LP) || (tamanho < 0)) gl.glCallList(displayList_HP); else if (tamanho > 1) gl.glCallList(displayList_LP);
    }
}
