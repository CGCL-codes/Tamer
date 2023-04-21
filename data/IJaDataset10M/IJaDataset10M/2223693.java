package org.elf.businesslayer.kernel.services.record;

import org.elf.datalayer.*;
import org.elf.businesslayer.*;

/**
 *
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public interface KernelRecord {

    boolean newRecord(Messages messages, boolean allowWarnings);

    boolean read(Messages messages, boolean allowWarnings);

    boolean hardSave(Messages messages, boolean allowWarnings);

    boolean hardDelete(Messages messages, boolean allowWarnings);

    boolean edit(Messages messages, boolean allowWarnings);

    void cancel(Messages messages);

    KernelFields getKernelFields();

    RecordState getState();

    String getName();

    boolean isEditing();
}
