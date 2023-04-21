package org.powerfolder.console.develop;

import java.util.ArrayList;
import org.powerfolder.PFRuntimeException;
import org.powerfolder.config.ConfigManager;
import org.powerfolder.config.ScriptTagSetHolder;
import org.powerfolder.console.ConsolePageContext;
import org.powerfolder.utils.misc.MiscHelper;
import org.powerfolder.workflow.model.script.RootScriptTagHolder;
import org.powerfolder.workflow.model.script.ScriptTagInitializer;

public class ScriptStudioTagContext extends ConsolePageContext {

    private String treeState = null;

    private ScriptTagInitializer sti = null;

    private ConfigManager cm = null;

    private RootScriptTagHolder rsth = null;

    private String stiCode = null;

    private ScriptTagSetHolder stsh = null;

    private ScriptStudioTagContext(int inIndent, String inTreeState, ScriptTagInitializer inSti, ConfigManager inCm, RootScriptTagHolder inRsth, String inStiCode, int inLeftMargin) {
        super(inIndent, inLeftMargin);
        this.treeState = inTreeState;
        this.sti = inSti;
        this.cm = inCm;
        this.rsth = inRsth;
        this.stiCode = inStiCode;
        this.stsh = inCm.getScriptTagSet();
    }

    public static final ScriptStudioTagContext newInstance(int inIndent, String inTreeState, ScriptTagInitializer inSti, ConfigManager inCm, RootScriptTagHolder inRsth, String inHighlightCode, int inLeftMargin) {
        return new ScriptStudioTagContext(inIndent, inTreeState, inSti, inCm, inRsth, inHighlightCode, inLeftMargin);
    }

    public String getTreeState() {
        return this.treeState;
    }

    public void setScriptTagInitializer(ScriptTagInitializer inSti) {
        this.sti = inSti;
    }

    public ScriptTagInitializer getScriptTagInitializer() {
        return this.sti;
    }

    public ConfigManager getConfigManager() {
        return this.cm;
    }

    public RootScriptTagHolder getRootScriptTagHolder() {
        return this.rsth;
    }

    public void setScriptTagInitializerCode(String inStiCode) {
        this.stiCode = inStiCode;
    }

    public String getScriptTagInitializerCode() {
        return this.stiCode;
    }

    public ScriptTagSetHolder getScriptTagSetHolder() {
        return this.stsh;
    }
}
