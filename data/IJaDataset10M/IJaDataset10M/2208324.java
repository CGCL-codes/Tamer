package com.liferay.portlet.cszsearch.util;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.util.WebCacheable;
import com.liferay.portlet.cszsearch.model.CSZAddress;
import com.liferay.util.ConverterException;
import com.liferay.util.Html;
import com.liferay.util.Http;
import com.liferay.util.HttpUtil;
import com.liferay.util.Time;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="CityStateConverter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CityStateConverter implements WebCacheable {

    public CityStateConverter(String cityAndState) {
        _cityAndState = cityAndState;
    }

    public Object convert(String id) throws ConverterException {
        List list = new ArrayList();
        String cityAndState = _cityAndState;
        String city = null;
        String state = null;
        try {
            int pos = cityAndState.indexOf(StringPool.COMMA);
            city = cityAndState.substring(0, pos);
            state = cityAndState.substring(pos + 1, cityAndState.length()).trim();
        } catch (Exception e) {
            return list;
        }
        try {
            String text = Http.URLtoString("http://zip4.usps.com/zip4/zcl_1_results.jsp?pagenumber=all" + "&city=" + HttpUtil.encodeURL(city) + "&state=" + HttpUtil.encodeURL(state));
            int x = text.indexOf("<!-- **");
            int y = text.lastIndexOf("<!-- **");
            BufferedReader br = new BufferedReader(new StringReader(Html.stripHtml(text.substring(x, y))));
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.equals("")) {
                    if (Validator.isNumber(line)) {
                        list.add(new CSZAddress(null, city, state, line));
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            throw new ConverterException(e);
        }
        return list;
    }

    public long getRefreshTime() {
        return _REFRESH_TIME;
    }

    private static final long _REFRESH_TIME = Time.DAY * 90;

    private String _cityAndState;
}
