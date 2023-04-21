package utoopia.content.html.forms;

import utoopia.content.html.Div;
import utoopia.content.html.HyperLink;
import utoopia.content.html.Image;
import utoopia.content.html.TextElement;
import utoopia.content.html.javascript.Script;
import utoopia.models.readers.SolutionReader;

/**
 * Representa un componente html para mostrar un botón de logout
 * @author Jose
 *
 */
public class Logout extends Div {

    /**
	 * Constructor
	 */
    public Logout() {
        String url = SolutionReader.getSolutionURL() + "?" + "action=logout";
        addElement(new HyperLink(url, new TextElement("Finalizar Sesión")));
        addElement(new Image(Image.getURL("/utoopia/content/images/logout.png"), new Script("location.href='" + url + "';").toString()));
    }
}
