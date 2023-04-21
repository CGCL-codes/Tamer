package shiva.domain.validation.logic.impl;

import java.util.Map;
import shiva.domain.exception.validation.InvalidAttributeValueException;
import shiva.domain.validation.logic.ValidationClass;
import shiva.util.Utils;

/**
 * @author Paulo Vitor
 * @author Roberto Su
 * 
 * @description
 *
 */
public class CnpjValidation implements ValidationClass {

    @Override
    public void validate(Map<String, Object> parameters, Object attributeValue, String attributeName) throws InvalidAttributeValueException {
        if (attributeValue == null) {
            throw new InvalidAttributeValueException(Utils.retrieveMessage("validator.invalid.notnull", new Object[] { attributeName }));
        }
        String strValue = (String) attributeValue;
        strValue = strValue.replaceAll("[^0-9]", "");
        if (strValue.length() == 0) {
            throw new InvalidAttributeValueException(Utils.retrieveMessage("validator.invalid.notempty", new Object[] { attributeName }));
        }
        if (!this.isCNPJ(strValue)) {
            throw new InvalidAttributeValueException(Utils.retrieveMessage("validator.invalid.cnpj", new Object[] { attributeName }));
        }
    }

    /**
	 * @param strCnpj
	 * @return
	 */
    private boolean isCNPJ(String strCnpj) {
        if (strCnpj.length() != 14) return false;
        int soma = 0, dig;
        String cnpj_calc = strCnpj.substring(0, 12);
        char[] chr_cnpj = strCnpj.toCharArray();
        for (int i = 0; i < 4; i++) if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
        for (int i = 0; i < 8; i++) if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);
        soma = 0;
        for (int i = 0; i < 5; i++) if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
        for (int i = 0; i < 8; i++) if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);
        return strCnpj.equals(cnpj_calc);
    }
}
