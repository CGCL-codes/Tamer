package com.heavylead.views.concrete.gbui;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import com.google.inject.Inject;
import com.heavylead.views.interfaces.ISelectCampaignView;
import com.heavylead.views.interfaces.ISelectCampaignViewListener;
import com.heavylead.views.interfaces.SelectCampaignViewTags;
import com.heavylead.wrappers.interfaces.IKeyBindingManager;
import com.jme.input.InputHandler;
import com.jmex.bui.BButton;
import com.jmex.bui.BLabel;
import com.jmex.bui.BMultiSelectBox;
import com.jmex.bui.BMultiSelectBox.SelectionMode;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BToggleButton;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.util.Point;

/**
 * The Class SelectCampaignWindow.
 */
public final class SelectCampaignWindow extends GbuiGameState implements ISelectCampaignView {

    private static final int FOUR = 4;

    /** The Constant VERTICAL_PADDING_COUNT. */
    private static final int VERTICAL_PADDING_COUNT = 5;

    /** The Constant MAX_BUTTONS_ACROSS. */
    private static final int MAX_BUTTONS_ACROSS = 6;

    /** The Constant PADDING_SIZE. */
    private static final int PADDING_SIZE = 10;

    /** The Constant LABEL_HEIGHT. */
    private static final int LABEL_HEIGHT = 32;

    /** The Constant ENTRY. */
    private static final String ENTRY = "entry";

    /** The _lbl select campaign. */
    private BLabel _lblSelectCampaign;

    /** The list of available campaigns. */
    private BMultiSelectBox<String> _lstCampaigns;

    /** The Campaign Description. */
    private BTextArea _txtCampaignDescription;

    /** The Campaign Description scrollpane. */
    private BScrollPane _campaignDescriptionScrollPane;

    /** The Cancel button. */
    private BButton _btnCancel;

    /** The Continue Campaign button. */
    private BButton _btnContinueCampaign;

    /** The Start New Campaign button. */
    private BButton _btnStartNewCampaign;

    /** The _select campaign view listener. */
    private ISelectCampaignViewListener _selectCampaignViewListener;

    /**
     * Instantiates a new select campaign window.
     * 
     * @param inputHandler the input handler
     * @param keyBindingManager the key binding manager
     */
    @Inject
    SelectCampaignWindow(final InputHandler inputHandler, final IKeyBindingManager keyBindingManager) {
        super("SelectCampaignView", inputHandler, keyBindingManager);
        final int windowWidth = 800;
        final int windowHeight = 600;
        getWindow().setSize(windowWidth, windowHeight);
        createSelectCampaignLabel();
        final int labelPosY = getWindow().getHeight() - PADDING_SIZE - LABEL_HEIGHT;
        getWindow().add(_lblSelectCampaign, new Point(PADDING_SIZE, labelPosY));
        createCampaignList();
        final int listHeight = (int) (getWindow().getHeight() * 0.15);
        final int listWidth = getWindow().getWidth() - (PADDING_SIZE * 2);
        _lstCampaigns.setPreferredSize(listWidth, listHeight);
        final int listPosY = labelPosY - PADDING_SIZE - listHeight;
        getWindow().add(_lstCampaigns, new Point(PADDING_SIZE, listPosY));
        createCampaignDescription(listHeight, listWidth, listPosY);
        final int baseButtonWidth = (getWindow().getWidth() / MAX_BUTTONS_ACROSS) - PADDING_SIZE;
        createCancelButton(baseButtonWidth);
        createContinueCampaignButton(baseButtonWidth);
        createStartNewCampaignButton(baseButtonWidth);
        getWindow().center();
    }

    /**
     * Layout.
     * 
     * @param screenWidth the screen width
     * @param screenHeight the screen height
     * 
     * @see com.heavylead.views.interfaces.IGameStateView#layout(java.lang.String)
     */
    @Override
    public void layout(final int screenWidth, final int screenHeight) {
        getWindow().center();
    }

    /**
     * Creates the select campaign label.
     */
    private void createSelectCampaignLabel() {
        final int labelWidth = getWindow().getWidth() - (PADDING_SIZE * 2);
        _lblSelectCampaign = new BLabel("");
        _lblSelectCampaign.setPreferredSize(labelWidth, LABEL_HEIGHT);
    }

