package net.sf.ideoreport.tools;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import net.sf.ideoreport.api.datastructure.containers.data.IDataRecordset;
import net.sf.ideoreport.api.datastructure.containers.parameter.IParameterValues;
import net.sf.ideoreport.common.config.IDataProviderConfig;
import net.sf.ideoreport.data.transform.crosstab.DataCrossTab;
import net.sf.ideoreport.data.transform.crosstab.DataFieldDef;
import net.sf.ideoreport.data.transform.crosstab.DataFieldDefMode;
import net.sf.ideoreport.data.transform.crosstab.DataFieldDefOperator;
import net.sf.ideoreport.data.transform.crosstab.DataGroupFieldDef;

/**
 * Classe utilitaire pour g�n�rer des tableaux crois�s � partir d'une config donn�e
 * @author jbeausseron
 * TODO HTML REFACTORING basculer dans le package tools une fois OK
 */
public class DataCrossTabTools {

    /**
    * Logger for this class
    */
    private static final Log LOGGER = LogFactory.getLog(DataCrossTabTools.class);

    /**
    * Construit un tableau crois� en fonction des param�tres et des donn�es
    * @param pRecordset
    * @param pDataProviderConfig
    * @param pParameterValues
    * @return le tableau crois�
    */
    public static DataCrossTab buildCrossTab(IDataRecordset pRecordset, IDataProviderConfig pDataProviderConfig, IParameterValues pParameterValues) {
        DataCrossTab vRetour = null;
        DataGroupFieldDef vRowGroups = buildGroupFieldDefs("ROWS", pDataProviderConfig, pParameterValues);
        DataGroupFieldDef vColGroups = buildGroupFieldDefs("COLS", pDataProviderConfig, pParameterValues);
        List vDataFields = buildDataFieldDefs(pDataProviderConfig, pParameterValues);
        if (vRowGroups == null && vColGroups == null && vDataFields.size() == 0) {
        } else {
            vRetour = new DataCrossTab();
            vRetour.setColGroupsDef(vColGroups);
            vRetour.setRowGroupsDef(vRowGroups);
            vRetour.setDataFieldsDef(vDataFields);
            vRetour.populate(pRecordset);
        }
        return vRetour;
    }

    /**
    * Construit les d�finition de groupes en fonction de la config et du mode demand�. On renvoie le groupe racine,
    * qui lui-m�me contient les d�finitions des sous-groupes en cascade.
    * @param pMode COLUMN ou ROW
    * @param pDataProviderConfig configuration
    * @param pParameterValues jeu de param�tres
    */
    protected static DataGroupFieldDef buildGroupFieldDefs(String pMode, IDataProviderConfig pDataProviderConfig, IParameterValues pParameterValues) {
        String vConfigParamPrefix;
        DataGroupFieldDef vRetour = null;
        DataGroupFieldDef vCurrentGroup = null;
        int vGroupdefIndex = 1;
        String vGroupdefField;
        do {
            vConfigParamPrefix = "data.transform.crosstab.groups." + pMode.toLowerCase() + "." + vGroupdefIndex;
            vGroupdefField = pDataProviderConfig.getParameter(vConfigParamPrefix + ".fieldname", pParameterValues, "");
            if (!StringUtils.isEmpty(vGroupdefField)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("group definition found (" + pMode + ") on field [" + vGroupdefField + "]");
                }
                DataGroupFieldDef vGroup = new DataGroupFieldDef(vGroupdefField);
                if (vCurrentGroup == null) {
                    vCurrentGroup = vGroup;
                    vRetour = vGroup;
                } else {
                    vCurrentGroup.setSubField(vGroup);
                    vCurrentGroup = vGroup;
                }
            }
            vGroupdefIndex++;
        } while (!StringUtils.isEmpty(vGroupdefField));
        return vRetour;
    }

    /**
    * Construit les d�finition de champs en fonction des param�tres
    * @param pDataProviderConfig configuration
    * @param pParameterValues jeu de param�tres
    */
    protected static List buildDataFieldDefs(IDataProviderConfig pDataProviderConfig, IParameterValues pParameterValues) {
        String vConfigParamPrefix;
        List vRetour = new ArrayList();
        int vDataFieldIndex = 1;
        String vDataFieldName;
        String vOperatorName;
        String vModeName;
        do {
            vConfigParamPrefix = "data.transform.crosstab.datafields." + vDataFieldIndex;
            vDataFieldName = pDataProviderConfig.getParameter(vConfigParamPrefix + ".fieldname", pParameterValues, "");
            vOperatorName = pDataProviderConfig.getParameter(vConfigParamPrefix + ".operator", pParameterValues, "");
            vModeName = pDataProviderConfig.getParameter(vConfigParamPrefix + ".mode", pParameterValues, "NORMAL");
            if (!StringUtils.isEmpty(vDataFieldName)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("data field found (" + vOperatorName + ") on field [" + vDataFieldName + "]");
                }
                DataFieldDef vField = new DataFieldDef(vDataFieldName, DataFieldDefOperator.findOperator(vOperatorName), DataFieldDefMode.findMode(vModeName));
                vRetour.add(vField);
            }
            vDataFieldIndex++;
        } while (!StringUtils.isEmpty(vDataFieldName));
        return vRetour;
    }
}
