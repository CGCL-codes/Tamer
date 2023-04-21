package org.deft.repository.ast.decoration.tokentype;

import org.deft.repository.ast.annotation.Ident;
import org.deft.repository.ast.annotation.NodeInformation;
import org.deft.repository.ast.decoration.Templates;

public class TokenTypeInformation extends NodeInformation {

    private String type;

    public TokenTypeInformation(String type) {
        this.type = type;
    }

    public Ident getIdent() {
        return Templates.TOKENTYPE;
    }

    public String getType() {
        return type;
    }

    @Override
    public NodeInformation copy() {
        return new TokenTypeInformation(type);
    }
}
