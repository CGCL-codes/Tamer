package org.openuss.braincontest;

import java.util.List;
import org.openuss.documents.FileInfo;
import org.openuss.foundation.DomainObject;
import org.openuss.security.UserInfo;

/**
 * 
 * @author Ingo Dueppe
 * 
 */
public interface BrainContestService {

    /**
	 * @return List of BrainContestInfo objects
	 */
    public List getContests(DomainObject domainObject);

    public BrainContestInfo getContest(BrainContestInfo contest);

    public void createContest(BrainContestInfo contest) throws BrainContestApplicationException;

    public void saveContest(BrainContestInfo contest) throws BrainContestApplicationException;

    /**
	 * Retrieve a list of attached file informations.
	 * 
	 * @param BrainContestInfo
	 *            object with BrainContest identifier
	 * @return List of org.openuss.documents.FileInfo objects
	 */
    public List getAttachments(BrainContestInfo contest);

    public void addAttachment(BrainContestInfo contest, FileInfo fileInfo) throws BrainContestApplicationException;

    /**
	 * @return true - if the answer is correct
	 */
    public boolean answer(String answer, UserInfo user, BrainContestInfo contest, boolean topList) throws BrainContestApplicationException;

    public void removeAttachment(BrainContestInfo contest, FileInfo fileInfo) throws BrainContestApplicationException;

    /**
	 * @return List of AnswerInfo objects
	 */
    public List getAnswers(BrainContestInfo contest);

    public void removeContest(BrainContestInfo contest) throws BrainContestApplicationException;
}
