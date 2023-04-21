package org.wikipediacleaner.gui.swing.component;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.text.BadLocationException;
import org.wikipediacleaner.api.constants.EnumWikipedia;
import org.wikipediacleaner.api.data.DataManager;
import org.wikipediacleaner.api.data.Namespace;
import org.wikipediacleaner.api.data.Page;
import org.wikipediacleaner.api.data.PageAnalysis;
import org.wikipediacleaner.api.data.PageElement;
import org.wikipediacleaner.api.data.PageElementCategory;
import org.wikipediacleaner.api.data.PageElementComment;
import org.wikipediacleaner.api.data.PageElementExternalLink;
import org.wikipediacleaner.api.data.PageElementImage;
import org.wikipediacleaner.api.data.PageElementInternalLink;
import org.wikipediacleaner.api.data.PageElementInterwikiLink;
import org.wikipediacleaner.api.data.PageElementLanguageLink;
import org.wikipediacleaner.api.data.PageElementTemplate;
import org.wikipediacleaner.gui.swing.basic.BasicWindow;
import org.wikipediacleaner.i18n.GT;

/**
 * A popup menu listener for MediaWikiPane. 
 */
public abstract class MWPanePopupListener implements MouseListener, KeyListener {

    private final EnumWikipedia wikipedia;

    private BasicWindow window;

    public MWPanePopupListener(EnumWikipedia wikipedia, BasicWindow window) {
        this.wikipedia = wikipedia;
        this.window = window;
    }

    /**
   * @return Wikipedia.
   */
    public EnumWikipedia getWikipedia() {
        return wikipedia;
    }

    /**
   * @return Window.
   */
    public BasicWindow getWindow() {
        return window;
    }

    /**
   * Construct and show popup menu if necessary.
   * 
   * @param e Event.
   */
    private void maybeShowPopup(MouseEvent e) {
        if (!e.isPopupTrigger()) {
            return;
        }
        if (!(e.getComponent() instanceof MWPane)) {
            return;
        }
        MWPane textPane = (MWPane) e.getComponent();
        showPopup(textPane, textPane.viewToModel(e.getPoint()), e.getX(), e.getY());
    }

    /**
   * Construct and show popup menu if necessary.
   * 
   * @param e Event.
   */
    private void maybeShowPopup(KeyEvent e) {
        if (e.getKeyCode() != KeyEvent.VK_CONTEXT_MENU) {
            return;
        }
        if (!(e.getComponent() instanceof MWPane)) {
            return;
        }
        MWPane textPane = (MWPane) e.getComponent();
        try {
            Rectangle rect = textPane.modelToView(textPane.getCaretPosition());
            showPopup(textPane, textPane.getSelectionStart(), rect.x, rect.y);
        } catch (BadLocationException e1) {
        }
    }

    /**
   * Construct and show popup menu.
   * 
   * @param textPane Text pane.
   * @param position Position in text.
   * @param x Position.
   * @param y Position.
   */
    private void showPopup(MWPane textPane, int position, int x, int y) {
        if (textPane == null) {
            return;
        }
        Page originalPage = textPane.getWikiPage();
        PageAnalysis pageAnalysis = new PageAnalysis(originalPage, textPane.getText());
        JPopupMenu popup = createPopup(textPane, position, pageAnalysis);
        if (popup == null) {
            popup = createDefaultPopup(textPane, position, pageAnalysis);
        }
        if (popup != null) {
            popup.show(textPane, x, y);
        }
    }

    /**
   * Construct popup menu.
   * 
   * @param textPane Text pane.
   * @param position Position in text.
   * @param pageAnalysis Page analysis.
   */
    protected abstract JPopupMenu createPopup(MWPane textPane, int position, PageAnalysis pageAnalysis);

