package graphics;

import game.Camera;
import game.TracingCamera;
import game.Universe;
import globals.GlobalObjects;
import graphics.Shapes.TexturedQuad;
import graphics.Shapes.TexturedQuadShader;
import graphics.Skybox.SkyBox;
import javax.media.opengl.GL;
import physics.Avatar;
import physics.GameObject;
import physics.PortalEntrance;
import physics.PortalExit;
import physics.Sun;
import viewControllers.PortalViewController;
import common.Math3D;
import common.Point3D;
import common.Position3D;

public class GraphicalUniverse {

    SkyBox skybox = null;

    boolean isInit = false;

    TexturedQuad rq;

    TracingCamera portalCam = new TracingCamera();

    public void render(GL gl) {
        if (!isInit) {
            rq = new TexturedQuad();
            rq.initialize(gl);
            isInit = true;
        }
        {
            for (PortalEntrance portal : Universe.portals) {
                if (Universe.getAvatar().getPlanet() != portal.getPlanet()) continue;
                gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
                Universe.getCamera().updateView(gl);
                setPortalCamera(portalCam, portal.getPosition(), portal.getExit().getPosition(), Universe.getCamera().getPosition());
                TexturedQuadShader tq = ((PortalViewController) portal.getGraphicalObject()).tq;
                portalCam.setWidth(tq.width);
                portalCam.setHeight(tq.height);
                portalCam.updateView(gl);
                renderUniverseModel(gl);
                tq.bindTexture(gl);
            }
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            Universe.getCamera().updateView(gl);
            renderUniverseModel(gl);
        }
    }

    private void renderUniverseModel(GL gl) {
        synchronized (GlobalObjects.synchronizer) {
            skybox.setCenter(Universe.getCamera().getPosition());
            GraphicalObject.prerender(gl, skybox);
            for (GameObject gameObject : Universe.objects) {
                prerenderGameObject(gl, gameObject);
            }
            for (GameObject gameObject : Universe.objects) {
                renderGameObject(gl, gameObject);
            }
            for (GameObject gameObject : Universe.objects) {
                postrenderGameObject(gl, gameObject);
            }
        }
    }

    private void renderUniverseModelNoPortals(GL gl) {
        skybox.setCenter(Universe.getCamera().getPosition());
        GraphicalObject.prerender(gl, skybox);
        for (GameObject gameObject : Universe.objects) {
            if (!(gameObject instanceof PortalEntrance)) prerenderGameObject(gl, gameObject);
        }
        for (GameObject gameObject : Universe.objects) {
            if (!(gameObject instanceof PortalEntrance)) renderGameObject(gl, gameObject);
        }
        for (GameObject gameObject : Universe.objects) {
            if (!(gameObject instanceof PortalEntrance)) postrenderGameObject(gl, gameObject);
        }
    }

    private void renderGameObject(GL gl, GameObject gameObject) {
        double sbl = getSunBlockLevel(Universe.getSun(), gameObject);
        gl.glColor4d(sbl, sbl, sbl, 1);
        GraphicalObject.render(gl, gameObject.getGraphicalObject());
    }

    private void prerenderGameObject(GL gl, GameObject gameObject) {
        GraphicalObject.prerender(gl, gameObject.getGraphicalObject());
    }

    private void postrenderGameObject(GL gl, GameObject gameObject) {
        GraphicalObject.postrender(gl, gameObject.getGraphicalObject());
    }

    private double getSunBlockLevel(Sun sun, GameObject obj) {
        if (obj.getPlanet() == null) return 1;
        double blockLevel = Math3D.raySphereIntersectionDistance(obj.getCenter(), sun.getCenter(), obj.getPlanet().getCenter(), obj.getPlanet().getRadius());
        double colorFactor = Math.max(1.0 - (19.0 * blockLevel), 0);
        return colorFactor;
    }

    private void setPortalCamera(TracingCamera tc, Position3D entrance, Position3D exit, Point3D viewer) {
        Point3D lookVector = Math3D.subPoint(viewer, entrance.getCenter());
        double lookUp = Math3D.getDotProduct(lookVector, entrance.getUp());
        double lookFc = Math3D.getDotProduct(lookVector, entrance.getFace());
        double lookLf = Math3D.getDotProduct(lookVector, entrance.getLeft());
        Position3D vp = exit.clone();
        vp.getCenter().add(Math3D.scalePoint(vp.getUp(), lookUp));
        vp.getCenter().add(Math3D.scalePoint(vp.getFace(), lookFc));
        vp.getCenter().add(Math3D.scalePoint(vp.getLeft(), lookLf));
        tc.setTarget(exit, vp);
    }

    public void setSkybox(SkyBox skybox) {
        if (this.skybox != null) {
        }
        this.skybox = skybox;
    }
}
