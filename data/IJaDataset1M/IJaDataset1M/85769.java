package net.sf.mzmine.desktop.impl.helpsystem;

import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.util.ExceptionUtils;

public class HelpImpl {

    private static Logger logger = Logger.getLogger(MZmineCore.class.getName());

    private MZmineHelpSet hs;

    public HelpImpl() {
        MZmineHelpMap helpMap;
        JarFile jarFile;
        try {
            String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedPath = URLDecoder.decode(path, "UTF-8");
            helpMap = new MZmineHelpMap(decodedPath);
            jarFile = new JarFile(decodedPath);
        } catch (Exception e) {
            logger.warning("Could not load help files: " + ExceptionUtils.exceptionToString(e));
            return;
        }
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = e.nextElement();
            String name = entry.getName();
            if ((name.endsWith("htm")) || (name.endsWith("html"))) {
                helpMap.setTarget(name);
            }
        }
        helpMap.setTargetImage("topic.png");
        hs = new MZmineHelpSet();
        hs.setLocalMap(helpMap);
        MZmineTOCView myTOC = new MZmineTOCView(hs, "TOC", "Table Of Contents", helpMap, jarFile);
        hs.setTitle("MZmine 2");
        hs.addTOCView(myTOC);
    }

    public MZmineHelpSet getHelpSet() {
        return hs;
    }
}