    /**
   * Create a default popup menu.
   * 
   * @param textPane Text pane.
   * @param position Position in the text.
   * @param pageAnalysis Page analysis.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopup(MWPane textPane, int position, PageAnalysis pageAnalysis) {
        if (pageAnalysis == null) {
            return null;
        }
        PageElement element = pageAnalysis.isInElement(position);
        if (element instanceof PageElementComment) {
            return null;
        }
        if (element instanceof PageElementInternalLink) {
            PageElementInternalLink internalLink = (PageElementInternalLink) element;
            return createDefaultPopupInternalLink(textPane, position, pageAnalysis, internalLink);
        }
        if (element instanceof PageElementImage) {
            PageElementImage image = (PageElementImage) element;
            return createDefaultPopupImage(textPane, position, pageAnalysis, image);
        }
        if (element instanceof PageElementExternalLink) {
            PageElementExternalLink externalLink = (PageElementExternalLink) element;
            return createDefaultPopupExternalLink(pageAnalysis, position, externalLink);
        }
        if (element instanceof PageElementTemplate) {
            PageElementTemplate template = (PageElementTemplate) element;
            return createDefaultPopupTemplate(pageAnalysis, position, template);
        }
        if (element instanceof PageElementCategory) {
            PageElementCategory category = (PageElementCategory) element;
            return createDefaultPopupCategory(textPane, position, pageAnalysis, category);
        }
        if (element instanceof PageElementInterwikiLink) {
            PageElementInterwikiLink interwiki = (PageElementInterwikiLink) element;
            return createDefaultPopupInterwikiLink(textPane, position, pageAnalysis, interwiki);
        }
        if (element instanceof PageElementLanguageLink) {
            PageElementLanguageLink language = (PageElementLanguageLink) element;
            return createDefaultPopupLanguageLink(textPane, position, pageAnalysis, language);
        }
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(GT._("Page: {0}", limitTextLength(pageAnalysis.getPage().getTitle(), 50)));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        popup.add(new JSeparator());
        MenuCreator.addViewToMenu(wikipedia, popup, pageAnalysis.getPage(), false);
        return popup;
    }

    /**
   * Create a default popup menu for an internal link.
   * 
   * @param textPane Text pane.
   * @param position Position in the text.
   * @param pageAnalysis Page analysis.
   * @param link Internal link.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopupInternalLink(MWPane textPane, int position, PageAnalysis pageAnalysis, PageElementInternalLink link) {
        if (link == null) {
            return null;
        }
        Page page = DataManager.getPage(wikipedia, link.getLink(), null, null);
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(GT._("Page: {0}", limitTextLength(page.getTitle(), 50)));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        popup.add(new JSeparator());
        MenuCreator.addViewToMenu(wikipedia, popup, page, false);
        MenuCreator.addAnalyzeToMenu(wikipedia, popup, page);
        MenuCreator.addDisambiguationToMenu(wikipedia, popup, page);
        popup.add(new JSeparator());
        MenuCreator.addRemoveLinkToMenu(popup, link.getDisplayedText(), textPane, link.getBeginIndex(), link.getEndIndex());
        return popup;
    }

    /**
   * Create a default popup menu for an interwiki link.
   * 
   * @param textPane Text pane.
   * @param position Position in the text.
   * @param pageAnalysis Page analysis.
   * @param link Interwiki link.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopupInterwikiLink(MWPane textPane, int position, PageAnalysis pageAnalysis, PageElementInterwikiLink link) {
        if (link == null) {
            return null;
        }
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(GT._("Interwiki: {0}", limitTextLength(link.getInterwiki() + ":" + link.getLink(), 50)));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        return popup;
    }

    /**
   * Create a default popup menu for a language link.
   * 
   * @param textPane Text pane.
   * @param position Position in the text.
   * @param pageAnalysis Page analysis.
   * @param link Language link.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopupLanguageLink(MWPane textPane, int position, PageAnalysis pageAnalysis, PageElementLanguageLink link) {
        if (link == null) {
            return null;
        }
        EnumWikipedia otherWikipedia = EnumWikipedia.getWikipedia(link.getLanguage());
        Page otherPage = null;
        if (otherWikipedia != null) {
            otherPage = DataManager.getPage(otherWikipedia, link.getLink(), null, null);
        }
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(link.getLanguage() + ":" + limitTextLength(link.getLink(), 50));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        if (otherWikipedia != null) {
            popup.add(new JSeparator());
            MenuCreator.addViewToMenu(otherWikipedia, popup, otherPage, false);
        }
        return popup;
    }

    /**
   * Create a default popup menu for an image.
   * 
   * @param textPane Text pane.
   * @param position Position in the text.
   * @param pageAnalysis Page analysis.
   * @param image Image.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopupImage(MWPane textPane, int position, PageAnalysis pageAnalysis, PageElementImage image) {
        if (image == null) {
            return null;
        }
        String fullName = Namespace.getTitle(Namespace.IMAGE, wikipedia.getNamespaces(), image.getImage());
        Page page = DataManager.getPage(wikipedia, fullName, null, null);
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(GT._("Image: {0}", limitTextLength(image.getImage(), 50)));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        popup.add(new JSeparator());
        MenuCreator.addViewToMenu(wikipedia, popup, page, false);
        MenuCreator.addAnalyzeToMenu(wikipedia, popup, page);
        popup.add(new JSeparator());
        return popup;
    }

    /**
   * Create a default popup menu for a category.
   * 
   * @param textPane Text pane.
   * @param position Position in the text.
   * @param pageAnalysis Page analysis.
   * @param category Category.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopupCategory(MWPane textPane, int position, PageAnalysis pageAnalysis, PageElementCategory category) {
        if (category == null) {
            return null;
        }
        String fullName = Namespace.getTitle(Namespace.CATEGORY, wikipedia.getNamespaces(), category.getName());
        Page page = DataManager.getPage(wikipedia, fullName, null, null);
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(GT._("Category: {0}", limitTextLength(category.getName(), 50)));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        popup.add(new JSeparator());
        MenuCreator.addViewToMenu(wikipedia, popup, page, false);
        MenuCreator.addAnalyzeToMenu(wikipedia, popup, page);
        popup.add(new JSeparator());
        return popup;
    }

    /**
   * Create a default popup menu for an external link.
   * 
   * @param pageAnalysis Page analysis.
   * @param position Position in the text.
   * @param link External link.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopupExternalLink(PageAnalysis pageAnalysis, int position, PageElementExternalLink link) {
        if (link == null) {
            return null;
        }
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(GT._("External link: {0}", limitTextLength(link.getLink(), 50)));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        popup.add(new JSeparator());
        MenuCreator.addViewToMenu(null, popup, link.getLink());
        return popup;
    }

    /**
   * Create a default popup menu for a template.
   * 
   * @param pageAnalysis Page analysis.
   * @param position Position in the text.
   * @param template Template.
   * @return Popup menu.
   */
    protected JPopupMenu createDefaultPopupTemplate(PageAnalysis pageAnalysis, int position, PageElementTemplate template) {
        if (template == null) {
            return null;
        }
        String fullName = Namespace.getTitle(Namespace.TEMPLATE, wikipedia.getNamespaces(), template.getTemplateName());
        Page page = DataManager.getPage(wikipedia, fullName, null, null);
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem(GT._("Template: {0}", limitTextLength(template.getTemplateName(), 50)));
        menuItem.setEnabled(false);
        popup.add(menuItem);
        MenuCreator.addCurrentChapterToMenu(popup, position, pageAnalysis);
        popup.add(new JSeparator());
        MenuCreator.addViewToMenu(wikipedia, popup, page, false);
        MenuCreator.addAnalyzeToMenu(wikipedia, popup, page);
        return popup;
    }

    /**
   * Limit text length to be used in menus.
   * 
   * @param text Original text.
   * @param maxLength Maximum length.
   * @return Text with length restricted to maximum length.
   */
    protected String limitTextLength(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        if (maxLength < 10) {
            maxLength = 10;
        }
        if (text.length() <= maxLength) {
            return text;
        }
        int middle = (maxLength - 3) / 2;
        text = text.substring(0, maxLength - middle) + "..." + text.substring(text.length() - middle);
        return text;
    }

    public void mouseClicked(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseEntered(@SuppressWarnings("unused") MouseEvent e) {
    }

    public void mouseExited(@SuppressWarnings("unused") MouseEvent e) {
    }

    public void keyPressed(@SuppressWarnings("unused") KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        maybeShowPopup(e);
    }

    public void keyTyped(@SuppressWarnings("unused") KeyEvent e) {
    }
}
