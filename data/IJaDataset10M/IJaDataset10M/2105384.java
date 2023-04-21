package org.gvsig.i18n.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;

public class ConfigOptions {

    public String defaultBaseName = "text";

    public String defaultBaseDir = ".";

    public String databaseDir = "database";

    private String configFileName = "config.xml";

    public String[] languages;

    public ArrayList projects = new ArrayList();

    private String defaultLangList = "ca;cs;de;en;es;eu;fr;gl;it;pt";

    public String sourceKeys = "sources";

    private String defaultPropertyDir = "config";

    public String[] outputLanguages = { "en", "es" };

    public String outputDir = "output";

    public String inputDir = "input";

    public String[] defaultSrcDirs = { "src" };

    /**
	 * The character encoding of the Java source files, used to search keys in the sources.
	 */
    public String sourcesEncoding = "ISO8859_1";

    /**
	 * The character encoding of the generated output files for missing keys.
	 */
    public String outputEncoding = "UTF8";

    /**
	 * Creates a new ConfigOptions object.
	 */
    public ConfigOptions() {
        try {
            this.defaultBaseDir = getAbsolutePath(".", this.defaultBaseDir);
            this.databaseDir = getAbsolutePath(this.defaultBaseDir, this.databaseDir);
            this.outputDir = getAbsolutePath(this.defaultBaseDir, this.outputDir);
            this.inputDir = getAbsolutePath(this.defaultBaseDir, this.inputDir);
        } catch (IOException e) {
            System.err.println("Error accediendo a los directorios de las traducciones: " + e.getLocalizedMessage());
        }
    }

    /**
	 *  Creates a new ConfigOptions object, defining the config file to use.
	 *  
	 * @param configFileName The file name of the config file to use.
	 */
    public ConfigOptions(String configFileName) {
        this.configFileName = configFileName;
        try {
            this.defaultBaseDir = getAbsolutePath(".", this.defaultBaseDir);
            this.databaseDir = getAbsolutePath(this.defaultBaseDir, this.databaseDir);
            this.outputDir = getAbsolutePath(this.defaultBaseDir, this.outputDir);
            this.inputDir = getAbsolutePath(this.defaultBaseDir, this.inputDir);
        } catch (IOException e) {
            System.err.println("Error accediendo a los directorios de las traducciones: " + e.getLocalizedMessage());
        }
    }

    /**
	 * Sets the name of the config file to use.
	 * 
	 * @param configFileName
	 */
    public void setConfigFile(String configFileName) {
        this.configFileName = configFileName;
    }

    /**
	 * Gets the name of the config file in use.
	 * 
	 * @return The name of the config file in use.
	 */
    public String getConfigFile() {
        return configFileName;
    }

    /**
	 *  Loads the config parameters and the projects to consider from the XML
	 * config file */
    public boolean load() {
        KXmlParser parser = new KXmlParser();
        try {
            parser.setInput(new FileInputStream(configFileName), null);
        } catch (FileNotFoundException e1) {
            System.err.println(e1.getLocalizedMessage());
            return false;
        } catch (XmlPullParserException e1) {
            System.err.println("Aviso: no se pudo leer correctamente el fichero de configuraci�n. Se usar�n los valores por defecto.");
            return false;
        }
        try {
            for (parser.next(); parser.getEventType() != KXmlParser.END_DOCUMENT; parser.next()) {
                if (parser.getEventType() == KXmlParser.START_TAG) {
                    if (parser.getName().equals("config")) {
                        parseVars(parser);
                    } else if (parser.getName().equals("projects")) {
                        parseProjects(parser);
                    }
                }
            }
        } catch (XmlPullParserException e1) {
            e1.getLocalizedMessage();
        } catch (IOException e1) {
            e1.getLocalizedMessage();
        }
        File outputDirFile = new File(outputDir);
        outputDirFile.mkdirs();
        File databaseDirFile = new File(databaseDir);
        databaseDirFile.mkdirs();
        return true;
    }

