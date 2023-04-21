package br.gov.serpro.lab.estacionamento.view;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractEditPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.serpro.lab.estacionamento.business.EstacionamentoBC;
import br.gov.serpro.lab.estacionamento.domain.Estacionamento;
import br.gov.serpro.lab.estacionamento.domain.Patio;

@ViewController
@PreviousView("/estacionamento_list.xhtml")
public class EstacionamentoEditMB extends AbstractEditPageBean<Estacionamento, Long> {

    private static final long serialVersionUID = 1L;

    private DataModel<Patio> patios;

    @Inject
    private EstacionamentoBC estacionamentoBC;

    @Override
    @Transactional
    public String delete() {
        this.estacionamentoBC.delete(getId());
        return getPreviousView();
    }

    @Override
    @Transactional
    public String insert() {
        this.estacionamentoBC.insert(getBean());
        return getPreviousView();
    }

    @Override
    @Transactional
    public String update() {
        this.estacionamentoBC.update(getBean());
        return getPreviousView();
    }

    @Override
    protected void handleLoad() {
        setBean(this.estacionamentoBC.load(getId()));
    }

    public void addPatio() {
        getBean().addPatio(new Patio());
    }

    public void deletePatio() {
        getBean().getPatios().remove(getPatios().getRowData());
    }

    public DataModel<Patio> getPatios() {
        if (patios == null) {
            patios = new ListDataModel<Patio>(getBean().getPatios());
        }
        return patios;
    }
}
