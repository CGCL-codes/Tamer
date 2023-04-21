package orimage.doc.doodle.ope;

import orimage.doc.doodle.Parser;
import java.io.IOException;
import orimage.doc.doodle.ParseException;
import java.io.PrintWriter;
import java.util.Collection;

/**
* Parametres d'appel d'un op�rateur
*
* @author
* @version
*/
public class ArchParams extends ArchArguments {

    public ArchParams() {
        super('(', ')');
    }

    public ArchParams(Parser parser) throws IOException, ParseException {
        super('(', ')');
        lire(parser);
    }

    public void lire(Parser parser) throws IOException, ParseException {
        super.lire(parser);
    }

    public void ecrire(PrintWriter out) {
        super.ecrire(out);
    }
}
