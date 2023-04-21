package at.riemers.zero.base.util;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.LocalizedResourceHelper;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 *
 * @author tobias
 */
public abstract class AbstractCsvView extends AbstractView {

    private static final String CONTENT_TYPE = "application/vnd.ms-excel";

    private static final String EXTENSION = ".csv";

    private String fileName;

    public AbstractCsvView() {
        setContentType(CONTENT_TYPE);
    }

    public AbstractCsvView(String fileName) {
        setContentType(CONTENT_TYPE);
        this.fileName = fileName;
    }

    /**
	 * Renders the Csv view, given the specified model.
	 */
    protected final void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CsvDocument doc = new CsvDocument();
        buildCsvDocument(model, doc, request, response);
        response.setContentType(getContentType());
        response.setCharacterEncoding("UTF-8");
        if (fileName != null) {
            response.setHeader("Content-disposition", "inline; filename=" + fileName);
        }
        PrintWriter out = response.getWriter();
        doc.write(out);
        out.flush();
    }

    protected abstract void buildCsvDocument(Map model, CsvDocument doc, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
