package co.edu.unal.ungrid.grid.image;

import co.edu.unal.space.util.SpaceObject;
import co.edu.unal.ungrid.registration.nonrigid.util.DisplacementField;

public class SpaceField extends SpaceObject {

    public static final long serialVersionUID = 1L;

    public SpaceField() {
    }

    public SpaceField(final int id) {
        setSpaceId(id);
    }

    public SpaceField(final String id) {
        setSpaceId(id);
    }

    public SpaceField(final int id, final DisplacementField field) {
        assert field != null;
        setSpaceId(id);
        setData(field);
    }

    private void setSpaceId(int id) {
        setSpaceId(HEADER + id);
    }

    public void setData(final DisplacementField field) {
        m_field = field;
    }

    public DisplacementField getData() {
        return m_field;
    }

    public int getWidth() {
        return (m_field != null ? m_field.getWidth() : 0);
    }

    public int getHeight() {
        return (m_field != null ? m_field.getHeight() : 0);
    }

    public DisplacementField m_field;

    public static final String HEADER = "field-";
}
