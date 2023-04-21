package com.microbrain.cosmos.core.command.composite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import com.microbrain.cosmos.core.auth.Authorization;
import com.microbrain.cosmos.core.command.CosmosCommand;
import com.microbrain.cosmos.core.command.CosmosExecuteException;
import com.microbrain.cosmos.core.command.CosmosExecuter;
import com.microbrain.cosmos.core.command.CosmosMetaArgument;
import com.microbrain.cosmos.core.command.CosmosMetaCommand;
import com.microbrain.cosmos.core.command.CosmosResult;
import com.microbrain.cosmos.core.command.StandardCosmosCommand;
import com.microbrain.cosmos.core.command.StandardCosmosResult;
import com.microbrain.cosmos.core.constants.CosmosCommandSource;
import com.microbrain.cosmos.core.domain.CosmosDomain;
import com.microbrain.cosmos.core.permission.CosmosPermissionException;

/**
 * <p>
 * <code>SequenceCosmosCommand</code>����������һ��Ĭ��ʵ�֣������ʵ�ֶ����Լ̳���
 * <code>SequenceCosmosCommand</code>��<code>SequenceCosmosCommand</code>
 * ʵ����˳��ִ���������������<code>execute</code>���������������������ִ�н����кϲ���
 * </p>
 * 
 * @author Richard Sun (Richard.SunRui@gmail.com)
 * @version 1.0, 08/12/10
 * @see com.microbrain.cosmos.core.command.CosmosCommand
 * @see com.microbrain.cosmos.core.command.StandardCosmosCommand
 * @see com.microbrain.cosmos.core.command.composite.CosmosCompositeCommand
 * @see com.microbrain.cosmos.core.command.composite.IfCosmosCommand
 * @since CFDK 1.0
 */
public class SequenceCosmosCommand extends StandardCosmosCommand implements CosmosCompositeCommand {

    /**
	 * �����������б?
	 */
    protected Collection<CosmosCommand> commands = new ArrayList<CosmosCommand>();

    /**
	 * �̳��Ը���<code>StandardCosmosCommand</code>�Ĺ��캯��
	 * 
	 * @param name
	 *            ������ơ�
	 * @param domain
	 *            ������
	 * @param command
	 *            �������ݡ�
	 * @param executer
	 *            ִ������
	 * @param metaArgs
	 *            �����б?
	 * @param type
	 *            �������͡�
	 * @param source
	 *            ������Դ��
	 */
    public SequenceCosmosCommand(String name, CosmosDomain domain, String command, CosmosExecuter executer, Collection<CosmosMetaArgument> metaArgs, CosmosMetaCommand type, CosmosCommandSource source) {
        super(name, domain, command, executer, metaArgs, type, source);
    }

    /**
	 * �̳��Ը���<code>StandardCosmosCommand</code>�Ĺ��캯��
	 * 
	 * @param name
	 *            ������ơ�
	 * @param domain
	 *            ������
	 * @param command
	 *            �������ݡ�
	 * @param executer
	 *            ִ������ơ�
	 * @param metaArgs
	 *            �����б?
	 * @param type
	 *            �������͡�
	 * @param source
	 *            ������Դ��
	 */
    public SequenceCosmosCommand(String name, CosmosDomain domain, String command, String executer, Collection<CosmosMetaArgument> metaArgs, CosmosMetaCommand type, CosmosCommandSource source) {
        super(name, domain, command, executer, metaArgs, type, source);
    }

    public boolean add(CosmosCommand command) {
        return commands.add(command);
    }

    public Collection<CosmosCommand> commands() {
        return commands;
    }

    public boolean remove(CosmosCommand command) {
        return commands.remove(command);
    }

    @Override
    public CosmosResult execute(Map<String, Object> args) throws CosmosExecuteException {
        CosmosResult totalResult = new StandardCosmosResult(this), result = null;
        for (CosmosCommand command : commands) {
            result = command.execute(args);
            if (result != null) {
                String domain = command.getDomain().getName();
                String name = command.getName();
                args.put(domain + "." + name, result);
                totalResult.combine(result);
            }
        }
        return totalResult;
    }

    @Override
    public CosmosResult execute(Authorization auth, Map<String, Object> args) throws CosmosExecuteException, CosmosPermissionException {
        CosmosResult totalResult = new StandardCosmosResult(this), result = null;
        for (CosmosCommand command : commands) {
            result = command.execute(auth, args);
            args.put(command.getName(), result);
            totalResult.combine(result);
        }
        return totalResult;
    }
}
