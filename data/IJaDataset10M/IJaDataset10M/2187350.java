package com.controltier.ctl.tasks.controller;

import com.controltier.ctl.CtlException;
import com.controltier.ctl.Constants;
import com.controltier.ctl.utils.StringArrayUtil;
import com.controltier.ctl.common.*;
import com.controltier.ctl.types.Command;
import com.controltier.ctl.types.Context;
import com.controltier.ctl.types.Opt;
import java.util.*;

/**
 * Lists framework resources
 * <p/>
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 17, 2005
 * Time: 5:02:27 PM
 */
public class ListDispatchAction implements DispatchAction {

    private final IObject context;

    private final ICommand command;

    private final IDepotMgr depotResourceMgr;

    private final String lineSep = System.getProperty("line.separator");

    private final String indent = "  ";

    public ListDispatchAction(final IObject context, final IDepotMgr depotResourceMgr) {
        this.context = context;
        this.command = new Command();
        this.depotResourceMgr = depotResourceMgr;
    }

    public ListDispatchAction(final ICommand command, final IDepotMgr depotResourceMgr) {
        this.context = new Context();
        this.command = command;
        this.depotResourceMgr = depotResourceMgr;
    }

    /**
     * Given the context (eg, object, type, depot, empty) lists the pertinent resource.
     *
     * @return ActionResult with listed resource
     */
    public ActionResult perform() {
        final BaseActionResult result = BaseActionResult.create("output", "error", true);
        result.setSuccessful(true);
        Collection c = new ArrayList();
        try {
            if (!context.isDepotContext() && !command.isDepotContext() && !command.isModuleContext()) {
                c = listEmptyContext();
            } else if (context.isDepotContext() && !command.isCommandContext()) {
                if (context.isObjectContext()) {
                    c = listObjectContext();
                } else if (context.isTypeContext()) {
                    c = listTypeContext();
                } else if (context.isDepotContext()) {
                    if (depotResourceMgr.existsDepot(context.getDepot()) && depotResourceMgr.getDepot(context.getDepot()).listChildren().size() > 0) {
                        c = listDepotContext();
                    } else {
                        c = listModules();
                    }
                }
            } else if (command.isModuleContext() && !context.isTypeContext()) {
                if (command.isModuleContext()) {
                    c = listModuleContext();
                }
            }
        } catch (CtlException e) {
            result.setBuildException(new ListException(e));
        }
        result.setOutputString(asString(c, System.getProperty("line.separator")));
        return result;
    }

    public String asString(final Collection c, final String sep) {
        final StringBuffer sb = new StringBuffer();
        for (Iterator iter = c.iterator(); iter.hasNext(); ) {
            final Object obj = iter.next();
            if (obj instanceof String) {
                sb.append(obj);
            } else if (obj instanceof FrameworkResource) {
                final FrameworkResource r = (FrameworkResource) obj;
                sb.append(r.getName());
            } else {
                throw new IllegalArgumentException("unrecognized collection element type: " + obj.getClass().getName());
            }
            sb.append(sep);
        }
        return sb.toString();
    }

    /**
     * Generates a human readable summary of the framework resources of the instance base dependending
     * on the command line context.
     * @return an {BaseActionResult} value with its output string property containing
     * the summary
     */
    public BaseActionResult summary() {
        final BaseActionResult result = BaseActionResult.create("output", "error", true);
        result.setSuccessful(true);
        StringBuffer sb = new StringBuffer();
        try {
            if (!context.isDepotContext() && !command.isDepotContext() && !command.isModuleContext()) {
                new AllSummary().appendSummary(sb, "");
            } else if (context.isDepotContext() && !command.isCommandContext()) {
                if (context.isObjectContext()) {
                    new ModuleSummary(fetchCmdModule(context), true).appendSummary(sb, "");
                } else if (context.isTypeContext()) {
                    new TypeSummary(fetchDepotType(context)).appendSummary(sb, "");
                } else if (context.isDepotContext()) {
                    if (depotResourceMgr.existsDepot(context.getDepot())) {
                        final Depot d = depotResourceMgr.getDepot(context.getDepot());
                        new DepotSummary(d).appendShortSummary(sb, "");
                    }
                }
            } else if (command.isModuleContext() && !context.isTypeContext()) {
                if (command.isModuleContext()) {
                    new ModuleSummary(fetchCmdModule(command), true).appendSummary(sb, "");
                }
            }
        } catch (CtlException e) {
            result.setBuildException(new ListException(e));
        }
        result.setOutputString(sb.toString());
        return result;
    }

