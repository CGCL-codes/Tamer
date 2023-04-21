package jchessboard;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;

/**
 * This class the represents the conntection indicator shown in the lower 
 * right corner.
 */
class ConnectionIndicator extends JLabel {

    public static String getVersion() {
        return "$Id: ConnectionIndicator.java 5 2009-11-10 07:56:47Z cdivossen $";
    }

    private Color noConnectionColor, connectedColor, waitingColor;

    private Color waitingColor2, errorColor;

    private boolean isWaiting = false;

    private boolean i = false;

    private java.awt.event.ActionListener listener = new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            toggleColor();
        }
    };

    private javax.swing.Timer toggleTimer = new javax.swing.Timer(250, listener);

    private void toggleColor() {
        if (i) setBackground(waitingColor); else setBackground(waitingColor2);
        i = !i;
    }

    /**
	 * Makes the ConnectionIndicator go in the connected state.
	 */
    public void setReady() {
        setBackground(connectedColor);
        setToolTipText("Connected");
        toggleTimer.stop();
        isWaiting = false;
    }

    /**
	 * Sets this ConnectionIndicator to the error state.
	 */
    public void setError() {
        setBackground(errorColor);
        setToolTipText("Error!");
        toggleTimer.stop();
        isWaiting = false;
    }

    /**
	 * Sets this ConnectionIndicator to the waiting state.
	 * The ConnectionIndicator will blink while waiting.
	 */
    public void setWaiting() {
        setToolTipText("Waiting...");
        isWaiting = true;
        toggleTimer.start();
    }

    /**
	 * Sets this ConnectionIndicator to not-connected state.
	 */
    public void setNoConnection() {
        setToolTipText("Not connected");
        isWaiting = false;
        toggleTimer.stop();
        setBackground(noConnectionColor);
    }

    /**
	 * Returns true if this ConnectionIndicator is in the waiting state.
	 */
    public boolean isWaiting() {
        return isWaiting;
    }

    /**
	 * Creates a new instance of ConnectionIndicator.
	 */
    public ConnectionIndicator() {
        setPreferredSize(new Dimension(15, 15));
        setOpaque(true);
        setBorder(javax.swing.border.LineBorder.createBlackLineBorder());
        noConnectionColor = getBackground();
        connectedColor = new Color(50, 170, 50);
        waitingColor = new Color(255, 255, 80);
        waitingColor2 = new Color(180, 180, 50);
        errorColor = new Color(170, 50, 50);
        setNoConnection();
    }
}
