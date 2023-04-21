package org.netxilia.server.service.event;

import java.util.List;
import org.netxilia.api.event.CellEvent;
import org.netxilia.api.event.ColumnEvent;
import org.netxilia.api.event.RowEvent;
import org.netxilia.api.event.SheetEvent;

/**
 * It converts the event structure on the server side on simpler coarser grained event for the client. Some event may
 * not be converted at all (the corresponding method returns null). In this case the communication infrastructure with
 * the client should ignore the event.
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public interface IClientEventConversionService {

    public List<WorkbookClientEvent> convert(CellEvent ev) throws EventConversionException;

    public List<WorkbookClientEvent> convert(ColumnEvent columnEvent) throws EventConversionException;

    public List<WorkbookClientEvent> convert(RowEvent rowEvent) throws EventConversionException;

    public List<WorkbookClientEvent> convert(SheetEvent sheetEvent) throws EventConversionException;
}
