package view;

import model.Register;

public class RegisterViewDecimal extends RegisterView {

    private static final String COLUMN_NAME = "Decimal Value";

    @Override
    public String getColumnName() {
        return COLUMN_NAME;
    }

    @Override
    public String convertValue(Register value) {
        return String.valueOf(value.getValue());
    }
}
