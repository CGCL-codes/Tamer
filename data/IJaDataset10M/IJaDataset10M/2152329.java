package org.opennms.netmgt.dao;

/**
 * <p>JasperReportConfigDao interface.</p>
 */
public interface JasperReportConfigDao {

    /**
     * <p>getTemplateLocation</p>
     *
     * @param id a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getTemplateLocation(String id);

    /**
     * <p>getEngine</p>
     *
     * @param id a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    String getEngine(String id);
}
