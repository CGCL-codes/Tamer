package au.edu.uq.itee.maenad.owlviewer;

import java.awt.GridLayout;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import prefuse.data.io.DataIOException;
import au.edu.uq.itee.maenad.owlviewer.view.OwlView;

public class OwlViewerApplet extends JApplet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
        try {
            javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    createGUI();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't successfully complete");
        }
    }

    private void createGUI() {
        final String documentUrl = getParameter("documentUrl");
        try {
            getContentPane().add(new OwlView(documentUrl));
        } catch (DataIOException ex) {
            setContentPane(new JPanel(new GridLayout(2, 1, 20, 20)));
            getContentPane().add(new JLabel("Error trying to read ontology from " + documentUrl));
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("<html>");
            Throwable current = ex;
            while (current != null) {
                messageBuilder.append(current.toString() + "<p>");
                current = current.getCause();
            }
            getContentPane().add(new JLabel(messageBuilder.toString()));
            ex.printStackTrace();
        }
    }

    @Override
    public String[][] getParameterInfo() {
        return new String[][] { { "documentUrl", "string", "Sets the URL for the document to display" } };
    }
}
