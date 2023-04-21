package org.monome.pages;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.midi.MidiMessage;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.illposed.osc.OSCMessage;

/**
 * The Ableton Clip Launcher page.  Usage information is available at:
 * 
 * http://code.google.com/p/monome-pages/wiki/AbletonClipLauncherPage
 *   
 * @author Tom Dinchak
 *
 */
public class AbletonLiveLooperPage implements ActionListener, Page {

    /**
	 * Reference to the MonomeConfiguration this page belongs to.
	 */
    MonomeConfiguration monome;

    /**
	 * This page's index (page number).
	 */
    private int index;

    /**
	 * This page's GUI / control panel.
	 */
    private JPanel panel;

    /**
	 * The amount to offset the monome display of the clips
	 */
    private int clipOffset = 0;

    /**
	 * The amount to offset the monome display of the tracks
	 */
    private int trackOffset;

    /**
	 * Ableton's current tempo/BPM setting
	 */
    private JCheckBox disableMuteCB = new JCheckBox();

    private JCheckBox disableSoloCB = new JCheckBox();

    private JCheckBox disableArmCB = new JCheckBox();

    private JCheckBox disableStopCB = new JCheckBox();

    private JButton refreshButton = new JButton();

    /**
	 * The number of control rows (track arm, track stop) that are enabled currently
	 */
    private int numEnabledRows = 4;

    private int loopLength = 1;

    private int loopButton = 4;

    private int tickNum;

    private AbletonState abletonState;

    /**
	 * The name of the page 
	 */
    private String pageName = "Ableton Live Looper";

    private JLabel pageNameLBL;

    /**
	 * @param monome The MonomeConfiguration this page belongs to
	 * @param index This page's index number
	 */
    public AbletonLiveLooperPage(MonomeConfiguration monome, int index) {
        this.monome = monome;
        this.index = index;
        this.monome.configuration.initAbleton();
        this.abletonState = this.monome.configuration.abletonState;
    }

    public void actionPerformed(ActionEvent e) {
        int numEnabledRows = 0;
        if (this.disableMuteCB.isSelected() == false) {
            numEnabledRows++;
        }
        if (this.disableSoloCB.isSelected() == false) {
            numEnabledRows++;
        }
        if (this.disableArmCB.isSelected() == false) {
            numEnabledRows++;
        }
        if (this.disableStopCB.isSelected() == false) {
            numEnabledRows++;
        }
        this.numEnabledRows = numEnabledRows;
        if (e.getActionCommand().equals("Refresh from Ableton")) {
            this.refreshAbleton();
        }
        return;
    }

    public void addMidiOutDevice(String deviceName) {
        return;
    }

    public String getName() {
        return pageName;
    }

    public void setName(String name) {
        this.pageName = name;
        this.pageNameLBL.setText("Page " + (this.index + 1) + ": " + pageName);
        this.monome.setJMenuBar(this.monome.createMenuBar());
    }

