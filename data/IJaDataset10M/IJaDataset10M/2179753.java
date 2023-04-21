package com.microbrain.cosmos.core.command;

import com.microbrain.cosmos.core.constants.ArgumentInOutType;

/**
 * <p>
 * ���������࣬������һ������Ĳ����������������Կ�����Ҫ������ת���������Ӧ������ת������
 * </p>
 * 
 * @author Richard Sun (Richard.SunRui@gmail.com)
 * @version 1.0, 08/12/10
 * @see com.microbrain.cosmos.core.command.CosmosCommand
 * @see com.microbrain.cosmos.core.command.StandardCosmosMetaArgument
 * @since CFDK 1.0
 */
public interface CosmosMetaArgument {

    /**
	 * ��ò�����ơ�
	 * 
	 * @return ������ơ�
	 */
    public String getName();

    /**
	 * ��ò����IN/OUT���͡�
	 * 
	 * @return �����IN/OUT���͡�
	 */
    public ArgumentInOutType getInOutType();

    /**
	 * ��ò��������ת������
	 * 
	 * @return ���������ת������
	 */
    public CosmosArgumentConverter getConverter();

    /**
	 * ��ò����ע�͡�
	 * 
	 * @return �����ע�͡�
	 */
    public String getRemark();
}
