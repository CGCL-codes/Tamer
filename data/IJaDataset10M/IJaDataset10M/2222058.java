package br.ufmg.lcc.pcollecta.test.pages.setting;

import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.arangi.annotations.Page;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiTabularPage;
import br.ufmg.lcc.arangitester.arangi.ui.Button;
import br.ufmg.lcc.arangitester.arangi.ui.GenericLine;
import br.ufmg.lcc.arangitester.ioc.ICreate;
import br.ufmg.lcc.arangitester.ui.UiInputText;

@Page(url = "GlobalParameter.faces", editButtons = { Button.CANCEL, Button.SAVE }, viewButtons = { Button.NEW, Button.EDIT, Button.OPEN, Button.PRINT }, includeTitle = "Inclusão de Parâmetros Globais", editTitle = "Alteração de Parâmetros Globais")
public class GlobalParameterPage extends ArangiTabularPage<GlobalParameterPage.GlobalParameterLinha> implements ICreate {

    public GlobalParameterPage() {
        super(GlobalParameterLinha.class);
    }

    public static class GlobalParameterLinha extends GenericLine {

        @Ui(desc = "Nome #{index}", id = "name")
        public UiInputText name;

        @Ui(desc = "Valor #{index}", id = "value")
        public UiInputText value;
    }

    @Override
    public void create() {
        getTable().setComponentId("globalParameterTable");
    }
}
