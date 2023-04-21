package net.hakulaite.maverick.dialog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import net.hakulaite.maverick.MaverickPlugin;
import net.hakulaite.maverick.util.SourceScanner;

/**
 * This class implements a "EditCommand" dialog
 * 
 * @author sam
 *  
 */
public class EditCommandDialog extends BaseMaverickDialog implements SelectionListener {

    /** Logger */
    private static Logger log = Logger.getLogger(EditCommandDialog.class);

    public EditCommandDialog(Shell parent, String title, String message, IDialogCallback cb, Map defaults) {
        super(parent, title, message, cb, defaults);
    }

    protected Control createDialogArea(Composite parent) {
        super.createDialogArea(parent);
        log.debug("createDialogArea()");
        createRowWithLabelAndText("create.new.command.name.label", "create.new.command.name.text");
        createRowWithLabelAndComboAndButton("create.new.command.controller.label", "create.new.command.controller.combo", "create.new.command.controller.button", this);
        Combo combo = (Combo) components.get("create.new.command.controller.combo");
        createRowWithCheckbox("create.new.command.createnew.checkbox", this);
        Button cb = (Button) components.get("create.new.command.createnew.checkbox");
        cb.setEnabled(false);
        List controllers = null;
        try {
            log.debug("callbackobject:" + this.cbObject);
            controllers = SourceScanner.getControllers(this.cbObject.getProject());
            log.debug("got:" + controllers.size() + "controllers back");
        } catch (Exception e) {
            log.error("problem in getting controllers", e);
        }
        if (controllers != null) {
            Iterator i = controllers.iterator();
            if (i.hasNext()) {
                while (i.hasNext()) {
                    String controller = (String) i.next();
                    combo.add(controller);
                }
            } else {
                combo.add("                              ");
            }
        }
        setDefaults();
        return composite;
    }

    public void widgetSelected(SelectionEvent e) {
        if (e.getSource().equals(components.get("create.new.command.controller.button"))) {
            String res = selectJavaResource(MaverickPlugin.getResourceString("create.new.command.controller.resourcedialog.message"));
            if (res != null) {
                Combo combo = ((Combo) components.get("create.new.command.controller.combo"));
                if (combo != null) {
                    combo.setText(res);
                }
            }
        }
        if (e.getSource().equals(components.get("create.new.command.createnew.checkbox"))) {
            boolean createnew = ((Button) components.get("create.new.command.createnew.checkbox")).getSelection();
            ((Button) components.get("create.new.command.controller.button")).setEnabled(!createnew);
            ((Combo) components.get("create.new.command.controller.combo")).setEnabled(!createnew);
        }
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }
}
