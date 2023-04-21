package gov.nasa.luv;

import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JOptionPane;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import gov.nasa.luv.SocketWrangler;
import gov.nasa.luv.DispatchHandler;

/** Represents one client connection to the LUV server. */
public class LuvSocketWrangler implements SocketWrangler {

    LuvSocketWrangler() {
    }

    public void wrangle(Socket s) {
        InputStream is;
        try {
            is = s.getInputStream();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Luv.getLuv(), "Error initializing socket.  See debug window for details.", "Internal Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        XMLReader parser;
        try {
            parser = XMLReaderFactory.createXMLReader();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Luv.getLuv(), "Error initializing XML reader.  See debug window for details.", "Internal Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }
        DispatchHandler dh = new DispatchHandler();
        parser.setContentHandler(dh);
        InputSource src = new InputSource(is);
        while (true) {
            try {
                parser.parse(src);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Luv.getLuv(), "Error parsing XML message.  See debug window for details.", "Parse Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
