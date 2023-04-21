package Repository.RepositoryChairMaintenance;

import Repository.Entities.IPCMember;
import Repository.Entities.IPaper;

/**
 * 
 *<p>
 * This interface associates a paper with a reviewer
 * </p>
 * 
 * @author G02
 * @version 0.5.1
 * @since 0.4
 * @see IPaper
 * @see IPCMember
 * 
 */
public interface IPaperAssignment {

    /**
	 * <p>This method returns a paper associated with the review</p>
	 * 
	 * @see IPaper
	 * @return the paper
	 */
    IPaper getPaper();

    /**
	 * <p>This method returns the reviewer associated with the paper</p>
	 * 
	 * @see IPCMember
	 * @return the reviewer
	 */
    IPCMember getReviewer();
}
