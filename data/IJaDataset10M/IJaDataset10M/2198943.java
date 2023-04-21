package mw.server.card.parser.antlr.command;

import mw.server.model.Card;
import mw.server.model.MagicWarsModel;
import mw.server.model.spell.Flashback;
import mw.server.pattern.Command;

public class AddFlashback extends Command {

    private static final long serialVersionUID = 1L;

    private String cost;

    public AddFlashback(String cost) {
        this.cost = cost;
    }

    @Override
    public void execute() {
        Card c = (Card) getTarget();
        Flashback flashback = new Flashback(c.getSpellAbility()[0], cost);
        c.addSpellAbility(flashback);
        c.addKeyword(MagicWarsModel.KEYWORD_FLASHBACK_SA);
    }
}
