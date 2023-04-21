package net.confex.customxml.jogl;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import net.confex.customxml.CustomXmlNode;
import net.confex.translations.Translator;
import net.confex.tree.ConfigTree;
import net.confex.tree.IStateObserver;
import org.eclipse.ui.IViewPart;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.opengl.util.Animator;

public class CustomXmlJoglNode extends CustomXmlNode {

    public String getAboutString() {
        return Translator.getString("ABOUT_CustomXmlJoglNode");
    }

    protected static String default_image_name = "cube.png";

    public String getDefaultImage() {
        return default_image_name;
    }

    public static String getDefaultImageName() {
        return default_image_name;
    }

    public CustomXmlJoglNode(ConfigTree configTree, IStateObserver stateObserver) {
        super(configTree, stateObserver);
    }

    /**
	 * Just a test!!!
	 */
    public void run(IViewPart view) {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        JFrame jf = new JFrame("JOGL Test");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jf.setBounds(0, 0, screenSize.width / 2, screenSize.height / 2);
        GLCanvas canvas = new GLCanvas();
        Gears gears = new Gears(canvas);
        canvas.addGLEventListener(gears);
        canvas.addKeyListener(gears);
        jf.add(canvas);
        final Animator animator = new Animator(canvas);
        jf.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                    }
                }).start();
            }
        });
        animator.start();
        jf.setVisible(true);
    }

    /**
	 * Custom method with three params. Main entry point.
	 * 
	 * @param par1 - CustomXmlNode
	 * @param par2 - maps of objects
	 * @param par3 - not used
	 *            
	 * @return 
	 */
    public Object customMethod3(Object par1, Object par2, Object par3) {
        if (!(par1 instanceof CustomXmlNode)) {
            System.err.println("par1 not instanceof CustomXmlNode!");
            return null;
        }
        if (!(par2 instanceof HashMap)) {
            System.err.println("par3 not instanceof HashMap<String,Object>!");
            return null;
        }
        return null;
    }
}
