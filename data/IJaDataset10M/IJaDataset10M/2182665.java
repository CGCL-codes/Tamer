package realtimetrading.gui.graphquote;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;
import javax.swing.JPanel;
import realtimetrading.exchage.ContractList;
import realtimetrading.gui.graphquote.perspective.Perspective;
import realtimetrading.gui.graphquote.perspective.PricePerspective;

/**
 * @author Mike
 * Panello su cui vine disegnato il grafico
 */
public class Chart extends JPanel {

    private static final long serialVersionUID = 1L;

    public ViewPort vp;

    private Vector<Perspective> perspective;

    protected ContractList contractList = new ContractList();

    public Chart() {
        super();
        setBackground(Color.WHITE);
        this.setPreferredSize(new java.awt.Dimension(506, 251));
        this.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent evt) {
                thisComponentResized(evt);
            }
        });
        vp = new ViewPort(this.getSize().width, this.getSize().height);
        perspective = new Vector<Perspective>();
        perspective.add(new PricePerspective());
        perspective.lastElement().active = true;
    }

    public Perspective getPerspective(int type) {
        for (int i = 0; i < perspective.size(); i++) if (perspective.get(i).type == type) return perspective.get(i);
        return perspective.firstElement();
    }

    public void enablePerspective(int type) {
        for (int i = 0; i < perspective.size(); i++) if (perspective.get(i).type == type) perspective.get(i).active = true;
        this.repaint();
    }

    public void disablePerspective(int type) {
        for (int i = 0; i < perspective.size(); i++) if (perspective.get(i).type == type) perspective.get(i).active = false;
        this.repaint();
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gd2 = (Graphics2D) g;
        for (int i = perspective.size() - 1; i >= 0; i--) {
            if (perspective.get(i).active) perspective.get(i).paint(gd2, vp, contractList);
        }
    }

    private void thisComponentResized(ComponentEvent evt) {
        vp.setSize(this.getSize().width, this.getSize().height);
        this.repaint();
    }
}
