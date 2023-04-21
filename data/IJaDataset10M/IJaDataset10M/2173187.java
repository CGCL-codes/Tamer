package entities;

import td.game.Main;
import td.game.MainGamePanel;
import util.Util;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;

/** Describes the panel on the left side of the screen.
 * This panel is responsible for showing information about the turrets/turret points clicked on the map.
 * 
 * @author Bruno Zumba (22/09/2011)
 *
 */
public class MapPanel {

    private final String TAG = MapPanel.class.getSimpleName();

    private final int WIDTH = (int) Math.round(Main.SCREEN_WIDTH * 0.3);

    private final int HEIGHT = (int) Math.round(Main.SCREEN_WIDTH * 0.9035);

    private final int BTN_WIDTH = 140;

    private final int BTN_HEIGHT = 50;

    private final int SEPARATE_BOX_WIDTH = 220;

    private final int SEPARATE_BOX_HEIGHT = 290;

    private final int SEPARATE_STICK_WIDTH = 226;

    private final int SEPARATE_STICK_HEIGHT = 17;

    private final Point SEPARATE_STICK_POINT = new Point(15, 130);

    public static final int CREATE_BUTTON_1_CLICKED = 1;

    public static final int CREATE_BUTTON_2_CLICKED = 2;

    public static final int SELL_BUTTON_CLICKED = 3;

    public static final int UPGRADE_BUTTON_CLICKED = 4;

    private Bitmap btnBkgBMP;

    private Bitmap separateBoxBMP;

    private Bitmap separateStickBMP;

    private Bitmap panelBMP;

    private Point pt;

    private Boolean touched;

    private Point last = new Point();

    private Point playerPt = new Point(15, 55);

    private TurretPoint selectedTp;

    private Turret selectedTurret;

    private String statusString;

    private Point status1Pt = new Point(25, 180);

    private Point status2Pt = new Point(25, 480);

    private Point button1 = new Point(58, 385);

    private Point button1CurrPos;

    private Point button2 = new Point(58, 685);

    private Point button2CurrPos;

    private TextPaint textPaint;

    private Boolean displayingTurret = null;

    private Boolean showingError;

    public MapPanel(Bitmap bmp, Point pt, Bitmap btnBkg, Bitmap separeteBox, Bitmap separateStick) {
        this.panelBMP = Util.resizeBitmap(bmp, WIDTH, HEIGHT);
        this.btnBkgBMP = Util.resizeBitmap(btnBkg, BTN_WIDTH, BTN_HEIGHT);
        this.separateBoxBMP = Util.resizeBitmap(separeteBox, SEPARATE_BOX_WIDTH, SEPARATE_BOX_HEIGHT);
        this.separateStickBMP = Util.resizeBitmap(separateStick, SEPARATE_STICK_WIDTH, SEPARATE_STICK_HEIGHT);
        this.showingError = false;
        this.pt = new Point(pt.x, pt.y);
        touched = false;
        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setFakeBoldText(true);
        textPaint.setTypeface(Main.getFont());
    }

    public Bitmap getBitmap() {
        return panelBMP;
    }

    public void setBitmap(Bitmap bmp) {
        this.panelBMP = bmp;
    }

    public Point getPoint() {
        return pt;
    }

    public void setPoint(Point pt) {
        this.pt = pt;
    }

    public Boolean isTouched() {
        return touched;
    }

