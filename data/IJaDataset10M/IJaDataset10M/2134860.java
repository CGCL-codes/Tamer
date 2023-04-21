package net.dromard.picasaweb.albumdownloader.controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import net.dromard.common.rss.RSSFeedReader;
import net.dromard.common.rss.feed.Enclosure;
import net.dromard.common.rss.feed.Item;
import net.dromard.common.rss.feed.RSS;
import net.dromard.picasaweb.albumdownloader.gui.JErrorsGlassPane;
import net.dromard.picasaweb.albumdownloader.gui.JPicasaWebAlbumDownloader;
import net.dromard.picasaweb.albumdownloader.gui.JProgressBarGlassPane;
import net.dromard.picasaweb.albumdownloader.resources.Messages;

public class PicasaWebAlbumDownloaderControler implements ActionListener, Runnable {

    private JPicasaWebAlbumDownloader frame;

    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals(JPicasaWebAlbumDownloader.DOWNLOAD_ACTION)) {
            new Thread(this).start();
        } else if (event.getActionCommand().equals(JPicasaWebAlbumDownloader.HIDE_GLASSPANE)) {
            removeGlassPane();
        }
    }

    public void run() {
        try {
            JProgressBarGlassPane glassPane = new JProgressBarGlassPane();
            glassPane.setSize(frame.getContentPane().getSize());
            glassPane.getProgressBar().setMinimum(0);
            setGlassPane(glassPane);
            glassPane.setString(Messages.getString("PicasaWebAlbumDownloaderControler.loading.feed"));
            glassPane.getProgressBar().setIndeterminate(true);
            RSSFeedReader reader = new RSSFeedReader();
            RSS rss = reader.load(new URL(frame.getURLFeed()));
            glassPane.setString(Messages.getString("PicasaWebAlbumDownloaderControler.downloading"));
            glassPane.getProgressBar().setStringPainted(true);
            glassPane.getProgressBar().setIndeterminate(false);
            glassPane.getProgressBar().setMaximum(rss.getChannel().getItems().size() + 1);
            glassPane.getProgressBar().setValue(0);
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                List<Exception> errors = new ArrayList<Exception>();
                for (Item item : rss.getChannel().getItems()) {
                    glassPane.getProgressBar().setString(glassPane.getProgressBar().getValue() + "/" + rss.getChannel().getItems().size());
                    glassPane.getProgressBar().setValue(glassPane.getProgressBar().getValue() + 1);
                    Enclosure enclosure = item.getEnclosure();
                    if (enclosure != null && enclosure.getType().equalsIgnoreCase("image/jpeg")) {
                        try {
                            download(enclosure.getUrl(), fileChooser.getSelectedFile() + File.separator + item.getTitle());
                        } catch (Exception e) {
                            e.printStackTrace();
                            errors.add(e);
                        }
                    }
                }
                removeGlassPane();
                if (errors.size() > 0) displayErrors(errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            displayError(e);
        } finally {
            frame.setUrlFeed("");
        }
    }

    private void displayErrors(List<Exception> errors) {
        JErrorsGlassPane glassPane = new JErrorsGlassPane();
        glassPane.setControler(this);
        glassPane.setSize(frame.getContentPane().getSize());
        setGlassPane(glassPane);
        String msg = "<html><b>" + Messages.getString("PicasaWebAlbumDownloaderControler.errors.title") + "</b><ul>";
        for (Exception error : errors) msg += "<li>" + error.getMessage() + "</li>";
        msg += "</ul></html>";
        glassPane.setString(msg);
    }

    private void displayError(Exception error) {
        JErrorsGlassPane glassPane = new JErrorsGlassPane();
        glassPane.setControler(this);
        glassPane.setSize(frame.getContentPane().getSize());
        setGlassPane(glassPane);
        String msg = "<html><b>" + Messages.getString("PicasaWebAlbumDownloaderControler.errors.title") + "</b><ul>";
        msg += "<li>" + error.getMessage() + "</li>";
        msg += "</ul></html>";
        glassPane.setString(msg);
    }

    private void download(String url, String destinationFile) throws MalformedURLException, IOException {
        FileOutputStream outputStream = new FileOutputStream(destinationFile);
        InputStream inputStream = null;
        try {
            inputStream = new URL(url).openStream();
            streamCopier(inputStream, outputStream);
        } catch (IOException ex) {
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
            throw ex;
        }
    }

    public void setFrame(JPicasaWebAlbumDownloader frame) {
        this.frame = frame;
        frame.setControler(this);
    }

    private void setGlassPane(JPanel glassPane) {
        frame.setGlassPane(glassPane);
        frame.getGlassPane().setVisible(true);
    }

    private void removeGlassPane() {
        frame.getGlassPane().setVisible(false);
    }

    /** Buffer size. */
    private static final int BUFFER_SIZE = 1024;

    /**
     * This static method copy the input stream into the output stream.
     * @param in The InputStream, where to read data
     * @param out The Output stream, where to write data
     * @throws IOException Occurred if you did a mistake in the given parameters ...
     */
    private static void streamCopier(final InputStream in, final OutputStream out) throws IOException {
        int len;
        byte[] b = new byte[BUFFER_SIZE];
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        in.close();
        out.close();
    }
}
