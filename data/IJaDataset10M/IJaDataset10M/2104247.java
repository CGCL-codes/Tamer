package discontinued;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.matsim.utils.misc.Time;
import org.matsim.utils.vis.netvis.VisConfig;
import playground.david.vis.OTFVisNet;
import playground.david.vis.OTFGUI.NetVisResizable;
import playground.david.vis.interfaces.OTFServerRemote;

public class NetVis3DToolbar extends JToolBar implements ActionListener, ItemListener, ChangeListener {

    private static final String TO_START = "to_start";

    private static final String PAUSE = "pause";

    private static final String PLAY = "play";

    private static final String STEP_F = "step_f";

    private static final String STEP_FF = "step_ff";

    private static final String STOP = "stop";

    private static final String ZOOM_IN = "zoom_in";

    private static final String ZOOM_OUT = "zoom_out";

    private static final String SET_TIME = "set_time";

    private static final String TOGGLE_AGENTS = "Agents";

    private static final String TOGGLE_LINK_LABELS = "Link Labels";

    private static final int SKIP = 30;

    private final MovieTimer movieTimer = new MovieTimer();

    private JButton playButton;

    private JFormattedTextField timeField;

    private final VisConfig visConfig;

    private int simTime = 0;

    private final int bigSteptime = 30;

    private NetVisResizable networkScrollPane = null;

    OTFVisNet visnet = null;

    OTFServerRemote host = null;

    public NetVis3DToolbar(OTFServerRemote host, OTFVisNet network, NetVisResizable networkScrollPane, VisConfig visConfig) {
        super();
        this.visnet = network;
        this.visConfig = visConfig;
        this.host = host;
        this.networkScrollPane = networkScrollPane;
        addButtons();
    }

    private void addButtons() {
        add(createButton("Pause", PAUSE));
        playButton = createButton("PLAY", PLAY);
        add(playButton);
        add(createButton(">", STEP_F));
        add(createButton(">>", STEP_FF));
        add(createButton("STOP", STOP));
        timeField = new JFormattedTextField(new MessageFormat("{0,number,00}-{1,number,00}-{2,number,00}"));
        timeField.setMaximumSize(new Dimension(90, 30));
        timeField.setActionCommand(SET_TIME);
        timeField.setHorizontalAlignment(JTextField.CENTER);
        add(timeField);
        timeField.addActionListener(this);
        add(createButton("--", ZOOM_OUT));
        add(createButton("+", ZOOM_IN));
        createCheckBoxes();
        SpinnerNumberModel model = new SpinnerNumberModel(visConfig.getLinkWidthFactor(), 0, 200, 1);
        JSpinner spin = addLabeledSpinner(this, "Lanewidth", model);
        spin.setMaximumSize(new Dimension(75, 30));
        spin.addChangeListener(this);
        movieTimer.start();
    }

    private JButton createButton(String display, String actionCommand) {
        JButton button;
        button = new JButton();
        button.setActionCommand(actionCommand);
        button.addActionListener(this);
        button.setText(display);
        return button;
    }

    public void updateTimeLabel() {
        timeField.setText(Time.strFromSec(simTime));
    }

    private void stopMovie() {
        if (movieTimer != null) {
            movieTimer.setActive(false);
            playButton.setText("PLAY");
        }
    }

    private void pressed_TO_START() throws IOException {
    }

    private void pressed_PAUSE() throws IOException {
        movieTimer.setActive(false);
        playButton.setSelected(false);
        host.pause();
    }

    private void pressed_PLAY() throws RemoteException {
        host.play();
        movieTimer.setActive(true);
        playButton.setSelected(true);
        simTime = 0;
    }

    private void pressed_STEP_F() throws IOException {
        playButton.setSelected(false);
        movieTimer.setActive(false);
        byte[] bbyte = host.getStateBuffer();
        if (bbyte == null) System.out.println("End of movie reached!"); else visnet.readMyself(new DataInputStream(new ByteArrayInputStream(bbyte, 0, bbyte.length)));
    }

