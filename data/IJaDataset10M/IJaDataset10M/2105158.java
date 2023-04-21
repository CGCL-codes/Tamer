package depth.formatting.tokentype;

import java.util.List;
import depth.formatting.Grouper;
import depth.formatting.Templates;
import depth.tree.TokenNode;

public class TokenTypeGrouper extends Grouper<TokenTypeGroup> {

    public TokenTypeGrouper(List<TokenNode> nodes) {
        super(nodes);
    }

    public boolean belongsToGroup(TokenTypeGroup group, TokenNode tn) {
        TokenTypeInformation tti = (TokenTypeInformation) tn.getInformation(Templates.TOKENTYPE);
        if (tti == null && !group.isTokenType()) {
            return true;
        }
        if (tti != null && tti.getType().equals(group.getTokenType())) {
            return true;
        }
        return false;
    }

    public TokenTypeGroup createNewGroup(TokenNode tn) {
        TokenTypeInformation tti = (TokenTypeInformation) tn.getInformation(Templates.TOKENTYPE);
        return new TokenTypeGroup(tti == null ? null : tti.getType());
    }
}
