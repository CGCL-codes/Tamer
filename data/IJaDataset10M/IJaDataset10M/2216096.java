package com.gever.goa.infoservice.vo;

import com.gever.vo.BaseTreeVO;
import com.gever.vo.VOInterface;

/**
 * <p>Title:�ͻ�������VO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: �������</p>
 * @author Hu.Walker
 * @version 1.0  ��������:2004-8-11
 */
public class IsCustomerSecTreeVO extends BaseTreeVO implements VOInterface {

    public IsCustomerSecTreeVO() {
    }

    public void setOtherProperty(String[] values) {
        super.setAction("/infoservice/customerlist.do?type=" + this.getNodeid());
        super.setIsfolder("0");
        super.setTarget("middle");
    }
}
