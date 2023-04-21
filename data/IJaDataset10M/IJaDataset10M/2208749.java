package com.gjzq.exception;

/**
 * ����:  
 * ��Ȩ:	 Copyright (c) 2009
 * ��˾:	 ˼�ϿƼ�
 * ����:	 ���
 * �汾:	 1.0
 * ��������: 2011-6-2
 * ����ʱ��: ����02:03:50
 */
public class TradeException extends RuntimeException {

    public TradeException() {
        super();
    }

    public TradeException(String message) {
        super(message);
    }

    public TradeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradeException(Throwable cause) {
        super(cause);
    }
}
