package common;

import com.opensymphony.xwork2.ActionSupport;

public class successAction extends ActionSupport {

    /**
	 * 这个serialVersionUID是干嘛用的？
	 */
    private static final long serialVersionUID = 5491285540594838058L;

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}
