package jsynoptic.plugins.merge.ui;

import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import jsynoptic.plugins.merge.JSAsynchronousMergeDSCollection;
import jsynoptic.plugins.merge.JSSynchronousMergeDSCollection;
import jsynoptic.ui.JSynoptic;
import simtools.data.DataInfo;
import simtools.data.DataSource;
import simtools.data.DataSourceCollection;
import simtools.data.DataSourcePool;
import simtools.data.async.TimeStampedDataSource;
import simtools.data.async.TimeStampedDataSourceCollection;
import simtools.data.async.TimeStampedDataSourceCollection.InvalidFormatException;
import simtools.data.merge.MergeDSCollection;
import simtools.data.merge.MergeDataException;
import simtools.data.merge.SynchronousMergeDSCollection;
import simtools.ui.ReportingDialog;
import simtools.ui.WizardDisplayer;
import simtools.ui.WizardManager;
import simtools.ui.WizardPage;

/**
 * Type 
 * <br><b>Summary:</b><br>
 * A wizard dedicated to create asynchornous or asynchronous merge collections
 */
public class MCWizardManager extends WizardManager {

    protected WizardPage namePage, namePageOption, addPage, addSynchronousDataOption, addAsynchronousDataOption, terminatePage;

    protected static final String COLLECTION_NAME = "NAME";

    protected static final String IS_ASYNCHRONOUS = "IS_ASYNCHRONOUS";

    protected static final String COLLECTION_TIME_REFERENCE_IS_RELATIVE = "REF_TIME_FORMAT_IS_RELATIVE";

    protected static final String INTERPOLATION_ORDER = "INTERPOLATION_ORDER";

    protected static final String DATA_TO_MERGE = "DATA_TO_MERGE";

    protected static final String TIME_REFERENCE_OFFSET = "OFFSET";

    protected static final String TIME_REFERENCE = "TIME_REFERENCE";

    protected static final String TIME_REFERENCE_IS_RELATIVE = "TIME_REFERENCE_IS_RELATIVE";

    protected static final String TIME_REFERENCE_INITIAL_DATE = "TIME_REFERENCE_INITIAL_DATE";

    protected static final String REFERENCE_FOR_MERGED_TIME = "REFERENCE_FOR_MERGED_TIME";

    protected int numberOfAddedData;

    protected SimpleDateFormat dateTimeFormatter;

    public MCWizardManager() {
        super();
        numberOfAddedData = 0;
        dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
        dateTimeFormatter.setTimeZone(TimeStampedDataSourceCollection.timeStampedDateFormat.getTimeZone());
        namePage = new MCWizardPageName();
        namePageOption = new MCWizardPageNameOption();
        addPage = new MCWizardPageAdd(this);
        addSynchronousDataOption = new MCWizardPageAddSynchronousDataOption(this);
        addAsynchronousDataOption = new MCWizardPageAddAsynchronousDataOption(this);
        terminatePage = new MCWizardPageTerminate();
        currentPage = namePage;
    }

    public Dimension getMaximumnPageSize() {
        return addSynchronousDataOption.getPreferredSize();
    }

    public String[] getStepsTitles() {
        String[] titles = new String[5];
        titles[0] = namePage.getTitle();
        titles[1] = namePageOption.getTitle();
        titles[2] = addPage.getTitle();
        titles[3] = addSynchronousDataOption.getTitle();
        titles[4] = terminatePage.getTitle();
        return titles;
    }

    public boolean canNext() {
        return true;
    }

