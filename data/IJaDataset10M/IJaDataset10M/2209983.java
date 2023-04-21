package il.ac.tau.dbcourse.gui;

import il.ac.tau.dbcourse.db.DBException;
import il.ac.tau.dbcourse.db.reports.IncompleteAlbum.AlbumDetails;
import il.ac.tau.dbcourse.logic.MixAndMatch;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.xml.sax.SAXException;

public class ResultWindow {

    Group albumGroup, trackGroup, browserGroup, playPauseGroup;

    Table albumTable, trackTable;

    List<AlbumDetails> albums;

    BrowserWidget browserWidget;

    AlbumResultWidget albumWidget;

    Button ppButton;

    Image ppImage;

    Player player;

    boolean blink;

    TableItem currentlySelectedTrack;

    Thread playerThread, blinkThread;

    void enablePPButton() {
    }

    void createPlayPauseGroup(Shell shell) {
        playPauseGroup = new Group(shell, SWT.NONE);
        playPauseGroup.setText("Available Track Player");
        playPauseGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout();
        playPauseGroup.setLayout(layout);
        ppImage = new Image(shell.getDisplay(), "Images/PlayButtonBW.jpg");
        ppButton = new Button(playPauseGroup, SWT.NONE);
        ppButton.setImage(ppImage);
        ppButton.setEnabled(true);
        ppButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                ImageData id = ppImage.getImageData();
                if (currentlySelectedTrack != null && !id.equals(new ImageData("Images/PlayButtonBW.jpg"))) {
                    try {
                        System.out.println(currentlySelectedTrack.getText(4));
                        if (playerThread != null && playerThread.isAlive()) {
                            player.close();
                        } else if (!currentlySelectedTrack.getText(4).equals("")) {
                            player = new Player(new FileInputStream(currentlySelectedTrack.getText(4)));
                            playerThread = new Thread(new Mp3Player(player));
                            playerThread.start();
                            final Image ppImage1 = new Image(playPauseGroup.getDisplay(), "Images/PlayButton.jpg");
                            final Image ppImage2 = new Image(playPauseGroup.getDisplay(), "Images/PlayButton2.jpg");
                            blinkThread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    while (playerThread.isAlive()) {
                                        playPauseGroup.getDisplay().asyncExec(new Runnable() {

                                            public void run() {
                                                if (ppButton != null && ppImage1 != null && ppImage2 != null && ppButton.isDisposed() == false) {
                                                    if (blink) {
                                                        ppImage = ppImage2;
                                                        ppButton.setImage(ppImage);
                                                        blink = false;
                                                    } else {
                                                        ppImage = ppImage1;
                                                        ppButton.setImage(ppImage);
                                                        blink = true;
                                                    }
                                                    ppButton.redraw();
                                                }
                                            }
                                        });
                                        try {
                                            Thread.sleep(333);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            blinkThread.start();
                        }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (JavaLayerException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    void createBrowserGroup(Shell shell) {
        browserGroup = new Group(shell, SWT.NONE);
        browserGroup.setText("YouTube Results");
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        browserGroup.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 1;
        data.heightHint = 340;
        browserGroup.setLayoutData(data);
    }

    private void createBrowserWidget(Composite comp) {
        try {
            browserWidget = new BrowserWidget(comp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createTrackGroup(Shell shell) {
        trackGroup = new Group(shell, SWT.NONE);
        trackGroup.setText("Track List");
        GridLayout layout = new GridLayout();
        trackGroup.setLayout(layout);
        GridData data = new GridData(GridData.FILL_BOTH);
        trackGroup.setLayoutData(data);
    }

    void createTrackWidgets(final Composite comp) {
        trackTable = new Table(trackGroup, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
        trackTable.setLinesVisible(true);
        trackTable.setHeaderVisible(true);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 2;
        gridData.heightHint = 200;
        trackTable.setLayoutData(gridData);
        String[] columnHeaders = getTrackTableFieldNames();
        for (int i = 0; i < columnHeaders.length; i++) {
            TableColumn column = new TableColumn(trackTable, SWT.NONE);
            column.setText(columnHeaders[i]);
            if (i == 0) column.setWidth(25); else if (i == 1) column.setWidth(200); else column.pack();
        }
        trackTable.addListener(SWT.MouseDown, new Listener() {

            public void handleEvent(Event event) {
                if (playerThread != null && playerThread.isAlive()) player.close();
                TableItem item;
                Rectangle clientArea = trackTable.getClientArea();
                Point selectedPoint = new Point(event.x, event.y);
                int index = trackTable.getTopIndex();
                while (index < trackTable.getItemCount()) {
                    boolean visible = false;
                    item = trackTable.getItem(index);
                    for (int i = 0; i < trackTable.getColumnCount(); i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(selectedPoint)) {
                            try {
                                currentlySelectedTrack = item;
                                System.out.println(albumWidget.currentlySelectedAlbum);
                                String searchString = albumWidget.currentlySelectedAlbum.getArtist().getName() + " " + currentlySelectedTrack.getText(1);
                                if (albumWidget.currentlySelectedAlbum != null) browserWidget.browseFor(searchString);
                                System.out.println("currently selected track- " + currentlySelectedTrack.getText(4));
                                if (currentlySelectedTrack.getText(3).equals("Exists")) {
                                    ppImage = new Image(comp.getDisplay(), "Images/PlayButton.jpg");
                                }
                                if (currentlySelectedTrack.getText(3).equals("Missing")) {
                                    ppImage = new Image(comp.getDisplay(), "Images/PlayButtonBW.jpg");
                                }
                                ppButton.setImage(ppImage);
                                ppButton.redraw();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    if (!visible) return;
                    index++;
                }
            }
        });
    }

    void createAlbumGroup(Shell shell) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        albumGroup = new Group(shell, SWT.NONE);
        albumGroup.setText("Incomplete Albums Detected In Scan: ");
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        albumGroup.setLayout(layout);
        GridData data = new GridData(GridData.FILL_VERTICAL);
        data.widthHint = 500;
        data.verticalSpan = 3;
        albumGroup.setLayoutData(data);
    }

    private void createAlbumWidget(final Composite comp) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        albumWidget = new AlbumResultWidget(comp, trackTable, trackGroup);
    }

    private String[] getTrackTableFieldNames() {
        return new String[] { "#", "Title", "Length", "Missing", "Path" };
    }

    public ResultWindow(final Shell shell) throws InterruptedException, XPathExpressionException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        blink = false;
        MyProperties properties = new MyProperties();
        String lastScanDate = properties.getProperty("lastScanDate");
        String lastScanDirectory = properties.getProperty("lastScanDirectory");
        shell.setText("Scan Results From " + lastScanDate + " , on Directory: " + lastScanDirectory);
        GridLayout grid = new GridLayout();
        grid.makeColumnsEqualWidth = true;
        grid.numColumns = 2;
        shell.setLayout(grid);
        MixAndMatch mixAndMatch = null;
        try {
            mixAndMatch = new MixAndMatch();
            albums = mixAndMatch.getIncompleteAlbums();
        } catch (DBException e) {
            e.printStackTrace();
        }
        if (mixAndMatch != null) mixAndMatch.close();
        createAlbumGroup(shell);
        createTrackGroup(shell);
        createBrowserGroup(shell);
        createPlayPauseGroup(shell);
        createBrowserWidget(browserGroup);
        createTrackWidgets(trackGroup);
        createAlbumWidget(albumGroup);
        albumWidget.setAlbumsData(albums);
        Thread t = new Thread() {

            @Override
            public void run() {
                albumWidget.updateAlbumsData();
            }
        };
        t.start();
        shell.setSize(1050, 700);
        shell.addListener(SWT.Close, new Listener() {

            public void handleEvent(Event event) {
                try {
                    if (player != null) player.close();
                    if (playerThread != null && playerThread.isAlive()) playerThread.join();
                    if (blinkThread != null && blinkThread.isAlive()) blinkThread.join();
                    event.doit = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!shell.getDisplay().readAndDispatch()) shell.getDisplay().sleep();
        }
        if (player != null) player.close();
        while (playerThread != null && playerThread.isAlive() && !player.isComplete()) Thread.sleep(30);
    }
}
