package org.oclc.da.ndiipp.dcmetadata;

import org.oclc.da.ndiipp.entity.EntityConst;

/**
  * EntitySeriesTranslationMap.java
  *
  * Maps element names between the Series object and Entity objects. 
  *
  * @author LAH
  *
  */
public class EntityDCTranslationMap extends TranslationMap {

    /** 
     *  constructor
     */
    public EntityDCTranslationMap() {
        super();
        map.add(new TranslationMapElement(EntityConst.KEY_NAME, DCElementSetNames.DCCREATOR, "", ""));
        map.add(new TranslationMapElement(EntityConst.SUBJ_HEAD, DCElementSetNames.DCSUBJECT, "", ""));
        map.add(new TranslationMapElement(EntityConst.HISTORY, DCElementSetNames.DCDESCRIPTION, "", ""));
        map.add(new TranslationMapElement(EntityConst.FUNCTIONS, DCElementSetNames.DCDESCRIPTION, "", ""));
        map.add(new TranslationMapElement(EntityConst.BEGIN_DATE_EXT, DCElementSetNames.DCCOVERAGE, DCElementSetNames.QUALTEMPORAL, ""));
        map.add(new TranslationMapElement(EntityConst.END_DATE_EXT, DCElementSetNames.DCCOVERAGE, DCElementSetNames.QUALTEMPORAL, ""));
    }
}
