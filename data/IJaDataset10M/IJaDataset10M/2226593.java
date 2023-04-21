package net.sf.bluex.parser;

import java.util.Vector;
import net.sf.bluex.plugin.Dependency;
import net.sf.bluex.plugin.MenuEntry;
import net.sf.bluex.plugin.ModuleDetail;
import net.sf.bluex.plugin.OtherEntry;
import net.sf.bluex.plugin.PluginMetaData;
import net.sf.bluex.plugin.Version;
import newComponents.DateSelector;
import newComponents.events.ParsingListener;

/**
 *
 * @author Blue
 */
public class PluginParsingListener implements ParsingListener {

    private Vector<PluginMetaData> vectPMD = new Vector<PluginMetaData>();

    private PluginMetaData pmd;

    private Dependency dep;

    private OtherEntry oe;

    private MenuEntry me;

    private ModuleDetail md;

    public void startElement(String element) {
        if (element.equals("plugin")) pmd = new PluginMetaData(); else if (element.equals("dependency")) dep = new Dependency(); else if (element.equals("other-entry")) oe = new OtherEntry(); else if (element.equals("menu")) me = new MenuEntry(); else if (element.equals("module-installed")) md = new ModuleDetail();
    }

    public void character(String data, String element) {
        if (element.equals("name")) pmd.setName(data); else if (element.equals("size")) pmd.setSize(new Integer(data)); else if (element.equals("author")) pmd.setAuthor(data); else if (element.equals("release-date")) pmd.setReleaseDate(DateSelector.getDate(data)); else if (element.equals("version")) pmd.setVersion(new Version(data)); else if (element.equals("jar-file-name")) pmd.setJarFileName(data); else if (element.equals("plugin-class")) pmd.setPluginClass(data); else if (element.equals("help-available")) pmd.setHelpAvailable(Boolean.parseBoolean(data)); else if (element.equals("plugin-description")) pmd.setPluginDescription(data); else if (element.equals("entry")) pmd.addChange(data); else if (element.equals("minimum-base-window-version")) pmd.setMinimumBaseWindowVersion(new Version(data)); else if (element.equals("dependency-jar-file-name")) dep.setJarFileName(data); else if (element.equals("minimum-dependency-version")) dep.setMinimumDependencyVersion(new Version(data)); else if (element.equals("plugin-name")) oe.setPluginName(data); else if (element.equals("location")) me.setLocation(data); else if (element.equals("text")) me.setText(data); else if (element.equals("event-class")) me.setEventClass(data); else if (element.equals("mi-type")) md.setModuleType(new Integer(data)); else if (element.equals("mapping-extension")) md.addExtension(data);
    }

    public void endElement(String element) {
        if (element.equals("plugin")) vectPMD.add(pmd); else if (element.equals("dependency")) pmd.addDependency(dep); else if (element.equals("other-entry")) pmd.addOtherEntry(oe); else if (element.equals("menu")) oe.addMenu(me); else if (element.equals("module-installed")) pmd.addModule(md);
    }

    public Vector<PluginMetaData> getPluginMetaDatas() {
        return vectPMD;
    }
}
