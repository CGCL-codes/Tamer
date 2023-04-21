package net.sf.poormans.gui.listener;

import java.io.File;
import net.sf.poormans.configuration.InitializationManager;
import net.sf.poormans.gui.BrowserManager;
import net.sf.poormans.gui.WorkspaceToolBarManager;
import net.sf.poormans.gui.dialog.DialogManager;
import net.sf.poormans.gui.treeview.TreeViewManager;
import net.sf.poormans.livecycle.GuiObjectHolder;
import net.sf.poormans.model.domain.IPersistentPojo;
import net.sf.poormans.model.domain.IRenderable;
import net.sf.poormans.model.domain.InstanceUtil;
import net.sf.poormans.model.domain.PojoInfo;
import net.sf.poormans.model.domain.PojoPathInfo;
import net.sf.poormans.model.domain.pojo.Content;
import net.sf.poormans.model.domain.pojo.Gallery;
import net.sf.poormans.model.domain.pojo.Image;
import net.sf.poormans.model.domain.pojo.Level;
import net.sf.poormans.model.domain.pojo.Page;
import net.sf.poormans.model.domain.pojo.Site;
import net.sf.poormans.model.persistence.dao.IMaintenanceDAO;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

/**
 * Listener for editing the properties of {@link IPersistentPojo} except for {@link Content}. Object specific checks or
 * tasks to trigger are respected.
 * 
 * @version $Id: ListenerEditSiteOrLevelOrPageOrGalleryOrImageProperties.java 1598 2009-04-18 15:44:41Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class ListenerEditSiteOrLevelOrPageOrGalleryOrImageProperties implements SelectionListener {

    private static Logger logger = Logger.getLogger(ListenerEditSiteOrLevelOrPageOrGalleryOrImageProperties.class);

    private IPersistentPojo persistentPojo;

    public ListenerEditSiteOrLevelOrPageOrGalleryOrImageProperties(final IPersistentPojo persistentPojo) {
        this.persistentPojo = persistentPojo;
    }

    public void widgetSelected(SelectionEvent e) {
        logger.debug("SEL edit");
        if (persistentPojo == null) return;
        IMaintenanceDAO dao = (IMaintenanceDAO) InitializationManager.getBean("mainDAO");
        final Shell shell = e.display.getActiveShell();
        if (InstanceUtil.isSite(persistentPojo)) {
            Site site = (Site) persistentPojo;
            String oldSiteUrl = site.getUrl();
            if (DialogManager.startDialogPersitentPojo(shell, site)) {
                if (!StringUtils.equals(site.getUrl(), oldSiteUrl)) {
                    File dir = new File(PojoPathInfo.getSitePath(site));
                    File oldDir = new File(PojoPathInfo.getSitePath(oldSiteUrl));
                    oldDir.renameTo(dir);
                }
                dao.save(site);
                WorkspaceToolBarManager.actionAfterSiteRenamed(site);
                logger.debug("Site is saved!");
            }
        } else if (InstanceUtil.isLevel(persistentPojo)) {
            Level level = (Level) persistentPojo;
            if (DialogManager.startDialogPersitentPojo(shell, level)) {
                dao.save(level);
                actionAfterChangedProperties(level);
                logger.debug("Level is saved!");
            }
        } else if (InstanceUtil.isGallery(persistentPojo)) {
            Gallery gallery = (Gallery) persistentPojo;
            String oldGalleryName = gallery.getName();
            if (DialogManager.startDialogPersitentPojo(shell, gallery)) {
                if (!oldGalleryName.equals(gallery.getName())) {
                    Site site = PojoInfo.getSite(gallery);
                    File gallerySrc = new File(PojoPathInfo.getSiteImageGalleryFullPath(site, oldGalleryName));
                    File galleryDest = new File(PojoPathInfo.getSiteImageGalleryFullPath(site, gallery.getName()));
                    gallerySrc.renameTo(galleryDest);
                    File cacheSrc = new File(PojoPathInfo.getSiteImageCachePath(site) + oldGalleryName);
                    if (cacheSrc.exists()) {
                        File cacheDest = new File(PojoPathInfo.getSiteImageCachePath(site) + gallery.getName());
                        cacheSrc.renameTo(cacheDest);
                    }
                    logger.debug("Gallery got a new name, dependent directories are renamed!");
                }
                dao.save(gallery);
                actionAfterChangedProperties(gallery);
                logger.debug("Gallery is saved!");
            }
        } else if (InstanceUtil.isPage(persistentPojo)) {
            Page page = (Page) persistentPojo;
            if (DialogManager.startDialogPersitentPojo(shell, page)) {
                dao.save(page);
                actionAfterChangedProperties(page);
                logger.debug("Page is saved!");
            }
        } else if (InstanceUtil.isImage(persistentPojo)) {
            Image image = (Image) persistentPojo;
            if (DialogManager.startDialogPersitentPojo(shell, image)) {
                dao.save(image);
                actionAfterChangedProperties(image);
                logger.debug("Image is saved!");
            }
        } else logger.debug("Unknown object to edit.");
    }

    public void widgetDefaultSelected(SelectionEvent e) {
    }

    private void actionAfterChangedProperties(final IPersistentPojo persistentPojo) {
        GuiObjectHolder.getInstance().putPersistentPojo(persistentPojo);
        TreeViewManager.fillAndExpands(persistentPojo);
        if (InstanceUtil.isRenderable(persistentPojo)) BrowserManager.setPreview((IRenderable) persistentPojo);
    }
}
