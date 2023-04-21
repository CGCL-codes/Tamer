package net.sf.pim.plugin.view;

import net.sf.pim.UiUtil;
import net.sf.pim.model.Personal;
import net.sf.util.StringUtil;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * 编辑器
 * @author levin
 */
public class PersonalEditor extends AbstractEditor {

    public PersonalEditor() {
        super("个人周报", Personal.class, StringUtil.getCurrentWeek());
    }

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        editor.getTable().addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                clearCache();
            }
        });
    }

    private void clearCache() {
        if (!UiUtil.getPlansCache().isEmpty()) {
            UiUtil.getPlansCache().clear();
        }
    }
}
