package com.rooster.c2c_candidate.submission;

import javax.sql.rowset.CachedRowSet;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class GetMySubmission {

    Hashtable table;

    String sEmailId = new String("");

    String DB_URL;

    String DB_USERNAME;

    String DB_PASSWORD;

    /** Creates a new instance of SearchTreePopulation */
    public GetMySubmission(String sEmailId, String DB_URL, String DB_USERNAME, String DB_PASSWORD) {
        this.sEmailId = sEmailId;
        this.setDB_URL(DB_URL);
        this.setDB_USERNAME(DB_USERNAME);
        this.setDB_PASSWORD(DB_PASSWORD);
    }

    private CachedRowSet getRs(String sSql) {
        CachedRowSet crs = new GetMySubmissionDB(this.getDB_URL(), this.getDB_USERNAME(), this.getDB_PASSWORD()).getRs(sSql);
        return crs;
    }

    public String getSearchContent() {
        String sFinalString = "";
        putStateCode();
        try {
            sFinalString = "<div class='smallgraytext' align='left'>My Submissions<ul class='mktree' id='my_submissions_tree'>";
            String sSql_State = "select distinct req_state from vendor_consultant_submission where visible=1 and candidate_approved=1 and email_id='" + sEmailId + "';";
            CachedRowSet crs_state = getRs(sSql_State);
            while (crs_state.next()) {
                String sState = crs_state.getString(1);
                String sStateName = String.valueOf(table.get(sState));
                if (sStateName.equals(new String("null"))) {
                    sStateName = sState;
                }
                if (sState.equals(new String("C1"))) {
                    sStateName = "California North";
                }
                if (sState.equals(new String("C2"))) {
                    sStateName = "California South";
                }
                String sStateJobCount = getJobCountForState(sState);
                sFinalString += "<li>" + sStateName + "[" + sStateJobCount + "]<ul>";
                String sSql_Location = "select distinct req_city from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and email_id='" + sEmailId + "';";
                CachedRowSet crs_Location = getRs(sSql_Location);
                while (crs_Location.next()) {
                    String sLocation = crs_Location.getString(1);
                    String sLocationJobCount = getJobCountForLocation(sState, sLocation);
                    sFinalString += "<li>" + sLocation + "[" + sLocationJobCount + "]<ul>";
                    String sSql_Req = "select distinct basic_skill from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and email_id='" + sEmailId + "';";
                    CachedRowSet crs_Req = getRs(sSql_Req);
                    while (crs_Req.next()) {
                        String sSkill = crs_Req.getString(1);
                        String sSkillJobCount = getJobCountForSkill(sState, sLocation, sSkill);
                        sFinalString += "<li>" + sSkill + "[" + sSkillJobCount + "]<ul>";
                        String sSql_jobid = "select distinct clrJobId from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and basic_skill='" + sSkill + "' and email_id='" + sEmailId + "';";
                        CachedRowSet crs_job_id = getRs(sSql_jobid);
                        while (crs_job_id.next()) {
                            String sJobId = crs_job_id.getString(1);
                            sFinalString += "<li><a href='C2C_job_detail.do?jobId=" + sJobId + "'onmouseover=\"getRequirementDetails('" + sJobId + "')\">" + sJobId + "</a></li>";
                        }
                        sFinalString += "</ul></li>";
                    }
                    sFinalString += "</ul></li>";
                }
                sFinalString += "</ul></li>";
            }
            sFinalString += "</ul></div>";
        } catch (Exception e) {
            e.printStackTrace();
            sFinalString = "ERROR";
        }
        return sFinalString;
    }

    private void putStateCode() {
        table = new Hashtable();
        table.put("AL", "Alabama");
        table.put("AK", "Alaska");
        table.put("AZ", "Arizona");
        table.put("AR", "Arkansas");
        table.put("CA", "California");
        table.put("CO", "Colorado");
        table.put("CT", "Connecticut");
        table.put("DE", "Delaware");
        table.put("FL", "Florida");
        table.put("GA", "Georgia");
        table.put("HI", "Hawaii");
        table.put("ID", "Idaho");
        table.put("IL", "Illinois");
        table.put("IN", "Indiana");
        table.put("IA", "Iowa");
        table.put("KS", "Kansas");
        table.put("KY", "Kentucky");
        table.put("LA", "Louisiana");
        table.put("ME", "Maine");
        table.put("MD", "Maryland");
        table.put("MA", "Massachusetts");
        table.put("MI", "Michigan");
        table.put("MN", "Minnesota");
        table.put("MS", "Mississippi");
        table.put("MO", "Missouri");
        table.put("MT", "Montana");
        table.put("NE", "Nebraska");
        table.put("NV", "Nevada");
        table.put("NH", "New Hampshire");
        table.put("NJ", "New Jersey");
        table.put("NM", "New Mexico");
        table.put("NY", "New York");
        table.put("NC", "North Carolina");
        table.put("ND", "North Dakota");
        table.put("OH", "Ohio");
        table.put("OK", "Oklahoma");
        table.put("OR", "Oregon");
        table.put("PA", "Pennsylvania");
        table.put("PR", "Puerto Rico");
        table.put("RI", "Rhode Island");
        table.put("SC", "South Carolina");
        table.put("SD", "South Dakota");
        table.put("TN", "Tennessee");
        table.put("TX", "Texas");
        table.put("UT", "Utah");
        table.put("VT", "Vermont");
        table.put("VA", "Virginia");
        table.put("WA", "Washington");
        table.put("DC", "Washington D.C.");
        table.put("WV", "West Virginia");
        table.put("WI", "Wisconsin");
        table.put("WY", "Wyoming");
    }

    private String getJobCountForState(String sState) {
        int iLocCount = 0;
        try {
            String sSql_Location = "select distinct req_city from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and email_id='" + sEmailId + "';";
            CachedRowSet crs_Location = getRs(sSql_Location);
            while (crs_Location.next()) {
                String sLocation = crs_Location.getString(1);
                String sSql_Req = "select distinct clrJobId from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and email_id='" + sEmailId + "';";
                CachedRowSet crs_Req = getRs(sSql_Req);
                while (crs_Req.next()) {
                    String sReq = crs_Req.getString(1);
                    String sSql_emailid = "select distinct email_id from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and clrJobId='" + sReq + "' and email_id='" + sEmailId + "';";
                    CachedRowSet crs_emailid = getRs(sSql_emailid);
                    crs_emailid.last();
                    int iLoc = crs_emailid.getRow();
                    iLocCount += iLoc;
                    crs_emailid.beforeFirst();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(iLocCount);
    }

    private String getJobCountForLocation(String sState, String sLocation) {
        int iLocCount = 0;
        try {
            String sSql_Req = "select distinct clrJobId from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and email_id='" + sEmailId + "';";
            CachedRowSet crs_Req = getRs(sSql_Req);
            while (crs_Req.next()) {
                String sReq = crs_Req.getString(1);
                String sSql_emailid = "select distinct email_id from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and clrJobId='" + sReq + "' and email_id='" + sEmailId + "';";
                CachedRowSet crs_emailid = getRs(sSql_emailid);
                crs_emailid.last();
                int iLoc = crs_emailid.getRow();
                iLocCount += iLoc;
                crs_emailid.beforeFirst();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(iLocCount);
    }

    private String getJobCountForSkill(String sState, String sLocation, String sSkill) {
        int iLocCount = 0;
        try {
            String sSql_emailid = "select distinct email_id from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and basic_skill='" + sSkill + "' and email_id='" + sEmailId + "';";
            CachedRowSet crs_emailid = getRs(sSql_emailid);
            crs_emailid.last();
            int iLoc = crs_emailid.getRow();
            iLocCount += iLoc;
            crs_emailid.beforeFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(iLocCount);
    }

    public String getStateSearch() {
        String sFinalString = "";
        putStateCode();
        try {
            sFinalString = "<div class='smallgraytext' align='left'>My Submissions<ul class='mktree' id='my_submissions_tree'>";
            String sSql_State = "select distinct req_state from vendor_consultant_submission where visible=1 and candidate_approved=1 and email_id='" + sEmailId + "';";
            CachedRowSet crs_state = getRs(sSql_State);
            while (crs_state.next()) {
                String sState = crs_state.getString(1);
                String sStateName = String.valueOf(table.get(sState));
                if (sStateName.equals(new String("null"))) {
                    sStateName = sState;
                }
                if (sState.equals(new String("C1"))) {
                    sStateName = "California North";
                }
                if (sState.equals(new String("C2"))) {
                    sStateName = "California South";
                }
                String sStateJobCount = getJobCountForState(sState);
                sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "'\">" + sStateName + "[" + sStateJobCount + "]<ul>";
                sFinalString += "</ul></li>";
            }
            sFinalString += "</ul></div>";
        } catch (Exception e) {
            sFinalString = "ERROR";
        }
        return sFinalString;
    }

    public String getStateCitySearch() {
        String sFinalString = "";
        putStateCode();
        try {
            sFinalString = "<div class='smallgraytext' align='left'>My Submissions<ul class='mktree' id='my_submissions_tree'>";
            String sSql_State = "select distinct req_state from vendor_consultant_submission where visible=1 and candidate_approved=1 and email_id='" + sEmailId + "';";
            CachedRowSet crs_state = getRs(sSql_State);
            while (crs_state.next()) {
                String sState = crs_state.getString(1);
                String sStateName = String.valueOf(table.get(sState));
                if (sStateName.equals(new String("null"))) {
                    sStateName = sState;
                }
                if (sState.equals(new String("C1"))) {
                    sStateName = "California North";
                }
                if (sState.equals(new String("C2"))) {
                    sStateName = "California South";
                }
                String sStateJobCount = getJobCountForState(sState);
                sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "'\">" + sStateName + "[" + sStateJobCount + "]<ul>";
                String sSql_Location = "select distinct req_city from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and email_id='" + sEmailId + "';";
                CachedRowSet crs_Location = getRs(sSql_Location);
                while (crs_Location.next()) {
                    String sLocation = crs_Location.getString(1);
                    String sLocationJobCount = getJobCountForLocation(sState, sLocation);
                    sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "&location=" + sLocation + "'\">" + sLocation + "[" + sLocationJobCount + "]<ul>";
                    sFinalString += "</ul></li>";
                }
                sFinalString += "</ul></li>";
            }
            sFinalString += "</ul></div>";
        } catch (Exception e) {
            sFinalString = "ERROR" + e;
        }
        return sFinalString;
    }

    public String getStateCitySkillSearch() {
        String sFinalString = "";
        putStateCode();
        try {
            sFinalString = "<div class='smallgraytext' align='left'>My Submissions<ul class='mktree' id='my_submissions_tree'>";
            String sSql_State = "select distinct req_state from vendor_consultant_submission where visible=1 and candidate_approved=1 and email_id='" + sEmailId + "';";
            CachedRowSet crs_state = getRs(sSql_State);
            while (crs_state.next()) {
                String sState = crs_state.getString(1);
                String sStateName = String.valueOf(table.get(sState));
                if (sStateName.equals(new String("null"))) {
                    sStateName = sState;
                }
                if (sState.equals(new String("C1"))) {
                    sStateName = "California North";
                }
                if (sState.equals(new String("C2"))) {
                    sStateName = "California South";
                }
                String sStateJobCount = getJobCountForState(sState);
                sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "'\">" + sStateName + "[" + sStateJobCount + "]<ul>";
                String sSql_Location = "select distinct req_city from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and email_id='" + sEmailId + "';";
                CachedRowSet crs_Location = getRs(sSql_Location);
                while (crs_Location.next()) {
                    String sLocation = crs_Location.getString(1);
                    String sLocationJobCount = getJobCountForLocation(sState, sLocation);
                    sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "&location=" + sLocation + "'\">" + sLocation + "[" + sLocationJobCount + "]<ul>";
                    String sSql_Req = "select distinct clrJobId from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and email_id='" + sEmailId + "';";
                    CachedRowSet crs_Req = getRs(sSql_Req);
                    while (crs_Req.next()) {
                        String sReq = crs_Req.getString(1);
                        String sReqCount = getJobCountForSkill(sState, sLocation, sReq);
                        sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "&location=" + sLocation + "&skill=" + sReq + "'\">" + sReq + "[" + sReqCount + "]<ul>";
                        sFinalString += "</ul></li>";
                    }
                    sFinalString += "</ul></li>";
                }
                sFinalString += "</ul></li>";
            }
            sFinalString += "</ul></div>";
        } catch (Exception e) {
            sFinalString = "ERROR";
        }
        return sFinalString;
    }

    public String getStateCitySkillReqSearch() {
        String sFinalString = "";
        putStateCode();
        try {
            sFinalString = "<div class='smallgraytext' align='left'>My Submissions<ul class='mktree' id='my_submissions_tree'>";
            String sSql_State = "select distinct req_state from vendor_consultant_submission where visible=1 and candidate_approved=1 and email_id='" + sEmailId + "';";
            CachedRowSet crs_state = getRs(sSql_State);
            while (crs_state.next()) {
                String sState = crs_state.getString(1);
                String sStateName = String.valueOf(table.get(sState));
                if (sStateName.equals(new String("null"))) {
                    sStateName = sState;
                }
                if (sState.equals(new String("C1"))) {
                    sStateName = "California North";
                }
                if (sState.equals(new String("C2"))) {
                    sStateName = "California South";
                }
                String sStateJobCount = getJobCountForState(sState);
                sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "'\">" + sStateName + "[" + sStateJobCount + "]<ul>";
                String sSql_Location = "select distinct req_city from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and email_id='" + sEmailId + "';";
                CachedRowSet crs_Location = getRs(sSql_Location);
                while (crs_Location.next()) {
                    String sLocation = crs_Location.getString(1);
                    String sLocationJobCount = getJobCountForLocation(sState, sLocation);
                    sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "&location=" + sLocation + "'\">" + sLocation + "[" + sLocationJobCount + "]<ul>";
                    String sSql_Skill = "select distinct clrJobId from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and email_id='" + sEmailId + "';";
                    CachedRowSet crs_Skill = getRs(sSql_Skill);
                    while (crs_Skill.next()) {
                        String sSkill = crs_Skill.getString(1);
                        String sSkillJobCount = getJobCountForSkill(sState, sLocation, sSkill);
                        sFinalString += "<li onclick=\"window.location='underconstruction.do?req_state=" + sState + "&state_code=" + sStateName + "&location=" + sLocation + "&skill=" + sSkill + "'\">" + sSkill + "[" + sSkillJobCount + "]<ul>";
                        String sSql_jobid = "select distinct email_id from vendor_consultant_submission where visible=1 and candidate_approved=1 and req_state='" + sState + "' and req_city = '" + sLocation + "' and clrJobId='" + sSkill + "' and email_id='" + sEmailId + "';";
                        CachedRowSet crs_jobid = getRs(sSql_jobid);
                        while (crs_jobid.next()) {
                            String sClrjobId = crs_jobid.getString(1);
                            sFinalString += "<li><a href='underconstruction.do?multiBoxReqId=" + sClrjobId + "&req_state=" + sState + "&state_code=" + sStateName + "&location=" + sLocation + "&skill=" + sSkill + "'>" + sClrjobId + "</a></li>";
                        }
                        sFinalString += "</ul></li>";
                    }
                    sFinalString += "</ul></li>";
                }
                sFinalString += "</ul></li>";
            }
            sFinalString += "</ul></div>";
        } catch (Exception e) {
            sFinalString = "ERROR";
        }
        return sFinalString;
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public void setDB_URL(String db_url) {
        DB_URL = db_url;
    }

    public String getDB_USERNAME() {
        return DB_USERNAME;
    }

    public void setDB_USERNAME(String db_username) {
        DB_USERNAME = db_username;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public void setDB_PASSWORD(String db_password) {
        DB_PASSWORD = db_password;
    }
}
