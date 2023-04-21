package ora4mas.nopl.simulator;

import jason.asSyntax.ASSyntax;
import jason.util.asl2html;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import ora4mas.nopl.OrgArt;
import c4jason.CAgentArch;
import cartago.ArtifactId;
import cartago.ArtifactInfo;
import cartago.CartagoService;
import cartago.ICartagoContext;
import cartago.ICartagoController;
import cartago.OpDescriptor;

public class AgentGUI {

    jason.asSemantics.Agent jasonAg;

    CAgentArch cartagoArch;

    ICartagoContext ctxt;

    ICartagoController ctrl;

    asl2html agTransformer = new asl2html("/xml/agInspection.xsl");

    public AgentGUI(final jason.asSemantics.Agent jasonAg, CAgentArch cartagoArch) {
        this.jasonAg = jasonAg;
        this.cartagoArch = cartagoArch;
        try {
            ctrl = CartagoService.getController("default");
            initGUI();
            SimulatorGUI.getInstance().addAg(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread() {

            public void run() {
                String lastBels = "";
                while (true) {
                    try {
                        Thread.sleep(1000);
                        String sMind = agTransformer.transform(jasonAg.getAgState());
                        if (!sMind.equals(lastBels)) txtBels.setText(sMind);
                        lastBels = sMind;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    void execCartagoOp(final String art, final String op, final String args) throws Exception {
        System.out.println("start doing " + op + ", arguments=" + args);
        jasonAg.addBel(ASSyntax.createLiteral("using", ASSyntax.createString(art)));
        jasonAg.getTS().getC().addAchvGoal(ASSyntax.createLiteral("doOrgAct", ASSyntax.createString(art), ASSyntax.createAtom(op), ASSyntax.parseList("[" + args + "]")), null);
    }

    static int guiCount = 0;

    JFrame frame;

    DefaultComboBoxModel artsCBmodel = new DefaultComboBoxModel();

    DefaultComboBoxModel opsCBmodel = new DefaultComboBoxModel();

    JComboBox artsCB = new JComboBox(artsCBmodel);

    JComboBox opsCB = new JComboBox(opsCBmodel);

    JTextPane txtBels = new JTextPane();

    JTextField argsTF = new JTextField(20);

    void initGUI() throws Exception {
        frame = new JFrame("-- " + jasonAg.getTS().getUserAgArch().getAgName() + " --");
        initArtsCBmodel();
        artsCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    initOpsCBmodel();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        initOpsCBmodel();
        JPanel optionsp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionsp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Operations", TitledBorder.LEFT, TitledBorder.TOP));
        final JButton runBT = new JButton("run");
        runBT.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String op = opsCB.getSelectedItem().toString();
                    int pos = op.indexOf("/");
                    if (pos > 0) op = op.substring(0, pos).trim();
                    execCartagoOp(artsCB.getSelectedItem().toString(), op, argsTF.getText());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        optionsp.add(artsCB);
        optionsp.add(opsCB);
        optionsp.add(argsTF);
        optionsp.add(runBT);
        txtBels.setContentType("text/html");
        txtBels.setEditable(false);
        txtBels.setAutoscrolls(false);
        JPanel plog = new JPanel(new BorderLayout());
        plog.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beliefs", TitledBorder.LEFT, TitledBorder.TOP));
        plog.add(BorderLayout.CENTER, new JScrollPane(txtBels));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.NORTH, optionsp);
        frame.getContentPane().add(BorderLayout.CENTER, plog);
        frame.setSize(800, 400);
        guiCount = guiCount + 30;
        frame.setLocation(guiCount + 100, guiCount + 50);
        frame.setVisible(true);
    }

    public void initArtsCBmodel() throws Exception {
        artsCBmodel.removeAllElements();
        for (ArtifactId artId : ctrl.getCurrentArtifacts()) {
            ArtifactInfo info = ctrl.getArtifactInfo(artId.getName());
            @SuppressWarnings("rawtypes") Class artClass = Class.forName(info.getId().getArtifactType());
            if (artClass.getSuperclass().getName().equals(OrgArt.class.getName())) {
                artsCBmodel.addElement(artId.getName());
            }
        }
    }

    public void initOpsCBmodel() throws Exception {
        opsCBmodel.removeAllElements();
        if (artsCB.getSelectedItem() != null) {
            try {
                for (OpDescriptor opName : ctrl.getArtifactInfo(artsCB.getSelectedItem().toString()).getOperations()) {
                    opsCBmodel.addElement(opName.getOp().getName());
                }
                cartagoArch.getEnvSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
