package plugin.qualifier.template;

import java.util.ArrayList;
import pcgen.core.PCTemplate;
import pcgen.core.PlayerCharacter;
import pcgen.rules.persistence.token.AbstractPCQualifierToken;

public class PCToken extends AbstractPCQualifierToken<PCTemplate> {

    @Override
    protected ArrayList<PCTemplate> getPossessed(PlayerCharacter pc) {
        return pc.getTemplateList();
    }
}
