package net.eejits.ejbgen;

import java.util.Observable;
import java.util.Observer;
import java.io.*;

/**
 * The <code>EjbGenProcess</code> encapsulates the source file generation process.
 * <p>
 * The class extends <code>java.util.Observable</code> in order that it's progress can
 * be monitored by any class that implements the <code>java.util.Observer</code> 
 * interface.
 * </p>
 * 
 * @author Nick Sharples
 */
public class EjbGenProcess extends Observable {

    /**
	 * Executes the process based on the specified <code>Config</code> implmementation
	 * @param cfg the configuration data
	 * @param sDestDir the directory to place generated java sources
	 * @param sXmlDir the directory to place generated xml sources
	 * @param bBuildBean whether to create the bean class
	 * 
	 * @see net.eejits.ejbgen.Config
	 * @exception Exception when an error occurs
	 */
    public void execute(Config cfg, String sDestDir, String sXmlDir, boolean bBuildBean) throws Exception {
        Entity[] ents = cfg.getEntities();
        for (int ndx = 0; ndx < ents.length; ndx++) {
            File entOutputDir = new File(sDestDir);
            entOutputDir.mkdirs();
            ClassGenerator[] gens = ents[ndx].getClassGenerators(bBuildBean);
            for (int i = 0; i < gens.length; i++) {
                String sPathElement = gens[i].getClassName().substring(0, gens[i].getClassName().lastIndexOf("."));
                String sClassFile = gens[i].getClassName().replace('.', java.io.File.separatorChar);
                File classDir = new File(entOutputDir, sPathElement);
                classDir.mkdirs();
                FileWriter fout = new FileWriter(new File(entOutputDir, sClassFile + ".java"));
                fout.write(gens[i].getSource());
                fout.close();
            }
            this.setChanged();
            this.notifyObservers("Created source files for " + ents[ndx].getObjectName());
        }
        this.setChanged();
        this.notifyObservers("Created source files for " + ents.length + " beans");
        EjbJarWriter ejbJar = new EjbJarWriter(cfg);
        File xmlDir = new File(sXmlDir);
        xmlDir.mkdirs();
        net.eejits.xml.XmlHelper.serializeDocument(new FileWriter(new File(xmlDir, "ejb-jar.xml")), ejbJar.getDocument());
        this.setChanged();
        this.notifyObservers("Created ejb-jar.xml file");
    }

    /**
	 * Executes the process based on the specified XML file
	 * @param sFile the configuration XML file
	 * @param sDestDir the directory to place generated java sources
	 * @param sXmlDir the directory to place generated xml sources
	 * @param bBuildBean whether to create the bean class
	 * 
	 * @see net.eejits.ejbgen.ConfigLoader
	 * @exception Exception when an error occurs
	 */
    public void execute(String sFile, String sDestDir, String sXmlDir, boolean bBuildBean) throws Exception {
        Config cfg = new ConfigLoader(sFile);
        this.execute(cfg, sDestDir, sXmlDir, bBuildBean);
    }
}
