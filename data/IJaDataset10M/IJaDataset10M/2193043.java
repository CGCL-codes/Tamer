package csiebug.util.zip;

/**
 * 可以被任何UI複寫printMessage方法,來呈現zip的progress
 * @author George_Tsai
 * @version 2010/8/3
 */
public abstract class ZipMonitor {

    private ZipUtility zipUtility;

    public void setZipUtility(ZipUtility zipUtility) {
        this.zipUtility = zipUtility;
    }

    /**
	 * 主動從ZipUtility取得目前訊息
	 * @return
	 */
    public String getCurrentMessage() {
        return zipUtility.getCurrentMessage();
    }

    /**
	 * 主動從ZipUtility取得目前完成的磁碟容量
	 * @return
	 */
    public long getFinishSpace() {
        return zipUtility.getFinishSpace();
    }

    /**
	 * 主動從ZipUtility取得完成的總磁碟容量
	 * @return
	 */
    public long getTotalSpace() {
        return zipUtility.getTotalSpace();
    }

    /**
	 * 主動從ZipUtility取得目前完成工作的百分比
	 * @return
	 */
    public long getZipPercetage() {
        return zipUtility.getZipPercetage();
    }

    /**
	 * 被ZipUtility被動告知進度改變
	 * 可用此method主動通知UI改變
	 */
    public abstract void printMessage();
}
