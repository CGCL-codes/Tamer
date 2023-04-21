package com.microbrain.cosmos.core.command.db;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import com.microbrain.cosmos.core.CosmosFactory;
import com.microbrain.cosmos.core.command.CosmosCommand;
import com.microbrain.cosmos.core.command.CosmosMetaArgument;
import com.microbrain.cosmos.core.command.CosmosMetaCommand;
import com.microbrain.cosmos.core.command.StandardCosmosMetaArgument;
import com.microbrain.cosmos.core.command.composite.CosmosCompositeCommand;
import com.microbrain.cosmos.core.command.loaders.AbstractCosmosGlobalCommandLoader;
import com.microbrain.cosmos.core.command.loaders.CosmosCommandLoaderException;
import com.microbrain.cosmos.core.constants.ArgumentInOutType;
import com.microbrain.cosmos.core.constants.CosmosCommandSource;
import com.microbrain.cosmos.core.constants.DebugLevel;
import com.microbrain.cosmos.core.domain.CosmosDomain;
import com.microbrain.cosmos.core.domain.db.CosmosSqlDomain;

/**
 * <p>
 * <code>CosmosDbGlobalCommandLoader</code> ��һ��������ݿ��ȫ������װ������ͨ�������������ݿ��У�
 * ������Ա�������ɵ�ʵ�ֵ���ʱ��װ�صĵķ�ʽ�����������Ч�ؽ��͵��Ե�ʱ�������������������ʱ�䡣
 * </p>
 * 
 * @author Richard Sun (Richard.SunRui@gmail.com)
 * @version 1.0, 08/12/10
 * @see com.microbrain.cosmos.core.command.loaders.CosmosGlobalCommandLoader
 * @see com.microbrain.cosmos.core.command.loaders.AbstractCosmosGlobalCommandLoader
 * @see com.microbrain.cosmos.core.domain.CosmosDomain
 * @see com.microbrain.cosmos.core.command.CosmosCommand
 * @since CFDK 1.0
 */
public class CosmosDbGlobalCommandLoader extends AbstractCosmosGlobalCommandLoader {

    /**
	 * װ�����и������SQL��䡣
	 */
    private static final String ALL_ROOT_COMMANDS = "SELECT command.ID, command.NAME, command.DOMAIN, command.COMMAND, command.EXECUTER, command.TYPE, command.REMARK, command.DEBUG_LEVEL," + " arg.NAME, arg.CONVERTER, arg.IN_OUT_TYPE, arg.REMARK, composite.LFT_INDEX, composite.RGT_INDEX" + " FROM tb_cos_composite_command composite" + " LEFT JOIN tb_cos_command command ON command.DOMAIN = composite.DOMAIN AND command.NAME = composite.COMMAND" + " LEFT JOIN tb_cos_command_arg arg ON arg.DOMAIN = command.DOMAIN AND arg.COMMAND = command.NAME" + " WHERE composite.LFT_INDEX = 1 ORDER BY command.DOMAIN ASC, command.NAME ASC, arg.ARG_INDEX ASC";

    /**
	 * װ��ĳ���������и������SQL��䡣
	 */
    private static final String DOMAIN_ROOT_COMMANDS = "SELECT command.ID, command.NAME, command.DOMAIN, command.COMMAND, command.EXECUTER, command.TYPE, command.REMARK, command.DEBUG_LEVEL," + " arg.NAME, arg.CONVERTER, arg.IN_OUT_TYPE, arg.REMARK, composite.LFT_INDEX, composite.RGT_INDEX" + " FROM tb_cos_composite_command composite" + " LEFT JOIN tb_cos_command command ON command.DOMAIN = composite.DOMAIN AND command.NAME = composite.COMMAND" + " LEFT JOIN tb_cos_command_arg arg ON arg.DOMAIN = command.DOMAIN AND arg.COMMAND = command.NAME" + " WHERE command.DOMAIN = ? AND composite.LFT_INDEX = 1 ORDER BY command.DOMAIN ASC, command.NAME ASC, arg.ARG_INDEX ASC";

