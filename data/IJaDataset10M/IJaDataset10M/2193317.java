package org.oclc.da.ndiipp.dcmetadata;

import org.oclc.da.ndiipp.entity.EntityConst;
import org.oclc.da.ndiipp.packager.data.PDI;
import org.oclc.da.ndiipp.series.SeriesConst;

/**
  * SeriesDCTranslationMap.java
  *
  * Maps element names between the Series object and Dublin Core objects. 
  *
  * @author LAH
  *
  */
public class SeriesDCTranslationMap extends TranslationMap {

    /** 
     *  constructor
     */
    public SeriesDCTranslationMap() {
        super();
        map.add(new TranslationMapElement(SeriesConst.TITLE, DCElementSetNames.DCTITLE, "", ""));
        map.add(new TranslationMapElement(SeriesConst.TITLE, DCElementSetNames.DCRELATION, DCElementSetNames.QUALISPARTOF, ""));
        map.add(new TranslationMapElement(SeriesConst.WEBSITE, DCElementSetNames.DCSOURCE, "", DCElementSetNames.SCHEMEURI));
        map.add(new TranslationMapElement(SeriesConst.SERIES_UNIQUE_ID, DCElementSetNames.DCIDENTIFIER, DCElementSetNames.QUALLOCALID, ""));
        map.add(new TranslationMapElement(SeriesConst.SERIES_LOCAL_ID, DCElementSetNames.DCIDENTIFIER, DCElementSetNames.QUALLOCALID, ""));
        map.add(new TranslationMapElement(SeriesConst.DESCRIPTION, DCElementSetNames.DCDESCRIPTION, "", ""));
        map.add(new TranslationMapElement(SeriesConst.ENTITY_NAME, DCElementSetNames.DCCREATOR, "", ""));
        map.add(new TranslationMapElement(SeriesConst.SERIES_OCLC_NUMBER, DCElementSetNames.DCIDENTIFIER, DCElementSetNames.QUALOCLCNUMBER, ""));
        map.add(new TranslationMapElement(SeriesConst.RELATED_TITLES, DCElementSetNames.DCRELATION, DCElementSetNames.QUALISPARTOF, ""));
        map.add(new TranslationMapElement(SeriesConst.RELATED_TEXTS, DCElementSetNames.DCRELATION, DCElementSetNames.QUALISPARTOF, ""));
    }

    /** Test routines for this class
     * @param args 
     * @throws Exception 
     *
     */
    public static void main(String args[]) throws Exception {
        PDI pdi = new PDI();
        SeriesDCTranslationMap map = new SeriesDCTranslationMap();
        System.out.println("Title DC: " + map.matchLeftElement(EntityConst.AUTHORITY_NAME) + "\nTitle Series: " + map.matchRightElement(SeriesConst.ENTITY_NAME));
    }
}
