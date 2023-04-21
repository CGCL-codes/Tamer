package soccer.client.dialog;

import java.net.URL;
import javax.swing.ImageIcon;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.*;
import soccer.client.SoccerMaster;
import soccer.client.World;
import soccer.common.*;

public class SituationDialog extends JPanel implements ActionListener {

    private DialogManager aDialogManager;

    private SoccerMaster aSoccerMaster;

    private File situationFile;

    private JDialog aJDialog;

    private JTextField filenameJTextField;

    private JTextField replicasJTextField;

    private JTextField stepsJTextField;

    private int numOfReplicas = 1;

    private int numOfSteps = 100;

    public SituationDialog(DialogManager aDialogManager, SoccerMaster aSoccerMaster, File situationFile) {
        super();
        this.setName("Situation Setup");
        this.aDialogManager = aDialogManager;
        this.aSoccerMaster = aSoccerMaster;
        this.situationFile = situationFile;
        setupViewPanel();
    }

    private void setupViewPanel() {
        setBorder(BorderFactory.createLoweredBevelBorder());
        TitledBorder title;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel nameJPanel = new JPanel(new BorderLayout());
        title = BorderFactory.createTitledBorder("Situation file:");
        nameJPanel.setBorder(title);
        filenameJTextField = new JTextField(situationFile.getName());
        filenameJTextField.setEditable(false);
        nameJPanel.add(filenameJTextField);
        add(nameJPanel);
        add(Box.createVerticalGlue());
        JPanel numJPanel = new JPanel(new BorderLayout());
        title = BorderFactory.createTitledBorder("Times to repeat:");
        numJPanel.setBorder(title);
        replicasJTextField = new JTextField(4);
        replicasJTextField.setText("1");
        replicasJTextField.setActionCommand("Replicas");
        replicasJTextField.addActionListener(this);
        numJPanel.add(replicasJTextField);
        add(numJPanel);
        add(Box.createVerticalGlue());
        JPanel stepsJPanel = new JPanel(new BorderLayout());
        title = BorderFactory.createTitledBorder("Duration in steps:");
        stepsJPanel.setBorder(title);
        stepsJTextField = new JTextField(4);
        stepsJTextField.setText("100");
        stepsJTextField.setActionCommand("Steps");
        stepsJTextField.addActionListener(this);
        stepsJPanel.add(stepsJTextField);
        add(stepsJPanel);
        add(Box.createVerticalGlue());
        JButton okJButton = new JButton("OK");
        okJButton.setActionCommand("OK");
        okJButton.addActionListener(this);
        JButton cancelJButton = new JButton("Cancel");
        cancelJButton.setActionCommand("Cancel");
        cancelJButton.addActionListener(this);
        JPanel option = new JPanel();
        option.setLayout(new FlowLayout());
        option.add(okJButton);
        option.add(cancelJButton);
        add(option);
        add(Box.createVerticalGlue());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("OK")) {
            undisplay();
            setSituation();
        } else if (e.getActionCommand().equals("Cancel")) undisplay();
    }

    private void setSituation() {
        try {
            numOfReplicas = Integer.parseInt(replicasJTextField.getText());
        } catch (NumberFormatException nfe) {
        }
        try {
            numOfSteps = Integer.parseInt(stepsJTextField.getText());
        } catch (NumberFormatException nfe) {
        }
        World aWorld = new World();
        aWorld.init();
        aWorld.preMode = RefereeData.PLAY_ON;
        try {
            if (readFileOK(situationFile, aWorld)) updateServer(aWorld);
        } catch (IOException ie) {
            System.out.println("Error opening file " + ie);
        }
    }

    public void display() {
        if (aJDialog == null) {
            aJDialog = new JDialog((Frame) null, "Situation Setup", true);
            aJDialog.getContentPane().setLayout(new BorderLayout());
            aJDialog.getContentPane().add(SituationDialog.this, BorderLayout.CENTER);
            aJDialog.setSize(250, 150);
            aJDialog.setResizable(false);
        }
        aDialogManager.showDialog(aJDialog);
    }

    public void undisplay() {
        aDialogManager.hideDialog(aJDialog);
    }

    private boolean readFileOK(File inputFile, World aWorld) throws IOException {
        boolean readingOK = false;
        try {
            FileReader frdr = new FileReader(inputFile);
            BufferedReader input = new BufferedReader(frdr);
            boolean done = false;
            boolean isData = false;
            int lineCount = -1;
            int recCount = 0;
            char key = '?';
            while (!done) {
                String line = input.readLine();
                if (line == null || line.equals("")) break;
                if (line.charAt(0) != '#') {
                    line = removeDelimiters(line);
                    isData = true;
                    if (line.equals(">> BALL")) {
                        key = 'B';
                        lineCount = 0;
                        isData = false;
                    } else if (line.equals(">> LEFT")) {
                        key = 'L';
                        lineCount = 0;
                        isData = false;
                    } else if (line.equals(">> RIGHT")) {
                        key = 'R';
                        lineCount = 0;
                        isData = false;
                    }
                    if (isData) {
                        done = parseString(line, lineCount, key, aWorld);
                        lineCount++;
                        recCount++;
                    }
                }
            }
            input.close();
            frdr.close();
            System.out.println("Total records read from file: " + recCount);
            readingOK = true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(aSoccerMaster.getJToolBar(), "Unable to read file. \n IOException:" + e, "Error Opening/Reading File", JOptionPane.ERROR_MESSAGE);
        }
        return readingOK;
    }

    private boolean parseString(String line, int lineCount, char key, World aWorld) throws IOException {
        int spaceidx = line.indexOf(" ");
        line = line.substring(0, spaceidx);
        switch(key) {
            case 'B':
                switch(lineCount) {
                    case 0:
                        aWorld.ball.position.setX(Double.parseDouble(line));
                        break;
                    case 1:
                        aWorld.ball.position.setY(Double.parseDouble(line));
                        break;
                    case 2:
                        aWorld.ball.controllerType = line.charAt(0);
                        break;
                    case 3:
                        aWorld.ball.controllerId = Integer.parseInt(line);
                        break;
                    case 4:
                        aWorld.ball.isGrabbed = Boolean.getBoolean(line);
                        break;
                    default:
                        System.out.println("Error parsing BALL line = " + line);
                        throw new IOException();
                }
                break;
            default:
                parseTeamString(line, lineCount, key, aWorld);
        }
        return false;
    }

    private boolean parseTeamString(String line, int lineCount, char key, World aWorld) throws IOException {
        int numOfPlayers;
        Vector team;
        String keyword;
        char side;
        if (key == 'L') {
            numOfPlayers = aWorld.leftTeam.size();
            keyword = "LEFT";
            team = aWorld.leftTeam;
            side = 'l';
        } else if (key == 'R') {
            numOfPlayers = aWorld.rightTeam.size();
            keyword = "RIGHT";
            team = aWorld.rightTeam;
            side = 'r';
        } else {
            System.out.println("Error parsing line = " + line + " key must be either LEFT or RIGHT");
            throw new IOException();
        }
        if (lineCount >= numOfPlayers * 4) {
            System.out.println("Error parsing " + keyword + " Too many lines; line = " + line + " lineCount = " + lineCount + " numOfPlayers = " + numOfPlayers);
            throw new IOException();
        }
        Player player = (Player) team.elementAt((int) (lineCount / 4));
        int idx = lineCount % 4;
        switch(idx) {
            case 0:
                player.id = Integer.parseInt(line);
                player.side = side;
                break;
            case 1:
                player.position.setX(Double.parseDouble(line));
                break;
            case 2:
                player.position.setY(Double.parseDouble(line));
                break;
            case 3:
                player.direction = Double.parseDouble(line);
                break;
            default:
                System.out.println("Error parsing  " + keyword + " line = " + line);
                throw new IOException();
        }
        return false;
    }

    private String removeDelimiters(String instr) {
        final String space = "" + (char) 32;
        final String tab = "" + (char) 9;
        final String endln = "" + (char) 13;
        final String cr = "" + (char) 10;
        final String comma = "" + (char) 44;
        String outstr = instr;
        outstr = outstr.replace((char) 9, (char) 32);
        outstr = outstr.replace((char) 13, (char) 32);
        outstr = outstr.replace((char) 10, (char) 32);
        outstr = outstr.replace((char) 44, (char) 32);
        return outstr.trim();
    }

    public void updateServer(World aWorld) {
        System.out.println("Entering updateServer()");
        int sizeL = aWorld.leftTeam.size();
        int sizeR = aWorld.rightTeam.size();
        int size = Math.max(sizeL, sizeR);
        int count = 0;
        for (int i = 0; i < size; i++) {
            Vector playersL = new Vector();
            if (i < sizeL) {
                Player plr = (Player) aWorld.leftTeam.elementAt(i);
                playersL.addElement(plr);
            }
            Vector playersR = new Vector();
            if (i < sizeR) {
                Player plr = (Player) aWorld.rightTeam.elementAt(i);
                playersR.addElement(plr);
            }
            if (!sendViewPacket(aWorld.ball, playersL, playersR)) return;
            count++;
        }
        System.out.println("Total " + count + " Packet.VIEW sent.");
        int newTime = 1500;
        sendSituationPacket(newTime);
        System.out.println("Packet.SITUATION sent.");
    }

    private boolean sendViewPacket(Ball ball, Vector leftT, Vector rightT) {
        boolean success = false;
        ViewData aViewData = new ViewData(100, ball, leftT, rightT);
        Packet infoToSend = new Packet(Packet.VIEW, aViewData, aSoccerMaster.getAddress(), aSoccerMaster.getPort());
        try {
            aSoccerMaster.getTransceiver().send(infoToSend);
            success = true;
        } catch (IOException ie) {
            System.out.println("Error sending Packet.VIEW " + ie);
        }
        return success;
    }

    private boolean sendSituationPacket(int time) {
        boolean success = false;
        SituationData aSituationData = new SituationData(time, numOfSteps, numOfReplicas);
        Packet infoToSend = new Packet(Packet.SITUATION, aSituationData, aSoccerMaster.getAddress(), aSoccerMaster.getPort());
        try {
            aSoccerMaster.getTransceiver().send(infoToSend);
            success = true;
        } catch (IOException ie) {
            System.out.println("Error sending Packet.SITUATION " + ie);
        }
        return success;
    }
}