    /**
	 * װ�ص��������SQL��䡣
	 */
    private static final String SINGLE_COMMAND = "SELECT command.ID, command.NAME, command.DOMAIN, command.COMMAND, command.EXECUTER, command.TYPE, command.REMARK, command.DEBUG_LEVEL," + " arg.NAME, arg.CONVERTER, arg.IN_OUT_TYPE, arg.REMARK, composite.LFT_INDEX, composite.RGT_INDEX" + " FROM tb_cos_composite_command composite" + " LEFT JOIN tb_cos_command command ON command.DOMAIN = composite.DOMAIN AND command.NAME = composite.COMMAND" + " LEFT JOIN tb_cos_command_arg arg ON arg.DOMAIN = command.DOMAIN AND arg.COMMAND = command.NAME" + " WHERE command.NAME = ? AND command.DOMAIN = ? AND composite.LFT_INDEX = 1 ORDER BY command.DOMAIN ASC, command.NAME ASC, arg.ARG_INDEX ASC";

    /**
	 * ���װ��ĳ�������������������SQL��䡣
	 */
    private static final String ALL_SUB_COMMANDS = "SELECT command.ID, composite.COMMAND, composite.DOMAIN, command.COMMAND, command.EXECUTER, command.TYPE, command.REMARK, command.DEBUG_LEVEL," + " arg.NAME, arg.CONVERTER, arg.IN_OUT_TYPE, arg.REMARK, composite.LFT_INDEX, composite.RGT_INDEX" + " FROM tb_cos_composite_command composite" + " LEFT JOIN tb_cos_command command ON command.DOMAIN = composite.DOMAIN AND command.NAME = composite.COMMAND" + " LEFT JOIN tb_cos_command_arg arg ON arg.DOMAIN = command.DOMAIN AND arg.COMMAND = command.NAME" + " WHERE composite.COMPOSITE = ? AND composite.LFT_INDEX > ? AND composite.LFT_INDEX < ? ORDER BY composite.LFT_INDEX ASC, command.DOMAIN ASC, command.NAME ASC";

    /**
	 * ���캯��
	 * 
	 * @param factory
	 *            Cosmos�����ࡣ
	 * @param domain
	 *            ������
	 */
    public CosmosDbGlobalCommandLoader(CosmosFactory factory, CosmosDomain domain) {
        super(factory, domain);
    }

