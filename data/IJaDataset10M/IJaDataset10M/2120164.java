package teste.simplify.view.tag;

import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import org.ajax4jsf.renderkit.ComponentVariables;
import org.richfaces.component.UITabPanel;
import org.richfaces.renderkit.html.TabPanelRenderer;
import br.gov.component.demoiselle.jsf.constants.HtmlWords;

public class ViewSourceRenderer extends TabPanelRenderer {

    private String sourceFile;

    @Override
    public void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) {
        try {
            ViewSource viewSource = (ViewSource) component;
            if (!viewSource.isVisible()) return;
            writer.startElement(HtmlWords.A, component);
            writer.writeAttribute(HtmlWords.HREF, "#", null);
            writer.writeAttribute(HtmlWords.ID, "link_viewsource", null);
            String idViewSource = viewSource.getId();
            String onclick = "if ( document.getElementById('" + idViewSource + "').style.display == 'none' ) { document.getElementById('" + idViewSource + "').style.display = ''; } else " + "{ document.getElementById('" + idViewSource + "').style.display = 'none'; }";
            writer.writeAttribute(HtmlWords.ONCLICK, onclick, null);
            writer.write("Visualizar/Esconder Código");
            writer.endElement(HtmlWords.A);
            super.doEncodeBegin(writer, context, component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UITabPanel component, ComponentVariables variables) throws IOException {
        super.doEncodeEnd(writer, context, component, variables);
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getSourceFile() {
        return sourceFile;
    }
}
