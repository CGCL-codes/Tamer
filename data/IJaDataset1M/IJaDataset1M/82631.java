package org.nicocube.airain.game.client.ui;

import org.nicocube.airain.common.client.table.Item;
import org.nicocube.airain.common.client.table.TableHeader;
import org.nicocube.airain.common.client.table.TableOfItem;
import org.nicocube.airain.domain.client.character.Possession;
import org.nicocube.airain.game.client.AirainGame;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class PossessionsPanel extends AirainPanel {

    private FlowPanel myPanel;

    public Widget getLabel() {
        return new Label("Possessions", false);
    }

    public PossessionsPanel(AirainGame aw) {
        super(aw);
        myPanel = new FlowPanel();
        TableOfItem possessionTable = new TableOfItem(new TableHeader().addCol("Object").addCol("Acquired"));
        for (Possession p : getAirainGame().getGc().getPossessions()) {
            possessionTable.addRow(new Item().setIdent(p.getType().toString()).addValue(p.getCreated().toString()));
        }
        myPanel.add(possessionTable.render());
    }

    @Override
    public Panel getPanel() {
        return myPanel;
    }
}
