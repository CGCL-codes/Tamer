package br.ufmg.lcc.pcollecta.test.searchpages.processing;

import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.arangi.annotations.Page;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiSearchPage;
import br.ufmg.lcc.arangitester.arangi.ui.Button;
import br.ufmg.lcc.arangitester.ui.UiInputText;
import br.ufmg.lcc.arangitester.ui.UiSelect;

@Page(url = "ScheduleSearch.faces", searchButtons = { Button.NEW, Button.DELETE, Button.SEARCH, Button.PRINT }, searchTitle = "Pesquisa de Agendamento")
public class ScheduleSearchPage extends ArangiSearchPage {

    public ScheduleSearchPage() {
        super("scheduleSelTable");
    }

    @Ui(desc = "Processo", id = "scheduleArg_importProcess_name")
    public UiInputText processo;

    @Ui(desc = "Coleta", id = "scheduleArg_harvest_name")
    public UiInputText coleta;

    @Ui(desc = "Situação", id = "scheduleArg_status")
    public UiSelect situacao;
}
