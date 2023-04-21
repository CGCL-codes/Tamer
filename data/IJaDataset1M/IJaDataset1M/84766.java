package org.snipsnap.render.macro.loader;

import groovy.lang.GroovyClassLoader;
import org.radeox.macro.Macro;
import org.radeox.macro.MacroLoader;
import org.radeox.macro.Repository;
import snipsnap.api.notification.Consumer;
import org.snipsnap.notification.Message;
import org.snipsnap.notification.MessageService;
import snipsnap.api.snip.Snip;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Plugin loader for macros with Groovy source
 * instead of Java
 *
 * @author Stephan J. Schmidt
 * @version $Id: GroovyMacroLoader.java 1849 2006-02-07 21:08:46Z leo $
 */
public class GroovyMacroLoader extends MacroLoader implements Consumer {

    public GroovyMacroLoader() {
        MessageService service = (MessageService) snipsnap.api.container.Components.getComponent(MessageService.class);
        service.register(this);
    }

    public void consume(Message messsage) {
        if (Message.SNIP_MODIFIED.equals(messsage.getType())) {
            Snip snip = (Snip) messsage.getValue();
            if (snip.getName().startsWith("SnipSnap/config/macros/")) {
                try {
                    Macro macro = compileMacro(snip.getContent());
                    if (null != macro) {
                        add(repository, macro);
                    }
                } catch (Exception e) {
                    System.err.println("GroovyMacroLoader: unable to reload macros: " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    private Macro compileMacro(String macroSource) {
        Macro macro = null;
        try {
            GroovyClassLoader gcl = new GroovyClassLoader();
            InputStream is = new ByteArrayInputStream(macroSource.getBytes());
            Class clazz = gcl.parseClass(is, "");
            Object aScript = clazz.newInstance();
            macro = (Macro) aScript;
        } catch (Exception e) {
            System.err.println("Cannot compile groovy macro: " + e.getMessage());
            e.printStackTrace();
        }
        return macro;
    }

    /**
   * Load all plugins of Class klass
   *
   * @param repository
   * @param klass
   * @return
   */
    public Repository loadPlugins(Repository repository, Class klass) {
        if (null != repository) {
            snipsnap.api.snip.SnipSpace space = (snipsnap.api.snip.SnipSpace) snipsnap.api.container.Components.getComponent(snipsnap.api.snip.SnipSpace.class);
            snipsnap.api.snip.Snip[] snips = space.match("SnipSnap/config/macros/");
            for (int i = 0; i < snips.length; i++) {
                Snip snip = snips[i];
                Macro macro = compileMacro(snip.getContent());
                add(repository, macro);
            }
        }
        return repository;
    }
}
