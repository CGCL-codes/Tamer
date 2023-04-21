package com.ibm.wala.j2ee.client;

import java.util.jar.JarFile;
import com.ibm.wala.classLoader.Module;

/**
 * 
 * An AnalysisEngine analyzes one or more J2EE modules, including
 * ear files, J2EE clients, web modules, and ejb modules.
 * 
 * @author Logan Colby
 * @author Stephen Fink
 */
public interface J2EEAnalysisEngine extends com.ibm.wala.client.AnalysisEngine {

    /**
   * Specify the jar file that represent the contents of the j2ee.jar 
   * that the application relies on
   * 
   * @param libs an array of jar files; for WAS, j2ee.jar and webcontainer.jar
   */
    void setJ2EELibraries(JarFile[] libs);

    /**
   * Specify the mdoules that represent the contents of the j2ee.jar 
   * that the application relies on
   * 
   * @param libs an array of Modules; for WAS, j2ee.jar and webcontainer.jar
   */
    void setJ2EELibraries(Module[] libs);
}
