package org.openuss.paperSubmission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.openuss.documents.DocumentApplicationException;
import org.openuss.documents.FileInfo;
import org.openuss.documents.FolderEntryInfo;
import org.openuss.documents.FolderInfo;
import org.openuss.security.Authority;
import org.openuss.security.Group;
import org.openuss.security.User;
import org.openuss.security.UserInfo;
import org.openuss.security.acl.LectureAclEntry;

/**
 * @author  Projektseminar WS 07/08, Team Collaboration
 * @see org.openuss.paperSubmission.PaperSubmissionService
 */
public class PaperSubmissionServiceImpl extends org.openuss.paperSubmission.PaperSubmissionServiceBase {

    private static final Logger LOGGER = Logger.getLogger(PaperSubmissionServiceImpl.class);

    @Override
    protected void handleCreateExam(ExamInfo examInfo) {
        Validate.notNull(examInfo, "examInfo cannot be null.");
        Validate.notNull(examInfo.getDomainId(), "domainId cannot be null.");
        final Exam examEntity = getExamDao().examInfoToEntity(examInfo);
        Validate.notNull(examEntity, "examInfoEntity cannot be null.");
        getExamDao().create(examEntity);
        Validate.notNull(examEntity, "examEntity cannot be null");
        examInfo.setId(examEntity.getId());
        getSecurityService().createObjectIdentity(examEntity, null);
        if (examInfo.getAttachments() != null) {
            LOGGER.debug("found " + examInfo.getAttachments().size() + " attachments.");
            getDocumentService().diffSave(examEntity, examInfo.getAttachments());
            for (FileInfo file : (List<FileInfo>) examInfo.getAttachments()) {
                final Group group = getParticipantsGroup(examInfo.getDomainId());
                getSecurityService().setPermissions(group, file, LectureAclEntry.READ);
            }
        }
        getExamDao().toExamInfo(examEntity, examInfo);
    }

    @Override
    protected void handleCreatePaperSubmission(PaperSubmissionInfo paperSubmissionInfo) {
        Validate.notNull(paperSubmissionInfo, "paperSubmissionInfo cannot be null.");
        Validate.notNull(paperSubmissionInfo.getExamId(), "ExanId cannot be null.");
        final PaperSubmission paperSubmissionEntity = getPaperSubmissionDao().paperSubmissionInfoToEntity(paperSubmissionInfo);
        Validate.notNull(paperSubmissionEntity, "paperSubmissionEntity cannot be null.");
        final Exam exam = getExamDao().load(paperSubmissionInfo.getExamId());
        final User user = getUserDao().load(paperSubmissionInfo.getUserId());
        paperSubmissionEntity.setExam(exam);
        paperSubmissionEntity.setSender(user);
        paperSubmissionEntity.setDeliverDate(new Date());
        getPaperSubmissionDao().create(paperSubmissionEntity);
        Validate.notNull(paperSubmissionEntity, "paperSubmissionId cannot be null");
        paperSubmissionInfo.setId(paperSubmissionEntity.getId());
        getExamDao().update(exam);
        getSecurityService().createObjectIdentity(paperSubmissionEntity, paperSubmissionEntity.getExam());
        getSecurityService().setPermissions(user, paperSubmissionEntity, LectureAclEntry.PAPER_PARTICIPANT);
    }

