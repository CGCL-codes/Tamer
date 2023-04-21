package ch.elca.leaf.buildsystem.emma;

import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import ch.elca.leaf.buildsystem.model.Attribute;
import ch.elca.leaf.buildsystem.model.ConfigurationEvent;
import ch.elca.leaf.buildsystem.model.ConfigurationListener;
import ch.elca.leaf.buildsystem.model.ExecutionUnit;
import ch.elca.leaf.buildsystem.model.Module;
import ch.elca.leaf.buildsystem.model.Plugin;
import ch.elca.leaf.buildsystem.model.ProjectRepository;
import ch.elca.leaf.buildsystem.propgen.RuntimePropertyGenerator;
import ch.elca.leaf.buildsystem.taskdefs.HookTask;

/**
 * This task eases the usage of the emma plugin. It automates the insertion of
 * needed attributes and hooks on a module to cover with emma.
 *
 * A plugin switch property can be used to control emma support. If the switch
 * is not set, support is permanent.
 *
 * The EMMA mode can be set globally or per module or per execution unit. If
 * set at a higher level, sub-level uses this value, except if overriden.
 *
 * <script type="text/javascript">printFileStatus
 *   ("$Source$",
 *   "$Revision: 227 $", "$Date: 2005-05-24 04:56:04 -0400 (Tue, 24 May 2005) $", "$Author: yma $"
 * );</script>
 *
 * @author Yves Martin (YMA)
 * @version $Revision: 227 $
 */
public class EmmaConfigurationListener extends Task implements ConfigurationListener {

    /** EMMA plugin name. */
    public static final String PLUGIN_NAME = "emma";

    /** EMMA mode attribute name. */
    public static final String MODE_ATTRIBUTE = "emma.mode";

    /** EMMA switch attribute name. */
    public static final String SWITCH_ATTRIBUTE = "emma.switch";

    /** Online mode value. */
    public static final String MODE_ONLINE = "online";

    /** Offline mode value. */
    public static final String MODE_OFFLINE = "offline";

    /** Compile hook name. */
    public static final String COMPILE_HOOK = "post.javac.[module]";

    /** Runtime hook name. */
    public static final String RUNTIME_HOOK = "runtime.[module].[eu]";

    /** Runtime hook prefix for modes. */
    public static final String HOOK_PREFIX = "runtime.hook.emma.";

    /** Instrumentation hook target name. */
    public static final String INSTRUMENTATION_HOOK = "emma.instrumentation.module";

    /** Property name that is used as switch to enable EMMA support. */
    private String m_switch = null;

    /** Configured global mode or <code>null</code>. */
    private String m_globalMode = null;

    /** Registers itself as configuration listener. */
    public void execute() {
        ProjectRepository pr = ProjectRepository.getInstance();
        Plugin emmaPlugin = pr.getPlugin(PLUGIN_NAME);
        Iterator attIt = emmaPlugin.getAttributesByName(SWITCH_ATTRIBUTE);
        if (attIt.hasNext()) {
            Attribute anAttribute = (Attribute) attIt.next();
            m_switch = anAttribute.getValue();
        }
        attIt = emmaPlugin.getAttributesByName(MODE_ATTRIBUTE);
        if (attIt.hasNext()) {
            Attribute anAttribute = (Attribute) attIt.next();
            m_globalMode = anAttribute.getValue();
            checkMode(m_globalMode);
        }
        if (MODE_OFFLINE.equals(m_globalMode)) {
            HookTask result = createCompileHook();
            result.execute();
        }
        pr.addConfigurationListener(this);
    }

    /**
     * In case of the configuration of a module, look for the runnable
     * attribute and process the module if needed.
     *
     * @param event a <code>ConfigurationEvent</code> value
     */
    public void componentConfiguring(ConfigurationEvent event) {
        if (!(event.getSource() instanceof Module)) {
            return;
        }
        Module source = (Module) event.getSource();
        boolean instrumentationNeeded = false;
        String moduleMode = m_globalMode;
        Iterator att = source.getAttributesByName(MODE_ATTRIBUTE);
        if (att.hasNext()) {
            Attribute anAttribute = (Attribute) att.next();
            moduleMode = anAttribute.getValue();
            checkMode(moduleMode);
            if (MODE_OFFLINE.equals(moduleMode)) {
                instrumentationNeeded = true;
            }
        }
        final String RUNNABLE_ATT = RuntimePropertyGenerator.RUNTIME_RUNNABLE;
        att = source.getAttributesByName(RUNNABLE_ATT);
        if (att.hasNext()) {
            Attribute anAttribute = (Attribute) att.next();
            if (anAttribute.isTrue()) {
                if (moduleMode != null) {
                    source.addHook(createRuntimeHook(moduleMode));
                }
            }
        }
        Iterator euIt = source.getExecutionUnitList().iterator();
        while (euIt.hasNext()) {
            ExecutionUnit eu = (ExecutionUnit) euIt.next();
            Iterator attEu = eu.getAttributesByName(RUNNABLE_ATT);
            if (!attEu.hasNext()) {
                continue;
            } else {
                Attribute anAttribute = (Attribute) attEu.next();
                if (!anAttribute.isTrue()) {
                    continue;
                }
            }
            String euMode = moduleMode;
            attEu = eu.getAttributesByName(MODE_ATTRIBUTE);
            if (attEu.hasNext()) {
                Attribute anAttribute = (Attribute) att.next();
                euMode = anAttribute.getValue();
                checkMode(euMode);
            }
            if (euMode != null) {
                eu.addHook(createRuntimeHook(euMode));
                if (MODE_OFFLINE.equals(euMode)) {
                    instrumentationNeeded = true;
                }
            }
        }
        if (instrumentationNeeded && !MODE_OFFLINE.equals(m_globalMode)) {
            source.addHook(createCompileHook());
        }
    }

    /**
     * Check if the given mode is valid (offline or online).
     *
     * @param mode a <code>String</code> value
     */
    private void checkMode(String mode) {
        if (!MODE_ONLINE.equals(mode) && !MODE_OFFLINE.equals(mode)) {
            throw new BuildException("Invalid EMMA mode '" + mode + "'");
        }
    }

    /**
     * Create a hook declaration to add emma support.
     *
     * @param mode the mode the hook will enable
     * @return a <code>HookTask</code> instance
     */
    private HookTask createRuntimeHook(String mode) {
        HookTask result = new HookTask();
        result.setProject(this.getProject());
        result.setAction(HookTask.APPEND_LAST);
        result.setName(RUNTIME_HOOK);
        result.setTarget(HOOK_PREFIX + mode);
        if (m_switch != null) {
            result.setIf(m_switch);
        }
        return result;
    }

    /**
     * Create a instrumentation hook declaration for a module.
     *
     * @return a <code>HookTask</code> instance
     */
    private HookTask createCompileHook() {
        HookTask result = new HookTask();
        result.setProject(this.getProject());
        result.setAction(HookTask.APPEND_LAST);
        result.setName(COMPILE_HOOK);
        result.setTarget(INSTRUMENTATION_HOOK);
        if (m_switch != null) {
            result.setIf(m_switch);
        }
        return result;
    }

    /**
     * Invoked when a project component configuration is over.
     *
     * @param event a <code>ConfigurationEvent</code> value
     */
    public void componentConfigured(ConfigurationEvent event) {
    }
}
