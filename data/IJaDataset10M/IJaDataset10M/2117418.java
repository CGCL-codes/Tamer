package net.sourceforge.ubcdcreator.plugin;

import net.sourceforge.ubcdcreator.plugin.Plugin;
import java.util.ArrayList;
import net.sourceforge.ubcdcreator.plugin.PluginSaxHandler;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class PluginReader {

    public PluginReader() {
    }

    public Category[] read() {
        int i, j;
        ArrayList<Plugin> plugins = null;
        try {
            PluginSaxHandler handler = new PluginSaxHandler();
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            ArrayList<File> files = getFiles();
            for (i = 0; i < files.size(); i++) parser.parse(files.get(i), handler);
            plugins = handler.getPlugins();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Category> categories = new ArrayList<Category>();
        for (i = 0; i < Category.getCategoryCount(); i++) {
            categories.add(new Category());
            categories.get(i).name = Category.getCategoryDesc(i);
        }
        if (plugins != null) {
            for (i = 0; i < plugins.size(); i++) {
                for (j = 0; j < categories.size(); j++) if (Category.getCategoryVal(j).equals(plugins.get(i).category)) categories.get(j).plugins.add(plugins.get(i));
            }
        }
        for (i = 0; i < categories.size(); i++) {
            if (categories.get(i).plugins.size() <= 0) {
                categories.remove(i);
                i--;
            }
        }
        return categories.toArray(new Category[0]);
    }

    public ArrayList<File> getFiles() {
        ArrayList<File> files = new ArrayList<File>();
        File file = new File("plugins");
        if (file.isDirectory()) {
            int i;
            Stack<File> stack = new Stack<File>();
            File[] children;
            stack.push(file);
            while (!stack.empty()) {
                file = stack.pop();
                if (file.isDirectory()) {
                    children = file.listFiles();
                    for (i = 0; i < children.length; i++) stack.push(children[i]);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }
}
