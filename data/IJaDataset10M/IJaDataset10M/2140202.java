package org.xngr.service.soap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.soap.Envelope;
import org.apache.soap.SOAPException;
import org.apache.soap.rpc.SOAPContext;
import org.apache.soap.transport.SOAPTransport;
import org.apache.soap.transport.http.SOAPHTTPConnection;
import org.bounce.FormLayout;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This window is able to show a SOAP message and allows for sending the
 * message.
 * 
 * @version $Revision: 1.2 $, $Date: 2002/08/17 18:43:50 $
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class SoapWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 379033916508343144L;

    private static final Dimension SIZE = new Dimension(350, 400);

    private static Point lastPosition = new Point(100, 100);

    private Document document = null;

    private JButton closeButton = null;

    private JButton sendButton = null;

    private SOAPTransport transport = null;

    private SOAPContext context = null;

    private Envelope envelope = null;

    private Hashtable<String, String> headers = null;

    private JComboBox urlField = null;

    private JComboBox actionField = null;

    private JTextArea messageArea = null;

    private JTextArea responseArea = null;

    private AccessedItems targets = null;

    private AccessedItems actions = null;

    /**
	 * Creates the window to show the Address element.
	 */
    public SoapWindow(AccessedItems targets, AccessedItems actions) {
        setResizable(true);
        setIconImage(new ImageIcon(this.getClass().getResource("/org/xngr/service/soap/icons/SOAPEnvelopeIcon.gif")).getImage());
        setTitle("SOAP Message");
        this.targets = targets;
        this.actions = actions;
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel soapPanel = new JPanel(new BorderLayout());
        urlField = new JComboBox(targets.get().toArray());
        urlField.setEditable(true);
        urlField.setPreferredSize(new Dimension(100, 21));
        fillCombo(urlField, targets.get());
        actionField = new JComboBox(actions.get().toArray());
        actionField.setEditable(true);
        actionField.setPreferredSize(new Dimension(100, 21));
        fillCombo(actionField, actions.get());
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel messageLabel = new JLabel("Message");
        messageLabel.setFont(messageLabel.getFont().deriveFont((float) 15));
        messageLabel.setForeground(Color.gray);
        titlePanel.add(messageLabel, BorderLayout.WEST);
        titlePanel.add(sendButton, BorderLayout.EAST);
        titlePanel.setBorder(new EmptyBorder(0, 0, 2, 0));
        JPanel formPanel = new JPanel(new FormLayout(5, 3));
        formPanel.add(new JLabel("URL:"), FormLayout.LEFT);
        formPanel.add(urlField, FormLayout.RIGHT_FILL);
        formPanel.add(new JLabel("Action:"), FormLayout.LEFT);
        formPanel.add(actionField, FormLayout.RIGHT_FILL);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder(new EmptyBorder(0, 0, 2, 0));
        northPanel.add(titlePanel, BorderLayout.NORTH);
        northPanel.add(formPanel, BorderLayout.CENTER);
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setBackground(soapPanel.getBackground());
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messageScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageScroll.setPreferredSize(new Dimension(100, 100));
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        messagePanel.add(messageScroll, BorderLayout.CENTER);
        messagePanel.add(northPanel, BorderLayout.NORTH);
        responseArea = new JTextArea();
        responseArea.setEditable(false);
        responseArea.setBackground(soapPanel.getBackground());
        JScrollPane responseScroll = new JScrollPane(responseArea);
        responseScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        responseScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        responseScroll.setPreferredSize(new Dimension(100, 100));
        JPanel responseTitlePanel = new JPanel(new BorderLayout());
        JLabel responseLabel = new JLabel("Response");
        responseLabel.setFont(messageLabel.getFont().deriveFont((float) 15));
        responseLabel.setForeground(Color.gray);
        responseTitlePanel.add(responseLabel, BorderLayout.WEST);
        JPanel responsePanel = new JPanel(new BorderLayout());
        responsePanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        responsePanel.add(responseTitlePanel, BorderLayout.NORTH);
        responsePanel.add(responseScroll, BorderLayout.CENTER);
        JSplitPane messagesPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, messagePanel, responsePanel);
        messagesPane.setDividerLocation(200);
        soapPanel.add(messagesPane, BorderLayout.CENTER);
        this.setContentPane(mainPanel);
        mainPanel.add(soapPanel, BorderLayout.CENTER);
        closeButton = new JButton("Close");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setBorder(new EmptyBorder(5, 0, 3, 0));
        buttonPanel.add(closeButton);
        closeButton.addActionListener(this);
        getRootPane().setDefaultButton(sendButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setLocation(lastPosition);
        setSize(SIZE);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                lastPosition = getLocationOnScreen();
                setVisible(false);
            }
        });
    }

    /**
	 * Sets the address element in the display panel and sets the window title.
	 * 
	 * @param element
	 *            the Address element.
	 */
    public void setDocument(Document document) {
        this.document = document;
        messageArea.setText(toString(document));
        messageArea.setCaretPosition(0);
    }

    /**
	 * The change button has been pressed, show the editor dialog!
	 * 
	 * @param e
	 *            the event from the change button.
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == closeButton) {
            lastPosition = getLocationOnScreen();
            close();
        } else if (e.getSource() == sendButton) {
            send((String) urlField.getSelectedItem(), (String) actionField.getSelectedItem());
        }
    }

    private Envelope getEnvelope() {
        if (envelope == null) {
            envelope = Envelope.unmarshall(document.getDocumentElement());
        }
        return envelope;
    }

    private SOAPContext getContext() {
        if (context == null) {
            context = new SOAPContext();
        }
        return context;
    }

    private Hashtable<String, String> getHeaders() {
        if (headers == null) {
            String title = System.getProperty("xngr.title");
            String version = System.getProperty("xngr.version");
            String reference = System.getProperty("xngr.reference");
            headers = new Hashtable<String, String>();
            headers.put("User-Agent", "SOAP-Service/0.2 " + title + "/" + version + " (" + reference + ")");
        }
        return headers;
    }

    private SOAPTransport getTransport() {
        if (transport == null) {
            transport = new SOAPHTTPConnection();
        }
        return transport;
    }

    private void send(String location, String action) {
        if (action != null) {
            actions.put(action);
        } else {
            action = "";
        }
        targets.put(location);
        try {
            URL url = new URL(location);
            getTransport().send(url, action, getHeaders(), getEnvelope(), null, getContext());
            responseArea.setForeground(Color.black);
            responseArea.setText(format(transport.receive()));
            responseArea.setCaretPosition(0);
        } catch (MalformedURLException mue) {
            responseArea.setText("");
            JOptionPane.showMessageDialog(this, "Invalid URL :" + location, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SOAPException e) {
            e.printStackTrace();
            responseArea.setText("");
            JOptionPane.showMessageDialog(this, "Could not Send Envelope!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        fillCombo(actionField, actions.get());
        fillCombo(urlField, targets.get());
    }

    public void close() {
        setVisible(false);
        dispose();
    }

    private void fillCombo(JComboBox box, List<String> items) {
        box.removeAllItems();
        for (int i = 0; i < items.size(); i++) {
            box.addItem(items.get(i));
        }
        resetCaretPosition(box);
    }

    private void resetCaretPosition(JComboBox box) {
        Component editor = box.getEditor().getEditorComponent();
        if (editor instanceof JTextField) {
            ((JTextField) editor).setCaretPosition(0);
        }
    }

    private String format(Reader reader) {
        BufferedReader buffer = new BufferedReader(reader);
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        try {
            buffer.mark(16000);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(buffer));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document == null) {
            String output = "";
            try {
                String line = null;
                buffer.reset();
                while ((line = buffer.readLine()) != null) {
                    output = output + line + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return output;
        }
        return toString(document);
    }

    private String toString(Node node) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Result result = new StreamResult(stream);
            Source source = new DOMSource(node);
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
        return stream.toString();
    }
}
