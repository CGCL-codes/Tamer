package com.melloware.jukes.gui.view.editor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ProgressMonitor;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.uif.builder.ToolBarBuilder;
import com.jgoodies.uif.component.ToolBarButton;
import com.jgoodies.uif.util.ResourceUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.melloware.jukes.db.HibernateDao;
import com.melloware.jukes.db.HibernateUtil;
import com.melloware.jukes.db.orm.Artist;
import com.melloware.jukes.db.orm.Disc;
import com.melloware.jukes.exception.InfrastructureException;
import com.melloware.jukes.file.image.ImageFactory;
import com.melloware.jukes.gui.tool.Actions;
import com.melloware.jukes.gui.tool.Resources;
import com.melloware.jukes.gui.view.component.AlbumImage;
import com.melloware.jukes.gui.view.component.ComponentFactory;
import com.melloware.jukes.gui.view.tasks.TimerListener;
import com.melloware.jukes.gui.view.tasks.UpdateTagsTask;
import com.melloware.jukes.gui.view.validation.ArtistValidationModel;
import com.melloware.jukes.gui.view.validation.IconFeedbackPanel;
import com.melloware.jukes.util.MessageUtil;

/**
 * An implementation of {@link Editor} that displays a {@link Artist}.<p>
 *
 * This container uses a <code>FormLayout</code> and the panel building
 * is done with the <code>PanelBuilder</code> class.
 * Columns and rows are specified before the panel is filled with components.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 * AZ - some modifications made 2009
 */
@SuppressWarnings("unchecked")
public final class ArtistEditor extends AbstractEditor {

    private static final Log LOG = LogFactory.getLog(ArtistEditor.class);

    private AlbumImage image1;

    private AlbumImage image2;

    private AlbumImage image3;

    private AlbumImage image4;

    private AlbumImage image5;

    private AlbumImage image6;

    private JComponent artistPanel;

    private JTextArea notesField;

    private JTextComponent nameField;

    private JToolBar headerToolbar;

    /**
     * Constructs a <code>ArtistEditor</code>.
     */
    public ArtistEditor() {
        super(Resources.ARTIST_TREE_ICON);
    }

    /**
     * Gets the domain class associated with this editor.
     */
    public Class getDomainClass() {
        return Artist.class;
    }

    public JToolBar getHeaderToolBar() {
        return headerToolbar;
    }

    /**
     * Builds the content pane.
     */
    public void build() {
        initComponents();
        initComponentAnnotations();
        initEventHandling();
        artistPanel = buildArtistPanel();
        FormLayout layout = new FormLayout("fill:pref:grow", "max(14dlu;pref), 3dlu, p, 4px, p, 4px, p, 4px, p, 4px, p, 4px, p");
        setLayout(layout);
        PanelBuilder builder = new PanelBuilder(layout, this);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.add(buildHintAreaPane(), cc.xy(1, 1));
        builder.addSeparator(Resources.getString("label.artist"), cc.xy(1, 3));
        builder.add(artistPanel, cc.xy(1, 5));
        builder.addSeparator(Resources.getString("label.featured.discs"), cc.xy(1, 7));
        builder.add(buildThumbnailPanel(), cc.xy(1, 9));
        JComponent audit = buildAuditInfoPanel();
        if (this.getSettings().isAuditInfo()) {
            builder.addSeparator(Resources.getString("label.auditinfo"), cc.xy(1, 11));
            builder.add(audit, cc.xy(1, 13));
        }
    }

    /**
     * Commits with the update tags flag on meaning it will write the ID3 tags
     * to all tracks.
     * AZ - commit with update flag as specified in Settings
     */
    public void commit() {
        commit(this.getSettings().isUpdateTags());
    }

    public void delete() {
        super.delete();
        try {
            if (!MessageUtil.confirmDelete(this)) {
                return;
            }
            setBusyCursor(true);
            Artist artist = getArtist();
            HibernateUtil.beginTransaction();
            HibernateDao.delete(artist);
            HibernateUtil.commitTransaction();
            if (this.getSettings().isCopyImagesToDirectory()) {
                for (Iterator iter = artist.getDiscs().iterator(); iter.hasNext(); ) {
                    Disc disc = (Disc) iter.next();
                    final String oldImageName = ImageFactory.standardImageFileName(artist.getName(), disc.getName(), disc.getYear());
                    File oldImageFile = new File(oldImageName);
                    if (oldImageFile.exists()) {
                        if (!oldImageFile.delete()) {
                            LOG.debug("Error deleting file: " + oldImageFile.getAbsolutePath());
                        }
                    }
                }
            }
            getValidationModel().setDirty(false);
            this.getMainModule().refreshSelection(null, Resources.NODE_DELETED);
        } catch (Exception ex) {
            final String errorMessage = ResourceUtils.getString("messages.ErrorDeletingArtist");
            MessageUtil.showError(this, errorMessage);
            LOG.error(errorMessage, ex);
            HibernateUtil.rollbackTransaction();
        } finally {
            setBusyCursor(false);
        }
    }

