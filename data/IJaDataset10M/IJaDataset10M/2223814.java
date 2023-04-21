package gu.client.view.workspace;

import gu.client.model.Shipper;
import gu.client.view.DatabaseEditorView;
import gu.client.view.dialogs.ShipperDialog;
import java.util.Iterator;
import java.util.List;

public class ShipperListView extends ListView {

    public ShipperListView(List stories, DatabaseEditorView view) {
        super("<img src='item-stories.png' hspace='3'>Shipper", view);
        addColumn("Company");
        addColumn("City");
        addColumn("Province");
        addColumn("PostalCode");
        addColumn("Latitude");
        addColumn("Longtitude");
        addColumn("ShipId");
        int row = 0;
        for (Iterator it = stories.iterator(); it.hasNext(); ) {
            Shipper story = (Shipper) it.next();
            addField(row, story.getName());
            addField(row, story.getCity());
            addField(row, story.getProv());
            addField(row, story.getPostalCode());
            addField(row, story.getLatitude());
            addField(row, story.getLongtitude());
            addField(row, story.get_ship_id());
            row++;
        }
    }

    protected void onCreate() {
        new ShipperDialog(getView());
    }
}