    @Override
    protected CosmosCompositeCommand deepLoadCommand(CosmosCompositeCommand command, Collection<CosmosCommand> loadedCommands) throws CosmosCommandLoaderException {
        Map<String, CosmosCommand> loadedCommandMap = new HashMap<String, CosmosCommand>();
        for (CosmosCommand cosmosCommand : loadedCommands) {
            loadedCommandMap.put(cosmosCommand.getDomain().getName() + "." + cosmosCommand.getName(), cosmosCommand);
        }
        Connection conn = null;
        try {
            conn = ((CosmosSqlDomain) this.domain).getConnection();
        } catch (SQLException e) {
            throw new CosmosCommandLoaderException(e);
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String currentCommand = null, id = null, name = null, domain = null, content = null, executer = null, type = null, remark = null, debugLevel = null;
        Long leftIndex = 0l, rightIndex = 0l;
        CosmosMetaCommand commandType = null;
        Collection<CosmosMetaArgument> metaArgs = null;
        try {
            Map<String, CosmosMetaCommand> commandTypes = config.getCommandTypes();
            String composite = command.getDomain().getName() + "." + command.getName();
            Stack<CosmosCommand> stack = new Stack<CosmosCommand>();
            stack.push(command);
            pstmt = conn.prepareStatement(ALL_SUB_COMMANDS);
            pstmt.setString(1, composite);
            pstmt.setLong(2, command.getLeftIndex());
            pstmt.setLong(3, command.getRightIndex());
            rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {
                id = rs.getString(1);
                if ((id != null && !id.equals(currentCommand)) || id == null) {
                    if (id != null && !id.equals(currentCommand) || (id == null && currentCommand != id)) {
                        if (currentCommand != null) {
                            commandType = config.getDefaultCommandType();
                            if (type != null && !"".equals(type.trim())) {
                                commandType = commandTypes.get(type);
                            }
                            CosmosDomain cosmosDomain = config.getDomains().get(domain);
                            Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                            CosmosCommand cosmosCommand = constructor.newInstance(name, cosmosDomain, content, executer, metaArgs, commandType, CosmosCommandSource.GLOBAL);
                            if (cosmosCommand == null) {
                                throw new CosmosCommandLoaderException("");
                            }
                            cosmosCommand.setRemark(remark);
                            if (debugLevel == null || "".equals(debugLevel.trim()) || DebugLevel.NO_DEBUG.toString().equals(debugLevel)) {
                                cosmosCommand.setDebugLevel(cosmosDomain.getDebugLevel());
                            } else {
                                cosmosCommand.setDebugLevel(DebugLevel.valueOf(debugLevel));
                            }
                            cosmosCommand.setLeftIndex(leftIndex);
                            cosmosCommand.setRightIndex(rightIndex);
                            if (stack.size() > 0) {
                                CosmosCompositeCommand parentCommand = null;
                                do {
                                    CosmosCommand priorCommand = stack.pop();
                                    if (priorCommand instanceof CosmosCompositeCommand) {
                                        parentCommand = (CosmosCompositeCommand) priorCommand;
                                    } else {
                                        parentCommand = null;
                                    }
                                } while (parentCommand == null || parentCommand.getRightIndex() < rightIndex);
                                parentCommand.add(cosmosCommand);
                                stack.push(parentCommand);
                            }
                            stack.push(cosmosCommand);
                        }
                        name = rs.getString(2);
                        domain = rs.getString(3);
                        content = rs.getString(4);
                        executer = rs.getString(5);
                        type = rs.getString(6);
                        remark = rs.getString(7);
                        debugLevel = rs.getString(8);
                        leftIndex = rs.getLong(13);
                        rightIndex = rs.getLong(14);
                        metaArgs = new ArrayList<CosmosMetaArgument>();
                        currentCommand = id;
                    }
                    if (id == null) {
                        name = rs.getString(2);
                        domain = rs.getString(3);
                        leftIndex = rs.getLong(13);
                        rightIndex = rs.getLong(14);
                        CosmosCommand cosmosCommand = loadedCommandMap.get(domain + "." + name);
                        if (cosmosCommand == null) {
                            throw new CosmosCommandLoaderException(domain + "." + name + " doesn't exist.");
                        }
                        cosmosCommand = cosmosCommand.duplicate();
                        cosmosCommand.setLeftIndex(leftIndex);
                        cosmosCommand.setRightIndex(rightIndex);
                        if (stack.size() > 0) {
                            CosmosCompositeCommand parentCommand = null;
                            do {
                                CosmosCommand priorCommand = stack.pop();
                                if (priorCommand instanceof CosmosCompositeCommand) {
                                    parentCommand = (CosmosCompositeCommand) priorCommand;
                                } else {
                                    parentCommand = null;
                                }
                            } while (parentCommand == null || parentCommand.getRightIndex() < rightIndex);
                            parentCommand.add(cosmosCommand);
                            stack.push(parentCommand);
                        }
                        stack.push(cosmosCommand);
                        currentCommand = id;
                        continue;
                    }
                }
                String argName = rs.getString(9);
                if (argName != null) {
                    String converter = rs.getString(10);
                    String inOutType = rs.getString(11);
                    String argRemark = rs.getString(12);
                    CosmosMetaArgument arg = new StandardCosmosMetaArgument(this.factory.getConverter(converter), argName, inOutType == null ? ArgumentInOutType.IN : ArgumentInOutType.valueOf(inOutType), argRemark);
                    metaArgs.add(arg);
                }
            }
            if (currentCommand != null) {
                commandType = config.getDefaultCommandType();
                if (type != null && !"".equals(type.trim())) {
                    commandType = commandTypes.get(type);
                }
                CosmosDomain cosmosDomain = config.getDomains().get(domain);
                Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                CosmosCommand cosmosCommand = constructor.newInstance(name, cosmosDomain, content, executer, metaArgs, commandType, CosmosCommandSource.GLOBAL);
                if (cosmosCommand == null) {
                    throw new CosmosCommandLoaderException("");
                }
                cosmosCommand.setRemark(remark);
                if (debugLevel == null || "".equals(debugLevel.trim()) || DebugLevel.NO_DEBUG.toString().equals(debugLevel)) {
                    cosmosCommand.setDebugLevel(cosmosDomain.getDebugLevel());
                } else {
                    cosmosCommand.setDebugLevel(DebugLevel.valueOf(debugLevel));
                }
                cosmosCommand.setLeftIndex(leftIndex);
                cosmosCommand.setRightIndex(rightIndex);
                if (stack.size() > 0) {
                    CosmosCompositeCommand parentCommand = null;
                    do {
                        CosmosCommand priorCommand = stack.pop();
                        if (priorCommand instanceof CosmosCompositeCommand) {
                            parentCommand = (CosmosCompositeCommand) priorCommand;
                        } else {
                            parentCommand = null;
                        }
                    } while (parentCommand == null || parentCommand.getRightIndex() < rightIndex);
                    parentCommand.add(cosmosCommand);
                }
            }
            stack.clear();
        } catch (Exception e) {
            throw new CosmosCommandLoaderException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new CosmosCommandLoaderException(e);
            }
        }
        return command;
    }

