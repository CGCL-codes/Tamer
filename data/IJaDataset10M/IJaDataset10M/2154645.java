package net.sourceforge.xhsi.panel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.sourceforge.xhsi.PreferencesObserver;
import net.sourceforge.xhsi.XHSIPreferences;
import net.sourceforge.xhsi.XHSISettings;
import net.sourceforge.xhsi.XHSIStatus;
import net.sourceforge.xhsi.model.Aircraft;
import net.sourceforge.xhsi.model.Avionics;
import net.sourceforge.xhsi.model.ModelFactory;
import net.sourceforge.xhsi.model.Observer;

public class NDComponent extends Component implements Observer, PreferencesObserver {

    private static final long serialVersionUID = 1L;

    public static boolean COLLECT_PROFILING_INFORMATION = false;

    public static long NB_OF_PAINTS_BETWEEN_PROFILING_INFO_OUTPUT = 100;

    private static Logger logger = Logger.getLogger("net.sourceforge.xhsi");

    ArrayList subcomponents = new ArrayList();

    long[] subcomponent_paint_times = new long[15];

    long total_paint_times = 0;

    long nb_of_paints = 0;

    Graphics2D g2;

    NDGraphicsConfig nd_gc;

    ModelFactory model_factory;

    boolean update_since_last_heartbeat = false;

    StatusMessage status_message_comp;

    Aircraft aircraft;

    Avionics avionics;

    public NDComponent(ModelFactory model_factory) {
        this.nd_gc = new NDGraphicsConfig(this);
        this.model_factory = model_factory;
        this.aircraft = this.model_factory.get_aircraft_instance();
        this.avionics = this.aircraft.get_avionics();
        nd_gc.reconfig = true;
        this.repaint();
        addComponentListener(nd_gc);
        subcomponents.add(new CDI(model_factory, nd_gc, this));
        subcomponents.add(new MovingMap(model_factory, nd_gc, this));
        subcomponents.add(new AltitudeRangeArc(model_factory, nd_gc, this));
        subcomponents.add(new HoldingPattern(model_factory, nd_gc, this));
        subcomponents.add(new PositionTrendVector(model_factory, nd_gc));
        subcomponents.add(new ClipRoseArea(model_factory, nd_gc, this));
        subcomponents.add(new CompassRose(model_factory, nd_gc));
        subcomponents.add(new GS(model_factory, nd_gc, this));
        subcomponents.add(new ForegroundMessages(model_factory, nd_gc, this));
        subcomponents.add(new SpeedsLabel(model_factory, nd_gc));
        subcomponents.add(new DestinationLabel(model_factory, nd_gc, this));
        subcomponents.add(new HeadingLabel(model_factory, nd_gc, this));
        subcomponents.add(new APHeading(model_factory, nd_gc));
        subcomponents.add(new RadioHeadingArrows(model_factory, nd_gc));
        subcomponents.add(new RadioLabel(model_factory, nd_gc, this));
        subcomponents.add(new RefSourceLabel(model_factory, nd_gc, this));
        subcomponents.add(new InstrumentFrame(model_factory, nd_gc));
    }

    public Dimension getPreferredSize() {
        return new Dimension(NDGraphicsConfig.INITIAL_PANEL_SIZE + 2 * NDGraphicsConfig.INITIAL_BORDER_SIZE, NDGraphicsConfig.INITIAL_PANEL_SIZE + 2 * NDGraphicsConfig.INITIAL_BORDER_SIZE);
    }

    public void paint(Graphics g) {
        drawAll(g);
    }

    public void drawAll(Graphics g) {
        g2 = (Graphics2D) g;
        g2.setRenderingHints(nd_gc.rendering_hints);
        g2.setStroke(new BasicStroke(2.0f));
        g2.setBackground(nd_gc.background_color);
        nd_gc.update_config(g2, this.avionics.map_mode(), this.avionics.map_submode(), this.avionics.map_range(), this.avionics.power());
        String rotation = XHSIPreferences.get_instance().get_preference(XHSIPreferences.PREF_ND_ORIENTATION);
        if (rotation.equalsIgnoreCase(XHSIPreferences.ND_LEFT)) {
            g2.rotate(-Math.PI / 2.0, nd_gc.panel_size.width / 2, nd_gc.panel_size.width / 2);
        } else if (rotation.equalsIgnoreCase(XHSIPreferences.ND_RIGHT)) {
            g2.rotate(Math.PI / 2.0, nd_gc.panel_size.height / 2, nd_gc.panel_size.height / 2);
        } else if (rotation.equalsIgnoreCase(XHSIPreferences.ND_UPSIDE_DOWN)) {
            g2.rotate(Math.PI, nd_gc.panel_size.width / 2, nd_gc.panel_size.height / 2);
        }
        g2.clearRect(0, 0, nd_gc.panel_size.width, nd_gc.panel_size.height);
        long time = 0;
        long paint_time = 0;
        for (int i = 0; i < this.subcomponents.size(); i++) {
            if (NDComponent.COLLECT_PROFILING_INFORMATION) {
                time = System.currentTimeMillis();
            }
            ((NDSubcomponent) this.subcomponents.get(i)).paint(g2);
            if (NDComponent.COLLECT_PROFILING_INFORMATION) {
                paint_time = System.currentTimeMillis() - time;
                this.subcomponent_paint_times[i] += paint_time;
                this.total_paint_times += paint_time;
            }
        }
        nd_gc.reconfigured = false;
        this.nb_of_paints += 1;
        if (NDComponent.COLLECT_PROFILING_INFORMATION) {
            if (this.nb_of_paints % NDComponent.NB_OF_PAINTS_BETWEEN_PROFILING_INFO_OUTPUT == 0) {
                logger.info("Paint profiling info");
                logger.info("=[ Paint profile info begin ]=================================");
                for (int i = 0; i < this.subcomponents.size(); i++) {
                    logger.info(this.subcomponents.get(i).toString() + ": " + ((1.0f * this.subcomponent_paint_times[i]) / (this.nb_of_paints * 1.0f)) + "ms " + "(" + ((this.subcomponent_paint_times[i] * 100) / this.total_paint_times) + "%)");
                }
                logger.info("Total                    " + (this.total_paint_times / this.nb_of_paints) + "ms \n");
                logger.info("=[ Paint profile info end ]===================================");
            }
        }
    }

    public void show_status_message(String text) {
        this.status_message_comp.set_message(text);
        repaint(100);
    }

    public void hide_status_message() {
        this.status_message_comp.clear();
        repaint();
    }

    public void update() {
        repaint();
        this.update_since_last_heartbeat = true;
    }

    public void componentResized() {
    }

    public void preference_changed(String key) {
        logger.finest("Preference changed");
        this.nd_gc.reconfig = true;
        repaint();
    }

    public void forceReconfig() {
        componentResized();
        this.nd_gc.reconfig = true;
        repaint();
    }
}
