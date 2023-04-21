package model.visitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import model.AngaNode;
import model.CoreoNode;
import model.SadhanaNode;
import model.TechniqueNode;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * Visitor for sadhana nodes, plays ping.wav on each technique and waits
 * for the next.
 * @author luigi
 * 
 */
public class PingVisitor extends AbstractSadhanaVisitor {

    /**
	 * milliseconds constant.
	 */
    private static final long MILLISECONDS = 1000;

    /**
	 * {@inheritDoc}
	 */
    public void visit(final SadhanaNode node) {
        for (final Enumeration children = node.children(); children.hasMoreElements(); ) {
            this.visit(children.nextElement());
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void visit(final AngaNode node) {
        for (final Enumeration children = node.children(); children.hasMoreElements(); ) {
            this.visit(children.nextElement());
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void visit(final CoreoNode node) {
        for (final Enumeration children = node.children(); children.hasMoreElements(); ) {
            this.visit(children.nextElement());
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public void visit(final TechniqueNode node) {
        InputStream in = null;
        try {
            in = new FileInputStream("media/ping.wav");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AudioStream as = null;
        try {
            as = new AudioStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(node.getTime() * MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AudioPlayer.player.start(as);
        if (node.isCompensated()) {
            try {
                Thread.sleep(node.getTime() * MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AudioPlayer.player.start(as);
        }
    }
}
