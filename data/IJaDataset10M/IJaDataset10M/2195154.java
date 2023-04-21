package usyd.comp5425.action;

import com.sun.jaf.ui.Action;
import com.sun.jaf.ui.ActionManager;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import org.jdesktop.swingworker.SwingWorker;
import usyd.comp5425.query.QueryListener;
import usyd.comp5425.query.QueryManager;
import usyd.comp5425.query.QueryResult;
import usyd.comp5425.ui.ImageAppFrame;
import usyd.comp5425.ui.QueryFormPanel;
import usyd.comp5425.ui.imageviewer.ImageListModel;
import usyd.comp5425.ui.imageviewer.JThumbnailPanel;
import usyd.comp5425.util.GraphicsUtilities;
import usyd.comp5425.util.LRUCache;

/**
 *
 * @author Yuezhong Zhang SID:305275631
 */
public class QueryActionHandler implements QueryListener {

    private ImageAppFrame frame;

    private JThumbnailPanel thumbPanel;

    private LRUCache<String, BufferedImage> cache;

    private String pattern = "Found image about {0} in {1} seconds";

    private long startTime;

    public QueryActionHandler(ImageAppFrame frame) {
        this.frame = frame;
        ActionManager.getInstance().registerActionHandler(this);
        QueryManager.getInstance().addQueryListener(this);
        cache = new LRUCache<String, BufferedImage>(200);
        thumbPanel = (JThumbnailPanel) frame.getPanel(frame.THUMBNAIL_PANEL);
    }

    @Action("query-command")
    public void handleQueryAction() {
        SwingWorker worker = new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                QueryFormPanel panel = (QueryFormPanel) frame.getPanel(frame.QUERY_FROM_PANEL);
                File file = panel.getSampleFile();
                if (file != null) {
                    QueryManager.getInstance().query(panel.getSelectedFeatures(), file);
                }
                panel = null;
                return null;
            }
        };
        worker.execute();
    }

    @Action("browse-command")
    public void handleOpenSample() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            QueryFormPanel panel = (QueryFormPanel) frame.getPanel(frame.QUERY_FROM_PANEL);
            panel.setSampleFile(file);
            panel = null;
            file = null;
        }
        jfc = null;
    }

    public void queryStarted(String text) {
        startTime = System.currentTimeMillis();
        thumbPanel.setListModel(new ImageListModel());
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                frame.getProgressPane().setText("Querying is in progress...");
                frame.getProgressPane().start();
                frame.setVisiblePanel(frame.THUMBNAIL_PANEL);
            }
        });
    }

    public void queryFinished(String text) {
    }

    @Action("imlucky-command")
    public void handleLuckyBrowse() {
        ActionManager.getInstance().setEnabled("imlucky-command", false);
        ActionManager.getInstance().setEnabled("query-command", false);
        SwingWorker worker = new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                QueryManager.getInstance().luckyQuery();
                return null;
            }

            @Override
            public void done() {
                ActionManager.getInstance().setEnabled("imlucky-command", true);
                ActionManager.getInstance().setEnabled("query-command", true);
            }
        };
        worker.execute();
    }

    public void itemFound(final List<QueryResult> list) {
        SwingWorker worker = new SwingWorker<ImageListModel, String>() {

            @Override
            protected ImageListModel doInBackground() throws Exception {
                StringBuffer userdir = new StringBuffer(System.getProperty("user.dir"));
                userdir.append(File.separatorChar);
                userdir.append("images");
                userdir.append(File.separatorChar);
                File file;
                for (QueryResult result : list) {
                    String text = result.getImage();
                    BufferedImage image = null;
                    synchronized (cache) {
                        if (text.indexOf(":") > 0) file = new File(text); else file = new File(userdir.toString(), text);
                        image = cache.get(file.getAbsolutePath());
                        publish(file.toString());
                        if (image == null) {
                            try {
                                if (text.indexOf(":") > 0) image = ImageIO.read(file); else image = ImageIO.read(file);
                                if (image.getWidth() > 160 || image.getHeight() > 128) image = GraphicsUtilities.createThumbnailFast(image, 160, 128);
                                cache.put(file.getAbsolutePath(), image);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        if (image != null) {
                            result.setBufferedImage(image);
                            result.setImage(file.getAbsolutePath());
                        }
                    }
                }
                Collections.sort(list);
                userdir = null;
                ImageListModel model = new ImageListModel();
                model.add(list);
                list.clear();
                System.gc();
                return model;
            }

            @Override
            protected void done() {
                try {
                    frame.getProgressPane().stop();
                    thumbPanel.setListModel(get());
                    int size = thumbPanel.getListModel().getRealRowCount();
                    long duration = (System.currentTimeMillis() - startTime) / 1000;
                    String text = MessageFormat.format(pattern, new Object[] { size, duration });
                    frame.setStatusText(text);
                    System.out.println(text);
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            protected void process(List<String> txts) {
                for (String txt : txts) {
                    frame.setStatusText("Found image :" + txt);
                }
            }
        };
        worker.execute();
    }
}
