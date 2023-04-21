package gui;

import interfaces.ProfessoreInterface;
import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.faceless.pdf2.PDF;
import org.faceless.pdf2.PDFReader;
import org.faceless.pdf2.viewer2.DocumentPanel;
import org.faceless.pdf2.viewer2.feature.AnnotationLinkFactory;
import org.faceless.pdf2.viewer2.feature.GoToActionHandler;

public class MainWindowStud extends JFrame {

    private static final long serialVersionUID = 1L;

    static DocumentPanel panel = new DocumentPanel();

    private Container content = new JPanel(new BorderLayout());

    private MenuBarStud menu = new MenuBarStud(this);

    private static int page = 0;

    private static PDF pdf;

    private static JLabel richieste = new JLabel("      Nessuna richiesta in attesa");

    private ProfessoreInterface prof;

    public MainWindowStud(File file, ProfessoreInterface prof) throws IOException {
        super("VirtualBeamer - Edizione Insegnante");
        this.prof = prof;
        pdf = new PDF(new PDFReader(file));
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.addAnnotationComponentFactory(AnnotationLinkFactory.getInstance());
        panel.addActionHandler(GoToActionHandler.getInstance());
        setContentPane(content);
        panel.setPDF(pdf);
        panel.setPageNumber(page);
        MenuBarStud.init(pdf.getNumberOfPages());
        content.add(menu, BorderLayout.NORTH);
        content.add(panel, BorderLayout.CENTER);
        content.add(richieste, BorderLayout.SOUTH);
    }

    public void nextPage() {
        page++;
        prof.r_cambiaPagina(page + 1);
    }

    public void prevPage() {
        page--;
        prof.r_cambiaPagina(page + 1);
    }

    public void gotoPage(int pagnum) {
        pagnum--;
        page = pagnum;
        prof.r_cambiaPagina(page + 1);
    }

    public static Integer getPageNum() {
        return page + 1;
    }

    public static boolean isLastPage() {
        if (page == pdf.getNumberOfPages() - 1) return true; else return false;
    }

    public static void setPageNoPubbl(int pagnum) {
        panel.setPageNumber(--pagnum);
        page = pagnum;
        MenuBarStud.setTextPagNum(pagnum + 1);
    }

    public static void setRichiesta(String string) {
        richieste.setText("      " + string);
    }
}
