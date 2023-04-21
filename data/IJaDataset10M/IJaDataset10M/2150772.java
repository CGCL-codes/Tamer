package gumbo.app.awt.util;

import gumbo.core.life.impl.DisposableImpl;
import gumbo.core.util.AssertUtils;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * Base class for tools used to build a JPanel, which consists of an optional
 * border and none or more formatted rows of labeled widgets.
 * @author jonb
 */
public class JPanelTool extends DisposableImpl.IdentityEquality {

    /**
	 * Creates an instance.
	 * @param target Shared exposed target panel. Never null.
	 */
    public JPanelTool(JPanel target) {
        AssertUtils.assertNonNullArg(target);
        _target = target;
        _target.setLayout(new GridBagLayout());
    }

    /**
	 * Gets the target panel.
	 * @return Shared exposed target panel. Never null.
	 */
    public JPanel getTarget() {
        return _target;
    }

    /**
	 * Sets the border of this panel to a default one, replacing any previous
	 * border.
	 * @param title Border title. None if null (i.e. untitled border).
	 */
    public void setBorder(String title) {
        setBorder(_target, title);
    }

    private JPanel _target;

    private static final long serialVersionUID = 1L;

    /**
	 * Sets the border of a target panel to a default one, replacing any
	 * previous border.
	 * @param target Temp exposed target panel. Never null.
	 * @param title Border title. None if null (i.e. untitled border).
	 * @return Reference to target. Never null.
	 */
    public static JPanel setBorder(JPanel target, String title) {
        AssertUtils.assertNonNullArg(target);
        Border border = BorderFactory.createEtchedBorder();
        if (title == null) {
        } else {
            border = BorderFactory.createTitledBorder(border, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null);
        }
        target.setBorder(border);
        return target;
    }
}
