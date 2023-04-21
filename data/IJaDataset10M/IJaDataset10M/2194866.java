package org.pojosoft.ria.gwt.server.service;

import javax.servlet.ServletContext;

/**
 * A contract for metadata loading support
 *
 * @author POJO Software
 */
interface MetadataLoader {

    /**
   * Loads a metadata
   *
   * @param metaId the metadata id
   * @param servletContext the servlet context
   * @return the metadata
   */
    public Object load(String metaId, ServletContext servletContext);
}
