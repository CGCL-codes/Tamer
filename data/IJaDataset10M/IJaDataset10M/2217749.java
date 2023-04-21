package project.cn.dataType;

import java.util.ArrayList;

public class DLogList extends Data {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5896365237018898675L;

    private ArrayList<DLog> loglist;

    public DLogList() {
        setLoglist(new ArrayList<DLog>());
    }

    public void setLoglist(ArrayList<DLog> loglist) {
        this.loglist = loglist;
    }

    public ArrayList<DLog> getLoglist() {
        return loglist;
    }
}
