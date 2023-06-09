package org.karlsland.m3g.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.karlsland.m3g.AnimationTrack;
import org.karlsland.m3g.Image2D;
import org.karlsland.m3g.KeyframeSequence;
import org.karlsland.m3g.Object3D;
import org.karlsland.m3g.Texture2D;

public class TestTexture2D {

    static {
        System.loadLibrary("jni-opengl");
    }

    @Test
    public void testInitialize() {
        Image2D img = new Image2D(Image2D.RGBA, 64, 64);
        Texture2D tex = new Texture2D(img);
        assertEquals(img, tex.getImage());
        assertEquals(Texture2D.WRAP_REPEAT, tex.getWrappingS());
        assertEquals(Texture2D.WRAP_REPEAT, tex.getWrappingT());
        assertEquals(Texture2D.FILTER_BASE_LEVEL, tex.getLevelFilter());
        assertEquals(Texture2D.FILTER_NEAREST, tex.getImageFilter());
        assertEquals(Texture2D.FUNC_MODULATE, tex.getBlending());
    }

    @Test
    public void testFinalize() {
        Image2D img = new Image2D(Image2D.RGBA, 64, 64);
        @SuppressWarnings("unused") Texture2D tex = new Texture2D(img);
        tex = null;
        System.gc();
    }

    @Test
    public void testSetBlendColor() {
        Image2D img = new Image2D(Image2D.RGBA, 64, 64);
        Texture2D tex = new Texture2D(img);
        tex.setBlendColor(0x12345678);
        assertEquals(0x00345678, tex.getBlendColor());
    }

    @Test
    public void testSetBlending() {
        Image2D img = new Image2D(Image2D.RGBA, 64, 64);
        Texture2D tex = new Texture2D(img);
        tex.setBlending(Texture2D.FUNC_DECAL);
        assertEquals(Texture2D.FUNC_DECAL, tex.getBlending());
    }

    @Test
    public void testSetFiltering() {
        Image2D img = new Image2D(Image2D.RGBA, 64, 64);
        Texture2D tex = new Texture2D(img);
        tex.setFiltering(Texture2D.FILTER_LINEAR, Texture2D.FILTER_LINEAR);
        assertEquals(Texture2D.FILTER_LINEAR, tex.getLevelFilter());
        assertEquals(Texture2D.FILTER_LINEAR, tex.getImageFilter());
    }

    @Test
    public void testSetImage() {
        Image2D img1 = new Image2D(Image2D.RGBA, 64, 64);
        Image2D img2 = new Image2D(Image2D.RGBA, 64, 64);
        Texture2D tex = new Texture2D(img1);
        tex.setImage(img2);
        assertEquals(img2, tex.getImage());
    }

    @Test
    public void testSetWrapping() {
        Image2D img = new Image2D(Image2D.RGBA, 64, 64);
        Texture2D tex = new Texture2D(img);
        tex.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP);
        assertEquals(Texture2D.WRAP_CLAMP, tex.getWrappingS());
        assertEquals(Texture2D.WRAP_CLAMP, tex.getWrappingT());
    }

    @Test
    public void testToString() {
        Image2D img = new Image2D(Image2D.RGBA, 64, 64);
        Texture2D tex = new Texture2D(img);
        tex.toString();
    }

    @Test
    public void testAddAnimationTrack() {
        KeyframeSequence keySeq1 = new KeyframeSequence(2, 3, KeyframeSequence.LINEAR);
        AnimationTrack anim1 = new AnimationTrack(keySeq1, AnimationTrack.COLOR);
        Image2D img = new Image2D(Image2D.RGB, 16, 16);
        Texture2D tex = new Texture2D(img);
        tex.addAnimationTrack(anim1);
        assertEquals(1, tex.getAnimationTrackCount());
        assertEquals(anim1, tex.getAnimationTrack(0));
    }

    @Test
    public void testGetReferences() {
        Image2D img = new Image2D(Image2D.RGB, 16, 16);
        Texture2D tex = new Texture2D(img);
        Object3D[] references = { null };
        int n = tex.getReferences(references);
        assertEquals(1, n);
        assertEquals(img, references[0]);
    }
}
