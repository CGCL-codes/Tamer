package org.hip.vif.web.stale;

import java.sql.SQLException;
import java.util.Locale;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.vif.core.Activator;
import org.hip.vif.core.bom.CompletionHome;
import org.hip.vif.core.bom.Member;
import org.hip.vif.core.bom.MemberHome;
import org.hip.vif.core.bom.Question;
import org.hip.vif.core.bom.QuestionHome;
import org.hip.vif.core.bom.ResponsibleHome;
import org.hip.vif.core.bom.Text;
import org.hip.vif.core.bom.TextHome;
import org.hip.vif.core.bom.impl.BOMHelper;
import org.hip.vif.core.interfaces.IMessages;
import org.hip.vif.core.interfaces.IReviewable;

/**
 * Helper class to handle new reviewers for stale requests.
 *
 * @author Luthiger
 * Created: 21.10.2010
 * @see StaleRequestRemover
 */
public class StaleRequestHelper {

    public static interface Collector extends IReviewable {

        Long getReviewerID();

        String getReviewerFirstname();

        String getReviewerName();

        String getReviewerMail();

        void removeReviewer() throws VException, SQLException;

        String getContributionType(Locale inLocale);

        String getContributionTitle();

        AuthorGroup getAuthorGroup() throws Exception;

        void accept(StaleTextCollector inNotificator);
    }

    public abstract static class AbstractCollector {

        private Long reviewerID;

        private String reviewerFirstname;

        private String reviewerName;

        private String reviewerMail;

        private IMessages messages = Activator.getMessages();

        AbstractCollector(GeneralDomainObject inModel) throws VException {
            reviewerID = new Long(inModel.get(ResponsibleHome.KEY_MEMBER_ID).toString());
            reviewerFirstname = inModel.get(MemberHome.KEY_FIRSTNAME).toString();
            reviewerName = inModel.get(MemberHome.KEY_NAME).toString();
            reviewerMail = inModel.get(MemberHome.KEY_MAIL).toString();
        }

        public Long getReviewerID() {
            return reviewerID;
        }

        public String getReviewerFirstname() {
            return reviewerFirstname;
        }

        public String getReviewerName() {
            return reviewerName;
        }

        public String getReviewerMail() {
            return reviewerMail;
        }

        public String getContributionType(Locale inLocale) {
            return messages.getMessage(getMessageKey());
        }

        protected abstract String getMessageKey();
    }

    public static class QuestionCollector extends AbstractCollector implements Collector {

        private static final String KEY_TYPE = "org.hip.vif.msg.question.Question";

        private Long questionID;

        private String title;

        private Long groupID;

        private String decimal;

        private String remark;

        protected QuestionCollector(GeneralDomainObject inModel) throws VException {
            super(inModel);
            questionID = new Long(inModel.get(QuestionHome.KEY_ID).toString());
            title = inModel.get(QuestionHome.KEY_QUESTION).toString();
            groupID = new Long(inModel.get(QuestionHome.KEY_GROUP_ID).toString());
            decimal = inModel.get(QuestionHome.KEY_QUESTION_DECIMAL).toString();
            remark = inModel.get(QuestionHome.KEY_REMARK).toString();
        }

        public void removeReviewer() throws VException, SQLException {
            BOMHelper.getQuestionAuthorReviewerHome().removeReviewer(getReviewerID(), questionID);
        }

        public String getContributionTitle() {
            return title;
        }

        protected String getMessageKey() {
            return KEY_TYPE;
        }

        public AuthorGroup getAuthorGroup() throws Exception {
            Member lMember = BOMHelper.getQuestionAuthorReviewerHome().getAuthor(questionID);
            return new AuthorGroup(lMember, groupID);
        }

        public void setReviewer(Long inReviewerID) throws VException, SQLException {
            BOMHelper.getQuestionAuthorReviewerHome().setReviewer(inReviewerID, questionID);
        }

        public boolean checkRefused(Long inReviewerID) throws VException, SQLException {
            return BOMHelper.getQuestionAuthorReviewerHome().checkRefused(inReviewerID, questionID);
        }

        public void accept(StaleTextCollector inNotificator) {
            inNotificator.visitQuestion(this);
        }

        public String getDecimal() {
            return decimal;
        }

        public String getRemark() {
            return remark;
        }
    }

    public static class CompletionCollector extends AbstractCollector implements Collector {

        private static final String KEY_TYPE = "org.hip.vif.msg.question.Completion";

        private Long completionID;

        private String title;

        private String decimal;

