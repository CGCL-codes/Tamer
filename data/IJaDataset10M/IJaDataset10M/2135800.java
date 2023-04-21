package magoffin.matt.ieat.util;

import magoffin.matt.ieat.biz.RecipeSearchBiz;

/**
 * Recipe command object.
 * 
 * @author Matt Magoffin (spamsqr@msqr.us)
 * @version $Revision: 26 $ $Date: 2009-05-03 19:35:54 -0400 (Sun, 03 May 2009) $
 */
public class UserCommand {

    private Integer userId;

    private RecipeSearchBiz.IndexCriteria index;

    /**
	 * @return the userId
	 */
    public Integer getUserId() {
        return userId;
    }

    /**
	 * @param userId the userId to set
	 */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
	 * @return the index
	 */
    public RecipeSearchBiz.IndexCriteria getIndex() {
        return index;
    }

    /**
	 * @param index the index to set
	 */
    public void setIndex(RecipeSearchBiz.IndexCriteria index) {
        this.index = index;
    }
}
