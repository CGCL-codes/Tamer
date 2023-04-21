package org.fao.fenix.web.client.re;

import java.util.List;
import net.mygwt.ui.client.widget.Info;
import org.fao.fenix.web.client.Fenix;
import org.fao.fenix.web.client.FenixResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedCategory {

    public static VerticalPanel panel = new VerticalPanel();

    public static ListBox listbox = new ListBox(false);

    public VerticalPanel build() {
        Fenix.resourceExplorerService.getTreeModel("category", new AsyncCallback() {

            public void onSuccess(Object result) {
                List list = (List) result;
                for (int i = listbox.getItemCount() - 1; i >= 0; i--) listbox.removeItem(i);
                for (int i = 0; i < list.size(); i++) {
                    FenixResource tmp = (FenixResource) list.get(i);
                    listbox.addItem(tmp.getTitle(), tmp.getId());
                }
                listbox.setSize(ResourceExplorerSettings.ADVANCED_WIDTH, "170px");
                listbox.setVisibleItemCount(10);
                listbox.setMultipleSelect(true);
                panel.add(listbox);
                panel.setWidth(FenixResourceExplorer.settings.ADVANCED_WIDTH);
                listbox.addChangeListener(new ChangeListener() {

                    public void onChange(Widget sender) {
                        FenixResourceExplorer.keywordPanel.updatePreview();
                    }
                });
            }

            public void onFailure(Throwable caught) {
                Info.show("Service call failed!", "Service call to {0} failed", "getTreeModel()");
            }
        });
        return panel;
    }
}