    public void rollback() {
        super.rollback();
        try {
            setBusyCursor(true);
            Artist artist = getArtist();
            HibernateDao.refresh(artist);
            updateView();
            super.rollback();
        } catch (Exception ex) {
            final String errorMessage = ResourceUtils.getString("messages.ErrorRefreshingArtist");
            MessageUtil.showError(this, errorMessage);
            LOG.error(errorMessage, ex);
        } finally {
            setBusyCursor(false);
        }
    }

    /**
     * Gets the title for the title bar.
     * <p>
     * @return the title to put on the title bar
     */
    protected String getTitleSuffix() {
        return getArtist().getName();
    }

    /**
     * Writes view contents to the underlying model.
     */
    protected void updateModel() {
        Artist artist = getArtist();
        if (!StringUtils.equals(artist.getName(), nameField.getText())) {
            artist.setName(nameField.getText());
        }
        if (!StringUtils.equalsIgnoreCase(artist.getNotes(), notesField.getText())) {
            artist.setNotes(notesField.getText());
        }
    }

    /**
     * Reads view contents from the underlying model.
     */
    protected void updateView() {
        Artist artist = getArtist();
        nameField.setText(artist.getName());
        notesField.setText(artist.getNotes());
        createdDateLabel.setText(DATE_FORMAT.format(artist.getCreatedDate()));
        createdByLabel.setText(artist.getCreatedUser());
        modifiedDateLabel.setText(DATE_FORMAT.format(artist.getModifiedDate()));
        modifiedByLabel.setText(artist.getModifiedUser());
        image1.setDisc(null);
        image2.setDisc(null);
        image3.setDisc(null);
        image4.setDisc(null);
        image5.setDisc(null);
        image6.setDisc(null);
        Random random = new Random();
        Collection discs = this.getDiscCollection();
        HashMap map = new HashMap();
        while ((map.size() < 6) && (map.size() <= (discs.size() - 1))) {
            Integer rand = Integer.valueOf(random.nextInt(discs.size()));
            map.put(rand, rand);
        }
        int count = 0;
        Object[] array = discs.toArray();
        Iterator iterator = map.values().iterator();
        IMAGE_LOOP: while (iterator.hasNext()) {
            Integer element = (Integer) iterator.next();
            Disc disc = (Disc) array[element.intValue()];
            AlbumImage preview = null;
            switch(count) {
                case 0:
                    {
                        preview = image1;
                        break;
                    }
                case 1:
                    {
                        preview = image2;
                        break;
                    }
                case 2:
                    {
                        preview = image3;
                        break;
                    }
                case 3:
                    {
                        preview = image4;
                        break;
                    }
                case 4:
                    {
                        preview = image5;
                        break;
                    }
                case 5:
                    {
                        preview = image6;
                        break;
                    }
                default:
                    {
                        break IMAGE_LOOP;
                    }
            }
            int dimension = this.getSettings().getCoverSizeSmall();
            final String currentCoverUrl;
            if (this.getSettings().isCopyImagesToDirectory()) {
                currentCoverUrl = ImageFactory.standardImageFileName(disc.getArtist().getName(), disc.getName(), disc.getYear());
            } else {
                currentCoverUrl = disc.getCoverUrl();
            }
            preview.setImage(ImageFactory.getScaledImage(currentCoverUrl, dimension, dimension).getImage());
            preview.setDisc(disc);
            count++;
        }
    }

    /**
     * Gets the domain object associated with this editor.
     * <p>
     * @return an Artist instance associated with this editor
     */
    private Artist getArtist() {
        return (Artist) getModel();
    }

    /**
     * Gets the discs for this artist.  If filtering then apply the filter
     * else just return all of the artists discs.
     * <p>
     * @return the Collection containing the discs
     */
    private Collection getDiscCollection() {
        Collection results = null;
        final String filter = this.getSettings().getFilter();
        if (StringUtils.isNotBlank(filter)) {
            final String resource = ResourceUtils.getString("hql.filter.disc");
            final String hql = MessageFormat.format(resource, new Object[] { getArtist().getId(), filter });
            LOG.debug(hql);
            results = HibernateDao.findByQuery(hql);
        } else {
            results = getArtist().getDiscs();
        }
        return results;
    }

