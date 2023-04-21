package com.inet.qlcbcc.converter;

import org.json.JSONException;
import org.json.JSONObject;
import org.webos.core.json.convert.ObjectToJsonObjectConverter;
import com.inet.qlcbcc.domain.SocialInsurance;

/**
 * SocialInsuranceToJsonConverter.
 *
 * @author Thoang Tran
 * @version $Id: SocialInsuranceToJsonConverter.java Dec 1, 2011 12:40:53 AM thoangtd $
 *
 * @since 1.0
 */
public class SocialInsuranceToJsonConverter implements ObjectToJsonObjectConverter<SocialInsurance> {

    /**
   * @see org.webos.core.convert.converter.Converter#convert(java.lang.Object)
   */
    public JSONObject convert(SocialInsurance source) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("id", source.getId());
            jsonObject.accumulate("ver", source.getVersion());
            jsonObject.accumulate("number", source.getSocialInsuranceNumber());
            return jsonObject;
        } catch (JSONException ex) {
            throw new IllegalStateException("Could not converter to socialInsurance json", ex);
        } catch (Exception ex) {
            throw new IllegalStateException("Could not converter to socialInsurance json", ex);
        }
    }
}
