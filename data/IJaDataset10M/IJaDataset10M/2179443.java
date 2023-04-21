package br.com.edawir.controlador.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import br.com.edawir.integracao.model.AgendaDisciplina;

/**
 * Classe que converte Objeto para String ou String para Objeto do/a Agenda Disciplina.
 * Produz uma representa��o de String dos objetos que � adequada para o processamento
 * das informa��es
 * 
 * @author Grupo EDAWIR
 * @since 24/11/2011
 */
@FacesConverter(forClass = AgendaDisciplina.class)
public class AgendaDisciplinaConverter implements Converter {

    /**
	 * Realiza a convers�o de uma String para um Objeto.
	 * 
	 * @param context o contexto do Faces
	 * @param component o componente da interface do usu�rio
	 * @param value o valor do tipo String
	 * 
	 * @return agendaDisciplina o objeto agendaDisciplina
	 */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        AgendaDisciplina agendaDisciplina = new AgendaDisciplina();
        agendaDisciplina.setId(new Long(value));
        return agendaDisciplina;
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
        return ((AgendaDisciplina) value).getId().toString();
    }
}
