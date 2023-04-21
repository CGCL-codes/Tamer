package net.sourceforge.ivi.core;

public class iviLogPoint {

    /**
	 * 
	 */
    public iviLogPoint(String name) {
        d_mgr = iviLogPointMgr.getDefault();
        d_pointName = name;
        d_mgr.addLogPoint(this);
    }

    public String getPointName() {
        return d_pointName;
    }

    public void enter(int log_level, String msg) {
        d_mgr.enter(this, log_level, msg);
    }

    public void log(int log_level, String msg) {
        d_mgr.log(this, log_level, msg);
    }

    public void leave(int log_level, String msg) {
        d_mgr.leave(this, log_level, msg);
    }

    private String d_pointName;

    private iviLogPointMgr d_mgr;
}
