package br.unb.cic.gerval.server.bo;

import org.springframework.test.AbstractTransactionalSpringContextTests;
import br.unb.cic.gerval.client.NegocioException;
import br.unb.cic.gerval.client.rpc.vo.Linha;

public class LinhaBOTest extends AbstractTransactionalSpringContextTests {

    protected SimplesBO linhaBO;

    protected Linha linha = null;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        linhaBO = (SimplesBO) applicationContext.getBean("linhaBO");
        linha = (Linha) applicationContext.getBean("linha");
    }

    protected String[] getConfigLocations() {
        return new String[] { "applicationContext.xml", "testes.xml" };
    }

    public void test() throws NegocioException {
        int numLinhas = linhaBO.getAll().size();
        linhaBO.saveOrUpdate(linha, null);
        assertEquals(numLinhas + 1, linhaBO.getAll().size());
        Linha linhaDoBanco = (Linha) linhaBO.get(linha.getId());
        assertEquals(linha, linhaDoBanco);
        assertEquals(linha.getNome(), linhaDoBanco.getNome());
        linhaDoBanco.setNome("nsdkfjsn");
        linhaBO.saveOrUpdate(linhaDoBanco, null);
        assertEquals(numLinhas + 1, linhaBO.getAll().size());
        linhaBO.delete(linhaDoBanco);
        assertEquals(numLinhas, linhaBO.getAll().size());
    }
}