    /**
     * Builds the Artist editor panel.
     * <p>
     * @return the panel to edit artist info.
     */
    private JComponent buildArtistPanel() {
        FormLayout layout = new FormLayout("right:max(14dlu;pref), 4dlu, left:min(80dlu;pref):grow, pref, 4dlu, 25px", "p, 4px, p, 7px");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel(Resources.getString("label.name") + ": ", cc.xy(1, 1));
        builder.add(nameField, cc.xyw(3, 1, 3));
        builder.add(ComponentFactory.createTitleCaseButton(nameField), cc.xy(6, 1));
        builder.addLabel(Resources.getString("label.notes") + ": ", cc.xy(1, 3, "left,top"));
        builder.add(notesField, cc.xyw(3, 3, 3));
        return new IconFeedbackPanel(getValidationModel().getValidationResultModel(), builder.getPanel());
    }

    /**
     * Builds the artist album cover thumbnail panel.
     * <p>
     * @return the artist cover thumbnail panel
     */
    private JComponent buildThumbnailPanel() {
        FormLayout layout = new FormLayout("4px, pref, pref, pref, pref, pref, pref, pref, 4px", "p, 7px");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(image1, cc.xyw(2, 1, 1));
        builder.add(image2, cc.xyw(3, 1, 1));
        builder.add(image3, cc.xyw(4, 1, 1));
        builder.add(image4, cc.xyw(5, 1, 1));
        builder.add(image5, cc.xyw(6, 1, 1));
        builder.add(image5, cc.xyw(7, 1, 1));
        builder.add(image6, cc.xyw(8, 1, 1));
        return builder.getPanel();
    }

    private JToolBar buildToolBar() {
        final ToolBarBuilder bar = new ToolBarBuilder("Artist Toolbar");
        ToolBarButton button = null;
        button = (ToolBarButton) ComponentFactory.createToolBarButton(Actions.UNLOCK_ID);
        button.putClientProperty(Resources.EDITOR_COMPONENT, this);
        bar.add(button);
        button = (ToolBarButton) ComponentFactory.createToolBarButton(Actions.COMMIT_ID);
        button.putClientProperty(Resources.EDITOR_COMPONENT, this);
        bar.add(button);
        button = (ToolBarButton) ComponentFactory.createToolBarButton(Actions.ROLLBACK_ID);
        button.putClientProperty(Resources.EDITOR_COMPONENT, this);
        bar.add(button);
        button = (ToolBarButton) ComponentFactory.createToolBarButton(Actions.DELETE_ID);
        button.putClientProperty(Resources.EDITOR_COMPONENT, this);
        bar.add(button);
        return bar.getToolBar();
    }

