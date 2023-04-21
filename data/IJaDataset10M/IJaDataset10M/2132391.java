package mr3.actions;

import java.awt.event.*;
import java.io.*;
import mr3.*;
import com.hp.hpl.mesa.rdf.jena.common.*;
import com.hp.hpl.mesa.rdf.jena.model.*;

/**
 * @author takeshi morita
 *
 */
public class ConvertClass extends AbstractActionFile {

    public ConvertClass(MR3 mr3, String name) {
        super(mr3, name);
    }

    private void convertClassSRC(boolean isSelected) {
        Writer output = new StringWriter();
        try {
            Model model = null;
            if (isSelected) {
                model = mr3.getSelectedClassModel();
            } else {
                model = mr3.getClassModel();
            }
            RDFWriter writer = new RDFWriterFImpl().getWriter("RDF/XML-ABBREV");
            writeModel(model, output, writer);
        } catch (RDFException e) {
            e.printStackTrace();
        }
        mr3.getSourceArea().setText(output.toString());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("RDFS(Class)/XML")) {
            convertClassSRC(false);
        } else {
            convertClassSRC(true);
        }
        showSrcView();
    }
}
