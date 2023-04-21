package gui.space;

import gui.value.ValueListGUI;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.softmed.jops.space.simple.LineGenerator;

public class LineGUI extends BasicSpaceEditor {

    private static final long serialVersionUID = -1207401491727102558L;

    JButton a = new JButton("Point A");

    JButton b = new JButton("Point B");

    private LineGenerator box;

    private ValueListGUI vgui;

    public LineGUI(JComponent destination) {
        super(destination);
        setEasyDimension(a, widthB + 40, heightB);
        setEasyDimension(b, widthB + 40, heightB);
        a.addActionListener(this);
        b.addActionListener(this);
        top.add(a);
        top.add(b);
        top.add(Box.createHorizontalGlue());
    }

    @Override
    protected void actionDone(Object source) {
        if (source == a) {
            destination.removeAll();
            vgui = new ValueListGUI("Point A");
            vgui.setValueList(null, box.getA());
            destination.add(vgui);
        } else if (source == b) {
            destination.removeAll();
            vgui = new ValueListGUI("Point B");
            vgui.setValueList(null, box.getB());
            destination.add(vgui);
        }
    }

    @Override
    public void setObjectMine(Object obj) {
        box = (LineGenerator) obj;
    }
}
