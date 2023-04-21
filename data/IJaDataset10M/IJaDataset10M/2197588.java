package net.kano.joustsim.app.forms;

import net.kano.joscar.DefensiveTools;
import net.kano.joustsim.app.GuiSession;
import net.kano.joustsim.oscar.AimConnection;
import net.kano.joustsim.oscar.AimSession;
import net.kano.joustsim.oscar.State;
import net.kano.joustsim.oscar.StateEvent;
import net.kano.joustsim.oscar.StateListener;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class SignonProgressBox extends JPanel implements SignonWindowBox {

    private JPanel mainPanel;

    private JLabel signingOnLabel;

    private JList progressList;

    private final GuiSession guiSession;

    private AimSession aimSession;

    private AimConnection conn;

    private ProgressListModel progressListModel;

    private final Icon notStartedIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/progress-item-not-started.png"));

    private final Icon succeededIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/progress-item-succeeded.png"));

    private final Icon workingIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/progress-item-working.png"));

    private SignonWindow signonWindow = null;

    {
        setLayout(new BorderLayout());
        add(mainPanel);
        progressList.setCellRenderer(new ProgressListRenderer());
    }

    public SignonProgressBox(GuiSession guiSession) {
        DefensiveTools.checkNull(guiSession, "guiSession");
        this.guiSession = guiSession;
        updateSession();
    }

    public Component getSignonWindowBoxComponent() {
        return this;
    }

    public void signonWindowBoxShown(SignonWindow window) {
        signonWindow = window;
        updateGui();
    }

    public void updateSession() {
        aimSession = guiSession.getAimSession();
        conn = guiSession.getAimConnection();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                updateGui();
            }
        });
    }

    private void updateGui() {
        if (progressListModel != null) progressListModel.stop();
        progressListModel = null;
        AimConnection conn = this.conn;
        if (conn != null) {
            signingOnLabel.setText("Signing on " + conn.getScreenname() + "...");
            progressListModel = new ProgressListModel(conn);
            progressList.setModel(progressListModel);
        }
        signonWindow.updateSize(this);
    }

    private static class ProgressListModel extends AbstractListModel {

        private final StateInfo[] states = new StateInfo[] { new StateInfo(State.CONNECTINGAUTH), new StateInfo(State.AUTHORIZING), new StateInfo(State.CONNECTING), new StateInfo(State.SIGNINGON), new StateInfo(State.ONLINE) };

        private final AimConnection conn;

        private StateListener stateListener = new StateListener() {

            public void handleStateChange(StateEvent event) {
                State state = event.getNewState();
                updateState(state);
            }
        };

        public ProgressListModel(AimConnection conn) {
            this.conn = conn;
            conn.addStateListener(stateListener);
            updateState(conn.getState());
        }

        private void updateState(State state) {
            int index = -1;
            for (int i = 0; i < states.length; i++) {
                StateInfo stateInfo = states[i];
                if (stateInfo.getState() == state) {
                    stateInfo.setDoing(true);
                    stateInfo.setDone(false);
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return;
            }
            for (int i = index - 1; i >= 0; i--) {
                StateInfo stateInfo = states[i];
                stateInfo.setDoing(false);
                stateInfo.setDone(true);
            }
            fireContentsChanged(this, 0, index);
        }

        public void stop() {
            conn.removeStateListener(stateListener);
        }

        public int getSize() {
            return states.length;
        }

        public Object getElementAt(int index) {
            return states[index];
        }

        public class StateInfo {

            private final State state;

            private boolean doing = false;

            private boolean done = false;

            public StateInfo(State state) {
                this.state = state;
            }

            public State getState() {
                return state;
            }

            public synchronized boolean isDoing() {
                return doing;
            }

            public synchronized void setDoing(boolean doing) {
                this.doing = doing;
            }

            public synchronized boolean isDone() {
                return done;
            }

            public synchronized void setDone(boolean done) {
                this.done = done;
            }
        }
    }

    private class ProgressListRenderer extends DefaultListCellRenderer {

        private Font origFont = getFont().deriveFont(Font.PLAIN);

        private Font boldFont = origFont.deriveFont(Font.BOLD);

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof ProgressListModel.StateInfo) {
                ProgressListModel.StateInfo si = (ProgressListModel.StateInfo) value;
                if (si.isDoing()) setFont(boldFont); else setFont(origFont);
                if (si.isDone() || si.isDoing()) setForeground(Color.BLACK); else setForeground(Color.GRAY);
                if (si.isDone()) setIcon(succeededIcon); else if (si.isDoing()) setIcon(workingIcon); else setIcon(notStartedIcon);
                setText(getTextFromState(si));
            } else {
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
            return this;
        }

        private String getTextFromState(ProgressListModel.StateInfo si) {
            State state = si.getState();
            String text;
            if (state == State.CONNECTINGAUTH) {
                text = "Connecting to authorization server";
            } else if (state == State.AUTHORIZING) {
                text = "Sending username and password";
            } else if (state == State.CONNECTING) {
                text = "Connecting to AIM server";
            } else if (state == State.SIGNINGON) {
                text = "Signing on";
            } else if (state == State.ONLINE) {
                text = "Online";
            } else {
                text = "State: " + state;
            }
            return text;
        }
    }
}
