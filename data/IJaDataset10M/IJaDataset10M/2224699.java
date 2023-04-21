package de.erdesignerng.visual.java2d;

import de.erdesignerng.model.Model;
import de.erdesignerng.model.ModelIOUtilities;
import de.erdesignerng.model.Table;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class TestFrame extends JFrame {

    private Model model;

    private Java2DEditor editor;

    public TestFrame() throws ParserConfigurationException, IOException, SAXException {
        InputStream theStream = new FileInputStream("D:\\Temp\\Capitastra_6_5_0.mxm");
        model = ModelIOUtilities.getInstance().deserializeModelFromXML(theStream);
        editor = new Java2DEditor();
        editor.setModel(model);
        setContentPane(editor.getDetailComponent());
        Table theTable = model.getTables().findByName("CAPITIMEPOINT");
        editor.setSelectedObject(theTable);
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        TestFrame theFrame = new TestFrame();
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setSize(800, 600);
        theFrame.setVisible(true);
    }
}
