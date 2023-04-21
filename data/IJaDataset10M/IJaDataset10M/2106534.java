package ac.hiu.j314.elmve.comp;

import ac.hiu.j314.elmve.*;
import ac.hiu.j314.elmve.ui.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.*;

public class EContainerCust extends CustomizerBase implements ActionListener {

    JFrame frame;

    JTextField widthTF;

    JTextField heightTF;

    JButton doneButton;

    public void startProcessing(Order o) {
        super.startProcessing(o);
        frame = new JFrame("EContainerCust");
        Box box = Box.createVerticalBox();
        Box b1 = Box.createHorizontalBox();
        b1.add(new JLabel("X:"));
        widthTF = new JTextField(10);
        widthTF.addActionListener(this);
        b1.add(widthTF);
        box.add(b1);
        Box b2 = Box.createHorizontalBox();
        b2.add(new JLabel("Y:"));
        heightTF = new JTextField(10);
        heightTF.addActionListener(this);
        b2.add(heightTF);
        box.add(b2);
        doneButton = new JButton("Done");
        doneButton.addActionListener(this);
        box.add(doneButton);
        frame.getContentPane().add(box);
        frame.pack();
        frame.show();
        Request r = makeRequest(elm, "getWH", null);
        prepareForReply(r, "setWH", null);
        sendMessage(r);
    }

    public void setWH(ReplySet rs) {
        widthTF.setText("" + rs.getReplyAt(0).getDoubleArgAt(0));
        heightTF.setText("" + rs.getReplyAt(0).getDoubleArgAt(1));
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == widthTF) {
            Double d = new Double(widthTF.getText());
            sendMessage(makeOrder(elm, "setContainerWidth", d));
        } else if (ae.getSource() == heightTF) {
            Double d = new Double(heightTF.getText());
            sendMessage(makeOrder(elm, "setContainerHeight", d));
        } else if (ae.getSource() == doneButton) {
            frame.dispose();
            dispose();
        }
    }
}
