package org.stars.dao.config;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Sorgente di una o piu' definizioni di dao appartenenti ad una configurazione
 * di daostars.
 * 
 * @author Francesco Benincasa (908099)
 * @date 06/nov/07, 20:47:06
 * 
 */
@Root
public class Source {

    /**
	 * Costruttore del source.
	 */
    public Source() {
    }

    /**
	 * Costruttore del source.
	 */
    public Source(String directory, String filePattern, String jar) {
        this.directory = directory;
        this.filePattern = filePattern;
        this.jar = jar;
    }

    /**
	 * Jar che contiene le definizioni. Di default e' <code>null</code>.
	 */
    @Attribute(name = "jar", required = false)
    protected String jar;

    /**
	 * Directory su filesystem o nel jar che contiene le definizioni Di default
	 * e' <code>/daosql</code>.
	 */
    @Attribute(required = false)
    protected String directory;

    /**
	 * pattern dei file da ricercare. Di default e' <code>*.xml</code>.
	 */
    @Attribute(required = false)
    protected String filePattern;

    /**
	 * Getter dell'attributo directory
	 * 
	 * @return the directory
	 */
    public String getDirectory() {
        return directory;
    }

    /**
	 * Setter dell'attributo directory
	 * 
	 * @param directory
	 *            the directory to set
	 */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
	 * Getter dell'attributo filePattern
	 * 
	 * @return the filePattern
	 */
    public String getFilePattern() {
        return filePattern;
    }

    /**
	 * Setter dell'attributo filePattern
	 * 
	 * @param filePattern
	 *            the filePattern to set
	 */
    public void setFilePattern(String filePattern) {
        this.filePattern = filePattern;
    }

    /**
	 * Getter dell'attributo jar
	 * 
	 * @return the jar
	 */
    public String getJar() {
        return jar;
    }

    /**
	 * Setter dell'attributo jar
	 * 
	 * @param jar
	 *            the jar to set
	 */
    public void setJar(String jar) {
        this.jar = jar;
    }

    public boolean isJarResource() {
        if (jar == null || jar.length() == 0) return false;
        return true;
    }
}
