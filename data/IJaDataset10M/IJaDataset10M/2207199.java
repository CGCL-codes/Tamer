package system.model;

import org.json.simple.JSONObject;
import com.sysnet_pioneer.model.Model;

public class tb_sss_application extends Model {

    public tb_sss_application() {
        super.setPrimaryKey("sss_application_id_pk");
        super.setOneToMany("resident_id_pk", "sss_application_resident_id_fk", "tb_resident_info", "resident_id_pk");
        super.saveStructure();
    }

    int sss_application_id_pk;

    int sss_application_resident_id_fk;

    String sss_application_date_issued = new String();

    String sss_application_job_title = new String();

    String sss_application_monthly_income = new String();

    /**** Short WAY ****/
    @SuppressWarnings({ "unchecked" })
    private JSONObject isNull(String input) {
        JSONObject data = new JSONObject();
        if (input.equals("")) {
            data.put("error1", "required");
        }
        return data;
    }

    @SuppressWarnings({ "unchecked" })
    private JSONObject setData(String field, JSONObject errors) {
        JSONObject data = new JSONObject();
        data.put("fieldname", field);
        data.put("errors", errors);
        return data;
    }

    @SuppressWarnings({ "unchecked" })
    private JSONObject isInt(String input) {
        JSONObject data = new JSONObject();
        try {
            Integer.parseInt(input);
        } catch (Exception e) {
            data.put("error2", "int_only");
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    public JSONObject set_sss_application_resident_id_fk(String sss_application_resident_id_fk) {
        JSONObject data = new JSONObject();
        JSONObject errors = new JSONObject();
        errors.putAll(this.isNull(sss_application_resident_id_fk));
        errors.putAll(this.isInt(sss_application_resident_id_fk));
        if (errors.isEmpty()) {
            this.sss_application_resident_id_fk = Integer.parseInt(sss_application_resident_id_fk);
        } else {
            data = setData(sss_application_resident_id_fk, errors);
        }
        return data;
    }

    public JSONObject set_sss_application_date_issued(String sss_application_date_issued) {
        JSONObject data = new JSONObject();
        JSONObject errors = this.isNull(sss_application_date_issued);
        if (errors.isEmpty()) {
            this.sss_application_date_issued = sss_application_date_issued;
        } else {
            data = this.setData("sss_application_date_issued", errors);
        }
        return data;
    }

    public JSONObject set_sss_application_job_title(String sss_application_job_title) {
        JSONObject data = new JSONObject();
        JSONObject errors = this.isNull(sss_application_job_title);
        if (errors.isEmpty()) {
            this.sss_application_job_title = sss_application_job_title;
        } else {
            data = this.setData("sss_application_job_title", errors);
        }
        return data;
    }

    public JSONObject set_sss_application_monthly_income(String sss_application_monthly_income) {
        JSONObject data = new JSONObject();
        JSONObject errors = this.isNull(sss_application_monthly_income);
        if (errors.isEmpty()) {
            this.sss_application_monthly_income = sss_application_monthly_income;
        } else {
            data = this.setData("sss_application_monthly_income", errors);
        }
        return data;
    }
}
