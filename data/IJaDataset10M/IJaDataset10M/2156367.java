package com._pmz0178.blogtxt.swing.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.log4j.Logger;
import com._pmz0178.blogtxt.entity.BaseEntity;
import com._pmz0178.blogtxt.entity.BlogCategoryEntity;
import com._pmz0178.blogtxt.entity.BlogEntity;
import com._pmz0178.blogtxt.service.api.ExportService;
import com._pmz0178.blogtxt.swing.mediator.api.IBlogSelection;
import com._pmz0178.blogtxt.swing.util.MessageUtil;

public class TreePopupMenu extends JPopupMenu implements PopupMenuListener, ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(TreePopupMenu.class);

    private IBlogSelection blogSelection;

    JMenuItem exportItem = new JMenuItem("Export...");

    private ExportService exportService;

    public void createUI() {
        exportItem.addActionListener(this);
        add(exportItem);
        addPopupMenuListener(this);
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (blogSelection.getSelectedEntity() == null) {
            exportItem.setEnabled(false);
        } else {
            exportItem.setEnabled(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        FileFilter[] ff = fc.getChoosableFileFilters();
        for (int i = 0; i < ff.length; i++) {
            fc.removeChoosableFileFilter(ff[i]);
        }
        if (blogSelection.getSelectedEntity() instanceof BlogEntity) {
            fc.addChoosableFileFilter(new FileNameExtensionFilter("html", ExportService.FORMAT_HTML));
        }
        fc.addChoosableFileFilter(new FileNameExtensionFilter("blog-txt xml", ExportService.FORMAT_XML));
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FileNameExtensionFilter filter = (FileNameExtensionFilter) fc.getFileFilter();
            export(blogSelection.getSelectedEntity(), fc.getSelectedFile(), filter.getExtensions()[0]);
        }
    }

    private void export(final BaseEntity entityParam, final File fileParam, String formatParam) {
        File realFile = null;
        if (fileParam.getAbsolutePath().endsWith("." + formatParam)) {
            realFile = fileParam;
        } else {
            realFile = new File(fileParam.getAbsolutePath() + "." + formatParam);
        }
        if (realFile.exists() && MessageUtil.showOKCancelDialog("File " + realFile.getName() + " exists, rewrite?", "file exists") != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            if (entityParam instanceof BlogEntity) {
                exportService.exportBlog((BlogEntity) entityParam, realFile, formatParam);
            } else {
                exportService.exportCategory((BlogCategoryEntity) entityParam, realFile);
            }
        } catch (Exception e) {
            logger.error("while saving", e);
            MessageUtil.showErrorDialog(e);
        }
    }

    public void setBlogSelection(IBlogSelection blogSelection) {
        this.blogSelection = blogSelection;
    }

    public void setExportService(ExportService exportService) {
        this.exportService = exportService;
    }
}
