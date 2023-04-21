package com.rooster.process.resume;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.rooster.bean.resume.SummaryBean;
import com.rooster.constants.PropertyFileConst;
import com.rooster.mail.RoosterMailer;
import com.rooster.utils.zip.ZipUtil;

public class ResumeExtractProcess implements org.quartz.Job {

    static Logger logger = Logger.getLogger(ResumeExtractProcess.class.getName());

    private static final DateFormat DATE_PARSER = new SimpleDateFormat("yyyy-MM-dd");

    private static String RESUME_BASE_DIR = "";

    private static String OLD_RESUME_BASE_DIR = "";

    private static String WEBSITE = "";

    private static String TEMP_DIR = "";

    /** Creates a new instance of CandidateRSSFeedCreator */
    public ResumeExtractProcess() {
        setUp();
    }

    private void setUp() {
        try {
            Properties DSettings = new Properties();
            DSettings.load(getClass().getClassLoader().getResourceAsStream("rooster.properties"));
            RESUME_BASE_DIR = DSettings.getProperty(PropertyFileConst.APPLICATION_ROOT_PATH);
            OLD_RESUME_BASE_DIR = RESUME_BASE_DIR + DSettings.getProperty(PropertyFileConst.OLD_DB_RESUME_FOlDER);
            TEMP_DIR = RESUME_BASE_DIR + DSettings.getProperty(PropertyFileConst.TEMPORARY_FOLDER);
            WEBSITE = DSettings.getProperty("WEBSITE");
            logger.debug("ResumeExtractProcess >>> Properties Loaded Successfully In ResumeExtractProcess.");
        } catch (Exception e) {
            logger.debug("ResumeExtractProcess >>> Could Not Load Properties " + e);
        }
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        boolean isValid = false;
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        File fInputFile = new File(dataMap.getString("FILE_PATH"));
        File fOutputFolder = new File(dataMap.getString("TARGET_FOLDER_PATH"));
        String sUnzipTarget = fInputFile.getAbsolutePath();
        String sUserId = dataMap.getString("USER_ID");
        String sFirstName = dataMap.getString("FNAME");
        String sLastName = dataMap.getString("LNAME");
        SummaryBean summary = new SummaryBean();
        ZipUtil oZipUtil = new ZipUtil();
        if (sUnzipTarget.lastIndexOf("/") > -1) {
            sUnzipTarget = sUnzipTarget.substring(0, sUnzipTarget.lastIndexOf("/"));
        } else {
            sUnzipTarget = sUnzipTarget.substring(0, sUnzipTarget.lastIndexOf("\\"));
        }
        logger.info("Unzip Target: " + sUnzipTarget);
        oZipUtil.Unzip(fInputFile);
        oZipUtil.setSummaryBean(summary);
        if (sUnzipTarget.indexOf(TEMP_DIR) > -1) {
            isValid = true;
        }
        if (isValid) {
            oZipUtil.deleteNonDocFiles(new File(sUnzipTarget), new File(OLD_RESUME_BASE_DIR + "/"), RESUME_BASE_DIR);
        }
        logger.info("Total no. of Files: " + summary.getTot_files());
        logger.info("Total no. of Docs: " + summary.getTot_docs());
        logger.info("Total Resume Files Size: " + summary.getTot_docs_size());
        sendSummaryMail(summary, sUserId, sFirstName, sLastName);
    }

    /**
	 * To send a Summary Mail.
	 * */
    private void sendSummaryMail(SummaryBean summary, String sUserId, String sFirstName, String sLastName) {
        RoosterMailer ms = new RoosterMailer();
        String sSub = "summary of files uploaded to resume bank";
        StringBuilder sbMessage = new StringBuilder();
        sbMessage.append("Dear ").append(sFirstName);
        if (sLastName == null || sLastName.equals("")) {
        } else {
            sbMessage.append(" ").append(sLastName);
        }
        sbMessage.append(",<br/>").append("You have successfully uploaded the resumes. Following is the summary:<br/><br/>").append("Total no. files in Zip : ").append(summary.getTot_files()).append("<br/>").append("Total no. of documents : ").append(summary.getTot_docs()).append("<br/>").append("Total size of documents : ").append(summary.getTot_docs_size()).append(" bytes").append("<br/><br/>").append("");
        ms.sendMail(sUserId, sUserId, sSub, sbMessage.toString(), new Vector(), new String(), new String());
    }
}
