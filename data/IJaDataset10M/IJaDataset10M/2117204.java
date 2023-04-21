package cbr2.agent;

import DE.FhG.IGD.ui.*;
import DE.FhG.IGD.util.*;
import DE.FhG.IGD.semoa.agent.*;
import DE.FhG.IGD.semoa.security.*;
import DE.FhG.IGD.semoa.server.*;
import DE.FhG.IGD.semoa.net.*;
import cbr2.gui.*;
import cbr2.service.*;
import cbr2.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.security.*;
import java.util.*;

public class FetchAgent extends WaitingAgent implements ActionListener, Runnable, Serializable {

    public static final String ANIM_NAME = "images/duke";

    public static final String PROP_IMAGE_URL = "image.url";

    public static final String PROP_IMAGE_NAME = "image.name";

    public static final String PROP_RETURN_URL = "return.url";

    public static final String PROP_INTERACTIVE = "interactive";

    public static final String PROP_BROKER_URL = "broker.url";

    public static final String PROP_IMAGE_SERVICE = "image.service";

    public static final String PROP_BROKER_SERVICE = "broker.service";

    public static final String PROP_SUBSTORE_SERVICE = "subscription.service";

    public static final String VICINITY = "/network/vicinity";

    public static final String TOKEN_FILE = "token";

    public static final String RECEIPT_FILE = "receipt";

    public static final int STATE_READY = 0;

    public static final int STATE_FETCH_TOKEN = 1;

    public static final int STATE_FETCH_IMAGE = 2;

    public static final int STATE_RETURN = 3;

    private transient volatile Ticket ticket_;

    private transient VariablesContext context_;

    private transient JList serverList_;

    private transient JFrame frame_;

    private ImageGrant grant_;

    private Set subscriptions_;

    private int state_;

    public FetchAgent() {
        state_ = STATE_READY;
    }

    public void run() {
        OutputStream os;
        Resource res;
        context_ = Variables.getContext();
        ticket_ = null;
        if (state_ != STATE_RETURN && context_.get(PROP_INTERACTIVE).equalsIgnoreCase("true")) {
            createGUI();
            try {
                waitForSignal("Action");
            } catch (InterruptedException ex) {
            }
        }
        if (ticket_ == null) {
            switch(state_) {
                case STATE_READY:
                    SubscriptionStore subst;
                    URL url;
                    subst = (SubscriptionStore) Environment.getEnvironment().lookup(context_.get(PROP_SUBSTORE_SERVICE));
                    try {
                        url = new URL(context_.get(PROP_BROKER_URL));
                        subscriptions_ = subst.get(url);
                    } catch (java.net.MalformedURLException ex) {
                        System.err.println("Malformed broker URL");
                        return;
                    }
                    ticket_ = new Ticket(url);
                    state_ = STATE_FETCH_TOKEN;
                    break;
                case STATE_FETCH_TOKEN:
                    BrokerService bs;
                    System.out.println("At broker");
                    try {
                        bs = (BrokerService) Environment.getEnvironment().lookup(context_.get(PROP_BROKER_SERVICE));
                        if (bs == null) {
                            System.err.println("Broker service not found");
                            return;
                        }
                        grant_ = bs.getToken(context_.get(PROP_IMAGE_NAME), subscriptions_);
                        try {
                            res = Mobility.getContext().getResource();
                            os = res.getOutputStream(AgentStructure.PATH_MUTABLE + "/" + TOKEN_FILE);
                            os.write(grant_.getToken());
                            os.close();
                        } catch (IOException ex) {
                            System.out.println("Can store ticket " + "in agent's coprus!");
                        }
                        try {
                            ticket_ = new Ticket(new URL(grant_.getURL()));
                        } catch (java.net.MalformedURLException e) {
                            System.err.println("Malformed image " + "provider URL");
                            return;
                        }
                        state_ = STATE_FETCH_IMAGE;
                    } catch (AccessControlException e) {
                        System.err.println("Access to token service denied");
                        return;
                    }
                    break;
                case STATE_FETCH_IMAGE:
                    byte[] image;
                    byte[] receipt;
                    ByteArrayOutputStream bos;
                    System.out.println("At image provider");
                    try {
                        GuardedPicsSource pics;
                        pics = (GuardedPicsSource) Environment.getEnvironment().lookup(context_.get(PROP_IMAGE_SERVICE));
                        if (pics == null) {
                            System.err.println("Pics source not found");
                            return;
                        }
                        res = Mobility.getContext().getResource();
                        bos = new ByteArrayOutputStream();
                        image = pics.getPicture(context_.get(PROP_IMAGE_NAME), grant_.getToken(), bos);
                        receipt = bos.toByteArray();
                        if (image == null) {
                            System.err.println("Image not found!");
                            return;
                        }
                        res = Mobility.getContext().getResource();
                        os = res.getOutputStream(AgentStructure.PATH_MUTABLE + "secret/" + context_.get(PROP_IMAGE_NAME));
                        os.write(image);
                        os.close();
                        System.out.println("Got image!");
                        os = res.getOutputStream(AgentStructure.PATH_MUTABLE + RECEIPT_FILE);
                        os.write(receipt);
                        os.close();
                        System.out.println("Got receipt!");
                        state_ = STATE_RETURN;
                        ticket_ = new Ticket(new URL(context_.get(PROP_RETURN_URL)));
                    } catch (AccessControlException e) {
                        e.printStackTrace();
                        System.err.println("No access to pics source");
                        return;
                    } catch (java.net.MalformedURLException e) {
                        System.err.println("Malformed return URL");
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error writing image");
                        return;
                    }
                    break;
                case STATE_RETURN:
                    returnImage();
                    return;
            }
            Mobility.getContext().setTicket(ticket_);
            System.out.println("[FetchAgent] hopping to " + ticket_.getTarget());
        }
        if (frame_ != null) {
            frame_.dispose();
        }
        Thread.currentThread().getThreadGroup().interrupt();
    }

