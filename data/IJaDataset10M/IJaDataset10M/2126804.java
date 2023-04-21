package br.org.databasetools.core.validator;

import java.util.Date;
import br.org.databasetools.core.ApplicationContext;
import br.org.databasetools.core.table.Bean;
import br.org.databasetools.core.util.BeanProvider;

public class DataAtualValidator extends ValidatorAbstract {

    private static Date hoje;

    private Bean bean;

    private String property;

    public DataAtualValidator(Bean bean, String property) {
        hoje = ApplicationContext.getDataHoraAberturaSistema();
        this.bean = bean;
        this.property = property;
    }

    public boolean isValid() {
        super.isValid();
        boolean result = true;
        Object data = BeanProvider.getProperty(bean, property);
        try {
            if (((Date) data).equals(hoje) == false) {
                addMessage(1, "A data informada deve ser a data atual!");
                result = false;
            }
        } catch (Exception e) {
            addMessage(3, "A valida��o n�o pode ser realizada. Erro de programa��o: " + e.getMessage());
            result = false;
        }
        return result;
    }
}
