package com.lowagie.tools.plugins;

import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JInternalFrame;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.tools.arguments.FileArgument;
import com.lowagie.tools.arguments.PdfFilter;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * This tool lets you generate a PDF that shows N pages on 1.
 */
public class Divide extends AbstractTool {

    static {
        addVersion("$Id: Divide.java 2288 2006-08-16 07:51:49Z blowagie $");
    }

    /**
	 * Constructs an Divide object.
	 */
    public Divide() {
        menuoptions = MENU_EXECUTE | MENU_EXECUTE_SHOW;
        arguments.add(new FileArgument(this, "srcfile", "The file you want to divide", false, new PdfFilter()));
        arguments.add(new FileArgument(this, "destfile", "The resulting PDF", true, new PdfFilter()));
    }

    /**
	 * @see com.lowagie.tools.plugins.AbstractTool#createFrame()
	 */
    protected void createFrame() {
        internalFrame = new JInternalFrame("Divide", true, false, true);
        internalFrame.setSize(300, 80);
        internalFrame.setJMenuBar(getMenubar());
        System.out.println("=== Divide OPENED ===");
    }

    /**
	 * @see com.lowagie.tools.plugins.AbstractTool#execute()
	 */
    public void execute() {
        try {
            if (getValue("srcfile") == null) {
                throw new InstantiationException("You need to choose a sourcefile");
            }
            File src = (File) getValue("srcfile");
            if (getValue("destfile") == null) {
                throw new InstantiationException("You need to choose a destination file");
            }
            File dest = (File) getValue("destfile");
            PdfReader reader = new PdfReader(src.getAbsolutePath());
            int total = reader.getNumberOfPages();
            System.out.println("There are " + total + " pages in the original file.");
            Rectangle pageSize = reader.getPageSize(1);
            Rectangle newSize = new Rectangle(pageSize.width() / 2, pageSize.height());
            Document document = new Document(newSize, 0, 0, 0, 0);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            float offsetX, offsetY;
            int p;
            for (int i = 0; i < total; i++) {
                p = i + 1;
                pageSize = reader.getPageSize(p);
                newSize = new Rectangle(pageSize.width() / 2, pageSize.height());
                document.newPage();
                offsetX = 0;
                offsetY = 0;
                page = writer.getImportedPage(reader, p);
                cb.addTemplate(page, 1, 0, 0, 1, offsetX, offsetY);
                document.newPage();
                offsetX = -newSize.width();
                offsetY = 0;
                page = writer.getImportedPage(reader, p);
                cb.addTemplate(page, 1, 0, 0, 1, offsetX, offsetY);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @see com.lowagie.tools.plugins.AbstractTool#valueHasChanged(com.lowagie.tools.arguments.ToolArgument)
	 */
    public void valueHasChanged(ToolArgument arg) {
        if (internalFrame == null) {
            return;
        }
    }

    /**
	 * Generates a divided version of an NUp version of an existing PDF file.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        Divide tool = new Divide();
        if (args.length < 2) {
            System.err.println(tool.getUsage());
        }
        tool.setArguments(args);
        tool.execute();
    }

    /**
	 * @see com.lowagie.tools.plugins.AbstractTool#getDestPathPDF()
	 */
    protected File getDestPathPDF() throws InstantiationException {
        return (File) getValue("destfile");
    }
}
