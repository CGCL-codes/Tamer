package net.sourceforge.kwaai.markup.view;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.kwaai.element.IElement;
import net.sourceforge.kwaai.markup.AbstractContainer;
import net.sourceforge.kwaai.markup.container.Panel;
import net.sourceforge.kwaai.util.writer.KwaaiServletUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.AbstractView;

public class KwaaiView extends AbstractView {

    private String viewTemplate;

    private Locale viewLocale;

    public KwaaiView(final String viewName, final Locale locale) {
        viewTemplate = viewName;
        viewLocale = locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void renderMergedOutputModel(final Map model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        AbstractContainer root = new Panel() {

            @Override
            public String getTemplate() {
                return viewTemplate;
            }
        };
        for (Object element : model.values()) {
            if (element instanceof IElement) {
                root.add((IElement) element);
            }
        }
        MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        KwaaiServletUtils.writeMarkup(response.getWriter(), root.render(messageSource, viewLocale).toString());
    }
}
