package com.technoetic.xplanner.export;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;
import org.apache.commons.lang.StringUtils;
import org.hibernate.classic.Session;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PdfExporter implements Exporter {

    public static final int SCALE_FACTOR = 2;

    public static final Font NORMAL_TEXT_FONT = new Font(Font.HELVETICA, 14 * SCALE_FACTOR, Font.NORMAL);

    public static final Font BOLD_TEXT_FONT = new Font(Font.HELVETICA, 14 * SCALE_FACTOR, Font.BOLD);

    public static final Font TITLE_FONT = new Font(Font.HELVETICA, 24 * SCALE_FACTOR, Font.BOLD);

    public void initializeHeaders(HttpServletResponse response) {
        response.setHeader("Content-type", "application/pdf");
        response.setHeader("Content-disposition", "inline; filename=export.pdf");
    }

    public byte[] export(Session session, Object object) throws ExportException {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            exportToPdf(object, output);
            return output.toByteArray();
        } catch (DocumentException e) {
            throw new ExportException(e);
        } finally {
            if (output != null) {
                output.reset();
            }
        }
    }

    public void exportToPdf(Object object, OutputStream output) throws DocumentException {
        PdfWriter docWriter = null;
        Rectangle pageRectangle = PageSize.LETTER.rotate();
        Document document = new Document(pageRectangle, 10, 10, 22 * SCALE_FACTOR, 10);
        try {
            docWriter = PdfWriter.getInstance(document, output);
            if (object instanceof Iteration) {
                write((Iteration) object, document, pageRectangle, docWriter);
            } else if (object instanceof UserStory) {
                write((UserStory) object, document, pageRectangle, docWriter);
            } else if (object instanceof Task) {
                write((Task) object, document, pageRectangle, docWriter);
            }
        } finally {
            if (document.isOpen()) document.close();
            if (docWriter != null) docWriter.close();
        }
    }

    private void write(Iteration iteration, Document document, Rectangle pageRectangle, PdfWriter docWriter) throws DocumentException {
        for (Iterator<UserStory> iterator = iteration.getUserStories().iterator(); iterator.hasNext(); ) {
            UserStory userStory = iterator.next();
            write(userStory, document, pageRectangle, docWriter);
        }
    }

    private void write(UserStory story, Document document, Rectangle pageRectangle, PdfWriter docWriter) throws DocumentException {
        Person customer = story.getCustomer();
        String customerName = "";
        if (customer != null) customerName = customer.getName();
        writeCard(document, pageRectangle, docWriter, story.getName(), story.getDescription(), new Field[] { new Field("Customer", customerName), new Field("Estimate", Double.toString(story.getEstimatedHours())) }, null);
        Collection<Task> tasks = story.getTasks();
        for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
            Task task = it.next();
            write(task, document, pageRectangle, docWriter);
        }
    }

    private void write(Task task, Document document, Rectangle pageRectangle, PdfWriter docWriter) throws DocumentException {
        writeCard(document, pageRectangle, docWriter, task.getName(), task.getDescription(), new Field[] { new Field("Acceptor", ""), new Field("Estimate", Double.toString(task.getEstimatedHours())) }, task.getUserStory().getName());
    }

    private void writeCard(Document document, Rectangle pageRectangle, PdfWriter docWriter, String title, String description, Field[] fields, String containerTitle) throws DocumentException {
        document.resetHeader();
        if (!document.isOpen()) document.open();
        document.newPage();
        PdfPTable headerTable = newTable(pageRectangle, 1);
        if (containerTitle != null) {
            headerTable.getDefaultCell().setPaddingBottom(-5);
            headerTable.addCell(new Paragraph(containerTitle, new Font(Font.HELVETICA, 6 * SCALE_FACTOR, Font.BOLD)));
        }
        headerTable.getDefaultCell().setBorderWidth(2);
        headerTable.getDefaultCell().setBorder(Rectangle.BOTTOM);
        headerTable.getDefaultCell().setPaddingBottom(15);
        headerTable.addCell(new Paragraph(title, TITLE_FONT));
        headerTable.getDefaultCell().setBorderWidth(0);
        headerTable.getDefaultCell().setPaddingBottom(0);
        headerTable.addCell(new Paragraph(StringUtils.defaultString(description), NORMAL_TEXT_FONT));
        headerTable.writeSelectedRows(0, -1, 10, pageRectangle.height(), docWriter.getDirectContent());
        PdfPTable table = newTable(pageRectangle, 2);
        for (int i = 0; i < fields.length; i++) {
            addFieldCell(table, fields[i]);
        }
        table.writeSelectedRows(0, -1, 10f, 10f + table.getRowHeight(0), docWriter.getDirectContent());
    }

    private PdfPTable newTable(Rectangle pageRectangle, int columns) {
        PdfPTable table = new PdfPTable(columns);
        table.getDefaultCell().setBorderWidth(0);
        table.setTotalWidth(pageRectangle.width() - 20);
        return table;
    }

    class Field {

        private final String title;

        private final String value;

        public Field(String title, String value) {
            this.title = StringUtils.defaultString(title);
            this.value = StringUtils.defaultString(value);
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }
    }

    private void addFieldCell(PdfPTable table, Field field) {
        Phrase cell = new Phrase(new Chunk("" + field.getTitle() + ": ", BOLD_TEXT_FONT));
        cell.add(new Chunk(field.getValue(), NORMAL_TEXT_FONT));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }

    public static void main(String[] args) throws Exception {
        FileOutputStream stream = new FileOutputStream("XPlannerStories.pdf");
        PdfExporter exporter = new PdfExporter();
        Iteration iteration = new Iteration();
        List<UserStory> stories = new ArrayList<UserStory>();
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(newTask("Task 1", "Description of Task 1", 2.0));
        tasks.add(newTask("Task 2", "Description of Task 2", 3.0));
        stories.add(newStory("Story 1", new Person(), 1.0, tasks));
        iteration.setUserStories(stories);
        exporter.exportToPdf(iteration, stream);
        stream.close();
    }

    private static UserStory newStory(String name, Person customer, double estimatedHours, List<Task> tasks) {
        UserStory story = new UserStory();
        story.setName(name);
        story.setCustomer(customer);
        story.setEstimatedHoursField(estimatedHours);
        story.setTasks(tasks);
        return story;
    }

    private static Task newTask(String name, String description, double estimatedHours) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setEstimatedHours(estimatedHours);
        return task;
    }
}
