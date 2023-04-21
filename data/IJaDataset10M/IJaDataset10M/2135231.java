package org.netxilia.api.command;

import org.netxilia.api.model.SheetData;

public interface ISheetCommand {

    public SheetData apply(SheetData data);
}
