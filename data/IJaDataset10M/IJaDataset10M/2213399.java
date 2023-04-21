package br.gov.frameworkdemoiselle.sample.business;

import br.gov.framework.demoiselle.core.layer.IBusinessController;

public interface INotaBC extends IBusinessController {

    Integer processarNotas(String usuario);
}
