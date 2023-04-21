package calendario;

import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/** Clase para mostrar el calendario.
 *
 * @author grupo InftelD 2009.
 */
public class CalendarWidget {

    static final String[] MONTH_LABELS = new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" };

    static final String[] WEEKDAY_LABELS = new String[] { "L", "M", "Mi", "J", "V", "S", "D" };

    public int startWeekday = 0;

    public int padding = 1;

    public int borderWidth = 4;

    public int borderColor = 0x5BABD0;

    public Font weekdayFont = Font.getDefaultFont();

    public int weekdayBgColor = 0x0000ff;

    public int weekdayColor = 0xffffff;

    public Font headerFont = Font.getDefaultFont();

    public int headerBgColor = 0x0000ff;

    public int headerColor = 0x5BABD0;

    public Font font = Font.getDefaultFont();

    public int foreColor = 0x000000;

    public int bgColor = 0x99CCFF;

    public int selectedBgColor = 0xCC3300;

    public int selectedForeColor = 0xfff000;

    int width = 0;

    int height = 0;

    int headerHeight = 0;

    int weekHeight = 0;

    int cellWidth = 0;

    int cellHeight = 0;

    long currentTimestamp = 0;

    Calendar calendar = null;

    int weeks = 0;

    /** Constructor
     *
     * @param date fecha seleccionada.
     */
    public CalendarWidget(Date date) {
        calendar = Calendar.getInstance();
        setDate(date);
        initialize();
    }

    /** getter para obtener la fecha seleccionada.
     *
     * @return Date
     */
    public Date getSelectedDate() {
        return calendar.getTime();
    }

    /** setter para introducir el tiempo actual, dado el dia.
     *
     * @param d
     */
    public void setDate(Date d) {
        currentTimestamp = d.getTime();
        calendar.setTime(d);
        this.weeks = (int) Math.ceil(((double) getStartWeekday() + getMonthDays()) / 7);
    }

    /** setter para introducir el tiempo actual, dado el dia en formato long.
     *
     * @param timestamp
     */
    public void setDate(long timestamp) {
        setDate(new Date(timestamp));
    }

    /** metodo para inicializar la clase.
     *
     */
    public void initialize() {
        this.cellWidth = font.stringWidth("MM") + 2 * padding;
        this.cellHeight = font.getHeight() + 2 * padding;
        this.headerHeight = headerFont.getHeight() + 2 * padding;
        this.weekHeight = weekdayFont.getHeight() + 2 * padding;
        this.width = 7 * (cellWidth + borderWidth) + borderWidth;
        initHeight();
    }

    void initHeight() {
        this.height = headerHeight + weekHeight + this.weeks * (cellHeight + borderWidth) + borderWidth;
    }

    int getMonthDays() {
        int month = calendar.get(Calendar.MONTH);
        switch(month) {
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1:
                return calendar.get(Calendar.YEAR) % 4 == 0 && calendar.get(Calendar.YEAR) % 100 != 0 ? 29 : 28;
            default:
                return 31;
        }
    }

    int getStartWeekday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        c.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        c.set(Calendar.DAY_OF_MONTH, 1);
        return (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;
    }

    /** Metodo que se ejecuta cuando pulsamos una tecla en el movil.
     ** Segun el boton pulsado realizamos una accion u otra.
     *
     * @param key indica la tecla que se pulsa.
     */
    public void keyPressed(int key) {
        switch(key) {
            case Canvas.UP:
                go(-7);
                break;
            case Canvas.DOWN:
                go(7);
                break;
            case Canvas.RIGHT:
                go(1);
                break;
            case Canvas.LEFT:
                go(-1);
                break;
        }
    }

    void go(int delta) {
        int prevMonth = calendar.get(Calendar.MONTH);
        setDate(currentTimestamp + 86400000 * delta);
        if (calendar.get(Calendar.MONTH) != prevMonth) {
            initHeight();
        }
    }

    /** Metodo para realizar la representacion grafica de la pantalla.
     *
     * @param g
     * @param ancho indica el ancho de la pantalla
     * @param alto indica el alto de la pantalla
     * 
     */
    public void paint(Graphics g, int ancho, int alto) {
        int posY = 65;
        int posX = (ancho / 2) - (width / 2);
        g.setColor(bgColor);
        g.fillRect(posX, posY, width, height);
        g.setFont(headerFont);
        g.setColor(headerColor);
        g.drawString(MONTH_LABELS[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR), (width / 2) + posX, (padding + posY), Graphics.TOP | Graphics.HCENTER);
        g.translate(posX, posY + headerHeight);
        g.setColor(weekdayBgColor);
        g.fillRect(0, 0, width, weekHeight);
        g.setColor(weekdayColor);
        g.setFont(weekdayFont);
        for (int i = 0; i < 7; i++) {
            g.drawString(WEEKDAY_LABELS[(i + startWeekday) % 7], borderWidth + i * (cellWidth + borderWidth) + cellWidth / 2, padding, Graphics.TOP | Graphics.HCENTER);
        }
        g.translate(0, weekHeight);
        g.setColor(borderColor);
        for (int i = 0; i <= weeks; i++) {
            g.fillRect(0, i * (cellHeight + borderWidth), width, borderWidth);
        }
        for (int i = 0; i <= 7; i++) {
            g.fillRect(i * (cellWidth + borderWidth), 0, borderWidth, height - headerHeight - weekHeight);
        }
        int days = getMonthDays();
        int dayIndex = (getStartWeekday() - this.startWeekday + 7) % 7;
        g.setColor(foreColor);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < days; i++) {
            int weekday = (dayIndex + i) % 7;
            int row = (dayIndex + i) / 7;
            int x = borderWidth + weekday * (cellWidth + borderWidth) + cellWidth / 2;
            int y = borderWidth + row * (cellHeight + borderWidth) + padding;
            if (i + 1 == currentDay) {
                g.setColor(selectedBgColor);
                g.fillRect(borderWidth + weekday * (cellWidth + borderWidth), borderWidth + row * (cellHeight + borderWidth), cellWidth, cellHeight);
                g.setColor(selectedForeColor);
            }
            g.drawString("" + (i + 1), x, y, Graphics.TOP | Graphics.HCENTER);
            if (i + 1 == currentDay) {
                g.setColor(foreColor);
            }
        }
        g.translate(0, -headerHeight - weekHeight);
    }
}
