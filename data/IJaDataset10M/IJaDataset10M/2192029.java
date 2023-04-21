package org.hip.vif.bom;

import java.sql.SQLException;
import org.hip.kernel.exc.VException;

/**
 * Interface for all homes of responsible domain objects, 
 * i.e. all author-reviewer objects.
 * 
 * Created on 15.08.2003
 * @author Luthiger
 */
public interface ResponsibleHome {

    public static final Integer VALUE_AUTHOR = new Integer(0);

    public static final Integer VALUE_REVIEWER = new Integer(1);

    public static final Integer VALUE_REVIEWER_REFUSED = new Integer(2);

    public static final String KEY_TYPE = "Type";

    public static final String KEY_MEMBER_ID = "MemberID";

    /**
	 * Returns the entry identified by the specified values, i.e. either
	 * author or reviwer.
	 * 
	 * @param inContributionID String
	 * @param inMemberID Long
	 * @return Responsible
	 * @throws VException
	 * @throws SQLException
	 */
    Responsible getResponsible(String inContributionID, Long inMemberID) throws VException, SQLException;

    /**
	 * Returns the responsible author.
	 * 
	 * @param inContributionID String
	 * @return Responsible
	 * @throws VException
	 * @throws SQLException
	 */
    Responsible getAuthor(String inContributionID) throws VException, SQLException;
}
