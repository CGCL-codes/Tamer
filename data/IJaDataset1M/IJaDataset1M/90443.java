package br.com.visualmidia.ui.report;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRImageRenderer;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintLine;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintRectangle;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBasePrintImage;
import net.sf.jasperreports.engine.base.JRBasePrintLine;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintRectangle;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.util.JRImageLoader;
import br.com.visualmidia.GD;
import br.com.visualmidia.business.Course;
import br.com.visualmidia.business.GDDate;
import br.com.visualmidia.core.Constants;
import br.com.visualmidia.persistence.GetCourse;
import br.com.visualmidia.system.GDSystem;
import br.com.visualmidia.tools.SortComparator;

/**
 * @author  Lucas
 */
public class RegistrationFicheReport {

    private static GDSystem system = GDSystem.getInstance();

    private List<Integer> numberOfVacancy = new ArrayList<Integer>();

    private int startWorkTime;

    private int endWorkTime;

    private int break1WorkTime;

    private int break2WorkTime;

    private int breakDurationWorkTime;

    private JRDesignStyle boldStyle;

    private JRDesignStyle normalStyle;

    private JRBasePrintText text;

    private JasperPrint jasperPrint;

    private JRBasePrintRectangle rectangle;

    private JRDesignStyle miniStyle;

    private List<String> workDays;

    public RegistrationFicheReport() {
        List<String> period = new ArrayList<String>();
        period.add("Manh�");
        period.add("Tarde");
        period.add("Noite");
        GD gd = GD.getInstance();
        this.startWorkTime = gd.getWorktime("start");
        this.endWorkTime = gd.getWorktime("end");
        this.break1WorkTime = gd.getWorktime("break1");
        this.break2WorkTime = gd.getWorktime("break2");
        this.breakDurationWorkTime = gd.getWorktime("breakDuration");
        this.workDays = gd.getWorkDays();
        setNumberOfVacancy();
    }

    private void setNumberOfVacancy() {
        if (break1WorkTime != 0) {
            for (int i = break1WorkTime; i < break1WorkTime + breakDurationWorkTime; i++) {
                if (breakDurationWorkTime != 0) {
                    numberOfVacancy.add(i);
                }
            }
        }
        if (break2WorkTime != 0) {
            for (int i = break2WorkTime; i < break2WorkTime + breakDurationWorkTime; i++) {
                if (breakDurationWorkTime != 0) {
                    numberOfVacancy.add(i);
                }
            }
        }
        for (int i = 0; i < 25; i++) {
            if (i < startWorkTime || i > endWorkTime) {
                numberOfVacancy.add(i);
            }
        }
    }

