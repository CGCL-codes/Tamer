package fairVote.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import fairVote.SeggioApplet;

class ActionBackToVote implements ActionListener {

    private SeggioApplet applet;

    private VoteContainer jvote;

    private JPanel jactual;

    public ActionBackToVote(SeggioApplet applet, VoteContainer jvote, JPanel jactual) {
        this.applet = applet;
        this.jvote = jvote;
        this.jactual = jactual;
    }

    public void actionPerformed(ActionEvent ae) {
        jactual.setVisible(false);
        jvote.remove(jactual);
        jvote.add(jvote.jv, BorderLayout.CENTER);
        jvote.jv.setVisible(true);
    }
}
