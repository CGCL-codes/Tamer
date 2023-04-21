package com.loribel.java.generator.builder;

import java.io.File;
import java.io.FileInputStream;
import com.loribel.commons.util.FTools;
import com.loribel.java.abstraction.GB_JavaClassInterface;
import com.loribel.java.abstraction.GB_JavaMethod;
import com.loribel.java.parser.GB_JavaParserDefault;
import com.loribel.java.writer.GB_JavaWriterTools;

/**
 * Tools for java generator builder.
 */
public abstract class GB_JavaGeneratorBuilderToolsDemo {

    public static void main(String[] p) {
        try {
            String l_path = "C:/gb/gb-java/src";
            String l_filename = "gb/java/writer/GB_JavaWriterTools.java";
            File l_file = new File(l_path, l_filename);
            FileInputStream l_stream = new FileInputStream(l_file);
            String l_str = FTools.readFile(l_file);
            long t1 = System.currentTimeMillis();
            GB_JavaParserDefault l_parser = new GB_JavaParserDefault(l_stream, l_str);
            GB_JavaClassInterface l_class = l_parser.CompilationUnit();
            long t2 = System.currentTimeMillis();
            System.out.println("Parser " + (t2 - t1) + "ms");
            GB_JavaMethod l_method = l_class.getMethods().getAll()[0];
            GB_JavaMethod l_methodBuilder = GB_JavaGeneratorBuilderTools.genMethod(l_method);
            GB_JavaWriterTools.echo(l_methodBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
