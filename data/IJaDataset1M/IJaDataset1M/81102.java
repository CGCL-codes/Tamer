package utoopia.content.producers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utoopia.content.html.BreakLine;
import utoopia.content.html.HeaderOne;
import utoopia.content.html.HyperLink;
import utoopia.content.html.Image;
import utoopia.content.html.TextElement;
import utoopia.content.html.forms.Edit;
import utoopia.content.html.pages.EntityPage;
import utoopia.models.EntityModel;
import utoopia.models.readers.ApplicationReader;
import utoopia.models.readers.SolutionReader;

/**
 * Productor de páginas de menú de fichero (en otras palabras, menú de entidad)
 * @author Jose
 *
 */
public class FileMenuPageProducer extends PageProducer {

    @Override
    public String doProduceContent(HttpServletRequest request, HttpServletResponse response) {
        String application = request.getParameter("application");
        String entityName = request.getParameter("entity");
        ApplicationReader reader = new ApplicationReader(application);
        EntityModel entityModel = reader.getEntityModel(entityName);
        if (entityModel != null) {
            EntityPage page = new EntityPage(request);
            page.getBody().addElement(new HeaderOne(entityModel.getLongName()));
            String url = EntityFormPageProducer.getAddEntityURL(entityModel);
            page.getBody().addElement(new HyperLink(url, new TextElement("Añadir " + entityModel.getTextOne() + " " + entityModel.getSingularName())));
            page.getBody().addElement(new BreakLine());
            page.getBody().addElement(new HyperLink(EntityListPageProducer.getURL(application, entityModel, "", true), new TextElement("Ver " + entityModel.getTextAll() + " " + entityModel.getTextPluralThe() + " " + entityModel.getPluralName())));
            page.getBody().addElement(new BreakLine());
            page.getBody().addElement(new TextElement("Buscar " + entityModel.getPluralName() + ": "));
            page.getBody().addElement(new Edit("search_edit", ""));
            Image searchImage = new Image(Image.getURL("/utoopia/content/images/search.png"), "javascript:searchValue=window.document.getElementById('search_edit').value;location.href='" + EntityListPageProducer.getURL(application, entityModel, "", true, "' + searchValue") + ";");
            searchImage.setWidth(22);
            searchImage.setHeight(22);
            searchImage.setAlign("top");
            page.getBody().addElement(searchImage);
            return page.toString();
        } else {
            return "";
        }
    }

    /**
	 * Devuelve la url del productor de páginas de menú de fichero
	 * @param application Nombre de la aplicación
	 * @param entityModel Nombre del modelo de la entidad
	 * @return Devuelve la url completa
	 */
    public static String getURL(String application, EntityModel entityModel) {
        return SolutionReader.getSolutionURL() + "?application=" + application + "&action=file&entity=" + entityModel.getName();
    }
}
