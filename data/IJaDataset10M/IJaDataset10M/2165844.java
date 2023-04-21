package br.com.edawir.controlador.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import br.com.edawir.integracao.model.TipoDeInvestimento;

/**
 * Classe que converte Objeto para String ou String para Objeto do/a Tipo De Investimento.
 * Produz uma representa��o de String dos objetos que � adequada para o processamento
 * das informa��es
 * 
 * @author Grupo EDAWIR
 * @since 03/11/2011
 */
@FacesConverter(forClass = TipoDeInvestimento.class)
public class TipoDeInvestimentoConverter implements Converter {

    /**
	 * Realiza a convers�o de uma String para um Objeto.
	 * 
	 * @param context o contexto do Faces
	 * @param component o componente da interface do usu�rio
	 * @param value o valor do tipo String
	 * 
	 * @return tipoDeInvestimento o objeto tipoDeInvestimento
	 */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        TipoDeInvestimento tipoDeInvestimento = new TipoDeInvestimento();
        tipoDeInvestimento.setId(new Long(value));
        return tipoDeInvestimento;
    }

    /**
	 * Realiza a convers�o de um Objeto para uma String.
	 * 
	 * @param context o contexto do Faces
	 * @param component o componente da interface do usu�rio
	 * @param value o valor do tipo Objeto
	 * 
	 * @return id o id do objeto convertido para String
	 */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((TipoDeInvestimento) value).getId().toString();
    }
}