    private void parseVars(KXmlParser parser) throws XmlPullParserException, IOException {
        int state;
        String name, value;
        for (state = parser.next(); state != KXmlParser.END_TAG || !parser.getName().equals("config"); state = parser.next()) {
            if (state == KXmlParser.START_TAG) {
                if (parser.getName().equals("variable")) {
                    name = parser.getAttributeValue(null, "name");
                    value = parser.getAttributeValue(null, "value");
                    if (name != null && value != null) {
                        value = parser.getAttributeValue(null, "value");
                        if (parser.getAttributeValue(null, "name").equals("basename")) {
                            defaultBaseName = parser.getAttributeValue(null, "value");
                        } else if (parser.getAttributeValue(null, "name").equals("basedir")) {
                            defaultBaseDir = parser.getAttributeValue(null, "value");
                        } else if (parser.getAttributeValue(null, "name").equals("databaseDir")) {
                            databaseDir = parser.getAttributeValue(null, "value");
                        } else if (parser.getAttributeValue(null, "name").equals("defaultPropertyDir")) {
                            defaultPropertyDir = parser.getAttributeValue(null, "value");
                        } else if (parser.getAttributeValue(null, "name").equals("outputDir")) {
                            outputDir = parser.getAttributeValue(null, "value");
                        } else if (parser.getAttributeValue(null, "name").equals("inputDir")) {
                            inputDir = parser.getAttributeValue(null, "value");
                        } else if (parser.getAttributeValue(null, "name").equals("sourceKeys")) {
                            sourceKeys = parser.getAttributeValue(null, "value");
                        } else if (parser.getAttributeValue(null, "name").equals("srcDirs")) {
                            String srcDirs = parser.getAttributeValue(null, "value");
                            this.defaultSrcDirs = srcDirs.split(";");
                        } else if (parser.getAttributeValue(null, "name").equals("languages")) {
                            languages = parser.getAttributeValue(null, "value").split(";");
                            if (languages.length == 0) {
                                System.err.println("Aviso: No se definieron idiomas a considerar. Se usar� la lista de idiomas\n por defecto: " + defaultLangList);
                                languages = defaultLangList.split(";");
                            }
                        }
                    } else {
                        if (name == null) System.err.println("Error leyendo el fichero de configuraci�n. No se encontr� el atributo 'name'\nrequerido en la etiqueta <variable>. La etiqueta ser� ignorada.");
                        if (value == null) System.err.println("Error leyendo el fichero de configuraci�n. No se encontr� el atributo 'value'\nrequerido en la etiqueta <variable>. La etiqueta ser� ignorada.");
                    }
                } else {
                    System.err.println("Aviso: se ignor� una etiqueta desconocida o inesperada: " + parser.getName());
                }
            }
        }
        try {
            this.defaultBaseDir = getAbsolutePath(".", this.defaultBaseDir);
            this.databaseDir = getAbsolutePath(this.defaultBaseDir, this.databaseDir);
            this.outputDir = getAbsolutePath(this.defaultBaseDir, this.outputDir);
            this.inputDir = getAbsolutePath(this.defaultBaseDir, this.inputDir);
        } catch (IOException e) {
            System.err.println("Error accediendo a los directorios de las traducciones: " + e.getLocalizedMessage());
        }
    }

    /**
	 * Parse the lines containing <project /> tags (between <projects> and </projects>).
	 * 
	 * @param parser The KXmlParser, pointing to the next <project /> tag (if any)
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
    private void parseProjects(KXmlParser parser) throws XmlPullParserException, IOException {
        int state;
        String dir;
        File dirFile;
        Project project;
        for (state = parser.next(); state != KXmlParser.END_TAG || !parser.getName().equals("projects"); state = parser.next()) {
            if (state == KXmlParser.START_TAG) {
                if (parser.getName().equals("project")) {
                    if (parser.getAttributeValue(null, "dir") != null) {
                        dir = parser.getAttributeValue(null, "dir");
                        if (dir != null) {
                            dirFile = new File(dir);
                            try {
                                if (dirFile.isAbsolute()) {
                                    dir = dirFile.getCanonicalPath();
                                } else {
                                    dir = new File(this.defaultBaseDir + File.separator + dir).getCanonicalPath();
                                }
                            } catch (IOException e) {
                                System.err.println("Error accediendo a los directorios de las traducciones: " + e.getLocalizedMessage());
                            }
                            project = new Project();
                            project.dir = dir;
                            project.basename = parser.getAttributeValue(null, "basename");
                            if (project.basename == null) project.basename = this.defaultBaseName;
                            project.propertyDir = parser.getAttributeValue(null, "propertyDir");
                            if (project.propertyDir == null) {
                                project.propertyDir = this.defaultPropertyDir;
                            }
                            File propDirFile = new File(project.propertyDir);
                            try {
                                if (propDirFile.isAbsolute()) {
                                    project.propertyDir = propDirFile.getCanonicalPath();
                                } else {
                                    project.propertyDir = new File(dir + File.separator + project.propertyDir).getCanonicalPath();
                                }
                            } catch (IOException e) {
                                System.err.println("Error accediendo a los directorios de las traducciones: " + e.getLocalizedMessage());
                            }
                            String srcDirs = parser.getAttributeValue(null, "srcDirs");
                            if (srcDirs != null) {
                                project.srcDirs = srcDirs.split(";");
                            } else {
                                project.srcDirs = this.defaultSrcDirs;
                            }
                            project.sourceKeys = parser.getAttributeValue(null, "sourceKeys");
                            if (project.sourceKeys == null) project.sourceKeys = this.sourceKeys;
                            projects.add(project);
                        } else System.err.println("Error leyendo el fichero de configuraci�n. No se encontr� el atributo 'dir'\nrequerido en la etiqueta <project>. La etiqueta ser� ignorada.");
                    }
                } else {
                    System.err.println("Aviso: se ignorar� una etiqueta desconocida o inesperada: " + parser.getName());
                }
            }
        }
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public void setProjects(ArrayList projectList) {
        this.projects = projectList;
    }

    /**
	 * Calculates the canonical path for the given path.
	 * If the given path is relative, it is calculated from
	 * the given baseDir.
	 * The 'path' parameter uses the '/' character to as path
	 * separator. The returned value uses the default system
	 * separator as path separator.  
	 * 
	 * @param baseDir
	 * @param path
	 * @return
	 * @throws IOException 
	 */
    public static String getAbsolutePath(String baseDir, String path) throws IOException {
        if ('/' != File.separatorChar) path = path.replace('/', File.separatorChar);
        File pathFile = new File(path);
        if (pathFile.isAbsolute()) path = pathFile.getCanonicalPath(); else {
            File newFile = new File(baseDir + File.separator + path);
            path = newFile.getAbsolutePath();
        }
        return path;
    }
}