    /**
     * Creates the campaign list.
     */
    private void createCampaignList() {
        _lstCampaigns = new BMultiSelectBox<String>() {

            @Override
            protected BToggleButton createListEntry(final String campaignName) {
                final BToggleButton b = new BToggleButton(campaignName, "select");
                b.setProperty(ENTRY, campaignName);
                b.addListener(new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent event) {
                        final BButton button = (BButton) event.getSource();
                        final String selectedCampaignName = (String) button.getProperty(ENTRY);
                        _selectCampaignViewListener.campaignSelected(selectedCampaignName);
                    }
                });
                b.setStyleClass("list_entry");
                return b;
            }
        };
        _lstCampaigns.setName("listEntry");
        _lstCampaigns.setSelectionMode(SelectionMode.SINGLE);
        _lstCampaigns.setStyleClass("scrolling_list");
    }

    /**
     * Creates the campaign description.
     * 
     * @param listHeight the list height
     * @param listWidth the list width
     * @param listPosY the list pos y
     */
    private void createCampaignDescription(final int listHeight, final int listWidth, final int listPosY) {
        final int scrollBarWidth = PADDING_SIZE * 2;
        _txtCampaignDescription = new BTextArea();
        final int campaignDescriptionHeight = getWindow().getHeight() - (LABEL_HEIGHT * 2) - listHeight - (PADDING_SIZE * VERTICAL_PADDING_COUNT);
        _campaignDescriptionScrollPane = new BScrollPane(_txtCampaignDescription);
        _campaignDescriptionScrollPane.setPreferredSize(listWidth - scrollBarWidth, campaignDescriptionHeight);
        final int descriptionPosY = listPosY - PADDING_SIZE - campaignDescriptionHeight;
        getWindow().add(_campaignDescriptionScrollPane, new Point(PADDING_SIZE, descriptionPosY));
    }

    /**
     * Creates the cancel button.
     * 
     * @param baseButtonWidth the base button width
     */
    private void createCancelButton(final int baseButtonWidth) {
        _btnCancel = new BButton("");
        _btnCancel.setPreferredSize(baseButtonWidth, LABEL_HEIGHT);
        _btnCancel.addListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
                _selectCampaignViewListener.cancelPressed();
            }
        });
        getWindow().add(_btnCancel, new Point(PADDING_SIZE, PADDING_SIZE));
    }

    /**
     * Creates the continue campaign button.
     * 
     * @param baseButtonWidth the base button width
     */
    private void createContinueCampaignButton(final int baseButtonWidth) {
        _btnContinueCampaign = new BButton("");
        _btnContinueCampaign.setPreferredSize(baseButtonWidth * 2, LABEL_HEIGHT);
        _btnContinueCampaign.setEnabled(false);
        _btnContinueCampaign.addListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
                _selectCampaignViewListener.continueCampaignPressed();
            }
        });
        getWindow().add(_btnContinueCampaign, new Point(getWindow().getWidth() - (PADDING_SIZE * 2) - (baseButtonWidth * FOUR), PADDING_SIZE));
    }

    /**
     * Creates the start new campaign button.
     * 
     * @param baseButtonWidth the base button width
     */
    private void createStartNewCampaignButton(final int baseButtonWidth) {
        _btnStartNewCampaign = new BButton("");
        _btnStartNewCampaign.setPreferredSize(baseButtonWidth * 2, LABEL_HEIGHT);
        _btnContinueCampaign.setEnabled(false);
        _btnStartNewCampaign.addListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent event) {
                _selectCampaignViewListener.startNewCampaignPressed();
            }
        });
        getWindow().add(_btnStartNewCampaign, new Point(getWindow().getWidth() - PADDING_SIZE - (baseButtonWidth * 2), PADDING_SIZE));
    }

    /**
     * Sets the view listener.
     * 
     * @param selectCampaignListener the select campaign listener
     * 
     * @see com.heavylead.ISelectCampaignView#setViewListener
     * (com.heavylead.ISelectCampaignViewListener)
     */
    @Override
    public void setViewListener(final ISelectCampaignViewListener selectCampaignListener) {
        _selectCampaignViewListener = selectCampaignListener;
    }

    /**
     * Sets the campaign names.
     * 
     * @param campaignNames the campaign names
     * 
     * @see com.heavylead.ISelectCampaignView#setCampaignNames(java.util.List)
     */
    @Override
    public void setCampaignNames(final List<String> campaignNames) {
        _lstCampaigns.removeValues();
        for (String campaignName : campaignNames) {
            _lstCampaigns.addValue(campaignName, false);
        }
        if (campaignNames.size() > 0) {
        }
    }

    /**
     * Sets the selected campaign.
     * 
     * @param listIndex the list index
     * 
     * @see com.heavylead.views.interfaces.ISelectCampaignView#setSelectedCampaign(int)
     */
    @Override
    public void setSelectedCampaign(final int listIndex) {
    }

    /**
     * Sets the campaign description.
     * 
     * @param description the description
     * 
     * @see com.heavylead.ISelectCampaignView#setCampaignDescription(java.lang.String)
     */
    @Override
    public void setCampaignDescription(final String description) {
        _txtCampaignDescription.setText(description);
    }

    /**
     * Enable continue campaign.
     * 
     * @param enabled the enabled
     * 
     * @see com.heavylead.ISelectCampaignView#enableContinueCampaign(boolean)
     */
    @Override
    public void enableContinueCampaign(final boolean enabled) {
        _btnContinueCampaign.setEnabled(enabled);
    }

    /**
     * Enable start new campaign.
     * 
     * @param enabled the enabled
     * 
     * @see com.heavylead.ISelectCampaignView#enableStartNewCampaign(boolean)
     */
    @Override
    public void enableStartNewCampaign(final boolean enabled) {
        _btnStartNewCampaign.setEnabled(enabled);
    }

    /**
     * Gets the translation tags.
     * 
     * @return the translation tags
     * 
     * @see com.heavylead.IGameStateView#getTranslationTags()
     */
    @Override
    public List<String> getTranslationTags() {
        final List<String> translationTags = new ArrayList<String>();
        translationTags.add(SelectCampaignViewTags.SelectCampaign.toString());
        translationTags.add(SelectCampaignViewTags.Cancel.toString());
        translationTags.add(SelectCampaignViewTags.CancelDescription.toString());
        translationTags.add(SelectCampaignViewTags.ContinueCampaign.toString());
        translationTags.add(SelectCampaignViewTags.ContinueCampaignDescription.toString());
        translationTags.add(SelectCampaignViewTags.StartNewCampaign.toString());
        translationTags.add(SelectCampaignViewTags.StartNewCampaignDescription.toString());
        return translationTags;
    }

    /**
     * Sets the translation phrases.
     * 
     * @param translatedPhrases the translated phrases
     * 
     * @see com.heavylead.IGameStateView#setTranslationPhrases(java.util.Dictionary)
     */
    @Override
    public void setTranslationPhrases(final Dictionary<String, String> translatedPhrases) {
        _lblSelectCampaign.setText(translatedPhrases.get(SelectCampaignViewTags.SelectCampaign.toString()));
        _btnCancel.setText(translatedPhrases.get(SelectCampaignViewTags.Cancel.toString()));
        _btnCancel.setTooltipText(translatedPhrases.get(SelectCampaignViewTags.CancelDescription.toString()));
        _btnContinueCampaign.setText(translatedPhrases.get(SelectCampaignViewTags.ContinueCampaign.toString()));
        _btnContinueCampaign.setTooltipText(translatedPhrases.get(SelectCampaignViewTags.ContinueCampaignDescription.toString()));
        _btnStartNewCampaign.setText(translatedPhrases.get(SelectCampaignViewTags.StartNewCampaign.toString()));
        _btnStartNewCampaign.setTooltipText(translatedPhrases.get(SelectCampaignViewTags.StartNewCampaignDescription.toString()));
    }

    /**
     * Frame tick.
     * 
     * @param timePerFrame the time per frame
     * 
     * @see com.heavylead.gbui.GbuiGameState#frameTick(float)
     */
    @Override
    public void frameTick(final float timePerFrame) {
    }

    /**
     * Gets the campaigns list.
     * Intended for testing only.
     * 
     * @return the campaigns list
     */
    BMultiSelectBox<String> getCampaignsList() {
        return _lstCampaigns;
    }

    /**
     * Gets the campaign description.
     * 
     * @return the campaign description
     */
    BTextArea getCampaignDescription() {
        return _txtCampaignDescription;
    }

    /**
     * Gets the cancel button.
     * Intended for testing only.
     * 
     * @return the cancel
     */
    BButton getCancel() {
        return _btnCancel;
    }

    /**
     * Gets the continue campaign button.
     * Intended for testing only.
     * 
     * @return the continue campaign
     */
    BButton getContinueCampaign() {
        return _btnContinueCampaign;
    }

    /**
     * Gets the start new campaign button.
     * Intended for testing only.
     * 
     * @return the start new campaign
     */
    BButton getStartNewCampaign() {
        return _btnStartNewCampaign;
    }

    /**
     * Gets the view listener.
     * Intended for testing only.
     * 
     * @return the view listener
     */
    ISelectCampaignViewListener getViewListener() {
        return _selectCampaignViewListener;
    }
}
