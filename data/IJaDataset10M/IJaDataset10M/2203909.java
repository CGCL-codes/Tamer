package uk.ac.cisban.symba.webapp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * This file is part of SyMBA.
 * SyMBA is covered under the GNU Lesser General Public License (LGPL).
 * Copyright (C) 2007 jointly held by Allyson Lister, Olly Shaw, and their employers.
 * To view the full licensing information for this software and ALL other files contained
 * in this distribution, please see LICENSE.txt
 * <p/>
 * $LastChangedDate: 2008-05-09 08:59:50 -0400 (Fri, 09 May 2008) $
 * $LastChangedRevision: 131 $
 * $Author: allysonlister $
 * $HeadURL: http://symba.svn.sourceforge.net/svnroot/symba/tags/release-8.05/webapp-helper/src/main/java/uk/ac/cisban/symba/webapp/util/GenericEquipmentSummary.java $
 */
public class GenericEquipmentSummary {

    private String equipmentName;

    private Map<String, String> parameterAndTerms;

    private Map<String, String> parameterAndAtomics;

    private String freeTextDescription;

    public GenericEquipmentSummary() {
        this.parameterAndTerms = new HashMap<String, String>();
        this.parameterAndAtomics = new HashMap<String, String>();
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Map<String, String> getParameterAndTerms() {
        return parameterAndTerms;
    }

    public void setParameterAndTerms(Map<String, String> parameterAndTerms) {
        this.parameterAndTerms = parameterAndTerms;
    }

    public Map<String, String> getParameterAndAtomics() {
        return parameterAndAtomics;
    }

    public void setParameterAndAtomics(Map<String, String> parameterAndAtomics) {
        this.parameterAndAtomics = parameterAndAtomics;
    }

    public String getFreeTextDescription() {
        return freeTextDescription;
    }

    public void setFreeTextDescription(String freeTextDescription) {
        this.freeTextDescription = freeTextDescription;
    }

    public void putParameterAndTermPair(String parameterEndurantId, String termEndurantId) {
        this.parameterAndTerms.put(parameterEndurantId, termEndurantId);
    }

    public void putParameterAndAtomicPair(String parameterEndurantId, String atomicValue) {
        this.parameterAndAtomics.put(parameterEndurantId, atomicValue);
    }
}
