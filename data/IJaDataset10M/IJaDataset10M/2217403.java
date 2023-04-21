package com.protocol7.casilda.model;

public interface MQHeader {

    void writeToMessage(com.ibm.mq.MQMessage mqMessage);
}