    /**
     * Defines an interface to listing summaries
     */
    abstract class ListingSummary {

        final boolean deepListing;

        ListingSummary(boolean deepListing) {
            this.deepListing = deepListing;
        }

        ListingSummary() {
            this(false);
        }

        /**
         * Adds summary info
         * @param sb StringBuffer to append summary data to
         * @param indent whitespace indent
         */
        abstract void appendSummary(StringBuffer sb, String indent);
    }

    /**
     * Lists all the depots and summary info
     */
    class AllSummary extends ListingSummary {

        AllSummary() {
            super();
        }

        void appendSummary(StringBuffer sb, String ind) {
            for (Iterator i = depotResourceMgr.listDepots().iterator(); i.hasNext(); ) {
                Depot d = (Depot) i.next();
                new DepotSummary(d).appendShortSummary(sb, ind + indent);
            }
        }
    }

    /**
     * Summarizes depot content
     */
    class DepotSummary extends ListingSummary {

        final Depot d;

        DepotSummary(final Depot d) {
            super();
            this.d = d;
        }

        void appendSummary(StringBuffer sb, String ind) {
            for (Iterator i = listRunnableModules(d).iterator(); i.hasNext(); ) {
                String modName = (String) i.next();
                CmdModule mod = d.getModuleLookup().getCmdModule(modName);
                new ModuleSummary(mod).appendSummary(sb, ind);
                final boolean hasObjects = d.existsChild(mod.getName()) && (d.getDepotType(mod.getName()).listChildren().size() > 0);
                if (d.existsChild(mod.getName()) && hasObjects) {
                    new TypeSummary(d.getDepotType(mod.getName())).appendSummary(sb, ind);
                }
                sb.append(lineSep);
            }
        }

        void appendShortSummary(StringBuffer sb, String ind) {
            for (Iterator i = listRunnableModules(d).iterator(); i.hasNext(); ) {
                String modName = (String) i.next();
                CmdModule mod = d.getModuleLookup().getCmdModule(modName);
                new ModuleSummary(mod).appendShortSummary(sb, ind);
                final boolean hasObjects = d.existsChild(mod.getName()) && (d.getDepotType(mod.getName()).listChildren().size() > 0);
                if (d.existsChild(mod.getName()) && hasObjects) {
                    new TypeSummary(d.getDepotType(mod.getName())).appendShortSummary(sb, ind);
                }
                sb.append(lineSep);
            }
        }
    }

    /**
     * Summarizes module info including commands and their options
     */
    class ModuleSummary extends ListingSummary {

        final CmdModule mod;

        ModuleSummary(final CmdModule mod) {
            this(mod, false);
        }

        ModuleSummary(final CmdModule mod, boolean deepListing) {
            super(deepListing);
            this.mod = mod;
        }

        void appendSummary(StringBuffer sb, String ind) {
            sb.append(ind).append(mod.getName()).append(": ").append(mod.getDescription()).append(lineSep);
            sb.append(ind).append("[commands]").append(lineSep);
            for (Iterator iter = sort(mod.getAllCommandNames()).iterator(); iter.hasNext(); ) {
                final String cmdName = (String) iter.next();
                if (mod.existsCmdHandler(cmdName)) {
                    CmdHandler cmd = mod.getCmdHandler(cmdName);
                    sb.append(ind).append(indent).append(cmd.getName()).append(": ").append(cmd.getDescription()).append(lineSep);
                    if (deepListing && cmd.getOpts().size() > 0) {
                        new OptSummary(cmd).appendSummary(sb, ind + indent + indent);
                    }
                }
            }
        }

