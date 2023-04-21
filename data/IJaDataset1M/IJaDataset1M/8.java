package de.beas.explicanto.client.rcp.pageeditor;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import de.bea.services.vidya.client.datasource.VidyaDataTree;
import de.bea.services.vidya.client.datasource.types.WSUser;
import de.bea.services.vidya.client.datastructures.CAttentionComponent;
import de.bea.services.vidya.client.datastructures.CBox;
import de.bea.services.vidya.client.datastructures.CClasifComponent;
import de.bea.services.vidya.client.datastructures.CComponent;
import de.bea.services.vidya.client.datastructures.CCourse;
import de.bea.services.vidya.client.datastructures.CCourseName;
import de.bea.services.vidya.client.datastructures.CCustomer;
import de.bea.services.vidya.client.datastructures.CHotspot;
import de.bea.services.vidya.client.datastructures.CLesson;
import de.bea.services.vidya.client.datastructures.CMediaComponent;
import de.bea.services.vidya.client.datastructures.CPage;
import de.bea.services.vidya.client.datastructures.CProject;
import de.bea.services.vidya.client.datastructures.CQuizComponent;
import de.bea.services.vidya.client.datastructures.CRegion;
import de.bea.services.vidya.client.datastructures.CRevealComponent;
import de.bea.services.vidya.client.datastructures.CStyleSheet;
import de.bea.services.vidya.client.datastructures.CTableComponent;
import de.bea.services.vidya.client.datastructures.CTextComponent;
import de.bea.services.vidya.client.datastructures.CTipComponent;
import de.bea.services.vidya.client.datastructures.CUnit;
import de.bea.services.vidya.client.datastructures.CUnitItem;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.ExplicantoClientPlugin;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.Resources;
import de.beas.explicanto.client.rcp.components.ClasificationEditor;
import de.beas.explicanto.client.rcp.components.QuizEditor;
import de.beas.explicanto.client.rcp.components.RevealEditor;
import de.beas.explicanto.client.rcp.components.html.TableFormatDlg;
import de.beas.explicanto.client.rcp.dialogs.EmailDialog;
import de.beas.explicanto.client.rcp.dialogs.ExplicantoMessageDialog;
import de.beas.explicanto.client.rcp.widgets.SidesPanel;

public class PageEditor extends ApplicationWindow {

    private static final Logger log = Logger.getLogger(PageEditor.class);

    private final CPage page, tmpPage;

    private Composite pageComposite;

    private PageComponent selComp;

    private PageRegion selRegion;

    private final SidesPanel sidesPanel;

    private boolean modified = false;

    private Action saveAction, editFlyAction, moveUpAction, moveDownAction, removeAction, editAction, editSourceAction, editLayoutAction;

    private MailAction mailAction;

    private ExitAction exitAction;

    private CStyleSheet styleSheet;

