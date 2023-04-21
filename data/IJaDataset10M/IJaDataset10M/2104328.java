package parser.FPac.elements;

import lexer.CBaseToken;
import lexer.FPac.CFPacKeywordList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import parser.FPac.CFPacElement;
import semantic.CBaseEntityFactory;
import semantic.CBaseLanguageEntity;
import semantic.Verbs.CEntityContinue;

public class CFPacGoback extends CFPacElement {

    public CFPacGoback(int line) {
        super(line);
    }

    @Override
    protected boolean DoParsing() {
        CBaseToken tok = GetCurrentToken();
        if (tok.GetKeyword() == CFPacKeywordList.GOBACK) {
            tok = GetNext();
        }
        return true;
    }

    @Override
    protected CBaseLanguageEntity DoCustomSemanticAnalysis(CBaseLanguageEntity parent, CBaseEntityFactory factory) {
        CEntityContinue cont = factory.NewEntityContinue(getLine());
        parent.AddChild(cont);
        return cont;
    }

    @Override
    protected Element ExportCustom(Document root) {
        Element eAdd = root.createElement("GOback");
        return eAdd;
    }
}