    private void pressed_STEP_FF() throws IOException {
        for (int i = 0; i < bigSteptime; i++) host.step();
    }

    private void pressed_STOP() throws IOException {
    }

    private void pressed_ZOOM_OUT() {
        float scale = networkScrollPane.getScale() / 1.42f;
        networkScrollPane.scaleNetwork(scale);
    }

    private void pressed_ZOOM_IN() {
        float scale = networkScrollPane.getScale() * 1.42f;
        networkScrollPane.scaleNetwork(scale);
    }

    private void changed_SET_TIME(ActionEvent event) throws IOException {
        String newTime = ((JFormattedTextField) event.getSource()).getText();
        int newTime_s = Time.secFromStr(newTime);
        stopMovie();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        try {
            if (TO_START.equals(command)) pressed_TO_START(); else if (PAUSE.equals(command)) pressed_PAUSE(); else if (PLAY.equals(command)) pressed_PLAY(); else if (STEP_F.equals(command)) pressed_STEP_F(); else if (STEP_FF.equals(command)) pressed_STEP_FF(); else if (STOP.equals(command)) pressed_STOP(); else if (ZOOM_OUT.equals(command)) pressed_ZOOM_OUT(); else if (ZOOM_IN.equals(command)) pressed_ZOOM_IN(); else if (command.equals(SET_TIME)) changed_SET_TIME(event);
        } catch (IOException e) {
            System.err.println("ControlToolbar encountered problem: " + e);
        }
        updateTimeLabel();
        repaint();
        networkScrollPane.repaint();
    }

    protected JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
        JLabel l = new JLabel(label);
        c.add(l);
        JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    private void createCheckBoxes() {
        JCheckBox VehBox = new JCheckBox(TOGGLE_AGENTS);
        VehBox.setMnemonic(KeyEvent.VK_V);
        VehBox.setSelected(true);
        VehBox.addItemListener(this);
        add(VehBox);
    }

    public void itemStateChanged(ItemEvent e) {
        JCheckBox source = (JCheckBox) e.getItemSelectable();
        if (source.getText().equals(TOGGLE_AGENTS)) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                visConfig.set("ShowAgents", "false");
            } else {
                visConfig.set("ShowAgents", "true");
            }
        } else if (source.getText().equals(TOGGLE_LINK_LABELS)) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                visConfig.set(VisConfig.SHOW_LINK_LABELS, "false");
            } else {
                visConfig.set(VisConfig.SHOW_LINK_LABELS, "true");
            }
        }
        repaint();
        networkScrollPane.repaint();
    }

    public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) e.getSource();
        int i = ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
        visConfig.set(VisConfig.LINK_WIDTH_FACTOR, Integer.toString(i));
        repaint();
        networkScrollPane.repaint();
    }

    class MovieTimer extends Thread {

        boolean isActive = false;

        boolean terminate = false;

        public MovieTimer() {
            setDaemon(true);
        }

        public synchronized boolean isActive() {
            return isActive;
        }

        public synchronized void setActive(boolean isActive) {
            this.isActive = isActive;
        }

        public synchronized void terminate() {
            this.terminate = true;
        }

        @Override
        public void run() {
            int actTime = 0;
            while (!terminate) {
                try {
                    sleep(100);
                    actTime = host.getLocalTime();
                    if (simTime != actTime) {
                        simTime = actTime;
                        updateTimeLabel();
                        repaint();
                        if (isActive) {
                            byte[] bbyte = host.getStateBuffer();
                            if (bbyte == null) {
                                System.out.println("End of movie reached!");
                                pressed_PAUSE();
                            } else visnet.readMyself(new DataInputStream(new ByteArrayInputStream(bbyte, 0, bbyte.length)));
                            networkScrollPane.repaint();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
