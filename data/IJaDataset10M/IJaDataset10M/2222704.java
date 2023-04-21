package com.heavylead.views.concrete.gbui;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import com.heavylead.views.interfaces.EditSettingsViewTags;
import com.heavylead.views.interfaces.IEditSettingsViewListener;
import com.heavylead.wrappers.interfaces.IKeyBindingManager;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BButton;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;

/**
 * The Class TestEditSettingsWindow.
 */
public final class TestEditSettingsWindow extends GbuiTestCase {

    private static final int RESOLUTION_HEIGHT = 600;

    private static final int RESOLUTION_WIDTH = 800;

    private static final String ENGLISH = "English";

    private static final String EN = "en";

    /** The Constant RESOLUTION. */
    private static final String RESOLUTION = "Resolution";

    /** The Constant DEPTH. */
    private static final String DEPTH = "Depth";

    /** The Constant FREQUENCY. */
    private static final String FREQUENCY = "Frequency";

    /** The Constant FULL_SCREEN. */
    private static final String FULL_SCREEN = "FullScreen";

    /** The Constant DISPLAY_LANGUAGE. */
    private static final String DISPLAY_LANGUAGE = "DisplayLanguage";

    /** The Constant CANCEL. */
    private static final String CANCEL = "Cancel";

    /** The Constant CANCEL_DESCRIPTION. */
    private static final String CANCEL_DESCRIPTION = "CancelDescription";

    /** The Constant APPLY. */
    private static final String APPLY = "Apply";

    /** The Constant APPLY_DESCRIPTION. */
    private static final String APPLY_DESCRIPTION = "ApplyDescription";

    /** The Constant OK. */
    private static final String OK = "Ok";

    /** The Constant OK_DESCRIPTION. */
    private static final String OK_DESCRIPTION = "OkDescription";

    private static final String RESOLUTION_1 = "800x600";

    private static final String DEPTH_1 = "16";

    private static final String FREQUENCY_1 = "60";

    /** The _mockery. */
    private final Mockery _mockery = new JUnit4Mockery();

    /** The KeyBindingManager. */
    private IKeyBindingManager _keyBindingManager;

    /** The _window. */
    private EditSettingsWindow _window;

    /** The _mock view listener. */
    private IEditSettingsViewListener _mockViewListener;

    /**
     * Instantiates a new test edit settings window.
     */
    public TestEditSettingsWindow() {
    }

    /**
     * Sets the up.
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        setUpGbui();
        _mockViewListener = _mockery.mock(IEditSettingsViewListener.class);
        _keyBindingManager = _mockery.mock(IKeyBindingManager.class);
        _window = new EditSettingsWindow(getInputHandler(), _keyBindingManager);
        GameStateManager.getInstance().attachChild((GameState) _window);
        _window.activate();
        GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE).execute();
        _window.setViewListener(_mockViewListener);
    }

    /**
     * Tear down.
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    public void tearDown() {
        _window.deactivate();
        GameStateManager.getInstance().detachAllChildren();
        tearDownGbui();
        _mockery.assertIsSatisfied();
    }

    /**
     * Test creation.
     */
    public void testCreation() {
        assertEquals(true, _window.isActive());
        assertEquals(true, _window.getCancel().isEnabled());
        assertEquals(true, _window.getApply().isEnabled());
        assertEquals(true, _window.getOk().isEnabled());
    }

    /**
     * Test get translation tags.
     */
    public void testGetTranslationTags() {
        final List<String> expectedTranslationTags = new ArrayList<String>();
        expectedTranslationTags.add(EditSettingsViewTags.Resolution.toString());
        expectedTranslationTags.add(EditSettingsViewTags.Depth.toString());
        expectedTranslationTags.add(EditSettingsViewTags.Frequency.toString());
        expectedTranslationTags.add(EditSettingsViewTags.FullScreen.toString());
        expectedTranslationTags.add(EditSettingsViewTags.DisplayLanguage.toString());
        expectedTranslationTags.add(EditSettingsViewTags.Cancel.toString());
        expectedTranslationTags.add(EditSettingsViewTags.CancelDescription.toString());
        expectedTranslationTags.add(EditSettingsViewTags.Apply.toString());
        expectedTranslationTags.add(EditSettingsViewTags.ApplyDescription.toString());
        expectedTranslationTags.add(EditSettingsViewTags.Ok.toString());
        expectedTranslationTags.add(EditSettingsViewTags.OkDescription.toString());
        assertEquals(expectedTranslationTags, _window.getTranslationTags());
    }

    /**
     * Test set translation phrases.
     */
    public void testSetTranslationPhrases() {
        final Dictionary<String, String> translatedPhrases = new Hashtable<String, String>();
        translatedPhrases.put(EditSettingsViewTags.Resolution.toString(), RESOLUTION);
        translatedPhrases.put(EditSettingsViewTags.Depth.toString(), DEPTH);
        translatedPhrases.put(EditSettingsViewTags.Frequency.toString(), FREQUENCY);
        translatedPhrases.put(EditSettingsViewTags.FullScreen.toString(), FULL_SCREEN);
        translatedPhrases.put(EditSettingsViewTags.DisplayLanguage.toString(), DISPLAY_LANGUAGE);
        translatedPhrases.put(EditSettingsViewTags.Cancel.toString(), CANCEL);
        translatedPhrases.put(EditSettingsViewTags.CancelDescription.toString(), CANCEL_DESCRIPTION);
        translatedPhrases.put(EditSettingsViewTags.Apply.toString(), APPLY);
        translatedPhrases.put(EditSettingsViewTags.ApplyDescription.toString(), APPLY_DESCRIPTION);
        translatedPhrases.put(EditSettingsViewTags.Ok.toString(), OK);
        translatedPhrases.put(EditSettingsViewTags.OkDescription.toString(), OK_DESCRIPTION);
        _window.setTranslationPhrases(translatedPhrases);
        assertEquals(CANCEL, _window.getCancel().getText());
        assertEquals(CANCEL_DESCRIPTION, _window.getCancel().getTooltipText());
        assertEquals(APPLY, _window.getApply().getText());
        assertEquals(APPLY_DESCRIPTION, _window.getApply().getTooltipText());
        assertEquals(OK, _window.getOk().getText());
        assertEquals(OK_DESCRIPTION, _window.getOk().getTooltipText());
    }

