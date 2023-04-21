package org.strutswrapper.servlet.config;

/**
 * <p>
 * The consumer of the {@link org.strutswrapper.servlet.config.MappingParser}
 * have to implement this interface to get the parsed servlet mapping.
 * </p>
 * 
 * @author Markus Hanses
 */
public interface MappingParserConsumer {

    /**
     * Return the name of the servlet.
     * 
     * @return The name of the servlet.
     */
    public String getServletName();

    /**
     * Adds a servlet mapping.
     * 
     * @param servletName
     *                The name of the servlet.
     * @param urlPattern
     *                The url of the servlet mapping.
     */
    public void addServletMapping(String servletName, String urlPattern);
}
