package com.dukesoftware.viewlon3.gui.realworldview;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import com.dukesoftware.utils.awt.AwtUtils;
import com.dukesoftware.utils.swing.others.SwingUtils;
import com.dukesoftware.viewlon3.communicator.ViewlonCommunicatorWrapper;
import com.dukesoftware.viewlon3.data.common.DataControl;
import com.dukesoftware.viewlon3.data.relation.interfacetool.RelationManager;
import com.dukesoftware.viewlon3.gui.common.CommonView;
import com.dukesoftware.viewlon3.gui.infopanel.InfoPanel;

/**
 * 実世界表示ビュークラスです。
 * 
 * 
 *
 *
 */
public class RealWorldView extends CommonView<RealWorldCanvas> {

    private static final long serialVersionUID = 1L;

    private final int centerx;

    private final int centery;

    public RealWorldView(RelationManager r_con, DataControl d_con, InfoPanel info_panel, ViewlonCommunicatorWrapper communicator, int mapx, int mapy) {
        super(r_con, d_con, info_panel, communicator, mapx, mapy, "Real World View");
        centerx = canvas.getCenterX();
        centery = canvas.getCenterY();
    }

    protected JPanel createComponentSlider(int init_val) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 1, 200, init_val);
        slider.addChangeListener(this);
        JPanel panel = new JPanel();
        AwtUtils.setComponentSizeFixed(panel, panelX / 2, panelY);
        panel.setLayout(new BorderLayout());
        panel.add(SwingUtils.createPanelwithLabel("ZOOM", null, null, panelX / 6, panelY), BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        return (panel);
    }

    public JPanel createComponentManage() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        AwtUtils.setComponentSizeFixed(panel, panelX, panelY);
        int init_val = (int) (canvas.getM_Scale() * 100);
        panel.add(createComponentSlider(init_val));
        panel.add(createComponentsButton());
        return (panel);
    }

    private JScrollBar scroll_canvas_x;

    private JScrollBar scroll_canvas_y;

    protected JPanel getCanvas() {
        scroll_canvas_x = new JScrollBar(JScrollBar.HORIZONTAL, centerx, 10, centerx - 1000, centerx + 1000);
        scroll_canvas_x.addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent e) {
                canvas.setCenterX(2 * centerx - e.getValue());
            }
        });
        scroll_canvas_y = new JScrollBar(JScrollBar.VERTICAL, centery, 10, centery - 1000, centery + 1000);
        scroll_canvas_y.addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent e) {
                canvas.setCenterY(2 * centery - e.getValue());
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(canvas, BorderLayout.CENTER);
        panel.add(scroll_canvas_x, BorderLayout.SOUTH);
        panel.add(scroll_canvas_y, BorderLayout.EAST);
        return panel;
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        canvas.setM_Scale((double) source.getValue() / 100);
    }

    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        JButton butt = (JButton) e.getSource();
        if (butt.equals(butt_track)) {
            scroll_canvas_x.setValue(2 * centerx - canvas.getCenterX());
            scroll_canvas_y.setValue(2 * centery - canvas.getCenterY());
        }
    }

    protected RealWorldCanvas createCanvas() {
        return new RealWorldCanvas(r_con, d_con, panelX - 10, mapY - 10, info_panel);
    }
}
