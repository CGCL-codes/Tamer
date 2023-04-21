package fr.soleil.mambo.data.view;

import fr.soleil.comete.dao.AbstractKey;
import fr.soleil.comete.dao.util.DefaultMatrixDAO;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.mambo.models.ViewSpectrumTableModel;

public class ViewDAOKey extends AbstractKey {

    public static final int TYPE_NUMBER_SCALAR = 0;

    public static final int TYPE_BOOLEAN_SCALAR = 1;

    public static final int TYPE_STRING_SCALAR = 2;

    public static final int TYPE_STATE_SCALAR = 3;

    public static final int TYPE_NUMBER_SPECTRUM = 4;

    public static final int TYPE_BOOLEAN_SPECTRUM = 5;

    public static final int TYPE_STRING_SPECTRUM = 6;

    public static final int TYPE_STATE_SPECTRUM = 7;

    public ViewDAOKey(DefaultMatrixDAO defaultMatrixDAO) {
        super(MamboViewDAOFactory.class.getName());
        registerProperty("dao", defaultMatrixDAO);
    }

    public ViewDAOKey(int typeData, String idViewSelected) {
        super(MamboViewDAOFactory.class.getName());
        registerProperty("viewSelectedId", idViewSelected);
        registerProperty("dataType", Integer.valueOf(typeData));
    }

    public ViewDAOKey(int typeData, DbData[] splitedData) {
        this(typeData, splitedData, null, null, false, false);
    }

    /**
     * Constructor for chart spectrum view
     * 
     * @param typeData
     * @param splitedData
     * @param model
     * @param spectrumViewType
     * @param subtractMean
     * @param compareMode
     */
    public ViewDAOKey(int typeData, DbData[] splitedData, ViewSpectrumTableModel model, Integer spectrumViewType, boolean subtractMean, boolean compareMode) {
        super(MamboViewDAOFactory.class.getName());
        registerProperty("dataType", Integer.valueOf(typeData));
        registerProperty("splitedData", splitedData);
        if (model != null) {
            registerProperty("model", model);
        }
        if (spectrumViewType != null) {
            registerProperty("spectrumType", spectrumViewType);
        }
        registerProperty("subtractMean", Boolean.valueOf(subtractMean));
        registerProperty("compareMode", Boolean.valueOf(compareMode));
    }

    /**
     * Constructor for stack chart view
     * 
     * @param splitedData
     * @param checkBoxSelected
     */
    public ViewDAOKey(DbData[] splitedData, int checkBoxSelected) {
        super(MamboViewDAOFactory.class.getName());
        registerProperty("splitedData", splitedData);
        registerProperty("checkBoxSelected", checkBoxSelected);
    }

    @Override
    public String getInformationKey() {
        return null;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