    public void setTouched(Boolean touched) {
        this.touched = touched;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(panelBMP, pt.x, pt.y, null);
        int y = playerPt.y + this.pt.y;
        String plyr = new String("Player: " + MainGamePanel.getPlayer());
        textPaint.setUnderlineText(true);
        textPaint.setTextSize(MainGamePanel.density * 25f);
        canvas.drawText(plyr, panelBMP.getWidth() / 2 - textPaint.measureText(plyr) / 2, y, textPaint);
        textPaint.setUnderlineText(false);
        textPaint.setTextSize(MainGamePanel.density * 20f);
        if (showingError) {
            int errorY = playerPt.y + this.pt.y + 40;
            TextPaint errorPaint = new TextPaint();
            errorPaint.setColor(Color.WHITE);
            errorPaint.setTextSize(25);
            errorPaint.setTypeface(Main.getFont());
            String errorString = new String();
            displayingTurret = null;
            errorString = "Not enough money";
            canvas.drawText(errorString, status1Pt.x, errorY, errorPaint);
            errorY += 30;
            errorString = "to build/upgrade";
            canvas.drawText(errorString, status1Pt.x, errorY, errorPaint);
            errorY += 30;
            errorString = "the turret";
            canvas.drawText(errorString, status1Pt.x, errorY, errorPaint);
            errorY += 30;
        }
        if (displayingTurret == null) {
        } else if (displayingTurret == true) {
            if (selectedTurret.getPlayer() == MainGamePanel.getPlayer()) {
                canvas.drawBitmap(separateStickBMP, SEPARATE_STICK_POINT.x, SEPARATE_STICK_POINT.y + pt.y, null);
                String turrType = null;
                if (selectedTurret.getType() == Util.TURRET1) {
                    turrType = "Metralhadora";
                } else {
                    turrType = "Canh�o";
                }
                canvas.drawText(turrType, panelBMP.getWidth() / 2 - textPaint.measureText(turrType) / 2, SEPARATE_STICK_POINT.y - 10 + pt.y, textPaint);
                int level = selectedTurret.getLevel();
                button1CurrPos = new Point(button1.x, button1.y + pt.y);
                switch(selectedTurret.getType()) {
                    case Util.TURRET1:
                        createStatusPanel(canvas, Util.getTurret1Status(Util.POWER, level), Util.getTurret1Status(Util.FIRE_SPEED, level), Util.getTurret1Status(Util.REACH, level), Util.getTurret1Status(Util.COST, level), status1Pt.x, status1Pt.y + pt.y, "N�vel " + String.valueOf(level), "Vender", button1CurrPos);
                        break;
                    case Util.TURRET2:
                        createStatusPanel(canvas, Util.getTurret2Status(Util.POWER, level), Util.getTurret2Status(Util.FIRE_SPEED, level), Util.getTurret2Status(Util.REACH, level), Util.getTurret2Status(Util.COST, level), status1Pt.x, status1Pt.y + pt.y, "N�vel " + String.valueOf(level), "Vender", button1CurrPos);
                }
                if ((level < 3) && (selectedTurret != null)) {
                    switch(selectedTurret.getType()) {
                        case Util.TURRET1:
                            button2CurrPos = new Point(button2.x, button2.y + pt.y);
                            createStatusPanel(canvas, Util.getTurret1Status(Util.POWER, level + 1), Util.getTurret1Status(Util.FIRE_SPEED, level + 1), Util.getTurret1Status(Util.REACH, level + 1), Util.getTurret1Status(Util.COST, level + 1), status2Pt.x, status2Pt.y + pt.y, "N�vel " + String.valueOf(level + 1), "Evoluir", button2CurrPos);
                            break;
                        case Util.TURRET2:
                            button2CurrPos = new Point(button2.x, button2.y + pt.y);
                            createStatusPanel(canvas, Util.getTurret2Status(Util.POWER, level + 1), Util.getTurret2Status(Util.FIRE_SPEED, level + 1), Util.getTurret2Status(Util.REACH, level + 1), Util.getTurret2Status(Util.COST, level + 1), status2Pt.x, status2Pt.y + pt.y, "N�vel " + String.valueOf(level + 1), "Evoluir", button2CurrPos);
                    }
                }
            }
        } else if (displayingTurret == false) {
            if (selectedTp.getPlayer() == MainGamePanel.getPlayer()) {
                button1CurrPos = new Point(button1.x, button1.y + pt.y);
                createStatusPanel(canvas, Util.getTurret1Status(Util.POWER, 1), Util.getTurret1Status(Util.FIRE_SPEED, 1), Util.getTurret1Status(Util.REACH, 1), Util.getTurret1Status(Util.COST, 1), status1Pt.x, status1Pt.y + pt.y, "Metralhadora", "Criar", button1CurrPos);
                button2CurrPos = new Point(button2.x, button2.y + pt.y);
                createStatusPanel(canvas, Util.getTurret2Status(Util.POWER, 1), Util.getTurret2Status(Util.FIRE_SPEED, 1), Util.getTurret2Status(Util.REACH, 1), Util.getTurret2Status(Util.COST, 1), status2Pt.x, status2Pt.y + pt.y, "Canh�o", "Criar", button2CurrPos);
            }
        }
    }

    private void createStatusPanel(Canvas canvas, float power, float speed, float reach, float cost, int x, int y, String title, String buttonText, Point buttonPos) {
        canvas.drawBitmap(separateBoxBMP, x - 10, y - 30, null);
        y += 30;
        statusString = new String(title);
        canvas.drawText(statusString, panelBMP.getWidth() / 2 - textPaint.measureText(title) / 2, y, textPaint);
        y += 40;
        statusString = new String("Ataque: " + power);
        canvas.drawText(statusString, x, y, textPaint);
        y += 40;
        statusString = "Velocidade: " + speed;
        canvas.drawText(statusString, x, y, textPaint);
        y += 40;
        statusString = "Alcance: " + reach;
        canvas.drawText(statusString, x, y, textPaint);
        y += 40;
        statusString = "Pre�o: " + cost;
        canvas.drawText(statusString, x, y, textPaint);
        y += 40;
        canvas.drawBitmap(btnBkgBMP, buttonPos.x, buttonPos.y, null);
        canvas.drawText(buttonText, panelBMP.getWidth() / 2 - textPaint.measureText(buttonText) / 2, buttonPos.y + btnBkgBMP.getHeight() - 15, textPaint);
    }

