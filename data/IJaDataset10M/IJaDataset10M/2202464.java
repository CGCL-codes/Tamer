package xml;

import gui.MainProg;
import harmonic.HarmonicActionListener;
import java.awt.Component;
import java.io.File;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import xml.Enum.ItemType;
import xml.castor.HarmonicList;
import xml.castor.KangasSoundEditor;
import common.CursorWait;
import common.Dialogs;
import common.IListItemIter;
import database.DbHarmonic;
import database.HarmonicFactor;
import database.HarmonicParams;
import database.DbCommon.SelectionMode;

public class Harmonic extends EntityBase {

    static HarmonicFactor[] convertFromXML(double[] xmlParams) {
        HarmonicFactor[] params = new HarmonicFactor[xmlParams.length];
        for (int i = 0; i < params.length; i++) {
            double xmlParam = xmlParams[i];
            HarmonicFactor param = new HarmonicFactor(i + 1, xmlParam);
            params[i] = param;
        }
        return params;
    }

    private static void convertToXML(HarmonicParams params, xml.castor.Harmonic xmlHarmonic, KangasSoundEditor kSE, boolean exportDependentEnts, String exportPrefix, String exportSuffix) throws Exception {
        xmlHarmonic.setName(params.getName());
        EnumMap<ItemType, DBDispatcher> dispatchers = DBDispatcher.getDispatchers();
        DBDispatcher dbVariationDispatcher = dispatchers.get(Enum.ItemType.VARIATIONS);
        int variationId = params.getVariationId();
        if (variationId > 0) {
            xmlHarmonic.setVariation(dbVariationDispatcher.load(variationId, kSE, exportDependentEnts, exportPrefix, exportSuffix));
        }
        byte flags = params.getFlags();
        xmlHarmonic.setLinkedVolumeRecalc((flags & HarmonicActionListener.LINKED_VOL_RECALC_BIT) != 0);
        xmlHarmonic.setSkipInitialVolRecalc((flags & HarmonicActionListener.SKIP_INITIAL_VOL_RECALC_BIT) != 0);
        xmlHarmonic.setAntiHarmonicAlgorithm((flags & HarmonicActionListener.ANTI_HARMONIC_ALGORITHM_BIT) != 0);
        HarmonicFactor[] harmonicFactors = params.getHarmonicFactors();
        double[] xmlParams = new double[harmonicFactors.length];
        for (int i = 0; i < xmlParams.length; i++) {
            HarmonicFactor factor = harmonicFactors[i];
            xmlParams[i] = factor.getHarmonicFactor();
        }
        xmlHarmonic.setFactor(xmlParams);
    }

    /**
     * Converts harmonic in harmonicParams to XML format, and adds it to harmonicList.
     * Returns true on success, false if an error occurred.
     */
    static boolean exportHarmonic(String exportPrefix, String exportSuffix, HarmonicList harmonicList, HarmonicParams harmonicParams, KangasSoundEditor KSE, boolean exportDependentEnts) {
        xml.castor.Harmonic xmlHarmonic = new xml.castor.Harmonic();
        try {
            convertToXML(harmonicParams, xmlHarmonic, KSE, exportDependentEnts, exportPrefix, exportSuffix);
        } catch (Exception e) {
            e.printStackTrace();
            Dialogs.showExceptionDialogEventDispatch("Export error:convertToXML", e, null);
            return false;
        }
        addPrefixSuffix(xmlHarmonic, exportPrefix, exportSuffix);
        harmonicList.addHarmonic(xmlHarmonic);
        return true;
    }

    public static void export(Component parent, String nameSpec, boolean regExp, Set itemIds, SelectionMode selectionMode) {
        final ExportDialog dlg = new ExportDialog((JFrame) parent.getParent());
        dlg.showDialog("XML Export Run-time Parameters (Harmonics)", true);
        if (dlg.isSaved()) {
            final File exportFile = ExportSaveAsDialog.saveAsDialog(parent);
            if (exportFile != null) {
                DbHarmonic.list(parent, nameSpec, regExp, true, 0, new DbHarmonic.IListHarmonicResults() {

                    public void setCount(int variationCount) {
                    }

                    @Override
                    public void setHarmonics(List<HarmonicParams> harmonics) {
                        CursorWait.showWait(MainProg.getFrame());
                        try {
                            String exportPrefix = dlg.getXmlExportPrefix();
                            String exportSuffix = dlg.getXmlExportSuffix();
                            boolean exportDependentEnts = dlg.exportDependentEnts();
                            xml.castor.KangasSoundEditor KSE = exportInit(true);
                            xml.castor.HarmonicList harmonicList = new xml.castor.HarmonicList();
                            for (HarmonicParams harmonic : harmonics) {
                                if (!exportHarmonic(exportPrefix, exportSuffix, harmonicList, harmonic, KSE, exportDependentEnts)) {
                                    Dialogs.showCompleteDialog("Harmonic XML export failed", false);
                                    return;
                                }
                            }
                            KSE.setHarmonicList(harmonicList);
                            exportTerm(exportFile, KSE, "Harmonic");
                        } finally {
                            CursorWait.showNormal(MainProg.getFrame());
                        }
                    }

                    public void setIter(IListItemIter iter) {
                    }

                    public void setNames(List names) {
                    }

                    @Override
                    public void setHarmonicIds(List harmonicIds) {
                    }
                }, false, itemIds, selectionMode, false);
            }
        }
    }

    protected static void addPrefixSuffix(xml.castor.Harmonic xmlHarmonic, String exportPrefix, String exportSuffix) {
        xmlHarmonic.setName(DBDispatcher.addPrefixSuffix(xmlHarmonic.getName(), exportPrefix, exportSuffix));
    }
}
