package org.ximtec.igesture.framework.algorithm.feature;

import org.sigtec.ink.Note;
import org.sigtec.ink.NoteTool;
import org.ximtec.igesture.algorithm.feature.Feature;
import org.ximtec.igesture.algorithm.feature.FeatureException;
import org.ximtec.igesture.algorithm.feature.FeatureTool;

public class GenerateTestMethods {

    private static final String NOTE_XML = "note.xml";

    private static Note note;

    public static void main(String[] args) {
        note = NoteTool.importXML(ClassLoader.getSystemResourceAsStream(NOTE_XML));
        for (int i = 1; i <= 25; i++) {
            print(i);
        }
    }

    public static double compute(Class<? extends Feature> feature) {
        try {
            return FeatureTool.createFeature(feature.getName()).compute(note);
        } catch (FeatureException e) {
            throw new RuntimeException();
        }
    }

    @SuppressWarnings("unchecked")
    public static void print(int i) {
        try {
            Class<? extends Feature> c = (Class<? extends Feature>) Class.forName("org.ximtec.igesture.algorithm.feature.F" + i);
            System.out.println("@org.junit.Test");
            System.out.println("public void feature" + i + "(){");
            System.out.println("org.junit.Assert.assertEquals(compute(F" + i + ".class)," + compute(c) + ", 0.0000005);");
            System.out.println("}");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
