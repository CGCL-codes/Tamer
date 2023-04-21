package howbuy.android.palmfund.clientbean;

import java.io.Serializable;

/**
 * 排序字段
 * 
 * @author yescpu
 * 
 */
public class FundInfoConfigSort implements Serializable {

    String name;

    String sortColomns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortColomns() {
        return sortColomns;
    }

    public void setSortColomns(String sortColomns) {
        this.sortColomns = sortColomns;
    }

    @Override
    public String toString() {
        return "FundInfoConfigSort [name=" + name + ", sortColomns=" + sortColomns + "]";
    }
}
