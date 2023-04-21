package net.matmas.pneditor.functions.coverability;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.matmas.pnapi.PetriNet;
import net.matmas.pneditor.PNEditor;

public class CheckCoverability extends JFrame implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private ArrayList<JTextField> marking;

    private JLabel order;

    private JButton check;

    private JTextArea result;

    private Graph graph;

    private Net net;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JPanel markingPanel;

    public CheckCoverability(Graph graph, Net net) {
        super();
        this.setTitle("Check Coverability");
        this.graph = graph;
        this.net = net;
        order = new JLabel("( ");
        markingPanel = new JPanel(new GridLayout(1, net.getPlaces().size()));
        check = new javax.swing.JButton("Check");
        jScrollPane1 = new javax.swing.JScrollPane();
        result = new javax.swing.JTextArea();
        setAlwaysOnTop(true);
        setName("Check Coverability");
        setResizable(false);
        order.setName("order");
        markingPanel.setName("markingPanel");
        markingPanel.setLayout(new java.awt.GridLayout(1, this.net.getPlaces().size()));
        check.setName("check");
        check.setActionCommand("check");
        check.addActionListener(this);
        jScrollPane1.setName("jScrollPane1");
        result.setColumns(20);
        result.setRows(5);
        result.setLineWrap(true);
        result.setName("result");
        jScrollPane1.setViewportView(result);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE).addComponent(markingPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(order, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addGap(89, 89, 89).addComponent(check))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(order, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(markingPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(check).addGap(18, 18, 18).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        this.marking = new ArrayList<JTextField>();
        String label = "( ";
        PetriNet petriNet = PNEditor.getInstance().getDocument().getPetriNet();
        int placeOrder = 0;
        for (net.matmas.pnapi.Place places : petriNet.getPlaces()) {
            placeOrder++;
            if (places.getLabel().getText() == null) label += "p" + placeOrder + ","; else label += places.getLabel().getText() + ",";
            this.marking.add(new JTextField());
        }
        label = label.substring(0, label.length() - 1);
        label += " )";
        this.order.setText(label);
        for (JTextField jt : this.marking) {
            jt.setSize(this.getWidth() / net.getPlaces().size(), 20);
            this.markingPanel.add(jt);
        }
        pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("check".equals(e.getActionCommand())) {
            int[] marks = new int[100];
            for (int i = 0; i < net.getPlaces().size(); i++) {
                marks[i] = Integer.valueOf(this.marking.get(i).getText());
            }
            Marking marking = new Marking(marks, net);
            this.result.setText(Coverability.findMarking(this.net, this.graph, marking));
        }
    }
}
