package de.dirkdittmar.flickr.group.comment.ui.worker;

import java.awt.EventQueue;
import javax.swing.JProgressBar;
import de.dirkdittmar.flickr.group.comment.ui.PhotoPanel;

abstract class AbstractThumbPhotoLoader extends AbstractWorker {

    private JProgressBar progressBar = new JProgressBar();

    private ThumbPhotoContainer container;

    private String text;

    AbstractThumbPhotoLoader(ThumbPhotoContainer container, String text) {
        this.container = container;
        this.text = text;
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString(text + "...");
    }

    protected void removeProgressBar() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                container.removeStatusBarComponent(progressBar);
            }
        });
    }

    protected void updateLoadStatus(final int max, final int val) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                progressBar.setIndeterminate(false);
                progressBar.setString(text + " " + val + "/" + max);
                progressBar.setMaximum(max);
                progressBar.setValue(val);
            }
        });
    }

    protected void addPhoto(final PhotoPanel<?> panel) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                container.addPhoto(panel);
            }
        });
    }

    protected void addProgressBar() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                container.addStatusBarComponent(progressBar);
            }
        });
    }
}
