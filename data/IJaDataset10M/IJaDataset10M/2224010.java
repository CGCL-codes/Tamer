package org.nicocube.airain.game.client.ui;

import org.nicocube.airain.common.client.table.Item;
import org.nicocube.airain.common.client.table.TableHeader;
import org.nicocube.airain.common.client.table.TableOfItem;
import org.nicocube.airain.domain.client.character.AttributeIdentifier;
import org.nicocube.airain.domain.client.character.SkillIdentifier;
import org.nicocube.airain.game.client.AirainGame;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class AttrAndSkillPanel extends AirainPanel {

    HorizontalPanel myPanel;

    public Widget getLabel() {
        return new Label("Attribute and Skills", false);
    }

    public AttrAndSkillPanel(AirainGame aw) {
        super(aw);
        myPanel = new HorizontalPanel();
        TableOfItem attributeTable = new TableOfItem(new TableHeader().addCol("Attribute").addCol("Level"));
        for (AttributeIdentifier attr : getAirainGame().getAttributes()) {
            attributeTable.addRow(new Item().setIdent(attr.toString()).addValue(getAirainGame().getGc().getAttributeLevel(attr).toString()));
        }
        myPanel.add(attributeTable.render());
        TableOfItem skillTable = new TableOfItem(new TableHeader().addCol("Skill").addCol("Expertise"));
        for (SkillIdentifier skill : getAirainGame().getSkills()) {
            skillTable.addRow(new Item().setIdent(skill.toString()).addValue(getAirainGame().getGc().getSkillExpertise(skill).toString()));
        }
        myPanel.add(skillTable.render());
    }

    @Override
    public Panel getPanel() {
        return myPanel;
    }
}