    public void setShowingError(Boolean showingError) {
        if (showingError) {
            pt.y = 0;
        }
        this.showingError = showingError;
    }

    /** Update the information shown on the panel
	 * 
	 */
    public void updateInfo(Turret turret) {
        showingError = false;
        displayingTurret = true;
        selectedTurret = turret;
        Log.i(TAG, "Updateando as informa��es com um turret no y: " + turret.getPoint().y);
    }

    public void updateInfo(TurretPoint tp) {
        showingError = false;
        displayingTurret = false;
        selectedTp = tp;
        Log.i(TAG, "Updateando as informa��es com um turret point no y: " + tp.getPoint().y);
    }

    public void updateInfo() {
        displayingTurret = null;
        selectedTp = null;
        selectedTurret = null;
        Log.i(TAG, "Setou como null");
    }

    public int handleActionDown(MotionEvent ev) {
        switch(ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (displayingTurret == null) {
                } else if ((displayingTurret == true)) {
                    if (selectedTurret.getPlayer() == MainGamePanel.getPlayer()) {
                        if ((button2CurrPos != null) && (ev.getX() >= button2CurrPos.x) && (ev.getX() <= button2CurrPos.x + btnBkgBMP.getWidth())) {
                            if (ev.getY() >= (button2CurrPos.y) && (ev.getY() <= (button2CurrPos.y + btnBkgBMP.getHeight()))) {
                                Log.i(TAG, "Clicou no bot�o Upgrade");
                                return UPGRADE_BUTTON_CLICKED;
                            }
                        }
                        if ((ev.getX() >= button1CurrPos.x) && (ev.getX() <= button1CurrPos.x + btnBkgBMP.getWidth())) {
                            if (ev.getY() >= (button1CurrPos.y) && (ev.getY() <= button1CurrPos.y + btnBkgBMP.getHeight())) {
                                Log.i(TAG, "Clicou no bot�o Sell");
                                return SELL_BUTTON_CLICKED;
                            }
                        }
                    } else {
                    }
                } else if ((displayingTurret == false)) {
                    if (selectedTp.getPlayer() == MainGamePanel.getPlayer()) {
                        if ((ev.getX() >= button1CurrPos.x) && (ev.getX() <= button1CurrPos.x + btnBkgBMP.getWidth())) {
                            if ((ev.getY() >= button1CurrPos.y) && (ev.getY() <= button1CurrPos.y + btnBkgBMP.getHeight())) {
                                Log.i(TAG, "Clicou no bot�o 1");
                                return CREATE_BUTTON_1_CLICKED;
                            }
                        }
                        if ((ev.getX() >= button2CurrPos.x) && (ev.getX() <= button2CurrPos.x + btnBkgBMP.getWidth())) {
                            if ((ev.getY() >= button2CurrPos.y) && (ev.getY() <= button2CurrPos.y + btnBkgBMP.getHeight())) {
                                Log.i(TAG, "Clicou no bot�o 2");
                                return CREATE_BUTTON_2_CLICKED;
                            }
                        }
                    } else {
                        Log.i(TAG, "O turret (" + selectedTp.getTile().x + ", " + selectedTp.getTile().y + ") n�o te pertence");
                    }
                }
                if (ev.getX() >= (pt.x) && (ev.getX() <= (pt.x + panelBMP.getWidth()))) {
                    if (ev.getY() >= (pt.y) && (ev.getY() <= (pt.y + panelBMP.getHeight()))) {
                        setTouched(true);
                        last.y = (int) ev.getY();
                    } else {
                        setTouched(false);
                    }
                } else {
                    setTouched(false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (touched) {
                    pt.y += ((int) ev.getY() - last.y);
                    last.y = (int) ev.getY();
                    if (pt.y > MainGamePanel.MAP_PANEL_RECT.top) {
                        pt.y = MainGamePanel.MAP_PANEL_RECT.top;
                    } else if (pt.y + panelBMP.getHeight() < MainGamePanel.MAP_PANEL_RECT.bottom - 75) {
                        pt.y = MainGamePanel.MAP_PANEL_RECT.bottom - panelBMP.getHeight() - 75;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touched) {
                    touched = false;
                }
                break;
        }
        return 0;
    }
}
