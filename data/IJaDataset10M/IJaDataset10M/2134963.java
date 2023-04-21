package net.iharder.script;

import java.awt.BorderLayout;
import java.util.List;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.swing.JApplet;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Student
 */
public class MainApplet extends JApplet {

    private JTabbedPane enginesTabbedPane;

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    @Override
    public void init() {
        this.enginesTabbedPane = new JTabbedPane();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(enginesTabbedPane);
        new Thread(new Runnable() {

            public void run() {
                ScriptEngineManager manager = new ScriptEngineManager();
                final List<ScriptEngineFactory> factories = manager.getEngineFactories();
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        for (ScriptEngineFactory f : factories) {
                            addFactory(f);
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Adds a ScriptEngineFactory to the layout.
     * Call on event thread.
     * @param factory
     */
    private void addFactory(ScriptEngineFactory factory) {
        int prevCount = this.enginesTabbedPane.getTabCount();
        for (int i = 0; i < prevCount; i++) {
            EnginePane ep = (EnginePane) this.enginesTabbedPane.getComponent(i);
            ScriptEngineFactory f = ep.getFactory();
            System.out.println("Inspecting " + f.getEngineName());
            if (ep != null && factory.getEngineName().equals(f.getEngineName()) && factory.getEngineVersion().equals(f.getEngineVersion())) {
                System.out.println("Already have this loaded: " + factory.getEngineName());
                return;
            }
        }
        EnginePane pane = new EnginePane();
        pane.setFactory(factory);
        this.enginesTabbedPane.add(pane);
    }
}
