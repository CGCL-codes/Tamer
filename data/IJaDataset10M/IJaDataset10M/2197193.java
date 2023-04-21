package net.sf.jbob.core;

import junit.framework.TestCase;

public class TestModel extends TestCase {

    public void testSetData() {
        Model model = new ModelBob();
        Pessoa pessoa = new Pessoa();
        model.setData(pessoa);
        assertEquals(model.getData(), pessoa);
    }

    public void testWriteProperty() {
        Model model = new ModelBob();
        Pessoa pessoa = new Pessoa();
        model.setData(pessoa);
        model.writeProperty("nome", "Teste");
        assertEquals("Teste", pessoa.getNome());
    }

    public void testReadProperty() {
        Model model = new ModelBob();
        Pessoa pessoa = new Pessoa();
        model.setData(pessoa);
        pessoa.setNome("Teste");
        assertEquals("Teste", pessoa.getNome());
    }

    public void testUpdatePath() {
        Model model = new ModelBob();
        Pessoa pessoa = new Pessoa();
        model.setData(pessoa);
        ViewBob view = new ViewTest();
        BindingDescription bind = new BindingDescription(model, "nome", view, "value");
        view.setBindingDescription(bind);
        ControllerBob c = new ControllerBob();
        c.addListener(view);
        c.addListener(model);
        view.addListener(c);
        model.addListener(c);
        pessoa.setNome("Teste");
        model.update("nome");
        assertEquals("Teste", view.getValue());
    }

    public void testUpdate() {
        Model model = new ModelBob();
        Pessoa pessoa = new Pessoa();
        model.setData(pessoa);
        ViewBob view = new ViewTest();
        BindingDescription bind = new BindingDescription(model, "nome", view, "value");
        view.setBindingDescription(bind);
        ControllerBob c = new ControllerBob();
        c.addListener(view);
        c.addListener(model);
        view.addListener(c);
        model.addListener(c);
        pessoa.setNome("Teste");
        model.update();
        assertEquals("Teste", view.getValue());
    }
}
