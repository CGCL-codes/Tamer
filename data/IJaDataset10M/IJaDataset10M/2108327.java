package rene.zirkel.dialogs;

import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.JPanel;
import rene.gui.ButtonAction;
import rene.gui.CloseDialog;
import rene.gui.MyLabel;
import rene.gui.MyPanel;
import rene.zirkel.Zirkel;
import rene.zirkel.macro.Macro;

/**
 * @author Rene Dialog to ask the user if all macros should be replaced from the
 *         loaded file.
 */
public class ReplaceMacroQuestion extends CloseDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public int Result = NO;

    public static int NO = 0, YES = 1, ALL = -1;

    public ReplaceMacroQuestion(final Frame f, final Macro m) {
        super(f, Zirkel.name("macro.replace.title"), true);
        final JPanel pc = new MyPanel();
        final FlowLayout fl = new FlowLayout();
        pc.setLayout(fl);
        fl.setAlignment(FlowLayout.CENTER);
        pc.add(new MyLabel(Zirkel.name("macro.replace") + " " + m.getName()));
        add("Center", pc);
        final JPanel p = new MyPanel();
        p.setLayout(new FlowLayout(FlowLayout.RIGHT));
        p.add(new ButtonAction(this, Zirkel.name("yes"), "Yes"));
        p.add(new ButtonAction(this, Zirkel.name("no"), "No"));
        p.add(new ButtonAction(this, Zirkel.name("macro.replace.all"), "All"));
        add("South", p);
        pack();
    }

    @Override
    public void doAction(final String o) {
        if (o.equals("Yes")) {
            tell(this, YES);
        } else if (o.equals("No")) {
            tell(this, NO);
        } else if (o.equals("All")) {
            tell(this, ALL);
        } else super.doAction(o);
    }

    public void tell(final ReplaceMacroQuestion q, final int f) {
        Result = f;
        doclose();
    }

    public boolean isNo() {
        return Result == NO;
    }

    public boolean isAll() {
        return Result == ALL;
    }
}
