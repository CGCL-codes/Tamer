package net.sf.jmp3renamer.plugins.CoverGrabber.gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import net.sf.jmp3renamer.FileManager;
import net.sf.jmp3renamer.I18N;
import net.sf.jmp3renamer.Main;
import net.sf.jmp3renamer.datamanager.DataManager;
import net.sf.jmp3renamer.datamanager.DataSet;
import net.sf.jmp3renamer.plugins.CoverGrabber.Cover;
import net.sf.jmp3renamer.plugins.CoverGrabber.CoverGrabber;
import net.sf.jmp3renamer.plugins.CoverGrabber.CoverImpl;
import net.sf.jmp3renamer.plugins.CoverGrabber.CoverProvider;
import net.sf.jmp3renamer.plugins.CoverGrabber.NoProviderException;
import net.sf.jmp3renamer.plugins.CoverGrabber.ProviderTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please
 * visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS
 * MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class CoverGrabberPanel extends JPanel implements ActionListener, Observer {

    private static transient Logger logger = LoggerFactory.getLogger(CoverGrabberPanel.class);

    private JLabel lAlbum = new JLabel(I18N.translate("album"));

    private JLabel lArtist = new JLabel(I18N.translate("artist"));

    private JTextField tAlbum = new JTextField();

    private JTextField tArtist = new JTextField();

    private JButton getCoverButton = new JButton(I18N.translate("get_cover"));

    private JButton saveCoverButton = new JButton(I18N.translate("save"));

    private JProgressBar progress = new JProgressBar(0, 100);

    private JPanel resultPanel = new JPanel();

    private JScrollPane scroll = new JScrollPane(resultPanel);

    private JLabel coverPreview = new JLabel();

    private List<Cover> images = new ArrayList<Cover>();

    private TitledBorder titledBorder = BorderFactory.createTitledBorder(I18N.translate("preview"));

    private ProviderTracker providerTracker;

    private CoverWrapper currentCover;

    private final int THUMB_SIZE = 64;

    public CoverGrabberPanel(ProviderTracker providerTracker) {
        this.providerTracker = providerTracker;
        initGUI();
        FileManager.getInstance().addObserver(this);
        DataManager.getInstance().addObserver(this);
    }

    private void initGUI() {
        GridBagLayout thisLayout = new GridBagLayout();
        this.setLayout(thisLayout);
        this.add(lArtist, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(tArtist, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(lAlbum, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(tAlbum, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(getCoverButton, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(progress, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        this.add(scroll, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        this.add(coverPreview, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        this.add(saveCoverButton, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        getCoverButton.addActionListener(this);
        progress.setVisible(false);
        progress.setStringPainted(true);
        progress.setIndeterminate(true);
        coverPreview.setBorder(titledBorder);
        coverPreview.setPreferredSize(new java.awt.Dimension(270, 270));
        coverPreview.setVisible(false);
        coverPreview.setHorizontalAlignment(SwingConstants.CENTER);
        saveCoverButton.setVisible(false);
        scroll.setBorder(BorderFactory.createTitledBorder(I18N.translate("search_result")));
        scroll.getVerticalScrollBar().setUnitIncrement(10);
        resultPanel.setLayout(new GridLayout(0, 3, 10, 10));
        saveCoverButton.addActionListener(this);
        tAlbum.addActionListener(this);
        tArtist.addActionListener(this);
        thisLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0 };
        thisLayout.rowHeights = new int[] { 7, 7, 7, 7, 7, 7, 7 };
        thisLayout.columnWeights = new double[] { 0.0, 1.0 };
        thisLayout.columnWidths = new int[] { 7, 7 };
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getCoverButton) {
            getCovers();
        } else if (e.getSource() == saveCoverButton) {
            saveCover();
        } else if (e.getSource() == tAlbum || e.getSource() == tArtist) {
            getCovers();
        }
    }

    private void setSearching(boolean searching) {
        if (searching) {
            tArtist.setEnabled(false);
            tAlbum.setEnabled(false);
            progress.setValue(0);
            getCoverButton.setVisible(false);
            progress.setVisible(true);
            Main.getGUI().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        } else {
            Main.getGUI().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            getCoverButton.setVisible(true);
            progress.setVisible(false);
            tArtist.setEnabled(true);
            tAlbum.setEnabled(true);
        }
    }

    private void getCovers() {
        final String artist = tArtist.getText();
        final String album = tAlbum.getText();
        new Thread() {

            @Override
            public void run() {
                setSearching(true);
                try {
                    logger.info("Searching for covers...");
                    progress.setString(I18N.translate("searching"));
                    try {
                        images = getCovers(artist, album);
                    } catch (NoProviderException e) {
                        String msg = e.getLocalizedMessage();
                        JOptionPane.showMessageDialog(Main.getGUI(), msg);
                        logger.info(msg);
                        setSearching(false);
                        return;
                    }
                    logger.info("Downloading possible covers...");
                    progress.setString(I18N.translate("downloading_thumbs"));
                    int counter = 0;
                    ExecutorService pool = Executors.newFixedThreadPool(10);
                    for (Iterator<Cover> iter = images.iterator(); iter.hasNext(); counter++) {
                        final CoverWrapper cover = (CoverWrapper) iter.next();
                        Runnable downloader = new Runnable() {

                            public void run() {
                                try {
                                    logger.debug("Fetching {}", cover.getThumbUri());
                                    HttpURLConnection http = (HttpURLConnection) new URL(cover.getThumbUri()).openConnection();
                                    http.setRequestProperty("User-Agent", "JMP3Renamer CoverGrabber " + CoverGrabber.VERSION);
                                    InputStream in = http.getInputStream();
                                    int length = -1;
                                    byte[] buffer = new byte[1024];
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    while ((length = in.read(buffer)) > 0) {
                                        bos.write(buffer, 0, length);
                                    }
                                    http.disconnect();
                                    ImageIcon thumb = new ImageIcon(bos.toByteArray());
                                    cover.setThumb(thumb);
                                } catch (Exception e) {
                                    logger.error("Couldn't download cover", e);
                                }
                            }
                        };
                        pool.submit(downloader);
                    }
                    pool.shutdown();
                    pool.awaitTermination(1, TimeUnit.MINUTES);
                    pool.shutdownNow();
                    progress.setString(I18N.translate("scaling_thumbs"));
                    progress.setIndeterminate(true);
                    if (images.size() > 0) {
                        coverPreview.setVisible(true);
                        resultPanel.removeAll();
                        for (final Cover cover : images) {
                            final CoverWrapper wrap = (CoverWrapper) cover;
                            JLabel thumb = new JLabel();
                            thumb.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            thumb.setToolTipText("<html>" + I18N.translate("source", wrap.getProvider().getName()) + "<br>" + "URL: " + wrap.getImageUri() + "</html>");
                            ImageIcon preview = new ImageIcon(wrap.getThumb().getImage().getScaledInstance(THUMB_SIZE, THUMB_SIZE, Image.SCALE_SMOOTH));
                            addProviderIcon(preview, wrap.getProvider());
                            thumb.setIcon(preview);
                            resultPanel.add(thumb);
                            thumb.addMouseListener(new MouseAdapter() {

                                @Override
                                public void mouseClicked(java.awt.event.MouseEvent e) {
                                    if (wrap.getImage() == null) {
                                        progress.setVisible(true);
                                        new Thread() {

                                            @Override
                                            public void run() {
                                                progress.setString(I18N.translate("downloading_cover"));
                                                loadImage(wrap);
                                                showCover(wrap);
                                                saveCoverButton.setVisible(true);
                                            }
                                        }.start();
                                    } else {
                                        showCover(wrap);
                                        saveCoverButton.setVisible(true);
                                    }
                                }
                            });
                        }
                        progress.setString(I18N.translate("downloading_first"));
                        loadImage((CoverWrapper) images.get(0));
                        showCover((CoverWrapper) images.get(0));
                        saveCoverButton.setVisible(true);
                    } else {
                        String mesg = I18N.translate("no_covers_found");
                        logger.info(mesg);
                        JOptionPane.showMessageDialog(Main.getGUI(), mesg);
                    }
                } catch (Exception e) {
                    logger.error(I18N.translate("error.load_covers"), e);
                }
                setSearching(false);
            }
        }.start();
    }

    private void addProviderIcon(ImageIcon preview, CoverProvider provider) {
        Icon icon = provider.getIcon();
        if (icon == null) {
            return;
        }
        BufferedImage bimg = new BufferedImage(THUMB_SIZE, THUMB_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bimg.getGraphics();
        preview.paintIcon(null, g, 0, 0);
        icon.paintIcon(null, g, THUMB_SIZE - icon.getIconWidth(), THUMB_SIZE - icon.getIconHeight());
        preview.setImage(bimg);
    }

    ;

    private void loadImage(CoverWrapper wrap) {
        try {
            HttpURLConnection http = (HttpURLConnection) new URL(wrap.getImageUri()).openConnection();
            http.setRequestProperty("User-Agent", "JMP3Renamer CoverGrabber " + CoverGrabber.VERSION);
            InputStream in = http.getInputStream();
            int length = -1;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while ((length = in.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            http.disconnect();
            ImageIcon image = new ImageIcon(bos.toByteArray());
            wrap.setImage(image);
        } catch (Exception e1) {
            logger.error("Couldn't load cover", e1);
        } finally {
            progress.setVisible(false);
        }
    }

    private void saveCover() {
        if (FileManager.getInstance().getFiles().size() > 0) {
            File mp3 = FileManager.getInstance().getFiles().get(0);
            File dir = mp3.getParentFile();
            File cover = new File(dir, CoverGrabber.getProperty("filename"));
            if (cover.exists()) {
                String msg = I18N.translate("cover_exists");
                logger.warn(msg);
                int selection = JOptionPane.showConfirmDialog(Main.getGUI(), msg);
                if (selection == JOptionPane.YES_OPTION) {
                    boolean deleted = cover.delete();
                    if (!deleted) {
                        logger.error(I18N.translate("error.couldnt_delete"));
                        return;
                    }
                } else {
                    return;
                }
            }
            ImageIcon icon = currentCover.getImage();
            BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
            icon.paintIcon(null, image.createGraphics(), 0, 0);
            try {
                ImageIO.write(image, "JPG", cover);
            } catch (IOException e) {
                logger.error(I18N.translate("error.save_image"), e);
            }
            FileManager.getInstance().addAdditionalFile(cover);
        } else {
            logger.error(I18N.translate("error.no_dir_selected"));
        }
    }

    private void showCover(CoverWrapper wrap) {
        if (wrap == null) {
            return;
        }
        currentCover = wrap;
        ImageIcon preview = new ImageIcon(wrap.getImage().getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH));
        coverPreview.setIcon(preview);
        coverPreview.setVisible(true);
        if (wrap.getProvider() != null && wrap.getImageUri() != null) {
            coverPreview.setToolTipText("<html>" + I18N.translate("source", wrap.getProvider().getName()) + "<br>" + "URL: " + wrap.getImageUri() + "</html>");
        }
        titledBorder.setTitle(I18N.translate("preview") + " " + wrap.getImage().getIconWidth() + "x" + wrap.getImage().getIconHeight());
    }

    private void showExistingCover(File coverFile) {
        ImageIcon cover = loadCover(coverFile);
        if (cover != null) {
            CoverImpl c = new CoverImpl();
            CoverWrapper wrap = new CoverWrapper(c, null);
            wrap.setImage(cover);
            showCover(wrap);
            titledBorder.setTitle(I18N.translate("current_cover"));
        }
    }

    private ImageIcon loadCover(File coverFile) {
        ImageIcon cover = null;
        try {
            cover = new ImageIcon(coverFile.toURI().toURL());
        } catch (Exception e) {
            logger.error(I18N.translate("error.load_existing"), e);
        }
        return cover;
    }

    private File cover;

    @SuppressWarnings("unchecked")
    public void update(Observable subject, Object obj) {
        if (subject == FileManager.getInstance()) {
            saveCoverButton.setVisible(false);
            Vector<File> vector = (Vector<File>) obj;
            if (vector.size() > 0) {
                File file = vector.elementAt(0);
                File dir = file.getParentFile();
                cover = new File(dir, CoverGrabber.getProperty("filename"));
                if (cover.exists()) {
                    FileManager.getInstance().addAdditionalFile(cover);
                    showExistingCover(cover);
                } else {
                    coverPreview.setVisible(false);
                    coverPreview.setIcon(null);
                }
            }
        } else if (subject == DataManager.getInstance()) {
            List<DataSet> datasets = (List<DataSet>) obj;
            for (DataSet dataset : datasets) {
                if (!"".equals(dataset.getArtist())) {
                    tArtist.setText(dataset.getArtist());
                }
                if (!"".equals(dataset.getAlbum())) {
                    tAlbum.setText(dataset.getAlbum());
                }
            }
        }
    }

    private List<Cover> getCovers(String artist, String album) throws NoProviderException {
        List<Cover> result = new LinkedList<Cover>();
        artist = artist != null ? artist : "";
        album = album != null ? album : "";
        Object[] providers = providerTracker.getServices();
        if (providers != null) {
            ExecutorService searchPool = Executors.newFixedThreadPool(providers.length);
            List<Future<List<Cover>>> results = new ArrayList<Future<List<Cover>>>(providers.length);
            for (Object provider : providers) {
                CoverProvider prov = (CoverProvider) provider;
                Future<List<Cover>> futureResult = searchPool.submit(new CoverSearchTask(artist, album, prov));
                results.add(futureResult);
            }
            if (!Thread.currentThread().isInterrupted()) {
                for (Future<List<Cover>> future : results) {
                    try {
                        List<Cover> albums = future.get();
                        if (albums != null) {
                            result.addAll(albums);
                        }
                    } catch (InterruptedException e) {
                        logger.debug("Thread has been interrupted. Stopping search task");
                        return new LinkedList<Cover>();
                    } catch (ExecutionException e) {
                        logger.error("Couldn't execute request", e);
                    }
                }
            }
        } else {
            throw new NoProviderException();
        }
        return result;
    }

    private class CoverSearchTask implements Callable<List<Cover>> {

        private String artist;

        private String album;

        private CoverProvider prov;

        public CoverSearchTask(String artist, String album, CoverProvider prov) {
            this.artist = artist;
            this.album = album;
            this.prov = prov;
        }

        /**
         * Returns a List of Cover objects
         */
        public List<Cover> call() throws Exception {
            List<Cover> albums = prov.getCovers(artist, album);
            List<Cover> result = new ArrayList<Cover>(albums.size());
            if (albums.size() > 0) {
                for (Cover cover2 : albums) {
                    Cover a = cover2;
                    result.add(new CoverWrapper(a, prov));
                }
            } else {
                logger.info("No album found");
            }
            return result;
        }
    }
}
