package com.microbrain.cosmos.core.command.loaders;

import java.util.Collection;
import com.microbrain.cosmos.core.command.CosmosCommand;
import com.microbrain.cosmos.core.domain.CosmosDomain;

/**
 * <p>
 * ȫ�ֵ�����װ����������Щ���У��ǿ��Դ洢����������ģ���������֮Ϊ����Ϊmaster���͵���
 * ���磺��ݿ��򣬿�������ݿ���д洢�����ʱ����Ҫͨ��ʵ�ֱ��ӿڣ��ﵽ�ܴ����������װ�������Ŀ�ġ�
 * </p>
 * 
 * @author Richard Sun (Richard.SunRui@gmail.com)
 * @version 1.0, 08/12/10
 * @see com.microbrain.cosmos.core.command.CosmosCommand
 * @see com.microbrain.cosmos.core.domain.CosmosDomain
 * @since CFDK 1.0
 */
public interface CosmosGlobalCommandLoader {

    /**
	 * װ�����е����
	 * 
	 * @return ���������б?
	 * @throws CosmosCommandLoaderException
	 *             װ��ʱ�׳����쳣��
	 */
    public Collection<CosmosCommand> loadAllCommands() throws CosmosCommandLoaderException;

    /**
	 * װ��ĳ�����µ��������
	 * 
	 * @param domain
	 *            ������
	 * @return ���������������б?
	 * @throws CosmosCommandLoaderException
	 *             װ��ʱ�׳����쳣��
	 */
    public Collection<CosmosCommand> loadDomainCommands(CosmosDomain domain) throws CosmosCommandLoaderException;

    /**
	 * װ��ĳ�����
	 * 
	 * @param domain
	 *            ������
	 * @param name
	 *            ������ơ�
	 * @return �����
	 * @throws CosmosCommandLoaderException
	 *             װ��ʱ�׳����쳣��
	 */
    public CosmosCommand loadCommand(CosmosDomain domain, String name) throws CosmosCommandLoaderException;
}
