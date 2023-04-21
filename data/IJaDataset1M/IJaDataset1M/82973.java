package gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import fiziksobject.magneticField;

public class MagneticFieldEditor extends ForcerEditor<magneticField> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private MagneticFieldEditor thiz = this;

    public MagneticFieldEditor(final magneticField f) {
        super(f);
        final NumberInput strength = new NumberInput("Strength(T)", f.getStrength());
        final BooleanDropDown bdd = new BooleanDropDown(f.getInto(), "Direction", "Into", "Out of");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(strength);
        add(bdd);
        JButton ok = new JButton("Ok");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                f.setInto(bdd.getValue());
                f.setStrength(strength.getNum());
                WindowEvent wev = new WindowEvent(thiz, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
            }
        });
        add(ok);
    }
}
