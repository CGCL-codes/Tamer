package jat.demo.vr.Copernicus1;

import jat.vr.*;
import jat.cm.*;
import jat.util.*;
import java.awt.*;
import java.applet.Applet;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.applet.MainFrame;
import javax.swing.*;
import java.awt.event.*;

/**
 *
 * @author Tobias Berthold
 * @version 1.0
 */
public class EarthOrbit extends Applet implements ActionListener {

    BranchGroup BG_root;

    BranchGroup BG_vp;

    TransformGroup TG_scene;

    Transform3D Trans = new Transform3D();

    ThreeDStudioObject carrier;

    TestGeom tg = null;

    ColorCube3D cc;

    Planet3D earth, mars;

    RGBAxis3D mainaxis;

    Orbit carrierorbit = null;

    Point3d origin = new Point3d(0.0f, 0.0f, 0.0f);

    BoundingSphere bounds = new BoundingSphere(origin, 1.e10);

    ControlPanel panel;

    public CapturingCanvas3D c;

    Timer animControl;

    double alpha;

    int i = 0;

    int steps = 10000;

    public EarthOrbit() {
        String b = FileUtil.getClassFilePath("jat.demo.vr.Copernicus1", "EarthOrbit");
        System.out.println(b);
        setLayout(new BorderLayout());
        c = createCanvas(b + "frames/");
        add("Center", c);
        panel = new ControlPanel(BG_root);
        add("South", panel);
        BG_root = new BranchGroup();
        BG_root.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        BG_root.setBounds(bounds);
        TG_scene = new TransformGroup();
        TG_scene.addChild(earth = new Planet3D(this, Planet3D.EARTH));
        TG_scene.addChild(mainaxis = new RGBAxis3D(7000.0f));
        TG_scene.addChild(carrier = new ThreeDStudioObject(this, "carrier.3DS", 1.f));
        BG_root.addChild(TG_scene);
        BG_root.addChild(carrierorbit = new Orbit(cm.earth_radius + 1500., 0.02, 5.0, 0.0, 0.0, 160, Colors.pink, steps));
        BG_root.addChild(jat_light.DirectionalLight(bounds));
        jat_light.setDirection(1.f, 1.f, -2.f);
        BG_vp = jat_view.view(0., -1000., 2000., c, 1.f, 100000.f);
        carrier.set_attitude(0, 0, -0.5 * Math.PI);
        Vector3d carrierpos = new Vector3d(carrierorbit.x[0], carrierorbit.y[0], carrierorbit.z[0]);
        carrier.set_position(carrierpos);
        Vector3d viewpos = new Vector3d(carrierorbit.x[0] - 1000., carrierorbit.y[0] + 1500, carrierorbit.z[0] + 300);
        jat_view.set_pos_direction(viewpos, carrierpos);
        jat_behavior.behavior(BG_root, BG_vp, bounds);
        jat_behavior.xyz_Behavior.set_translate(100.f);
        VirtualUniverse universe = new VirtualUniverse();
        Locale locale = new Locale(universe);
        locale.addBranchGraph(BG_root);
        locale.addBranchGraph(BG_vp);
        int delayValue = 50;
        animControl = new Timer(delayValue, this);
        animControl.start();
    }

    public void actionPerformed(ActionEvent e) {
        i++;
        if (i < 100) {
            carrier.set_attitude(0, 0, -0.5 * Math.PI - alpha);
        }
        if (i > 100) {
            carrier.thrusters_on();
        }
        alpha += 0.001;
        earth.set_attitude(Math.PI / 2., 0, alpha);
        Vector3d V_view = jat_view.get_view();
        panel.label.setText("  x " + (long) V_view.x + "  y " + (long) V_view.y + "  z " + (long) V_view.z);
        carrier.set_position(carrierorbit.x[i], carrierorbit.y[i], carrierorbit.z[i]);
        Vector3d carrierpos = new Vector3d(carrierorbit.x[i], carrierorbit.y[i], carrierorbit.z[i]);
        Vector3d viewpos = new Vector3d(carrierorbit.x[i] - 1000., carrierorbit.y[i] + 1500, carrierorbit.z[i] + 300);
        jat_view.set_pos_direction(viewpos, carrierpos);
        if (i <= 300) {
            try {
                Thread.sleep(100);
            } catch (Exception f) {
            }
            ;
            c.takePicture();
        }
    }

    private CapturingCanvas3D createCanvas(String frames_path) {
        GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
        GraphicsConfiguration gc1 = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getBestConfiguration(template);
        return new CapturingCanvas3D(gc1, frames_path);
    }

    public void init() {
    }

    public static void main(String[] args) {
        EarthOrbit sh = new EarthOrbit();
        MainFrame m = new MainFrame(sh, 800, 600);
        m.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    }
}
