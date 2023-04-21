package cwterm.service.rigctl;

public class ChannelCap {

    private int cap;

    private long funcs, levels;

    /**
	 * @return the cap
	 */
    public int getCap() {
        return cap;
    }

    /**
	 * @param cap the cap to set
	 */
    public void setCap(int cap) {
        this.cap = cap;
    }

    /**
	 * @return the funcs
	 */
    public long getFuncs() {
        return funcs;
    }

    /**
	 * @param funcs the funcs to set
	 */
    public void setFuncs(long funcs) {
        this.funcs = funcs;
    }

    /**
	 * @return the levels
	 */
    public long getLevels() {
        return levels;
    }

    /**
	 * @param levels the levels to set
	 */
    public void setLevels(long levels) {
        this.levels = levels;
    }
}
