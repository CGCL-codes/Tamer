package br.ufrgs.inf.biblioteca.view.managedbean;

import java.util.ArrayList;
import java.util.List;
import br.gov.framework.demoiselle.core.exception.ApplicationRuntimeException;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.core.message.IMessage;
import br.gov.framework.demoiselle.view.faces.controller.AbstractManagedBean;
import br.gov.framework.demoiselle.view.faces.util.ManagedBeanUtil;
import br.ufrgs.inf.biblioteca.bean.Autor;
import br.ufrgs.inf.biblioteca.business.IAutorBC;

public class AutorMB extends AbstractManagedBean {

    private Autor autor = new Autor();

    private List<Autor> autores = new ArrayList<Autor>();

    public AutorMB() {
        listar();
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    @Injection
    private IAutorBC autorBC;

    public String preInserir() {
        this.autor = new Autor();
        listar();
        return "autor";
    }

    public String preAtualizar() {
        listar();
        return "autor";
    }

    public void salvar() {
        try {
            if (autor.getId() == null) {
                autorBC.insert(autor);
            } else {
                autorBC.update(autor);
            }
            if (!autores.contains(autor)) {
                autores.add(autor);
            }
            autor = new Autor();
        } catch (ApplicationRuntimeException e) {
            ManagedBeanUtil.addMessage(e.getObjectMessage());
        }
        for (IMessage msg : messageContext.getMessages()) {
            ManagedBeanUtil.addMessage(msg);
        }
        messageContext.clear();
    }

    public void excluir() {
        try {
            autores.remove(autor);
            autor = autorBC.findById(autor.getId());
            autorBC.remove(autor);
            autores.add(autor);
        } catch (ApplicationRuntimeException e) {
            ManagedBeanUtil.addMessage(e.getObjectMessage());
        }
        for (IMessage msg : messageContext.getMessages()) {
            ManagedBeanUtil.addMessage(msg);
        }
        messageContext.clear();
    }

    public void listar() {
        try {
            autores = new ArrayList<Autor>(autorBC.list());
        } catch (ApplicationRuntimeException e) {
            ManagedBeanUtil.addMessage(e.getObjectMessage());
        }
        for (IMessage msg : messageContext.getMessages()) {
            ManagedBeanUtil.addMessage(msg);
        }
        messageContext.clear();
    }
}
