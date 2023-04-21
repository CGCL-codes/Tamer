package howbuy.android.palmfund.clientbean;

import java.io.Serializable;

/**
 * 切换字段
 * 
 * @author yescpu
 * 
 */
public class FundInfoConfigChange implements Serializable {

    private String name;

    /**
	 * 数据库对应字段
	 */
    private String changePostion;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChangePostion() {
        return changePostion;
    }

    public void setChangePostion(String changePostion) {
        this.changePostion = changePostion;
    }

    @Override
    public String toString() {
        return "FundInfoConfigChange [name=" + name + ", changePostion=" + changePostion + "]";
    }
}
