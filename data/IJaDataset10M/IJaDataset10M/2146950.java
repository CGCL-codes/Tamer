package org.japano.metadata.xjavadoc;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.selectors.BaseExtendSelector;
import org.japano.metadata.MetadataProcessor;
import org.japano.util.Util;

/**
 
 An ant custom file selector which ensures that only those classes
 are parsed again, which are newer than their corresponding tag library definition file.
  
 @author Sven Helmberger ( sven dot helmberger at gmx dot de )
 @version $Id: Selector.java,v 1.5 2005/09/27 21:30:51 fforw Exp $
 #SFLOGO# 
 */
public class Selector extends BaseExtendSelector {

    private Hashtable packageToStamp = new Hashtable();

    /** Holds value of property webApp. */
    private File tldLocation;

    private static Map class2lib = null;

    private static boolean mapRead = false;

    public void setParameters(Parameter[] params) {
        for (int i = 0; i < params.length; i++) {
            Parameter p = params[i];
            if (p.getName().equalsIgnoreCase("TLDLocation")) {
                tldLocation = new File(p.getValue());
                readMap();
            } else {
                throw new BuildException("Illegal Parameter " + p.getName());
            }
        }
    }

    private void readMap() {
        if (!mapRead) {
            Map m = Util.readMap(tldLocation.getPath() + File.separator + MetadataProcessor.TAGLIB_MAP);
            if (m != null) {
                for (Iterator i = m.keySet().iterator(); i.hasNext(); ) {
                    String klass = (String) i.next();
                    String uri = (String) m.get(klass);
                    File tld = new File(tldLocation, org.japano.metadata.MetadataProcessor.getTLDNameFromURI(uri));
                    if (tld.exists()) {
                        m.put(klass, tld);
                    } else {
                        i.remove();
                    }
                }
                class2lib = m;
            }
            mapRead = true;
        }
    }

    public boolean isSelected(File baseDir, String fileName, File file) {
        validate();
        if (file.isDirectory()) return true;
        if (!fileName.endsWith(".java")) return false;
        if (class2lib == null) return true;
        String className = fileName.substring(0, fileName.length() - 5).replace(File.separatorChar, '.');
        File tldFile = (File) class2lib.get(className);
        if (tldFile == null) return true;
        long javaMod = file.lastModified();
        long tldMod = tldFile.lastModified();
        long now = System.currentTimeMillis();
        if (javaMod > now) file.setLastModified(now);
        if (tldMod > now) tldFile.setLastModified(now - 1);
        return javaMod >= tldMod;
    }

    public void verifySettings() {
        if (!tldLocation.exists()) throw new BuildException("TLD location \"" + tldLocation + "\" does not exist");
        if (!tldLocation.isDirectory()) throw new BuildException(tldLocation + "\" is no directory.");
    }
}
