package diagnostics;

import commons.Common;

public interface Diagnostic extends Common {

    public byte getAppointmentID();

    public void setAppointmentID(byte appointmentID);

    public short getDescriptionSize();

    public byte[] getDescription(short offset, short size);

    public void setDescription(byte[] description_buffer, short size_buffer, short offset_buffer, boolean firstBlock);

    public byte getDiagnosticID();

    public void setDiagnosticID(byte id);

    public byte[] getTitle();

    public void setTitle(byte[] title);
}
