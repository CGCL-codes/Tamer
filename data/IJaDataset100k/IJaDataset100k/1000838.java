package br.com.sse.validation;

import org.mentawai.filter.ValidationFilter;
import org.mentawai.rule.*;

public class ProdutoValidationFilter extends ValidationFilter {

    private static final int FIELD_REQUIRED_ERROR = 1;

    public void initValidator() {
        add("produto", new RequiredFieldRule(), FIELD_REQUIRED_ERROR);
        add("unidade", new RequiredFieldRule(), FIELD_REQUIRED_ERROR);
    }
}