    /**
     * Test layout.
     */
    public void testLayout() {
        final int layoutWidth = 800;
        final int layoutHeight = 600;
        _window.layout(layoutWidth, layoutHeight);
    }

    /**
     * Test ok button pressed.
     */
    public void testOkButtonPressed() {
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).saveSettingsPressed();
            }
        });
        performClick(_window.getOk());
    }

    /**
     * Test apply button pressed.
     */
    public void testApplyButtonPressed() {
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).applyPressed();
            }
        });
        performClick(_window.getApply());
    }

    /**
     * Test cancel button pressed.
     */
    public void testCancelButtonPressed() {
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).cancelPressed();
            }
        });
        performClick(_window.getCancel());
    }

    /**
     * Test frame tick.
     */
    public void testFrameTick() {
        _window.frameTick(TINY_TICK);
    }

    /**
     * Test set resolution options.
     */
    public void testSetResolutionOptions() {
        final List<String> resolutionOptions = new ArrayList<String>();
        resolutionOptions.add(RESOLUTION_1);
        resolutionOptions.add("resolution2");
        _window.setResolutionOptions(resolutionOptions);
        assertEquals(resolutionOptions.size(), _window.getResolutionCombo().getItemCount());
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).selectionChanged();
            }
        });
        _window.setResolution(RESOLUTION_1);
        assertEquals(RESOLUTION_1, _window.getResolutionCombo().getSelectedItem().toString());
        assertEquals(RESOLUTION_WIDTH, _window.getResolutionWidth());
        assertEquals(RESOLUTION_HEIGHT, _window.getResolutionHeight());
    }

    /**
     * Test set depth options.
     */
    public void testSetDepthOptions() {
        final List<String> depthOptions = new ArrayList<String>();
        depthOptions.add(DEPTH_1);
        depthOptions.add("depth2");
        _window.setDepthOptions(depthOptions);
        assertEquals(depthOptions.size(), _window.getDepthCombo().getItemCount());
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).selectionChanged();
            }
        });
        _window.setDepth(DEPTH_1);
        assertEquals(DEPTH_1, _window.getDepthCombo().getSelectedItem().toString());
        assertEquals(Integer.parseInt(DEPTH_1), _window.getDepth());
    }

    /**
     * Test set frequency options.
     */
    public void testSetFrequencyOptions() {
        final List<String> frequencyOptions = new ArrayList<String>();
        frequencyOptions.add(FREQUENCY_1);
        frequencyOptions.add("frequency2");
        _window.setFrequencyOptions(frequencyOptions);
        assertEquals(frequencyOptions.size(), _window.getFrequencyCombo().getItemCount());
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).selectionChanged();
            }
        });
        _window.setFrequency(FREQUENCY_1);
        assertEquals(FREQUENCY_1, _window.getFrequencyCombo().getSelectedItem().toString());
        assertEquals(Integer.parseInt(FREQUENCY_1), _window.getFrequency());
    }

    /**
     * Test set is full screen.
     */
    public void testSetIsFullScreen() {
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).selectionChanged();
            }
        });
        performClick(_window.getIsFullScreenCheckBox());
    }

    /**
     * Test set is full screen to true.
     */
    public void testSetIsFullScreenToTrue() {
        _window.setIsFullScreen(true);
        assertTrue(_window.isFullScreen());
    }

    /**
     * Test set is full screen to false.
     */
    public void testSetIsFullScreenToFalse() {
        _window.setIsFullScreen(false);
        assertFalse(_window.isFullScreen());
    }

    /**
     * Test set language options.
     */
    public void testSetLanguageOptions() {
        final Dictionary<String, String> languageOptions = new Hashtable<String, String>();
        languageOptions.put(EN, ENGLISH);
        languageOptions.put("fr", "French");
        _window.setLanguageOptions(languageOptions);
        assertEquals(languageOptions.size(), _window.getDisplayLanguageCombo().getItemCount());
        _mockery.checking(new Expectations() {

            {
                oneOf(_mockViewListener).selectionChanged();
            }
        });
        _window.setDisplayLanguage(EN);
        assertEquals(ENGLISH, _window.getDisplayLanguageCombo().getSelectedItem().toString());
        assertEquals(EN, _window.getDisplayLanguage());
    }

    /**
     * Test enable apply.
     */
    public void testEnableApply() {
        final BButton btnApply = _window.getApply();
        _window.enableApply(false);
        assertFalse(btnApply.isEnabled());
        _window.enableApply(true);
        assertTrue(btnApply.isEnabled());
    }

    /**
     * Test enable ok.
     */
    public void testEnableOk() {
        final BButton btnOk = _window.getOk();
        _window.enableOk(false);
        assertFalse(btnOk.isEnabled());
        _window.enableOk(true);
        assertTrue(btnOk.isEnabled());
    }
}
