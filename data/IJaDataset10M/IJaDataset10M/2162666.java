package criaturas.prototipos;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import criaturas.animacion.AnimationSet;
import criaturas.animacion.CriaturaAnimada;
import criaturas.animacion.EnumAnimacion;
import criaturas.animacion.funciones.MultiPolarizedAnimation;
import criaturas.animacion.tipos.Animation;
import criaturas.CriaturaFisica;
import criaturas.fisica.formas.TipoForma;
import criaturas.fisica.PartBuilder;

/**
 *
 * @author Valero
 */
public class PruebaCompleja2 {

    /**
     * 
     * @param rootNode
     */
    public PruebaCompleja2(Node rootNode) {
        CriaturaFisica cria = new CriaturaFisica(PartBuilder.create(TipoForma.CUBO, "Cuerpo", 8f, 3f, 1f));
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "Pata00", 0.8f, 2.5f, 0.5f), new Vector3f(3f, -1.5f, 0f));
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "Pata01", 0.8f, 2.5f, 0.5f), new Vector3f(0f, -1.5f, 0.5f));
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "Pata011", 0.8f, 2.5f, 0.5f), new Vector3f(0f, -1.5f, -0.5f));
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "Pata02", 0.8f, 2.5f, 0.5f), new Vector3f(-3f, -1.5f, 0f));
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "PataL00", 0.4f, 2f, 0.4f), "Pata00");
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "PataL01", 0.4f, 2f, 0.4f), "Pata01");
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "PataL011", 0.4f, 2f, 0.4f), "Pata011");
        cria.addComponente(PartBuilder.create(TipoForma.CUBO, "PataL02", 0.4f, 2f, 0.4f), "Pata02");
        rootNode.attachChild(cria.getNodo());
        CriaturaAnimada cria2 = cria.getCriaturaAnimada();
        AnimationSet caminar = cria2.getAnimationSet(EnumAnimacion.CAMINAR);
        MultiPolarizedAnimation mp1 = new MultiPolarizedAnimation(new float[] { 15, 5f, 15f }, new float[] { 0, 0.5f, 0.5f });
        MultiPolarizedAnimation mp2 = new MultiPolarizedAnimation(new float[] { -15, -5f, -15f }, new float[] { 0, 0.5f, 0.5f });
        MultiPolarizedAnimation mp3 = new MultiPolarizedAnimation(new float[] { 20, 30f, 20f }, new float[] { 0, 0.5f, 0.5f });
        MultiPolarizedAnimation mp4 = new MultiPolarizedAnimation(new float[] { -20, -30f, -20f }, new float[] { 0, 0.5f, 0.5f });
        caminar.addAnimation("Pata00", Animation.EJEX, mp1);
        caminar.addAnimation("Pata01", Animation.EJEX, mp3);
        caminar.addAnimation("Pata011", Animation.EJEX, mp4);
        caminar.addAnimation("Pata02", Animation.EJEX, mp2);
        MultiPolarizedAnimation mpl1 = new MultiPolarizedAnimation(new float[] { 30, -30, 30 }, new float[] { 0, 0.7f, 0.3f });
        MultiPolarizedAnimation mpl2 = new MultiPolarizedAnimation(new float[] { -30, 30, -30 }, new float[] { 0, 0.7f, 0.3f });
        caminar.addAnimation("PataL00", Animation.EJEZ, mpl2);
        caminar.addAnimation("PataL011", Animation.EJEZ, mpl2);
        caminar.addAnimation("PataL01", Animation.EJEZ, mpl2);
        caminar.addAnimation("PataL02", Animation.EJEZ, mpl2);
        cria2.runAnimation(EnumAnimacion.CAMINAR);
        EVA.animConfig.startAnimations();
        EVA.svConfig.startServoSimulation();
    }
}
