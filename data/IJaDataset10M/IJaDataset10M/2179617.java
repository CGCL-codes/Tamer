package documentation;

import org.sourceforge.xazzle.xhtml.HtmlTag;
import static documentation.ArgoPage.*;
import static org.sourceforge.xazzle.xhtml.Href.href;
import static org.sourceforge.xazzle.xhtml.Tags.*;

final class IndexPage {

    private IndexPage() {
    }

    @SuppressWarnings({ "StaticMethodOnlyUsedInOneClass" })
    static HtmlTag indexPage(final String version) {
        return anArgoPage(h2Tag(xhtmlText("Introduction")), paragraphTag(xhtmlText("Argo is a JSON parser and generator for Java. It offers three parse interfaces - a push parser, a " + "pull parser, and a DOM style parser. It is written to be easy to use, typesafe, and fast. It is open source, and " + "free for you to use.")), paragraphTag(xhtmlText("The latest version of Argo available for download is "), anchorTag(xhtmlText(version)).withHref(href("https://sourceforge.net/projects/argo/files/latest")), xhtmlText(".  The "), anchorTag(xhtmlText("javadoc")).withHref(href("javadoc/")), xhtmlText(" is also available online.")), h2Tag(xhtmlText("Example")), paragraphTag(xhtmlText("A brief example demonstrates the DOM style parser. The example is based on the following JSON, " + "which is assumed to be available in a "), simpleNameOf(String.class), xhtmlText(" called "), variableName("jsonText"), xhtmlText(".")), codeBlock("{\n" + "    \"name\": \"Black Lace\",\n" + "    \"sales\": 110921,\n" + "    \"totalRoyalties\": 10223.82,\n" + "    \"singles\": [\n" + "        \"Superman\", \"Agadoo\"\n" + "    ]\n" + "}"), paragraphTag(xhtmlText("We can use Argo to get the second single like this:")), codeBlock("String secondSingle = new JdomParser().parse(jsonText)\n" + "    .getStringValue(\"singles\", 1);"), paragraphTag(xhtmlText("On the first line, we parse the JSON text into an object hierarchy. There are various options for " + "navigating this, but the simplest is just to give the path and type of node we expect.")), paragraphTag(xhtmlText("On line two, we ask for the JSON string at index 1 of the array in the "), variableName("singles"), xhtmlText(" field.")), paragraphTag(xhtmlText("If we check the "), variableName("secondSingle"), xhtmlText(" variable, we will find, as expected, it contains the "), simpleNameOf(String.class), xhtmlText(" \""), codeTag(xhtmlText("Agadoo")), xhtmlText("\".")));
    }
}
