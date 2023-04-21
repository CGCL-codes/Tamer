package es.ua.dlsi.tradubi.main.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;
import es.ua.dlsi.tradubi.main.client.TradubiMain;
import es.ua.dlsi.tradubi.main.client.apppanels.EditSubtitleTranslationPanel;
import es.ua.dlsi.tradubi.main.client.entities.SubtitleContentEntity;
import es.ua.dlsi.tradubi.main.client.i18n.I18nConstants;
import es.ua.dlsi.tradubi.main.client.i18n.I18nConstantsFactory;
import es.ua.dlsi.tradubi.main.client.misc.GenericCallback;
import es.ua.dlsi.tradubi.main.client.services.DictionaryService;
import es.ua.dlsi.tradubi.main.client.services.DictionaryServiceAsync;

/**
 * 
 * @author jft3
 * 
 */
public class EditableSubContentArea extends Composite implements HasValue<String> {

    private static EditableSubContentAreaUiBinder uiBinder = GWT.create(EditableSubContentAreaUiBinder.class);

    interface EditableSubContentAreaUiBinder extends UiBinder<Widget, EditableSubContentArea> {
    }

    /**
	 * Callback class. Processes server response after requesting lock the
	 * sequence.
	 * 
	 * @author jft3
	 * 
	 */
    class LockSequenceCallback extends GenericCallback implements AsyncCallback<SubtitleContentEntity> {

        @Override
        public void onFailure(Throwable caught) {
            notificationArea.hideContent();
            super.onFailure(caught, notificationArea);
            editArea.setText(editLabel.getText());
            switchToLabel();
        }

        @Override
        public void onSuccess(SubtitleContentEntity result) {
            if (result.getId() == -1) {
                editArea.setText(editLabel.getText());
                switchToLockedLabel(result.getUserLocks().getUsername());
            } else {
                setDraft(result.isDraft());
                setValue(result.getText());
                user.setText(result.getUser().getUsername());
                switchToEdit();
            }
        }
    }

    /**
	 * Callback class. Processes server response after requesting unlock the
	 * sequence.
	 * 
	 * @author jft3
	 * 
	 */
    public class UnlockSequenceCallback extends GenericCallback implements AsyncCallback<Void> {

        @Override
        public void onFailure(Throwable caught) {
        }

        @Override
        public void onSuccess(Void result) {
        }
    }

    /**
	 * {@link es.ua.dlsi.tradubi.main.client.services.DictionaryService} proxy
	 * 
	 * @uml.property name="dictService"
	 * @uml.associationEnd
	 */
    DictionaryServiceAsync dictService = DictionaryService.Util.getInstance(TradubiMain.getInstance().getUser().getToken());

    /**
	 * i18n constants
	 * 
	 * @uml.property name="constants"
	 * @uml.associationEnd
	 */
    private final I18nConstants constants = I18nConstantsFactory.getI18nConstants();

    protected final NotificationText notificationArea = TradubiMain.getInstance().getNotificationMain();

    @UiField
    protected Label editLabel;

    @UiField
    protected DeckPanel deckPanel;

    @UiField
    protected TextArea editArea;

    @UiField
    protected Button saveButton;

    @UiField
    protected Button cancelButton;

    @UiField
    protected Button draftButton;

    @UiField
    protected Label lbChars;

    @UiField
    protected Label lockedLabel;

    @UiField
    protected Label loadingLabel;

    @UiField
    protected HorizontalPanel hPanelLoading;

    @UiField
    protected HorizontalPanel hPanelLocks;

    @UiField
    protected Image lockedImage;

    @UiField
    protected Image loadingImage;

    private String editText;

    private ScrollTable parent;

    private long idSeq;

    private boolean isEditing;

    private boolean untranslated;

    private boolean isDraft;

    private boolean save;

    private Label user;

    private int rowIndex;

    private SubtitleContentEntity meta;

    /**
	 * @return the idSeq
	 */
    public long getIdSeq() {
        return idSeq;
    }

    /**
	 * @param idSeq
	 *            the idSeq to set
	 */
    public void setIdSeq(long idSeq) {
        this.idSeq = idSeq;
    }

    /**
	 * @return the isEditing
	 */
    public boolean isEditing() {
        return isEditing;
    }

