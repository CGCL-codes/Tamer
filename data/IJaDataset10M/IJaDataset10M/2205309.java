package com.pk.platform.business.report.vo;

import com.pk.platform.business.common.vo.CommonVO;

public class OperateLogVO extends CommonVO {

    private String operateTimeStart;

    private String operateTimeEnd;

    public String getOperateTimeStart() {
        return operateTimeStart;
    }

    public void setOperateTimeStart(String operateTimeStart) {
        this.operateTimeStart = operateTimeStart;
    }

    public String getOperateTimeEnd() {
        return operateTimeEnd;
    }

    public void setOperateTimeEnd(String operateTimeEnd) {
        this.operateTimeEnd = operateTimeEnd;
    }
}
