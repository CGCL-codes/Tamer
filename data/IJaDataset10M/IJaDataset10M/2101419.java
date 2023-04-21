package softwareengineering;

import java.net.URL;
import java.util.List;
import softwareengineering.parser.Node;
import softwareengineering.parser.Parser;
import softwareengineering.renderer.Page;
import softwareengineering.renderer.Renderer;
import softwareengineering.tokeniser.Token;
import softwareengineering.tokeniser.Tokeniser;

/**
 * Class that provides a facade between the GUI and the rest of the parsing
 * operations.
 */
public class Controller {

    Renderer renderer;

    Node root;

    /**
     * Construct a new instance of the Controller class.
     */
    public Controller() {
        renderer = new Renderer();
    }

    /**
     * Render the previously opened and parsed html document, returning a Page
     * object that contains the rendered image, a list of link hotspots, and
     * the page title.
     *
     * @param windowWidth   The desired width of the rendering in pixels
     * @param context       The file from which all contained images should be
     *                      open relative to.
     * @return              The rendered Page object
     */
    public Page render(int windowWidth, URL context) {
        return renderer.draw(root, windowWidth, context);
    }

    /**
     * Load the html file at the given url. This should be followed by a call to
     * the render() to complete the process.
     *
     * @param url   The html file url or path
     * @throws java.lang.Exception  If a file I/O error occurs.
     */
    public void load(URL url) throws Exception {
        String contents = FileIO.readUrl(url);
        Tokeniser tokeniser = new Tokeniser(contents);
        List<Token> tokens = tokeniser.createTokens();
        Parser parser = new Parser(tokens);
        root = parser.parseDocument();
    }
}
