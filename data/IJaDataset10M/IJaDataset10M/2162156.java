package com.maziade.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.maziade.qml.contentnode.QMLQuest;
import com.maziade.qml.renderer.PHPRendererSingleFile;
import com.maziade.qml.tools.XMLUtils;

public class TestPHPRendererSingleFile {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        render("bargames.xml", "demo_bargames.php", true);
        render("google.xml", "demo_google.php", true);
        render("candle.xml", "demo_candle.php", true);
        render("little_bird.xml", "demo_little_bird.php", true);
        render("test_cases.xml", "demo_testcases.php", false);
    }

    private static void render(String inFile, String outFile, boolean privat) {
        Writer out = null;
        try {
            File f = new File("phpEngine\\" + outFile);
            if (f.exists()) f.delete();
            out = new FileWriter(f);
            QMLQuest quest = new QMLQuest();
            quest.loadFromFile(XMLUtils.parseXMLDocument(new File("qml-samples\\" + (privat ? "private\\" : "") + inFile)), false);
            PHPRendererSingleFile renderer = new PHPRendererSingleFile(out, "");
            quest.render(renderer);
            out.flush();
        } catch (IOException e) {
            try {
                if (out != null) out.flush();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            if (out != null) out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