    public JPanel getPanel() {
        if (this.panel != null) {
            return this.panel;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        pageNameLBL = new JLabel("Page " + (this.index + 1) + ": Ableton Live Looper");
        panel.add(pageNameLBL);
        disableMuteCB.setText("Disable Mute");
        disableMuteCB.addActionListener(this);
        panel.add(disableMuteCB);
        disableSoloCB.setText("Disable Solo");
        disableSoloCB.addActionListener(this);
        panel.add(disableSoloCB);
        disableArmCB.setText("Disable Arm");
        disableArmCB.addActionListener(this);
        panel.add(disableArmCB);
        disableStopCB.setText("Disable Stop");
        disableStopCB.addActionListener(this);
        panel.add(disableStopCB);
        refreshButton.setText("Refresh from Ableton");
        refreshButton.addActionListener(this);
        panel.add(refreshButton);
        this.panel = panel;
        return panel;
    }

    public void handlePress(int x, int y, int value) {
        if (value == 1) {
            if (x == this.monome.sizeX - 1) {
                if (y == 0) {
                    if (this.clipOffset > 0) {
                        this.clipOffset -= 1;
                    }
                } else if (y == 1) {
                    if ((this.clipOffset + 1) * (this.monome.sizeY - this.numEnabledRows) < 960) {
                        this.clipOffset += 1;
                    }
                } else if (y == 2) {
                    if (this.trackOffset > 0) {
                        this.trackOffset -= 1;
                    }
                } else if (y == 3) {
                    if ((this.trackOffset + 1) * (this.monome.sizeX - 1) < 100) {
                        this.trackOffset += 1;
                    }
                } else if (y >= 4 || y <= 7) {
                    this.monome.led(x, this.loopButton, 0, this.index);
                    this.loopButton = y;
                    this.monome.led(x, this.loopButton, 1, this.index);
                    this.loopLength = (int) Math.pow(2, (y - 3));
                }
            } else {
                if (y == this.monome.sizeY - 1 && this.disableArmCB.isSelected() == false) {
                    int track_num = x + (this.trackOffset * (this.monome.sizeX - 1));
                    AbletonTrack track = this.abletonState.getTrack(track_num);
                    if (track != null) {
                        if (track.getArm() == 0) {
                            this.armTrack(track_num);
                        } else {
                            this.disarmTrack(track_num);
                        }
                    }
                } else if ((y == this.monome.sizeY - 2 && this.disableSoloCB.isSelected() == false && this.disableArmCB.isSelected() == false) || y == this.monome.sizeY - 1 && this.disableSoloCB.isSelected() == false && this.disableArmCB.isSelected() == true) {
                    int track_num = x + (this.trackOffset * (this.monome.sizeX - 1));
                    AbletonTrack track = this.abletonState.getTrack(track_num);
                    if (track != null) {
                        if (track.getSolo() == 0) {
                            this.soloTrack(track_num);
                        } else {
                            this.unsoloTrack(track_num);
                        }
                        this.viewTrack(track_num);
                    }
                } else if ((y == this.monome.sizeY - 3 && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == false && this.disableSoloCB.isSelected() == false) || (y == this.monome.sizeY - 2 && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == false && this.disableSoloCB.isSelected() == true) || (y == this.monome.sizeY - 2 && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == true && this.disableSoloCB.isSelected() == false) || (y == this.monome.sizeY - 1 && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == true && this.disableSoloCB.isSelected() == true)) {
                    int track_num = x + (this.trackOffset * (this.monome.sizeX - 1));
                    AbletonTrack track = this.abletonState.getTrack(track_num);
                    if (track != null) {
                        if (track.getMute() == 0) {
                            this.muteTrack(track_num);
                        } else {
                            this.unmuteTrack(track_num);
                        }
                        this.viewTrack(track_num);
                    }
                } else if ((y == this.monome.sizeY - 4 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == false && this.disableSoloCB.isSelected() == false) || (y == this.monome.sizeY - 3 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == true && this.disableArmCB.isSelected() == false && this.disableSoloCB.isSelected() == false) || (y == this.monome.sizeY - 3 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == true && this.disableSoloCB.isSelected() == false) || (y == this.monome.sizeY - 3 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == false && this.disableSoloCB.isSelected() == true) || (y == this.monome.sizeY - 2 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == true && this.disableArmCB.isSelected() == true && this.disableSoloCB.isSelected() == false) || (y == this.monome.sizeY - 2 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == false && this.disableArmCB.isSelected() == true && this.disableSoloCB.isSelected() == true) || (y == this.monome.sizeY - 2 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == true && this.disableArmCB.isSelected() == false && this.disableSoloCB.isSelected() == true) || (y == this.monome.sizeY - 1 && this.disableStopCB.isSelected() == false && this.disableMuteCB.isSelected() == true && this.disableArmCB.isSelected() == true && this.disableSoloCB.isSelected() == true)) {
                    int track_num = x + (this.trackOffset * (this.monome.sizeX - 1));
                    this.stopTrack(track_num);
                    this.viewTrack(track_num);
                } else {
                    int clip_num = y + (this.clipOffset * (this.monome.sizeY - this.numEnabledRows));
                    int track_num = x + (this.trackOffset * (this.monome.sizeX - 1));
                    this.viewTrack(track_num);
                    this.playClip(track_num, clip_num);
                }
            }
        }
    }

    /**
	 * Sends "/live/play/clip track clip" to LiveOSC.
	 * 
	 * @param track The track number to play (0 = first track)
	 * @param clip The clip number to play (0 = first clip)
	 */
    public void playClip(int trackNum, int clipNum) {
        AbletonTrack track = this.abletonState.getTrack(trackNum);
        if (track != null) {
            AbletonClip clip = track.getClip(clipNum);
            if (clip != null) {
                if (clip.getState() == AbletonClip.STATE_EMPTY) {
                    int delay = (int) (((60000.0 / (double) this.abletonState.getTempo()) * 2.0 * this.loopLength) - 100.0);
                    AbletonClipDelay acd = new AbletonClipDelay(delay, trackNum, clipNum, this.monome.configuration);
                    new Thread(acd).start();
                }
                this.monome.configuration.getAbletonControl().playClip(trackNum, clipNum);
            }
        }
    }

    /**
	 * Sends "/live/arm track" to LiveOSC.
	 * 
	 * @param track The track number to arm (0 = first track)
	 */
    public void armTrack(int track) {
        this.monome.configuration.getAbletonControl().armTrack(track);
    }

    /**
	 * Sends "/live/disarm track" to LiveOSC.
	 * 
	 * @param track The track number to disarm (0 = first track)
	 */
    public void disarmTrack(int track) {
        this.monome.configuration.getAbletonControl().disarmTrack(track);
    }

    /**
	 * Sends "/live/stop/track track" to LiveOSC.
	 * 
	 * @param track The track number to stop (0 = first track)
	 */
    public void stopTrack(int track) {
        this.monome.configuration.getAbletonControl().stopTrack(track);
    }

    /**
	 * Sends "/live/track/mute track" to LiveOSC.
	 * 
	 * @param track The track number to stop (0 = first track)
	 */
    public void muteTrack(int track) {
        this.monome.configuration.getAbletonControl().muteTrack(track);
    }

    public void soloTrack(int track) {
        this.monome.configuration.getAbletonControl().soloTrack(track);
    }

    public void unsoloTrack(int track) {
        this.monome.configuration.getAbletonControl().unsoloTrack(track);
    }

    /**
	 * Sends "/live/track/mute track" to LiveOSC.
	 * 
	 * @param track The track number to stop (0 = first track)
	 */
    public void unmuteTrack(int track) {
        this.monome.configuration.getAbletonControl().unmuteTrack(track);
    }

    public void refreshAbleton() {
        this.monome.configuration.getAbletonControl().refreshAbleton();
    }

    /**
	 * Sends "/live/track/view track" to LiveOSC.
	 * 
	 * @param track The track number to stop (0 = first track)
	 */
    public void viewTrack(int track) {
        this.monome.configuration.getAbletonControl().viewTrack(track);
    }

    public void handleReset() {
        tickNum = 0;
        return;
    }

    public void handleTick() {
        tickNum++;
        if (tickNum == 96) {
            tickNum = 0;
        }
        for (int x = 0; x < this.monome.sizeX; x++) {
            int trackNum = x + (this.trackOffset * (this.monome.sizeX - 1));
            AbletonTrack track = this.abletonState.getTrack(trackNum);
            if (track != null) {
                for (int y = 0; y < this.monome.sizeY - numEnabledRows; y++) {
                    int clipNum = y + (this.clipOffset * (this.monome.sizeY - this.numEnabledRows));
                    AbletonClip clip = track.getClip(clipNum);
                    if (clip != null) {
                        if (clip.getState() == AbletonClip.STATE_PLAYING) {
                            if (tickNum % 24 == 0) {
                                this.monome.led(x, y, 1, this.index);
                            }
                            if ((tickNum + 12) % 24 == 0) {
                                this.monome.led(x, y, 0, this.index);
                            }
                        }
                        if (clip.getState() == AbletonClip.STATE_TRIGGERED) {
                            if (tickNum % 12 == 0) {
                                this.monome.led(x, y, 1, this.index);
                            }
                            if ((tickNum + 6) % 12 == 0) {
                                this.monome.led(x, y, 0, this.index);
                            }
                        }
                    }
                }
            }
        }
        return;
    }

    public void redrawMonome() {
        for (int x = 0; x < this.monome.sizeX - 1; x++) {
            int trackNum = x + (this.trackOffset * (this.monome.sizeX - 1));
            AbletonTrack track = this.abletonState.getTrack(trackNum);
            if (track != null) {
                for (int y = 0; y < (this.monome.sizeY - this.numEnabledRows); y++) {
                    int clipNum = y + (this.clipOffset * (this.monome.sizeY - this.numEnabledRows));
                    AbletonClip clip = track.getClip(clipNum);
                    if (clip != null) {
                        if (clip.getState() == AbletonClip.STATE_STOPPED) {
                            this.monome.led(x, y, 1, this.index);
                        } else if (clip.getState() == AbletonClip.STATE_EMPTY) {
                            this.monome.led(x, y, 0, this.index);
                        }
                    } else {
                        this.monome.led(x, y, 0, this.index);
                    }
                }
            } else {
                ArrayList<Integer> colParams = new ArrayList<Integer>();
                colParams.add(x);
                colParams.add(0);
                colParams.add(0);
                this.monome.led_col(colParams, this.index);
            }
        }
        if (this.disableArmCB.isSelected() == false) {
            for (int i = 0; i < this.monome.sizeX - 1; i++) {
                int track_num = i + (this.trackOffset * (this.monome.sizeX - 1));
                int yRow = this.monome.sizeY - 1;
                AbletonTrack track = this.abletonState.getTrack(track_num);
                if (track != null) {
                    if (track.getArm() == 1) {
                        this.monome.led(i, yRow, 1, this.index);
                    } else {
                        this.monome.led(i, yRow, 0, this.index);
                    }
                }
            }
        }
        if (this.disableSoloCB.isSelected() == false) {
            for (int i = 0; i < this.monome.sizeX - 1; i++) {
                int track_num = i + (this.trackOffset * (this.monome.sizeX - 1));
                int yRow;
                if (disableArmCB.isSelected() == false) {
                    yRow = this.monome.sizeY - 2;
                } else {
                    yRow = this.monome.sizeY - 1;
                }
                AbletonTrack track = this.abletonState.getTrack(track_num);
                if (track != null) {
                    if (track.getSolo() == 1) {
                        this.monome.led(i, yRow, 1, this.index);
                    } else {
                        this.monome.led(i, yRow, 0, this.index);
                    }
                }
            }
        }
        if (this.disableMuteCB.isSelected() == false) {
            for (int i = 0; i < this.monome.sizeX - 1; i++) {
                int track_num = i + (this.trackOffset * (this.monome.sizeX - 1));
                int yRow;
                if (disableArmCB.isSelected() == true && disableSoloCB.isSelected() == true) {
                    yRow = this.monome.sizeY - 1;
                } else if (disableArmCB.isSelected() == true || disableSoloCB.isSelected() == true) {
                    yRow = this.monome.sizeY - 2;
                } else {
                    yRow = this.monome.sizeY - 3;
                }
                AbletonTrack track = this.abletonState.getTrack(track_num);
                if (track != null) {
                    if (track.getMute() == 0) {
                        this.monome.led(i, yRow, 1, this.index);
                    } else {
                        this.monome.led(i, yRow, 0, this.index);
                    }
                }
            }
        }
        if (this.disableStopCB.isSelected() == false) {
            for (int i = 0; i < this.monome.sizeX - 1; i++) {
                int yRow;
                yRow = this.monome.sizeY - this.numEnabledRows;
                this.monome.led(i, yRow, 0, this.index);
            }
        }
        for (int y = 0; y < this.monome.sizeY; y++) {
            if (this.loopButton == y) {
                this.monome.led(this.monome.sizeX - 1, y, 1, this.index);
            } else {
                this.monome.led(this.monome.sizeX - 1, y, 0, this.index);
            }
        }
    }

    public void send(MidiMessage message, long timeStamp) {
        return;
    }

    public String toXml() {
        String disableArm = "false";
        String disableSolo = "false";
        String disableMute = "false";
        String disableStop = "false";
        if (disableArmCB.isSelected() == true) {
            disableArm = "true";
        }
        if (disableSoloCB.isSelected() == true) {
            disableSolo = "true";
        }
        if (disableMuteCB.isSelected() == true) {
            disableMute = "true";
        }
        if (disableStopCB.isSelected() == true) {
            disableStop = "true";
        }
        String xml = "";
        xml += "      <name>Ableton Live Looper</name>\n";
        xml += "      <pageName>" + this.pageName + "</pageName>\n";
        xml += "      <disablearm>" + disableArm + "</disablearm>\n";
        xml += "      <disablesolo>" + disableSolo + "</disablesolo>\n";
        xml += "      <disablemute>" + disableMute + "</disablemute>\n";
        xml += "      <disablestop>" + disableStop + "</disablestop>\n";
        return xml;
    }

    public void setDisableArm(String disableArm) {
        if (disableArm.equals("true")) {
            this.disableArmCB.doClick();
        }
    }

    public void setDisableSolo(String disableSolo) {
        if (disableSolo.equals("true")) {
            this.disableSoloCB.doClick();
        }
    }

    public void setDisableMute(String disableMute) {
        if (disableMute.equals("true")) {
            this.disableMuteCB.doClick();
        }
    }

    public void setDisableStop(String disableStop) {
        if (disableStop.equals("true")) {
            this.disableStopCB.doClick();
        }
    }

    public boolean getCacheDisabled() {
        return false;
    }

    public void destroyPage() {
        return;
    }

    public void clearPanel() {
        this.panel = null;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void handleADC(int adcNum, float value) {
    }

    public void handleADC(float x, float y) {
    }

    public boolean isTiltPage() {
        return false;
    }

    public ADCOptions getAdcOptions() {
        return null;
    }

    public void setAdcOptions(ADCOptions options) {
    }

    public void configure(Element pageElement) {
        NodeList nameNL = pageElement.getElementsByTagName("pageName");
        Element el = (Element) nameNL.item(0);
        if (el != null) {
            NodeList nl = el.getChildNodes();
            String name = ((Node) nl.item(0)).getNodeValue();
            this.setName(name);
        }
        NodeList armNL = pageElement.getElementsByTagName("disablearm");
        el = (Element) armNL.item(0);
        if (el != null) {
            NodeList nl = el.getChildNodes();
            String disableArm = ((Node) nl.item(0)).getNodeValue();
            this.setDisableArm(disableArm);
        }
        NodeList soloNL = pageElement.getElementsByTagName("disablesolo");
        el = (Element) soloNL.item(0);
        if (el != null) {
            NodeList nl = el.getChildNodes();
            String disableSolo = ((Node) nl.item(0)).getNodeValue();
            this.setDisableSolo(disableSolo);
        }
        NodeList muteNL = pageElement.getElementsByTagName("disablemute");
        el = (Element) muteNL.item(0);
        if (el != null) {
            NodeList nl = el.getChildNodes();
            String disableMute = ((Node) nl.item(0)).getNodeValue();
            this.setDisableMute(disableMute);
        }
        NodeList stopNL = pageElement.getElementsByTagName("disablestop");
        el = (Element) stopNL.item(0);
        if (el != null) {
            NodeList nl = el.getChildNodes();
            String disableStop = ((Node) nl.item(0)).getNodeValue();
            this.setDisableStop(disableStop);
        }
    }
}
