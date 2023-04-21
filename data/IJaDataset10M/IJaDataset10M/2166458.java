package rgzm.gui.gleis;

import java.awt.Component;
import javax.swing.JButton;
import rgzm.bean.Gleis;
import rgzm.bean.Zug;
import rgzm.gui.gleis.listeners.GleisAction;
import base.resources.Resources;

public class GleisButton extends JButton implements GleisView {

    private Gleis oGleis;

    public GleisButton(final String text, final Gleis gleis) {
        super(text);
        setHorizontalAlignment(CENTER);
        addActionListener(new GleisAction(this));
        oGleis = gleis;
        updateFromModel();
    }

    /** Liefert sich selbst (JButton) */
    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public Gleis getGleis() {
        return oGleis;
    }

    private void updateFromModel() {
        final Zug z = oGleis.getZug();
        if (z != null) {
            String format = Resources.getDatePattern("timetable.view_time_pattern");
            String an = "-";
            String ab = "-";
            if (z.getAn() != null) {
                an = z.getAn().toString(format);
            }
            if (z.getAb() != null) {
                ab = z.getAb().toString(format);
            }
            setText(z.getZugNr() + " ( " + an + " / " + ab + " )");
            setToolTipText(getText());
        } else {
            setText(Resources.getText("track_view.insert"));
            setToolTipText(null);
        }
    }

    @Override
    public void trackAllocationChanged(final Gleis track) {
        updateFromModel();
    }
}
