package tufts.vue;

import tufts.google.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.Vector;
import java.util.Iterator;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import java.io.OutputStreamWriter;
import org.xml.sax.InputSource;

/**
 *
 * @author  rsaigal
 */
public class SearchPanel extends JPanel {

    private static String searchURL;

    private static java.util.prefs.Preferences prefs;

    private static String XML_MAPPING;

    private static String query;

    private static int NResults = 10;

    private static String result = "";

    private static URL url;

    public SearchPanel() {
        searchURL = VueResources.getString("url.google");
        XML_MAPPING = VueResources.getString("mapping.google");
        this.setLayout(new BorderLayout());
        JPanel queryPanel = new JPanel();
        final JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        queryPanel.setLayout(new BorderLayout());
        final JTextField queryBox = new JTextField();
        queryPanel.add(queryBox, BorderLayout.NORTH);
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(100, 30));
        queryPanel.add(searchButton, BorderLayout.SOUTH);
        this.add(queryPanel, BorderLayout.NORTH);
        this.add(resultPanel, BorderLayout.CENTER);
        searchButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int index = 0;
                JScrollPane jsp = new JScrollPane();
                String searchString = queryBox.getText();
                if (!searchString.equals("")) {
                    try {
                        url = new URL(searchURL + searchString);
                        InputStream input = url.openStream();
                        int c;
                        while ((c = input.read()) != -1) {
                            result = result + (char) c;
                        }
                        FileWriter fileWriter = new FileWriter("google_result.xml");
                        fileWriter.write(result);
                        fileWriter.close();
                        result = "";
                        GSP gsp = loadGSP("google_result.xml");
                        Iterator i = gsp.getRES().getResultList().iterator();
                        Vector resultVector = new Vector();
                        while (i.hasNext()) {
                            Result r = (Result) i.next();
                            resultVector.add(r.getUrl());
                            System.out.println(r.getTitle() + " " + r.getUrl());
                        }
                        VueDragTree tree = new VueDragTree(resultVector.iterator(), "GoogleSearchResults");
                        tree.setEditable(true);
                        tree.setRootVisible(false);
                        JTextField ja = new JTextField("Google Search Results");
                        jsp = new JScrollPane(tree);
                        resultPanel.add(ja, BorderLayout.NORTH);
                        resultPanel.add(jsp, BorderLayout.CENTER, index);
                        index = index++;
                        resultPanel.revalidate();
                    } catch (Exception ex) {
                    }
                }
            }
        });
    }

    private static GSP loadGSP(String filename) {
        try {
            Unmarshaller unmarshaller = getUnmarshaller();
            unmarshaller.setValidation(false);
            GSP gsp = (GSP) unmarshaller.unmarshal(new InputSource(new FileReader(filename)));
            return gsp;
        } catch (Exception e) {
            System.out.println("loadGSP[" + filename + "]: " + e);
            e.printStackTrace();
            return null;
        }
    }

    private static GSP loadGSP(URL url) {
        try {
            InputStream input = url.openStream();
            int c;
            while ((c = input.read()) != -1) {
                result = result + (char) c;
            }
            Unmarshaller unmarshaller = getUnmarshaller();
            unmarshaller.setValidation(false);
            GSP gsp = (GSP) unmarshaller.unmarshal(new InputSource());
            return gsp;
        } catch (Exception e) {
            System.out.println("loadGSP " + e);
            e.printStackTrace();
            return null;
        }
    }

    private static Unmarshaller unmarshaller = null;

    private static Unmarshaller getUnmarshaller() {
        if (unmarshaller == null) {
            unmarshaller = new Unmarshaller();
            Mapping mapping = new Mapping();
            try {
                mapping.loadMapping(XML_MAPPING);
                unmarshaller.setMapping(mapping);
            } catch (Exception e) {
                System.out.println("getUnmarshaller: " + XML_MAPPING + e);
            }
        }
        return unmarshaller;
    }
}
