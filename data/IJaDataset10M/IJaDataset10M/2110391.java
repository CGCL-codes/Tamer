package net.sf.pim.plugin.view;

import net.sf.component.config.ConfigHelper;
import net.sf.component.simplenote.SimpleNote;
import net.sf.pim.UiCore;
import net.sf.pim.UiUtil;
import net.sf.pim.action.AttachFileAction;
import net.sf.pim.model.psp.WorkList;
import net.sf.pim.plugin.MyworkPlugin;
import net.sf.util.StringUtil;
import net.sf.util.persistence.DataException;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * 备忘工具
 * @author levin
 *
 */
public class PspMemoTool extends ViewPart {

    public PspMemoTool() {
        super();
    }

    @Override
    public void createPartControl(Composite parent) {
        final UiCore ui = UiUtil.getInstance();
        WorkList wl = null;
        try {
            wl = (WorkList) ui.getDataManager().readData(StringUtil.getCurrentDay());
        } catch (DataException e1) {
            e1.printStackTrace();
        }
        ui.setMemoText(new SimpleNote(parent, SWT.WRAP | SWT.V_SCROLL));
        ui.getMemoText().setBasePath(ConfigHelper.getDataHome() + "/data/");
        ui.getMemoText().setText(wl.getMemo());
        ui.getMemoText().setFont(UiUtil.getFont());
        ui.getMemoText().addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                if (!ui.isMulti() && e.stateMask == 0) ui.setDirty(true);
            }
        });
        Action action = new AttachFileAction();
        action.setImageDescriptor(MyworkPlugin.getImageDescriptor("icons/attach.gif"));
        getViewSite().getActionBars().getToolBarManager().add(action);
    }

    @Override
    public void setFocus() {
    }
}
