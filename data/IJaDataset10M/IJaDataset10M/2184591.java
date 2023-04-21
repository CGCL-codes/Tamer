package mw.server.card.colorless;

import mw.mtgforge.Command;
import mw.server.GameManager;
import mw.server.model.Card;
import mw.server.model.MagicWarsModel;

@SuppressWarnings("serial")
public class SpidersilkNet {

    public Card getCard(final GameManager game, final Card card) {
        Command onEquip = new Command() {

            public void execute() {
                if (card.isEquipping()) {
                    Card target = card.getEquipping().get(0);
                    target.addDefense(2);
                    target.addKeyword(MagicWarsModel.KEYWORD_REACH_SA);
                }
            }
        };
        Command onUnEquip = new Command() {

            public void execute() {
                if (card.isEquipping()) {
                    Card target = card.getEquipping().get(0);
                    target.subDefense(2);
                    target.removeKeyword(MagicWarsModel.KEYWORD_REACH_SA);
                }
            }
        };
        card.setEquip(onEquip);
        card.setUnEquip(onUnEquip);
        return card;
    }
}
