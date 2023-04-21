package djudge.acmcontester.server;

import java.util.Date;
import java.util.HashSet;
import org.apache.log4j.Logger;
import org.apache.commons.codec.binary.Base64;
import db.AbstractTableDataModel;
import db.DBRowAbstract;
import db.DBRowSubmissions;
import db.DBRowUsers;
import db.LanguagesDataModel;
import db.MonitorModel;
import db.ProblemsDataModel;
import db.SubmissionsDataModel;
import db.UsersDataModel;
import djudge.acmcontester.structures.LanguageData;
import djudge.acmcontester.structures.MonitorData;
import djudge.acmcontester.structures.ProblemData;
import djudge.acmcontester.structures.SubmissionData;
import djudge.acmcontester.structures.UserData;

public class ContestCoreInternals {

    private static final Logger log = Logger.getLogger(ContestCoreInternals.class);

    protected static final String versionString = "1.0";

    protected UsersDataModel usersModel;

    protected ProblemsDataModel problemsModel;

    protected LanguagesDataModel languagesModel;

    protected SubmissionsDataModel submissionsModel;

    protected MonitorModel monitorModel;

    protected final ContestSettings settings = new ContestSettings("contest.xml");

    protected final ContestState state = new ContestState(settings);

    protected ContesterServer2DServiceLink djudgeInterface;

    protected void initCore(boolean flagStandalone) {
        usersModel = new UsersDataModel();
        usersModel.updateData();
        problemsModel = new ProblemsDataModel();
        problemsModel.updateData();
        languagesModel = new LanguagesDataModel();
        languagesModel.updateData();
        submissionsModel = new SubmissionsDataModel();
        submissionsModel.updateData();
        if (flagStandalone) {
            djudgeInterface = new ContesterServer2DServiceLink();
            djudgeInterface.start();
        }
        monitorModel = new MonitorModel();
    }

    @SuppressWarnings("deprecation")
    public void stopCore() {
        if (djudgeInterface != null) {
            djudgeInterface.stop();
        }
    }

    public UsersDataModel getUsersModel() {
        return usersModel;
    }

    public LanguagesDataModel getLanguagesModel() {
        return languagesModel;
    }

    public ProblemsDataModel getProblemsModel() {
        return problemsModel;
    }

    public SubmissionsDataModel getSubmissionsDataModel() {
        return submissionsModel;
    }

    public boolean deleteAbstract(AbstractTableDataModel model, String id) {
        DBRowAbstract rd = model.getRowByID(id);
        if (rd == null) {
            log.info("Wrong ID: " + id);
            return false;
        }
        boolean res = rd.delete(model);
        if (res) {
            model.updateData();
            log.info("Deleted OK");
        }
        return res;
    }

    public boolean deleteAllAbstract(AbstractTableDataModel model) {
        if (model.deleteAllRows()) {
            log.info("Clearing successful");
            if (model instanceof UsersDataModel) {
                UsersDataModel um = (UsersDataModel) model;
                um.updateData();
                UserData ud = new UserData("root", "root", "root", "ADMIN");
                um.insertRow(um.toRow(ud));
                model.updateData();
            }
            return true;
        } else {
            log.info("Clearing failed");
            return false;
        }
    }

    protected boolean addLanguageCore(String sid, String shortName, String fullName, String compilationComand, String djudgeID) {
        LanguageData ld = new LanguageData(sid, shortName, fullName, compilationComand, djudgeID);
        DBRowAbstract rd = languagesModel.toRow(ld);
        boolean res = rd.appendTo(languagesModel);
        if (res) log.info("Language " + sid + " " + shortName + " " + fullName + " " + djudgeID + " added");
        return res;
    }

    protected boolean editLanguageCore(String id, String sid, String shortName, String fullName, String compilationComand, String djudgeID) {
        DBRowAbstract rd = languagesModel.getRowByID(id);
        if (rd == null) {
            log.info("Wrong ID: " + id);
            return false;
        }
        LanguageData ld = new LanguageData(id, sid, shortName, fullName, compilationComand, djudgeID);
        rd = languagesModel.toRow(ld);
        boolean res = rd.save();
        if (res) {
            languagesModel.updateData();
            log.info("Language " + id + " " + sid + " " + shortName + " " + fullName + " " + djudgeID + " changed");
        }
        return res;
    }

