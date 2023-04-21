package org.osmorc.settings;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class ApplicationSettingsUpdateNotifierTest {

    @Before
    public void setUp() {
        _applicationSettingsChangeListener = new ApplicationSettingsUpdateNotifier.Listener() {

            public void applicationSettingsChanged() {
                _applicationSettingsChanged = true;
            }
        };
        _testObject = new ApplicationSettingsUpdateNotifier();
    }

    public void testWithoutListeners() {
        _applicationSettingsChanged = false;
        _testObject.fireApplicationSettingsChanged();
        assertThat(_applicationSettingsChanged, equalTo(false));
    }

    @Test
    public void testWithListener() {
        _testObject.addListener(_applicationSettingsChangeListener);
        _applicationSettingsChanged = false;
        _testObject.fireApplicationSettingsChanged();
        assertThat(_applicationSettingsChanged, equalTo(true));
        _testObject.removeListener(_applicationSettingsChangeListener);
        _applicationSettingsChanged = false;
        _testObject.fireApplicationSettingsChanged();
        assertThat(_applicationSettingsChanged, equalTo(false));
    }

    private boolean _applicationSettingsChanged;

    private ApplicationSettingsUpdateNotifier.Listener _applicationSettingsChangeListener;

    private ApplicationSettingsUpdateNotifier _testObject;
}
