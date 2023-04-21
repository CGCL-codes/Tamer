package br.ufmg.lcc.pcollecta.test.pages.management;

import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.arangi.annotations.Page;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiPage;
import br.ufmg.lcc.arangitester.arangi.ui.Button;
import br.ufmg.lcc.arangitester.ui.UiButton;

@Page(url = "Approval.faces", editButtons = { Button.CANCEL }, editTitle = "Alteração de Aprovação")
public class ApprovalPage extends ArangiPage {

    @Ui(desc = "Reprovar", id = "buttonDisapproveRequestItem")
    public UiButton reprovar;

    @Ui(desc = "Aprovar", id = "buttonApproveRequestItem")
    public UiButton aprovar;
}
