package com.kescom.matrix.core.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.freebss.sprout.banner.util.StreamUtils;
import com.kescom.matrix.core.access.AccessGate;
import com.kescom.matrix.core.alias.AliasUtils;
import com.kescom.matrix.core.chart.ChartServlet;
import com.kescom.matrix.core.env.IMatrixEnvironment;
import com.kescom.matrix.core.env.MatrixContext;
import com.kescom.matrix.core.format.CSVSeriesFormat;
import com.kescom.matrix.core.format.ISeriesFormat;
import com.kescom.matrix.core.format.XMLSeriesFormat;
import com.kescom.matrix.core.i18n.MessageBankUtils;
import com.kescom.matrix.core.i18n.TranslationUtils;
import com.kescom.matrix.core.props.EntityPropUtils;
import com.kescom.matrix.core.series.ISeries;
import com.kescom.matrix.core.series.Series;
import com.kescom.matrix.core.session.MatrixSession;
import com.kescom.matrix.core.user.IReportingEntity;
import com.kescom.matrix.core.user.ISeriesList;
import com.kescom.matrix.core.user.IUser;
import com.kescom.matrix.core.user.SeriesList;
import com.kescom.matrix.core.user.UserUtils;

public class SeriesImportApiVerbHandler extends SeriesInfoApiVerbHandler {

    public ApiDict process(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        ApiDict dict = new ApiDict();
        ISeries series = getRequestSeries(request, response);
        dict.put("status", series != null ? "ok" : "ko");
        if (series != null) {
            TimeZone timeZone = (TimeZone) MatrixContext.getThreadUserdata("TimeZone");
            if (timeZone == null) timeZone = TimeZone.getDefault();
            ISeriesFormat format;
            String ifmt = request.getParameter("if");
            if ("csv".equals(ifmt)) format = new CSVSeriesFormat(timeZone); else format = new XMLSeriesFormat(timeZone);
            String ip = request.getParameter("ip");
            int policy;
            if ("replace".equals(ip)) policy = ISeriesFormat.POLICY_REPLACE; else if ("keep".equals(ip)) policy = ISeriesFormat.POLICY_KEEP; else if ("append".equals(ip)) policy = ISeriesFormat.POLICY_APPEND; else if ("merge".equals(ip)) policy = ISeriesFormat.POLICY_MERGE; else policy = ISeriesFormat.POLICY_CLEAR;
            try {
                format.parse(series, request.getInputStream(), policy);
            } catch (ParseException e) {
                dict.put("status", "ko");
                dict.put("reason", e.getMessage());
                return dict;
            }
        }
        return dict;
    }
}
