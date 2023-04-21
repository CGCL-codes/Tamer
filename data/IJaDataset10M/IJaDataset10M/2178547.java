package org.deft.repository.ast.annotation.csformat;

import org.deft.repository.ast.decoration.Format;
import org.deft.repository.ast.decoration.Ident;
import org.deft.repository.ast.decoration.NodeInformation;
import org.deft.repository.ast.decoration.Templates;

public class CSFormatInformation extends NodeInformation {

    private String replaceGroupId, target;

    private Format format;

    public CSFormatInformation(Format format, String target, String replaceGroupId) {
        this.format = format;
        this.replaceGroupId = replaceGroupId;
        this.target = target;
    }

    public Format getFormat() {
        return format;
    }

    public String getReplaceGroupId() {
        return replaceGroupId;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public Ident getIdent() {
        return Templates.CSFORMAT;
    }

    @Override
    public NodeInformation copy() {
        return new CSFormatInformation(format, target, replaceGroupId);
    }

    @Override
    public void addContentFromOtherNodeInformation(NodeInformation newInformation) {
    }
}
