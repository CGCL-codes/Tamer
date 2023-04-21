package org.xnap.commons.settings;

/**
 * @author Steffen Pingel
 */
public class IntSettingTest extends AbstractSettingTest<Integer> {

    public IntSettingTest() {
        super(1, 0, -1);
    }

    @Override
    protected Setting<Integer> createSetting(SettingResource backend, String key, Integer defaultValue) {
        return new IntSetting(backend, key, defaultValue);
    }

    public void testMinMaxConstructor() {
        IntSetting setting = new IntSetting(backend, "test", 1, 1, 3);
        setting.setValue(2);
        assertEquals(new Integer(2), setting.getValue());
        assertIllegalArgumentException(setting, 0);
        assertIllegalArgumentException(setting, 4);
    }
}
