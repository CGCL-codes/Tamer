package com.cell.rpg.client.j3d.Set2D;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.cell.game.edit.SetInput;
import com.cell.rpg.client.World;
import com.cell.rpg.client.j3d.TerrainImpl;
import com.cell.rpg.client.j3d.Util;
import com.cell.rpg.client.j3d.WorldImpl;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;

public class SetMap2D extends TerrainImpl {

    Shape3D Shape;

    SetInput.TMap MapSet;

    double Width;

    double Height;

    public SetMap2D(String name, SetInput.TImages ts, SetInput.TMap ms) throws IOException {
        super(name);
        MapSet = ms;
        Width = MapSet.XCount * MapSet.CellW * WorldImpl.MainSizeRate;
        Height = MapSet.YCount * MapSet.CellH * WorldImpl.MainSizeRate;
        boolean isOneShape = false;
        if (!isOneShape) {
            Group = new TransformGroup();
            Group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            Group.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(ts.FileName));
            BufferedImage images[] = new BufferedImage[ts.ClipsX.length];
            for (int i = 0; i < images.length; i++) {
                if (ts.ClipsW[i] != 0 && ts.ClipsH[i] != 0) {
                    images[i] = img.getSubimage(ts.ClipsX[i], ts.ClipsY[i], ts.ClipsW[i], ts.ClipsH[i]);
                }
            }
            int xcount = MapSet.XCount;
            int ycount = MapSet.YCount;
            float cellw = MapSet.CellW * WorldImpl.MainSizeRate;
            float cellh = MapSet.CellH * WorldImpl.MainSizeRate;
            for (int w = 0; w < xcount; ++w) {
                for (int h = 0; h < ycount; ++h) {
                    int i = (h * xcount + w);
                    float mx = w * cellw;
                    float my = h * cellh;
                    Shape3D shape = new Shape3D();
                    float[] verts = new float[] { mx, 0, my + cellh, mx + cellw, 0, my + cellh, mx + cellw, 0, my, mx, 0, my };
                    float[] tcords = new float[] { 0, 1, 1, 1, 1, 0, 0, 0 };
                    GeometryInfo geom = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
                    geom.setCoordinates(verts);
                    Appearance appearance = new Appearance();
                    {
                        int imgIndex = ms.getLayerImagesIndex(w, h, 0);
                        TextureLoader texLoader = new TextureLoader(images[imgIndex], TextureLoader.GENERATE_MIPMAP | TextureLoader.BY_REFERENCE);
                        Texture2D texture = (Texture2D) texLoader.getTexture();
                        ImageComponent2D imageComp = (ImageComponent2D) texture.getImage(0);
                        BufferedImage bImage = imageComp.getImage();
                        bImage = Util.convertImage(bImage, BufferedImage.TYPE_4BYTE_ABGR);
                        Util.flipImage(bImage);
                        imageComp.set(bImage);
                        texture.setBoundaryModeS(Texture.CLAMP);
                        texture.setBoundaryModeT(Texture.CLAMP);
                        texture.setBoundaryColor(1.0f, 1.0f, 1.0f, 1.0f);
                        texture.setImage(0, imageComp);
                        appearance.setTexture(texture);
                        TextureAttributes texAttr = new TextureAttributes();
                        texAttr.setTextureMode(TextureAttributes.MODULATE);
                        appearance.setTextureAttributes(texAttr);
                        geom.setTextureCoordinateParams(1, 2);
                        geom.setTextureCoordinates(0, tcords);
                    }
                    {
                        TransparencyAttributes ta = new TransparencyAttributes();
                        ta.setTransparencyMode(TransparencyAttributes.NONE);
                        ta.setTransparency(0f);
                    }
                    {
                        Color3f write = new Color3f(1f, 1f, 1f);
                        Material wMat = new Material(write, write, write, write, 25.0f);
                        wMat.setLightingEnable(true);
                    }
                    {
                        PolygonAttributes pa = new PolygonAttributes();
                        pa.setCullFace(PolygonAttributes.CULL_NONE);
                        pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                    }
                    NormalGenerator norms = new NormalGenerator();
                    norms.generateNormals(geom);
                    shape.setGeometry(geom.getGeometryArray());
                    shape.setAppearance(appearance);
                    Group.addChild(shape);
                }
            }
            Root = new BranchGroup();
            Root.setCapability(BranchGroup.ALLOW_DETACH);
            Root.addChild(Group);
            Root.compile();
        } else {
            Shape3D shape = new Shape3D();
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream(ts.FileName));
            {
                float[] verts = new float[MapSet.XCount * MapSet.YCount * 12];
                float[] texturecords = new float[MapSet.XCount * MapSet.YCount * 8];
                {
                    int xcount = MapSet.XCount;
                    int ycount = MapSet.YCount;
                    float cellw = MapSet.CellW * WorldImpl.MainSizeRate;
                    float cellh = MapSet.CellH * WorldImpl.MainSizeRate;
                    for (int w = 0; w < xcount; ++w) {
                        for (int h = 0; h < ycount; ++h) {
                            int i = (h * xcount + w);
                            float mx = w * cellw;
                            float my = h * cellh;
                            System.arraycopy(new float[] { mx, 0, my + cellh, mx + cellw, 0, my + cellh, mx + cellw, 0, my, mx, 0, my }, 0, verts, i * 12, 12);
                            int imgIndex = ms.getLayerImagesIndex(w, h, 0);
                            int imgTrans = ms.getLayerTrans(w, h, 0);
                            float imgx = (float) ts.ClipsX[imgIndex] / img.getWidth();
                            float imgy = (float) ts.ClipsY[imgIndex] / img.getHeight();
                            float imgw = (float) ts.ClipsW[imgIndex] / img.getWidth();
                            float imgh = (float) ts.ClipsH[imgIndex] / img.getHeight();
                            System.arraycopy(new float[] { imgx, imgy + imgh, imgx + imgw, imgy + imgh, imgx + imgw, imgy, imgx, imgy }, 0, texturecords, i * 8, 8);
                        }
                    }
                }
                GeometryInfo geom = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
                geom.setCoordinates(verts);
                Appearance appearance = new Appearance();
                {
                    TextureLoader texLoader = new TextureLoader(img, TextureLoader.GENERATE_MIPMAP | TextureLoader.BY_REFERENCE);
                    Texture2D texture = (Texture2D) texLoader.getTexture();
                    ImageComponent2D imageComp = (ImageComponent2D) texture.getImage(0);
                    BufferedImage bImage = imageComp.getImage();
                    bImage = Util.convertImage(bImage, BufferedImage.TYPE_4BYTE_ABGR);
                    Util.flipImage(bImage);
                    imageComp.set(bImage);
                    texture.setBoundaryModeS(Texture.CLAMP);
                    texture.setBoundaryModeT(Texture.CLAMP);
                    texture.setBoundaryColor(1.0f, 1.0f, 1.0f, 1.0f);
                    texture.setImage(0, imageComp);
                    appearance.setTexture(texture);
                    TextureAttributes texAttr = new TextureAttributes();
                    texAttr.setTextureMode(TextureAttributes.MODULATE);
                    appearance.setTextureAttributes(texAttr);
                    geom.setTextureCoordinateParams(1, 2);
                    geom.setTextureCoordinates(0, texturecords);
                }
                {
                    RenderingAttributes ra = new RenderingAttributes();
                }
                {
                    TransparencyAttributes ta = new TransparencyAttributes();
                    ta.setTransparencyMode(TransparencyAttributes.NONE);
                    ta.setTransparency(0f);
                }
                {
                    Color3f write = new Color3f(1f, 1f, 1f);
                    Material wMat = new Material(write, write, write, write, 25.0f);
                    wMat.setLightingEnable(true);
                }
                {
                    PolygonAttributes pa = new PolygonAttributes();
                    pa.setCullFace(PolygonAttributes.CULL_NONE);
                    pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
                }
                NormalGenerator norms = new NormalGenerator();
                norms.generateNormals(geom);
                shape.setGeometry(geom.getGeometryArray());
                shape.setAppearance(appearance);
            }
            Shape = shape;
            init(shape);
        }
    }

    public double getWidth() {
        return Width;
    }

    public double getHeight() {
        return Height;
    }

    protected class GridMap extends Shape3D {

        private float[] verts;

        private float[] colors;

        private float[] texturecords;

        public final int MapWidth;

        public final int MapHeight;

        public final float CellWidth;

        public final float CellHeight;

        public final float Width;

        public final float Height;

        public GridMap(int mapw, int maph, float cellw, float cellh) {
            MapWidth = mapw;
            MapHeight = maph;
            CellWidth = cellw;
            CellHeight = cellh;
            Width = mapw * cellw;
            Height = maph * cellh;
            {
                int count = MapWidth * MapHeight * 12;
                verts = new float[count];
                colors = new float[count];
                texturecords = new float[MapWidth * MapHeight * 8];
                for (int w = 0; w < MapWidth; ++w) {
                    for (int h = 0; h < MapHeight; ++h) {
                        int i = (h * MapWidth + w) * 12;
                        float mx = w * cellw;
                        float my = h * cellh;
                        System.arraycopy(new float[] { mx, 0, my + cellh, mx + cellw, 0, my + cellh, mx + cellw, 0, my, mx, 0, my }, 0, verts, i, 12);
                        System.arraycopy(new float[] { 1f, 0, 0, 0, 1f, 0, 0, 0, 1f, 1f, 1f, 1f }, 0, colors, i, 12);
                        int ti = (h * MapWidth + w) * 8;
                        System.arraycopy(new float[] { 0, 0, 1, 0, 1, 1, 0, 1 }, 0, texturecords, ti, 8);
                    }
                }
                Appearance appearance = new Appearance();
                try {
                    BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/tiles.png"));
                    TextureLoader texLoader = new TextureLoader(img, TextureLoader.GENERATE_MIPMAP);
                    appearance.setTexture(texLoader.getTexture());
                    TextureAttributes texAttr = new TextureAttributes();
                    texAttr.setTextureMode(TextureAttributes.MODULATE);
                    appearance.setTextureAttributes(texAttr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Color3f write = new Color3f(1f, 1f, 1f);
                Material wMat = new Material(write, write, write, write, 25.0f);
                wMat.setLightingEnable(true);
                GeometryInfo geom = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
                geom.setCoordinates(verts);
                geom.setTextureCoordinateParams(1, 2);
                geom.setTextureCoordinates(0, texturecords);
                NormalGenerator norms = new NormalGenerator();
                norms.generateNormals(geom);
                setGeometry(geom.getGeometryArray());
                this.setAppearance(appearance);
            }
        }
    }
}
