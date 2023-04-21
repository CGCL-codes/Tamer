package org.personalsmartspace.ex.dpi_data_gen.impl.operators;

import org.personalsmartspace.ex.dpi_data_gen.impl.values.ActivityValues;
import org.personalsmartspace.ex.dpi_data_gen.impl.values.StatusValues;
import org.personalsmartspace.ex.dpi_data_gen.impl.values.SymLocValues;

/**
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas
 *         Liampotis</a> (ICCS)
 * @since 0.5.3
 */
public final class Victor extends ConfigurablePSS {

    @Override
    protected String getActivity() {
        return ActivityValues.LYING;
    }

    @Override
    protected String getAge() {
        return "15";
    }

    @Override
    protected String getEducation() {
        return "none";
    }

    @Override
    protected String getEmail() {
        return "victor.timberland@gmail.com";
    }

    @Override
    protected String getName() {
        return "Victor Timberland";
    }

    @Override
    protected String getProfession() {
        return "none";
    }

    @Override
    protected String getStatus() {
        return StatusValues.BUSY;
    }

    @Override
    protected String getSymbolicLocation() {
        return SymLocValues.OUTDOOR;
    }

    @Override
    protected String getLocation() {
        return "3,3,3";
    }

    @Override
    protected String getRole() {
        return "victim";
    }
}
