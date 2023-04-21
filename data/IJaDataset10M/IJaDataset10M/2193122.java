package marla.ide.problem;

import org.jdom.Element;

/**
 * Simple interface.
 *
 * @author Ryan Morehart
 */
public interface ProblemPart extends Changeable, Loadable {

    /**
	 * Gets the description for this part of the problem
	 * @return String description of the question for this sub problem
	 */
    public String getStatement();

    /**
	 * Problem statement for this problem.
	 *
	 * @param newStatement String description of problem. See
	 *		Problem(String) for more information.
	 */
    public void setStatement(String newStatement);

    /**
	 * Gets the current conclusion of this problem part. (See setConclusion)
	 * @return Current conclusion. Null if there was none
	 */
    public String getConclusion();

    /**
	 * Sets a new conclusion for this problem part. This is intended as
	 * to contain the ending thought of the associated analysis and operations.
	 * @param newConclusion New conclusion for ProblemPart
	 * @return Current conclusion. Null if there was none
	 */
    public String setConclusion(String newConclusion);

    /**
	 * Trues true if the problem has unsaved changes
	 * @return true if the problem has changes that are not yet saved
	 */
    public boolean isChanged();

    /**
	 * Returns this problem part as a JDOM Element
	 * @return JDOM element containing all information needed to rebuild
	 *		this exact problem
	 */
    public Element toXml();
}
