package org.pojosoft.lms.content.scorm2004;

import org.apache.log4j.Logger;
import org.pojosoft.core.reference.ReferenceService;
import org.pojosoft.core.support.DateTimeUtils;
import org.pojosoft.core.support.ServiceLocator;
import org.pojosoft.lms.content.ContentService;
import org.pojosoft.lms.content.scorm2004.rte.*;
import org.pojosoft.lms.content.scorm2004.sequencer.ADLLaunch;
import org.pojosoft.lms.content.scorm2004.sequencer.SeqNavigation;
import org.pojosoft.lms.course.CourseService;
import org.pojosoft.lms.course.model.CourseStatus;
import org.pojosoft.lms.course.model.UserCourse;
import org.pojosoft.lms.support.BaseTest;
import org.pojosoft.lms.user.UserServiceTest;
import org.pojosoft.lms.user.model.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.ArrayList;

/**
 * Unit test for SCORM 2004 service..
 *
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class Scorm2004ServiceTest extends BaseTest {

    static Logger logger = Logger.getLogger(Scorm2004ServiceTest.class);

    ContentService contentService = null;

    GlobalObjectiveService globalObjectiveService = null;

    CourseService courseService = null;

    ReferenceService referenceService;

    UserServiceTest userUtil = null;

    static String globalObjectiveStr = "Scorm2004ServiceTest.globalObjective";

    static String userID = "Scorm2004ServiceTest.user1";

    static String courseID = "Scorm2004ServiceTest.course1";

    org.pojosoft.lms.course.model.Course course;

    org.pojosoft.lms.content.model.OnlineLearningContent onlineLearningContent;

    org.pojosoft.lms.content.model.ContentOrganization contentOrg;

    CourseStatus activeStatus;

    User user = null;

    org.pojosoft.lms.course.model.UserCourse userCourse = null;

    org.pojosoft.lms.content.model.UserOnlineLearningContent userOnlineLC = null;

    Scorm2004manifestImportTest scormImport = null;

    Scorm2004Service scorm2004Service = null;

    @BeforeClass
    public void setUp() {
        super.setup();
        scormImport = new Scorm2004manifestImportTest();
        contentService = (ContentService) ServiceLocator.getService("content.contentService");
        globalObjectiveService = (GlobalObjectiveService) ServiceLocator.getService("content.globalObjectivesServiceImpl");
        courseService = (CourseService) ServiceLocator.getService("course.courseService");
        referenceService = (ReferenceService) ServiceLocator.getService("reference.referenceService");
        scorm2004Service = (Scorm2004Service) ServiceLocator.getService("content.scorm2004Service");
        userUtil = new UserServiceTest();
    }

    /**
   * Deep test. import the package, assign to user
   */
    @Test
    public void testPackage54_OB_5a() {
        try {
            user = userUtil.addUser(userID);
            contentOrg = scormImport.importScorm2004ContentPackage("LMSTestPackage54");
            onlineLearningContent = new org.pojosoft.lms.content.model.OnlineLearningContent();
            onlineLearningContent.setId("Scorm2004ServiceTest.lc.a");
            onlineLearningContent.setContentOrganization(contentOrg);
            courseService.addLearningContent(onlineLearningContent);
            activeStatus = new org.pojosoft.lms.course.model.CourseStatus("Scorm2004ServiceTest.1-active", "active");
            referenceService.addReference(activeStatus);
            course = new org.pojosoft.lms.course.model.Course();
            course.setId(Scorm2004ServiceTest.courseID);
            course.setTitle("this is course test course");
            course.setStatus(activeStatus);
            course.setAutoRecordLearningEventUponCompletion(false);
            course.addOnlineLearningContent(onlineLearningContent);
            course.setLastUpdateDate(DateTimeUtils.getCurrentTimestamp());
            courseService.addCourse(course);
            userCourse = new UserCourse(user, course);
            courseService.addUserCourse(userCourse);
            org.pojosoft.lms.content.model.UserOnlineLearningContent[] userOnlineLCs = userCourse.getUserOnlineLearningContents().toArray(new org.pojosoft.lms.content.model.UserOnlineLearningContent[userCourse.getUserOnlineLearningContents().size()]);
            userOnlineLC = userOnlineLCs[0];
            assert userOnlineLC != null;
            assert userOnlineLC.getUserOnlineLearningContentActivities().size() == 4;
            if (!scorm2004Service.isSuspended(userID, userOnlineLC.getId())) {
                logger.debug("issue a start navigation request");
                ADLLaunch launch = scorm2004Service.doSeqNavigationByLeaningContent(userID, userOnlineLC.getId(), SeqNavigation.NAV_START);
                logger.debug("\t-- launch.getActivityID() = " + launch.getActivityId());
                logger.debug("\t-- launch.getLaunchResultCode() = " + launch.getNoContentReason());
                assert launch.getActivityId().equals("activity_1");
                Long userOnlineActId = userOnlineLC.getUserOnlineLearningContentActivity(launch.getActivityId()).getId();
                RteDTO rte2 = new RteDTO();
                rte2.setExit(org.pojosoft.lms.content.model.ScormConstants.CMI_EXIT_SUSPEND);
                rte2.setScoreScaled(0.85);
                rte2.setSuccessStatus(org.pojosoft.lms.content.model.ScormConstants.CMI_SUCCESS_STATUS_PASSED);
                scorm2004Service.setRteData(rte2, userID, userOnlineActId);
                logger.debug("issue a continue navigation request");
                launch = scorm2004Service.doSeqNavigationByLeaningContent(userID, userOnlineLC.getId(), SeqNavigation.NAV_CONTINUE);
                logger.debug("\t-- launch.getActivityID() = " + launch.getActivityId());
                logger.debug("\t-- launch.getLaunchResultCode() = " + launch.getNoContentReason());
                assert launch.getActivityId().equals("activity_4");
            } else assert false;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        } finally {
            try {
                startTransaction();
                userOnlineLC = contentService.getUserOnlineLearningContent(userOnlineLC.getId());
                for (org.pojosoft.lms.content.model.UserOnlineLearningContentActivity act : userOnlineLC.getUserOnlineLearningContentActivities()) {
                    logger.debug("\t--activityID:" + act.getActivity().getActivityID() + ",  SuccessStatus:" + act.getSuccessStatus() + ",  ScoreScaled:" + act.getScoreScaled());
                    logger.debug("\t\t objectives:");
                    for (org.pojosoft.lms.content.model.UserOnlineActivityObjective userActObj : act.getUserOnlineActivityObjectives()) {
                        logger.debug("\t\t--objID:" + userActObj.getObjective().getObjective() + ",SuccessStatus:" + userActObj.getSuccessStatus() + ",ScoreScaled:" + userActObj.getScoreScaled());
                    }
                }
                commit();
            } finally {
                cleanData();
                contentService.removeContentObject("SeqConTest_OB-5a_SEQ01_1");
                try {
                    contentService.removeObjective(contentService.getObjective("PRIMARYOBJ", true).getId());
                } catch (Exception e) {
                }
            }
        }
    }

    protected void cleanData() {
        Scorm2004ServiceTest.logger.info("------------------ cleanData()");
        try {
            courseService.removeUserCourse(userCourse.getId());
            courseService.removeCourse(Scorm2004ServiceTest.courseID);
            referenceService.removeReference(activeStatus.getClass(), activeStatus.getId());
            courseService.removeLearningContent(onlineLearningContent.getId());
            userUtil.removeUser(Scorm2004ServiceTest.userID);
            contentService.removeContentOrganization(contentOrg.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSetRteData_package02() {
        try {
            user = userUtil.addUser(userID);
            contentOrg = scormImport.importScorm2004ContentPackage("LMSTestPackage02");
            onlineLearningContent = new org.pojosoft.lms.content.model.OnlineLearningContent();
            onlineLearningContent.setId("Scorm2004ServiceTest.lc.b");
            onlineLearningContent.setContentOrganization(contentOrg);
            courseService.addLearningContent(onlineLearningContent);
            activeStatus = new org.pojosoft.lms.course.model.CourseStatus("Scorm2004ServiceTest.1-active", "active");
            referenceService.addReference(activeStatus);
            course = new org.pojosoft.lms.course.model.Course();
            course.setId(Scorm2004ServiceTest.courseID);
            course.setTitle("this is course test course");
            course.setStatus(activeStatus);
            course.setAutoRecordLearningEventUponCompletion(false);
            course.addOnlineLearningContent(onlineLearningContent);
            course.setLastUpdateDate(DateTimeUtils.getCurrentTimestamp());
            courseService.addCourse(course);
            userCourse = new UserCourse(user, course);
            courseService.addUserCourse(userCourse);
            org.pojosoft.lms.content.model.UserOnlineLearningContent[] userOnlineLCs = userCourse.getUserOnlineLearningContents().toArray(new org.pojosoft.lms.content.model.UserOnlineLearningContent[userCourse.getUserOnlineLearningContents().size()]);
            userOnlineLC = userOnlineLCs[0];
            assert userOnlineLC != null;
            assert userOnlineLC.getUserOnlineLearningContentActivities().size() == 7;
            org.pojosoft.lms.content.model.UserOnlineLearningContentActivity[] userOnlineActs = userOnlineLC.getUserOnlineLearningContentActivities().toArray(new org.pojosoft.lms.content.model.UserOnlineLearningContentActivity[userOnlineLC.getUserOnlineLearningContentActivities().size()]);
            logger.debug("issue a start navigation reques");
            ADLLaunch launch = null;
            if (!scorm2004Service.isSuspended(userID, userOnlineLC.getId())) launch = scorm2004Service.doSeqNavigationByLeaningContent(userID, userOnlineLC.getId(), SeqNavigation.NAV_START);
            launch = scorm2004Service.doChoiceNavigation(userID, userOnlineLC.getId(), "LO01");
            logger.debug("\t-- launch.getActivityID() = " + launch.getActivityId());
            logger.debug("\t-- launch.getLaunchResultCode() = " + launch.getNoContentReason());
            org.pojosoft.lms.content.model.UserOnlineLearningContentActivity userAct = userOnlineLC.getUserOnlineLearningContentActivity(launch.getActivityId());
            Long userOnlineActId = userAct.getId();
            logger.debug("userId=" + userID);
            logger.debug("userOnlineActId=" + userOnlineActId);
            scorm2004Service.initializeRte(userID, userOnlineActId);
            RteDTO rte = scorm2004Service.getRteData(userID, userOnlineActId);
            assert rte.getTimeLimitAction().equals("continue,message");
            assert rte.getLaunchData().equals("Launch Data Test");
            assert rte.getCompletionThreshold().doubleValue() == 0.8;
            assert rte.getCredit().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_CREDIT_FOR_CREDIT);
            assert rte.getSuccessStatus().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_SUCCESS_STATUS_UNKNOWN);
            assert rte.getCompletionStatus().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_COMPLETION_STATUS_UNKNOWN);
            assert rte.getMode().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_MODE_NORMAL);
            assert rte.getLearnerID().equals(userID);
            assert rte.getLearnerName().equals(user.getLastName() + "," + user.getFirstName());
            assert rte.getEntry().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_ENTRY_ABINITIO);
            assert rte.getCommentsFromLms().size() == 0;
            assert rte.getCommentsFromLearner().size() == 0;
            assert rte.getLearnerPreference().getAudioLevel() == 1;
            assert rte.getLearnerPreference().getDeliverySpeed() == 1;
            assert rte.getLearnerPreference().getLanguage().equals("");
            assert rte.getLearnerPreference().getAudioCaptioning().equals(org.pojosoft.lms.content.model.ScormConstants.AUDIO_CAPTIONING_NO_CHANGE);
            rte.setExit(org.pojosoft.lms.content.model.ScormConstants.CMI_EXIT_SUSPEND);
            rte.setSessionTime(new Double(100.01));
            rte.setProgressMesure(0.9);
            RteCommentDTO c1 = new RteCommentDTO();
            c1.setComment("c1-c1");
            c1.setDateTime(DateTimeUtils.getCurrentTimestamp());
            c1.setLocation("location:q1");
            rte.addCommentsFromLearner(c1);
            RteCommentDTO c2 = new RteCommentDTO();
            c2.setComment("c2-c2");
            c2.setDateTime(DateTimeUtils.getCurrentTimestamp());
            c2.setLocation("location:q2");
            rte.addCommentsFromLearner(c2);
            RteLearnerPreferenceDTO rteLearnerPreferenceDTO = new RteLearnerPreferenceDTO();
            rteLearnerPreferenceDTO.setAudioCaptioning(org.pojosoft.lms.content.model.ScormConstants.AUDIO_CAPTIONING_ON);
            rteLearnerPreferenceDTO.setAudioLevel(new Double(2));
            rteLearnerPreferenceDTO.setDeliverySpeed(null);
            rteLearnerPreferenceDTO.setLanguage("English");
            rte.setLearnerPreference(rteLearnerPreferenceDTO);
            RteInteractionDTO interDTO1 = new RteInteractionDTO();
            rte.addInteraction(interDTO1);
            interDTO1.addCorrectResponsePattern("pattern1");
            interDTO1.addCorrectResponsePattern("<pattern2>");
            interDTO1.addInteractionObjective("obj1");
            interDTO1.addInteractionObjective("obj2");
            interDTO1.setDateTime(DateTimeUtils.getCurrentTimestamp());
            interDTO1.setDescription("setDescription1");
            interDTO1.setInteractionId("id1");
            interDTO1.setInteractionType("type1");
            interDTO1.setLatency(2.9d);
            interDTO1.setLearnerResponse("setLearnerResponse");
            interDTO1.setResult("1.24567");
            interDTO1.setWeighting(1.23d);
            RteInteractionDTO interDTO2 = new RteInteractionDTO();
            rte.addInteraction(interDTO2);
            interDTO2.setInteractionId("id2");
            interDTO2.addCorrectResponsePattern("pattern1");
            interDTO2.addCorrectResponsePattern("<pattern2>");
            interDTO2.setDateTime(DateTimeUtils.getCurrentTimestamp());
            interDTO2.setDescription("setDescription2");
            interDTO2.setInteractionType("type2");
            interDTO2.setLatency(2.91d);
            interDTO2.setLearnerResponse("setLearnerResponse2");
            interDTO2.setResult("1.245671");
            interDTO2.setWeighting(1.231d);
            scorm2004Service.setRteData(rte, userID, userOnlineActId);
            scorm2004Service.initializeRte(userID, userOnlineActId);
            rte = scorm2004Service.getRteData(userID, userOnlineActId);
            assert rte.getCompletionStatus().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_COMPLETION_STATUS_COMPLETED);
            assert rte.getEntry().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_ENTRY_RESUME);
            assert rte.getTotalTime().doubleValue() == 100.01;
            assert rte.getCommentsFromLearner().size() == 2;
            assert rte.getLearnerPreference().getAudioLevel() == 2;
            assert rte.getLearnerPreference().getDeliverySpeed() == 1;
            assert rte.getLearnerPreference().getLanguage().equals("English");
            assert rte.getLearnerPreference().getAudioCaptioning().equals(org.pojosoft.lms.content.model.ScormConstants.AUDIO_CAPTIONING_ON);
            assert rte.getInteractions().size() == 2;
            for (int i = 0; i < rte.getInteractions().size(); i++) {
                RteInteractionDTO inter = (RteInteractionDTO) rte.getInteractions().get(i);
                if (inter.getInteractionId().equals("id1")) {
                    assert inter.getCorrectResponsePatterns().size() == 2;
                    assert inter.getObjectives().size() == 2;
                    assert inter.getDescription().equals("setDescription1");
                    assert inter.getInteractionType().equals("type1");
                    assert inter.getLatency() == 2.9d;
                    assert inter.getLearnerResponse().equals("setLearnerResponse");
                    assert inter.getResult().equals("1.24567");
                    assert inter.getWeighting() == 1.23d;
                } else if (inter.getInteractionId().equals("id2")) {
                    assert inter.getCorrectResponsePatterns().size() == 2;
                    assert inter.getObjectives().size() == 0;
                }
            }
            rte.setExit(org.pojosoft.lms.content.model.ScormConstants.CMI_EXIT_NORMAL);
            scorm2004Service.setRteData(rte, userID, userOnlineActId);
            scorm2004Service.initializeRte(userID, userOnlineActId);
            rte = scorm2004Service.getRteData(userID, userOnlineActId);
            assert rte.getTimeLimitAction().equals("continue,message");
            assert rte.getLaunchData().equals("Launch Data Test");
            assert rte.getCompletionThreshold().doubleValue() == 0.8;
            assert rte.getCredit().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_CREDIT_FOR_CREDIT);
            assert rte.getSuccessStatus().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_SUCCESS_STATUS_UNKNOWN);
            assert rte.getCompletionStatus().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_COMPLETION_STATUS_UNKNOWN);
            assert rte.getMode().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_MODE_NORMAL);
            assert rte.getLearnerID().equals(userID);
            assert rte.getLearnerName().equals(user.getLastName() + "," + user.getFirstName());
            assert rte.getEntry().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_ENTRY_ABINITIO);
            assert rte.getCommentsFromLms().size() == 0;
            assert rte.getCommentsFromLearner().size() == 0;
            assert rte.getLearnerPreference().getAudioLevel() == 1;
            assert rte.getLearnerPreference().getDeliverySpeed() == 1;
            assert rte.getLearnerPreference().getLanguage().equals("");
            assert rte.getLearnerPreference().getAudioCaptioning().equals(org.pojosoft.lms.content.model.ScormConstants.AUDIO_CAPTIONING_NO_CHANGE);
            assert rte.getObjectives().size() == 0;
            assert rte.getInteractions().size() == 0;
            rte = new RteDTO();
            rte.setExit(org.pojosoft.lms.content.model.ScormConstants.CMI_EXIT_SUSPEND);
            rte.setSessionTime(new Double(10.01));
            c1.setComment("c1-c1--changed");
            c1.setDateTime(DateTimeUtils.getCurrentTimestamp());
            c1.setLocation("location:q1-changed");
            rte.addCommentsFromLearner(c1);
            scorm2004Service.setRteData(rte, userID, userOnlineActId);
            rte.setSessionTime(100.05);
            scorm2004Service.setRteData(rte, userID, userOnlineActId);
            rte = scorm2004Service.getRteData(userID, userOnlineActId);
            assert rte.getTotalTime().doubleValue() == 110.06;
            RteCommentDTO comment = (RteCommentDTO) (rte.getCommentsFromLearner().get(0));
            assert comment.getComment().equals(c1.getComment());
            assert comment.getDateTime().getTime() / 1000 == c1.getDateTime().getTime() / 1000;
            assert comment.getLocation().equals(c1.getLocation());
            assert rte.getInteractions().size() == 0;
            rte.setExit(org.pojosoft.lms.content.model.ScormConstants.CMI_EXIT_NONE);
            scorm2004Service.setRteData(rte, userID, userOnlineActId);
            scorm2004Service.initializeRte(userID, userOnlineActId);
            rte = scorm2004Service.getRteData(userID, userOnlineActId);
            assert rte.getTotalTime() == null;
            assert rte.getCommentsFromLearner().size() == 0;
            assert rte.getInteractions().size() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        } finally {
            cleanData();
            contentService.removeContentObject("LMSTestPackage01_DMIMPLEMENTATION01_1");
            contentService.removeContentObject("LMSTestPackage01_DMBEHAVIOR01_2");
            contentService.removeContentObject("LMSTestPackage01_DMBEHAVIOR02_3");
            contentService.removeContentObject("LMSTestPackage01_PERSISTSTATE01_4");
            contentService.removeContentObject("LMSTestPackage01_PERSISTSTATE02_5");
            contentService.removeContentObject("LMSTestPackage01_ADHOC01_6");
        }
    }

    @Test
    public void testPackage44_RTEObjective() {
        try {
            user = userUtil.addUser(userID);
            contentOrg = scormImport.importScorm2004ContentPackage("LMSTestPackage44");
            onlineLearningContent = new org.pojosoft.lms.content.model.OnlineLearningContent();
            onlineLearningContent.setId("Scorm2004ServiceTest.lc.c");
            onlineLearningContent.setContentOrganization(contentOrg);
            courseService.addLearningContent(onlineLearningContent);
            activeStatus = new CourseStatus("Scorm2004ServiceTest.1-active", "active");
            referenceService.addReference(activeStatus);
            course = new org.pojosoft.lms.course.model.Course();
            course.setId(Scorm2004ServiceTest.courseID);
            course.setTitle("this is course test course");
            course.setStatus(activeStatus);
            course.setAutoRecordLearningEventUponCompletion(false);
            course.addOnlineLearningContent(onlineLearningContent);
            course.setLastUpdateDate(DateTimeUtils.getCurrentTimestamp());
            courseService.addCourse(course);
            userCourse = new UserCourse(user, course);
            courseService.addUserCourse(userCourse);
            org.pojosoft.lms.content.model.UserOnlineLearningContent[] userOnlineLCs = userCourse.getUserOnlineLearningContents().toArray(new org.pojosoft.lms.content.model.UserOnlineLearningContent[userCourse.getUserOnlineLearningContents().size()]);
            userOnlineLC = userOnlineLCs[0];
            assert userOnlineLC != null;
            assert userOnlineLC.getUserOnlineLearningContentActivities().size() == 6;
            org.pojosoft.lms.content.model.UserOnlineLearningContentActivity[] userOnlineActs = userOnlineLC.getUserOnlineLearningContentActivities().toArray(new org.pojosoft.lms.content.model.UserOnlineLearningContentActivity[userOnlineLC.getUserOnlineLearningContentActivities().size()]);
            org.pojosoft.lms.content.model.UserOnlineLearningContentActivity userAct = userOnlineLC.getUserOnlineLearningContentActivity("activity_1");
            assert userAct.equals(userOnlineActs[0]);
            ADLLaunch launch = null;
            if (!scorm2004Service.isSuspended(userID, userOnlineLC.getId())) launch = scorm2004Service.doSeqNavigationByLeaningContent(userID, userOnlineLC.getId(), SeqNavigation.NAV_START);
            logger.debug("\t-- launch.getActivityID() = " + launch.getActivityId());
            logger.debug("\t-- launch.getLaunchResultCode() = " + launch.getNoContentReason());
            RteDTO rte = scorm2004Service.getRteData(userID, userAct.getId());
            assert rte.getObjectives().size() == 3;
            rte.setExit(org.pojosoft.lms.content.model.ScormConstants.CMI_EXIT_SUSPEND);
            ArrayList<RteObjectiveDTO> newOobjDTOList = new ArrayList<RteObjectiveDTO>();
            for (int i = 0; i < rte.getObjectives().size(); i++) {
                RteObjectiveDTO objectiveDTO = (RteObjectiveDTO) rte.getObjectives().get(i);
                if (objectiveDTO.getObjectiveId().equals("obj1")) {
                    objectiveDTO.setDescription("new desc");
                    objectiveDTO.setScoreMin(1.0);
                    objectiveDTO.setScoreMax(100.0);
                    objectiveDTO.setScoreRaw(92.0);
                    objectiveDTO.setScoreScaled(0.92);
                    objectiveDTO.setSuccessStatus(org.pojosoft.lms.content.model.ScormConstants.CMI_SUCCESS_STATUS_PASSED);
                    objectiveDTO.setCompletionStatus(org.pojosoft.lms.content.model.ScormConstants.CMI_COMPLETION_STATUS_COMPLETED);
                }
            }
            scorm2004Service.setRteData(rte, userID, userAct.getId());
            rte = scorm2004Service.getRteData(userID, userAct.getId());
            assert rte.getObjectives().size() == 3;
            for (int i = 0; i < rte.getObjectives().size(); i++) {
                RteObjectiveDTO objectiveDTO = (RteObjectiveDTO) rte.getObjectives().get(i);
                if (objectiveDTO.getObjectiveId().equals("obj1")) {
                    assert objectiveDTO.getScoreMin() == 1.0;
                    assert objectiveDTO.getScoreMax() == 100.0;
                    assert objectiveDTO.getScoreRaw() == 92.0;
                    assert objectiveDTO.getScoreScaled() == 0.92;
                    assert objectiveDTO.getSuccessStatus().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_SUCCESS_STATUS_PASSED);
                    assert objectiveDTO.getCompletionStatus().equals(org.pojosoft.lms.content.model.ScormConstants.CMI_COMPLETION_STATUS_COMPLETED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        } finally {
            cleanData();
            try {
                contentService.removeContentObject("SeqConTest_SX-3_SEQ01_1");
                contentService.removeObjective(contentService.getObjective("obj1", false).getId());
                contentService.removeObjective(contentService.getObjective("obj2", false).getId());
                contentService.removeObjective(contentService.getObjective("obj3", false).getId());
                contentService.removeObjective(contentService.getObjective("PRIMARYOBJ", true).getId());
            } catch (Exception e) {
            }
        }
    }
}