    @Override
    protected Collection<CosmosCommand> loadDomainRootCommands(CosmosDomain domain) throws CosmosCommandLoaderException {
        Connection conn = null;
        try {
            conn = ((CosmosSqlDomain) this.domain).getConnection();
        } catch (SQLException e) {
            throw new CosmosCommandLoaderException(e);
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String currentId = null, id = null, name = null, command = null, executer = null, type = null, remark = null, debugLevel = null;
        Long leftIndex = 0l, rightIndex = 0l;
        CosmosMetaCommand commandType = null;
        Collection<CosmosMetaArgument> metaArgs = null;
        Collection<CosmosCommand> commands = new ArrayList<CosmosCommand>();
        try {
            Map<String, CosmosMetaCommand> commandTypes = config.getCommandTypes();
            pstmt = conn.prepareStatement(DOMAIN_ROOT_COMMANDS);
            pstmt.setString(1, domain.getName());
            rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {
                id = rs.getString(1);
                if (!id.equals(currentId)) {
                    if (currentId != null) {
                        commandType = config.getDefaultCommandType();
                        if (type != null && !"".equals(type.trim())) {
                            commandType = commandTypes.get(type);
                        }
                        Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                        CosmosCommand cosmosCommand = constructor.newInstance(name, domain, command, executer, metaArgs, commandType, CosmosCommandSource.GLOBAL);
                        cosmosCommand.setRemark(remark);
                        if (debugLevel == null || "".equals(debugLevel.trim()) || DebugLevel.NO_DEBUG.toString().equals(debugLevel)) {
                            cosmosCommand.setDebugLevel(domain.getDebugLevel());
                        } else {
                            cosmosCommand.setDebugLevel(DebugLevel.valueOf(debugLevel));
                        }
                        cosmosCommand.setLeftIndex(leftIndex);
                        cosmosCommand.setRightIndex(rightIndex);
                        commands.add(cosmosCommand);
                    }
                    name = rs.getString(2);
                    command = rs.getString(4);
                    executer = rs.getString(5);
                    type = rs.getString(6);
                    remark = rs.getString(7);
                    debugLevel = rs.getString(8);
                    leftIndex = rs.getLong(13);
                    rightIndex = rs.getLong(14);
                    metaArgs = new ArrayList<CosmosMetaArgument>();
                    currentId = id;
                }
                String argName = rs.getString(9);
                if (argName != null) {
                    String converter = rs.getString(10);
                    String inOutType = rs.getString(11);
                    String argRemark = rs.getString(12);
                    CosmosMetaArgument arg = new StandardCosmosMetaArgument(this.factory.getConverter(converter), argName, inOutType == null ? ArgumentInOutType.IN : ArgumentInOutType.valueOf(inOutType), argRemark);
                    metaArgs.add(arg);
                }
            }
            if (id != null) {
                commandType = config.getDefaultCommandType();
                if (type != null && !"".equals(type.trim())) {
                    commandType = commandTypes.get(type);
                }
                Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                CosmosCommand cosmosCommand = constructor.newInstance(name, domain, command, executer, metaArgs, commandType, CosmosCommandSource.GLOBAL);
                cosmosCommand.setRemark(remark);
                if (debugLevel == null || "".equals(debugLevel.trim()) || DebugLevel.NO_DEBUG.toString().equals(debugLevel)) {
                    cosmosCommand.setDebugLevel(domain.getDebugLevel());
                } else {
                    cosmosCommand.setDebugLevel(DebugLevel.valueOf(debugLevel));
                }
                cosmosCommand.setLeftIndex(leftIndex);
                cosmosCommand.setRightIndex(rightIndex);
                commands.add(cosmosCommand);
            }
        } catch (Exception e) {
            throw new CosmosCommandLoaderException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new CosmosCommandLoaderException(e);
            }
        }
        return commands;
    }

