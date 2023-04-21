package ces.arch.query;

/**
 * @author ������
 *  
 */
public class ListQuery extends Query {

    private int pageSize = 20;

    private int cursor = 1;

    private int totalNum = 20;

    /**
	 * @return Returns the cursor.
	 */
    public int getCursor() {
        return cursor;
    }

    /**
	 * @param cursor
	 *            The cursor to set.
	 */
    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    /**
	 * @return Returns the pageSize.
	 */
    public int getPageSize() {
        return pageSize;
    }

    /**
	 * @param pageSize
	 *            The pageSize to set.
	 */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
	 * @return Returns the totalNum.
	 */
    public int getTotalNum() {
        return totalNum;
    }

    /**
	 * @param totalNum
	 *            The totalNum to set.
	 */
    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
