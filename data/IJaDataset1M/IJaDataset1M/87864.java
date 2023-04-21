package ru.ipo.dces.buildutils;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ilya
 * Date: 12.11.2010
 * Time: 19:53:27
 */
public class GenerateAll {

    private static final String[] args = new String[] {};

    public static void generateDCES_API() throws IOException {
        APIGenerator.main(args);
    }

    public static void generateUsingCompiledDCES_API() throws Exception {
        GenerateBeans.main(args);
        PHPCodeGenerator.main(args);
    }
}