        void appendShortSummary(StringBuffer sb, String ind) {
            sb.append(ind).append(mod.getName()).append(": ").append(mod.getDescription()).append(lineSep);
            sb.append(ind).append("commands: ");
            StringBuffer sb2 = new StringBuffer();
            for (Iterator iter = sort(mod.getAllCommandNames()).iterator(); iter.hasNext(); ) {
                final String cmdName = (String) iter.next();
                if (mod.existsCmdHandler(cmdName)) {
                    if (sb2.length() > 0) {
                        sb2.append(", ");
                    }
                    sb2.append(cmdName);
                }
            }
            if (sb2.length() > 0) {
                sb.append("[").append(sb2).append("]");
            } else {
                sb.append("(none)");
            }
            sb.append(lineSep);
        }
    }

    /**
     * Summarizes type info including their objects
     */
    class TypeSummary extends ListingSummary {

        final DepotType type;

        TypeSummary(final DepotType type) {
            super();
            this.type = type;
        }

        void appendSummary(StringBuffer sb, String ind) {
            final Collection objs = type.listChildren();
            sb.append(ind).append("[objects]").append(lineSep);
            for (Iterator k = objs.iterator(); k.hasNext(); ) {
                DepotObject obj = (DepotObject) k.next();
                sb.append(indent).append(obj.getName()).append(": ").append(obj.getDescription()).append(lineSep);
            }
        }

        void appendShortSummary(StringBuffer sb, String ind) {
            final Collection objs = type.listChildren();
            sb.append(ind).append("objects: ");
            StringBuffer sb2 = new StringBuffer();
            for (Iterator k = objs.iterator(); k.hasNext(); ) {
                DepotObject obj = (DepotObject) k.next();
                if (sb2.length() > 0) {
                    sb2.append(", ");
                }
                sb2.append(obj.getName());
            }
            if (sb2.length() > 0) {
                sb.append("[").append(sb2).append("]");
            } else {
                sb.append("(none)");
            }
            sb.append(lineSep);
        }
    }

    /**
     * Summarize command options
     */
    class OptSummary extends ListingSummary {

        final CmdHandler cmd;

        OptSummary(final CmdHandler cmd) {
            super();
            this.cmd = cmd;
        }

        void appendSummary(StringBuffer sb, String ind) {
            sb.append(ind).append("[options]").append(lineSep);
            for (Iterator j = cmd.getOpts().iterator(); j.hasNext(); ) {
                Opt opt = (Opt) j.next();
                sb.append(ind);
                sb.append(opt.toUsageString());
                sb.append(": ").append(opt.getDescription()).append(lineSep);
            }
        }
    }

    /**
     * Lists all the commands for the object
     *
     * @return returns a Collection of {@link String} objects representing the module's commands
     */
    protected Collection listObjectContext() {
        final CmdModule mod = fetchCmdModule(context);
        final List l = new ArrayList();
        for (Iterator iter = mod.getAllCommandNames().iterator(); iter.hasNext(); ) {
            CmdHandler cmd = mod.getCmdHandler((String) iter.next());
            l.add(cmd.getName() + " - " + cmd.getDescription());
        }
        return sort(l);
    }

    /**
     * Lists all the objects of specified type
     *
     * @return returns a Collection of {@link DepotObject} instances
     */
    protected Collection listTypeContext() {
        final IFrameworkResourceParent depotType = fetchDepotType(context);
        List l = new ArrayList();
        for (Iterator iter = depotType.listChildren().iterator(); iter.hasNext(); ) {
            DepotObject obj = (DepotObject) iter.next();
            l.add(obj.getName());
        }
        return sort(l);
    }

    private DepotType fetchDepotType(IObject context) {
        final Depot depot = depotResourceMgr.getDepot(context.getDepot());
        if (!depotResourceMgr.existsDepotType(context.getDepot(), context.getEntityType())) {
            throw new CtlException("type could not be found: " + context.getEntityType());
        }
        return (DepotType) depot.getChild(context.getEntityType());
    }

    /**
     * list all the types for this depot
     *
     * @return returns a Collection of {@link DepotType} instances
     */
    protected Collection listDepotContext() {
        final Depot depot = depotResourceMgr.getDepot(context.getDepot());
        final List l = listRunnableModules(depot);
        return sort(l);
    }

