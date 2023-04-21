package yati.gui;

import yati.data.Block;
import yati.data.Spielfeld;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.ZBufferState;
import com.jmex.awt.SimplePassCanvasImpl;

public class GameOutCanvas3D extends SimplePassCanvasImpl implements IYatiCanvas {

    private Block b;

    private Spielfeld s;

    private MaterialState ms;

    private MaterialState blue;

    private MaterialState red;

    private MaterialState green;

    private MaterialState yellow;

    private MaterialState orange;

    private MaterialState pink;

    private MaterialState magenta;

    private Node n;

    protected GameOutCanvas3D(int arg0, int arg1) {
        super(arg0, arg1);
        this.doSetup();
        ZBufferState buf = renderer.createZBufferState();
        buf.setFunction(ZBufferState.CF_LEQUAL);
        buf.setEnabled(true);
        rootNode.setRenderState(buf);
        n = new Node("MyNode");
        red = renderer.createMaterialState();
        red.setDiffuse(ColorRGBA.red);
        red.setEnabled(true);
        green = renderer.createMaterialState();
        green.setDiffuse(ColorRGBA.green);
        green.setEnabled(true);
        blue = renderer.createMaterialState();
        blue.setDiffuse(ColorRGBA.blue);
        blue.setEnabled(true);
        orange = renderer.createMaterialState();
        orange.setDiffuse(ColorRGBA.orange);
        orange.setEnabled(true);
        pink = renderer.createMaterialState();
        pink.setDiffuse(ColorRGBA.pink);
        pink.setEnabled(true);
        magenta = renderer.createMaterialState();
        magenta.setDiffuse(ColorRGBA.magenta);
        magenta.setEnabled(true);
        ms = renderer.createMaterialState();
        ms.setDiffuse(ColorRGBA.white);
        ms.setEnabled(true);
        n.setRenderState(ms);
        LightState ls = renderer.createLightState();
        ls.detachAll();
        PointLight l = new PointLight();
        l.setLocation(new Vector3f(5, 0, 15));
        l.setDiffuse(ColorRGBA.white);
        l.setEnabled(true);
        ls.attach(l);
        n.setRenderState(ls);
        rootNode.attachChild(n);
        n.updateRenderState();
        this.cam.setLocation(new Vector3f(9, -18, 43));
        this.cam.setDirection(new Vector3f(0, 0, -1));
        this.cam.update();
        this.rootNode.updateRenderState();
    }

    public void drawGame() {
        Block block = b;
        Spielfeld feld = s;
        n.detachAllChildren();
        int[][] feldData = feld.getFeld();
        int[][] blockData = block.getBlock_data();
        int x = block.getPosition().x;
        int y = block.getPosition().y;
        for (int j = 0; j < feldData.length; ++j) {
            for (int i = 0; i < feldData[0].length; ++i) {
                if ((x <= i && i <= x + 3) && (y <= j && j <= y + 3)) {
                    if (blockData[j - y][i - x] > 0) {
                        Box sml = new Box("Hehe" + i + x + "," + j + y, new Vector3f((1f * i) * 2, (1f * -j) * 2, 0), .92f, .92f, .92f);
                        sml.setRenderState(getMatState(blockData[j - y][i - x]));
                        n.attachChild(sml);
                    } else {
                        if (feldData[j][i] > 0) {
                            Box sml = new Box("Hehe" + i + x + "," + j + y, new Vector3f((1f * i) * 2, (1f * -j) * 2, 0), .92f, .92f, .92f);
                            sml.setRenderState(getMatState(feldData[j][i]));
                            n.attachChild(sml);
                        }
                    }
                } else {
                    if (feldData[j][i] > 0) {
                        Box sml = new Box("Hehe" + i + x + "," + j + y, new Vector3f((1f * i) * 2, (1f * -j) * 2, 0), .92f, .92f, .92f);
                        sml.setRenderState(getMatState(feldData[j][i]));
                        n.attachChild(sml);
                    }
                }
            }
        }
        n.updateRenderState();
    }

    private MaterialState getMatState(int i) {
        switch(i) {
            case 1:
                return red;
            case 2:
                return green;
            case 3:
                return blue;
            case 4:
                return yellow;
            case 5:
                return orange;
            case 6:
                return pink;
            case 7:
                return magenta;
            default:
                return red;
        }
    }

    @Override
    public void setGame(Block b, Spielfeld s) {
        this.b = b;
        this.s = s;
    }

    @Override
    public void repaint() {
        this.drawGame();
    }
}
