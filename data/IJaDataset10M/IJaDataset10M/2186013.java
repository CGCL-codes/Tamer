package com.umc.gui.content.manage;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.java.plugin.registry.Extension;
import com.umc.beans.Genre;
import com.umc.beans.media.Episode;
import com.umc.beans.media.Movie;
import com.umc.beans.media.MovieGroup;
import com.umc.beans.media.Music;
import com.umc.beans.media.MusicAlbum;
import com.umc.beans.media.Photo;
import com.umc.beans.media.PhotoAlbum;
import com.umc.beans.media.Plugins;
import com.umc.beans.media.Season;
import com.umc.beans.media.SeriesGroup;
import com.umc.beans.persons.Actor;
import com.umc.beans.persons.Author;
import com.umc.beans.persons.Director;
import com.umc.beans.persons.IPerson;
import com.umc.beans.persons.Producer;
import com.umc.beans.persons.ProducerOST;
import com.umc.collector.Publisher;
import com.umc.dao.DataAccessFactory;
import com.umc.dao.UMCDataAccessInterface;
import com.umc.gui.GuiController;
import com.umc.gui.content.plugin.PluginDialog;
import com.umc.helper.UMCLanguage;
import com.umc.plugins.gui.IPluginMovie;
import com.umc.plugins.gui.IPluginMusic;
import com.umc.plugins.gui.IPluginPhoto;
import com.umc.plugins.gui.IPluginSeries;
import de.umcProject.xmlbeans.SkinDocument;

/**
 *
 * @author Administrator
 */
