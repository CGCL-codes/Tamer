package org.commonfarm.community.web;

import org.commonfarm.community.model.Space;
import org.commonfarm.service.BusinessException;
import org.commonfarm.service.ThinkingService;
import org.commonfarm.util.StringUtil;
import org.commonfarm.web.WebWorkAction;
import com.opensymphony.xwork.Preparable;

public class SpaceAction extends WebWorkAction implements Preparable {

    private long actionId;

    /** Search Criterias Start **/
    private String s_name;

    private String s_type;

    /** Search Criterias End **/
    public SpaceAction(ThinkingService thinkingService) {
        super(thinkingService);
    }

    public void remove(String id) throws BusinessException {
        thinkingService.removeObject(model, new Long(id), new String[] { "topics", "space" });
    }

    public void prepare() throws Exception {
        if (StringUtil.isEmpty(getSearchName())) setSearchName("space");
        if (actionId == 0) {
            model = new Space();
        } else {
            model = thinkingService.getObject(Space.class, new Long(actionId));
        }
    }

    /**
	 * @return the s_name
	 */
    public String getS_name() {
        return s_name;
    }

    /**
	 * @param s_name the s_name to set
	 */
    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    /**
	 * @return the s_type
	 */
    public String getS_type() {
        return s_type;
    }

    /**
	 * @param s_type the s_type to set
	 */
    public void setS_type(String s_type) {
        this.s_type = s_type;
    }
}
