package espresso3d.engine.logo.logos;

import espresso3d.engine.E3DEngine;
import espresso3d.engine.logo.actors.E3DHighResLogoActor;
import espresso3d.engine.logo.base.IE3DLogo;
import espresso3d.engine.logo.particlesystems.BluePlasmaParticleFountain;
import espresso3d.engine.logo.particlesystems.RedPlasmaParticleFountain;
import espresso3d.engine.lowlevel.vector.E3DVector3F;
import espresso3d.engine.lowlevel.vector.E3DVector4F;
import espresso3d.engine.window.viewport.E3DViewport;
import espresso3d.engine.world.E3DWorld;
import espresso3d.engine.world.sector.E3DSector;
import espresso3d.engine.world.sector.actor.E3DCameraActor;
import espresso3d.engine.world.sector.light.E3DLight;

/**
 * @author Curt
 *
 * The logo animation created for version 0.2
 */
public final class E3DLogo0dot2 implements IE3DLogo {

    public void displayEngineLogo(E3DEngine engine) throws Exception {
        E3DWorld logoWorld = new E3DWorld(engine, "logoWorld");
        E3DSector logoSector = new E3DSector(engine, "logoSector");
        logoWorld.addSector(logoSector);
        E3DCameraActor cameraActor = new E3DCameraActor(engine, logoWorld, "camera1");
        cameraActor.rotate(Math.toRadians(90), new E3DVector3F(1.0, 0.0, 0.0));
        cameraActor.rotate(Math.toRadians(-180), new E3DVector3F(0.0, 1.0, 0.0));
        cameraActor.setPosition(new E3DVector3F(0.0, 50.0, 0.0));
        E3DHighResLogoActor logoActor = new E3DHighResLogoActor(engine, logoWorld, true);
        logoActor.scale(20);
        engine.addWorld(logoWorld);
        E3DViewport viewport = new E3DViewport(engine, 0, 0, engine.getWindow().getWidth(), engine.getWindow().getHeight(), "logoViewport");
        viewport.setWorld(logoWorld);
        engine.getWindow().addViewport(viewport);
        logoSector.addActor(logoActor);
        logoSector.addActor(cameraActor);
        viewport.setCameraActor(cameraActor);
        logoActor.setPosition(new E3DVector3F(0, 0, 0));
        BluePlasmaParticleFountain blueFountain = new BluePlasmaParticleFountain(engine, logoSector, new E3DVector3F(0, 0, 1.5));
        blueFountain.setPosition(new E3DVector3F(-14, 5, 9));
        blueFountain.rotate(Math.toRadians(-90), new E3DVector3F(1, 0, 0));
        logoSector.addParticleSystem(blueFountain);
        RedPlasmaParticleFountain redFountain = new RedPlasmaParticleFountain(engine, logoSector, new E3DVector3F(0, 0, 1.5));
        redFountain.setPosition(new E3DVector3F(14, 5, 9));
        redFountain.rotate(Math.toRadians(-90), new E3DVector3F(1, 0, 0));
        logoSector.addParticleSystem(redFountain);
        double time = 0.0;
        boolean startLastPiece = false;
        E3DLight lightEnd = new E3DLight(engine, "lightEnd");
        lightEnd.setBrightness(0);
        lightEnd.setColor(new E3DVector4F(1, 1, 1, 1));
        lightEnd.setFalloff(200);
        boolean startFadeIn = false;
        while (time < 5) {
            if (redFountain.getParticleList().size() <= 0 && blueFountain.getParticleList().size() <= 0 && startLastPiece == false) {
                startLastPiece = true;
                logoSector.removeParticleSystem(blueFountain);
                logoSector.removeParticleSystem(redFountain);
            }
            if (time > 1.5) {
                logoSector.addLight(lightEnd);
                startFadeIn = true;
            }
            if (startFadeIn) lightEnd.setBrightness(((time - 1.5) / 3) * 175);
            if (engine.getFpsTimer().getLastUpdateTimeSeconds() != 0) time += engine.getFpsTimer().getLastUpdateTimeSeconds();
            engine.render();
        }
        engine.removeAllWorlds();
        engine.getWindow().removeAllViewports();
        engine.getFpsTimer().resetTimer();
    }
}
