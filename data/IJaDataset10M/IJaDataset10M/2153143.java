package gcp.testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import gcp.Amigo;
import gcp.enums.Sexo;

/**
 * Classe de teste de Amigo.
 */
public class AmigoTeste {

    private Amigo amigo1, amigo2, amigo3, amigo4;

    /**
	 * Testa a cricao de um amigo.
	 * @throws Exception Lancada quando algum parametro for nulo.
	 */
    @Test
    public void testaAmigo() throws Exception {
        new Amigo("Catharine", "Quintans", Sexo.FEMININO);
        new Amigo("Natasha", "Beeeeezerra", Sexo.SELECIONAR);
    }

    /**
	 * Testa criacao de amigo com sobrenome nulo.
	 * @throws Exception Lancada quando algum parametro for nulo.
	 */
    @Test(expected = Exception.class)
    public void testaSobreNomeNulo() throws Exception {
        new Amigo("Catharine", null, Sexo.FEMININO);
        new Amigo("Natasha", null, Sexo.MASCULINO);
    }

    /**
	 * Testa a criacao de amigo com nome nulo.
	 * @throws Exception Lancada quando algum parametro for nulo.
	 */
    @Test(expected = Exception.class)
    public void testaNomeNulo() throws Exception {
        new Amigo(null, "Ernesto", Sexo.MASCULINO);
        new Amigo(null, "Furtado", Sexo.FEMININO);
    }

    /**
	 * Testa criacao de amigo com nome vazio.
	 * @throws Exception Lancada quando algum parametro for vazio.
	 */
    @Test(expected = Exception.class)
    public void testaNomeVazio() throws Exception {
        new Amigo("", "Quintans", Sexo.FEMININO);
        new Amigo("", "Bezerra", Sexo.MASCULINO);
    }

    /**
	 * Testa criacao de amigo com sobrenome vazio.
	 * @throws Exception Lancada quando algum parametro for vazio.
	 */
    @Test(expected = Exception.class)
    public void testaSobreNomeVazio() throws Exception {
        new Amigo("Hildegard", "", Sexo.FEMININO);
        new Amigo("Diego", "", Sexo.MASCULINO);
    }

    /**
	 * Criacao das variaveis
	 * @throws Exception Lancada quando algum parametro for invalido.
	 */
    @Before
    public void setUp() throws Exception {
        amigo1 = new Amigo("Catharine", "Quintans", Sexo.FEMININO);
        amigo2 = new Amigo("Natasha", "Bezerra", Sexo.SELECIONAR);
        amigo3 = new Amigo("Diego", "Ernesto", Sexo.MASCULINO);
        amigo4 = new Amigo("Hildegard", "Furtado", Sexo.FEMININO);
    }

    /**
	 * Testa o metodo getNome.
	 */
    @Test
    public void testaGetNomes() {
        assertEquals("Catharine", amigo1.getNome());
        assertEquals("Natasha", amigo2.getNome());
        assertEquals("Diego", amigo3.getNome());
        assertEquals("Hildegard", amigo4.getNome());
    }

    /**
	 * Testa o metodo setNome.
	 * @throws Exception Lancada quando algum parametro for invalido.
	 */
    @Test
    public void testaSetNomes() throws Exception {
        amigo1.setNome("Natasha");
        amigo2.setNome("Diego");
        amigo3.setNome("Hildegard");
        amigo4.setNome("Catharine");
        assertEquals("Natasha", amigo1.getNome());
        assertEquals("Diego", amigo2.getNome());
        assertEquals("Hildegard", amigo3.getNome());
        assertEquals("Catharine", amigo4.getNome());
    }

    /**
	 * Testa o metodo getSobrenome.
	 */
    @Test
    public void testagetSobrenome() {
        assertEquals("Quintans", amigo1.getSobrenome());
        assertEquals("Bezerra", amigo2.getSobrenome());
        assertEquals("Ernesto", amigo3.getSobrenome());
        assertEquals("Furtado", amigo4.getSobrenome());
    }

    /**
	 * Testa o metodo setSobrenome
	 * @throws Exception Lancada quando algum parametro for invalido.
	 */
    @Test
    public void testaSetSobrenomes() throws Exception {
        amigo1.setSobrenome("Bezerra");
        amigo2.setSobrenome("Ernesto");
        amigo3.setSobrenome("Furtado");
        amigo4.setSobrenome("Quintans");
        assertEquals("Bezerra", amigo1.getSobrenome());
        assertEquals("Ernesto", amigo2.getSobrenome());
        assertEquals("Furtado", amigo3.getSobrenome());
        assertEquals("Quintans", amigo4.getSobrenome());
    }

