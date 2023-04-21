package jbomberman.client.renderer;

import jbomberman.game.Board;
import jbomberman.game.DeserializationException;
import jbomberman.game.RuleSet;
import jbomberman.util.ThreadObservable;
import jbomberman.util.ThreadObserver;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.swing.SwingUtilities;

/**
 * A swing wrapper for the renderer.
 * At the moment it should be only used with Components that don't have borders. (JPanel, ???, ...)
 *
 * Created: Sun Jun 15 10:11:55 2003
 *
 * @author <a href="mailto:manni@sbox.tugraz.at">Manfred Klopschitz</a>
 * @version 1.0
 */
public class JRenderer {

    protected Renderer renderer_;

    protected Component component_;

    protected Image buffer_;

    protected Runner runner_ = new Runner();

    private String plugin_;

    public JRenderer(Component component, String plugin) throws RendererPluginException {
        plugin_ = plugin;
        component_ = component;
        renderer_ = new Renderer(plugin);
        renderer_.addObserver(runner_);
    }

    public void update(Board board, RuleSet rule_set) throws IOException, DeserializationException {
        if ((buffer_ == null) || (buffer_.getWidth(null) != component_.getWidth()) || (buffer_.getHeight(null) != component_.getHeight())) {
            buffer_ = component_.createImage(component_.getWidth(), component_.getHeight());
        }
        Rectangle clipping_area = new Rectangle(0, 0, component_.getWidth(), component_.getHeight());
        Board copy = new Board(board.getAFrame(), rule_set);
        renderer_.update((Graphics2D) buffer_.getGraphics(), clipping_area, copy);
    }

    public void loadPlugin(String plugin) throws RendererPluginException {
        renderer_.loadPlugin(plugin);
        plugin_ = plugin;
    }

    public String[] getPlugins() {
        return new String[] { "minimal", "bitmap" };
    }

    public String getLoadedPlugin() {
        return plugin_;
    }

    public void stop() {
        renderer_.stop();
    }

    protected class Runner implements Runnable, ThreadObserver {

        public void run() {
            Graphics graphics = component_.getGraphics();
            graphics.drawImage(buffer_, 0, 0, null);
        }

        public void threadUpdate(ThreadObservable observable, Object o) {
            SwingUtilities.invokeLater(this);
        }
    }
}