    protected WizardPage next() {
        if (currentPage.equals(namePage)) {
            if (((Boolean) settings.get("IS_ASYNCHRONOUS")).booleanValue()) return addPage; else return namePageOption;
        } else if (currentPage.equals(namePageOption)) {
            return addPage;
        } else if (currentPage.equals(addPage)) {
            Object o = settings.get("DATA_TO_MERGE_" + (numberOfAddedData - 1));
            if ((o instanceof TimeStampedDataSourceCollection) || (o instanceof TimeStampedDataSource)) {
                return addAsynchronousDataOption;
            } else {
                return addSynchronousDataOption;
            }
        } else if (currentPage.equals(addAsynchronousDataOption)) {
            ((MCWizardPageTerminate) terminatePage).setAddedDataTable(getAddedData());
            Integer mergedTimeReference = (Integer) settings.get(REFERENCE_FOR_MERGED_TIME);
            int index = 0;
            if (mergedTimeReference != null) {
                index = mergedTimeReference.intValue();
            }
            if (index >= numberOfAddedData) {
                index = numberOfAddedData - 1;
            }
            ((MCWizardPageTerminate) terminatePage).setMergedTimeReference(!(((Boolean) settings.get("IS_ASYNCHRONOUS")).booleanValue()), index);
            return terminatePage;
        } else if (currentPage.equals(addSynchronousDataOption)) {
            ((MCWizardPageTerminate) terminatePage).setAddedDataTable(getAddedData());
            Integer mergedTimeReference = (Integer) settings.get(REFERENCE_FOR_MERGED_TIME);
            int index = 0;
            if (mergedTimeReference != null) {
                index = mergedTimeReference.intValue();
            }
            if (index >= numberOfAddedData) {
                index = numberOfAddedData - 1;
            }
            ((MCWizardPageTerminate) terminatePage).setMergedTimeReference(!(((Boolean) settings.get("IS_ASYNCHRONOUS")).booleanValue()), index);
            return terminatePage;
        } else if (currentPage.equals(terminatePage)) {
            return addPage;
        }
        return null;
    }

    public boolean canFinish() {
        return currentPage.equals(terminatePage);
    }

    private Object[][] getAddedData() {
        Object[][] ret = new Object[numberOfAddedData][4];
        for (int i = 0; i < numberOfAddedData; i++) {
            Object o = settings.get(DATA_TO_MERGE + "_" + i);
            DataSource refTime = null;
            if (o instanceof TimeStampedDataSource) {
                refTime = ((TimeStampedDataSource) o).getTime();
            } else if (o instanceof TimeStampedDataSourceCollection) {
                refTime = ((TimeStampedDataSource) ((TimeStampedDataSourceCollection) o).get(0)).getTime();
            } else {
                refTime = (DataSource) settings.get(TIME_REFERENCE + "_" + i);
            }
            double offset = ((Double) settings.get(TIME_REFERENCE_OFFSET + "_" + i)).doubleValue();
            String di = "";
            if (settings.get(TIME_REFERENCE_INITIAL_DATE + "_" + i) != null) {
                long dateinit = ((Long) settings.get(TIME_REFERENCE_INITIAL_DATE + "_" + i)).longValue();
                di = dateTimeFormatter.format(new Date(dateinit));
            }
            ret[i][0] = DataInfo.getId(o);
            ret[i][1] = DataInfo.getId(refTime);
            ret[i][2] = new Double(offset);
            ret[i][3] = di;
        }
        return ret;
    }

