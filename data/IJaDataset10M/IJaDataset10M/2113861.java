package wangzx.gwt.databean.client.交罚代办单;

import wangzx.gwt.databean.client.model.FieldInfo;
import wangzx.gwt.databean.client.model.ReflectibleBean;

public class 交罚代办工单 implements ReflectibleBean {

    @FieldInfo(createEditable = false)
    String 工单编号;

    @FieldInfo(createEditable = false)
    String 当前状态;

    @FieldInfo(createEditable = false)
    String 车牌号;

    @FieldInfo(createEditable = false)
    String 车牌颜色;

    @FieldInfo(createEditable = false)
    String 文书号;

    int 补款金额;

    int 补款方式;

    String 补款原因;

    String 车主$姓名;

    String 车主$手机;

    String 车主$配送地址;

    String 车资料$车型;

    String 车资料$座位数;

    String 车资料$吨位数;
}
