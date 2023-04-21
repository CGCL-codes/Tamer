package sqlTestes.tComplexoSelect;

import junit.framework.Assert;
import org.junit.Test;
import sql.ddl.Tabela;
import sql.ddl.coluna.Coluna;
import sql.dml.selecao.FabricaSelecao;
import sql.dml.selecao.Select;
import sql.dml.selecao.coluna.ColunaSelecao;
import sql.dml.selecao.coluna.ListaOQue;
import sql.dml.selecao.coluna.Oque;
import sql.dml.selecao.tabela.Onde;
import sql.dml.selecao.tabela.TabelaSelecao;

public class TesteSelecoesSimplesDeTabelas implements Especificacao {

    Tabela __agua = new Tabela("Agua");

    ListaOQue colunas = new ListaOQue();

    TabelaSelecao __TabelaAgua = new TabelaSelecao(__agua, colunas);

    @Test
    public void selecionarTabelaAgua() {
        Select selecaoAgua = FabricaSelecao.fabricaSelecao(new Onde[] { new TabelaSelecao(__agua) });
        Assert.assertEquals("SELECT * FROM Agua", selecaoAgua.codigoSQL());
    }

    @Test
    public void selecionarTabelaAgua_ColunaA() {
        Select selecaoAgua = FabricaSelecao.fabricaSelecao(new Onde[] { new TabelaSelecao(__agua) }, new Oque[] { new ColunaSelecao(new Coluna("int", "a")) });
        Assert.assertEquals("SELECT a FROM Agua", selecaoAgua.codigoSQL());
    }

    @Test
    public void seleccionarTabelaAguaColunaA_B() {
        Select selecaoAgua = FabricaSelecao.fabricaSelecao(new Onde[] { new TabelaSelecao(__agua) }, new Oque[] { new ColunaSelecao(new Coluna("int", "a")), new ColunaSelecao(new Coluna("int", "b")) });
        Assert.assertEquals("SELECT a,b FROM Agua", selecaoAgua.codigoSQL());
    }

    @Test
    public void selecionarTabelaAgua_Fogo() {
        Select selecaoAgua = FabricaSelecao.fabricaSelecao(new Onde[] { new TabelaSelecao(__agua), new TabelaSelecao(new Tabela("Fogo")) });
        Assert.assertEquals("SELECT * FROM Agua,Fogo", selecaoAgua.codigoSQL());
    }

    @Test
    public void selecionarTabelaAgua_Fogo_colunasAgua_a_Coluna_Fogo_b() {
        Onde fogo = new TabelaSelecao(new Tabela("Fogo"));
        Onde agua = new TabelaSelecao(__agua);
        Select selecaoAgua = FabricaSelecao.fabricaSelecao(new Onde[] { agua, fogo }, new Oque[] { new ColunaSelecao(new Coluna("int", "a"), agua), new ColunaSelecao(new Coluna("int", "b"), fogo) });
        Assert.assertEquals("SELECT Agua.a,Fogo.b FROM Agua,Fogo", selecaoAgua.codigoSQL());
    }
}