    public Object processFinish() {
        MergeDataException.mergeDataErrors.clear();
        MergeDSCollection ret = null;
        String collectionName = (String) settings.get(COLLECTION_NAME);
        boolean isAsynchronous = ((Boolean) settings.get(IS_ASYNCHRONOUS)).booleanValue();
        if (isAsynchronous) {
            try {
                ret = new JSAsynchronousMergeDSCollection(collectionName);
            } catch (IOException e) {
                MergeDataException.mergeDataErrors.add(e.getMessage());
                ret = null;
            } catch (InvalidFormatException e) {
                MergeDataException.mergeDataErrors.add(e.getMessage());
                ret = null;
            }
        } else {
            int interpolationOrder = ((Integer) settings.get(INTERPOLATION_ORDER)).intValue();
            boolean collectionTimeReferenceIsRelative = ((Boolean) settings.get(COLLECTION_TIME_REFERENCE_IS_RELATIVE)).booleanValue();
            Integer mergedTimeRefIndex = (Integer) settings.get(REFERENCE_FOR_MERGED_TIME);
            int refTimeIndex = 0;
            if (mergedTimeRefIndex != null) {
                refTimeIndex = mergedTimeRefIndex.intValue();
            }
            Object o = settings.get(DATA_TO_MERGE + "_" + refTimeIndex);
            DataSource mergedTimeReferenceDs = null;
            if (o instanceof TimeStampedDataSource) {
                mergedTimeReferenceDs = ((TimeStampedDataSource) o).getTime();
            } else if (o instanceof TimeStampedDataSourceCollection) {
                mergedTimeReferenceDs = ((TimeStampedDataSource) ((TimeStampedDataSourceCollection) o).get(0)).getTime();
            } else {
                mergedTimeReferenceDs = (DataSource) settings.get(TIME_REFERENCE + "_" + refTimeIndex);
            }
            double offset = ((Double) settings.get(TIME_REFERENCE_OFFSET + "_" + refTimeIndex)).doubleValue();
            boolean refTimeIsRelative = false;
            if (settings.get(TIME_REFERENCE_IS_RELATIVE + "_" + refTimeIndex) != null) {
                refTimeIsRelative = ((Boolean) settings.get(TIME_REFERENCE_IS_RELATIVE + "_" + refTimeIndex)).booleanValue();
            }
            long initialDate = 0;
            if (settings.get(TIME_REFERENCE_INITIAL_DATE + "_" + refTimeIndex) != null) {
                initialDate = ((Long) settings.get(TIME_REFERENCE_INITIAL_DATE + "_" + refTimeIndex)).longValue();
            }
            try {
                ret = new JSSynchronousMergeDSCollection(collectionName, interpolationOrder, collectionTimeReferenceIsRelative, mergedTimeReferenceDs, refTimeIsRelative, offset, initialDate);
            } catch (MergeDataException e) {
                ret = null;
            }
        }
        if (ret != null) {
            for (int i = 0; i < numberOfAddedData; i++) {
                try {
                    Object o = settings.get(DATA_TO_MERGE + "_" + i);
                    double offset = ((Double) settings.get(TIME_REFERENCE_OFFSET + "_" + i)).doubleValue();
                    boolean refTimeIsRelative = false;
                    if (settings.get(TIME_REFERENCE_IS_RELATIVE + "_" + i) != null) refTimeIsRelative = ((Boolean) settings.get(TIME_REFERENCE_IS_RELATIVE + "_" + i)).booleanValue();
                    long initialDate = 0;
                    if (settings.get(TIME_REFERENCE_INITIAL_DATE + "_" + i) != null) initialDate = ((Long) settings.get(TIME_REFERENCE_INITIAL_DATE + "_" + i)).longValue();
                    if (o instanceof DataSourceCollection) {
                        if (o instanceof TimeStampedDataSourceCollection) {
                            ret.add((TimeStampedDataSourceCollection) o, offset, initialDate);
                        } else {
                            DataSource refTime = (DataSource) settings.get(TIME_REFERENCE + "_" + i);
                            ret.add((DataSourceCollection) o, refTime, refTimeIsRelative, offset, initialDate);
                        }
                    } else {
                        if (o instanceof TimeStampedDataSource) {
                            ret.add((TimeStampedDataSource) o, offset, initialDate);
                        } else {
                            DataSource refTime = (DataSource) settings.get(TIME_REFERENCE + "_" + i);
                            ret.add((DataSource) o, refTime, refTimeIsRelative, offset, initialDate);
                        }
                    }
                } catch (MergeDataException e) {
                }
            }
        }
        try {
            if ((ret != null) && (!((DataSourceCollection) ret).isEmpty())) {
                if (ret instanceof SynchronousMergeDSCollection) {
                    ((SynchronousMergeDSCollection) ret).mergeData();
                }
                DataSourcePool.global.addDataSourceCollection((DataSourceCollection) ret);
            }
        } catch (MergeDataException e) {
        }
        if (!MergeDataException.mergeDataErrors.isEmpty()) {
            JOptionPane.showMessageDialog(JSynoptic.gui.getOwner(), new ReportingDialog("Following errors have occured during merging process:", MergeDataException.mergeDataErrors), "Merging process errors", JOptionPane.INFORMATION_MESSAGE);
        }
        return (DataSourceCollection) ret;
    }

    public void processToNextStep() {
        if (currentPage.equals(addPage)) {
            numberOfAddedData++;
        }
        super.processToNextStep();
    }

    public void processToPreviousStep() {
        super.processToPreviousStep();
        if (currentPage.equals(addPage)) {
            numberOfAddedData--;
        }
        if (currentPage.equals(terminatePage)) {
            ((MCWizardPageTerminate) terminatePage).setAddedDataTable(getAddedData());
            Integer mergedTimeReference = (Integer) settings.get(REFERENCE_FOR_MERGED_TIME);
            int index = 0;
            if (mergedTimeReference != null) {
                index = mergedTimeReference.intValue();
            }
            if (index >= numberOfAddedData) {
                index = numberOfAddedData - 1;
            }
            ((MCWizardPageTerminate) terminatePage).setMergedTimeReference(!(((Boolean) settings.get("IS_ASYNCHRONOUS")).booleanValue()), index);
        }
    }

    public static void main(String[] args) {
        WizardDisplayer displayer = new WizardDisplayer(null, "Test", new MCWizardManager());
        displayer.show();
        System.exit(-1);
    }
}
