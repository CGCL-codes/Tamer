package org.t18n.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.t18n.analyzer.ContentAnalyzer;
import org.t18n.translator.Translator;

public class T18nResponse extends T18nAbstractResponseWrapper {

    public T18nResponse(HttpServletResponse response, ContentAnalyzer analyzer) {
        super(response, analyzer);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        ServletOutputStream stream = response.getOutputStream();
        T18nOutputStream wrapper = new T18nOutputStream(stream, analyzer);
        return wrapper;
    }

    public PrintWriter getWriter() throws IOException {
        PrintWriter writer = response.getWriter();
        T18nWriter t18nWriter = new T18nWriter(writer, analyzer);
        return t18nWriter;
    }
}
