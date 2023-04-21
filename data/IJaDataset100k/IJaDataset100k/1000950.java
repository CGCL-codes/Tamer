package gameserver.skillengine.effect;

import gameserver.model.gameobjects.player.Player;
import gameserver.model.templates.item.ItemTemplate;
import gameserver.services.TeleportService;
import gameserver.skillengine.model.Effect;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReturnPointEffect")
public class ReturnPointEffect extends EffectTemplate {

    @Override
    public void applyEffect(Effect effect) {
        ItemTemplate itemTemplate = effect.getItemTemplate();
        int worldId = itemTemplate.getReturnWorldId();
        String pointAlias = itemTemplate.getReturnAlias();
        TeleportService.teleportToPortalExit(((Player) effect.getEffector()), pointAlias, worldId, 500);
    }

    @Override
    public void calculate(Effect effect) {
        ItemTemplate itemTemplate = effect.getItemTemplate();
        if (itemTemplate != null) effect.addSucessEffect(this);
    }
}