    @Override
    protected Collection<CosmosCommand> loadRootCommands() throws CosmosCommandLoaderException {
        Connection conn = null;
        try {
            conn = ((CosmosSqlDomain) this.domain).getConnection();
        } catch (SQLException e) {
            throw new CosmosCommandLoaderException(e);
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String currentId = null, id = null, name = null, domain = null, command = null, executer = null, type = null, remark = null, debugLevel = null;
        Long leftIndex = 0l, rightIndex = 0l;
        CosmosMetaCommand commandType = null;
        Collection<CosmosMetaArgument> metaArgs = null;
        Collection<CosmosCommand> commands = new ArrayList<CosmosCommand>();
        try {
            Map<String, CosmosMetaCommand> commandTypes = config.getCommandTypes();
            Map<String, CosmosDomain> domains = this.config.getDomains();
            pstmt = conn.prepareStatement(ALL_ROOT_COMMANDS);
            rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {
                id = rs.getString(1);
                if (!id.equals(currentId)) {
                    if (currentId != null) {
                        commandType = config.getDefaultCommandType();
                        if (type != null && !"".equals(type.trim())) {
                            commandType = commandTypes.get(type);
                        }
                        CosmosDomain cosmosDomain = domains.get(domain);
                        Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                        CosmosCommand cosmosCommand = constructor.newInstance(name, cosmosDomain, command, executer, metaArgs, commandType, CosmosCommandSource.GLOBAL);
                        cosmosCommand.setRemark(remark);
                        if (debugLevel == null || "".equals(debugLevel.trim()) || DebugLevel.NO_DEBUG.toString().equals(debugLevel)) {
                            cosmosCommand.setDebugLevel(cosmosDomain.getDebugLevel());
                        } else {
                            cosmosCommand.setDebugLevel(DebugLevel.valueOf(debugLevel));
                        }
                        cosmosCommand.setLeftIndex(leftIndex);
                        cosmosCommand.setRightIndex(rightIndex);
                        commands.add(cosmosCommand);
                    }
                    name = rs.getString(2);
                    domain = rs.getString(3);
                    command = rs.getString(4);
                    executer = rs.getString(5);
                    type = rs.getString(6);
                    remark = rs.getString(7);
                    debugLevel = rs.getString(8);
                    leftIndex = rs.getLong(13);
                    rightIndex = rs.getLong(14);
                    metaArgs = new ArrayList<CosmosMetaArgument>();
                    currentId = id;
                }
                String argName = rs.getString(9);
                if (argName != null) {
                    String converter = rs.getString(10);
                    String inOutType = rs.getString(11);
                    String argRemark = rs.getString(12);
                    CosmosMetaArgument arg = new StandardCosmosMetaArgument(this.factory.getConverter(converter), argName, inOutType == null ? ArgumentInOutType.IN : ArgumentInOutType.valueOf(inOutType), argRemark);
                    metaArgs.add(arg);
                }
            }
            if (id != null) {
                commandType = config.getDefaultCommandType();
                if (type != null && !"".equals(type.trim())) {
                    commandType = commandTypes.get(type);
                }
                CosmosDomain cosmosDomain = domains.get(domain);
                Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                CosmosCommand cosmosCommand = constructor.newInstance(name, cosmosDomain, command, executer, metaArgs, commandType, CosmosCommandSource.GLOBAL);
                cosmosCommand.setRemark(remark);
                if (debugLevel == null || "".equals(debugLevel.trim()) || DebugLevel.NO_DEBUG.toString().equals(debugLevel)) {
                    cosmosCommand.setDebugLevel(cosmosDomain.getDebugLevel());
                } else {
                    cosmosCommand.setDebugLevel(DebugLevel.valueOf(debugLevel));
                }
                cosmosCommand.setLeftIndex(leftIndex);
                cosmosCommand.setRightIndex(rightIndex);
                commands.add(cosmosCommand);
            }
        } catch (Exception e) {
            throw new CosmosCommandLoaderException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new CosmosCommandLoaderException(e);
            }
        }
        return commands;
    }

