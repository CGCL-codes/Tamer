package teste.simplify.view.managedbean;

import teste.simplify.bean.Bairro;
import br.gov.component.demoiselle.crud.annotation.CrudPaged;
import br.gov.component.demoiselle.crud.supercrud.SuperCrudMB;

@SuppressWarnings("serial")
@CrudPaged(title = "Cadastro de Bairros", view = "bairro_crud")
public class BairroMB extends SuperCrudMB<Bairro> {
}