public class ManagePanel extends javax.swing.JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3714002918611424402L;

    public static final String ID = "MANAGE";

    private static Logger log = Logger.getLogger("com.umc.file");

    private javax.swing.JComboBox comboMovieLangugage;

    private javax.swing.JComboBox comboMusicLanguage;

    private javax.swing.JComboBox comboSeriesLangugage;

    private javax.swing.JComboBox comboTagLanguage;

    private javax.swing.JComboBox comboPhotoLanguage;

    private javax.swing.JComboBox comboGenreLanguage;

    private javax.swing.JComboBox comboPersonLanguage;

    private javax.swing.JPanel panelLeft;

    private javax.swing.JPanel panelGenre;

    private javax.swing.JPanel panelMovie;

    private javax.swing.JPanel panelMusic;

    private javax.swing.JPanel panelPerson;

    private javax.swing.JPanel panelPhoto;

    private javax.swing.JPanel panelSeries;

    private javax.swing.JPanel panelTag;

    private javax.swing.JScrollPane scrollPaneTreeGenre;

    private javax.swing.JScrollPane scrollPaneTreeMovie;

    private javax.swing.JScrollPane scrollPaneTreeMusic;

    private javax.swing.JScrollPane scrollPaneTreePerson;

    private javax.swing.JScrollPane scrollPaneTreePhoto;

    private javax.swing.JScrollPane scrollPaneTreeSeries;

    private javax.swing.JScrollPane scrollPaneTreeTag;

    private javax.swing.JSplitPane splitPane;

    private javax.swing.JTabbedPane tabbedPaneManage;

    private javax.swing.JTextField textFieldFilterGenre;

    private javax.swing.JTextField textFieldFilterMovie;

    private javax.swing.JTextField textFieldFilterMusic;

    private javax.swing.JTextField textFieldFilterPerson;

    private javax.swing.JTextField textFieldFilterPhoto;

    private javax.swing.JTextField textFieldFilterSeries;

    private javax.swing.JTextField textFieldFilterTag;

    private javax.swing.JToggleButton toggleGenre1;

    private javax.swing.JToggleButton toggleGenre2;

    private javax.swing.JToggleButton toggleMovieFM;

    private javax.swing.JToggleButton toggleMovieUG;

    private javax.swing.JToggleButton toggleMusic1;

    private javax.swing.JToggleButton toggleMusic2;

    private javax.swing.JToggleButton togglePerson1;

    private javax.swing.JToggleButton togglePerson2;

    private javax.swing.JToggleButton togglePhoto1;

    private javax.swing.JToggleButton togglePhoto2;

    private javax.swing.JToggleButton toggleSeries1;

    private javax.swing.JToggleButton toggleSeries2;

    private javax.swing.JToggleButton toggleTag1;

    private javax.swing.JToggleButton toggleTag2;

    private javax.swing.JToolBar toolbarGenre;

    private javax.swing.JToolBar toolbarMovie;

    private javax.swing.JToolBar toolbarMusic;

    private javax.swing.JToolBar toolbarPerson;

    private javax.swing.JToolBar toolbarPhoto;

    private javax.swing.JToolBar toolbarSeries;

    private javax.swing.JToolBar toolbarTag;

    private javax.swing.JTree treeGenre;

    private javax.swing.JTree treeMovie;

    private javax.swing.JTree treeMusic;

    private javax.swing.JTree treePerson;

    private javax.swing.JTree treePhoto;

    private javax.swing.JTree treeSeries;

    private javax.swing.JTree treeTag;

    private ManageMoviePanel panelManageMovie;

    private ManageMusicPanel panelManageMusic;

    private ManagePhotoPanel panelManagePhoto;

    private ManageSeriesPanel panelManageSeries;

    private ManagePersonPanel panelManagePerson;

    private ManageGenrePanel panelManageGenre;

    private JPanel panelManageTag;

    private FormListener formListener;

    private Collection<Object> movieList = null;

    private Collection<Object> seriesList = null;

    private Collection<Object> musicList = null;

    private Collection<Object> photoList = null;

    private Collection<IPerson> personList = null;

    private Collection<Genre> genreList = null;

    private Object selectedMovie = null;

    private boolean updateMovieList = true;

    private boolean updateSeriesList = true;

    private boolean updateMusicList = true;

    private boolean updatePhotoList = true;

    private boolean updatePersonList = true;

    private boolean updateGenreList = true;

    private InitMovieList initMovie = null;

    private InitSeriesList initSeries = null;

    private InitMusicList initMusic = null;

    private InitPhotoList initPhoto = null;

    private InitPersonList initPersons = null;

    private InitGenreList initGenres = null;

    private DefaultTreeModel loadingTreeModel = null;

    /** Creates new form GenrePanel */
    public ManagePanel() {
        initComponents();
        initContent();
    }

    private void initContent() {
        splitPane.setRightComponent(panelManageMovie);
        comboMovieLangugage.addActionListener(formListener);
        comboMusicLanguage.addActionListener(formListener);
        comboSeriesLangugage.addActionListener(formListener);
        comboTagLanguage.addActionListener(formListener);
        comboGenreLanguage.addActionListener(formListener);
    }

    private void initComponents() {
        splitPane = new javax.swing.JSplitPane();
        panelLeft = new javax.swing.JPanel();
        tabbedPaneManage = new javax.swing.JTabbedPane();
        panelMovie = new javax.swing.JPanel();
        toolbarMovie = new javax.swing.JToolBar();
        toggleMovieFM = new javax.swing.JToggleButton();
        toggleMovieUG = new javax.swing.JToggleButton();
        textFieldFilterMovie = new javax.swing.JTextField();
        scrollPaneTreeMovie = new javax.swing.JScrollPane();
        treeMovie = new javax.swing.JTree();
        comboMovieLangugage = new javax.swing.JComboBox();
        panelSeries = new javax.swing.JPanel();
        toolbarSeries = new javax.swing.JToolBar();
        toggleSeries1 = new javax.swing.JToggleButton();
        toggleSeries2 = new javax.swing.JToggleButton();
        textFieldFilterSeries = new javax.swing.JTextField();
        scrollPaneTreeSeries = new javax.swing.JScrollPane();
        treeSeries = new javax.swing.JTree();
        comboSeriesLangugage = new javax.swing.JComboBox();
        panelMusic = new javax.swing.JPanel();
        toolbarMusic = new javax.swing.JToolBar();
        toggleMusic1 = new javax.swing.JToggleButton();
        toggleMusic2 = new javax.swing.JToggleButton();
        textFieldFilterMusic = new javax.swing.JTextField();
        scrollPaneTreeMusic = new javax.swing.JScrollPane();
        treeMusic = new javax.swing.JTree();
        comboMusicLanguage = new javax.swing.JComboBox();
        panelPhoto = new javax.swing.JPanel();
        toolbarPhoto = new javax.swing.JToolBar();
        togglePhoto1 = new javax.swing.JToggleButton();
        togglePhoto2 = new javax.swing.JToggleButton();
        textFieldFilterPhoto = new javax.swing.JTextField();
        scrollPaneTreePhoto = new javax.swing.JScrollPane();
        comboPhotoLanguage = new javax.swing.JComboBox();
        treePhoto = new javax.swing.JTree();
        panelGenre = new javax.swing.JPanel();
        toolbarGenre = new javax.swing.JToolBar();
        toggleGenre1 = new javax.swing.JToggleButton();
        toggleGenre2 = new javax.swing.JToggleButton();
        textFieldFilterGenre = new javax.swing.JTextField();
        scrollPaneTreeGenre = new javax.swing.JScrollPane();
        treeGenre = new javax.swing.JTree();
        comboGenreLanguage = new javax.swing.JComboBox();
        panelPerson = new javax.swing.JPanel();
        comboPersonLanguage = new javax.swing.JComboBox();
        toolbarPerson = new javax.swing.JToolBar();
        togglePerson1 = new javax.swing.JToggleButton();
        togglePerson2 = new javax.swing.JToggleButton();
        textFieldFilterPerson = new javax.swing.JTextField();
        scrollPaneTreePerson = new javax.swing.JScrollPane();
        treePerson = new javax.swing.JTree();
        panelTag = new javax.swing.JPanel();
        toolbarTag = new javax.swing.JToolBar();
        toggleTag1 = new javax.swing.JToggleButton();
        toggleTag2 = new javax.swing.JToggleButton();
        textFieldFilterTag = new javax.swing.JTextField();
        scrollPaneTreeTag = new javax.swing.JScrollPane();
        treeTag = new javax.swing.JTree();
        comboTagLanguage = new javax.swing.JComboBox();
        LinkedHashMap<String, Plugins> pluginConfig = Publisher.getInstance().getPluginConfiguration();
        Iterator<String> ite = pluginConfig.keySet().iterator();
        String language = null;
        while (ite.hasNext()) {
            language = ite.next();
            if (StringUtils.isNotEmpty(language)) {
                comboMovieLangugage.addItem(language);
                comboSeriesLangugage.addItem(language);
                comboMusicLanguage.addItem(language);
                comboTagLanguage.addItem(language);
                comboGenreLanguage.addItem(language);
            }
        }
        loadingTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Loading..."));
        panelManageMovie = new ManageMoviePanel(this);
        panelManageMusic = new ManageMusicPanel();
        panelManagePhoto = new ManagePhotoPanel();
        panelManageSeries = new ManageSeriesPanel();
        panelManageGenre = new ManageGenrePanel();
        panelManagePerson = new ManagePersonPanel();
        panelManageTag = new JPanel();
        formListener = new FormListener();
        splitPane.setDividerLocation(333);
        splitPane.setDividerSize(4);
        panelLeft.setMinimumSize(new java.awt.Dimension(333, 100));
        tabbedPaneManage.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        tabbedPaneManage.setMinimumSize(new java.awt.Dimension(333, 100));
        tabbedPaneManage.setPreferredSize(new java.awt.Dimension(333, 100));
        tabbedPaneManage.addChangeListener(formListener);
        textFieldFilterMovie.addKeyListener(formListener);
        treeSeries.setCellRenderer(new SeriesTreeCellRenderer());
        treeMusic.setCellRenderer(new MusicTreeCellRenderer());
        treePhoto.setCellRenderer(new PhotoTreeCellRenderer());
        treePerson.setCellRenderer(new PersonsTreeCellRenderer());
        treeMovie.addTreeSelectionListener(formListener);
        treeMovie.setDragEnabled(false);
        treeMovie.setDropMode(DropMode.ON);
        treeMovie.setCellRenderer(new MovieTreeCellRenderer());
        treeMovie.setTransferHandler(new MovieTreeTransferHandler());
        treeMovie.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        treeMovie.addMouseListener(formListener);
        treeMovie.addTreeSelectionListener(formListener);
        treeMovie.setModel(loadingTreeModel);
        toggleMovieFM.addActionListener(formListener);
        toggleMovieUG.addActionListener(formListener);
        scrollPaneTreeMovie.setViewportView(treeMovie);
        comboMovieLangugage.addActionListener(formListener);
        toolbarMovie.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolbarMovie.setFloatable(false);
        toolbarMovie.setMargin(new java.awt.Insets(1, 1, 1, 1));
        toggleMovieFM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleMovieFM.setSelected(true);
        toggleMovieFM.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleMovieFM.setRolloverEnabled(false);
        toggleMovieFM.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarMovie.add(toggleMovieFM);
        toggleMovieUG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleMovieUG.setSelected(true);
        toggleMovieUG.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleMovieUG.setRolloverEnabled(false);
        toggleMovieUG.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarMovie.add(toggleMovieUG);
        org.jdesktop.layout.GroupLayout panelMovieLayout = new org.jdesktop.layout.GroupLayout(panelMovie);
        panelMovie.setLayout(panelMovieLayout);
        panelMovieLayout.setHorizontalGroup(panelMovieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, panelMovieLayout.createSequentialGroup().addContainerGap().add(panelMovieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, scrollPaneTreeMovie, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, comboMovieLangugage, 0, 226, Short.MAX_VALUE).add(panelMovieLayout.createSequentialGroup().add(textFieldFilterMovie, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toolbarMovie, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelMovieLayout.setVerticalGroup(panelMovieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelMovieLayout.createSequentialGroup().addContainerGap().add(comboMovieLangugage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelMovieLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(toolbarMovie, 0, 0, Short.MAX_VALUE).add(textFieldFilterMovie)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPaneTreeMovie, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE).addContainerGap()));
        tabbedPaneManage.addTab(UMCLanguage.getText("ManagePanel.panelMovie.TabConstraints.tabTitle"), panelMovie);
        textFieldFilterSeries.addKeyListener(formListener);
        treeSeries.addTreeSelectionListener(formListener);
        scrollPaneTreeSeries.setViewportView(treeSeries);
        toolbarSeries.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolbarSeries.setFloatable(false);
        toolbarSeries.setBorderPainted(false);
        toggleSeries1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleSeries1.setSelected(true);
        toggleSeries1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleSeries1.setRolloverEnabled(false);
        toggleSeries1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarSeries.add(toggleSeries1);
        toggleSeries2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleSeries2.setSelected(true);
        toggleSeries2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleSeries2.setRolloverEnabled(false);
        toggleSeries2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarSeries.add(toggleSeries2);
        org.jdesktop.layout.GroupLayout panelSeriesLayout = new org.jdesktop.layout.GroupLayout(panelSeries);
        panelSeries.setLayout(panelSeriesLayout);
        panelSeriesLayout.setHorizontalGroup(panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, panelSeriesLayout.createSequentialGroup().addContainerGap().add(panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, scrollPaneTreeSeries, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, comboSeriesLangugage, 0, 226, Short.MAX_VALUE).add(panelSeriesLayout.createSequentialGroup().add(textFieldFilterSeries, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toolbarSeries, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelSeriesLayout.setVerticalGroup(panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelSeriesLayout.createSequentialGroup().addContainerGap().add(comboSeriesLangugage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelSeriesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(toolbarSeries, 0, 0, Short.MAX_VALUE).add(textFieldFilterSeries)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPaneTreeSeries, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE).addContainerGap()));
        tabbedPaneManage.addTab(UMCLanguage.getText("ManagePanel.panelSeries.TabConstraints.tabTitle"), panelSeries);
        textFieldFilterMusic.addKeyListener(formListener);
        treeMusic.addTreeSelectionListener(formListener);
        scrollPaneTreeMusic.setViewportView(treeMusic);
        toolbarMusic.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolbarMusic.setFloatable(false);
        toolbarMusic.setBorderPainted(false);
        toggleMusic1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleMusic1.setSelected(true);
        toggleMusic1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleMusic1.setRolloverEnabled(false);
        toggleMusic1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarMusic.add(toggleMusic1);
        toggleMusic2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleMusic2.setSelected(true);
        toggleMusic2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleMusic2.setRolloverEnabled(false);
        toggleMusic2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarMusic.add(toggleMusic2);
        org.jdesktop.layout.GroupLayout panelMusicLayout = new org.jdesktop.layout.GroupLayout(panelMusic);
        panelMusic.setLayout(panelMusicLayout);
        panelMusicLayout.setHorizontalGroup(panelMusicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, panelMusicLayout.createSequentialGroup().addContainerGap().add(panelMusicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, scrollPaneTreeMusic, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, comboMusicLanguage, 0, 226, Short.MAX_VALUE).add(panelMusicLayout.createSequentialGroup().add(textFieldFilterMusic, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toolbarMusic, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelMusicLayout.setVerticalGroup(panelMusicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelMusicLayout.createSequentialGroup().addContainerGap().add(comboMusicLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelMusicLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(toolbarMusic, 0, 0, Short.MAX_VALUE).add(textFieldFilterMusic)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPaneTreeMusic, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE).addContainerGap()));
        tabbedPaneManage.addTab(UMCLanguage.getText("ManagePanel.panelMusic.TabConstraints.tabTitle"), panelMusic);
        textFieldFilterPhoto.addKeyListener(formListener);
        treePhoto.addTreeSelectionListener(formListener);
        scrollPaneTreePhoto.setViewportView(treePhoto);
        toolbarPhoto.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolbarPhoto.setFloatable(false);
        toolbarPhoto.setBorderPainted(false);
        togglePhoto1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        togglePhoto1.setSelected(true);
        togglePhoto1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        togglePhoto1.setRolloverEnabled(false);
        togglePhoto1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarPhoto.add(togglePhoto1);
        togglePhoto2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        togglePhoto2.setSelected(true);
        togglePhoto2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        togglePhoto2.setRolloverEnabled(false);
        togglePhoto2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarPhoto.add(togglePhoto2);
        org.jdesktop.layout.GroupLayout panelPhotoLayout = new org.jdesktop.layout.GroupLayout(panelPhoto);
        panelPhoto.setLayout(panelPhotoLayout);
        panelPhotoLayout.setHorizontalGroup(panelPhotoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelPhotoLayout.createSequentialGroup().addContainerGap().add(panelPhotoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, scrollPaneTreePhoto, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).add(comboPhotoLanguage, 0, 226, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, panelPhotoLayout.createSequentialGroup().add(textFieldFilterPhoto, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toolbarPhoto, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelPhotoLayout.setVerticalGroup(panelPhotoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelPhotoLayout.createSequentialGroup().addContainerGap().add(comboPhotoLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelPhotoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(toolbarPhoto, 0, 0, Short.MAX_VALUE).add(textFieldFilterPhoto)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPaneTreePhoto, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE).addContainerGap()));
        tabbedPaneManage.addTab(UMCLanguage.getText("ManagePanel.panelPhoto.TabConstraints.tabTitle"), panelPhoto);
        textFieldFilterGenre.addKeyListener(formListener);
        treeGenre.addTreeSelectionListener(formListener);
        scrollPaneTreeGenre.setViewportView(treeGenre);
        toolbarGenre.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolbarGenre.setFloatable(false);
        toolbarGenre.setBorderPainted(false);
        toggleGenre1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleGenre1.setSelected(true);
        toggleGenre1.setFocusable(false);
        toggleGenre1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleGenre1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleGenre1.setRolloverEnabled(false);
        toggleGenre1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarGenre.add(toggleGenre1);
        toggleGenre2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleGenre2.setSelected(true);
        toggleGenre2.setFocusable(false);
        toggleGenre2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleGenre2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleGenre2.setRolloverEnabled(false);
        toggleGenre2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarGenre.add(toggleGenre2);
        org.jdesktop.layout.GroupLayout panelGenreLayout = new org.jdesktop.layout.GroupLayout(panelGenre);
        panelGenre.setLayout(panelGenreLayout);
        panelGenreLayout.setHorizontalGroup(panelGenreLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, panelGenreLayout.createSequentialGroup().addContainerGap().add(panelGenreLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, scrollPaneTreeGenre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, comboGenreLanguage, 0, 226, Short.MAX_VALUE).add(panelGenreLayout.createSequentialGroup().add(textFieldFilterGenre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toolbarGenre, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelGenreLayout.setVerticalGroup(panelGenreLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelGenreLayout.createSequentialGroup().addContainerGap().add(comboGenreLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelGenreLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(toolbarGenre, 0, 0, Short.MAX_VALUE).add(textFieldFilterGenre)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPaneTreeGenre, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE).addContainerGap()));
        tabbedPaneManage.addTab(UMCLanguage.getText("ManagePanel.panelGenre.TabConstraints.tabTitle"), panelGenre);
        textFieldFilterPerson.addKeyListener(formListener);
        treePerson.addTreeSelectionListener(formListener);
        scrollPaneTreePerson.setViewportView(treePerson);
        toolbarPerson.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolbarPerson.setFloatable(false);
        toolbarPerson.setBorderPainted(false);
        togglePerson1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        togglePerson1.setSelected(true);
        togglePerson1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        togglePerson1.setRolloverEnabled(false);
        togglePerson1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarPerson.add(togglePerson1);
        togglePerson2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        togglePerson2.setSelected(true);
        togglePerson2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        togglePerson2.setRolloverEnabled(false);
        togglePerson2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarPerson.add(togglePerson2);
        org.jdesktop.layout.GroupLayout panelPersonLayout = new org.jdesktop.layout.GroupLayout(panelPerson);
        panelPerson.setLayout(panelPersonLayout);
        panelPersonLayout.setHorizontalGroup(panelPersonLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, panelPersonLayout.createSequentialGroup().addContainerGap().add(panelPersonLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, scrollPaneTreePerson, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, comboPersonLanguage, 0, 226, Short.MAX_VALUE).add(panelPersonLayout.createSequentialGroup().add(textFieldFilterPerson, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toolbarPerson, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelPersonLayout.setVerticalGroup(panelPersonLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelPersonLayout.createSequentialGroup().addContainerGap().add(comboPersonLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelPersonLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(toolbarPerson, 0, 0, Short.MAX_VALUE).add(textFieldFilterPerson)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPaneTreePerson, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE).addContainerGap()));
        tabbedPaneManage.addTab(UMCLanguage.getText("ManagePanel.panelPerson.TabConstraints.tabTitle"), panelPerson);
        textFieldFilterTag.addKeyListener(formListener);
        treeTag.addTreeSelectionListener(formListener);
        scrollPaneTreeTag.setViewportView(treeTag);
        toolbarTag.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        toolbarTag.setFloatable(false);
        toggleTag1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleTag1.setSelected(true);
        toggleTag1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleTag1.setRolloverEnabled(false);
        toggleTag1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarTag.add(toggleTag1);
        toggleTag2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toggleTag2.setSelected(true);
        toggleTag2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        toggleTag2.setRolloverEnabled(false);
        toggleTag2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/umc/gui/resources/manage/movie0.png")));
        toolbarTag.add(toggleTag2);
        org.jdesktop.layout.GroupLayout panelTagLayout = new org.jdesktop.layout.GroupLayout(panelTag);
        panelTag.setLayout(panelTagLayout);
        panelTagLayout.setHorizontalGroup(panelTagLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, panelTagLayout.createSequentialGroup().addContainerGap().add(panelTagLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, scrollPaneTreeTag, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, comboTagLanguage, 0, 226, Short.MAX_VALUE).add(panelTagLayout.createSequentialGroup().add(textFieldFilterTag, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(toolbarTag, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        panelTagLayout.setVerticalGroup(panelTagLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(panelTagLayout.createSequentialGroup().addContainerGap().add(comboTagLanguage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(panelTagLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(toolbarTag, 0, 0, Short.MAX_VALUE).add(textFieldFilterTag)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(scrollPaneTreeTag, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE).addContainerGap()));
        tabbedPaneManage.addTab(UMCLanguage.getText("ManagePanel.panelTag.TabConstraints.tabTitle"), panelTag);
        org.jdesktop.layout.GroupLayout panelLeftLayout = new org.jdesktop.layout.GroupLayout(panelLeft);
        panelLeft.setLayout(panelLeftLayout);
        panelLeftLayout.setHorizontalGroup(panelLeftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(tabbedPaneManage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE));
        panelLeftLayout.setVerticalGroup(panelLeftLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(tabbedPaneManage, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE));
        splitPane.setLeftComponent(panelLeft);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(splitPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE));
    }

    private class FormListener implements java.awt.event.ActionListener, java.awt.event.KeyListener, javax.swing.event.ChangeListener, TreeSelectionListener, MouseListener {

        FormListener() {
        }

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == toggleMovieFM) {
                ManagePanel.this.toggleMovieFMActionPerformed(evt);
            } else if (evt.getSource() == toggleSeries1) {
                ManagePanel.this.toggleSeries1ActionPerformed(evt);
            } else if (evt.getSource() == toggleMusic1) {
                ManagePanel.this.toggleMusic1ActionPerformed(evt);
            } else if (evt.getSource() == toggleMovieUG) {
                ManagePanel.this.toggleMovieUGActionPerformed(evt);
            } else if (evt.getSource() == togglePhoto1) {
                ManagePanel.this.togglePicture1ActionPerformed(evt);
            } else if (evt.getSource() == togglePhoto2) {
                ManagePanel.this.togglePicture2ActionPerformed(evt);
            } else if (evt.getSource() == toggleGenre1) {
                ManagePanel.this.toggleGenre1ActionPerformed(evt);
            } else if (evt.getSource() == toggleGenre2) {
                ManagePanel.this.toggleGenre2ActionPerformed(evt);
            } else if (evt.getSource() == togglePerson1) {
                ManagePanel.this.togglePerson1ActionPerformed(evt);
            } else if (evt.getSource() == togglePerson2) {
                ManagePanel.this.togglePerson2ActionPerformed(evt);
            } else if (evt.getSource() == toggleTag1) {
                ManagePanel.this.toggleTag1ActionPerformed(evt);
            } else if (evt.getSource() == toggleTag2) {
                ManagePanel.this.toggleTag2ActionPerformed(evt);
            } else if (evt.getSource() == comboMovieLangugage) {
                ManagePanel.this.comboMovieLangugageActionPerformed(evt);
            } else if (evt.getActionCommand().equals("add2group")) {
            }
        }

        public void keyPressed(java.awt.event.KeyEvent evt) {
        }

        public void keyReleased(java.awt.event.KeyEvent evt) {
            if (evt.getSource() == textFieldFilterMovie) {
                ManagePanel.this.textFieldFilterMovieKeyReleased(evt);
            } else if (evt.getSource() == textFieldFilterSeries) {
                ManagePanel.this.textFieldFilterSeriesKeyReleased(evt);
            } else if (evt.getSource() == textFieldFilterMusic) {
                ManagePanel.this.textFieldFilterMusicKeyReleased(evt);
            } else if (evt.getSource() == textFieldFilterPhoto) {
                ManagePanel.this.textFieldFilterPictureKeyReleased(evt);
            } else if (evt.getSource() == textFieldFilterGenre) {
                ManagePanel.this.textFieldFilterGenreKeyReleased(evt);
            } else if (evt.getSource() == textFieldFilterPerson) {
                ManagePanel.this.textFieldFilterPersonKeyReleased(evt);
            } else if (evt.getSource() == textFieldFilterTag) {
                ManagePanel.this.textFieldFilterTagKeyReleased(evt);
            }
        }

        public void keyTyped(java.awt.event.KeyEvent evt) {
        }

        public void stateChanged(javax.swing.event.ChangeEvent evt) {
            if (evt.getSource() == tabbedPaneManage) {
                ManagePanel.this.tabbedPaneManageStateChanged(evt);
            }
        }

        public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
            if (evt.getSource() == treeMovie) {
                ManagePanel.this.treeMovieValueChanged(evt);
            } else if (evt.getSource() == treeSeries) {
                ManagePanel.this.treeSeriesValueChanged(evt);
            } else if (evt.getSource() == treeMusic) {
                ManagePanel.this.treeMusicValueChanged(evt);
            } else if (evt.getSource() == treePhoto) {
                ManagePanel.this.treePhotoValueChanged(evt);
            } else if (evt.getSource() == treeGenre) {
                ManagePanel.this.treeGenreValueChanged(evt);
            } else if (evt.getSource() == treePerson) {
                ManagePanel.this.treePersonValueChanged(evt);
            } else if (evt.getSource() == treeTag) {
                ManagePanel.this.treeTagValueChanged(evt);
            }
        }

        public void mouseClicked(java.awt.event.MouseEvent evt) {
        }

        public void mouseEntered(java.awt.event.MouseEvent evt) {
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {
        }

        public void mousePressed(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == treeMovie) {
                ManagePanel.this.showTreeMoviePopupMenu(evt);
            } else if (evt.getSource() == treeMusic) {
                ManagePanel.this.treeMusicMousePressed(evt);
            } else if (evt.getSource() == treePhoto) {
                ManagePanel.this.treePhotoMousePressed(evt);
            }
        }

        public void mouseReleased(java.awt.event.MouseEvent evt) {
            if (evt.getSource() == treeMovie) {
                ManagePanel.this.showTreeMoviePopupMenu(evt);
            } else if (evt.getSource() == treeMusic) {
                ManagePanel.this.treeMusicMousePressed(evt);
            } else if (evt.getSource() == treePhoto) {
                ManagePanel.this.treePhotoMousePressed(evt);
            }
        }
    }

    private void textFieldFilterMovieKeyReleased(java.awt.event.KeyEvent evt) {
        filterMovieTree();
    }

    private void toggleMovieFMActionPerformed(java.awt.event.ActionEvent evt) {
        filterMovieTree();
    }

    private void toggleMovieUGActionPerformed(java.awt.event.ActionEvent evt) {
        filterMovieTree();
    }

    private void treeMovieValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMovie.getLastSelectedPathComponent();
        if (node == null || node.isRoot() || treeMovie.getSelectionCount() == 0 || treeMovie.getSelectionCount() > 1) {
            panelManageMovie.showMovie(null, null);
            return;
        }
        panelManageMovie.showMovie(node.getUserObject(), comboMovieLangugage.getSelectedItem().toString());
    }

    public void updateMovieTreeSelection(Object o) {
        treeMovie.getModel().valueForPathChanged(treeMovie.getSelectionPath(), o);
    }

    private void toggleSeries1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void textFieldFilterSeriesKeyReleased(java.awt.event.KeyEvent evt) {
    }

    private void treeSeriesValueChanged(javax.swing.event.TreeSelectionEvent evt) {
    }

    private void toggleMusic1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void textFieldFilterMusicKeyReleased(java.awt.event.KeyEvent evt) {
    }

    private void treeMusicValueChanged(javax.swing.event.TreeSelectionEvent evt) {
    }

    private void treePhotoValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePhoto.getLastSelectedPathComponent();
        if (node == null || node.isRoot() || treePhoto.getSelectionCount() == 0 || treePhoto.getSelectionCount() > 1) {
            panelManagePhoto.showPhoto(null);
            return;
        }
        panelManagePhoto.showPhoto(node.getUserObject());
    }

    private void tabbedPaneManageStateChanged(javax.swing.event.ChangeEvent evt) {
        panelLeft.setPreferredSize(new Dimension(panelLeft.getWidth(), 0));
        switch(tabbedPaneManage.getSelectedIndex()) {
            case 0:
                panelManageMovie.setPreferredSize(new Dimension(panelManageMovie.getWidth(), 0));
                splitPane.setRightComponent(panelManageMovie);
                if ((initMovie == null || (initMovie != null && initMovie.isDone())) && (movieList == null || updateMovieList)) {
                    initMovie = new InitMovieList(updateMovieList);
                    initMovie.execute();
                    updateMovieList = false;
                }
                break;
            case 1:
                splitPane.setRightComponent(panelManageSeries);
                if ((initSeries == null || (initSeries != null && initSeries.isDone())) && (seriesList == null || updateSeriesList)) {
                    initSeries = new InitSeriesList(updateSeriesList);
                    initSeries.execute();
                    updateSeriesList = false;
                }
                break;
            case 2:
                splitPane.setRightComponent(panelManageMusic);
                if ((initMusic == null || (initMusic != null && initMusic.isDone())) && (musicList == null || updateMusicList)) {
                    initMusic = new InitMusicList(updateMusicList);
                    initMusic.execute();
                    updateMusicList = false;
                }
                break;
            case 3:
                splitPane.setRightComponent(panelManagePhoto);
                if ((initPhoto == null || (initPhoto != null && initPhoto.isDone())) && (photoList == null || updatePhotoList)) {
                    initPhoto = new InitPhotoList(updatePhotoList);
                    initPhoto.execute();
                    updatePhotoList = false;
                }
                break;
            case 4:
                splitPane.setRightComponent(panelManageGenre);
                if ((initGenres == null || (initGenres != null && initGenres.isDone())) && (genreList == null || updateGenreList)) {
                    initGenres = new InitGenreList(updateGenreList);
                    initGenres.execute();
                    updateGenreList = false;
                }
                break;
            case 5:
                splitPane.setRightComponent(panelManagePerson);
                if ((initPersons == null || (initPersons != null && initPersons.isDone())) && (personList == null || updatePersonList)) {
                    initPersons = new InitPersonList(updatePersonList);
                    initPersons.execute();
                    updatePersonList = false;
                }
                break;
            case 6:
                splitPane.setRightComponent(panelManageTag);
                break;
        }
    }

    private void togglePicture1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void togglePicture2ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void textFieldFilterPictureKeyReleased(java.awt.event.KeyEvent evt) {
    }

    private void toggleGenre1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void toggleGenre2ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void textFieldFilterGenreKeyReleased(java.awt.event.KeyEvent evt) {
    }

    private void togglePerson1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void togglePerson2ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void textFieldFilterPersonKeyReleased(java.awt.event.KeyEvent evt) {
        filterPersonTree();
    }

    private void treePersonValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePerson.getLastSelectedPathComponent();
        if (node == null || node.isRoot() || treePerson.getSelectionCount() == 0 || treePerson.getSelectionCount() > 1) {
            panelManagePerson.showPerson(null);
            return;
        }
        panelManagePerson.showPerson(node.getUserObject());
    }

    private void treeGenreValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeGenre.getLastSelectedPathComponent();
        if (node == null || node.isRoot() || treeGenre.getSelectionCount() == 0 || treeGenre.getSelectionCount() > 1) {
            panelManageGenre.showGenre(null, null);
            return;
        }
        panelManageGenre.showGenre(node.getUserObject(), comboGenreLanguage.getSelectedItem().toString());
    }

    private void toggleTag1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void toggleTag2ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void textFieldFilterTagKeyReleased(java.awt.event.KeyEvent evt) {
    }

    private void treeTagValueChanged(javax.swing.event.TreeSelectionEvent evt) {
    }

    private void comboMovieLangugageActionPerformed(java.awt.event.ActionEvent evt) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMovie.getLastSelectedPathComponent();
        if (node != null) {
            selectedMovie = node.getUserObject();
        }
        GuiController.getInstance().setSelectedScanLanguage(comboMovieLangugage.getSelectedItem().toString());
        textFieldFilterMovie.setText("");
        treeMovie.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Loading")));
        panelManageMovie.showMovie(null, null);
        initMovie = new InitMovieList(true);
        initMovie.execute();
        updateMovieList = false;
    }

    public void updateManageComboLanguage() {
        DefaultComboBoxModel movieLng = new DefaultComboBoxModel();
        DefaultComboBoxModel seriesLng = new DefaultComboBoxModel();
        LinkedHashMap<String, Plugins> pluginConfig = Publisher.getInstance().getPluginConfiguration();
        Iterator<String> ite = pluginConfig.keySet().iterator();
        String language = null;
        while (ite.hasNext()) {
            language = ite.next();
            if (StringUtils.isNotEmpty(language)) {
                movieLng.addElement(language);
                seriesLng.addElement(language);
            }
        }
        comboMovieLangugage.setModel(movieLng);
        comboSeriesLangugage.setModel(seriesLng);
    }

    /**
     * Wird unter anderem von UMCScanner aufgerufen um den aktuellen Baum aus der DB zu holen.
     */
    public void updateManageTree() {
        updateMovieList = true;
        updateSeriesList = true;
        updateMusicList = true;
        updatePhotoList = true;
        switch(tabbedPaneManage.getSelectedIndex()) {
            case 0:
                initMovie = new InitMovieList(updateMovieList);
                initMovie.execute();
                updateMovieList = false;
                break;
            case 1:
                initSeries = new InitSeriesList(updateSeriesList);
                initSeries.execute();
                updateSeriesList = false;
                break;
            case 2:
                initMusic = new InitMusicList(updateMusicList);
                initMusic.execute();
                updateMusicList = false;
                break;
            case 3:
                updatePhotoList = false;
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                initPersons = new InitPersonList(updatePersonList);
                initPersons.execute();
                updatePersonList = false;
                break;
        }
    }

    private void filterMovieTree() {
        TreePath moviePath = null;
        String lng = comboMovieLangugage.getSelectedItem().toString();
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Movies");
        for (Object o : movieList) {
            if (o instanceof Movie && toggleMovieFM.isSelected()) {
                Movie movie = (Movie) o;
                String title = StringUtils.isNotEmpty(movie.getLanguageData().getTitle(lng)) ? movie.getLanguageData().getTitle(lng) : movie.getFilename();
                if (title.toLowerCase().indexOf(textFieldFilterMovie.getText().toLowerCase()) != -1) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                    node.setUserObject(movie);
                    rootNode.add(node);
                    if (selectedMovie != null && movie.equals(selectedMovie)) {
                        moviePath = new TreePath(node.getPath());
                    }
                }
            } else if (o instanceof MovieGroup && toggleMovieUG.isSelected()) {
                MovieGroup mg = (MovieGroup) o;
                DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode();
                groupNode.setUserObject(mg);
                rootNode.add(groupNode);
                for (Movie m : mg.getChilds()) {
                    String title = StringUtils.isNotEmpty(m.getLanguageData().getTitle(lng)) ? m.getLanguageData().getTitle(lng) : m.getFilename();
                    if (title.toLowerCase().indexOf(textFieldFilterMovie.getText().toLowerCase()) != -1) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                        node.setUserObject(m);
                        groupNode.add(node);
                        if (selectedMovie != null && m.equals(selectedMovie)) {
                            moviePath = new TreePath(node.getPath());
                        }
                    }
                }
            }
        }
        DefaultTreeModel tree = new DefaultTreeModel(rootNode);
        treeMovie.setModel(tree);
        treeMovie.setSelectionPath(moviePath);
        treeMovie.scrollPathToVisible(moviePath);
    }

    private void showTreeMoviePopupMenu(MouseEvent e) {
        if (e.isPopupTrigger() && treeMovie.isEnabled()) {
            boolean contains = false;
            JPopupMenu pop = new JPopupMenu();
            pop.add(new DeleteMovieAction("Delete", true));
            JMenu tagAsMenu = new JMenu("Tag as");
            tagAsMenu.add(new TagMovieAction("Watched", true, TagMovieAction.WATCHED, true));
            tagAsMenu.add(new TagMovieAction("UnWatched", true, TagMovieAction.WATCHED, false));
            tagAsMenu.add(new TagMovieAction("Locked", true, TagMovieAction.LOCKED, true));
            tagAsMenu.add(new TagMovieAction("Unlock", true, TagMovieAction.LOCKED, false));
            pop.add(tagAsMenu);
            boolean showPop = false;
            int[] paths = treeMovie.getSelectionRows();
            if (paths != null) {
                int clickedRow = treeMovie.getRowForLocation(e.getX(), e.getY());
                for (int i : paths) {
                    if (clickedRow == i) {
                        contains = true;
                        break;
                    }
                }
                if (contains) {
                    showPop = true;
                } else {
                    if (clickedRow != -1) {
                        treeMovie.setSelectionRow(clickedRow);
                        showPop = true;
                    } else {
                        treeMovie.setSelectionRow(-1);
                    }
                }
            } else {
                int selRow = treeMovie.getRowForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    treeMovie.setSelectionRow(selRow);
                    if (pop != null) {
                        showPop = true;
                    }
                }
            }
            if (showPop) {
                pop.add(new JSeparator());
                JMenu skinsMenu = new JMenu("Create images for");
                skinsMenu.setToolTipText("Skin dependent images will be generated for the selected movies");
                for (SkinDocument sd : Publisher.getInstance().getAllValidSkins()) {
                    skinsMenu.add(new SkinAction(sd.getSkin().getName(), true, sd));
                }
                pop.add(skinsMenu);
                pop.add(new JSeparator());
                JMenu pluginsMenu = new JMenu("Plugins");
                for (final Extension ext : Publisher.getInstance().getGuiMoviePlugins().values()) {
                    pluginsMenu.add(new PluginMovieAction(ext, true));
                }
                pop.add(pluginsMenu);
                pop.show(treeMovie, e.getX(), e.getY());
            }
        }
    }

    private class SkinAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        SkinDocument skinDoc;

        public SkinAction(String text, boolean enabled, SkinDocument sd) {
            super(text, null);
            this.enabled = enabled;
            this.skinDoc = sd;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            TreePath[] paths = treeMovie.getSelectionPaths();
            for (TreePath path : paths) {
                Object o = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                if (o instanceof Movie) {
                    Movie movie = (Movie) o;
                } else if (o instanceof MovieGroup) {
                    MovieGroup group = (MovieGroup) o;
                }
            }
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    private class PluginMovieAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private Extension ext;

        public PluginMovieAction(Extension ext, boolean enabled) {
            super(ext.getParameter("name").valueAsString(), null);
            this.enabled = enabled;
            this.ext = ext;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final IPluginMovie plugin = Publisher.getInstance().loadGuiMoviePlugin(ext);
            if (plugin != null) {
                Collection<Movie> movieList = new ArrayList<Movie>();
                for (TreePath path : treeMovie.getSelectionPaths()) {
                    if (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() instanceof Movie) {
                        Movie m = (Movie) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                        movieList.add(m);
                    }
                }
                plugin.setMovieBean(movieList, comboMovieLangugage.getSelectedItem().toString());
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        new PluginDialog(GuiController.getInstance().getMainframe(), plugin, ext);
                    }
                });
            }
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    private class PluginSeriesAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        IPluginSeries plugin;

        public PluginSeriesAction(String text, boolean enabled, IPluginSeries plugin) {
            super(text, null);
            this.enabled = enabled;
            this.plugin = plugin;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    private class PluginMusicAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        IPluginMusic plugin;

        public PluginMusicAction(String text, boolean enabled, IPluginMusic plugin) {
            super(text, null);
            this.enabled = enabled;
            this.plugin = plugin;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    private class PluginPhotoAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        IPluginPhoto plugin;

        public PluginPhotoAction(String text, boolean enabled, IPluginPhoto plugin) {
            super(text, null);
            this.enabled = enabled;
            this.plugin = plugin;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    private class DeleteMovieAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        public DeleteMovieAction(String text, boolean enabled) {
            super(text, null);
            this.enabled = enabled;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            TreePath[] paths = treeMovie.getSelectionPaths();
            for (TreePath path : paths) {
                Object o = path.getLastPathComponent();
            }
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    private class TagMovieAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private static final int WATCHED = 0;

        private static final int LOCKED = 1;

        private int action;

        private boolean status;

        public TagMovieAction(String text, boolean enabled, int action, boolean status) {
            super(text, null);
            this.enabled = enabled;
            this.action = action;
            this.status = status;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            UMCDataAccessInterface dao = DataAccessFactory.getUMCDataSourceAccessor(DataAccessFactory.DB_TYPE_SQLITE, Publisher.getInstance().getParamDBDriverconnect() + Publisher.getInstance().getParamDBName(), Publisher.getInstance().getParamDBDriver(), Publisher.getInstance().getParamDBUser(), Publisher.getInstance().getParamDBPwd());
            if (this.action == WATCHED) {
                List<String> uuids = new ArrayList<String>();
                TreePath[] paths = treeMovie.getSelectionPaths();
                for (TreePath path : paths) {
                    Object o = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                    if (o instanceof Movie) {
                        Movie movie = (Movie) o;
                        if (movie.isWatched() != status) {
                            movie.setWatched(status);
                            uuids.add(movie.getUuid());
                            treeMovie.getModel().valueForPathChanged(path, movie);
                        }
                    } else if (o instanceof MovieGroup) {
                        MovieGroup group = (MovieGroup) o;
                        group.setWatched(status);
                        treeMovie.getModel().valueForPathChanged(path, group);
                        for (Movie child : group.getChilds()) {
                            if (child.isWatched() != status) {
                                child.setWatched(status);
                                uuids.add(child.getUuid());
                            }
                        }
                    }
                }
                dao.setMovieWatchedTag(uuids, status);
            } else if (this.action == LOCKED) {
                List<String> uuids = new ArrayList<String>();
                TreePath[] paths = treeMovie.getSelectionPaths();
                for (TreePath path : paths) {
                    Object o = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                    if (o instanceof Movie) {
                        Movie movie = (Movie) o;
                        if (movie.isLocked() != status) {
                            movie.setLocked(status);
                            uuids.add(movie.getUuid());
                            treeMovie.getModel().valueForPathChanged(path, movie);
                        }
                    } else if (o instanceof MovieGroup) {
                        MovieGroup group = (MovieGroup) o;
                        group.setLocked(status);
                        treeMovie.getModel().valueForPathChanged(path, group);
                        for (Movie child : group.getChilds()) {
                            if (child.isLocked() != status) {
                                child.setLocked(status);
                                uuids.add(child.getUuid());
                            }
                        }
                    }
                }
                dao.setMovieLockedTag(uuids, status);
            }
        }

        public boolean isEnabled() {
            return this.enabled;
        }
    }

    private void filterSeriesTree() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Series");
        for (Object o : seriesList) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode();
            SeriesGroup sg = (SeriesGroup) o;
            node.setUserObject(sg);
            for (Season s : sg.getSeasons().values()) {
                DefaultMutableTreeNode childSeason = new DefaultMutableTreeNode();
                childSeason.setUserObject(s);
                for (Episode e : s.getEpisodes().values()) {
                    DefaultMutableTreeNode childEpisode = new DefaultMutableTreeNode();
                    childEpisode.setUserObject(e);
                    childSeason.add(childEpisode);
                }
                node.add(childSeason);
            }
            rootNode.add(node);
        }
        rootNode.setUserObject("Series [" + Integer.toString(rootNode.getChildCount()) + "]");
        DefaultTreeModel tree = new DefaultTreeModel(rootNode);
        treeSeries.setModel(tree);
    }

    private void treeSeriesMousePressed(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            TreePath[] paths = treeSeries.getSelectionPaths();
            if (paths != null) {
                JPopupMenu treeMenu = new JPopupMenu();
                JMenuItem menuItem = new JMenuItem("Delete");
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        if (JOptionPane.showConfirmDialog(null, UMCLanguage.getText("gui.question.2.text"), UMCLanguage.getText("gui.question.2.title"), JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        }
                    }
                });
                treeMenu.add(menuItem);
                treeMenu.addSeparator();
                JMenu groupListMenu = new JMenu("Tag as");
                menuItem = new JMenuItem("Locked");
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        UMCDataAccessInterface dao = DataAccessFactory.getUMCDataSourceAccessor(DataAccessFactory.DB_TYPE_SQLITE, "jdbc:sqlite:database/umc.db", "org.sqlite.JDBC", "", "");
                    }
                });
                groupListMenu.add(menuItem);
                menuItem = new JMenuItem("Watched");
                menuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent ae) {
                        UMCDataAccessInterface dao = DataAccessFactory.getUMCDataSourceAccessor(DataAccessFactory.DB_TYPE_SQLITE, "jdbc:sqlite:database/umc.db", "org.sqlite.JDBC", "", "");
                    }
                });
                groupListMenu.add(menuItem);
                String[] xxx = { "asdf", "sadf" };
                treeMenu.add(groupListMenu);
                treeMenu.show(treeMovie, evt.getX(), evt.getY());
            }
        }
    }

    /**
     * Füllt den Baum für die Musik mit einträgen aus der Datenbank.
     */
    public void filterMusicTree() {
        try {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Music");
            for (Object o : musicList) {
                if (o instanceof Music && toggleMusic1.isSelected()) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                    node.setUserObject((Music) o);
                    rootNode.add(node);
                } else if (o instanceof MusicAlbum && toggleMusic2.isSelected()) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                    MusicAlbum ma = (MusicAlbum) o;
                    node.setUserObject(ma);
                    for (Music m : ma.getChilds()) {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode();
                        child.setUserObject(m);
                        node.add(child);
                    }
                    rootNode.add(node);
                }
            }
            DefaultTreeModel tree = new DefaultTreeModel(rootNode);
            treeMusic.setModel(tree);
        } catch (Exception exc) {
            log.error("Could not add music to tree", exc);
        }
    }

    private void treeMusicMousePressed(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            int selRow = treeMusic.getRowForLocation(evt.getX(), evt.getY());
            if (selRow != -1) {
                treeMusic.setSelectionRow(selRow);
                JPopupMenu treeMenu = new JPopupMenu();
                JMenu groupListMenu = new JMenu("Tag as");
                JMenuItem menuItem = new JMenuItem("Locked");
                groupListMenu.add(menuItem);
                menuItem = new JMenuItem("Heared");
                groupListMenu.add(menuItem);
                treeMenu.add(groupListMenu);
                treeMenu.show(treeMusic, evt.getX(), evt.getY());
            }
        }
    }

    /**
     * Füllt den Baum für die Photos mit einträgen aus der Datenbank.
     */
    public void filterPhotoTree() {
        try {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Photos");
            for (Object o : photoList) {
                if (o instanceof Photo && togglePhoto1.isSelected()) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                    node.setUserObject((Photo) o);
                    rootNode.add(node);
                } else if (o instanceof PhotoAlbum && togglePhoto2.isSelected()) {
                    DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                    PhotoAlbum ma = (PhotoAlbum) o;
                    node.setUserObject(ma);
                    for (Photo p : ma.getChilds()) {
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode();
                        child.setUserObject(p);
                        node.add(child);
                    }
                    rootNode.add(node);
                }
            }
            DefaultTreeModel tree = new DefaultTreeModel(rootNode);
            treePhoto.setModel(tree);
        } catch (Exception exc) {
            log.error("Could not add photos to tree", exc);
        }
    }

    private void treePhotoMousePressed(java.awt.event.MouseEvent evt) {
    }

    /**
     * Füllt den Baum für die Personen mit einträgen aus der Datenbank.
     */
    public void filterPersonTree() {
        TreePath personPath = null;
        try {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Persons");
            for (Object o : personList) {
                if (o instanceof Actor) {
                    IPerson ip = (Actor) o;
                    if (ip.getName().toLowerCase().indexOf(textFieldFilterPerson.getText().toLowerCase()) != -1) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                        node.setUserObject(ip);
                        rootNode.add(node);
                    }
                } else if (o instanceof Director) {
                    IPerson ip = (Director) o;
                    if (ip.getName().toLowerCase().indexOf(textFieldFilterPerson.getText().toLowerCase()) != -1) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                        node.setUserObject(ip);
                        rootNode.add(node);
                    }
                } else if (o instanceof Author) {
                    IPerson ip = (Author) o;
                    if (ip.getName().toLowerCase().indexOf(textFieldFilterPerson.getText().toLowerCase()) != -1) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                        node.setUserObject(ip);
                        rootNode.add(node);
                    }
                } else if (o instanceof Producer) {
                    IPerson ip = (Producer) o;
                    if (ip.getName().toLowerCase().indexOf(textFieldFilterPerson.getText().toLowerCase()) != -1) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                        node.setUserObject(ip);
                        rootNode.add(node);
                    }
                } else if (o instanceof ProducerOST) {
                    IPerson ip = (ProducerOST) o;
                    if (ip.getName().toLowerCase().indexOf(textFieldFilterPerson.getText().toLowerCase()) != -1) {
                        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                        node.setUserObject(ip);
                        rootNode.add(node);
                    }
                }
            }
            DefaultTreeModel tree = new DefaultTreeModel(rootNode);
            treePerson.setModel(tree);
            if (rootNode.getChildCount() == 1) {
                personPath = new TreePath(rootNode.getChildAt(0));
                treePerson.setSelectionPath(personPath);
                treePerson.scrollPathToVisible(personPath);
            }
        } catch (Exception exc) {
            log.error("Could not add persons to tree", exc);
        }
    }

    private void treePersonsMousePressed(java.awt.event.MouseEvent evt) {
    }

    /**
     * Füllt den Baum für die Genre mit einträgen aus der Datenbank.
     */
    public void filterGenreTree() {
        try {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Genre");
            for (Genre g : genreList) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode();
                node.setUserObject(g);
                rootNode.add(node);
            }
            DefaultTreeModel tree = new DefaultTreeModel(rootNode);
            treeGenre.setModel(tree);
        } catch (Exception exc) {
            log.error("Could not add genre to tree", exc);
        }
    }

    private void treeGenresMousePressed(java.awt.event.MouseEvent evt) {
    }

    private void setMovieTreeEnabled(boolean aFlag) {
        textFieldFilterMovie.setEnabled(aFlag);
        toggleMovieFM.setEnabled(aFlag);
        toggleMovieUG.setEnabled(aFlag);
        comboMovieLangugage.setEnabled(aFlag);
    }

    private void setSeriesTreeEnabled(boolean aFlag) {
        textFieldFilterSeries.setEnabled(aFlag);
        toggleSeries1.setEnabled(aFlag);
        toggleSeries2.setEnabled(aFlag);
        comboSeriesLangugage.setEnabled(aFlag);
    }

    private void setMusicTreeEnabled(boolean aFlag) {
        textFieldFilterMusic.setEnabled(aFlag);
        toggleMusic1.setEnabled(aFlag);
        toggleMusic2.setEnabled(aFlag);
        comboMusicLanguage.setEnabled(aFlag);
    }

    private void setPhotoTreeEnabled(boolean aFlag) {
        textFieldFilterPhoto.setEnabled(aFlag);
        togglePhoto1.setEnabled(aFlag);
        togglePhoto2.setEnabled(aFlag);
    }

    private void setPersonTreeEnabled(boolean aFlag) {
        textFieldFilterPerson.setEnabled(aFlag);
        togglePerson1.setEnabled(aFlag);
        togglePerson2.setEnabled(aFlag);
    }

    private void setGenreTreeEnabled(boolean aFlag) {
        textFieldFilterGenre.setEnabled(aFlag);
        toggleGenre1.setEnabled(aFlag);
        toggleGenre2.setEnabled(aFlag);
    }

    /**
     * Will return the currently active/selected language from the combobox located above the tree.
     * 
     * @return current active/selected language
     */
    public String getActiveLanguage() {
        return comboMovieLangugage.getSelectedItem().toString();
    }

    /**
     * Gibt das SplitPane Element zurück
     * @return
     */
    public JSplitPane getSplitPane() {
        return splitPane;
    }

    /**
     * Gib den Movie-Baum zurück
     * @return
     */
    public JTree getTreeMovie() {
        return treeMovie;
    }

    /**
     * Setzt das gewünschtes gewählte Tab
     * @param selectedIndex
     */
    public void setSelectedTab(int selectedIndex) {
        tabbedPaneManage.setSelectedIndex(selectedIndex);
    }

    /**
     * Selektiert den Personen-Bereich und filtert den Baum nach dem übergebenen Namen.
     * @param person Personenname nach dem gefiltert werden soll
     */
    public void showPersonDetails(String person) {
        tabbedPaneManage.setSelectedIndex(5);
        textFieldFilterPerson.setText(person);
        filterPersonTree();
    }

    private class InitMovieList extends SwingWorker<Collection<Object>, Void> {

        private boolean reload;

        public InitMovieList(boolean reload) {
            setMovieTreeEnabled(false);
            treeMovie.setModel(loadingTreeModel);
            this.reload = reload;
        }

        @Override
        protected Collection<Object> doInBackground() throws Exception {
            Collection<Object> mList = Publisher.getInstance().getSortedMovies(comboMovieLangugage.getSelectedItem().toString(), reload);
            return mList;
        }

        public void done() {
            try {
                movieList = get();
                filterMovieTree();
                setMovieTreeEnabled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class InitSeriesList extends SwingWorker<Collection<Object>, Void> {

        private boolean reload;

        public InitSeriesList(boolean reload) {
            setSeriesTreeEnabled(false);
            treeSeries.setModel(loadingTreeModel);
            this.reload = reload;
        }

        @Override
        protected Collection<Object> doInBackground() throws Exception {
            Collection<Object> seriesList = Publisher.getInstance().getSortedSeries(comboSeriesLangugage.getSelectedItem().toString(), reload);
            return seriesList;
        }

        public void done() {
            try {
                seriesList = get();
                filterSeriesTree();
                setSeriesTreeEnabled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class InitMusicList extends SwingWorker<Collection<Object>, Void> {

        private boolean reload;

        public InitMusicList(boolean reload) {
            setMusicTreeEnabled(false);
            treeMusic.setModel(loadingTreeModel);
            this.reload = reload;
        }

        @Override
        protected Collection<Object> doInBackground() throws Exception {
            Collection<Object> musicList = Publisher.getInstance().getSortedMusic(reload);
            return musicList;
        }

        public void done() {
            try {
                musicList = get();
                filterMusicTree();
                setMusicTreeEnabled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class InitPhotoList extends SwingWorker<Collection<Object>, Void> {

        private boolean reload;

        public InitPhotoList(boolean reload) {
            setPhotoTreeEnabled(false);
            treePhoto.setModel(loadingTreeModel);
            this.reload = reload;
        }

        @Override
        protected Collection<Object> doInBackground() throws Exception {
            Collection<Object> photoList = Publisher.getInstance().getSortedPhotos(reload);
            return photoList;
        }

        public void done() {
            try {
                photoList = get();
                filterPhotoTree();
                setPhotoTreeEnabled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class InitPersonList extends SwingWorker<Collection<IPerson>, Void> {

        private boolean reload;

        public InitPersonList(boolean reload) {
            setPersonTreeEnabled(false);
            treePerson.setModel(loadingTreeModel);
            this.reload = reload;
        }

        @Override
        protected Collection<IPerson> doInBackground() throws Exception {
            UMCDataAccessInterface dao = DataAccessFactory.getUMCDataSourceAccessor(DataAccessFactory.DB_TYPE_SQLITE, Publisher.getInstance().getParamDBDriverconnect() + Publisher.getInstance().getParamDBName(), Publisher.getInstance().getParamDBDriver(), Publisher.getInstance().getParamDBUser(), Publisher.getInstance().getParamDBPwd());
            Collection<IPerson> mList = dao.getAllPersons();
            return mList;
        }

        public void done() {
            try {
                personList = get();
                filterPersonTree();
                setPersonTreeEnabled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private class InitGenreList extends SwingWorker<Collection<Genre>, Void> {

        private boolean reload;

        public InitGenreList(boolean reload) {
            setGenreTreeEnabled(false);
            treeGenre.setModel(loadingTreeModel);
            this.reload = reload;
        }

        @Override
        protected Collection<Genre> doInBackground() throws Exception {
            Collection<Genre> genreList = Publisher.getInstance().getAllGenres(comboGenreLanguage.getSelectedItem().toString());
            return genreList;
        }

        public void done() {
            try {
                genreList = get();
                filterGenreTree();
                setGenreTreeEnabled(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
