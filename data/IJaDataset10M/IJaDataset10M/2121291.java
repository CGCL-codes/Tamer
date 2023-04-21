package de.spotnik.mail.ui.inbox.editors;

import de.spotnik.mail.application.SWTResourceManager;
import de.spotnik.mail.core.model.SpotnikMessage;
import java.util.logging.Logger;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

/**
 * MessageEditor
 * 
 * @author Jens Rehp�hler
 * @since 04.06.2006
 */
public class SimpleMessageEditor extends AbstractMessageEditor {

    /** eclipse id */
    public static final String ID = "de.spotnik.mail.ui.views.MessageEditor";

    private static final Logger LOG = Logger.getLogger(SimpleMessageEditor.class.getName());

    private static int MARGIN = 5;

    private Combo combo;

    private ImageHyperlink lnkCC;

    private ImageHyperlink lnkTo;

    private StyledText sourceText;

    private FormToolkit toolkit = new FormToolkit(Display.getCurrent());

    /**
     * Creates a new instance of MessageEditor.
     */
    public SimpleMessageEditor() {
        super();
    }

    void addControl(Control control, int offset) {
        StyleRange style = new StyleRange();
        style.start = offset;
        style.length = 1;
        control.pack();
        Rectangle rect = control.getBounds();
        int ascent = 2 * rect.height / 3;
        int descent = rect.height - ascent;
        style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, rect.width + 2 * MARGIN);
    }

    /**
     * Create contents of the editor part
     * 
     * @param parent
     */
    @Override
    public void createPartControl(Composite parent) {
        ToolBar toolBar;
        Composite composite;
        CTabFolder messageTabFolder;
        CTabItem sourceTab;
        TextViewer sourceViewer;
        Browser browser;
        GridData gridData;
        ToolItem btnReply;
        ToolItem btnReplyAll;
        ToolItem btnForward;
        ToolItem btnDelete;
        ToolItem btnSpam;
        ToolItem btnCreateFilter;
        GridLayout gridLayout;
        ComboViewer labelViewer;
        ExpandableComposite expandableComposite;
        Composite composite_1;
        FormText receiversText;
        parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        parent.setLayout(new GridLayout());
        composite = this.toolkit.createComposite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        composite.setBackgroundMode(SWT.INHERIT_FORCE);
        gridLayout = new GridLayout();
        gridLayout.numColumns = 5;
        composite.setLayout(gridLayout);
        this.toolkit.paintBordersFor(composite);
        toolBar = new ToolBar(composite, SWT.FLAT);
        toolBar.setBackgroundMode(SWT.INHERIT_FORCE);
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
        gridData.horizontalIndent = 5;
        toolBar.setLayoutData(gridData);
        this.toolkit.adapt(toolBar, true, true);
        btnReply = new ToolItem(toolBar, SWT.PUSH);
        btnReply.setImage(SWTResourceManager.getImage(SimpleMessageEditor.class, "/icons/reply_small.png"));
        btnReply.setToolTipText(MessageEditorMessages.getString("MessageEditor.btnReply.toolTipText"));
        btnReplyAll = new ToolItem(toolBar, SWT.PUSH);
        btnReplyAll.setImage(SWTResourceManager.getImage(SimpleMessageEditor.class, "/icons/reply_all_small.png"));
        btnReplyAll.setToolTipText(MessageEditorMessages.getString("MessageEditor.btnReplyAll.toolTipText"));
        btnForward = new ToolItem(toolBar, SWT.PUSH);
        btnForward.setImage(SWTResourceManager.getImage(SimpleMessageEditor.class, "/icons/forward_small.png"));
        btnForward.setToolTipText(MessageEditorMessages.getString("MessageEditor.btnForward.toolTipText"));
        new ToolItem(toolBar, SWT.SEPARATOR);
        btnDelete = new ToolItem(toolBar, SWT.PUSH);
        btnDelete.setImage(SWTResourceManager.getImage(SimpleMessageEditor.class, "/icons/delete_small.png"));
        btnDelete.setToolTipText(MessageEditorMessages.getString("MessageEditor.btnDelete.toolTipText"));
        btnSpam = new ToolItem(toolBar, SWT.CHECK);
        btnSpam.setHotImage(SWTResourceManager.getImage(SimpleMessageEditor.class, "/icons/virus-detected.png"));
        btnSpam.setImage(SWTResourceManager.getImage(SimpleMessageEditor.class, "/icons/virus-detected-2.png"));
        btnSpam.setToolTipText(MessageEditorMessages.getString("MessageEditor.btnSpam.toolTipText"));
        new ToolItem(toolBar, SWT.SEPARATOR);
        btnCreateFilter = new ToolItem(toolBar, SWT.PUSH);
        btnCreateFilter.setImage(SWTResourceManager.getImage(SimpleMessageEditor.class, "/icons/kview.png"));
        btnCreateFilter.setToolTipText(MessageEditorMessages.getString("MessageEditor.btnCreateFilter.toolTipText"));
        this.toolkit.createLabel(composite, MessageEditorMessages.getString("MessageEditor.label.text"), SWT.NONE);
        labelViewer = new ComboViewer(composite, SWT.READ_ONLY);
        labelViewer.setLabelProvider(new ListLabelProvider());
        labelViewer.setContentProvider(new ContentProvider_1());
        labelViewer.setInput(new Object());
        this.combo = labelViewer.getCombo();
        this.toolkit.adapt(this.combo, true, true);
        this.combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        new Label(composite, SWT.NONE);
        expandableComposite = this.toolkit.createExpandableComposite(composite, ExpandableComposite.COMPACT | ExpandableComposite.TWISTIE | ExpandableComposite.NO_TITLE | ExpandableComposite.FOCUS_TITLE);
        gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
        gridData.widthHint = 295;
        expandableComposite.setLayoutData(gridData);
        expandableComposite.setText("Empf�nger");
        this.toolkit.paintBordersFor(expandableComposite);
        composite_1 = this.toolkit.createComposite(expandableComposite, SWT.NONE);
        this.toolkit.paintBordersFor(composite_1);
        expandableComposite.setClient(composite_1);
        receiversText = this.toolkit.createFormText(composite_1, false);
        this.lnkTo = this.toolkit.createImageHyperlink(receiversText, SWT.NONE);
        this.lnkTo.setBounds(0, 0, 130, 24);
        this.lnkTo.setImage(SWTResourceManager.getImage(MessageEditor.class, "/icons/abcard-item.png"));
        this.lnkTo.setToolTipText("to: dummy@spotnik.de");
        this.lnkTo.setText("Dummy");
        this.lnkCC = this.toolkit.createImageHyperlink(receiversText, SWT.NONE);
        this.lnkCC.setBounds(0, 0, 97, 24);
        this.lnkCC.setImage(SWTResourceManager.getImage(MessageEditor.class, "/icons/ablist-item.png"));
        this.lnkCC.setText("Jack Black");
        receiversText.setControl("to", this.lnkTo);
        receiversText.setControl("cc", this.lnkCC);
        receiversText.setText("<form><p><control href=\"to\" /> <control href=\"cc\" /></p></form>", true, true);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);
        messageTabFolder = new CTabFolder(parent, SWT.BOTTOM | SWT.FLAT);
        gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        messageTabFolder.setLayoutData(gridData);
        messageTabFolder.setSelectionBackground(new Color[] { Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND), Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT), Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND) }, new int[] { 50, 100 });
        messageTabFolder.setBorderVisible(true);
        messageTabFolder.setBackgroundMode(SWT.INHERIT_FORCE);
        this.toolkit.adapt(messageTabFolder, true, true);
        CTabItem emailTab = new CTabItem(messageTabFolder, SWT.NONE);
        emailTab.setText(MessageEditorMessages.getString("MessageEditor.emailTab.text"));
        Composite emailComposite = new Composite(messageTabFolder, SWT.NONE);
        emailComposite.setLayout(new FillLayout());
        this.toolkit.adapt(emailComposite);
        emailTab.setControl(emailComposite);
        browser = new Browser(emailComposite, SWT.NONE);
        this.toolkit.adapt(browser, true, true);
        browser.setUrl("file:///D:/spotnik.workspace/de.spotnik.mail.core/src/main/resources/message.html");
        sourceTab = new CTabItem(messageTabFolder, SWT.NONE);
        sourceTab.setText(MessageEditorMessages.getString("MessageEditor.sourceTab.text"));
        Composite sourceComposite = new Composite(messageTabFolder, SWT.NONE);
        sourceComposite.setLayout(new FillLayout());
        this.toolkit.adapt(sourceComposite);
        sourceTab.setControl(sourceComposite);
        sourceViewer = new TextViewer(sourceComposite, SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL);
        sourceViewer.setEditable(false);
        this.sourceText = sourceViewer.getTextWidget();
        this.toolkit.adapt(this.sourceText, true, true);
        sourceViewer.setInput(new Object());
    }

    class ContentProvider_1 implements IStructuredContentProvider {

        public void dispose() {
        }

        @SuppressWarnings("unused")
        public Object[] getElements(Object inputElement) {
            return new Object[] { "item_0", "item_1", "item_2" };
        }

        @SuppressWarnings("unused")
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    class ListLabelProvider extends LabelProvider {

        @Override
        @SuppressWarnings("unused")
        public Image getImage(Object element) {
            return null;
        }

        @Override
        public String getText(Object element) {
            return element.toString();
        }
    }

    @Override
    public void initMessage(SpotnikMessage rawMessage) {
    }
}
