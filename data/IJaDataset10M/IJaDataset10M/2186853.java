package net.sf.xwav.soundrenderer.test;

import junit.framework.TestCase;
import net.sf.xwav.soundrenderer.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConstantComponent extends TestCase {

    ConstantComponent mono1;

    ConstantComponent stereo1;

    SoundBuffer monoSb1;

    SoundBuffer monoSb2;

    SoundBuffer monoSb3;

    SoundBuffer monoSb4;

    SoundBuffer stereoSb1;

    SoundBuffer stereoSb2;

    SoundBuffer stereoSb3;

    SoundBuffer stereoSb4;

    @Before
    public void setUp() throws Exception {
        mono1 = new ConstantComponent(2.0f);
        monoSb1 = new SoundBuffer(1, 150);
        monoSb2 = new SoundBuffer(1, 60);
        monoSb3 = new SoundBuffer(1, 60);
        monoSb4 = new SoundBuffer(1, 30);
        mono1.open(new SoundDescriptor(150, 22100, 1, 16));
        stereo1 = new ConstantComponent(3.0f);
        stereoSb1 = new SoundBuffer(2, 150);
        stereoSb2 = new SoundBuffer(2, 60);
        stereoSb3 = new SoundBuffer(2, 60);
        stereoSb4 = new SoundBuffer(2, 30);
        stereo1.open(new SoundDescriptor(150, 22100, 2, 16));
    }

    @After
    public void tearDown() throws Exception {
        mono1.close();
        stereo1.close();
    }

    @Test
    public void testGenerateMono() throws BadParameterException {
        mono1.generate(monoSb1);
        for (int i = 0; i < 150; i++) {
            assertEquals(2.0f, monoSb1.getData()[0][i], 0.001);
        }
    }

    @Test
    public void testGenerateMonoMultiBlock() throws BadParameterException {
        mono1.generate(monoSb2);
        mono1.generate(monoSb3);
        mono1.generate(monoSb4);
        for (int i = 0; i < 60; i++) {
            assertEquals(2.0f, monoSb2.getData()[0][i], 0.001);
        }
        for (int i = 0; i < 60; i++) {
            assertEquals(2.0f, monoSb3.getData()[0][i], 0.001);
        }
        for (int i = 0; i < 30; i++) {
            assertEquals(2.0f, monoSb4.getData()[0][i], 0.001);
        }
    }

    @Test
    public void testGenerateStereo() throws BadParameterException {
        stereo1.generate(stereoSb1);
        for (int i = 0; i < 150; i++) {
            assertEquals(3.0f, stereoSb1.getData()[0][i], 0.001);
            assertEquals(3.0f, stereoSb1.getData()[1][i], 0.001);
        }
    }

    @Test
    public void testGenerateStereoMultiBlock() throws BadParameterException {
        stereo1.generate(stereoSb2);
        stereo1.generate(stereoSb3);
        stereo1.generate(stereoSb4);
        for (int i = 0; i < 60; i++) {
            assertEquals(3.0f, stereoSb2.getData()[0][i], 0.001);
            assertEquals(3.0f, stereoSb2.getData()[1][i], 0.001);
        }
        for (int i = 0; i < 60; i++) {
            assertEquals(3.0f, stereoSb3.getData()[0][i], 0.001);
            assertEquals(3.0f, stereoSb3.getData()[1][i], 0.001);
        }
        for (int i = 0; i < 30; i++) {
            assertEquals(3.0f, stereoSb4.getData()[0][i], 0.001);
            assertEquals(3.0f, stereoSb4.getData()[1][i], 0.001);
        }
    }
}