    /**
	 * @return
	 * @throws JRException
	 * @uml.property  name="jasperPrint"
	 */
    @SuppressWarnings("unchecked")
    public JasperPrint getJasperPrint() throws JRException {
        jasperPrint = new JasperPrint();
        jasperPrint.setName("NoReport");
        jasperPrint.setOrientation(JasperReport.ORIENTATION_PORTRAIT);
        jasperPrint.setPageWidth(convert(210));
        jasperPrint.setPageHeight(convert(297));
        JRDesignStyle titleStyle = new JRDesignStyle();
        titleStyle.setName("Arial_Title");
        titleStyle.setDefault(true);
        titleStyle.setFontName("Arial");
        titleStyle.setFontSize(18);
        titleStyle.setBold(true);
        titleStyle.setPdfFontName("Helvetica");
        titleStyle.setPdfEncoding("Cp1252");
        titleStyle.setPdfEmbedded(false);
        jasperPrint.addStyle(titleStyle);
        boldStyle = new JRDesignStyle();
        boldStyle.setName("Arial_Bold");
        boldStyle.setDefault(true);
        boldStyle.setFontName("Arial");
        boldStyle.setFontSize(12);
        boldStyle.setBold(true);
        boldStyle.setMode(JRElement.MODE_OPAQUE);
        boldStyle.setPdfFontName("Helvetica");
        boldStyle.setPdfEncoding("Cp1252");
        boldStyle.setPdfEmbedded(false);
        jasperPrint.addStyle(boldStyle);
        normalStyle = new JRDesignStyle();
        normalStyle.setName("Arial_Normal");
        normalStyle.setDefault(true);
        normalStyle.setFontName("Arial");
        normalStyle.setFontSize(10);
        normalStyle.setPdfFontName("Helvetica");
        normalStyle.setPdfEncoding("Cp1252");
        normalStyle.setPdfEmbedded(false);
        jasperPrint.addStyle(normalStyle);
        miniStyle = new JRDesignStyle();
        miniStyle.setName("Arial_Mini");
        miniStyle.setDefault(true);
        miniStyle.setFontName("Arial");
        miniStyle.setFontSize(8);
        miniStyle.setPdfFontName("Helvetica");
        miniStyle.setPdfEncoding("Cp1252");
        miniStyle.setPdfEmbedded(false);
        jasperPrint.addStyle(miniStyle);
        try {
            int posX = convert(10);
            int posY = convert(7);
            JRPrintPage page = new JRBasePrintPage();
            page.addElement(printImage("titleReport.gif", posX, posY, 535, 45));
            page.addElement(printImage("corporateLogo.jpg", posX + 11, posY, 65, 45));
            text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
            text.setText("FICHA DE CADASTRO");
            text.setX(posX + 251);
            text.setY(posY + 11);
            text.setWidth(200);
            text.setHeight(22);
            text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_RIGHT);
            text.setLineSpacingFactor(2.0f);
            text.setLeadingOffset(-4.0f);
            text.setStyle(titleStyle);
            page.addElement(text);
            posY += 45;
            page.addElement(printTitleText("Dados Pessoais", posX + 2, posY + 3, 98));
            page.addElement(printNormalText("Nome:", posX + 2, posY + 19, 31));
            page.addElement(printRectangle(posX + 36, posY + 19, 412, 20, 3));
            rectangle = new JRBasePrintRectangle(jasperPrint.getDefaultStyleProvider());
            rectangle.setX(posX + 478);
            rectangle.setY(posY + 19);
            rectangle.setWidth(56);
            rectangle.setHeight(13);
            rectangle.setRadius(3);
            rectangle.setBackcolor(new Color(204, 204, 204));
            page.addElement(rectangle);
            page.addElement(printRectangle(posX + 484, posY + 21, 8, 8, 0));
            page.addElement(printRectangle(posX + 511, posY + 21, 8, 8, 0));
            page.addElement(printNormalText("Sexo:        M       F", posX + 450, posY + 19, 83));
            page.addElement(printRectangle(posX + 36, posY + 43, 96, 13, 3));
            page.addElement(printNormalText("Nasc.:          /        /", posX + 2, posY + 43, 88));
            page.addElement(printNormalText("Nacionalidade:", posX + 136, posY + 43, 65));
            page.addElement(printRectangle(posX + 203, posY + 43, 135, 13, 3));
            page.addElement(printNormalText("Estado Civil:", posX + 341, posY + 43, 56));
            page.addElement(printRectangle(posX + 400, posY + 43, 135, 13, 3));
            page.addElement(printNormalText("CPF:", posX + 2, posY + 60, 32));
            page.addElement(printRectangle(posX + 36, posY + 60, 132, 13, 3));
            page.addElement(printNormalText("RG:", posX + 171, posY + 60, 20));
            page.addElement(printRectangle(posX + 193, posY + 60, 132, 13, 3));
            page.addElement(printNormalText("E-mail:", posX + 328, posY + 60, 32));
            page.addElement(printRectangle(posX + 362, posY + 60, 172, 13, 3));
            page.addElement(printNormalText("Endere�o:", posX + 2, posY + 77, 47));
            page.addElement(printRectangle(posX + 51, posY + 77, 483, 13, 3));
            page.addElement(printNormalText("Compl.:", posX + 2, posY + 94, 36));
            page.addElement(printRectangle(posX + 40, posY + 94, 115, 13, 3));
            page.addElement(printNormalText("CEP:", posX + 158, posY + 94, 25));
            page.addElement(printRectangle(posX + 184, posY + 94, 96, 13, 3));
            page.addElement(printNormalText("Cidade:", posX + 283, posY + 94, 35));
            page.addElement(printRectangle(posX + 320, posY + 94, 166, 13, 3));
            page.addElement(printNormalText("UF:", posX + 489, posY + 94, 17));
            page.addElement(printRectangle(posX + 508, posY + 94, 26, 13, 3));
            page.addElement(printNormalText("Telefone Res.:", posX + 2, posY + 111, 68));
            page.addElement(printRectangle(posX + 72, posY + 111, 304, 13, 3));
            page.addElement(printNormalText("M�vel:", posX + 379, posY + 111, 31));
            page.addElement(printRectangle(posX + 412, posY + 111, 122, 13, 3));
            page.addElement(printTitleText("Dados Adicionais", posX + 2, posY + 128, 106));
            page.addElement(printRectangle(posX + 2, posY + 148, 6, 6, 0));
            page.addElement(printMiniText("Fund. Incompleto", posX + 11, posY + 146, 74));
            page.addElement(printRectangle(posX + 2, posY + 157, 6, 6, 0));
            page.addElement(printMiniText("M�dio Incompleto", posX + 11, posY + 155, 74));
            page.addElement(printRectangle(posX + 2, posY + 166, 6, 6, 0));
            page.addElement(printMiniText("Superior Imcompleto", posX + 11, posY + 164, 74));
            page.addElement(printRectangle(posX + 92, posY + 148, 6, 6, 0));
            page.addElement(printMiniText("Fund. Completo", posX + 101, posY + 146, 68));
            page.addElement(printRectangle(posX + 92, posY + 157, 6, 6, 0));
            page.addElement(printMiniText("M�dio Completo", posX + 101, posY + 155, 68));
            page.addElement(printRectangle(posX + 92, posY + 166, 6, 6, 0));
            page.addElement(printMiniText("Superior", posX + 101, posY + 164, 68));
            page.addElement(printNormalText("Escola:", posX + 176, posY + 146, 34));
            page.addElement(printRectangle(posX + 212, posY + 146, 322, 13, 3));
            page.addElement(printNormalText("Empresa:", posX + 176, posY + 163, 44));
            page.addElement(printRectangle(posX + 222, posY + 163, 208, 13, 3));
            page.addElement(printNormalText("Tel.:", posX + 433, posY + 163, 20));
            page.addElement(printRectangle(posX + 455, posY + 163, 79, 13, 3));
            page.addElement(printTitleText("Dados Respons�vel", posX + 2, posY + 177, 119));
            page.addElement(printNormalText("Nome:", posX + 2, posY + 194, 29));
            page.addElement(printRectangle(posX + 33, posY + 194, 397, 20, 3));
            page.addElement(printNormalText("Tel.:", posX + 433, posY + 194, 20));
            page.addElement(printRectangle(posX + 455, posY + 194, 79, 13, 3));
            page.addElement(printNormalText("Parentesco:", posX + 2, posY + 218, 54));
            page.addElement(printRectangle(posX + 58, posY + 218, 157, 13, 3));
            page.addElement(printNormalText("CPF:", posX + 219, posY + 218, 24));
            page.addElement(printRectangle(posX + 245, posY + 218, 132, 13, 3));
            page.addElement(printNormalText("RG:", posX + 381, posY + 218, 19));
            page.addElement(printRectangle(posX + 402, posY + 218, 132, 13, 3));
            page.addElement(printTitleText("Pesquisa", posX + 2, posY + 238, 58));
            page.addElement(printNormalText("Como Conheceu:", posX + 2, posY + 257, 77));
            page.addElement(printRectangle(posX + 81, posY + 257, 258, 13, 3));
            page.addElement(printNormalText("Motivo:", posX + 342, posY + 257, 33));
            page.addElement(printRectangle(posX + 377, posY + 257, 157, 13, 3));
            page.addElement(printNormalText("Atendido Por:", posX + 2, posY + 274, 77));
            page.addElement(printRectangle(posX + 81, posY + 274, 258, 13, 3));
            page.addElement(printRectangle(posX + 440, posY + 274, 94, 13, 3));
            page.addElement(printNormalText("Data do Atendimento:        /        /", posX + 342, posY + 274, 154));
            page.addElement(printTitleText("Cursos", posX + 2, posY + 292, 47));
            posY += 311;
            Map<String, Course> courses = (Map<String, Course>) system.query(new GetCourse(null));
            List<Course> coursesList = new ArrayList<Course>(courses.values());
            SortComparator comparator = new SortComparator();
            Collections.sort(coursesList, comparator);
            int cont = 0;
            for (int i = 0; i < coursesList.size(); i++) {
                if (coursesList.get(i).isActive()) {
                    page.addElement(printRectangle(posX + Math.round((535 / 3) * (cont % 3)) + 6, posY + 3, 6, 6, 0));
                    page.addElement(printNormalText(coursesList.get(i).getDescription(), posX + Math.round((535 / 3) * (cont % 3)) + 16, posY, 162));
                    if (cont % 3 == 2 || i == coursesList.size() - 1) {
                        posY += 13;
                    }
                    cont++;
                }
            }
            page.addElement(printRectangle(posX + 2, posY + 10, 188, 100, 5));
            page.addElement(printTitleText("  Financeiro:", posX + 16, posY + 2, 74));
            page.addElement(printNormalText("Valor da Mens.:", posX + 8, posY + 20, 90));
            page.addElement(printNormalText("R$ ________,____", posX + 100, posY + 20, 87));
            page.addElement(printNormalText("Desc. Pontualidade:", posX + 8, posY + 32, 90));
            page.addElement(printNormalText("R$ ________,____", posX + 100, posY + 32, 87));
            page.addElement(printNormalText("VALOR TOTAL:", posX + 8, posY + 52, 90));
            page.addElement(printNormalText("R$ ________,____", posX + 100, posY + 52, 87));
            page.addElement(printNormalText("N� de parcelas: ______", posX + 78, posY + 74, 106));
            page.addElement(printNormalText("Data da 1� parcela: ____/____/_______", posX + 4, posY + 91, 181));
            int totalOfHours = endWorkTime - startWorkTime - getHourVacancy(startWorkTime, endWorkTime);
            page.addElement(printRectangle(posX + 195, posY + 9, 337, 40 + ((totalOfHours + 1) * 10), 5));
            page.addElement(printTitleText("  Dia/Hor�rio:", posX + 439, posY + 2, 80));
            int width = Math.round(330 / (workDays.size() + 1));
            for (int i = 0; i < workDays.size(); i++) {
                page.addElement(printMiniText(workDays.get(i), posX + (width * (i % (workDays.size() + 1))) + 200 + width, posY + 19, width));
            }
            int posYTemp = 0;
            for (int i = 0; i < workDays.size() + 1; i++) {
                posYTemp = posY + 29;
                for (int j = 0; j < 25; j++) {
                    if (!isHourVacancy(j)) {
                        page.addElement(printRectangle(posX + (width * (i % (workDays.size() + 1))) + 199, posYTemp, width, 10, 0));
                        if (i == 0) {
                            page.addElement(printMiniText(j + ":00", posX + (width * (i % (workDays.size() + 1))) + 205, posYTemp + 1, width));
                        }
                        posYTemp += 10;
                    }
                }
            }
            posY = posYTemp;
            page.addElement(printNormalText("In�cio do Curso: ____/_____/________.", posX + 346, posY + 4, 181));
            JRPrintLine line = new JRBasePrintLine(jasperPrint.getDefaultStyleProvider());
            line.setX(posX + 2);
            line.setY(convert(290) - 40);
            line.setWidth(188);
            line.setHeight(0);
            page.addElement(line);
            page.addElement(printNormalText("Assinatura do Aluno / Respons�vel", posX + 12, convert(290) - 40, 169));
            page.addElement(printMiniText("Observa��es no verso.", posX + 451, convert(290) - 30, 90));
            page.addElement(printMiniText("Criado por Gerente Digital.", posX + 190, convert(290) - 10, 157));
            jasperPrint.addPage(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperPrint;
    }

    private JRPrintRectangle printRectangle(int posX, int posY, int width, int height, int radius) {
        rectangle = new JRBasePrintRectangle(jasperPrint.getDefaultStyleProvider());
        rectangle.setX(posX);
        rectangle.setY(posY);
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        rectangle.setRadius(radius);
        return rectangle;
    }

    private JRPrintText printNormalText(String textToWrite, int posX, int posY, int width) {
        text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
        text.setText(textToWrite);
        text.setX(posX);
        text.setY(posY);
        text.setWidth(width);
        text.setHeight(12);
        text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
        text.setLineSpacingFactor(2.0f);
        text.setLeadingOffset(-4.0f);
        text.setStyle(normalStyle);
        return text;
    }

    private JRPrintText printMiniText(String textToWrite, int posX, int posY, int width) {
        text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
        text.setText(textToWrite);
        text.setX(posX);
        text.setY(posY);
        text.setWidth(width);
        text.setHeight(10);
        text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
        text.setLineSpacingFactor(2.0f);
        text.setLeadingOffset(-4.0f);
        text.setStyle(miniStyle);
        return text;
    }

    private JRPrintElement printImage(String imageName, int posX, int posY, int width, int height) {
        JRBasePrintImage image = new JRBasePrintImage(jasperPrint.getDefaultStyleProvider());
        image.setX(posX);
        image.setY(posY);
        image.setWidth(width);
        image.setHeight(height);
        image.setScaleImage(JRImage.SCALE_IMAGE_FILL_FRAME);
        try {
            File photoPath = new File(Constants.PHOTO_DIR + imageName);
            if (photoPath.exists()) {
                image.setRenderer(JRImageRenderer.getInstance(JRImageLoader.loadImageDataFromLocation(Constants.PHOTO_DIR + imageName)));
            }
        } catch (JRException e) {
            e.printStackTrace();
        }
        return image;
    }

    private JRPrintText printTitleText(String textToWrite, int posX, int posY, int width) {
        text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
        text.setText(textToWrite);
        text.setX(posX);
        text.setY(posY);
        text.setWidth(width);
        text.setHeight(14);
        text.setHorizontalAlignment(JRAlignment.HORIZONTAL_ALIGN_LEFT);
        text.setLineSpacingFactor(2.0f);
        text.setLeadingOffset(-4.0f);
        text.setStyle(boldStyle);
        return text;
    }

    private int getHourVacancy(int startHour, int endHour) {
        int cont = 0;
        for (int i = 0; i < numberOfVacancy.size(); i++) {
            if (numberOfVacancy.get(i) >= startHour && numberOfVacancy.get(i) <= endHour) {
                cont++;
            }
        }
        return cont;
    }

    private boolean isHourVacancy(int hour) {
        for (int i = 0; i < numberOfVacancy.size(); i++) {
            if (numberOfVacancy.get(i) == hour) {
                return true;
            }
        }
        return false;
    }

    public List<String> createListDate() {
        final List<String> listDate = new ArrayList<String>();
        GDDate startDate = new GDDate();
        startDate.addDays((1 - startDate.getWeekDay()));
        for (int i = 0; i < 7; i++) {
            startDate.addDays(1);
            listDate.add(startDate.getFormatedDate());
        }
        return listDate;
    }

    public List<String> createListDate(final String inicialDateText, final String endDateText) {
        GDDate inicialDate = new GDDate(inicialDateText);
        GDDate endDate = new GDDate(endDateText);
        List<String> listDate = new ArrayList<String>();
        for (; inicialDate.beforeOrEqualsDay(endDate); inicialDate.addDays(1)) {
            listDate.add(inicialDate.getFormatedDate());
        }
        return listDate;
    }

    private int convert(float milimeters) {
        return Math.round(milimeters * 2.85f);
    }
}
