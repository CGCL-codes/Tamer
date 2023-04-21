package org.sss.presentation.zk.component;

import java.util.ArrayList;
import org.sss.common.model.IModuleList;
import org.sss.presentation.zk.ZkContext;
import org.sss.presentation.zk.ZkUtils;
import org.sss.presentation.zk.common.spi.AbstractComposer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

/**
 * Listbox扩展类
 * @author Jason.Hoo (latest modification by $Author: hujianxin78728 $)
 * @version $Revision: 541 $ $Date: 2009-10-01 09:47:46 -0400 (Thu, 01 Oct 2009) $
 */
public class ListboxComposer extends AbstractComposer {

    public void doAfterCompose(Component component) throws Exception {
        final Listbox listbox = (Listbox) component;
        final ZkContext ctx = ZkUtils.getZkContext(ZkUtils.getSession(component.getDesktop().getSession()));
        final IModuleList list = (IModuleList) getBaseObject(ctx, listbox);
        bindField(list, listbox, ctx.isIgnore());
        addChild(ctx, listbox, list);
        listbox.setTooltiptext(ZkUtils.i18n.getString(ctx.getLocale(), (String) listbox.getAttribute(I18NTOOLTIPTEXT)));
        listbox.addEventListener("onSelect", new EventListener() {

            public void onEvent(Event event) throws Exception {
                if (listbox.isMultiple()) {
                    ArrayList<Integer> selected = new ArrayList<Integer>();
                    for (Object item : listbox.getSelectedItems()) selected.add((Integer) ((Listitem) item).getValue());
                    listSelectEvent(ctx, listbox.getDesktop(), list, selected);
                } else {
                    int index = (Integer) listbox.getSelectedItem().getValue();
                    list.setIndex(index);
                    listSelectEvent(ctx, listbox.getDesktop(), list, index);
                }
            }
        });
        listbox.addEventListener("onModified", new EventListener() {

            public void onEvent(Event event) throws Exception {
                addChild(ctx, listbox, list);
            }
        });
    }
}
