package ca.uhn.hl7v2.conf.classes.generator.builders;

import java.io.*;

/** This class creates an ANT build.xml file
 * @author <table><tr>James Agnew</tr>
 *                <tr>Paul Brohman</tr>
 *                <tr>Mitch Delachevrotiere</tr>
 *                <tr>Shawn Dyck</tr>
 * 				  <tr>Cory Metcalf</tr></table>
 */
public class AntGenerator {

    private String baseDir;

    private String name;

    /** Returns a String representation of the ANT build.xml file
	* @return a String representation of the ANT build.xml file
	*/
    private String antString() {
        String classPath[] = System.getProperty("java.class.path", ".").split(";", -1);
        String ant = "<!-- Build file for the Conformance Classes -->\n" + "<project name=\"" + name + "\" default=\"dist\" basedir=\"" + baseDir + "\">\n" + "<!--  set global properties for this build -->\n" + "<property name=\"src\" value=\".\" />\n" + "<property name=\"build\" value=\"${basedir}/build\" />\n" + "<property name=\"classes\" value=\"${build}/classes\" />\n" + "<property name=\"docs\" value=\"${build}/docs\" />\n" + "<property name=\"dist\" value=\"${basedir}/dist\" />\n" + "<property name=\"build\" value=\"ca\" />\n" + "<!-- The class path -->\n" + "<path id=\"class.path\">\n" + "<pathelement path=\"${src}\" />\n" + "<pathelement path=\"${classes}\" />\n";
        for (int i = 0; i < classPath.length; i++) {
            ant += "<pathelement location=\"" + classPath[i] + "\" />\n";
        }
        ant += "<pathelement location=\"${java.home}/jre/lib/rt.jar\" />\n" + "</path>\n" + "<!-- Compile the project -->\n" + "<target name=\"compile_core\">\n" + "<mkdir dir=\"${classes}\" />\n" + "<javac srcdir=\"${src}\" destdir=\"${classes}\" includes=\"**\">\n" + "<classpath refid=\"class.path\" />\n" + "</javac>\n" + "</target>\n" + "<!--  Creates JavaDoc documentation (core classes only)   -->\n" + "<target name=\"doc\">\n" + "<mkdir dir=\"${docs}\" />\n" + "<javadoc packagenames=\"*\" classpathref=\"class.path\" sourcepath=\"${src}\" destdir=\"${docs}\" windowtitle=\"HAPI API Documentation\" />\n" + "</target>\n" + "<!-- Add the files to a Jar Archive -->\n" + "<target name=\"jar\" depends=\"compile_core, doc\">\n" + "<mkdir dir=\"${dist}\" />\n" + "<jar jarfile=\"${dist}/" + name + ".jar\" basedir=\"${classes}\">\n" + "<fileset dir=\"${docs}\" />\n" + "</jar>\n" + "</target>\n" + "<target name=\"dist\" depends=\"jar,doc\" />\n" + "<target name=\"clean\">\n" + "<delete>\n" + "<fileset dir=\"${build}\" />\n" + "</delete>\n" + "</target>\n" + "</project>\n";
        return ant;
    }

    /** Creates the ANT build.xml file
	* @param baseDir the directory where the ANT build.xml file will be saved
	* @param name the name of the project
	*/
    public void createAnt(String baseDir, String name) {
        this.baseDir = baseDir;
        this.name = name;
        FileOutputStream fstream;
        try {
            File f = new File(baseDir + File.separator + "build.xml");
            fstream = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            System.out.println("Filenotfoundexception: " + e.toString());
            return;
        }
        DataOutputStream ostream = null;
        try {
            ostream = new DataOutputStream(fstream);
            ostream.writeBytes(this.antString());
        } catch (IOException e) {
            System.out.println("IOexception:\n" + e.toString() + "\n");
        } finally {
            try {
                ostream.flush();
                fstream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
