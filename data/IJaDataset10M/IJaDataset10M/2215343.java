package br.ufmg.lcc.pcollecta.test.testsuite.management;

import br.ufmg.lcc.arangitester.annotations.Db;
import br.ufmg.lcc.arangitester.annotations.Login;
import br.ufmg.lcc.arangitester.annotations.Test;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.pcollecta.test.searchpages.management.InvalidKeyMappingSearch;

@Login
@Db({ "pcollecta" })
public class InvalidKeyMappingSearchTestCase {

    @Test(value = "Testa ETC com destino para Arquivo CVS", order = 1)
    public void testETCwithTargetForCVSfile() {
        InvalidKeyMappingSearch searchPage = UiComponentFactory.getInstance(InvalidKeyMappingSearch.class);
        ;
        searchPage.invoke();
        searchPage.ETC.select("ETC Usuários - BDR to CSV");
        searchPage.pesquisar.click();
        searchPage.verifyTextNotPresent("java.lang.reflect.InvocationTargetException");
    }

    @Test(value = "Testa ETC com mapeamento inválido", order = 1)
    public void testETCwithInvalidMapping() {
        InvalidKeyMappingSearch searchPage = UiComponentFactory.getInstance(InvalidKeyMappingSearch.class);
        ;
        searchPage.invoke();
        searchPage.ETC.select("ETC Usuários");
        searchPage.pesquisar.click();
        searchPage.verifyTextNotPresent("Não foram encontrados Mapeamentos Inválidos para a ETC informada.");
    }

    @Test(value = "Verifica mensagem logo ao entrar na página", order = 2)
    public void testMsgAfterInvokePage() {
        InvalidKeyMappingSearch searchPage = UiComponentFactory.getInstance(InvalidKeyMappingSearch.class);
        ;
        searchPage.invoke();
        searchPage.verifyTextNotPresent("Por favor informe a ETC para a consulta.");
    }

    @Test(value = "Testa se ETC sem useMapping é exibido no caixa de ETC", order = 2)
    public void testETCwithoutUseMappgingIsShown() {
        InvalidKeyMappingSearch searchPage = UiComponentFactory.getInstance(InvalidKeyMappingSearch.class);
        ;
        searchPage.invoke();
        searchPage.ETC.verifyTextNotPresent("Etc Tabela Discente");
    }
}
