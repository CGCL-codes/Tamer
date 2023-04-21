package org.frameworkset.spi.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.jgroups.Address;

/**
 * 
 * <p>
 * Title: Target.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-4-20 ����05:30:19
 * @author biaoping.yin
 * @version 1.0
 */
public interface Target extends java.io.Serializable {

    /**
	 * BROADCAST_TYPE_MUTICAST��BROADCAST_TYPE_UNICASTΪ����Э�飬�°�ϵͳ����֧��
	 * �ֱ������¼��������滻
	 * BROADCAST_TYPE_JRGOUP
	 * BROADCAST_TYPE_MINA
	 * BROADCAST_TYPE_JMS
	 * BROADCAST_TYPE_WEBSERVICE
	 * 	 * 
	 * ָ����Ӧ��Э�����Ҫ����ÿ��Э���������ò���
	 */
    public static final String BROADCAST_TYPE_MUTICAST = "muticast";

    public static final String BROADCAST_TYPE_UNICAST = "unicast";

    public static final String BROADCAST_TYPE_JRGOUP = "jgroup";

    public static final String BROADCAST_TYPE_MINA = "mina";

    public static final String BROADCAST_TYPE_NETTY = "netty";

    public static final String BROADCAST_TYPE_JMS = "jms";

    public static final String BROADCAST_TYPE_RMI = "rmi";

    public static final String BROADCAST_TYPE_EJB = "ejb";

    public static final String BROADCAST_TYPE_HTTP = "http";

    public static final String BROADCAST_TYPE_HTTPS = "https";

    public static final String BROADCAST_TYPE_CORBA = "corba";

    public static final String BROADCAST_TYPE_WEBSERVICE = "webservice";

    public static final String BROADCAST_TYPE_REST = "rest";

    public static final String REST_LOCAL = "_local_";

    public String getStringTargets();

    public boolean ismuticast();

    public boolean isunicast();

    public boolean protocol_jgroup();

    public boolean protocol_rest();

    public boolean protocol_mina();

    public boolean protocol_netty();

    public boolean protocol_webservice();

    public boolean protocol_jms();

    public boolean protocol_rmi();

    public boolean protocol_http();

    public boolean protocol_ejb();

    public List<RPCAddress> getTargets();

    public boolean isAll();

    public boolean isSelf();

    public String getFirstNode();

    public String getNextNode();
}
