package mw.server.card.compiler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilerConfiguration;

public class CardCompiler {

    private org.codehaus.groovy.tools.Compiler c;

    private static final Logger log = Logger.getLogger(CardCompiler.class);

    public CardCompiler() {
        CompilerConfiguration config = new CompilerConfiguration();
        File classCache = new File("cache/cards");
        if (!classCache.exists()) classCache.mkdirs();
        config.setTargetDirectory(classCache);
        config.setDebug(false);
        c = new org.codehaus.groovy.tools.Compiler(config);
    }

    public String compileCard(String baseFolder, File cardFile) throws IOException {
        String className = cardFile.getName().replace(" ", "").replace(",", "").replace("'", "").replace("-", "").replaceAll(".card$", "");
        File classFile = new File("cache/cards/mw/server/card/" + className + ".class");
        if (classFile.exists() && classFile.lastModified() >= cardFile.lastModified()) return "mw.server.card." + className;
        File groovyFile = new File(cardFile.getParent() + "/" + className + ".groovy");
        if (groovyFile.exists()) groovyFile.delete();
        BufferedWriter writer = new BufferedWriter(new FileWriter(groovyFile));
        BufferedReader reader = new BufferedReader(new FileReader(cardFile));
        writer.append("package mw.server.card\n");
        writer.append("name = ''\n");
        writer.append("manacost = ''\n");
        writer.append("types = []\n");
        writer.append("text = []\n");
        writer.append("prints = []\n");
        writer.append("script = ''\n");
        writer.append("power = -1\n");
        writer.append("toughness = -1\n");
        writer.append("loyalty = -1\n");
        writer.append("color = null\n");
        writer.append("parsing = true\n");
        String s = reader.readLine();
        while (s != null) {
            writer.write(s + "\n");
            s = reader.readLine();
        }
        writer.write("[name, manacost, types, text, prints, script, power, toughness, loyalty, color, parsing]");
        reader.close();
        writer.close();
        c.compile(groovyFile);
        groovyFile.delete();
        return "mw.server.card." + className;
    }

    public String compileScript(File cardFile) throws IOException {
        String className = cardFile.getName().replace(" ", "").replace(",", "").replace("'", "").replace("-", "").replaceAll(".groovy$", "");
        File classFile = new File("cache/cards/mw/server/scripted/" + className + ".class");
        if (classFile.exists() && classFile.lastModified() >= cardFile.lastModified()) {
            log.info("Card is already compiled (" + cardFile + "). No compilation is needed.");
            return "mw.server.scripted." + className;
        } else if (classFile.exists()) {
            log.info("Refreshing compiled classes.");
        }
        log.info("Compiling " + cardFile + ".");
        long milliSec = new Date().getTime();
        File groovyFile = new File(cardFile.getParent() + "/" + className + ".groovy");
        c.compile(groovyFile);
        log.info("Card compilation time: " + (new Date().getTime() - milliSec) + "ms.");
        return "mw.server.scripted." + className;
    }
}
