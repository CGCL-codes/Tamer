package criaturas.prototipos;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jmex.physics.material.Material;
import criaturas.animacion.EnumAnimacion;
import criaturas.animacion.AnimationSet;
import criaturas.animacion.CriaturaAnimada;
import criaturas.animacion.funciones.MultiPolarizedAnimation;
import criaturas.animacion.funciones.SineWaveAnimation;
import criaturas.animacion.tipos.Animation;
import criaturas.CriaturaFisica;
import criaturas.fisica.formas.TipoForma;
import criaturas.fisica.PartBuilder;

/**
 *
 * @author Valero
 */
public class PruebaCompleja7 {

    /**
     * 
     * @param rootNode
     */
    public PruebaCompleja7(Node rootNode) {
        EVA.animConfig.startAnimations();
        EVA.svConfig.startServoSimulation();
        CriaturaFisica cria = new CriaturaFisica(PartBuilder.create(TipoForma.CILINDRO, "Cuerpo", 2.8f, 4f));
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "Pata00", 0.5f, 1), new Vector3f(-3.5f, 0f, 2f));
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "PataL00", 0.5f, 0.5f), "Pata00");
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "Pata01", 0.5f, 1), new Vector3f(-3.5f, 0f, -2f));
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "PataL01", 0.5f, 0.5f), "Pata01");
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "Pata10", 0.5f, 1), new Vector3f(3.5f, 0f, -2f));
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "PataL10", 0.5f, 0.5f), "Pata10");
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "Pata11", 0.5f, 1), new Vector3f(3.5f, 0f, 2f));
        cria.addComponente(PartBuilder.create(TipoForma.CAPSULA, "PataL11", 0.5f, 0.5f), "Pata11");
        cria.setCriatureMaterial(Material.RUBBER);
        cria.setMasa(3);
        rootNode.attachChild(cria.getNodo());
        CriaturaAnimada cria2 = cria.getCriaturaAnimada();
        SineWaveAnimation mov1 = new SineWaveAnimation(0, 30, 0.5f, 0);
        SineWaveAnimation mov2 = new SineWaveAnimation(0, 30, 0.5f, 180);
        AnimationSet caminar = cria2.getAnimationSet(EnumAnimacion.CAMINAR);
        caminar.addAnimation("Pata00", Animation.EJEZ, mov1);
        caminar.addAnimation("Pata01", Animation.EJEZ, mov2);
        caminar.addAnimation("Pata10", Animation.EJEZ, mov1);
        caminar.addAnimation("Pata11", Animation.EJEZ, mov2);
        SineWaveAnimation movl1 = new SineWaveAnimation(45f, 45, 0.5f, 90);
        SineWaveAnimation movl2 = new SineWaveAnimation(-45f, 45, 0.5f, 90);
        SineWaveAnimation movl3 = new SineWaveAnimation(-45f, 45, 0.5f, 270);
        SineWaveAnimation movl4 = new SineWaveAnimation(45f, 45, 0.5f, 270);
        caminar.addAnimation("PataL00", Animation.EJEX, movl1);
        caminar.addAnimation("PataL01", Animation.EJEX, movl2);
        caminar.addAnimation("PataL10", Animation.EJEX, movl3);
        caminar.addAnimation("PataL11", Animation.EJEX, movl4);
        MultiPolarizedAnimation mpa1 = new MultiPolarizedAnimation(new float[] { 0, 60, 0 }, new float[] { 0, 4f, 0.005f });
        MultiPolarizedAnimation mpa2 = new MultiPolarizedAnimation(new float[] { 0, -60, 0 }, new float[] { 0, 4f, 0.005f });
        AnimationSet salto = cria2.getAnimationSet(EnumAnimacion.SALTO);
        salto.addAnimation("Pata00", Animation.EJEX, mpa1);
        salto.addAnimation("Pata11", Animation.EJEX, mpa1);
        salto.addAnimation("Pata01", Animation.EJEX, mpa2);
        salto.addAnimation("Pata10", Animation.EJEX, mpa2);
        MultiPolarizedAnimation mpal1 = new MultiPolarizedAnimation(new float[] { 0, -120, 0 }, new float[] { 0, 4f, 0.005f });
        MultiPolarizedAnimation mpal2 = new MultiPolarizedAnimation(new float[] { 0, 120, 0 }, new float[] { 0, 4f, 0.005f });
        salto.addAnimation("PataL00", Animation.EJEX, mpal1);
        salto.addAnimation("PataL11", Animation.EJEX, mpal1);
        salto.addAnimation("PataL01", Animation.EJEX, mpal2);
        salto.addAnimation("PataL10", Animation.EJEX, mpal2);
        cria2.runAnimation(EnumAnimacion.CAMINAR);
    }
}
