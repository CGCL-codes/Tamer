package org.oseb.sacred;

import org.adempierelbr.util.TextUtil;

/**
 * Interface de Registro do Projeto SACRED
 * 
 * Sistema de Apuração do Crédito Acumulado
 * 
 * @author Mario Grigioni
 * @version $Id: RegSacred.java, 13/04/2010, 09:38, mgrigioni
 */
public interface RegSacred {

    public static final String PIPE = "|";

    public static final String EOL = TextUtil.EOL_WIN32;

    public String toString();

    void addCounter();
}
