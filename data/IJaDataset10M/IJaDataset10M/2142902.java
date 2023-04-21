package universe.base;

import universe.control.*;
import java.awt.Color;
import java.awt.RenderingHints;
import universe.*;
import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import image.*;

/** A Class representing a Universe (a collection of Worlds) of some type, and related methods
 *    and Function Objects for handling messages and events */
public class UniverseBase<Msg extends Serializable> {

    public static int PORT = 4567;

    private Universe<Msg> initial;

    double time;

    OnDraw<Msg> ondraw;

    OnTick<Msg> ontick;

    OnNew<Msg> onnew;

    OnDisconnect<Msg> ondisconnect;

    OnMsg<Msg> onmsg;

    public UniverseBase(Universe<Msg> initial) {
        this(initial, 0.05, null, null, null, null, null);
    }

    public UniverseBase<Msg> onDraw(OnDraw<Msg> ondraw) {
        return new UniverseBase<Msg>(this.initial, this.time, ondraw, this.ontick, this.onnew, this.ondisconnect, this.onmsg);
    }

    public UniverseBase<Msg> onTick(OnTick<Msg> ontick) {
        return onTick(ontick, 0.05);
    }

    public UniverseBase<Msg> onTick(OnTick<Msg> ontick, double time) {
        return new UniverseBase<Msg>(this.initial, this.time, this.ondraw, ontick, this.onnew, this.ondisconnect, this.onmsg);
    }

    public UniverseBase<Msg> onNew(OnNew<Msg> onnew) {
        return new UniverseBase<Msg>(this.initial, this.time, this.ondraw, this.ontick, onnew, this.ondisconnect, this.onmsg);
    }

    public UniverseBase<Msg> onDisconnect(OnDisconnect<Msg> ondisconnect) {
        return new UniverseBase<Msg>(this.initial, this.time, this.ondraw, this.ontick, this.onnew, ondisconnect, this.onmsg);
    }

    public UniverseBase<Msg> onMsg(OnMsg<Msg> onmsg) {
        return new UniverseBase<Msg>(this.initial, this.time, this.ondraw, this.ontick, this.onnew, this.ondisconnect, onmsg);
    }

    private static int SPACE = 5;

    public Universe<Msg> universe() {
        if (this.onmsg == null) throw new RuntimeException("No Universe Message Handler");
        if (this.ondraw == null) throw new RuntimeException("No Universe Draw Handler");
        JDialog f = new JDialog((JFrame) null, "Universe", true);
        Scene scn = doOnDraw(this.initial);
        final Handler<Msg> handler = new Handler<Msg>(this, this.initial, new BufferedImage((int) (scn.width() + 2 * SPACE), (int) (scn.height() + 2 * SPACE), BufferedImage.TYPE_INT_RGB), (int) (this.time * 1000), f);
        f.setSize((int) (SPACE * 2 + Math.max(20, 14 + scn.width())), (int) (Math.max(20, SPACE * 2 + 31 + scn.height())));
        f.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        f.setResizable(false);
        f.getContentPane().add(handler);
        handler.run.start();
        try {
            final Server<Msg> server = new Server<Msg>(PORT, false, this);
            Thread msgs = new Thread() {

                public void run() {
                    while (true) {
                        try {
                            synchronized (server) {
                                server.wait();
                            }
                            if (server.hasMessage()) {
                                WithWorld m = server.nextMessage();
                                Bundle<Msg> res = null;
                                if (m.isConnect()) {
                                    res = doOnNew(handler.u, m.getWImp());
                                } else if (m.isDisconnect()) {
                                    res = doDisconnect(handler.u, m.getWImp(), server);
                                } else if (m.isTransfer()) {
                                    res = doOnMsg(handler.u, m.getWImp(), m.<Msg>payload());
                                } else Server.p("Unknown Message Type");
                                if (res != null) {
                                    handler.replace(res);
                                    for (Mail<Msg> mail : res.getMails()) {
                                        server.processMail(mail, m.getWImp());
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            Server.p("!!Interrupted!!");
                        }
                    }
                }
            };
            msgs.start();
            f.setVisible(true);
            System.exit(0);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return handler.u;
    }

    private UniverseBase(Universe<Msg> init, double time, OnDraw<Msg> ondraw, OnTick<Msg> ontick, OnNew<Msg> onnew, OnDisconnect<Msg> ondisconnect, OnMsg<Msg> onmsg) {
        this.initial = init;
        this.time = time;
        this.ondraw = ondraw;
        this.ontick = ontick;
        this.onnew = onnew;
        this.ondisconnect = ondisconnect;
        this.onmsg = onmsg;
    }

    /** Handles the nitty-gritty of Universe updates */
    static class Handler<Msg extends Serializable> extends javax.swing.JComponent implements ActionListener {

        private static final long serialVersionUID = 1L;

        UniverseBase<Msg> univ;

        Universe<Msg> u;

        BufferedImage buffer;

        Graphics2D graph;

        Timer run;

        Handler(UniverseBase<Msg> univ, Universe<Msg> u, BufferedImage buff, int msec, JDialog dia) {
            this.univ = univ;
            this.u = u;
            this.run = new Timer(msec, this);
            this.buffer = buff;
            this.graph = buff.createGraphics();
            this.graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            this.graph.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }

        public void actionPerformed(ActionEvent e) {
            replace(this.univ.doOnTick(this.u));
        }

        private synchronized void replace(Bundle<Msg> b) {
            if (b == null) return;
            boolean change = !this.u.equals(b.getUniverse());
            this.u = b.getUniverse();
            if (change) repaint();
        }

        public void paint(java.awt.Graphics g) {
            this.graph.setColor(Color.white);
            this.graph.fillRect(0, 0, this.getWidth(), this.getHeight());
            this.graph.clipRect(SPACE, SPACE, this.buffer.getWidth() - SPACE * 2, this.buffer.getHeight() - SPACE * 2);
            this.univ.doOnDraw(this.u).paint(this.graph, SPACE, SPACE);
            g.drawImage(this.buffer, 0, 0, null);
        }
    }

    Scene doOnDraw(Universe<Msg> u) {
        return this.ondraw.apply(u);
    }

    Bundle<Msg> doOnTick(Universe<Msg> u) {
        if (this.ontick == null) return null;
        return this.ontick.apply(u);
    }

    Bundle<Msg> doOnNew(Universe<Msg> u, IWorld w) {
        if (this.onnew == null) return null;
        return this.onnew.apply(u, w);
    }

    Bundle<Msg> doDisconnect(Universe<Msg> u, IWorld w, Server<Msg> server) {
        if (this.ondisconnect == null) return null;
        server.removeWorld(w);
        return this.ondisconnect.apply(u, w);
    }

    Bundle<Msg> doOnMsg(Universe<Msg> u, IWorld w, Msg msg) {
        if (this.onmsg == null) return null;
        return this.onmsg.apply(u, w, msg);
    }
}
