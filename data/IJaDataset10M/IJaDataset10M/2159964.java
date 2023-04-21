package mediareader;

import common.DBFunc;
import common.DualPromptDlg;
import common.FileUtilities;
import common.Profile;
import common.TextFieldLimit;
import common.Ut;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import netutils.HttpUtils;
import org.jdesktop.application.Action;

/**
 *
 * @author rernst
 */
public class MovieEditDlg extends javax.swing.JDialog {

    Component dialogComponents[];

    boolean componentState[];

    final ImageIcon nullIcon = new ImageIcon(getClass().getResource("resources/no-Image.png"));

    protected Boolean okButtonPressed = false;

    /**
     * Get the value of okButtonPressed
     *
     * @return the value of okButtonPressed
     */
    public Boolean getOkButtonPressed() {
        return okButtonPressed;
    }

    /**
     * Set the value of okButtonPressed
     *
     * @param okButtonPressed new value of okButtonPressed
     */
    public void setOkButtonPressed(Boolean okButtonPressed) {
        this.okButtonPressed = okButtonPressed;
    }

    Connection conn;

    String sIMDBID;

    static HttpUtils URLUtils = new HttpUtils();

    static DBFunc dbUtil;

    int nID;

    String oldPath = "";

    /** Creates new form EditTVSeriesDlg */
    public MovieEditDlg(java.awt.Frame parent, boolean modal, Connection dbConn, int movieID) {
        super(parent, modal);
        initComponents();
        dialogComponents = new Component[] { okButton, cancelButton };
        componentState = new boolean[dialogComponents.length];
        conn = dbConn;
        URLUtils.setDbConn(dbConn);
        dbUtil = new DBFunc(conn);
        sIMDBID = dbUtil.getIMDBID(movieID);
        nID = movieID;
        PreparedStatement ps;
        DefaultListModel actorModel = new DefaultListModel();
        DefaultListModel genreModel = new DefaultListModel();
        try {
            ps = conn.prepareStatement("SELECT * FROM MOVIEINFO WHERE ID = ?");
            ps.setInt(1, nID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contentRating.setText(rs.getString("CONTENTRATING"));
                releaseDate.setText(rs.getString("RELEASE"));
                imdbID.setText(rs.getString("IMDBID"));
                director.setText(rs.getString("DIRECTOR"));
                overView.setText(rs.getString("OUTLINE"));
                overView.setCaretPosition(0);
                plot.setText(rs.getString("PLOT"));
                plot.setCaretPosition(0);
                audioLang.setText(rs.getString("AUDIOLANG"));
                demuxer.setText(rs.getString("DEMUXER"));
                videoFormat.setText(rs.getString("VIDEOFORMAT"));
                bitrate.setText(rs.getString("VIDEOBITRATE"));
                hash.setText(rs.getString("HASH"));
                videoWidth.setText(rs.getString("VIDEOWIDTH"));
                if (videoWidth.getText().equals("")) {
                    videoWidth.setText("0");
                }
                videoHeight.setText(rs.getString("VIDEOHEIGHT"));
                if (videoHeight.getText().equals("")) {
                    videoHeight.setText("0");
                }
                videoFps.setText(rs.getString("VIDEOFPS"));
                if (videoFps.getText().equals("")) {
                    videoFps.setText("0.0");
                }
                videoAspect.setText(rs.getString("VIDEOASPECT"));
                length.setText(rs.getString("VIDEOLENGTH"));
                videoCodec.setText(rs.getString("VIDEOCODEC"));
                audioFormat.setText(rs.getString("AUDIOFORMAT"));
                audioCodec.setText(rs.getString("AUDIOCODEC"));
                audioBitrate.setText(rs.getString("AUDIOBITRATE"));
                audioRate.setText(rs.getString("AUDIORATE"));
                audioChannels.setText(rs.getString("AUDIOCHANNELS"));
                if (audioChannels.getText().equals("")) {
                    audioChannels.setText("0");
                }
                audioCodec.setText(rs.getString("AUDIOCODEC"));
                rating.setText(rs.getString("RATING"));
                runTime.setText(rs.getString("RUNTIME"));
                credits.setText(rs.getString("CREDITS"));
                movieTitle.setText(rs.getString("NAME"));
                movieTitle.setCaretPosition(0);
                trailer.setText(rs.getString("TRAILER"));
                studio.setText(rs.getString("STUDIO"));
                tagLine.setText(rs.getString("TAGLINE"));
                trailer.setText(rs.getString("TRAILER"));
                filePath.setText(rs.getString("PATH"));
                interlaced.setSelected(Ut.safeString(rs.getString("INTERLACED")).equals("I"));
                oldPath = filePath.getText();
                trailer.setCaretPosition(0);
                if (filePath.getText().equals("")) {
                    playButton.setVisible(false);
                } else {
                    File file = new File(filePath.getText());
                    if (file.exists()) {
                        DecimalFormat formatter = new DecimalFormat("###,###,###,###");
                        fileSize.setText(formatter.format(file.length() / 1024) + " KB.");
                    }
                }
                File checkFile = new File(Profile.getPlayerLocation());
                if (!checkFile.exists()) {
                    playButton.setEnabled(false);
                }
                ps.close();
                ps = conn.prepareStatement("SELECT MOVIEACTOR.NAME AS MNAME, MOVIETOACTOR.ROLE AS MROLE," + "MOVIEACTOR.IMGURL AS MIMGURL, MOVIEACTOR.ID AS MID " + "FROM ((MOVIEINFO LEFT JOIN MOVIETOACTOR ON " + "MOVIETOACTOR.MOVIEID = MOVIEINFO.ID) " + "LEFT JOIN MOVIEACTOR ON MOVIEACTOR.ID = MOVIETOACTOR.ACTORID) " + "WHERE MOVIEINFO.ID = ? ORDER BY MOVIEACTOR.NAME");
                ps.setInt(1, nID);
                rs = ps.executeQuery();
                while (rs.next()) {
                    actorModel.addElement(new MovieActor(rs.getString("MNAME"), rs.getString("MROLE"), rs.getInt("MID"), rs.getString("MIMGURL")));
                }
                actorList.setModel(actorModel);
            }
            ps.close();
            ps = conn.prepareStatement("SELECT GENRE.ID AS MID, GENRE.NAME AS MNAME FROM ((MOVIEINFO LEFT JOIN MOVIETOGENRE ON " + "MOVIETOGENRE.MOVIEID = MOVIEINFO.ID) " + "LEFT JOIN GENRE ON GENRE.ID = MOVIETOGENRE.GENREID) " + "WHERE MOVIEINFO.ID = ? ORDER BY GENRE.NAME");
            ps.setInt(1, nID);
            rs = ps.executeQuery();
            while (rs.next()) {
                genreModel.addElement(new GenreDef(rs.getInt("MID"), rs.getString("MNAME")));
            }
            genreList.setModel(genreModel);
            ps.close();
            ps = conn.prepareStatement("SELECT IMGURL FROM MOVIEART WHERE MOVIEID = ? AND SIZE = ?");
            ps.setString(1, sIMDBID);
            ps.setString(2, "mid");
            rs = ps.executeQuery();
            boolean bFound = rs.next();
            if (!bFound) {
                ps = conn.prepareStatement("SELECT IMGURL FROM MOVIEART WHERE MOVIEID = ? AND SIZE = ?");
                ps.setString(1, sIMDBID);
                ps.setString(2, "original");
                rs = ps.executeQuery();
                bFound = rs.next();
            }
            cover.setIcon(Ut.scaleImageIcon(nullIcon, cover.getWidth(), cover.getHeight()));
            if (bFound) {
                ImageIcon movieIcon = URLUtils.getImageIcon(rs.getString("IMGURL"));
                cover.setIcon(nullIcon);
                if (movieIcon != null) {
                    if (movieIcon.getIconHeight() != -1) {
                        cover.setIcon(Ut.scaleImageIcon(movieIcon, cover.getWidth(), cover.getHeight()));
                    }
                }
            }
        } catch (SQLException ex) {
            Ut.showSQLError(ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        genrePopup = new javax.swing.JPopupMenu();
        addGenre = new javax.swing.JMenuItem();
        deleteGenre = new javax.swing.JMenuItem();
        actorPopup = new javax.swing.JPopupMenu();
        updAct = new javax.swing.JMenuItem();
        delAct = new javax.swing.JMenuItem();
        genreAddPopup = new javax.swing.JPopupMenu();
        addGenre1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        actorList = new javax.swing.JList();
        contentRating = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        releaseDate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        imdbID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        overView = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rating = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        runTime = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        genreList = new javax.swing.JList();
        director = new javax.swing.JTextField();
        movieTitle = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        plot = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        tagLine = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        credits = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        studio = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        trailer = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        filePath = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        fileSize = new javax.swing.JLabel();
        cover = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        videoFps = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        videoAspect = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        audioLang = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        demuxer = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        length = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        videoCodec = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        videoFormat = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        videoWidth = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        bitrate = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        videoHeight = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        audioFormat = new javax.swing.JTextField();
        audioBitrate = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        audioRate = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        audioChannels = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        audioCodec = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        interlaced = new javax.swing.JCheckBox();
        jLabel34 = new javax.swing.JLabel();
        hash = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mediareader.MediaReaderApp.class).getContext().getResourceMap(MovieEditDlg.class);
        genrePopup.setBackground(resourceMap.getColor("genrePopup.background"));
        genrePopup.setName("genrePopup");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(mediareader.MediaReaderApp.class).getContext().getActionMap(MovieEditDlg.class, this);
        addGenre.setAction(actionMap.get("addGenre"));
        addGenre.setBackground(resourceMap.getColor("addGenre.background"));
        addGenre.setIcon(resourceMap.getIcon("addGenre.icon"));
        addGenre.setText(resourceMap.getString("addGenre.text"));
        addGenre.setName("addGenre");
        addGenre.setOpaque(true);
        genrePopup.add(addGenre);
        deleteGenre.setAction(actionMap.get("deleteGenre"));
        deleteGenre.setBackground(resourceMap.getColor("deleteGenre.background"));
        deleteGenre.setIcon(resourceMap.getIcon("deleteGenre.icon"));
        deleteGenre.setText(resourceMap.getString("deleteGenre.text"));
        deleteGenre.setName("deleteGenre");
        deleteGenre.setOpaque(true);
        genrePopup.add(deleteGenre);
        actorPopup.setBackground(resourceMap.getColor("actorPopup.background"));
        actorPopup.setName("actorPopup");
        updAct.setAction(actionMap.get("UpdActor"));
        updAct.setBackground(resourceMap.getColor("updAct.background"));
        updAct.setIcon(resourceMap.getIcon("updAct.icon"));
        updAct.setText(resourceMap.getString("updAct.text"));
        updAct.setName("updAct");
        updAct.setOpaque(true);
        actorPopup.add(updAct);
        delAct.setAction(actionMap.get("deleteActor"));
        delAct.setBackground(resourceMap.getColor("delAct.background"));
        delAct.setIcon(resourceMap.getIcon("delAct.icon"));
        delAct.setText(resourceMap.getString("delAct.text"));
        delAct.setName("delAct");
        delAct.setOpaque(true);
        actorPopup.add(delAct);
        genreAddPopup.setBackground(resourceMap.getColor("genreAddPopup.background"));
        genreAddPopup.setName("genreAddPopup");
        addGenre1.setAction(actionMap.get("addGenre"));
        addGenre1.setBackground(resourceMap.getColor("addGenre1.background"));
        addGenre1.setIcon(resourceMap.getIcon("addGenre1.icon"));
        addGenre1.setText(resourceMap.getString("addGenre1.text"));
        addGenre1.setName("addGenre1");
        addGenre1.setOpaque(true);
        genreAddPopup.add(addGenre1);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(650, 580));
        setName("Form");
        setResizable(false);
        jPanel1.setBackground(resourceMap.getColor("jPanel1.background"));
        jPanel1.setName("jPanel1");
        jScrollPane2.setName("jScrollPane2");
        actorList.setBackground(resourceMap.getColor("actorList.background"));
        actorList.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        actorList.setName("actorList");
        actorList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                actorListMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                actorListMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(actorList);
        contentRating.setDocument(new TextFieldLimit(120));
        contentRating.setText(resourceMap.getString("contentRating.text"));
        contentRating.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        contentRating.setName("contentRating");
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        releaseDate.setDocument(new TextFieldLimit(10));
        releaseDate.setText(resourceMap.getString("releaseDate.text"));
        releaseDate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        releaseDate.setName("releaseDate");
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        imdbID.setDocument(new TextFieldLimit(10));
        imdbID.setText(resourceMap.getString("imdbID.text"));
        imdbID.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        imdbID.setName("imdbID");
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        jScrollPane1.setName("jScrollPane1");
        overView.setBackground(resourceMap.getColor("overView.background"));
        overView.setColumns(20);
        overView.setFont(resourceMap.getFont("overView.font"));
        overView.setLineWrap(true);
        overView.setRows(5);
        overView.setWrapStyleWord(true);
        overView.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        overView.setName("overView");
        jScrollPane1.setViewportView(overView);
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        rating.setDocument(new TextFieldLimit(4, false, false, true));
        rating.setText(resourceMap.getString("rating.text"));
        rating.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        rating.setName("rating");
        rating.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                ratingFocusLost(evt);
            }
        });
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        runTime.setDocument(new TextFieldLimit(10, false, true));
        runTime.setText(resourceMap.getString("runTime.text"));
        runTime.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        runTime.setName("runTime");
        getRootPane().setDefaultButton(okButton);
        okButton.setAction(actionMap.get("updateRecord"));
        okButton.setBackground(resourceMap.getColor("okButton.background"));
        okButton.setIcon(resourceMap.getIcon("okButton.icon"));
        okButton.setText(resourceMap.getString("okButton.text"));
        okButton.setName("okButton");
        cancelButton.setAction(actionMap.get("cancelPressed"));
        cancelButton.setBackground(resourceMap.getColor("cancelButton.background"));
        cancelButton.setIcon(resourceMap.getIcon("cancelButton.icon"));
        cancelButton.setName("cancelButton");
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        jScrollPane3.setName("jScrollPane3");
        genreList.setBackground(resourceMap.getColor("genreList.background"));
        genreList.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        genreList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        genreList.setName("genreList");
        genreList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                genreListMousePressed(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                genreListMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(genreList);
        director.setDocument(new TextFieldLimit(50));
        director.setText(resourceMap.getString("director.text"));
        director.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        director.setName("director");
        movieTitle.setDocument(new TextFieldLimit(100));
        movieTitle.setText(resourceMap.getString("movieTitle.text"));
        movieTitle.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        movieTitle.setName("movieTitle");
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        jScrollPane4.setName("jScrollPane4");
        plot.setBackground(resourceMap.getColor("plot.background"));
        plot.setColumns(20);
        plot.setFont(resourceMap.getFont("plot.font"));
        plot.setLineWrap(true);
        plot.setRows(5);
        plot.setWrapStyleWord(true);
        plot.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        plot.setName("plot");
        jScrollPane4.setViewportView(plot);
        jLabel12.setText(resourceMap.getString("jLabel12.text"));
        jLabel12.setName("jLabel12");
        tagLine.setText(resourceMap.getString("tagLine.text"));
        tagLine.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        tagLine.setName("tagLine");
        jLabel13.setText(resourceMap.getString("jLabel13.text"));
        jLabel13.setName("jLabel13");
        credits.setText(resourceMap.getString("credits.text"));
        credits.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        credits.setName("credits");
        jLabel14.setText(resourceMap.getString("jLabel14.text"));
        jLabel14.setName("jLabel14");
        studio.setText(resourceMap.getString("studio.text"));
        studio.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        studio.setName("studio");
        jLabel15.setText(resourceMap.getString("jLabel15.text"));
        jLabel15.setName("jLabel15");
        trailer.setText(resourceMap.getString("trailer.text"));
        trailer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        trailer.setName("trailer");
        jLabel16.setText(resourceMap.getString("jLabel16.text"));
        jLabel16.setName("jLabel16");
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        filePath.setText(resourceMap.getString("filePath.text"));
        filePath.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        filePath.setName("filePath");
        jButton1.setAction(actionMap.get("setPath"));
        jButton1.setBackground(resourceMap.getColor("jButton1.background"));
        jButton1.setText(resourceMap.getString("jButton1.text"));
        jButton1.setName("jButton1");
        playButton.setAction(actionMap.get("playFile"));
        playButton.setBackground(resourceMap.getColor("playButton.background"));
        playButton.setIcon(resourceMap.getIcon("playButton.icon"));
        playButton.setText(resourceMap.getString("playButton.text"));
        playButton.setName("playButton");
        jLabel17.setText(resourceMap.getString("jLabel17.text"));
        jLabel17.setName("jLabel17");
        fileSize.setText(resourceMap.getString("fileSize.text"));
        fileSize.setName("fileSize");
        cover.setBackground(resourceMap.getColor("cover.background"));
        cover.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cover.setText(resourceMap.getString("cover.text"));
        cover.setName("cover");
        cover.setOpaque(true);
        jLabel18.setText(resourceMap.getString("jLabel18.text"));
        jLabel18.setName("jLabel18");
        videoFps.setDocument(new TextFieldLimit(10, false, false, true));
        videoFps.setText(resourceMap.getString("videoFps.text"));
        videoFps.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        videoFps.setName("videoFps");
        jLabel19.setText(resourceMap.getString("jLabel19.text"));
        jLabel19.setName("jLabel19");
        videoAspect.setDocument(new TextFieldLimit(20));
        videoAspect.setText(resourceMap.getString("videoAspect.text"));
        videoAspect.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        videoAspect.setName("videoAspect");
        jLabel20.setText(resourceMap.getString("jLabel20.text"));
        jLabel20.setName("jLabel20");
        audioLang.setDocument(new TextFieldLimit(20));
        audioLang.setText(resourceMap.getString("audioLang.text"));
        audioLang.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        audioLang.setName("audioLang");
        jLabel21.setText(resourceMap.getString("jLabel21.text"));
        jLabel21.setName("jLabel21");
        demuxer.setDocument(new TextFieldLimit(20));
        demuxer.setText(resourceMap.getString("demuxer.text"));
        demuxer.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        demuxer.setName("demuxer");
        jLabel22.setText(resourceMap.getString("jLabel22.text"));
        jLabel22.setName("jLabel22");
        length.setDocument(new TextFieldLimit(20));
        length.setText(resourceMap.getString("length.text"));
        length.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        length.setName("length");
        jLabel23.setText(resourceMap.getString("jLabel23.text"));
        jLabel23.setName("jLabel23");
        videoCodec.setDocument(new TextFieldLimit(20));
        videoCodec.setText(resourceMap.getString("videoCodec.text"));
        videoCodec.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        videoCodec.setName("videoCodec");
        jLabel24.setText(resourceMap.getString("jLabel24.text"));
        jLabel24.setName("jLabel24");
        videoFormat.setDocument(new TextFieldLimit(20));
        videoFormat.setText(resourceMap.getString("videoFormat.text"));
        videoFormat.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        videoFormat.setName("videoFormat");
        jLabel25.setText(resourceMap.getString("jLabel25.text"));
        jLabel25.setName("jLabel25");
        videoWidth.setDocument(new TextFieldLimit(4, false, true));
        videoWidth.setText(resourceMap.getString("videoWidth.text"));
        videoWidth.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        videoWidth.setName("videoWidth");
        jLabel26.setText(resourceMap.getString("jLabel26.text"));
        jLabel26.setName("jLabel26");
        bitrate.setDocument(new TextFieldLimit(20));
        bitrate.setText(resourceMap.getString("bitrate.text"));
        bitrate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        bitrate.setName("bitrate");
        jLabel27.setText(resourceMap.getString("jLabel27.text"));
        jLabel27.setName("jLabel27");
        videoHeight.setDocument(new TextFieldLimit(4, false, true));
        videoHeight.setText(resourceMap.getString("videoHeight.text"));
        videoHeight.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        videoHeight.setName("videoHeight");
        jLabel28.setText(resourceMap.getString("jLabel28.text"));
        jLabel28.setName("jLabel28");
        audioFormat.setDocument(new TextFieldLimit(10));
        audioFormat.setText(resourceMap.getString("audioFormat.text"));
        audioFormat.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        audioFormat.setName("audioFormat");
        audioBitrate.setDocument(new TextFieldLimit(20));
        audioBitrate.setText(resourceMap.getString("audioBitrate.text"));
        audioBitrate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        audioBitrate.setName("audioBitrate");
        jLabel29.setText(resourceMap.getString("jLabel29.text"));
        jLabel29.setName("jLabel29");
        jLabel30.setText(resourceMap.getString("jLabel30.text"));
        jLabel30.setName("jLabel30");
        audioRate.setDocument(new TextFieldLimit(20));
        audioRate.setText(resourceMap.getString("audioRate.text"));
        audioRate.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        audioRate.setName("audioRate");
        jLabel31.setText(resourceMap.getString("jLabel31.text"));
        jLabel31.setName("jLabel31");
        audioChannels.setDocument(new TextFieldLimit(1, false, true));
        audioChannels.setText(resourceMap.getString("audioChannels.text"));
        audioChannels.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        audioChannels.setName("audioChannels");
        jLabel32.setText(resourceMap.getString("jLabel32.text"));
        jLabel32.setName("jLabel32");
        audioCodec.setDocument(new TextFieldLimit(20));
        audioCodec.setText(resourceMap.getString("audioCodec.text"));
        audioCodec.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        audioCodec.setName("audioCodec");
        jLabel33.setText(resourceMap.getString("jLabel33.text"));
        jLabel33.setName("jLabel33");
        interlaced.setBackground(resourceMap.getColor("interlaced.background"));
        interlaced.setText(resourceMap.getString("interlaced.text"));
        interlaced.setName("interlaced");
        jLabel34.setText(resourceMap.getString("jLabel34.text"));
        jLabel34.setName("jLabel34");
        hash.setDocument(new TextFieldLimit(16));
        hash.setEditable(false);
        hash.setText(resourceMap.getString("hash.text"));
        hash.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        hash.setName("hash");
        jButton2.setAction(actionMap.get("browseForImage"));
        jButton2.setBackground(resourceMap.getColor("jButton2.background"));
        jButton2.setIcon(resourceMap.getIcon("jButton2.icon"));
        jButton2.setText(resourceMap.getString("jButton2.text"));
        jButton2.setName("jButton2");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap(15, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(audioFormat, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(rating).addComponent(runTime, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(interlaced)).addGap(23, 23, 23).addComponent(jLabel34).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel10).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE).addComponent(hash)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(audioChannels).addComponent(audioBitrate).addComponent(bitrate).addComponent(videoFormat).addComponent(length).addComponent(audioLang).addComponent(videoFps, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel25, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(audioRate, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addComponent(audioCodec, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addComponent(videoHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addComponent(videoWidth, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addComponent(videoCodec, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addComponent(demuxer, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE).addComponent(videoAspect, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))).addComponent(fileSize, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, 588, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()).addGroup(jPanel1Layout.createSequentialGroup().addGap(38, 38, 38).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel7)).addGap(642, 642, 642)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(releaseDate, javax.swing.GroupLayout.Alignment.LEADING).addComponent(imdbID, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(movieTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addComponent(contentRating, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addComponent(director, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addComponent(tagLine, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addComponent(credits, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addComponent(studio, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(trailer, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cover, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE).addComponent(jLabel9).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)).addGap(10, 10, 10)).addGroup(jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addComponent(okButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 419, Short.MAX_VALUE).addComponent(playButton).addGap(34, 34, 34)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(movieTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel9).addComponent(jLabel11)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(contentRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(releaseDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(imdbID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(director, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(tagLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel13).addComponent(credits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(studio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(trailer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton2))).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel16).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(cover, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(rating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(runTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel7))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel18).addComponent(videoFps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel20).addComponent(audioLang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(videoAspect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel19)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel21).addComponent(demuxer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel22).addComponent(length, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(videoFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel24)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel26).addComponent(bitrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(videoCodec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel23)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(videoWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel25)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(videoHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel27)).addGap(2, 2, 2)))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel28).addComponent(audioFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(audioBitrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel29)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel30).addComponent(audioRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(audioChannels, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel31)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel32).addComponent(audioCodec, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jLabel33).addComponent(interlaced)).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(hash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel34))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(filePath, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton1).addComponent(jLabel8)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(fileSize, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel17)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton).addComponent(okButton).addComponent(playButton)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        pack();
    }

    private void genreListMouseReleased(java.awt.event.MouseEvent evt) {
        if (SwingUtilities.isRightMouseButton(evt)) {
            int i = genreList.locationToIndex(evt.getPoint());
            if (i == -1) {
                genreAddPopup.show(evt.getComponent(), evt.getX(), evt.getY());
                return;
            }
            if (i != genreList.getSelectedIndex()) {
                genreList.setSelectedIndex(i);
            }
            genrePopup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    private void ratingFocusLost(java.awt.event.FocusEvent evt) {
        if (rating.getText().length() > 0) {
            try {
                BigDecimal bdRating = new BigDecimal(rating.getText());
            } catch (NumberFormatException ex) {
                rating.requestFocus();
            }
        }
    }

    private void actorListMouseReleased(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            int i = actorList.locationToIndex(evt.getPoint());
            if (i == -1) {
                return;
            }
            if (i != actorList.getSelectedIndex()) {
                actorList.setSelectedIndex(i);
            }
            actorPopup.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }

    private void actorListMousePressed(java.awt.event.MouseEvent evt) {
        actorListMouseReleased(evt);
    }

    private void genreListMousePressed(java.awt.event.MouseEvent evt) {
        genreListMouseReleased(evt);
    }

    @Action
    public void updateRecord() {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE MOVIEINFO SET " + "IMDBID = ?, " + "NAME = ?, " + "DIRECTOR = ?, " + "RATING = ?, " + "RELEASE = ?, " + "OUTLINE = ?, " + "PLOT = ?, " + "TAGLINE = ?, " + "RUNTIME = ?, " + "CONTENTRATING = ?, " + "CREDITS = ?, " + "STUDIO = ?, " + "TRAILER = ?, " + "PATH = ?, " + "AUDIOLANG = ?, " + "DEMUXER = ?, " + "VIDEOFORMAT = ?, " + "VIDEOBITRATE = ?, " + "VIDEOWIDTH = ?, " + "VIDEOHEIGHT = ?, " + "VIDEOFPS = ?, " + "VIDEOASPECT = ?, " + "VIDEOLENGTH = ?, " + "VIDEOCODEC = ?, " + "AUDIOFORMAT = ?, " + "AUDIOBITRATE = ?, " + "AUDIORATE = ?, " + "AUDIOCHANNELS = ?, " + "AUDIOCODEC = ?, " + "INTERLACED = ?, " + "HASH = ? " + "WHERE ID = ?");
            ps.setString(1, Ut.trunc(imdbID.getText(), 10));
            ps.setString(2, Ut.trunc(movieTitle.getText(), 100));
            ps.setString(3, Ut.trunc(director.getText(), 50));
            ps.setBigDecimal(4, new BigDecimal(rating.getText()));
            ps.setString(5, releaseDate.getText());
            ps.setString(6, overView.getText());
            ps.setString(7, plot.getText());
            ps.setString(8, Ut.trunc(tagLine.getText(), 100));
            ps.setInt(9, Integer.parseInt(runTime.getText()));
            ps.setString(10, contentRating.getText());
            ps.setString(11, Ut.trunc(credits.getText(), 255));
            ps.setString(12, Ut.trunc(studio.getText(), 150));
            ps.setString(13, Ut.trunc(trailer.getText(), 255));
            ps.setString(14, Ut.trunc(filePath.getText(), 255));
            ps.setString(15, Ut.trunc(audioLang.getText(), 20));
            ps.setString(16, Ut.trunc(demuxer.getText(), 20));
            ps.setString(17, Ut.trunc(videoFormat.getText(), 20));
            ps.setString(18, Ut.trunc(bitrate.getText(), 20));
            ps.setInt(19, Integer.parseInt(videoWidth.getText()));
            ps.setInt(20, Integer.parseInt(videoHeight.getText()));
            ps.setDouble(21, Double.parseDouble(videoFps.getText()));
            ps.setString(22, Ut.trunc(videoAspect.getText(), 20));
            ps.setString(23, Ut.trunc(length.getText(), 20));
            ps.setString(24, Ut.trunc(videoCodec.getText(), 20));
            ps.setString(25, Ut.trunc(audioFormat.getText(), 20));
            ps.setString(26, Ut.trunc(audioBitrate.getText(), 20));
            ps.setString(27, Ut.trunc(audioRate.getText(), 20));
            ps.setInt(28, Integer.parseInt(audioChannels.getText()));
            ps.setString(29, Ut.trunc(audioCodec.getText(), 20));
            ps.setString(30, interlaced.isSelected() ? "I" : "P");
            ps.setString(31, Ut.trunc(hash.getText(), 16));
            ps.setInt(32, nID);
            ps.executeUpdate();
            ps.close();
            okButtonPressed = true;
        } catch (SQLException ex) {
            Ut.showSQLError(ex);
        }
        if (!oldPath.equals(filePath.getText())) {
            if (!oldPath.equals("")) {
                FileUtilities.cleanupMovie(oldPath);
            }
        }
        CursorToolkit.startWaitCursor(this.getRootPane());
        dbUtil.writeMovieNfo(nID);
        dbUtil.writeMovieArt(nID);
        dbUtil.writeMovieTrailer(nID);
        CursorToolkit.stopWaitCursor(this.getRootPane());
        dispose();
    }

    @Action
    public void cancelPressed() {
        dispose();
    }

    @Action
    public void deleteGenre() {
        int nGenre = genreList.getSelectedIndex();
        DefaultListModel genreModel = (DefaultListModel) genreList.getModel();
        GenreDef genre = (GenreDef) genreModel.getElementAt(nGenre);
        int nGenreID = genre.getGenreID();
        genreModel.removeElementAt(nGenre);
        try {
            String sqlQuery = "DELETE FROM MOVIETOGENRE WHERE GENREID = ? AND MOVIEID = ?";
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, nGenreID);
            ps.setInt(2, nID);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Ut.showSQLError(ex);
        }
    }

    @Action
    public void addGenre() {
        GenreListDlg genreDialog = new GenreListDlg(MediaReaderApp.getApplication().getMainFrame(), true);
        DefaultListModel genreModel = (DefaultListModel) genreList.getModel();
        genreDialog.init(conn);
        MediaReaderApp.getApplication().show(genreDialog);
        if (genreDialog.isOkButtonPressed()) {
            String name = "";
            int nCatID = genreDialog.getSelectedGenre();
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT NAME FROM GENRE WHERE ID = ?");
                ps.setInt(1, nCatID);
                ResultSet rs = ps.executeQuery();
                rs.next();
                name = rs.getString("NAME");
                ps.close();
                ps = conn.prepareStatement("SELECT * FROM MOVIETOGENRE WHERE MOVIEID = ? AND GENREID= ?");
                ps.setInt(1, nID);
                ps.setInt(2, nCatID);
                rs = ps.executeQuery();
                if (rs.next()) {
                    Ut.showMsg("Genre already associated");
                } else {
                    ps.close();
                    ps = conn.prepareStatement("INSERT INTO MOVIETOGENRE VALUES( ?, ?)");
                    ps.setInt(1, nID);
                    ps.setInt(2, nCatID);
                    ps.executeUpdate();
                }
                ps.close();
            } catch (SQLException ex) {
                Ut.showSQLError(ex);
            }
            genreModel.addElement(new GenreDef(nCatID, name));
        }
    }

    @Action
    public void deleteActor() {
        int nActor = actorList.getSelectedIndex();
        DefaultListModel actorModel = (DefaultListModel) actorList.getModel();
        MovieActor actor = (MovieActor) actorModel.getElementAt(nActor);
        int nActorID = actor.getID();
        actorModel.removeElementAt(nActor);
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM MOVIETOACTOR WHERE MOVIEID = ? AND ACTORID = ?");
            ps.setInt(1, nID);
            ps.setInt(2, nActorID);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Ut.showSQLError(ex);
        }
        actorModel.remove(nActor);
    }

    @Action
    public void UpdActor() {
        int nActor = actorList.getSelectedIndex();
        DefaultListModel actorModel = (DefaultListModel) actorList.getModel();
        MovieActor actor = (MovieActor) actorModel.getElementAt(nActor);
        int nActorID = actor.getID();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM MOVIEACTOR WHERE ID = ?");
            ps.setInt(1, nActorID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sName = rs.getString("NAME");
                ps.close();
                ps = conn.prepareStatement("SELECT * FROM MOVIETOACTOR WHERE ACTORID = ? AND MOVIEID = ?");
                ps.setInt(1, nActorID);
                ps.setInt(2, nID);
                rs = ps.executeQuery();
                String sRole = "";
                if (rs.next()) {
                    sRole = rs.getString("ROLE");
                }
                ps.close();
                DualPromptDlg dualTextDialog = new DualPromptDlg(this, true, "Name", sName, "Role:", sRole);
                MediaReaderApp.getApplication().show(dualTextDialog);
                if (dualTextDialog.isOkButtonClicked()) {
                    ps = conn.prepareStatement("UPDATE MOVIEACTOR SET NAME = ? WHERE ID = ?");
                    ps.setString(1, Ut.trunc(dualTextDialog.getValue1(), 50));
                    ps.setInt(2, nActorID);
                    ps.executeUpdate();
                    ps.close();
                    ps = conn.prepareStatement("UPDATE MOVIETOACTOR SET ROLE = ? WHERE MOVIEID = ? AND ACTORID = ?");
                    ps.setString(1, Ut.trunc(dualTextDialog.getValue2(), 50));
                    ps.setInt(2, nID);
                    ps.setInt(3, nActorID);
                    ps.executeUpdate();
                    ps.close();
                    actor.setName(dualTextDialog.getValue1());
                    actor.setRole(dualTextDialog.getValue2());
                    actorModel.setElementAt(actor, nActor);
                }
            }
        } catch (SQLException ex) {
            Ut.showSQLError(ex);
        }
    }

    @Action
    public void setPath() {
        MediaFileChooser chooser = new MediaFileChooser();
        chooser.setDialogTitle("Select media file:");
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            filePath.setText(chooser.getSelectedFile().getAbsolutePath());
        }
        if (filePath.getText().equals("")) {
            playButton.setVisible(false);
        } else {
            playButton.setVisible(true);
            File file = new File(filePath.getText());
            if (file.exists()) {
                DecimalFormat formatter = new DecimalFormat("###,###,###,###");
                fileSize.setText(formatter.format(file.length() / 1024) + " KB.");
            }
        }
    }

    @Action
    public void playFile() {
        Ut.playFile(filePath.getText());
    }

    class BrowserListener implements WindowListener {

        public void windowIconified(WindowEvent ev) {
        }

        public void windowDeiconified(WindowEvent ev) {
        }

        public void windowActivated(WindowEvent ev) {
        }

        public void windowDeactivated(WindowEvent ev) {
        }

        public void windowClosing(WindowEvent ev) {
            Ut.unregisterMozillaListener(this);
            popComponents();
            dialogInstance.removeWindowListener(listener);
        }

        public void windowOpened(WindowEvent ev) {
        }

        public void windowClosed(WindowEvent ev) {
            Ut.unregisterMozillaListener(this);
            popComponents();
            dialogInstance.removeWindowListener(listener);
            if (!Ut.okMozilla()) {
                return;
            }
            URL url = Ut.MozillaURL();
            trailer.setText(URLUtils.getYouTubeVideoURL(url.toString()));
            popComponents();
            dialogInstance.removeWindowListener(listener);
        }
    }

    public class PopWindowListener implements WindowListener {

        public void windowIconified(WindowEvent ev) {
        }

        public void windowDeiconified(WindowEvent ev) {
        }

        public void windowActivated(WindowEvent ev) {
        }

        public void windowDeactivated(WindowEvent ev) {
        }

        public void windowClosing(WindowEvent ev) {
        }

        public void windowOpened(WindowEvent ev) {
        }

        public void windowClosed(WindowEvent ev) {
            Ut.cancelMozilla();
        }
    }

    JDialog dialogInstance = this;

    PopWindowListener listener = new PopWindowListener();

    public void pushComponents() {
        for (int i = 0; i < dialogComponents.length; i++) {
            componentState[i] = dialogComponents[i].isEnabled();
            dialogComponents[i].setEnabled(false);
        }
    }

    public void popComponents() {
        for (int i = 0; i < dialogComponents.length; i++) {
            dialogComponents[i].setEnabled(componentState[i]);
        }
    }

    @Action
    public void browseForImage() {
        String url = URLUtils.getYouTubeURL(movieTitle.getText());
        Ut.loadMozilla(url);
        Ut.registerMozillaListener(new BrowserListener());
        pushComponents();
        this.addWindowListener(listener);
        Ut.setMozillaOk(true);
        Ut.showMozilla();
    }

    private javax.swing.JList actorList;

    private javax.swing.JPopupMenu actorPopup;

    private javax.swing.JMenuItem addGenre;

    private javax.swing.JMenuItem addGenre1;

    private javax.swing.JTextField audioBitrate;

    private javax.swing.JTextField audioChannels;

    private javax.swing.JTextField audioCodec;

    private javax.swing.JTextField audioFormat;

    private javax.swing.JTextField audioLang;

    private javax.swing.JTextField audioRate;

    private javax.swing.JTextField bitrate;

    private javax.swing.JButton cancelButton;

    private javax.swing.JTextField contentRating;

    private javax.swing.JLabel cover;

    private javax.swing.JTextField credits;

    private javax.swing.JMenuItem delAct;

    private javax.swing.JMenuItem deleteGenre;

    private javax.swing.JTextField demuxer;

    private javax.swing.JTextField director;

    private javax.swing.JLabel filePath;

    private javax.swing.JLabel fileSize;

    private javax.swing.JPopupMenu genreAddPopup;

    private javax.swing.JList genreList;

    private javax.swing.JPopupMenu genrePopup;

    private javax.swing.JTextField hash;

    private javax.swing.JTextField imdbID;

    private javax.swing.JCheckBox interlaced;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel25;

    private javax.swing.JLabel jLabel26;

    private javax.swing.JLabel jLabel27;

    private javax.swing.JLabel jLabel28;

    private javax.swing.JLabel jLabel29;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel30;

    private javax.swing.JLabel jLabel31;

    private javax.swing.JLabel jLabel32;

    private javax.swing.JLabel jLabel33;

    private javax.swing.JLabel jLabel34;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JTextField length;

    private javax.swing.JTextField movieTitle;

    private javax.swing.JButton okButton;

    private javax.swing.JTextArea overView;

    private javax.swing.JButton playButton;

    private javax.swing.JTextArea plot;

    private javax.swing.JTextField rating;

    private javax.swing.JTextField releaseDate;

    private javax.swing.JTextField runTime;

    private javax.swing.JTextField studio;

    private javax.swing.JTextField tagLine;

    private javax.swing.JTextField trailer;

    private javax.swing.JMenuItem updAct;

    private javax.swing.JTextField videoAspect;

    private javax.swing.JTextField videoCodec;

    private javax.swing.JTextField videoFormat;

    private javax.swing.JTextField videoFps;

    private javax.swing.JTextField videoHeight;

    private javax.swing.JTextField videoWidth;
}
