package net.woodstock.rockapi.jsf_test.test;

import java.util.Collection;
import net.woodstock.rockapi.jsf_test.entidade.Pessoa;
import net.woodstock.rockapi.service.generic.GenericService;

public class TestPessoa extends TestSpring {

    public void testSalvar() throws Exception {
        GenericService service = this.getObject(GenericService.class);
        Pessoa p = new Pessoa();
        service.create(p);
        System.out.println(p.getId() + " => " + p.getNome());
    }

    public void xtestPesquisar() throws Exception {
        GenericService service = this.getObject(GenericService.class);
        Collection<Pessoa> pessoas = service.query(null);
        for (Pessoa pessoa : pessoas) {
            System.out.println(pessoa.getNome());
        }
    }
}
