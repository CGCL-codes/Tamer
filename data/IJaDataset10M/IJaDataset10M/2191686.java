package com.patientis.business.plugins;

import java.util.List;
import com.patientis.business.results.PatientFormRecordListController;
import com.patientis.model.clinical.FormRecordModel;

public class EHPatientFormRecordListController extends PatientFormRecordListController {

    private static final String EH_DIAGNOSIS_RANK_TABLE_COLUMN_NAME = "Rank";

    private boolean isDiagnosisRankColumn(String columnName) {
        return columnName.startsWith(EH_DIAGNOSIS_RANK_TABLE_COLUMN_NAME);
    }

    public EHPatientFormRecordListController() {
    }

    private Integer getDiagnosisRank(FormRecordModel record) {
        for (int i = 0; i < getSettings().getColumns().size(); i++) {
            if (isDiagnosisRankColumn(getSettings().getColumns().get(i).getColumnName())) {
                try {
                    return Integer.valueOf(getDisplayText(record, i));
                } catch (Exception e) {
                }
            }
        }
        return Integer.valueOf(0);
    }

    @Override
    public void setAlternateSortOrder(List<FormRecordModel> records) {
        System.out.println("setAlternateSortOrder called with " + records.size() + " records");
        Integer rank = 0;
        for (FormRecordModel record : records) {
            rank = getDiagnosisRank(record);
            System.out.println("set record " + record.getRecordSequence() + " to rank " + rank);
            record.setAlternateSort(rank);
        }
    }

    @Override
    public boolean refreshRecords() throws Exception {
        System.out.println("refresh records called");
        return super.refreshRecords();
    }
}
