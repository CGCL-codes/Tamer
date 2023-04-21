package gui.value;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Vector3f;
import org.softmed.jops.fileloading.converters.IValueConverter;
import org.softmed.jops.values.GenericValue;
import gui.Editor;
import gui.SizeablePanel;
import gui.indicators.ColorIndicator;
import gui.indicators.FloatIndicator;
import gui.indicators.Indicator;
import gui.indicators.Point3fIndicator;
import gui.indicators.Vector3fIndicator;

public class GenericValueGUI extends SizeablePanel implements ActionListener, ChangeListener {

    private static final long serialVersionUID = -8341729438398983670L;

    static final Icon red = new ImageIcon("media/images/12-em-cross.png");

    static final Icon green = new ImageIcon("media/images/12-em-plus.png");

    static final int B_WIDTH = 15;

    static final int B_HEIGHT = 20;

    FloatIndicator timeInd = new FloatIndicator();

    Indicator valueInd;

    JButton remove = new JButton(red);

    JButton add = new JButton(green);

    Object temp;

    EditActionListener editActionListener;

    GenericValue gvalue;

    private IValueConverter converter;

    public void setCustomEnabled(boolean enabled) {
        remove.setEnabled(enabled);
        add.setEnabled(enabled);
        timeInd.setCustomEnabled(enabled);
        valueInd.setCustomEnabled(enabled);
    }

    public GenericValueGUI(IValueConverter converter2, GenericValue v) {
        this.converter = converter2;
        setDimension(560, 40);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        gvalue = v;
        timeInd.setArgs(100f, 0f, 20f, 0.01f, 1f, 0f, 5f, 10f);
        timeInd.setValue(gvalue.getTime());
        timeInd.setChangeListener(this);
        remove.addActionListener(this);
        add.addActionListener(this);
        add.setMaximumSize(new Dimension(B_WIDTH, B_HEIGHT));
        remove.setMaximumSize(new Dimension(B_WIDTH, B_HEIGHT));
        add.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        remove.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        add(timeInd);
        temp = gvalue.getValue();
        if (temp instanceof Float) {
            valueInd = new FloatIndicator();
        } else if (temp instanceof Vector3f) {
            valueInd = new Vector3fIndicator();
        } else if (temp instanceof Colorf) {
            valueInd = new ColorIndicator();
        } else if (temp instanceof Point3f) {
            valueInd = new Point3fIndicator();
        } else throw new RuntimeException("Can't Find a proper Indicator !!! Class Not Recognized !!! No Known Indicators !!!");
        if (valueInd != null) {
            valueInd.setConverter(converter);
            setValue(temp);
            valueInd.setChangeListener(this);
            add(valueInd);
        }
        add(remove);
        add(add);
    }

    protected void setValue(Object temp) {
        valueInd.setValue(temp);
    }

    @SuppressWarnings("unchecked")
    public void refresh() {
        timeInd.setValue(gvalue.getTime());
        if (valueInd != null) setValue(gvalue.value);
        refreshGUI();
    }

    private void refreshGUI() {
        invalidate();
        revalidate();
        repaint();
    }

    public EditActionListener getEditActionListener() {
        return editActionListener;
    }

    public void setEditActionListener(EditActionListener editActionListener) {
        this.editActionListener = editActionListener;
    }

    public void actionPerformed(ActionEvent e) {
        Editor.setDirty(true);
        if (e.getSource() == remove) {
            if (editActionListener != null) editActionListener.remove(this);
        } else if (e.getSource() == add) {
            if (editActionListener != null) editActionListener.addNew(this);
        } else {
            if (e.getActionCommand().equalsIgnoreCase("remove")) {
                if (editActionListener != null) editActionListener.remove(this);
            } else {
            }
        }
    }

    public void changed(Indicator indicator) {
        gvalue.setTime(timeInd.getValue());
        if (valueInd != null) {
            gvalue.setValue(valueInd.getValue());
        }
        if (editActionListener != null) editActionListener.changed(this);
    }

    public void setArgs(Object... args) {
        if (valueInd != null) valueInd.setArgs(args);
    }

    public GenericValue getGvalue() {
        return gvalue;
    }

    public void setGvalue(GenericValue gvalue) {
        this.gvalue = gvalue;
        refresh();
    }

    public Indicator getValueInd() {
        return valueInd;
    }

    public FloatIndicator getTimeInd() {
        return timeInd;
    }

    public void setConverter(IValueConverter converter) {
        this.converter = converter;
    }
}
