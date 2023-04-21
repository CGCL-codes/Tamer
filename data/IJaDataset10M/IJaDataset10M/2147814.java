package org.spotit;

/**
 * A simple bubble that only focuses on a single component only
 */
public class SimpleBubble extends AbstractBubble {

    public void display() {
        setVisible(true);
    }

    public void dismiss() {
        setVisible(false);
        dispose();
    }

    public static void main(String... args) throws Exception {
        Bubble b = new SimpleBubble();
        b.display();
        Thread.sleep(3000);
        b.dismiss();
    }
}