    /**
     * Commits to the database and if updateTags flag is set to true it updates
     * the ID3 tags as well.
     * <p>
     * @param aUpdateTags true to update tags, false to not update tags
     */
    private void commit(boolean aUpdateTags) {
        super.commit();
        Artist artist = getArtist();
        Artist foundArtist = null;
        String oldArtistName = artist.getName();
        boolean hasErrors = false;
        boolean artistChanged = (!StringUtils.equals(artist.getName(), nameField.getText()));
        boolean notesChanged = (!StringUtils.equalsIgnoreCase(artist.getNotes(), notesField.getText()));
        if (notesChanged) {
            artist.setNotes(notesField.getText());
        }
        if (artistChanged) {
            try {
                setBusyCursor(true);
                final String resource = ResourceUtils.getString("hql.artist.findCaseSensitive");
                final String hql = MessageFormat.format(resource, new Object[] { StringEscapeUtils.escapeSql(nameField.getText()) });
                foundArtist = (Artist) HibernateDao.findUniqueByQuery(hql);
                if (foundArtist == null) {
                    updateModel();
                }
                hasErrors = hasErrors();
                if (hasErrors) {
                    MessageUtil.showError(this, Resources.getString("messages.editorerrors"));
                    LOG.error(Resources.getString("messages.editorerrors"));
                    return;
                }
                HibernateUtil.beginTransaction();
                if (foundArtist != null) {
                    for (Iterator iter = artist.getDiscs().iterator(); iter.hasNext(); ) {
                        Disc disc = (Disc) iter.next();
                        foundArtist.addDisc(disc);
                    }
                    HibernateDao.persist(foundArtist);
                }
                HibernateDao.saveOrUpdate(artist);
                HibernateUtil.commitTransaction();
                if (this.getSettings().isCopyImagesToDirectory()) {
                    for (Iterator iter = artist.getDiscs().iterator(); iter.hasNext(); ) {
                        Disc disc = (Disc) iter.next();
                        final String oldImageName = ImageFactory.standardImageFileName(oldArtistName, disc.getName(), disc.getYear());
                        File oldImageFile = new File(oldImageName);
                        if (oldImageFile.exists()) {
                            final String newImageName = ImageFactory.standardImageFileName(nameField.getText(), disc.getName(), disc.getYear());
                            final File newImageFile = new File(newImageName);
                            oldImageFile.renameTo(newImageFile);
                        }
                    }
                }
                updateView();
            } catch (InfrastructureException ex) {
                HibernateUtil.rollbackTransaction();
                final String errorMessage = ResourceUtils.getString("messages.ArtistNameUnique");
                MessageUtil.showError(this, errorMessage);
                LOG.error(errorMessage);
                HibernateDao.refresh(artist);
                hasErrors = true;
            } catch (Exception ex) {
                HibernateUtil.rollbackTransaction();
                final String errorMessage = ResourceUtils.getString("messages.ErrorUpdatingArtist");
                MessageUtil.showError(this, errorMessage);
                LOG.error(errorMessage, ex);
                HibernateDao.refresh(artist);
                hasErrors = true;
            } finally {
                setBusyCursor(false);
            }
            if (!hasErrors) {
                if (aUpdateTags && artistChanged) {
                    final Artist whichArtist = (foundArtist == null) ? artist : foundArtist;
                    task = new UpdateTagsTask(whichArtist);
                    UIManager.put("ProgressMonitor.progressText", Resources.getString("label.ProgressTitle"));
                    UIManager.put("OptionPane.cancelButtonText", Resources.getString("label.Cancel"));
                    progressMonitor = new ProgressMonitor(getMainFrame(), Resources.getString("messages.updatetracks"), "", 0, (int) task.getLengthOfTask());
                    progressMonitor.setProgress(0);
                    progressMonitor.setMillisToDecideToPopup(10);
                    task.go();
                    timer = new Timer(50, null);
                    timer.addActionListener(new TimerListener(progressMonitor, task, timer));
                    timer.start();
                } else {
                    MessageUtil.showSuccess(this);
                }
                super.commit();
            }
            if (foundArtist == null) {
                this.getMainModule().refreshSelection(artist, Resources.NODE_CHANGED);
            } else {
                this.getMainModule().refreshTree();
            }
        }
    }

    /**
     * Initializes validation annotations.
     */
    private void initComponentAnnotations() {
        ValidationComponentUtils.setInputHint(nameField, Resources.getString("messages.NameIsMandatory"));
        ValidationComponentUtils.setMandatory(nameField, true);
        ValidationComponentUtils.setMessageKey(nameField, "Artist.Name");
        ValidationComponentUtils.setInputHint(notesField, Resources.getString("messages.NotesLength"));
        ValidationComponentUtils.setMessageKey(notesField, "Artist.Notes");
    }

    /**
     *  Creates and configures the UI components;
     */
    private void initComponents() {
        validationModel = new ArtistValidationModel(new Artist());
        headerToolbar = buildToolBar();
        final Dimension dimension = new Dimension(this.getSettings().getCoverSizeSmall(), this.getSettings().getCoverSizeSmall());
        image1 = new AlbumImage(dimension);
        image2 = new AlbumImage(dimension);
        image3 = new AlbumImage(dimension);
        image4 = new AlbumImage(dimension);
        image5 = new AlbumImage(dimension);
        image6 = new AlbumImage(dimension);
        nameField = ComponentFactory.createTextField(getValidationModel().getModel(Artist.PROPERTYNAME_NAME), false);
        notesField = ComponentFactory.createTextArea(getValidationModel().getModel(Artist.PROPERTYNAME_NOTES), false);
        notesField.setLineWrap(true);
        notesField.setWrapStyleWord(true);
        notesField.setRows(3);
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                AlbumImage preview = (AlbumImage) event.getSource();
                if (preview.getDisc() != null) {
                    setBusyCursor(true);
                    getMainModule().selectNodeInTree(preview.getDisc());
                    setBusyCursor(false);
                }
            }
        };
        image1.addActionListener(actionListener);
        image2.addActionListener(actionListener);
        image3.addActionListener(actionListener);
        image4.addActionListener(actionListener);
        image5.addActionListener(actionListener);
        image6.addActionListener(actionListener);
    }
}
