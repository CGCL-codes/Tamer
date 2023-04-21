package net.simpleframework.web.page.component.ui.list;

import java.util.Collection;
import net.simpleframework.web.page.component.ComponentParameter;
import net.simpleframework.web.page.component.IComponentHandle;

public interface IListHandle extends IComponentHandle {

    Collection<? extends ListNode> getListnodes(ComponentParameter compParameter, ListNode listNode);
}
