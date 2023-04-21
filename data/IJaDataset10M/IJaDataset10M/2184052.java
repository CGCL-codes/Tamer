package br.com.viisi.bean;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.jboss.seam.faces.validation.InputField;

@FacesValidator("testeValidator")
public class TesteValidator implements Validator {

    @Inject
    @InputField("teste")
    private String nome;

    @Override
    public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
        if (nome == null || nome.isEmpty() || nome.length() <= 2) {
            throw new ValidatorException(new FacesMessage("O campo precisa ter pelo menos 3 caracteres"));
        }
    }
}
