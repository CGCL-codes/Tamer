package geocosm.ui;

import geocosm.resource.CacheProxyFiller;
import geocosm.resource.CacheProxyFiller.CacheProxyFillerListener;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import uk.me.jstott.jcoord.LatLng;
import java.awt.event.KeyEvent;
import java.io.File;

public class CacheProgressDialog extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -374805745515505455L;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JLabel jLabel = null;

    private JProgressBar zoomLevelProgressBar = null;

    private JLabel jLabel1 = null;

    private JProgressBar tilesProgressBar = null;

    private JButton hideButton = null;

    private JButton stopButton = null;

    private LatLng topLeftCoord = null;

    private LatLng bottomRightCoord = null;

    private int startZoom = 14;

    private int stopZoom = 16;

    private File cacheDir = null;

    private CacheProxyFiller filler = null;

    /**
	 * This method initializes 
	 * 
	 */
    public CacheProgressDialog() {
        super();
        initialize();
    }

    /**
	 * @param owner
	 * @param modal
	 */
    public CacheProgressDialog(Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
	 * @param owner
	 * @param title
	 * @param modal
	 */
    public CacheProgressDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.setContentPane(getJPanel());
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            BorderLayout borderLayout = new BorderLayout();
            borderLayout.setHgap(5);
            borderLayout.setVgap(5);
            jPanel = new JPanel();
            jPanel.setLayout(borderLayout);
            jPanel.add(getJPanel1(), BorderLayout.CENTER);
            jPanel.add(getJPanel2(), BorderLayout.SOUTH);
        }
        return jPanel;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints2.gridy = 3;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.anchor = GridBagConstraints.WEST;
            gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints11.gridy = 2;
            jLabel1 = new JLabel();
            jLabel1.setText("Tiles");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.anchor = GridBagConstraints.WEST;
            gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.weighty = 0.0D;
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = GridBagConstraints.WEST;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.gridy = 0;
            jLabel = new JLabel();
            jLabel.setText("Zoom");
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(jLabel, gridBagConstraints);
            jPanel1.add(getZoomLevelProgressBar(), gridBagConstraints1);
            jPanel1.add(jLabel1, gridBagConstraints11);
            jPanel1.add(getTilesProgressBar(), gridBagConstraints2);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 1;
            gridBagConstraints4.insets = new Insets(5, 50, 5, 50);
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.gridy = 0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.insets = new Insets(5, 50, 5, 50);
            gridBagConstraints3.weightx = 1.0D;
            gridBagConstraints3.gridy = 0;
            jPanel2 = new JPanel();
            jPanel2.setLayout(new GridBagLayout());
            jPanel2.add(getHideButton(), gridBagConstraints3);
            jPanel2.add(getStopButton(), gridBagConstraints4);
        }
        return jPanel2;
    }

    /**
	 * This method initializes zoomLevelProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
    private JProgressBar getZoomLevelProgressBar() {
        if (zoomLevelProgressBar == null) {
            zoomLevelProgressBar = new JProgressBar();
            zoomLevelProgressBar.setStringPainted(true);
            zoomLevelProgressBar.setString("");
        }
        return zoomLevelProgressBar;
    }

    /**
	 * This method initializes tilesProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
    private JProgressBar getTilesProgressBar() {
        if (tilesProgressBar == null) {
            tilesProgressBar = new JProgressBar();
            tilesProgressBar.setStringPainted(true);
            tilesProgressBar.setString("");
        }
        return tilesProgressBar;
    }

    /**
	 * This method initializes hideButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getHideButton() {
        if (hideButton == null) {
            hideButton = new JButton();
            hideButton.setMnemonic(KeyEvent.VK_UNDEFINED);
            hideButton.setText("Hide");
            hideButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    setVisible(false);
                }
            });
        }
        return hideButton;
    }

    /**
	 * This method initializes stopButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getStopButton() {
        if (stopButton == null) {
            stopButton = new JButton();
            stopButton.setText("Stop");
            stopButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    CacheProxyFiller filler = getFiller();
                    filler.setStop(true);
                }
            });
        }
        return stopButton;
    }

    public void cacheProgress(int minZoom, int maxZoom, int currentZoom, int maxTile, int currentTile) {
        JProgressBar zoomProgress = getZoomLevelProgressBar();
        JProgressBar tileProgress = getTilesProgressBar();
        zoomProgress.setMinimum(minZoom);
        zoomProgress.setMaximum(maxZoom);
        zoomProgress.setValue(currentZoom);
        zoomProgress.setString("" + currentZoom + "/" + maxZoom);
        tileProgress.setMinimum(0);
        tileProgress.setMaximum(maxTile);
        tileProgress.setValue(currentTile);
        tileProgress.setString("" + currentTile + "/" + maxTile);
    }

    public void start() {
        CacheProxyFiller filler = getFiller();
        filler.setStop(false);
        filler.start();
    }

    public boolean isRunning() {
        CacheProxyFiller filler = getFiller();
        return filler.isRunning();
    }

    private CacheProxyFiller getFiller() {
        if (filler == null) {
            filler = new CacheProxyFiller();
            filler.setStartZoom(startZoom);
            filler.setStopZoom(stopZoom);
            filler.setBottomRightCoord(bottomRightCoord);
            filler.setTopLeftCoord(topLeftCoord);
            filler.addListener(new CacheProxyFillerListener() {

                @Override
                public void progress(int minZoom, int maxZoom, int currentZoom, int maxTile, int currentTile) {
                    cacheProgress(minZoom, maxZoom, currentZoom, maxTile, currentTile);
                }

                @Override
                public void finished() {
                    filler = null;
                    setVisible(false);
                }
            });
        }
        return filler;
    }

    /**
	 * @return the topLeftCoord
	 */
    public LatLng getTopLeftCoord() {
        return topLeftCoord;
    }

    /**
	 * @param topLeftCoord the topLeftCoord to set
	 */
    public void setTopLeftCoord(LatLng topLeftCoord) {
        this.topLeftCoord = topLeftCoord;
    }

    /**
	 * @return the bottomRightCoord
	 */
    public LatLng getBottomRightCoord() {
        return bottomRightCoord;
    }

    /**
	 * @param bottomRightCoord the bottomRightCoord to set
	 */
    public void setBottomRightCoord(LatLng bottomRightCoord) {
        this.bottomRightCoord = bottomRightCoord;
    }

    /**
	 * @return the startZoom
	 */
    public int getStartZoom() {
        return startZoom;
    }

    /**
	 * @param startZoom the startZoom to set
	 */
    public void setStartZoom(int startZoom) {
        this.startZoom = startZoom;
    }

    /**
	 * @return the stopZoom
	 */
    public int getStopZoom() {
        return stopZoom;
    }

    /**
	 * @param stopZoom the stopZoom to set
	 */
    public void setStopZoom(int stopZoom) {
        this.stopZoom = stopZoom;
    }

    /**
	 * @return the cacheDir
	 */
    public File getCacheDir() {
        return cacheDir;
    }

    /**
	 * @param cacheDir the cacheDir to set
	 */
    public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }
}
