package org.openXpertya.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MSurveyQuestion extends X_C_Survey_Question {

    public MSurveyQuestion(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public MSurveyQuestion(Properties ctx, int C_Survey_Question_ID, String trxName) {
        super(ctx, C_Survey_Question_ID, trxName);
    }

    protected boolean beforeSave(boolean newRecord) {
        if (get_Value("IsTitle").toString().equals("false") && get_Value("IsBoolean").toString().equals("false") && get_Value("IsNumber").toString().equals("false") && get_Value("IsRadioButton").toString().equals("false") && get_Value("IsText").toString().equals("false") && get_Value("IsListRef").toString().equals("false")) {
            log.saveError("Rellene Campos Obligatorios: ", "Debes seleccionar el tipo de pregunta que se trata");
            return false;
        } else if ((get_Value("IsRadioButton").toString().equals("true") || get_Value("IsListRef").toString().equals("true")) && get_Value("AD_Reference_ID") == null) {
            log.saveError("Rellene Campos Obligatorios: ", "Debes seleccionar una referencia");
            return false;
        }
        return true;
    }
}
