package com.microbrain.cosmos.core.command.config;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import com.microbrain.cosmos.core.CosmosFactory;
import com.microbrain.cosmos.core.command.CosmosArgumentConverter;
import com.microbrain.cosmos.core.command.CosmosCommand;
import com.microbrain.cosmos.core.command.CosmosMetaArgument;
import com.microbrain.cosmos.core.command.CosmosMetaCommand;
import com.microbrain.cosmos.core.command.StandardCosmosMetaArgument;
import com.microbrain.cosmos.core.command.loaders.AbstractCosmosLocalCommandLoader;
import com.microbrain.cosmos.core.command.loaders.CosmosCommandLoaderException;
import com.microbrain.cosmos.core.constants.ArgumentInOutType;
import com.microbrain.cosmos.core.constants.Constants;
import com.microbrain.cosmos.core.constants.CosmosCommandSource;
import com.microbrain.cosmos.core.constants.DebugLevel;
import com.microbrain.cosmos.core.domain.CosmosDomain;

/**
 * <p>
 * ��ȡ�����ļ��ı�������װ������ʵ����<code>CosmosLocalCommandLoader</code>�ӿڣ���Ҫ�Ĺ����ǴӸ������Լ��������ļ���
 * ��ȡ��Ӧ���������װ�ɿ���ִ�еĵ�Ԫ��
 * </p>
 * <p>
 * <code>CosmosConfigLocalCommandLoader</code>ʵ����װ��һ���������б�������ķ�����
 * �Լ�װ��ĳ�����µ�������ķ�����
 * </p>
 * 
 * @author Richard Sun (Richard.SunRui@gmail.com)
 * @version 1.0, 08/12/10
 * @see com.microbrain.cosmos.core.command.loaders.CosmosLocalCommandLoader
 * @see com.microbrain.cosmos.core.command.loaders.AbstractCosmosLocalCommandLoader
 * @see com.microbrain.cosmos.core.domain.CosmosDomain
 * @see com.microbrain.cosmos.core.command.CosmosCommand
 * @since CFDK 1.0
 */
public class CosmosConfigLocalCommandLoader extends AbstractCosmosLocalCommandLoader {

    /**
	 * ��������ļ���ĳ���������е�����ڵ㡣
	 */
    public static final String COSMOS_DOMAIN_COMMAND = "cosmos.domains.domain(%d).commands.command";

    /**
	 * ��������ļ���ĳ���������е�����ڵ�����ݡ�
	 */
    public static final String COSMOS_DOMAIN_COMMAND_CONTENT = "cosmos.domains.domain(%d).commands.command(%d)";

    /**
	 * ��������ļ���ĳ���������е�����ڵ��ִ������
	 */
    public static final String COSMOS_DOMAIN_COMMAND_EXECUTER = "cosmos.domains.domain(%d).commands.command(%d).[@executer]";

    /**
	 * ��������ļ���ĳ���������е�����ڵ�����͡�
	 */
    public static final String COSMOS_DOMAIN_COMMAND_TYPE = "cosmos.domains.domain(%d).commands.command(%d).[@type]";

    /**
	 * ��������ļ���ĳ���������е�����ڵ�ĵ��Լ���
	 */
    public static final String COSMOS_DOMAIN_COMMAND_DEBUG = "cosmos.domains.domain(%d).commands.command(%d).[@debug]";

    /**
	 * ��������ļ���ĳ���������е�����ڵ�Ĳ����б?
	 */
    public static final String COSMOS_DOMAIN_COMMAND_ARG = "cosmos.domains.domain(%d).commands.command(%d).argument";

    /**
	 * ��������ļ���ĳ���������е�����ڵ��˵�����ݡ�
	 */
    public static final String COSMOS_DOMAIN_COMMAND_REMARK = "cosmos.domains.domain(%d).commands.command(%d).remark";

    /**
	 * ��������ļ���ĳ������ĳ������ڵ��ĳ������
	 */
    public static final String COSMOS_DOMAIN_COMMAND_ARG_REMARK = "cosmos.domains.domain(%d).commands.command(%d).argument(%d)";

    /**
	 * ��������ļ���ĳ���������е�����ڵ�Ĳ���IN/OUT���͡�
	 */
    public static final String COSMOS_DOMAIN_COMMAND_ARG_INOUTTYPE = "cosmos.domains.domain(%d).commands.command(%d).argument(%d).[@in-out-type]";

    /**
	 * ��������ļ���ĳ���������е�����ڵ�Ĳ�������ת������
	 */
    public static final String COSMOS_DOMAIN_COMMAND_ARG_CONVERTER = "cosmos.domains.domain(%d).commands.command(%d).argument(%d).[@converter]";

    /**
	 * ���캯��
	 * 
	 * @param factory
	 *            Cosmos�����ࡣ
	 * @param domain
	 *            ��װ����������
	 */
    public CosmosConfigLocalCommandLoader(CosmosFactory factory, CosmosDomain domain) {
        super(factory, domain);
    }

