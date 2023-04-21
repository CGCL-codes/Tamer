package schemacrawler.tools.integration.spring;

import schemacrawler.schemacrawler.Options;

/**
 * Additional options needed for Spring.
 * 
 * @author Sualeh Fatehi
 */
public class SpringOptions implements Options {

    private static final long serialVersionUID = 5125868244511892692L;

    private String executableName;

    private String dataSourceName;

    private String contextFileName;

    /**
   * Spring context file name.
   * 
   * @return Spring context file name.
   */
    public String getContextFileName() {
        return contextFileName;
    }

    /**
   * Bean name for the datasource.
   * 
   * @return Bean name for the datasource.
   */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
   * Bean name of the SchemaCrawler executable.
   * 
   * @return Bean name of the SchemaCrawler executable.
   */
    public String getExecutableName() {
        return executableName;
    }

    /**
   * Set the Spring context file name.
   * 
   * @param contextFileName
   *        Spring context file name.
   */
    public void setContextFileName(final String contextFileName) {
        this.contextFileName = contextFileName;
    }

    /**
   * Set the bean name for the datasource.
   * 
   * @param dataSourceName
   *        Bean name for the datasource.
   */
    public void setDataSourceName(final String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
   * Set the bean name of the SchemaCrawler executable.
   * 
   * @param executableName
   *        Bean name of the SchemaCrawler executable.
   */
    public void setExecutableName(final String executableName) {
        this.executableName = executableName;
    }
}
