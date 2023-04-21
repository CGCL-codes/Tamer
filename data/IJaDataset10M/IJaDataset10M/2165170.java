package wangzx.gwt.databean.client.交罚代办单.state;

import wangzx.gwt.databean.client.交罚代办单.FlowAction;

public class State异地已缴纳 {

    @FlowAction(next = State异地已回单.class)
    static class Action异地已回单 {
    }
}
