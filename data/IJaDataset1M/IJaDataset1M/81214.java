package com.itextpdf.text.pdf.events;

import java.util.ArrayList;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;

/**
 * If you want to add more than one event to a cell,
 * you have to construct a PdfPCellEventForwarder, add the
 * different events to this object and add the forwarder to
 * the PdfPCell.
 */
public class PdfPCellEventForwarder implements PdfPCellEvent {

    /** ArrayList containing all the PageEvents that have to be executed. */
    protected ArrayList<PdfPCellEvent> events = new ArrayList<PdfPCellEvent>();

    /**
	 * Add a page event to the forwarder.
	 * @param event an event that has to be added to the forwarder.
	 */
    public void addCellEvent(PdfPCellEvent event) {
        events.add(event);
    }

    /**
	 * @see com.itextpdf.text.pdf.PdfPCellEvent#cellLayout(com.itextpdf.text.pdf.PdfPCell, com.itextpdf.text.Rectangle, com.itextpdf.text.pdf.PdfContentByte[])
	 */
    public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
        for (PdfPCellEvent event : events) {
            event.cellLayout(cell, position, canvases);
        }
    }
}
