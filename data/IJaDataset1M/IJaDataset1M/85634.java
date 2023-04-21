package sqlTestes.tComplexoSelect.tTabelaSelecao;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import sql.ddl.Tabela;
import sql.dml.selecao.filtro.FiltroSQl;
import sql.dml.selecao.filtro.FiltroSQlBasico;
import sql.dml.selecao.tabela.TabelaSelecao;
import sql.dml.selecao.tabela.juncao.TabelaSelecaoJoin;
import sql.dml.selecao.tabela.juncao.TipoJuncaoPesquisa;

public class TesteJoinsComON {

    TabelaSelecaoJoin obj;

    Tabela tabela1 = new Tabela("Tabela1");

    Tabela tabela2 = new Tabela("Tabela2");

    FiltroSQl[] fitro;

    @Before
    public void up() {
        fitro = new FiltroSQl[] { new FiltroSQlBasico("a", "b", "="), new FiltroSQlBasico("a", "1", "=") };
    }

    @Test
    public void tabela1InnerTabela2ComDoisFiltros() {
        obj = new TabelaSelecaoJoin(new TabelaSelecao(tabela1), new TabelaSelecao(tabela2), TipoJuncaoPesquisa.INNER, fitro);
        Assert.assertEquals("Tabela1 INNER JOIN Tabela2 ON a = b AND a = 1", obj.codigoSQL());
    }

    @Test
    public void selecaoComJuncaoRIGHTeFiltro() {
        obj = new TabelaSelecaoJoin(new TabelaSelecao(tabela1), new TabelaSelecao(tabela2), TipoJuncaoPesquisa.RIGHT, fitro);
        Assert.assertEquals("Tabela1 RIGHT JOIN Tabela2 ON a = b AND a = 1", obj.codigoSQL());
    }

    @Test
    public void selecaoComJuncaoLeftEFiltros() {
        obj = new TabelaSelecaoJoin(new TabelaSelecao(tabela1), new TabelaSelecao(tabela2), TipoJuncaoPesquisa.LEFT, fitro);
        Assert.assertEquals("Tabela1 LEFT JOIN Tabela2 ON a = b AND a = 1", obj.codigoSQL());
    }

    @Test
    public void selecaoComJuncaoEAliasAguaParaAJuncao() {
        obj = new TabelaSelecaoJoin(new TabelaSelecao(tabela1), new TabelaSelecao(tabela2), TipoJuncaoPesquisa.LEFT, "agua", fitro);
        Assert.assertEquals("(Tabela1 LEFT JOIN Tabela2 ON a = b AND a = 1)AS agua", obj.codigoSQL());
    }
}