    /**
	 * @param isEditing
	 *            the isEditing to set
	 */
    public void setEditing(boolean isEditing) {
        this.isEditing = isEditing;
    }

    /**
	 * @return the untranslated
	 */
    public boolean isUntranslated() {
        return untranslated;
    }

    /**
	 * @param untranslated
	 *            the untranslated to set
	 */
    public void setUntranslated(boolean untranslated) {
        this.untranslated = untranslated;
    }

    /**
	 * @return the isDraft
	 */
    public boolean isDraft() {
        return isDraft;
    }

    /**
	 * @param isDraft
	 *            the isDraft to set
	 */
    public void setDraft(boolean isDraft) {
        this.isDraft = isDraft;
    }

    /**
	 * @return the rowIndex
	 */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
	 * @param rowIndex
	 *            the rowIndex to set
	 */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
	 * @return the meta
	 */
    public SubtitleContentEntity getMeta() {
        return meta;
    }

    /**
	 * @param meta
	 *            the meta to set
	 */
    public void setMeta(SubtitleContentEntity meta) {
        this.meta = meta;
    }

    public EditableSubContentArea(ScrollTable p, Label u, int ri, SubtitleContentEntity me) {
        parent = p;
        user = u;
        isEditing = false;
        untranslated = false;
        save = false;
        rowIndex = ri;
        meta = me;
        initWidget(uiBinder.createAndBindUi(this));
        deckPanel.showWidget(0);
        editLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                lockSequence();
            }
        });
        editArea.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(KeyPressEvent event) {
                EditableSubContentArea changeWidget = null;
                boolean closeActualWidget = false;
                int closeWidgetMode = 0;
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && event.isShiftKeyDown()) {
                    saveOperation();
                } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    cancelOperation();
                } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && event.isControlKeyDown()) {
                    closeActualWidget = true;
                    closeWidgetMode = 0;
                    int nextIndex = rowIndex + 1;
                    if (nextIndex < parent.getDataTable().getRowCount()) {
                        changeWidget = (EditableSubContentArea) parent.getDataTable().getWidget(nextIndex, EditSubtitleTranslationPanel.visibleColumns.indexOf("editsubtitletrans_targettext"));
                    } else {
                        saveOperation();
                    }
                } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && event.isAltKeyDown()) {
                    closeActualWidget = true;
                    closeWidgetMode = 1;
                    int nextIndex = rowIndex + 1;
                    if (nextIndex < parent.getDataTable().getRowCount()) {
                        changeWidget = (EditableSubContentArea) parent.getDataTable().getWidget(nextIndex, EditSubtitleTranslationPanel.visibleColumns.indexOf("editsubtitletrans_targettext"));
                    } else {
                        draftOperation();
                    }
                } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_UP && event.isControlKeyDown()) {
                    int previousIndex = rowIndex - 1;
                    if (previousIndex >= 0) {
                        changeWidget = (EditableSubContentArea) parent.getDataTable().getWidget(previousIndex, EditSubtitleTranslationPanel.visibleColumns.indexOf("editsubtitletrans_targettext"));
                    }
                } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN && event.isControlKeyDown()) {
                    int nextIndex = rowIndex + 1;
                    if (nextIndex < parent.getDataTable().getRowCount()) {
                        changeWidget = (EditableSubContentArea) parent.getDataTable().getWidget(nextIndex, EditSubtitleTranslationPanel.visibleColumns.indexOf("editsubtitletrans_targettext"));
                    }
                }
                if (editArea.getText().length() > 100) {
                    editArea.setText(editText);
                }
                editText = editArea.getText();
                lbChars.setText(String.valueOf(editArea.getText().trim().length()));
                if (changeWidget != null) {
                    if (changeWidget.isEditing()) {
                        changeWidget.switchToEdit();
                    } else {
                        changeWidget.lockSequence();
                    }
                    changeWidget.editArea.setFocus(true);
                    if (closeActualWidget) {
                        if (closeWidgetMode == 0) {
                            saveOperation();
                        } else if (closeWidgetMode == 1) {
                            draftOperation();
                        }
                    } else {
                        editArea.cancelKey();
                    }
                }
            }
        });
        editArea.setVisibleLines(2);
        editArea.setWidth("100%");
        saveButton.setText(constants.getString("save"));
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                saveOperation();
            }
        });
        cancelButton.setText(constants.getString("cancel"));
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                cancelOperation();
            }
        });
        draftButton.setText(constants.getString("savedraft"));
        draftButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                draftOperation();
            }
        });
        lockedImage.setResource(TradubiMain.getImageBundle().lock());
        lockedLabel.setText(constants.getString("editsubtitletrans_lockedsequence"));
        lockedLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                lockSequence();
            }
        });
        hPanelLocks.setCellHorizontalAlignment(lockedImage, HasHorizontalAlignment.ALIGN_LEFT);
        hPanelLocks.setCellHorizontalAlignment(lockedLabel, HasHorizontalAlignment.ALIGN_LEFT);
        loadingImage.setResource(TradubiMain.getImageBundle().loading());
        loadingLabel.setText(constants.getString("loading"));
        hPanelLoading.setCellHorizontalAlignment(loadingImage, HasHorizontalAlignment.ALIGN_RIGHT);
        hPanelLoading.setCellHorizontalAlignment(loadingLabel, HasHorizontalAlignment.ALIGN_LEFT);
    }

    private void saveOperation() {
        if (isDraft) {
            save = true;
        } else {
            save = false;
        }
        setDraft(false);
        switchToLabel();
        if (parent != null) {
            EditSubtitleTranslationPanel.updateRowStatus(rowIndex, meta, parent.getDataTable(), "editsubtitletrans_status_finaltrad");
        }
    }

    private void cancelOperation() {
        if (isEditing) {
            dictService.unlockSequence(idSeq, new UnlockSequenceCallback());
        }
        editArea.setText(editLabel.getText());
        switchToLabel();
    }

    private void draftOperation() {
        if (!isDraft) {
            save = true;
        } else {
            save = false;
        }
        setDraft(true);
        switchToLabel();
        if (parent != null) {
            EditSubtitleTranslationPanel.updateRowStatus(rowIndex, meta, parent.getDataTable(), "editsubtitletrans_status_drafttrad");
        }
    }

    public void switchToEdit() {
        isEditing = true;
        if (deckPanel.getVisibleWidget() == 1) return;
        editArea.setText(getValue());
        deckPanel.showWidget(1);
        editArea.setFocus(true);
        editArea.selectAll();
        lbChars.setText(String.valueOf(editArea.getText().trim().length()));
        if (parent != null) {
            parent.onResize(0, 0);
            ResizableWidgetCollection.get().updateWidgetSize(parent);
        }
    }

    public void switchToLabel() {
        isEditing = false;
        if (deckPanel.getVisibleWidget() == 0) return;
        setValue(editArea.getText(), true);
        if (editArea.getText().trim().equals("")) {
            setValue(constants.getString("editsubtitletrans_empty"));
        }
        deckPanel.showWidget(0);
        if (parent != null) {
            parent.onResize(0, 0);
            ResizableWidgetCollection.get().updateWidgetSize(parent);
        }
    }

    public void switchToLockedLabel(String userName) {
        isEditing = false;
        lockedLabel.setText(constants.getString("editsubtitletrans_lockedsequence") + ": " + userName);
        if (deckPanel.getVisibleWidget() == 2) return;
        deckPanel.showWidget(2);
        if (parent != null) {
            parent.onResize(0, 0);
            ResizableWidgetCollection.get().updateWidgetSize(parent);
        }
    }

    public void switchToLoading() {
        isEditing = false;
        if (deckPanel.getVisibleWidget() == 3) return;
        deckPanel.showWidget(3);
        if (parent != null) {
            parent.onResize(0, 0);
            ResizableWidgetCollection.get().updateWidgetSize(parent);
        }
    }

    public void lockSequence() {
        dictService.lockSequence(idSeq, new LockSequenceCallback());
        switchToLoading();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public String getValue() {
        return editLabel.getText();
    }

    @Override
    public void setValue(String value) {
        editLabel.setText(value);
        editArea.setText(value);
    }

    @Override
    public void setValue(String value, boolean fireEvents) {
        if (save) {
            value += "\n";
            save = false;
        }
        if (fireEvents) ValueChangeEvent.fireIfNotEqual(this, getValue(), value);
        setValue(value.trim());
    }

    @Override
    public void onDetach() {
        if (isEditing) {
            dictService.unlockSequence(idSeq, new UnlockSequenceCallback());
        }
        super.onDetach();
    }
}
