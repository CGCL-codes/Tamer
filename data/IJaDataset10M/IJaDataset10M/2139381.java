package com.controltier.ctl.utils;

import com.controltier.ctl.CtlException;
import com.controltier.ctl.common.ICommand;
import com.controltier.ctl.common.IContext;
import com.controltier.ctl.common.context.ICommandContext;
import com.controltier.ctl.common.context.IExecutionContext;
import com.controltier.ctl.common.context.IObjectContext;
import com.controltier.ctl.types.controller.Arg;
import java.util.Iterator;

/**
 * Utility class for building command line args for the ctl shell tool.
 * Used for configuring CLOPTS appropriately depending on context;
 * eg, if object context uses -p,-t,-o,-c, if handler context, -p,-m,-c
 */
public class CtlArgUtil {

    /**
     * Build argument string for the ctl shell tool.
     *
     * @param object  Object object
     * @param command Command to execute
     * @return command line arg string
     * @throws CtlException thrown if unrecognized context
     */
    public static String buildAsString(final IContext object, final ICommand command) {
        final StringBuffer sb = new StringBuffer();
        if (object.isObjectContext() && command.isCommandContext() && command.isModuleContext()) {
            appendDepotPart(object.getDepot(), sb);
            appendTypePart(object.getEntityType(), sb);
            appendObjectPart(object.getEntityName(), sb);
            appendModulePart(command.getModule(), sb);
            appendCommandPart(command.getName(), sb);
        } else if (object.isObjectContext() && command.isCommandContext()) {
            appendDepotPart(object.getDepot(), sb);
            appendTypePart(object.getEntityType(), sb);
            appendObjectPart(object.getEntityName(), sb);
            appendCommandPart(command.getName(), sb);
        } else if (command.isCommandContext()) {
            appendDepotPart(command.getDepot(), sb);
            appendModulePart(command.getModule(), sb);
            appendCommandPart(command.getName(), sb);
        } else {
            throw new CtlException("Unrecognized execution object: " + object.toString() + ", " + command.toString());
        }
        if (null != System.getProperty("ctl.dispatchArgs")) {
            sb.append(System.getProperty("ctl.dispatchArgs"));
        }
        return sb.toString();
    }

    /**
     * Build argument string for the ctl shell tool.
     *
     * @param action The ExecutionContext containing data for command line
     * @return command line arg string
     */
    public static String buildAsString(final IExecutionContext action) {
        final StringBuffer sb = new StringBuffer(buildAsString(action.getObjectContext(), action.getCommandContext()));
        if (action.getArg() != null && action.getArg().getLine() != null && !"".equals(action.getArg().getLine())) {
            appendArgPart(action.getArg(), sb).toString();
        }
        return sb.toString();
    }

    /**
     * Build argument string for the ctl shell tool. If the system property,
     * "ctl.dispatchArgs" is set, then its value is also appended to the command line argument string.
     *
     * @param objectContext
     * @param commandContext
     * @return command line arg string
     */
    public static String buildAsString(final IObjectContext objectContext, final ICommandContext commandContext) {
        final StringBuffer sb = new StringBuffer();
        if (objectContext.isObjectContext() && commandContext.isCommandContext() && commandContext.isModuleContext()) {
            appendDepotPart(objectContext.getDepot(), sb);
            appendTypePart(objectContext.getEntityType(), sb);
            appendObjectPart(objectContext.getEntityName(), sb);
            appendModulePart(commandContext.getModule(), sb);
            appendCommandPart(commandContext.getCommand(), sb);
        } else if (objectContext.isObjectContext()) {
            appendDepotPart(objectContext.getDepot(), sb);
            appendTypePart(objectContext.getEntityType(), sb);
            appendObjectPart(objectContext.getEntityName(), sb);
            appendCommandPart(commandContext.getCommand(), sb);
        } else if (commandContext.isCommandContext()) {
            appendDepotPart(commandContext.getDepot(), sb);
            appendModulePart(commandContext.getModule(), sb);
            appendCommandPart(commandContext.getCommand(), sb);
        } else {
            throw new CtlException("Unrecognized execution object: " + objectContext.toString() + ", " + commandContext.toString());
        }
        if (null != System.getProperty("ctl.dispatchArgs")) {
            sb.append(" ").append(System.getProperty("ctl.dispatchArgs"));
        }
        return sb.toString();
    }

    private static StringBuffer appendDepotPart(final String project, final StringBuffer sb) {
        return appendPart("-p", project, sb);
    }

    private static StringBuffer appendTypePart(final String type, final StringBuffer sb) {
        return appendPart("-t", type, sb);
    }

    private static StringBuffer appendObjectPart(final String obj, final StringBuffer sb) {
        return appendPart("-o", obj, sb);
    }

    private static StringBuffer appendModulePart(final String module, final StringBuffer sb) {
        return appendPart("-m", module, sb);
    }

    private static StringBuffer appendCommandPart(final String command, final StringBuffer sb) {
        return appendPart("-c", command, sb);
    }

    private static StringBuffer appendProcessPart(final String processInstance, final StringBuffer sb) {
        return appendPart("-P", processInstance, sb);
    }

    private static StringBuffer appendReportPart(final String reportCategory, final StringBuffer sb) {
        return appendPart("-R", reportCategory, sb);
    }

    private static StringBuffer appendArgPart(final Arg arg, final StringBuffer sb) {
        return appendPart("--", arg.getLine(), sb);
    }

    /**
     * Append an arg and param to the command line option list
     *
     * @param arg   Argument flag. eg, -p
     * @param param Argument paramaeter. eg., project
     * @param sb    StringBuffer where command list is being created
     * @return retuns sb that was passed in.
     */
    private static StringBuffer appendPart(final String arg, final String param, final StringBuffer sb) {
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append(arg);
        sb.append(" ");
        sb.append(param);
        return sb;
    }
}
