package xmage.turbine.test.vt01;

import xmage.raster.Image;
import xmage.raster.codec.TGAInputCodec;
import xmage.turbine.Material;
import xmage.turbine.Skin;
import xmage.turbine.Texture;
import xmage.turbine.TriMesh;
import xmage.turbine.test.VtBase;
import java.io.IOException;

public class Vt05RectangleTextured extends VtBase {

    public Vt05RectangleTextured() throws IOException {
        TriMesh mesh = new TriMesh();
        mesh.setNumVertices(4);
        mesh.getVertices().put(new double[] { -8.0, -8.0, 0.0, 8.0, -8.0, 0.0, 8.0, 8.0, 0.0, -8.0, 8.0, 0.0 });
        mesh.getTexCoords().put(new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f });
        mesh.setNumTriangles(2);
        mesh.getTriangles().put(new int[] { 0, 1, 2, 0, 2, 3 });
        mesh.getMaterials().put(new short[] { 0, 1 });
        TGAInputCodec codec = new TGAInputCodec();
        Image img0 = codec.read("xmage/turbine/test/images/checkerboard_blueyellow.tga");
        Image img1 = codec.read("xmage/turbine/test/images/checkerboard_orangegreen.tga");
        Skin skin = new Skin(2);
        skin.setMaterial(0, new Material(new Texture(img0)));
        skin.setMaterial(1, new Material(new Texture(img1)));
        mesh.setSkin(skin);
        getCanvas().getRenderer().setRoot(mesh);
        show();
    }

    public static void main(String[] args) throws IOException {
        new Vt05RectangleTextured();
    }
}
