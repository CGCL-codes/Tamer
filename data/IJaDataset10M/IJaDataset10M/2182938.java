package org.netxilia.server.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.netxilia.api.command.ColumnCommands;
import org.netxilia.api.exception.NotFoundException;
import org.netxilia.api.exception.StorageException;
import org.netxilia.api.model.ISheet;
import org.netxilia.api.model.WorkbookId;
import org.netxilia.api.reference.Range;

@Path("/columns")
public class ColumnResource extends AbstractResource {

    @PUT
    @Path("/{workbook}/{sheet}/{column}")
    public void insert(@PathParam("workbook") WorkbookId workbookName, @PathParam("sheet") String sheetName, @PathParam("column") int pos) throws NotFoundException, StorageException {
        ISheet sheet = getWorkbookProcessor().getWorkbook(workbookName).getSheet(sheetName);
        sheet.sendCommand(ColumnCommands.insert(Range.range(pos)));
    }

    @DELETE
    @Path("/{workbook}/{sheet}/{column}")
    public void delete(@PathParam("workbook") WorkbookId workbookName, @PathParam("sheet") String sheetName, @PathParam("column") int pos) throws NotFoundException, StorageException {
        ISheet sheet = getWorkbookProcessor().getWorkbook(workbookName).getSheet(sheetName);
        sheet.sendCommand(ColumnCommands.delete(Range.range(pos)));
    }

    @POST
    @Path("/{workbook}/{sheet}/{column}")
    public void modify(@PathParam("workbook") WorkbookId workbookName, @PathParam("sheet") String sheetName, @PathParam("column") int pos, @FormParam("width") int width) throws NotFoundException, StorageException {
        ISheet sheet = getWorkbookProcessor().getWorkbook(workbookName).getSheet(sheetName);
        sheet.sendCommand(ColumnCommands.width(Range.range(pos), width));
    }
}
