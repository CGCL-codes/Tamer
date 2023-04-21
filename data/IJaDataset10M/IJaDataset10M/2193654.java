package net.sf.fallfair.BusinessLogic.Validation;

import net.sf.fallfair.BusinessLogic.Province;

/**
 *
 * @author nathanj
 */
public class ProvinceValidation {

    public static void validate(Province aProvince) {
        validateProvinceId(aProvince.getProvinceId());
        validateProvinceCode(aProvince.getProvinceCode());
        validateName(aProvince.getName());
    }

    public static void validateProvinceId(int provinceId) {
    }

    public static void validateProvinceCode(String provinceCode) {
    }

    public static void validateName(String name) {
    }
}
