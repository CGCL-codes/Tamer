package com.hs.core;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import com.hs.dao.CheckCardDao;
import com.hs.domain.CheckCard;

/**
 * @author <a href="mailto:guangzong@gmail.com">Guangzong Syu</a>
 *
 */
public class SwapListerner extends BaseAction {

    private static final long serialVersionUID = 8931619990440158065L;

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private CheckCardDao checkCardDao;

    private String cardNo;

    private String readerNo;

    private String time;

    private Timestamp checkTime;

    /**
	 * @return the cardNo
	 */
    public String getCardNo() {
        return cardNo;
    }

    /**
	 * @param cardNo the cardNo to set
	 */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    /**
	 * @return the readerNo
	 */
    public String getReaderNo() {
        return readerNo;
    }

    /**
	 * @param readerNo the readerNo to set
	 */
    public void setReaderNo(String readerNo) {
        this.readerNo = readerNo;
    }

    /**
	 * @return the time
	 */
    public String getTime() {
        return time;
    }

    /**
	 * @param time the time to set
	 */
    public void setTime(String time) {
        if (StringUtils.isNotBlank(time)) {
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
            try {
                checkTime = new Timestamp(sdf.parse(time).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.time = time;
    }

    /**
	 * @param checkCardDap the checkCardDap to set
	 */
    public void setCheckCardDao(CheckCardDao checkCardDao) {
        this.checkCardDao = checkCardDao;
    }

    @Override
    public String execute() throws Exception {
        if (StringUtils.isNotBlank(cardNo) && StringUtils.isNotBlank(readerNo)) {
            if (checkTime == null) {
                checkTime = DateUtil.getCurrentTimestamp();
            }
            CheckCard cc = new CheckCard();
            cc.setCardNo(cardNo);
            cc.setReader(readerNo);
            cc.setCreatedDate(checkTime);
        }
        return super.execute();
    }
}
