package org.systemsbiology.PIPE2.client.PIPElets.dialogs;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import org.systemsbiology.PIPE2.client.PIPElets.IDMapperPIPElet;
import org.systemsbiology.PIPE2.client.PIPElets.IDMapperPIPEletView;

public class IDMapperAskProteinColumnDialog extends DialogBox {

    private IDMapperPIPEletView view;

    private String[] column_names;

    private ListBox colNamesLB;

    public IDMapperAskProteinColumnDialog(final IDMapperPIPEletView view, String[] column_names) {
        super();
        this.view = view;
        this.column_names = column_names;
        setText("Protein Column Not Detected.");
        VerticalPanel vp = new VerticalPanel();
        vp.setSpacing(3);
        vp.setWidth("100%");
        vp.add(new HTML("Please select the column containing your protein identifiers (or other Primary Key for your data)."));
        colNamesLB = new ListBox();
        for (int i = 0; i < column_names.length; i++) colNamesLB.addItem(column_names[i]);
        colNamesLB.setVisibleItemCount(column_names.length);
        vp.add(colNamesLB);
        vp.add(new Button("OK", new ClickHandler() {

            public void onClick(ClickEvent event) {
                view.changeColumnToFirstColumn(colNamesLB.getSelectedIndex());
                view.update();
                hide();
            }
        }));
        add(vp);
    }
}