    protected boolean editUserCore(String id, String newUserName, String newPassword, String name, String role) {
        DBRowAbstract rd = usersModel.getRowByID(id);
        if (rd == null) {
            log.info("Wrong ID: " + id);
            return false;
        }
        UserData ud = new UserData(id, newUserName, newPassword, name, role);
        rd = usersModel.toRow(ud);
        if (rd.save()) {
            usersModel.updateData();
            log.debug("User editing finished");
            return true;
        }
        return false;
    }

    protected boolean submitSolutionCore(String userID, String problemID, String languageID, String courceCode, boolean fFirstTestOnly) {
        SubmissionData sd = new SubmissionData();
        sd.contestTime = (int) state.getContestTime();
        sd.languageID = languageID;
        sd.problemID = problemID;
        sd.sourceCode = new String(Base64.encodeBase64(courceCode.getBytes()));
        sd.userID = userID;
        sd.fFirstTestOnly = fFirstTestOnly ? 1 : 0;
        sd.active = fFirstTestOnly ? 0 : 1;
        DBRowAbstract row = submissionsModel.toRow(sd);
        if (row.appendTo(submissionsModel)) {
            log.info("Submissions added");
            return true;
        }
        return false;
    }

    protected boolean addProblemCore(String sid, String name, String djudgeProblem, String djudgeContest) {
        ProblemData pd = new ProblemData(sid, name, djudgeProblem, djudgeContest);
        DBRowAbstract rd = problemsModel.toRow(pd);
        if (rd.appendTo(problemsModel)) {
            log.info("Problem " + name + " (" + sid + ") added");
            problemsModel.updateData();
            return true;
        }
        return false;
    }

    protected boolean addUserCore(String newUserName, String newPassword, String name, String role) {
        UserData ud = new UserData(newUserName, newPassword, name, role);
        DBRowAbstract rd = usersModel.toRow(ud);
        if (rd.appendTo(usersModel)) {
            log.info("User " + newUserName + " like " + role + " added");
            usersModel.updateData();
            return true;
        }
        return false;
    }

    protected boolean changePasswordCore(String userID, String newPassword) {
        DBRowUsers row = (DBRowUsers) usersModel.getRowByID(userID);
        row.setPassword(newPassword);
        if (row.save()) {
            log.info("Password changed");
            return true;
        } else {
            log.info("Password change failed");
            usersModel.updateData();
            return false;
        }
    }

    protected boolean activateSubmissionInternal(String id, int active) {
        DBRowSubmissions rd = (DBRowSubmissions) submissionsModel.getRowByID(id);
        if (rd == null) return false;
        rd.data[SubmissionsDataModel.getActiveFlagIndex()] = active;
        rd.save();
        submissionsModel.updateData();
        return true;
    }

    protected boolean generateLoginsInternal(int count, String loginType) {
        log.info("Genarating logins: " + count + " of " + loginType);
        int counter = 1;
        HashSet<String> hs = new HashSet<String>();
        for (int i = 0; i < usersModel.getRowCount(); i++) hs.add(usersModel.getRow(i).data[1].toString());
        for (int i = 0; i < count; i++) {
            String t = "";
            while (true) {
                t = loginType.toLowerCase() + counter;
                if (!hs.contains(t)) {
                    hs.add(t);
                    break;
                }
                counter++;
            }
            UserData ud = new UserData();
            ud.name = ud.password = ud.username = t;
            ud.role = loginType;
            usersModel.insertRow(usersModel.toRow(ud));
            counter++;
        }
        usersModel.updateData();
        return true;
    }

    protected MonitorData getMonitorInternal(boolean ignoreFrozenFlag) {
        MonitorData res = null;
        if (!ignoreFrozenFlag && state.isFrozen()) {
            res = monitorModel.getMonitorACM(state.getContestTimeFrozen());
            res.contestTime = state.getContestTimeFrozen();
            res.isFrozen = true;
        } else {
            res = monitorModel.getMonitorACM(state.getContestTime());
            res.contestTime = state.getContestTime();
        }
        res.lastUpdateTime = new Date();
        res.contestName = settings.getContestName();
        return res;
    }
}