        protected CompletionCollector(GeneralDomainObject inModel) throws VException {
            super(inModel);
            completionID = new Long(inModel.get(CompletionHome.KEY_ID).toString());
            title = inModel.get(CompletionHome.KEY_COMPLETION).toString();
            decimal = (String) getOwningQuestion(inModel).get(QuestionHome.KEY_QUESTION_DECIMAL);
        }

        private Question getOwningQuestion(GeneralDomainObject inModel) throws VException {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue(QuestionHome.KEY_ID, inModel.get(CompletionHome.KEY_QUESTION_ID));
            return (Question) BOMHelper.getQuestionHome().findByKey(lKey);
        }

        public void removeReviewer() throws VException, SQLException {
            BOMHelper.getCompletionAuthorReviewerHome().removeReviewer(getReviewerID(), completionID);
        }

        public String getContributionTitle() {
            return title;
        }

        protected String getMessageKey() {
            return KEY_TYPE;
        }

        public AuthorGroup getAuthorGroup() throws Exception {
            Member lMember = BOMHelper.getCompletionAuthorReviewerHome().getAuthor(completionID);
            Long lGroupID = BOMHelper.getJoinCompletionToQuestionHome().getGroupID(completionID);
            return new AuthorGroup(lMember, lGroupID);
        }

        public void setReviewer(Long inReviewerID) throws VException, SQLException {
            BOMHelper.getCompletionAuthorReviewerHome().setReviewer(inReviewerID, completionID);
        }

        public boolean checkRefused(Long inReviewerID) throws VException, SQLException {
            return BOMHelper.getCompletionAuthorReviewerHome().checkRefused(inReviewerID, completionID);
        }

        public void accept(StaleTextCollector inNotificator) {
            inNotificator.visitCompletion(this);
        }

        public String getDecimalID() {
            return decimal;
        }
    }

    public static class TextCollector extends AbstractCollector implements Collector {

        private static final String KEY_TYPE = "org.hip.vif.msg.question.Bibliography";

        private Long textID;

        private int textVersion;

        private String title;

        private String contentPlain;

        private String contentHtml;

        protected TextCollector(GeneralDomainObject inModel, String inLanguage) throws VException, SQLException {
            super(inModel);
            textID = new Long(inModel.get(TextHome.KEY_ID).toString());
            textVersion = Integer.parseInt(inModel.get(TextHome.KEY_VERSION).toString());
            title = inModel.get(TextHome.KEY_REFERENCE).toString();
            Text lText = getText(textID, textVersion);
            contentPlain = lText.getNotification();
            contentHtml = lText.getNotificationHtml();
        }

        private Text getText(Long inTextID, int inVersion) throws VException, SQLException {
            return BOMHelper.getTextHome().getText(inTextID.toString(), inVersion);
        }

        public void removeReviewer() throws VException, SQLException {
            BOMHelper.getTextAuthorReviewerHome().removeReviewer(getReviewerID(), textID, textVersion);
        }

        public String getContributionTitle() {
            return title;
        }

        protected String getMessageKey() {
            return KEY_TYPE;
        }

        public AuthorGroup getAuthorGroup() throws Exception {
            Member lMember = BOMHelper.getTextAuthorReviewerHome().getAuthor(textID, textVersion);
            return new AuthorGroup(lMember, null);
        }

        public void setReviewer(Long inReviewerID) throws VException, SQLException {
            BOMHelper.getTextAuthorReviewerHome().setReviewer(inReviewerID, textID, textVersion);
        }

        public boolean checkRefused(Long inReviewerID) throws VException, SQLException {
            return BOMHelper.getTextAuthorReviewerHome().checkRefused(inReviewerID, textID, textVersion);
        }

        public void accept(StaleTextCollector inNotificator) {
            inNotificator.visitText(this);
        }

        public String getContentPlain() {
            return contentPlain;
        }

        public String getContentHtml() {
            return contentHtml;
        }
    }

    /**
	 * Parameter object containing both the <code>Member</code> object and the group ID.
	 *
	 * @author Luthiger
	 * Created: 17.10.2010
	 */
    public static class AuthorGroup {

        private Member author;

        private Long groupID;

        AuthorGroup(Member inAuthor, Long inGroupID) {
            author = inAuthor;
            groupID = inGroupID;
        }

        public Long getAuthorID() throws VException {
            return new Long(author.get(MemberHome.KEY_ID).toString());
        }

        public Member getAuthor() {
            return author;
        }

        /**
		 * @return Long, may be <code>null</code> in case of Text entries
		 */
        public Long getGroupID() {
            return groupID;
        }
    }
}
