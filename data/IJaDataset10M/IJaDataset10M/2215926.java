package org.netxilia.api.impl.command;

import org.netxilia.api.command.AbstractRowCommand;
import org.netxilia.api.command.IMoreRowCommands;
import org.netxilia.api.command.IRowCommand;
import org.netxilia.api.display.IStyleService;
import org.netxilia.api.display.StyleApplyMode;
import org.netxilia.api.display.Styles;
import org.netxilia.api.exception.NetxiliaBusinessException;
import org.netxilia.api.model.RowData;
import org.netxilia.api.model.WorkbookId;
import org.netxilia.api.reference.Range;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class MoreRowCommandsImpl implements IMoreRowCommands {

    @Autowired
    private IStyleService styleService;

    @Override
    public IRowCommand applyStyles(final WorkbookId workbookId, final Range range, final Styles applyStyle, final StyleApplyMode applyMode) {
        return new AbstractRowCommand(range) {

            @Override
            public RowData apply(RowData data) throws NetxiliaBusinessException {
                Styles newStyles = styleService.applyStyle(workbookId, data.getStyles(), applyStyle, applyMode);
                return data.withStyles(newStyles);
            }
        };
    }
}
