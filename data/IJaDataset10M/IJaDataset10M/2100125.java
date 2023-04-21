package br.gov.serpro.lab.estacionamento.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.NextView;
import br.gov.frameworkdemoiselle.annotation.PreviousView;
import br.gov.frameworkdemoiselle.report.Report;
import br.gov.frameworkdemoiselle.report.Type;
import br.gov.frameworkdemoiselle.report.annotation.Path;
import br.gov.frameworkdemoiselle.stereotype.ViewController;
import br.gov.frameworkdemoiselle.template.AbstractListPageBean;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Faces;
import br.gov.frameworkdemoiselle.util.FileRenderer;
import br.gov.serpro.lab.estacionamento.business.ClienteBC;
import br.gov.serpro.lab.estacionamento.domain.Cliente;

@ViewController
@NextView("/cliente_edit.xhtml")
@PreviousView("/cliente_list.xhtml")
public class ClienteListMB extends AbstractListPageBean<Cliente, Long> {

    private static final long serialVersionUID = 1L;

    @Inject
    @Path("reports/clientes.jrxml")
    private Report relatorio;

    @Inject
    private FileRenderer renderer;

    @Inject
    private ClienteBC clienteBC;

    @Transactional
    public String deleteSelection() {
        boolean delete;
        for (Iterator<Long> iter = getSelection().keySet().iterator(); iter.hasNext(); ) {
            Long id = iter.next();
            delete = getSelection().get(id);
            if (delete) {
                this.clienteBC.delete(id);
                iter.remove();
            }
        }
        return getPreviousView();
    }

    public String exibirRelatorioGeral() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("titulo", "Relatório de Clientes");
        try {
            byte[] buffer = this.relatorio.export(getResultList(), param, Type.PDF);
            this.renderer.render(buffer, FileRenderer.ContentType.PDF, "relatorio.pdf");
        } catch (Exception e) {
            Faces.addMessage(e);
        }
        return getNextView();
    }

    @Override
    protected List<Cliente> handleResultList() {
        return this.clienteBC.findAll();
    }
}
