package org.oclc.da.ndiipp.common.pvt.storage.dbupgrade;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.informationobject.InformationObjectType;
import org.oclc.da.ndiipp.common.DataContainer;
import org.oclc.da.ndiipp.common.DataDictionary;
import org.oclc.da.ndiipp.common.pvt.DataDictionaryDataContainer;
import org.oclc.da.ndiipp.common.pvt.storage.db.RDBMS;

/**
 * This class will handle manager-level conversions to go from DB
 * version 4 to DB version 5.
 *
 * @author Marsel Tadger
 *
 */
public class ConvertV4_V5 extends Converter {

    /** Hold list of all institution GUIDs. */
    private String[] instGUIDs = null;

    private static String DATE_TEMPLATE = "yyyy-MM-dd";

    /** Construct a Converter instance.
     */
    public ConvertV4_V5() {
    }

    /** Run conversion 
     * @throws DAException 
     */
    public void convert() throws DAException {
        instGUIDs = getAllInstGUIDs();
        convertDates();
    }

    /** Convert spider settings. Use old domain spider options as a basis
     * to create new default spider settings. 
     * @throws DAException 
     */
    private void convertDates() throws DAException {
        RDBMS rdbmsEntity = null;
        RDBMS rdbmsSeries = null;
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TEMPLATE);
        try {
            rdbmsEntity = new RDBMS("ENTITY", new DataDictionary(InformationObjectType.ENTITY, 0));
            rdbmsSeries = new RDBMS("SERIES", new DataDictionary(InformationObjectType.SERIES, 0));
            for (int index = 0; index < instGUIDs.length; index++) {
                DataContainer oldObj = null;
                String[] guids_ent = findAll(rdbmsEntity, instGUIDs[index]);
                for (int index2 = 0; index2 < guids_ent.length; index2++) {
                    oldObj = new DataContainer(InformationObjectType.ENTITY);
                    rdbmsEntity.read(guids_ent[index2], instGUIDs[index], oldObj);
                    DataDictionaryDataContainer dddc = new DataDictionaryDataContainer(oldObj, new DataDictionary(InformationObjectType.ENTITY));
                    Date date_beginDate = (Date) dddc.getAttr("BEGIN_DATE");
                    String date_begin = formatter.format(date_beginDate);
                    oldObj.setAttr("BEGIN_DATE_EXT", date_begin);
                    Date date_endDate = (Date) dddc.getAttr("END_DATE");
                    String date_end = formatter.format(date_endDate);
                    oldObj.setAttr("END_DATE_EXT", date_end);
                    rdbmsEntity.update(oldObj);
                }
                String[] guids_series = findAll(rdbmsSeries, instGUIDs[index]);
                for (int index2 = 0; index2 < guids_series.length; index2++) {
                    oldObj = new DataContainer(InformationObjectType.SERIES);
                    rdbmsSeries.read(guids_series[index2], instGUIDs[index], oldObj);
                    DataDictionaryDataContainer dddc = new DataDictionaryDataContainer(oldObj, new DataDictionary(InformationObjectType.SERIES));
                    Date dateCreated = (Date) dddc.getAttr("CREATED_DATE");
                    String date_created = formatter.format(dateCreated);
                    oldObj.setAttr("CREATED_DATE_EXT", date_created);
                    rdbmsSeries.update(oldObj);
                }
            }
        } finally {
            closeRDBMS(rdbmsEntity);
            closeRDBMS(rdbmsSeries);
        }
    }
}