    private void createGUI() {
        GridBagConstraints c;
        DefaultListModel listModel;
        AudibleButton button;
        GridBagLayout grid;
        AnimatedIcon icon;
        Vicinity vicinity;
        Iterator it;
        JLabel label;
        frame_ = new JFrame(context_.get(AgentStructure.PROP_AGENT_ALIAS));
        grid = new GridBagLayout();
        c = new GridBagConstraints();
        frame_.getContentPane().setLayout(grid);
        try {
            icon = new AnimatedIcon(ANIM_NAME);
            label = new JLabel(icon);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            icon.setParentComponent(label);
            icon.setLooping();
            icon.start();
            c.gridy = 0;
            grid.setConstraints(label, c);
            frame_.getContentPane().add(label);
        } catch (Exception ex) {
        }
        button = new AudibleButton();
        switch(state_) {
            case STATE_READY:
                button.setText("Jump to broker");
                break;
            case STATE_FETCH_TOKEN:
                button.setText("Fetch image");
                break;
            case STATE_FETCH_IMAGE:
                button.setText("Return image");
        }
        button.addActionListener(this);
        button.setActionCommand("continue");
        c.gridy = 1;
        grid.setConstraints(button, c);
        frame_.getContentPane().add(button);
        listModel = new DefaultListModel();
        try {
            vicinity = (Vicinity) Environment.getEnvironment().lookup(VICINITY);
        } catch (AccessControlException e) {
            System.err.println("No access to vicinity");
            frame_.pack();
            frame_.setVisible(true);
            return;
        }
        if (vicinity == null) {
            System.err.println("Vicinity not found");
            frame_.pack();
            frame_.setVisible(true);
            return;
        }
        it = vicinity.getContactTable().values().iterator();
        while (it.hasNext()) {
            listModel.addElement(it.next());
        }
        serverList_ = new JList(listModel);
        serverList_.setPreferredSize(new Dimension(300, 100));
        c.gridy = 2;
        grid.setConstraints(serverList_, c);
        frame_.getContentPane().add(serverList_);
        button = new AudibleButton("Jump");
        button.addActionListener(this);
        button.setActionCommand("jump");
        c.gridy = 3;
        grid.setConstraints(button, c);
        frame_.getContentPane().add(button);
        frame_.pack();
        frame_.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("jump")) {
            try {
                ticket_ = new Ticket((URL) serverList_.getSelectedValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        sendSignal("Action");
    }

    private void returnImage() {
        FileOutputStream fos;
        InputStream is;
        Resource res;
        String dname;
        String name;
        String path;
        String time;
        Image image;
        byte[] buffer;
        File dir;
        int startOffset;
        int endOffset;
        int read;
        try {
            res = Mobility.getContext().getResource();
            is = res.getInputStream(AgentStructure.PATH_MUTABLE + "secret/" + context_.get(PROP_IMAGE_NAME));
            image = Images.newImage(is);
            name = context_.get(PROP_IMAGE_NAME);
            is.close();
            returnToController(image, name);
            dname = Mobility.getContext().getCard().getCertificate().getSubjectDN().getName();
            startOffset = dname.indexOf("CN=");
            if (startOffset != -1) {
                endOffset = dname.indexOf(",", startOffset);
                if (endOffset == -1) {
                    dname = dname.substring(startOffset + 3);
                } else {
                    dname = dname.substring(startOffset + 3, endOffset);
                }
                dname = dname.replace(' ', '_');
            }
            time = DateString.get();
            path = System.getProperty("user.home") + File.separator + dname + File.separator + CBRConfig.getProperty("cbr.fetchagent.dir");
            dir = new File(path);
            if (!dir.isDirectory()) {
                System.out.println("Creating " + path);
                dir.mkdirs();
            }
            path += File.separator + time + "-" + name;
            System.out.print("Autosaving image to " + path + "...");
            fos = new FileOutputStream(path);
            buffer = new byte[1000];
            is = res.getInputStream(AgentStructure.PATH_MUTABLE + "secret/" + context_.get(PROP_IMAGE_NAME));
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            is.close();
            fos.close();
            System.out.println("Done!");
            path = System.getProperty("user.home") + File.separator + dname + File.separator + CBRConfig.getProperty("cbr.receipts.dir");
            dir = new File(path);
            if (!dir.isDirectory()) {
                System.out.println("Creating " + path);
                dir.mkdirs();
            }
            path += File.separator + time + "-Receipt.rcp";
            System.out.print("Autosaving receipt to " + path + "...");
            fos = new FileOutputStream(path);
            buffer = new byte[1000];
            is = res.getInputStream(AgentStructure.PATH_MUTABLE + RECEIPT_FILE);
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            is.close();
            fos.close();
            System.out.println("Done!");
        } catch (Exception e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    private void returnToController(Image image, String name) {
        SearchController controller;
        try {
            System.out.print("Checking for service " + SearchController.PATH + "...");
            controller = (SearchController) Environment.getEnvironment().lookup(SearchController.PATH);
            System.out.println("OK");
            controller.returnImage(image, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