    private List listRunnableModules(Depot depot) {
        final List l = new ArrayList();
        for (Iterator iter = depot.getModuleLookup().listCmdModules().iterator(); iter.hasNext(); ) {
            final CmdModule mod = (CmdModule) iter.next();
            if ((depot.existsChild(mod.getName()) && depot.getDepotType(mod.getName()).listChildren().size() > 0) || mod.hasStaticCommands()) {
                l.add(mod.getName());
            }
        }
        return l;
    }

    /**
     * list all the modules
     *
     * @return
     */
    protected Collection listEmptyModuleContext() {
        return listResourceNames(depotResourceMgr);
    }

    /**
     * lists all the depots
     *
     * @return Collection of {@link Depot} objects
     */
    protected Collection listEmptyContext() {
        final Collection c = new ArrayList(depotResourceMgr.listChildNames());
        return sort(c);
    }

    private Collection listResourceNames(final IFrameworkResourceParent parent) {
        final Collection c = new ArrayList(parent.listChildNames());
        return sort(c);
    }

    public Collection listModules() {
        final IModuleLookup lookup;
        if (context.isDepotContext()) {
            final Depot depot = depotResourceMgr.getDepot(context.getDepot());
            lookup = depot.getModuleLookup();
        } else {
            lookup = depotResourceMgr.getModuleLookup();
        }
        final List l = new ArrayList();
        for (Iterator iter = lookup.listCmdModules().iterator(); iter.hasNext(); ) {
            final CmdModule mod = (CmdModule) iter.next();
            l.add(mod.getName() + " - " + ((mod.getDescription() != null) ? mod.getDescription() : ""));
        }
        return sort(l);
    }

    /**
     * Lists all the commands for the object
     *
     * @return Collection of {@link String} objects representing the names of commands in the module
     */
    public Collection listModuleContext() {
        final CmdModule mod = fetchCmdModule(command);
        final List l = new ArrayList();
        for (Iterator iter = mod.getAllCommandNames().iterator(); iter.hasNext(); ) {
            CmdHandler cmd = mod.getCmdHandler((String) iter.next());
            l.add(cmd.getName() + " - " + ((cmd.getDescription() != null) ? cmd.getDescription() : ""));
        }
        return sort(l);
    }

    /**
     * @param command Command context info describing module name
     * @return Matching {CmdModule} or throws {CtlException} if none found.
     */
    private CmdModule fetchCmdModule(ICommand command) {
        final IModuleLookup lookup;
        if (command.isDepotContext()) {
            final Depot depot = depotResourceMgr.getDepot(command.getDepot());
            lookup = depot.getModuleLookup();
        } else {
            lookup = depotResourceMgr.getModuleLookup();
        }
        if (!lookup.existsCmdModule(command.getModule())) {
            throw new CtlException("Module not found: " + command.getModule());
        }
        return lookup.getCmdModule(command.getModule());
    }

    /**
     * @param context {IObject} info describing an object
     * @return Matching {CmdModule} or throws {CtlException} if none found.
     */
    private CmdModule fetchCmdModule(IObject context) {
        if (!depotResourceMgr.existsDepot(context.getDepot())) {
            throw new CtlException("depot not found: " + context.getDepot());
        }
        if (!DepotObject.exists(context, depotResourceMgr)) {
            throw new CtlException("object not found: " + context.getEntityName());
        }
        final DepotObject obj = (DepotObject) depotResourceMgr.getDepotType(context).getChild(context.getEntityName());
        return obj.getCmdModule();
    }

    private Collection sort(final Collection strings) {
        if (null == strings) throw new IllegalArgumentException("null collection");
        final List l = new ArrayList(strings);
        Collections.sort(l);
        return l;
    }

    /**
     * Exception thrown for Listing errors
     */
    public class ListException extends CtlException {

        public ListException(final String msg) {
            super(msg);
        }

        public ListException(final String msg, final Exception e) {
            super(msg, e);
        }

        public ListException(final Exception e) {
            super(e);
        }
    }
}