    @Override
    protected CosmosCommand loadSingleCommand(CosmosDomain domain, String name) throws CosmosCommandLoaderException {
        Connection conn = null;
        try {
            conn = ((CosmosSqlDomain) this.domain).getConnection();
        } catch (SQLException e) {
            throw new CosmosCommandLoaderException(e);
        }
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String id = null, command = null, executer = null, type = null, remark = null, debugLevel = null;
        Long leftIndex = 0l, rightIndex = 0l;
        CosmosMetaCommand commandType = null;
        Collection<CosmosMetaArgument> metaArgs = null;
        CosmosCommand cosmosCommand = null;
        try {
            Map<String, CosmosMetaCommand> commandTypes = config.getCommandTypes();
            pstmt = conn.prepareStatement(SINGLE_COMMAND);
            pstmt.setString(1, name);
            pstmt.setString(2, domain.getName());
            rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {
                if (id == null) {
                    id = rs.getString(1);
                    name = rs.getString(2);
                    command = rs.getString(4);
                    executer = rs.getString(5);
                    type = rs.getString(6);
                    remark = rs.getString(7);
                    debugLevel = rs.getString(8);
                    leftIndex = rs.getLong(13);
                    rightIndex = rs.getLong(14);
                    metaArgs = new ArrayList<CosmosMetaArgument>();
                    commandType = config.getDefaultCommandType();
                    if (type != null && !"".equals(type.trim())) {
                        commandType = commandTypes.get(type);
                    }
                }
                String argName = rs.getString(9);
                if (argName != null) {
                    String converter = rs.getString(10);
                    String inOutType = rs.getString(11);
                    String argRemark = rs.getString(12);
                    CosmosMetaArgument arg = new StandardCosmosMetaArgument(this.factory.getConverter(converter), argName, inOutType == null ? ArgumentInOutType.IN : ArgumentInOutType.valueOf(inOutType), argRemark);
                    metaArgs.add(arg);
                }
            }
            if (id != null) {
                Constructor<CosmosCommand> constructor = commandType.getClazz().getConstructor(String.class, CosmosDomain.class, String.class, String.class, Collection.class, CosmosMetaCommand.class, CosmosCommandSource.class);
                cosmosCommand = constructor.newInstance(name, domain, command, executer, metaArgs, commandType, CosmosCommandSource.GLOBAL);
                cosmosCommand.setRemark(remark);
                if (debugLevel == null || "".equals(debugLevel.trim()) || DebugLevel.NO_DEBUG.toString().equals(debugLevel)) {
                    cosmosCommand.setDebugLevel(domain.getDebugLevel());
                } else {
                    cosmosCommand.setDebugLevel(DebugLevel.valueOf(debugLevel));
                }
                cosmosCommand.setLeftIndex(leftIndex);
                cosmosCommand.setRightIndex(rightIndex);
            }
        } catch (Exception e) {
            throw new CosmosCommandLoaderException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new CosmosCommandLoaderException(e);
            }
        }
        return cosmosCommand;
    }
}
