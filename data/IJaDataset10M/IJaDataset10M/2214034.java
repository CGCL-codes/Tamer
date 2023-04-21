package be.lassi.ui.library;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;
import be.lassi.base.Holder;
import be.lassi.domain.FixtureDefinition;

/**
 * Tests class <code>PresetsPresentationModel</code>.
 */
public class PresetsPresentationModelTestCase {

    private final Holder<FixtureDefinition> holder = new Holder<FixtureDefinition>();

    private final PresetsPresentationModel model = new PresetsPresentationModel(holder);

    @Test
    public void testNoDefinition() {
        holder.setValue(null);
        assertFalse(model.getActionAdd().isEnabled());
        assertFalse(model.getActionRemove().isEnabled());
    }

    @Test
    public void testNoPresets() {
        FixtureDefinition definition = new FixtureDefinition();
        holder.setValue(definition);
        assertTrue(model.getActionAdd().isEnabled());
        assertFalse(model.getActionRemove().isEnabled());
    }

    @Test
    public void testAdd() {
        FixtureDefinition definition = new FixtureDefinition();
        holder.setValue(definition);
        assertEquals(definition.getPresetCount(), 0);
        model.getActionAdd().action();
        assertEquals(definition.getPresetCount(), 1);
        assertEquals(definition.getPreset(0).getName(), "");
        assertTrue(model.getActionAdd().isEnabled());
        assertFalse(model.getActionRemove().isEnabled());
    }

    @Test
    public void testRemove() {
        FixtureDefinition definition = new FixtureDefinition();
        definition.addPreset("preset");
        holder.setValue(definition);
        assertFalse(model.getActionRemove().isEnabled());
        model.getSelectionModel().setSelectionInterval(0, 0);
        assertTrue(model.getActionRemove().isEnabled());
        model.getActionRemove().action();
        assertEquals(definition.getPresetCount(), 0);
        assertFalse(model.getActionRemove().isEnabled());
    }
}
