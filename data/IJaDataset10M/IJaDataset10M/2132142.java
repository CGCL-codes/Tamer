package net.sf.lunareclipse.core;

import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;

public class LuaLanguageToolkit extends AbstractLanguageToolkit {

    private static LuaLanguageToolkit sToolkit = new LuaLanguageToolkit();

    private static final String[] languageExtensions = new String[] { "lua" };

    public LuaLanguageToolkit() {
    }

    public static IDLTKLanguageToolkit getDefault() {
        return sToolkit;
    }

    public String getDelimeterReplacerString() {
        return ".";
    }

    public String[] getLanguageFileExtensions() {
        return languageExtensions;
    }

    public String getLanguageName() {
        return "Lua";
    }

    public String getNatureId() {
        return LuaNature.NATURE_ID;
    }

    protected String getCorePluginID() {
        return LuaPlugin.PLUGIN_ID;
    }

    public String getLanguageContentType() {
        return "net.sf.lunareclipse.luaContentType";
    }
}