    public Collection<CosmosCommand> loadAllLocalRootCommands() throws CosmosCommandLoaderException {
        int domainIndex = 0;
        Collection<CosmosCommand> commands = new ArrayList<CosmosCommand>();
        try {
            Map<String, CosmosMetaCommand> commandTypes = config.getCommandTypes();
            for (String curr : this.config.getByName(Constants.COSMOS_DOMAINS_DOMAIN)) {
                if (curr.equals(domain.getName())) {
                    int commandIndex = 0;
                    for (String name : this.config.getByName(String.format(COSMOS_DOMAIN_COMMAND, domainIndex))) {
                        String executer = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_EXECUTER, domainIndex, commandIndex));
                        String command = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_CONTENT, domainIndex, commandIndex));
                        String type = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_TYPE, domainIndex, commandIndex));
                        String remark = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_REMARK, domainIndex, commandIndex));
                        String debug = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_DEBUG, domainIndex, commandIndex));
                        CosmosMetaCommand commandType = config.getDefaultCommandType();
                        Collection<CosmosMetaArgument> metaArgs = new ArrayList<CosmosMetaArgument>();
                        int argIndex = 0;
                        for (String argName : this.config.getByName(String.format(COSMOS_DOMAIN_COMMAND_ARG, domainIndex, commandIndex))) {
                            String inOutTypeValue = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_ARG_INOUTTYPE, domainIndex, commandIndex, argIndex));
                            String converterValue = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_ARG_CONVERTER, domainIndex, commandIndex, argIndex));
                            String argRemark = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_ARG_REMARK, domainIndex, commandIndex, argIndex));
                            ArgumentInOutType inOutType = inOutTypeValue == null ? ArgumentInOutType.IN : ArgumentInOutType.valueOf(inOutTypeValue);
                            CosmosArgumentConverter converter = converterValue == null ? null : this.factory.getConverter(converterValue);
                            CosmosMetaArgument arg = new StandardCosmosMetaArgument(converter, argName, inOutType, argRemark);
                            metaArgs.add(arg);
                            argIndex++;
                        }
                        if (type != null && !"".equals(type.trim())) {
                            commandType = commandTypes.get(type);
                        }
                        Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                        CosmosCommand cosmosCommand = constructor.newInstance(name, domain, command, executer, metaArgs, commandType, CosmosCommandSource.LOCAL);
                        cosmosCommand.setRemark(remark);
                        if (debug == null || "".equals(debug.trim()) || DebugLevel.NO_DEBUG.toString().equals(debug)) {
                            cosmosCommand.setDebugLevel(domain.getDebugLevel());
                        } else {
                            cosmosCommand.setDebugLevel(DebugLevel.valueOf(debug));
                        }
                        commands.add(cosmosCommand);
                        commandIndex++;
                    }
                    break;
                }
                domainIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commands;
    }

    public CosmosCommand loadLocalRootCommand(String name) throws CosmosCommandLoaderException {
        int domainIndex = 0;
        CosmosCommand cosmosCommand = null;
        try {
            Map<String, CosmosMetaCommand> commandTypes = config.getCommandTypes();
            for (String curr : this.config.getByName(Constants.COSMOS_DOMAINS_DOMAIN)) {
                if (curr.equals(domain.getName())) {
                    int commandIndex = 0;
                    for (String commandName : this.config.getByName(String.format(COSMOS_DOMAIN_COMMAND, domainIndex))) {
                        if (commandName.equals(name)) {
                            String executer = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_EXECUTER, domainIndex, commandIndex));
                            String command = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_CONTENT, domainIndex, commandIndex));
                            String type = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_TYPE, domainIndex, commandIndex));
                            String remark = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_REMARK, domainIndex, commandIndex));
                            String debug = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_DEBUG, domainIndex, commandIndex));
                            CosmosMetaCommand commandType = config.getDefaultCommandType();
                            Collection<CosmosMetaArgument> metaArgs = new ArrayList<CosmosMetaArgument>();
                            int argIndex = 0;
                            for (String argName : this.config.getByName(String.format(COSMOS_DOMAIN_COMMAND_ARG, domainIndex, commandIndex))) {
                                String inOutTypeValue = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_ARG_INOUTTYPE, domainIndex, commandIndex, argIndex));
                                String converterValue = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_ARG_CONVERTER, domainIndex, commandIndex, argIndex));
                                String argRemark = this.config.getString(String.format(COSMOS_DOMAIN_COMMAND_ARG_REMARK, domainIndex, commandIndex, argIndex));
                                ArgumentInOutType inOutType = inOutTypeValue == null ? ArgumentInOutType.IN : ArgumentInOutType.valueOf(inOutTypeValue);
                                CosmosArgumentConverter converter = converterValue == null ? null : this.factory.getConverter(converterValue);
                                CosmosMetaArgument arg = new StandardCosmosMetaArgument(converter, argName, inOutType, argRemark);
                                metaArgs.add(arg);
                                argIndex++;
                            }
                            if (type != null && !"".equals(type.trim())) {
                                commandType = commandTypes.get(type);
                            }
                            Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                            cosmosCommand = constructor.newInstance(name, domain, command, executer, metaArgs, commandType, CosmosCommandSource.LOCAL);
                            cosmosCommand.setRemark(remark);
                            if (debug == null || "".equals(debug.trim()) || DebugLevel.NO_DEBUG.toString().equals(debug)) {
                                cosmosCommand.setDebugLevel(domain.getDebugLevel());
                            } else {
                                cosmosCommand.setDebugLevel(DebugLevel.valueOf(debug));
                            }
                            break;
                        }
                        commandIndex++;
                    }
                    break;
                }
                domainIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cosmosCommand;
    }
}
