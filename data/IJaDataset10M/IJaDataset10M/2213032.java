package com.calipso.reportgenerator.common;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * User: soliveri
 * Date: Feb 5, 2004
 * Time: 4:55:16 PM
 *
 */
public interface IJasperDefinition {

    public JasperDesign getJasperDefinition(boolean isLandscape) throws JRException;
}
