package org.escapek.core.internal.assemblers;

import org.escapek.core.dto.logging.ChangeLogDTO;
import org.escapek.core.internal.model.logging.ChangeLog;

public class ChangeLogAssembler extends AbstractAssembler<ChangeLog, ChangeLogDTO> {

    @Override
    public ChangeLogDTO createDTO(ChangeLog source) {
        if (source == null) return null;
        ChangeLogDTO dest = new ChangeLogDTO();
        fillDTO(dest, source);
        return dest;
    }

    @Override
    public ChangeLog createModel(ChangeLogDTO source) {
        if (source == null) return null;
        ChangeLog dest = new ChangeLog();
        fillModel(dest, source);
        return dest;
    }

    @Override
    public void fillDTO(ChangeLogDTO dest, ChangeLog source) {
        dest.setRecordChanges(false);
        dest.setChangeDate(source.getChangeDate());
        dest.setChangedField(source.getChangedField());
        dest.setChangedNodeId(source.getChangedNode().getId());
        dest.setChangeTicketId(source.getChangeTicket().getId());
        dest.setId(source.getId());
        dest.setOldValue(source.getOldValue());
        dest.setRecordChanges(true);
    }

    @Override
    public void fillModel(ChangeLog dest, ChangeLogDTO source) {
        dest.setChangeDate(source.getChangeDate());
        dest.setChangedField(source.getChangedField());
        dest.setOldValue(source.getOldValue());
    }
}
