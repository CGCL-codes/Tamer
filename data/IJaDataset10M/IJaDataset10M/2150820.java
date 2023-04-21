package javaTeste.tVariavel;

import java.atributo.TiposPrimitivos;
import java.classe.Classe;
import java.variavel.Variavel;
import java.variavel.VariavelObjeto;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class TesteDeclaracaoVariavel implements Especificacao {

    Variavel VARINT = null;

    Variavel pessoa = null;

    Variavel string = null;

    @Before
    public void up() {
        VARINT = new Variavel(TiposPrimitivos.INT, "varInt");
        pessoa = new VariavelObjeto(new Classe("Pessoa"), "pessoa");
        string = new Variavel(TiposPrimitivos.STRING, "string");
    }

    @Test
    public void criarAVariavelIntIdentificadorVARINT() {
        Assert.assertEquals("int varInt", VARINT.codigoFonte());
    }

    @Test
    public void criarAVariavelClassePessoaDefault() {
        Assert.assertEquals("Pessoa pessoa", pessoa.codigoFonte());
    }

    @Test
    public void criarAVariavelStringIdentificadorString() {
        Assert.assertEquals("String string", string.codigoFonte());
    }

    @Test
    public void pegarNomeVariavelStringIdentificadorString() {
        Assert.assertEquals("string", string.getNome());
    }
}
