package org.rapla.gui.internal.print;

import java.awt.Component;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

public interface ExportService extends org.rapla.entities.Named {

    void export(Printable printable, PageFormat pageFormat, Component parentComponent) throws Exception;
}