    /** 
	 * @return List of PaperSubmissionInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleFindPaperSubmissionsByExam(Long examId) {
        Validate.notNull(examId, "examId cannot be null.");
        final Exam exam = getExamDao().load(examId);
        final List<PaperSubmissionInfo> submissions = getPaperSubmissionDao().findByExam(PaperSubmissionDao.TRANSFORM_PAPERSUBMISSIONINFO, exam);
        for (PaperSubmissionInfo submission : submissions) {
            final UserInfo user = getSecurityService().getUser(submission.getUserId());
            submission.setFirstName(user.getFirstName());
            submission.setLastName(user.getLastName());
            submission.setDisplayName(user.getDisplayName());
            if (exam.getDeadline().after(submission.getDeliverDate())) {
                submission.setSubmissionStatus(SubmissionStatus.IN_TIME);
            } else {
                submission.setSubmissionStatus(SubmissionStatus.NOT_IN_TIME);
            }
        }
        return submissions;
    }

    /** 
	 * @return List of PaperSubmissionInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleFindPaperSubmissionsByExamAndUser(Long examId, Long userId) {
        Validate.notNull(examId, "examId cannot be null.");
        Validate.notNull(userId, "userId cannot be null");
        final User user = getUserDao().load(userId);
        final Exam exam = getExamDao().load(examId);
        final List<PaperSubmissionInfo> submissions = getPaperSubmissionDao().findByExamAndUser(PaperSubmissionDao.TRANSFORM_PAPERSUBMISSIONINFO, exam, user);
        for (PaperSubmissionInfo submission : submissions) {
            submission.setFirstName(user.getFirstName());
            submission.setLastName(user.getLastName());
            submission.setDisplayName(user.getDisplayName());
            if (exam.getDeadline().after(submission.getDeliverDate())) {
                submission.setSubmissionStatus(SubmissionStatus.IN_TIME);
            } else {
                submission.setSubmissionStatus(SubmissionStatus.NOT_IN_TIME);
            }
        }
        return submissions;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ExamInfo handleGetExam(Long examId) {
        Validate.notNull(examId, "examId cannot be null.");
        ExamInfo examInfo = null;
        examInfo = (ExamInfo) getExamDao().load(ExamDao.TRANSFORM_EXAMINFO, examId);
        if (examInfo == null) {
            return null;
        }
        final List<FileInfo> attachments = getDocumentService().getFileEntries(examInfo);
        examInfo.setAttachments(attachments);
        return examInfo;
    }

    @Override
    protected PaperSubmissionInfo handleGetPaperSubmission(Long paperSubmissionId) {
        Validate.notNull(paperSubmissionId, "paperSubmissionId cannot be null");
        PaperSubmissionInfo submission = null;
        submission = (PaperSubmissionInfo) getPaperSubmissionDao().load(PaperSubmissionDao.TRANSFORM_PAPERSUBMISSIONINFO, paperSubmissionId);
        if (submission == null) {
            return null;
        }
        final Exam exam = getExamDao().load(submission.getExamId());
        final UserInfo user = getSecurityService().getUser(submission.getUserId());
        submission.setFirstName(user.getFirstName());
        submission.setLastName(user.getLastName());
        submission.setDisplayName(user.getDisplayName());
        if (exam.getDeadline().after(submission.getDeliverDate())) {
            submission.setSubmissionStatus(SubmissionStatus.IN_TIME);
        } else {
            submission.setSubmissionStatus(SubmissionStatus.NOT_IN_TIME);
        }
        return submission;
    }

    @Override
    protected void handleRemoveExam(Long examId) throws DocumentApplicationException {
        Validate.notNull(examId, "examId cannot be null.");
        final Exam examEntity = getExamDao().load(examId);
        for (PaperSubmission paperSubmission : examEntity.getPapersubmissions()) {
            FolderInfo folder = getDocumentService().getFolder(paperSubmission);
            getDocumentService().removeFolderEntry(folder.getId());
        }
        getExamDao().remove(examEntity);
    }

    @Override
    protected void handleUpdateExam(ExamInfo examInfo) {
        LOGGER.debug("Starting method handleUpdateExam");
        Validate.notNull(examInfo, "examInfo cannot be null");
        Validate.notNull(examInfo.getId(), "Parameter examInfo must contain a valid id.");
        final Exam examEntity = getExamDao().examInfoToEntity(examInfo);
        LOGGER.debug("Updating exam");
        getExamDao().update(examEntity);
        LOGGER.debug("Updating file attachments");
        getDocumentService().diffSave(examEntity, examInfo.getAttachments());
        getExamDao().toExamInfo(examEntity, examInfo);
    }

    @Override
    protected PaperSubmissionInfo handleUpdatePaperSubmission(PaperSubmissionInfo paperSubmissionInfo, boolean changeDeliverDate) throws Exception {
        if (changeDeliverDate) {
            LOGGER.debug("Starting method handleUpdatePaperSubmission");
            Validate.notNull(paperSubmissionInfo, "paperSubmissionInfo cannot be null");
            Validate.notNull(paperSubmissionInfo.getId(), "Parameter paperSubmissionInfo must contain a valid id");
            final ExamInfo exam = getExam(paperSubmissionInfo.getExamId());
            paperSubmissionInfo.setDeliverDate(new Date());
            if (paperSubmissionInfo.getSubmissionStatus().equals(SubmissionStatus.IN_TIME) && exam.getDeadline().before(paperSubmissionInfo.getDeliverDate())) {
                paperSubmissionInfo.setComment(null);
                paperSubmissionInfo.setId(null);
                this.createPaperSubmission(paperSubmissionInfo);
                return paperSubmissionInfo;
            } else {
                final PaperSubmission paperSubmissionEntity = getPaperSubmissionDao().paperSubmissionInfoToEntity(paperSubmissionInfo);
                getPaperSubmissionDao().update(paperSubmissionEntity);
                return paperSubmissionInfo;
            }
        } else {
            final PaperSubmission paperSubmissionEntity = getPaperSubmissionDao().paperSubmissionInfoToEntity(paperSubmissionInfo);
            getPaperSubmissionDao().update(paperSubmissionEntity);
            return paperSubmissionInfo;
        }
    }

    /** 
	 * @return List of ExamInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleFindExamsByDomainId(Long domainId) {
        Validate.notNull(domainId, "domainId cannot be null.");
        return getExamDao().findByDomainId(ExamDao.TRANSFORM_EXAMINFO, domainId);
    }

    /** 
	 * @return List of ExamInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleFindActiveExamsByDomainId(Long domainId) {
        Validate.notNull(domainId, "courseId cannot be null.");
        final List<ExamInfo> exams = getExamDao().findByDomainId(ExamDao.TRANSFORM_EXAMINFO, domainId);
        final List<ExamInfo> activeExams = new ArrayList<ExamInfo>();
        Date now = new Date();
        for (ExamInfo exam : exams) {
            if (exam.getDeadline().after(now)) {
                activeExams.add(exam);
            }
        }
        return activeExams;
    }

    /** 
	 * @return List of ExamInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleFindInactiveExamsByDomainId(Long domainId) {
        Validate.notNull(domainId, "domainId cannot be null.");
        final List<ExamInfo> exams = getExamDao().findByDomainId(ExamDao.TRANSFORM_EXAMINFO, domainId);
        final List<ExamInfo> inactiveExams = new ArrayList<ExamInfo>();
        final Date now = new Date();
        for (ExamInfo exam : exams) {
            if (exam.getDeadline().before(now)) {
                inactiveExams.add(exam);
            }
        }
        return inactiveExams;
    }

    /** 
	 * @return List of SubmissionInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleGetMembersAsPaperSubmissionsByExam(Long examId) {
        Validate.notNull(examId, "examId cannot be null.");
        final Exam exam = getExamDao().load(examId);
        final List<PaperSubmissionInfo> allSubmissions = new ArrayList();
        final List<PaperSubmissionInfo> submissions = findPaperSubmissionsByExam(exam.getId());
        final List<UserInfo> members = loadCourseMembers(exam.getDomainId());
        for (UserInfo member : members) {
            boolean submitted = false;
            for (PaperSubmissionInfo submission : submissions) {
                if (member.getId().equals(submission.getUserId())) {
                    final PaperSubmissionInfo paper = new PaperSubmissionInfo();
                    paper.setUserId(member.getId());
                    paper.setDisplayName(member.getLastName() + ", " + member.getFirstName());
                    paper.setId(submission.getId());
                    paper.setSubmissionStatus(submission.getSubmissionStatus());
                    allSubmissions.add(paper);
                    submitted = true;
                }
            }
            if (!submitted) {
                final PaperSubmissionInfo paper = new PaperSubmissionInfo();
                paper.setUserId(member.getId());
                paper.setDisplayName(member.getLastName() + ", " + member.getFirstName());
                paper.setSubmissionStatus(SubmissionStatus.NOT_SUBMITTED);
                allSubmissions.add(paper);
            }
        }
        return allSubmissions;
    }

    /** 
	 * @return List of UserInfo
	 */
    @SuppressWarnings("unchecked")
    private List<UserInfo> loadCourseMembers(long domainId) {
        final Group group = getParticipantsGroup(domainId);
        final List<Authority> members = group.getMembers();
        final List<UserInfo> courseMembers = new ArrayList<UserInfo>(members.size());
        for (Authority auth : members) {
            courseMembers.add(getSecurityService().getUser(auth.getId()));
        }
        return courseMembers;
    }

