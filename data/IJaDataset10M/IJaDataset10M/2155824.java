package org.wdcode.web.mina.need;

import org.apache.mina.core.service.IoAcceptor;

/**
 * Mina 接受器必须的类
 * @author WD
 * @since JDK6
 * @version 1.0 2010-08-08
 */
public interface MinaAcceptorNeed extends MinaNeed {

    /**
	 * 获得IO接受器
	 * @return IO接受器
	 */
    IoAcceptor getIoAcceptor();

    /**
	 * 设置IO接受器
	 * @param ioAcceptor IO接受器
	 */
    void setIoAcceptor(IoAcceptor ioAcceptor);
}