    public PageEditor(SidesPanel sidesPanel, CPage page, CPage tmpPage) {
        super(sidesPanel.getShell());
        setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.MIN | SWT.PRIMARY_MODAL);
        this.sidesPanel = sidesPanel;
        this.page = page;
        this.tmpPage = tmpPage;
        String css = page.getCourseOfPage().getStyleSheet();
        this.styleSheet = Resources.getStyleSheetElem(css);
        addMenuBar();
        addCoolBar(SWT.NONE);
        sidesPanel.refreshPageList();
    }

    public boolean close() {
        if (isModified()) {
            int result = ExplicantoMessageDialog.openYesNoCancel(getShell(), I18N.translate("pageEditor.exitSave.title"), I18N.translate("pageEditor.exitSave.message"));
            if (result == 0) savePage(); else if (result == 2 || result == -1) return false;
        }
        page.setEditorOpen(false);
        sidesPanel.refreshPageList();
        sidesPanel.editorClosed(page);
        super.close();
        return true;
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        int X = 150;
        int Y = 20;
        shell.setBounds(X, Y, 0, 0);
    }

    protected MenuManager createMenuManager() {
        MenuManager menu = super.createMenuManager();
        MenuManager fileMenu = new MenuManager(I18N.translate("pageEditor.menu.page"));
        menu.add(fileMenu);
        saveAction = new SaveAction();
        fileMenu.add(saveAction);
        exitAction = new ExitAction();
        fileMenu.add(exitAction);
        MenuManager insertMenu = new MenuManager(I18N.translate("pageEditor.menu.insert"));
        menu.add(insertMenu);
        insertMenu.add(new AddTextAction());
        insertMenu.add(new AddAttentionAction());
        insertMenu.add(new AddTipAction());
        insertMenu.add(new AddMediaAction());
        insertMenu.add(new AddQuizAction());
        insertMenu.add(new AddTableAction());
        insertMenu.add(new AddHotspotAction());
        insertMenu.add(new AddRevealAction());
        insertMenu.add(new AddClasifAction());
        MenuManager editMenu = new MenuManager(I18N.translate("pageEditor.menu.edit"));
        menu.add(editMenu);
        moveUpAction = new MoveUpAction();
        editMenu.add(moveUpAction);
        moveDownAction = new MoveDownAction();
        editMenu.add(moveDownAction);
        removeAction = new RemoveAction();
        editMenu.add(removeAction);
        editAction = new EditAction();
        editMenu.add(editAction);
        editSourceAction = new EditSourceAction();
        editMenu.add(editSourceAction);
        editLayoutAction = new EditLayoutAction();
        editMenu.add(editLayoutAction);
        editFlyAction = new EditFlyInAction();
        editMenu.add(editFlyAction);
        MenuManager tableMenu = new MenuManager(I18N.translate("pageEditor.menu.table"));
        menu.add(tableMenu);
        return menu;
    }

    public CStyleSheet getCourseCSS() {
        return styleSheet;
    }

    protected CoolBarManager createCoolBarManager(int style) {
        CoolBarManager cbm = super.createCoolBarManager(style | SWT.FLAT);
        log.debug("adding cool bar contribution");
        ToolBarManager tbm = new ToolBarManager();
        cbm.add(tbm);
        tbm.add(saveAction);
        tbm.add(moveUpAction);
        tbm.add(moveDownAction);
        tbm.add(removeAction);
        mailAction = new MailAction();
        tbm.add(mailAction);
        tbm = new ToolBarManager();
        cbm.add(tbm);
        tbm.add(new TextContribution("text.contribution", tmpPage, this));
        return cbm;
    }

    protected Control createContents(Composite parent) {
        Composite background = new Composite(parent, SWT.NONE);
        FormLayout layout = new FormLayout();
        background.setLayout(layout);
        pageComposite = new Composite(background, SWT.BORDER);
        pageComposite.setBackground(new Color(parent.getDisplay(), 250, 200, 200));
        FormData data = new FormData();
        data.left = new FormAttachment(0, 10);
        data.right = new FormAttachment(100, -10);
        data.top = new FormAttachment(0, 10);
        data.bottom = new FormAttachment(100, -10);
        pageComposite.setLayoutData(data);
        pageComposite.setLayout(null);
        createRegions(pageComposite);
        getShell().setText(I18N.translate("pageEditor.title"));
        return pageComposite;
    }

    private final int leftRegions(CRegion region) {
        Iterator iter = tmpPage.getChildren().iterator();
        int count = 0;
        while (iter.hasNext()) {
            CRegion r = (CRegion) iter.next();
            if (r.getBoundingBox().getLeft() < region.getBoundingBox().getLeft() && r.getBoundingBox().getTop() < region.getBoundingBox().getTop() + region.getBoundingBox().getHeight() && r.getBoundingBox().getTop() + r.getBoundingBox().getHeight() > region.getBoundingBox().getTop()) count++;
        }
        log.debug("leftRegions returning :" + count);
        return count;
    }

    private final int topRegions(CRegion region) {
        Iterator iter = tmpPage.getChildren().iterator();
        int count = 0;
        while (iter.hasNext()) {
            CRegion r = (CRegion) iter.next();
            if (r.getBoundingBox().getTop() < region.getBoundingBox().getTop() && r.getBoundingBox().getLeft() < region.getBoundingBox().getLeft() + region.getBoundingBox().getWidth() && r.getBoundingBox().getLeft() + r.getBoundingBox().getWidth() > region.getBoundingBox().getLeft()) count++;
        }
        log.debug("topRegions returning :" + count);
        return count;
    }

    private void createRegions(Composite pageComp) {
        log.debug("creating regions ...");
        Iterator regions = tmpPage.getChildren().iterator();
        int maxX = 500, maxY = 200;
        final int BAR_WIDTH = 25;
        while (regions.hasNext()) {
            CRegion region = (CRegion) regions.next();
            PageRegion pr = new PageRegion(pageComp, this, region);
            CBox box = region.getBoundingBox();
            log.debug("region:" + box.getLeft() + " " + box.getTop() + " " + box.getWidth() + " " + box.getHeight());
            int dx = BAR_WIDTH * leftRegions(region);
            int dy = BAR_WIDTH * topRegions(region);
            pr.setBounds(dx + box.getLeft(), dy + box.getTop(), box.getWidth() + BAR_WIDTH, box.getHeight() + BAR_WIDTH);
            if (maxX < pr.getBounds().x + pr.getBounds().width) maxX = pr.getBounds().x + pr.getBounds().width;
            if (maxY < pr.getBounds().y + pr.getBounds().height) maxY = pr.getBounds().y + pr.getBounds().height;
            if (selRegion == null) selRegion = pr;
        }
        getShell().setSize(maxX + 50, maxY + 130);
        log.debug("set shell size: " + getShell().getSize());
    }

    private class ExitAction extends Action {

        private ExitAction() {
            super(I18N.translate("pageEditor.menu.page.exit"));
        }

        public void run() {
            log.debug("closing");
            close();
        }
    }

    private final class SaveAction extends Action {

        private SaveAction() {
            super(I18N.translate("pageEditor.menu.page.save"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/save.png"));
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/saveDisabled.png"));
            setEnabled(false);
        }

        public void run() {
            log.debug("saving page");
            savePage();
        }
    }

    protected void savePage() {
        try {
            if (page.getUid() == 0) {
                if (ExplicantoMessageDialog.openConfirm(getShell(), "explicanto", I18N.translate("pageEditor.menu.page.save.confirm"))) {
                    page.takeAttributes(tmpPage);
                    VidyaDataTree.getDefault().storeUnit((CUnit) page.getParent().getParent());
                    tmpPage.setUid(page.getUid());
                } else return;
            }
            {
                page.takeAttributes(tmpPage);
                VidyaDataTree.getDefault().storePage(page);
            }
            page.setLocallyModified(true);
            setModified(false);
        } catch (Exception e) {
            ExplicantoClientPlugin.handleException(e, page);
        }
    }

    private final class EditAction extends Action {

        private EditAction() {
            super(I18N.translate("pageEditor.menu.edit.editComponent"));
        }

        public void run() {
            log.debug("editing component");
            if (selComp != null) selComp.openEditor(getShell());
        }
    }

    private final class AddTextAction extends Action {

        private AddTextAction() {
            super(I18N.translate("pageEditor.menu.insert.textComponent"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/textComp.png"));
        }

        public void run() {
            log.debug("inserting text component");
            setModified(true);
            String html = "<html><head></head><body><p>" + I18N.translate("pageEditor.menu.insert.textComponent") + "</p></body></html>";
            CTextComponent text = new CTextComponent(selRegion.getSize().x, 100, html, null, getPage().getNextUidIncr());
            insertComponent(text);
        }
    }

    private final class AddAttentionAction extends Action {

        private AddAttentionAction() {
            super(I18N.translate("pageEditor.menu.insert.attentionComponent"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/attComp.png"));
        }

        public void run() {
            log.debug("inserting text component");
            setModified(true);
            String html = "<html><head></head><body><p>" + I18N.translate("pageEditor.menu.insert.attentionComponent") + "</p></body></html>";
            CAttentionComponent attention = new CAttentionComponent(selRegion.getSize().x, 100, html, getPage().getNextUidIncr());
            insertComponent(attention);
        }
    }

    private final class AddTipAction extends Action {

        private AddTipAction() {
            super(I18N.translate("pageEditor.menu.insert.tipComponent"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/tipComp.png"));
        }

        public void run() {
            log.debug("inserting text component");
            setModified(true);
            String html = "<html><head></head><body><p>" + I18N.translate("pageEditor.menu.insert.tipComponent") + "</p></body></html>";
            CTipComponent tip = new CTipComponent(selRegion.getSize().x, 100, html, tmpPage.getNextUidIncr());
            insertComponent(tip);
        }
    }

    private final class AddMediaAction extends Action {

        private AddMediaAction() {
            super(I18N.translate("pageEditor.menu.insert.mediaComponent"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/mediaComp.png"));
        }

        public void run() {
            log.debug("inserting text component");
            setModified(true);
            CMediaComponent media = new CMediaComponent(selRegion.getSize().x, 100, getPage().getNextUidIncr(), I18N.translate("pageEditor.menu.insert.mediaComponent"), "");
            insertComponent(media);
        }
    }

    private class MoveUpAction extends Action {

        private MoveUpAction() {
            setText(I18N.translate("pageEditor.menu.edit.moveUp"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/compUp.png"));
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/compUpDisabled.png"));
            setEnabled(false);
        }

        public void run() {
            log.debug("moving component up");
            selRegion.moveUp(selComp);
            setModified(true);
        }
    }

    private class MoveDownAction extends Action {

        private MoveDownAction() {
            setText(I18N.translate("pageEditor.menu.edit.moveDown"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/compDown.png"));
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/compDownDisabled.png"));
            setEnabled(false);
        }

        public void run() {
            log.debug("moving component down");
            selRegion.moveDown(selComp);
            setModified(true);
        }
    }

    private class RemoveAction extends Action {

        private RemoveAction() {
            setText(I18N.translate("pageEditor.menu.edit.remove"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/compDelete.png"));
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/compDeleteDisabled.png"));
            setEnabled(false);
        }

        public void run() {
            log.debug("removing component");
            selRegion.saveCurrentSelectedComponent();
            boolean sure = ExplicantoMessageDialog.openQuestion(getShell(), I18N.translate("pageEditor.menu.edit.remove.confirmation"));
            if (sure) selRegion.removeSelectedComponent();
        }
    }

    private class MailAction extends Action {

        private MailAction() {
            setText(I18N.translate("pageEditor.menu.edit.email"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/mail.png"));
            setDisabledImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/mailDisabled.png"));
            setEnabled(false);
        }

        public void run() {
            log.debug("sending email");
            TreeNode sel = page;
            TreeNode crt = sel;
            List l = new LinkedList();
            while (crt != null) {
                l.add(crt);
                crt = crt.getParent();
            }
            Collections.reverse(l);
            StringBuffer buf = new StringBuffer();
            StringBuffer tabs = new StringBuffer();
            Iterator it = l.iterator();
            while (it.hasNext()) {
                String title = "";
                TreeNode tn = (TreeNode) it.next();
                if (tn instanceof CCustomer) title = ((CCustomer) tn).getCustomerName(); else if (tn instanceof CProject) title = ((CProject) tn).getProjectName(); else if (tn instanceof CCourseName) title = ((CCourseName) tn).getMasterCourse().getCourseTitle(); else if (tn instanceof CCourse) title = ((CCourse) tn).getCourseTitle(); else if (tn instanceof CLesson) title = ((CLesson) tn).getLessonTitle(); else if (tn instanceof CUnit) title = ((CUnit) tn).getUnitTitle(); else if (tn instanceof CUnitItem) continue; else if (tn instanceof CPage) title = ((CPage) tn).getPageTitle();
                buf.append(tabs.toString() + "- ");
                buf.append(title);
                buf.append("\n");
                tabs.append("     ");
            }
            try {
                List users = (List) VidyaDataTree.getDefault().loadSystemProperties().get(1);
                Iterator iter = users.iterator();
                List emails = new LinkedList();
                while (iter.hasNext()) {
                    WSUser user = (WSUser) iter.next();
                    emails.add(user.getEmail());
                }
                EmailDialog ed = new EmailDialog(getShell(), (String[]) emails.toArray(new String[0]), "explicanto", buf.toString());
                ed.open();
                if (ed.isOk()) {
                    VidyaDataTree.getDefault().vws.sendEmail(VidyaDataTree.getDefault().getAuthentification(), ed.getEmail(), ed.getSubject(), ed.getContent());
                    log.debug(ed.getEmail());
                    log.debug(buf.toString());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class EditSourceAction extends Action {

        public EditSourceAction() {
            setText(I18N.translate("pageEditor.menu.edit.source"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/htmlcode.png"));
        }

        public void run() {
            log.debug("editing source");
        }
    }

    private class EditLayoutAction extends Action {

        public EditLayoutAction() {
            setText(I18N.translate("pageEditor.menu.edit.layout"));
        }

        public void run() {
            log.debug("editing layout");
        }
    }

    private class EditFlyInAction extends Action {

        public EditFlyInAction() {
            setText(I18N.translate("pageEditor.menu.edit.flyOut"));
            setEnabled(false);
        }

        public void run() {
            log.debug("editing fly in / out");
            FlyOutEditor foe = new FlyOutEditor(getShell(), selComp.getComponent().getFly());
            foe.open();
            if (foe.isOk()) {
                selComp.getComponent().setFly(foe.getFly());
                setModified(true);
            }
        }
    }

    private class AddQuizAction extends Action {

        private QuizEditor editor;

        private CQuizComponent newQuiz;

        public AddQuizAction() {
            setText(I18N.translate("pageEditor.menu.insert.quiz"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/quizComp.png"));
        }

        public void run() {
            log.debug("editing source");
            newQuiz = new CQuizComponent(selRegion.getSize().x, 100, getPage().getNextUidIncr());
            newQuiz.setQuizDesc("");
            editor = new QuizEditor(getShell(), newQuiz);
            editor.open();
            editor.getShell().addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    editorClosed();
                }
            });
        }

        protected void editorClosed() {
            if (editor.wasNeverSaved()) return;
            selRegion.addComp(newQuiz);
            editor = null;
        }
    }

    private class AddTableAction extends Action {

        public AddTableAction() {
            setText(I18N.translate("pageEditor.menu.insert.table"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/tableComp.png"));
        }

        public void run() {
            log.debug("adding table component");
            TableFormatDlg dlg = new TableFormatDlg(getShell(), VidyaDataTree.getDefault().getRootCustomer().getStyleSheets());
            if (dlg.open() != Dialog.OK) return;
            int cols = dlg.getColsCount();
            int rows = dlg.getRowsCount();
            int type = dlg.getType();
            CStyleSheet css = dlg.getCss();
            CTableComponent table = new CTableComponent(getSelRegion().getRegion().getBoundingBox().getWidth(), 100, rows, cols, type, css.getStyleSheetType(), getPage().getNextUidIncr(), getPage());
            selRegion.addComp(table);
        }
    }

    private class AddHotspotAction extends Action {

        public AddHotspotAction() {
            setText(I18N.translate("pageEditor.menu.insert.hotspot"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/hotspotComp.png"));
        }

        public void run() {
            log.debug("adding hotspot component");
            CHotspot hotspot = new CHotspot(selRegion.getSize().x, 100, getPage().getNextUidIncr());
            hotspot.setName(I18N.translate("components.names.hotspot"));
            selRegion.addComp(hotspot);
        }
    }

    private class AddRevealAction extends Action {

        private RevealEditor editor;

        private CRevealComponent newComp;

        public AddRevealAction() {
            setText(I18N.translate("pageEditor.menu.insert.reveal"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/revealComp.png"));
        }

        public void run() {
            log.debug("adding sequence component");
            newComp = new CRevealComponent(selRegion.getRegion().getBoundingBox().getWidth(), 100, getPage().getNextUidIncr());
            editor = new RevealEditor(getShell(), newComp, null);
            editor.open();
            editor.getShell().addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    editorClosed();
                }
            });
        }

        protected void editorClosed() {
            if (editor.wasNeverSaved()) return;
            selRegion.addComp(newComp);
        }
    }

    private class AddClasifAction extends Action {

        private ClasificationEditor editor;

        private CClasifComponent newComp;

        public AddClasifAction() {
            setText(I18N.translate("pageEditor.menu.insert.clasifComponent"));
            setImageDescriptor(ImageDescriptor.createFromFile(PageEditor.class, "/de/beas/explicanto/client/rcp/resources/images/classifComp.png"));
        }

        public void run() {
            log.debug("adding sequence component");
            newComp = new CClasifComponent(selRegion.getSize().x, 100, getPage().getNextUidIncr());
            newComp.setName(I18N.translate("components.names.clasif"));
            editor = new ClasificationEditor(getShell(), newComp, true, null);
            editor.open();
            editor.getShell().addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    editorClosed();
                }
            });
        }

        protected void editorClosed() {
            if (editor.wasNeverSaved()) return;
            selRegion.addComp(newComp);
            editor = null;
        }
    }

    void setSelectedRegion(PageRegion region) {
        if (selRegion != null) selRegion.setSelected(false);
        selRegion = region;
        selRegion.setSelected(true);
    }

    void setSelectedComponent(PageComponent comp) {
        if (selComp == comp) {
            log.debug("same selected component, aborting...");
            return;
        }
        if (selComp != null) selComp.setSelected(false);
        selComp = comp;
        if (selComp != null) selComp.setSelected(true);
        boolean state = selComp != null;
        editFlyAction.setEnabled(state);
        moveUpAction.setEnabled(state);
        moveDownAction.setEnabled(state);
        removeAction.setEnabled(state);
        mailAction.setEnabled(state);
    }

    protected int getActiveRegionNr() {
        return 0;
    }

    void insertComponent(CComponent comp) {
        if (selRegion == null) selRegion = (PageRegion) pageComposite.getChildren()[0];
        selRegion.addComp(comp);
    }

    public PageComponent getSelectedComp() {
        return selComp;
    }

    protected boolean isModified() {
        return modified;
    }

    protected void setModified(boolean modified) {
        this.modified = modified;
        saveAction.setEnabled(modified);
    }

    public CPage getPage() {
        return tmpPage;
    }

    public PageRegion getSelRegion() {
        return selRegion;
    }
}