    /** 
	 * @return List of FileInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleGetPaperSubmissionFiles(Collection submissions) {
        Validate.notNull(submissions, "submissions cannot be null.");
        final List<FileInfo> allFiles = new ArrayList<FileInfo>();
        for (PaperSubmissionInfo submission : (Collection<PaperSubmissionInfo>) submissions) {
            final List<FileInfo> filesOfSubmission = new ArrayList<FileInfo>();
            final FolderInfo folder = getDocumentService().getFolder(submission);
            final List<FolderEntryInfo> files = getDocumentService().getFolderEntries(submission, folder);
            filesOfSubmission.addAll(getDocumentService().allFileEntries(files));
            for (FileInfo file : filesOfSubmission) {
                final String path = submission.getFirstName() + "_" + submission.getLastName();
                file.setPath(path);
                file.setAbsoluteName(path + "/" + file.getFileName());
            }
            allFiles.addAll(filesOfSubmission);
        }
        return allFiles;
    }

    /** 
	 * @return List of SubmissionInfo
	 */
    @SuppressWarnings("unchecked")
    @Override
    protected List handleFindInTimePaperSubmissionsByExam(Long examId) {
        final List<PaperSubmissionInfo> allSubmissions = findPaperSubmissionsByExam(examId);
        final List<PaperSubmissionInfo> inTimeSubmissions = new ArrayList<PaperSubmissionInfo>();
        for (PaperSubmissionInfo submission : allSubmissions) {
            if (SubmissionStatus.IN_TIME.equals(submission.getSubmissionStatus())) {
                inTimeSubmissions.add(submission);
            }
        }
        return inTimeSubmissions;
    }

    private Group getParticipantsGroup(Long domainId) {
        return getSecurityService().getGroupByName("GROUP_COURSE_" + domainId + "_PARTICIPANTS");
    }
}