    /**
	 * Testa o metodo getSexo.
	 */
    @Test
    public void testaGetSexo() {
        assertEquals(Sexo.FEMININO, amigo1.getSexo());
        assertEquals(Sexo.SELECIONAR, amigo2.getSexo());
        assertEquals(Sexo.MASCULINO, amigo3.getSexo());
        assertEquals(Sexo.FEMININO, amigo4.getSexo());
    }

    /**
	 * Testa o metodo setSexo.
	 */
    @Test
    public void testaSetSexo() {
        amigo1.setSexo(Sexo.FEMININO);
        amigo2.setSexo(Sexo.SELECIONAR);
        amigo3.setSexo(Sexo.SELECIONAR);
        amigo4.setSexo(Sexo.SELECIONAR);
        assertEquals(Sexo.FEMININO, amigo1.getSexo());
        assertEquals(Sexo.SELECIONAR, amigo2.getSexo());
        assertEquals(Sexo.SELECIONAR, amigo3.getSexo());
        assertEquals(Sexo.SELECIONAR, amigo4.getSexo());
    }

    /**
	 * Testa os metodos setEndereco e getEndereco.
	 * @throws Exception Lanaada quando algum parametro for invalido.
	 */
    @Test
    public void testaSetEGetEndereco() throws Exception {
        amigo1.setEndereco("");
        assertEquals("", amigo1.getEndereco());
        amigo2.setEndereco("Rua das Almas, nº 234, Centro");
        amigo3.setEndereco("Rua Chica da Silva, nº 67, Prata");
        amigo4.setEndereco("Rua dos Afazeres, nº 110, Zé da Silva");
        assertEquals("Rua das Almas, nº 234, Centro", amigo2.getEndereco());
        assertEquals("Rua Chica da Silva, nº 67, Prata", amigo3.getEndereco());
        assertEquals("Rua dos Afazeres, nº 110, Zé da Silva", amigo4.getEndereco());
    }

    /**
	  * Testa os metodos setEmail e getEmail.
	  * @throws Exception Lancada quando algum parametro for invalido.
	  */
    @Test
    public void testaSetEGetEmail() throws Exception {
        amigo1.setEmail("");
        assertEquals("", amigo1.getEmail());
        amigo2.setEmail("natasha@lcc.ufcg.edu.br");
        amigo3.setEmail("diego@lcc.ufcg.edu.br");
        amigo4.setEmail("hildegard@lcc.ufcg.edu.br");
        assertEquals("natasha@lcc.ufcg.edu.br", amigo2.getEmail());
        assertEquals("diego@lcc.ufcg.edu.br", amigo3.getEmail());
        assertEquals("hildegard@lcc.ufcg.edu.br", amigo4.getEmail());
    }

    /**
	  * Testa os metodos setTelefone e getTelefone.
	  * @throws Exception Lancada quando algum parametro for invalido.
	  */
    @Test
    public void testaSetEGetTelefone() throws Exception {
        amigo1.setTelefone("");
        assertEquals("", amigo1.getTelefone());
        amigo2.setTelefone("3333-3333");
        amigo3.setTelefone("4444-4444");
        amigo4.setTelefone("5555-5555");
        assertEquals("3333-3333", amigo2.getTelefone());
        assertEquals("4444-4444", amigo3.getTelefone());
        assertEquals("5555-5555", amigo4.getTelefone());
    }

    /**
     * Testa os metodos setTelefone e getTelefone.
     * @throws Exception Lancada quando algum parametro for invalido.
     */
    @Test
    public void testaSetEGetCelular() throws Exception {
        amigo1.setTelefone("");
        assertEquals("", amigo1.getTelefone());
        amigo2.setCelular("3333-3333");
        amigo3.setCelular("4444-4444");
        amigo4.setCelular("5555-5555");
        assertEquals("3333-3333", amigo2.getCelular());
        assertEquals("4444-4444", amigo3.getCelular());
        assertEquals("5555-5555", amigo4.getCelular());
    }

    /**
	 * Testa o metodo equals.
	 * @throws Exception Lancada quando algum parametro for invalido.
	 */
    @Test
    public void testaAmigoEquals() throws Exception {
        Amigo lala = new Amigo("Lala", "Lele", Sexo.FEMININO);
        Amigo lele = new Amigo("Lele", "Lili", Sexo.MASCULINO);
        Amigo lili = new Amigo("Lili", "Lala", Sexo.MASCULINO);
        Amigo lulu = new Amigo("Lala", "Lele", Sexo.FEMININO);
        assertFalse(lala.equals(lele));
        assertTrue(lala.equals(lulu));
        assertFalse(lulu.equals(lili));
        lili.setNome("Lala");
        lili.setSobrenome("Lele");
        lili.setSexo(Sexo.FEMININO);
        assertTrue(lulu.equals(lili));
        assertTrue(lala.equals(lulu));
        assertTrue(lala.equals(lili));
    }
}
