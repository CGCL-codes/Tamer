package net.israfil.markup.plugin.swing;

import java.net.URL;
import javax.swing.JComponent;
import javax.swing.JFrame;
import net.israfil.markup.core.ComponentTracker;
import net.israfil.markup.core.JAXBHelper;
import net.israfil.markup.model.Markup;
import net.israfil.markup.plugin.AbstractPlugin;
import net.israfil.markup.plugin.GUI;

public class SwingPlugin extends AbstractPlugin {

    private ComponentTracker<JFrame, JComponent> components;

    public SwingPlugin(ComponentTracker<JFrame, JComponent> components, JAXBHelper jaxbHelper) {
        super(jaxbHelper);
        this.components = components;
    }

    public String getToolkit() {
        return "Swing";
    }

    protected String getPluginPackage() {
        return "net.israfil.markup.model.swing";
    }

    public GUI buildGUI(URL definition) {
        Markup markup = getMarkup(definition);
        return new SwingGUI(components, markup);
    }
}
