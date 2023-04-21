package com.flagstone.transform.test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.zip.DataFormatException;
import junit.framework.TestCase;
import junit.framework.Assert;
import com.flagstone.transform.*;
import com.flagstone.transform.util.*;

public class FSSoundConstructorTest extends TestCase {

    public void testEventSound() {
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File directory, String name) {
                return name.endsWith(".wav");
            }
        };
        try {
            File sourceDir = new File("/windows/media");
            File destDir = new File("test/results/FSSoundConstructor/event/windows");
            assertTrue(sourceDir.exists());
            if (destDir.exists() == false) assertTrue("Count not create destination directory: " + destDir, destDir.mkdirs());
            String[] files = sourceDir.list(filter);
            File sourceFile = null;
            File destFile = null;
            for (int i = 0; i < files.length; i++) {
                try {
                    sourceFile = new File(sourceDir, files[i]);
                    destFile = new File(destDir, files[i].substring(0, files[i].lastIndexOf('.')) + ".swf");
                    FSSoundConstructor soundGenerator = new FSSoundConstructor(sourceFile.getPath());
                    encodeEventSoundToFile(soundGenerator, destFile);
                } catch (DataFormatException e) {
                    System.err.println(sourceFile + ": " + e.toString());
                }
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    public void testStreamingSound() {
        try {
            FilenameFilter filter = new FilenameFilter() {

                public boolean accept(File directory, String name) {
                    return name.endsWith(".wav");
                }
            };
            File sourceDir = new File("/windows/media");
            File destDir = new File("test/results/FSSoundConstructor/streaming/windows");
            assertTrue(sourceDir.exists());
            if (destDir.exists() == false) assertTrue("Count not create destination directory: " + destDir, destDir.mkdirs());
            String[] files = sourceDir.list(filter);
            File sourceFile = null;
            File destFile = null;
            for (int i = 0; i < files.length; i++) {
                try {
                    sourceFile = new File(sourceDir, files[i]);
                    destFile = new File(destDir, files[i].substring(0, files[i].lastIndexOf('.')) + ".swf");
                    FSSoundConstructor soundGenerator = new FSSoundConstructor(sourceFile.getPath());
                    encodeStreamingSoundToFile(soundGenerator, destFile);
                } catch (DataFormatException e) {
                    System.err.println(sourceFile + ": " + e.toString());
                }
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    private void encodeEventSoundToFile(FSSoundConstructor soundGenerator, File file) throws Exception {
        FSMovie movie = new FSMovie();
        float framesPerSecond = 12.0f;
        movie.setFrameSize(new FSBounds(0, 0, 8000, 4000));
        movie.setFrameRate(framesPerSecond);
        movie.add(new FSSetBackgroundColor(FSColorTable.lightblue()));
        int soundId = movie.newIdentifier();
        float duration = ((float) soundGenerator.getSamplesPerChannel()) / ((float) soundGenerator.getSampleRate());
        int numberOfFrames = (int) (duration * framesPerSecond);
        FSDefineSound sound = soundGenerator.defineSound(soundId);
        movie.add(sound);
        movie.add(new FSStartSound(new FSSound(soundId, FSSound.Start)));
        for (int j = 0; j < numberOfFrames; j++) movie.add(new FSShowFrame());
        movie.encodeToFile(file.getPath());
    }

    private void encodeStreamingSoundToFile(FSSoundConstructor soundGenerator, File file) throws Exception {
        float framesPerSecond = 12.0f;
        FSMovie movie = new FSMovie();
        movie.setFrameSize(new FSBounds(0, 0, 8000, 4000));
        movie.setFrameRate(framesPerSecond);
        movie.add(new FSSetBackgroundColor(FSColorTable.lightblue()));
        int samplesPerBlock = soundGenerator.getSampleRate() / (int) framesPerSecond;
        int numberOfBlocks = soundGenerator.getSamplesPerChannel() / samplesPerBlock;
        movie.add(soundGenerator.streamHeader(samplesPerBlock));
        for (int j = 0; j < numberOfBlocks; j++) {
            movie.add(soundGenerator.streamBlock(j, samplesPerBlock));
            movie.add(new FSShowFrame());
        }
        movie.encodeToFile(file.getPath());
    }
}
