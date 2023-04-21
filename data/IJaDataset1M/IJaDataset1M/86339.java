package com.homedepot.provisioning;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.homedepot.provisioning.reporting.*;
import com.homedepot.provisioning.server.Server;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This report will generates a list of computer types 
 * that are currently formatted as OPENWEBTERM
 * 
 * @version	1.0
 * @author	Felicia Rosemond (frosemond@gmail.com)
 *
 */
public class HardwareReportODP {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String ttServer = "cpntqa45";
        Reporter ttDataReport = new Reporter();
        ttDataReport.setMachineName(ttServer);
        System.out.println("attempting to connect to " + ttServer);
        ttDataReport.generateServerList();
        if (ttDataReport.getServers() != null) {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("Open-WebTerm-HardwareReport.pdf"));
                document.open();
                document.addTitle("Home Depot - Open WebTerm Hardware Report");
                document.addAuthor("Felicia Rosemond");
                document.addSubject("Home Depot - Open WebTerm Hardware Report");
                document.addKeywords("hardware, open, pc, webterm");
                document.addCreator("Felicia Rosemond");
                document.add(new Paragraph("Home Depot - Open WebTerm Hardware Report"));
                document.add(new Paragraph("\nGenerated on " + new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()) + "\n"));
                document.add(new Paragraph("\n "));
                for (Server tempSrvr : ttDataReport.getServers()) {
                    String[] parts = tempSrvr.getName().split("_");
                    System.out.println("Checking " + parts[0]);
                    Reporter tempRptr = new Reporter();
                    tempRptr.setMachineName(parts[0]);
                    System.out.println("Generating list of server");
                    tempRptr.generateServerList();
                    System.out.println("Completed list of servers");
                    if (tempRptr.getServers() != null) {
                        PdfPTable table = new PdfPTable(2);
                        boolean addTable = false;
                        boolean printHeader = false;
                        for (Server device : tempRptr.getServers()) {
                            if (device.getName().contains("OPENWEBTERM")) {
                                addTable = true;
                                if (!printHeader) {
                                    PdfPCell cell = new PdfPCell(new Paragraph("Store " + parts[0].substring(4)));
                                    cell.setColspan(2);
                                    cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
                                    table.addCell(cell);
                                    table.addCell("Device Name");
                                    table.addCell("Machine Type");
                                    printHeader = true;
                                }
                                String[] dev_parts = device.getName().split("_");
                                System.out.println(device.getName());
                                table.addCell(dev_parts[0]);
                                table.addCell(dev_parts[3]);
                            }
                        }
                        if (addTable) {
                            document.add(table);
                            document.add(new Paragraph("\n"));
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.close();
        }
        return;
    }
}
