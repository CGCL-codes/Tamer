package ATMFinder;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.*;
import net.rim.device.api.collection.util.*;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.compress.GZIPInputStream;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.system.EncodedImage;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.Connector;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import ATMFinder.resource.ATMFinderResource;
import com.field.Button;
import com.field.HeaderBar;
import com.field.ATMBTField;
import com.mapping.BANK;
import com.mapping.CITY;
import com.mapping.DIST;
import conf.GlobalCfg;

public class ATMFinder extends UiApplication {

    public ATMFinder() {
        pushScreen(new ATMFinder_MainScreen());
    }

    public static void main(String[] args) {
        new ATMFinder().enterEventDispatcher();
    }
}

final class ATMFinder_FontLab {

    private String _fontname;

    public ATMFinder_FontLab(String fontname) {
        _fontname = fontname;
    }

    /**
     * ReLoadFont
     * Set font for application
     * @return  Font
     * @param   style (int)     - Style of font
     * @param   height (it)     - Font height
     */
    public Font ReLoadFont(int style, int height) {
        try {
            FontFamily theFam = FontFamily.forName(_fontname);
            return theFam.getFont(style, height);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

final class ATMFinder_SplashScreen extends MainScreen implements ATMFinderResource {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private static final Bitmap _img_splashscreen = Bitmap.getBitmapResource("splashscreen.gif");

    private Timer _timer = new Timer();

    public boolean _Splash_Running = true;

    private class CountDown extends TimerTask {

        public void run() {
            DismissThread dThread = new DismissThread();
            UiApplication.getUiApplication().invokeLater(dThread);
        }
    }

    private class DismissThread implements Runnable {

        public void run() {
            Close_SplashScreen();
        }
    }

    /**
     * ATMFinder_SplashScreen
     * Init time schedule show if splashscreen
     * @return  void
     * @param   _splashscreen (boolean)     - If true show splashscreen else show About
     */
    public ATMFinder_SplashScreen(boolean _splashscreen) {
        super(DEFAULT_MENU | DEFAULT_CLOSE);
        if (_splashscreen) _timer.schedule(new CountDown(), 1000);
    }

    /**
     * Close_SplashScreen
     * Close splashscreen if time out
     * @return  void
     * @param   void
     */
    private void Close_SplashScreen() {
        _timer.cancel();
        UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
        _Splash_Running = false;
    }

    /**
     * paint - is super.paint
     * Paint this screen
     * @return  void
     * @param   g (Graphics)    - Graphics base object
     */
    public void paint(Graphics g) {
        super.paint(g);
        g.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawBitmap((this.getWidth() / 2) - (_img_splashscreen.getWidth() / 2), (this.getHeight() / 2) - (_img_splashscreen.getHeight() / 2) - 20, _img_splashscreen.getWidth(), _img_splashscreen.getHeight(), _img_splashscreen, 0, 0);
        g.setColor(Color.BLACK);
        g.drawText(_resources.getString(ATMFINDER_VERSION), 0, (this.getHeight() / 2) + (_img_splashscreen.getHeight() / 2) - 10, DrawStyle.HCENTER, this.getWidth());
        g.drawText(_resources.getString(ATMFINDER_PRESSKEY), 0, (this.getHeight() / 2) + (_img_splashscreen.getHeight() / 2) + 10, DrawStyle.HCENTER, this.getWidth());
        g.drawText(_resources.getString(ATMFINDER_AUTHOR_EMAIL), 0, (this.getHeight() / 2) + (_img_splashscreen.getHeight() / 2) + 30, DrawStyle.HCENTER, this.getWidth());
    }
}

final class ATMFinder_MainScreen extends MainScreen implements ATMFinderResource {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private static final Bitmap icon_atmfinder = Bitmap.getBitmapResource("icon_atmfinder.png");

    private static final Bitmap icon_config = Bitmap.getBitmapResource("icon_config.png");

    private static final Bitmap icon_search = Bitmap.getBitmapResource("icon_search.png");

    private static final Bitmap icon_update = Bitmap.getBitmapResource("icon_update.png");

    private static final Bitmap icon_atmfinder_off = Bitmap.getBitmapResource("icon_atmfinder_off.png");

    private static final Bitmap icon_config_off = Bitmap.getBitmapResource("icon_config_off.png");

    private static final Bitmap icon_search_off = Bitmap.getBitmapResource("icon_search_off.png");

    private static final Bitmap icon_update_off = Bitmap.getBitmapResource("icon_update_off.png");

    private static final Bitmap _background = Bitmap.getBitmapResource("bg_2.jpg");

    private static int _menu_option = 3;

    private ATMFinder_SplashScreen _ATMFinder_SplashScreen = new ATMFinder_SplashScreen(true);

    private static GlobalCfg _GlobalCfg = new GlobalCfg();

    public ATMFinder_MainScreen() {
        UiApplication.getUiApplication().invokeLater(new Runnable() {

            public void run() {
                UiApplication.getUiApplication().pushScreen(_ATMFinder_SplashScreen);
            }
        });
        _GlobalCfg.LoadConfig();
        HeaderBar _HeaderBar = new HeaderBar(_resources.getString(ATMFinderResource.ATMFINDER_CAPTION));
        _HeaderBar.showBattery(false);
        _HeaderBar.showSignal(false);
        _HeaderBar.setBackgroundColour(0xEFEFEF);
        _HeaderBar.setBatteryBackground(0xEE1155);
        _HeaderBar.setFontColour(0xFFFFFF);
        setTitle(_HeaderBar);
    }

    /**
     * paint - is super.paint
     * Paint this screen
     * @return  void
     * @param   g (Graphics)    - Graphics base object
     */
    public void paint(Graphics g) {
        super.paint(g);
        g.drawBitmap(0, 18, _background.getWidth(), _background.getHeight(), _background, 0, 0);
        g.drawBitmap((this.getWidth() / 2) - (icon_atmfinder.getWidth() / 2), 30, icon_atmfinder.getWidth(), icon_atmfinder.getHeight(), _menu_option == 1 ? icon_atmfinder_off : _menu_option == 2 ? icon_update_off : _menu_option == 3 ? icon_search_off : icon_config_off, 0, 0);
        g.drawBitmap((this.getWidth() / 2) - (icon_search.getWidth() / 2), this.getHeight() - icon_search.getHeight() - 20, icon_search.getWidth(), icon_search.getHeight(), _menu_option == 1 ? icon_search : _menu_option == 2 ? icon_config : _menu_option == 3 ? icon_atmfinder : icon_update, 0, 0);
        g.drawBitmap(15, (this.getHeight() / 2) - (icon_search.getHeight() / 2), icon_config.getWidth(), icon_config.getHeight(), _menu_option == 1 ? icon_config_off : _menu_option == 2 ? icon_atmfinder_off : _menu_option == 3 ? icon_update_off : icon_search_off, 0, 0);
        g.drawBitmap((this.getWidth()) - (icon_update.getWidth() + 15), (this.getHeight() / 2) - (icon_update.getHeight() / 2), icon_update.getWidth(), icon_update.getHeight(), _menu_option == 1 ? icon_update_off : _menu_option == 2 ? icon_search_off : _menu_option == 3 ? icon_config_off : icon_atmfinder_off, 0, 0);
        g.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.APPLICATION, 14));
        g.setColor(Color.WHITE);
        g.drawText(_resources.getString(_menu_option == 1 ? ATMFinderResource.ATMFINDER_MENU_ITEM2 : _menu_option == 2 ? ATMFinderResource.ATMFINDER_MENU_ITEM3 : _menu_option == 3 ? ATMFinderResource.ATMFINDER_MENU_ITEM1 : ATMFinderResource.ATMFINDER_MENU_ITEM4), 0, this.getHeight() - 20, DrawStyle.HCENTER, this.getWidth());
    }

    public boolean trackwheelRoll(int amount, int status, int time) {
        if (amount == 1) {
            this._menu_option -= 1;
            if (this._menu_option < 1) this._menu_option = 4;
        } else {
            this._menu_option += 1;
            if (this._menu_option > 4) this._menu_option = 1;
        }
        UiApplication.getUiApplication().repaint();
        return false;
    }

    public boolean trackwheelClick(int status, int time) {
        if (_ATMFinder_SplashScreen._Splash_Running) return false;
        switch(_menu_option) {
            case 1:
                UiApplication.getUiApplication().pushScreen(new ATMFinder_SplashScreen(false));
                break;
            case 2:
                UiApplication.getUiApplication().pushScreen(new ATMFinder_Config());
                break;
            case 3:
                UiApplication.getUiApplication().pushScreen(new ATMFinder_OptionToSearch());
                break;
            case 4:
                this.close();
                break;
        }
        return true;
    }
}

final class ATMFinder_Config extends MainScreen implements KeyListener, TrackwheelListener, ATMFinderResource, FieldChangeListener {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private ObjectChoiceField _op1 = null;

    private ObjectChoiceField _op2 = null;

    private ObjectChoiceField _op3 = null;

    private Button _bt_save = null;

    private Button _bt_cancel = null;

    private static GlobalCfg _GlobalCfg = new GlobalCfg();

    public ATMFinder_Config() {
        _GlobalCfg.LoadConfig();
        HeaderBar _HeaderBar = new HeaderBar(_resources.getString(ATMFINDER_CAPTION) + " - " + _resources.getString(ATMFINDER_MENU_ITEM3));
        _HeaderBar.showDate(false);
        _HeaderBar.showTime(false);
        _HeaderBar.showBattery(false);
        _HeaderBar.showSignal(false);
        _HeaderBar.setBackgroundColour(0xEFEFEF);
        _HeaderBar.setBatteryBackground(0xEE1155);
        _HeaderBar.setFontColour(0xFFFFFF);
        setTitle(_HeaderBar);
        setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        String[] strop1 = { _resources.getString(INTERNALMEMORY), _resources.getString(SDCARD) };
        _op1 = new ObjectChoiceField(_resources.getString(SAVEDATAAS), strop1, _GlobalCfg.INTERNALMEMORY - 48);
        _op1.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        add(_op1);
        add(new SeparatorField());
        String[] strop2 = { "sourceforge.net", "code.google.com" };
        _op2 = new ObjectChoiceField(_resources.getString(SERVERUPDATE), strop2, _GlobalCfg.SERVERUPDATE - 48);
        _op2.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        add(_op2);
        String[] strop3 = { "GPRS", "WI-FI" };
        _op3 = new ObjectChoiceField(_resources.getString(INTERNETOVER), strop3, _GlobalCfg.INTERNETOVER - 48);
        _op3.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        add(_op3);
        add(new SeparatorField());
        add(new NullField(NullField.NON_FOCUSABLE) {

            protected void layout(int arg0, int arg1) {
                setExtent(getPreferredWidth(), 5);
            }
        });
        _bt_save = new Button(_resources.getString(SAVECONFIG), _font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _bt_save.setChangeListener(this);
        _bt_cancel = new Button(_resources.getString(EXITCONFIG), _font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _bt_cancel.setChangeListener(this);
        add(_bt_save);
        add(_bt_cancel);
    }

    /**
     * SaveConfig
     * Save config if change value in options
     * @return  void
     * @param   void
     */
    private void SaveConfig() {
        _GlobalCfg.INTERNALMEMORY = _op1.getSelectedIndex();
        _GlobalCfg.SERVERUPDATE = _op2.getSelectedIndex();
        _GlobalCfg.INTERNETOVER = _op3.getSelectedIndex();
        _GlobalCfg.SaveConfig();
        Dialog.inform(_resources.getString(CONFIGSAVEOK));
    }

    public void fieldChanged(Field field, int context) {
        if (_bt_save == (Button) field) SaveConfig(); else if (_bt_cancel == (Button) field) close();
    }

    public boolean trackwheelRoll(int amount, int status, int time) {
        return false;
    }

    public boolean trackwheelUnclick(int status, int time) {
        return false;
    }

    public boolean trackwheelClick(int status, int time) {
        if (_op1.isFocus() || _op2.isFocus() || _op3.isFocus()) getDefaultMenuItem(0).run(); else if (_bt_save == getFieldWithFocus()) SaveConfig(); else if (_bt_cancel == getFieldWithFocus()) close();
        return true;
    }

    public boolean keyChar(char key, int status, int time) {
        boolean retval = false;
        switch(key) {
            case Characters.ESCAPE:
                UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                retval = true;
                break;
            default:
                retval = super.keyChar(key, status, time);
        }
        return retval;
    }

    public boolean keyStatus(int keycode, int time) {
        return false;
    }

    public boolean keyRepeat(int keycode, int time) {
        return false;
    }

    public boolean keyUp(int keycode, int time) {
        return false;
    }

    public boolean keyDown(int keycode, int time) {
        return false;
    }
}

final class ATMFinder_OptionToSearch extends MainScreen implements KeyListener, TrackwheelListener, ATMFinderResource, FieldChangeListener {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private ObjectChoiceField _chon_NGANHANG = null;

    private ObjectChoiceField _chon_TINH_TP = null;

    private ObjectChoiceField _chon_QUAN_HUYEN = null;

    private Button _bt_searchnow = null;

    private Button _bt_advsearch = null;

    private Button _bt_updatenow = null;

    private ATMFinder_Result _ATMFinder_Result = null;

    private static BANK _BANK = new BANK();

    private static CITY _CITY = new CITY();

    private static DIST _DIST = new DIST();

    private SeparatorField sf1 = null;

    private NullField _nf1 = null;

    private NullField _nf2 = null;

    private static GlobalCfg _GlobalCfg = new GlobalCfg();

    private ATMFinder_ProgressBar _ATMFinder_ProgressBar = null;

    public ATMFinder_OptionToSearch() {
        super();
        _GlobalCfg.LoadConfig();
        HeaderBar _HeaderBar = new HeaderBar(_resources.getString(ATMFinderResource.ATMFINDER_CAPTION) + " - " + _resources.getString(ATMFinderResource.ATMFINDER_MENU_ITEM1));
        _HeaderBar.showDate(false);
        _HeaderBar.showTime(false);
        _HeaderBar.showBattery(false);
        _HeaderBar.showSignal(false);
        _HeaderBar.setBackgroundColour(0xEFEFEF);
        _HeaderBar.setBatteryBackground(0xEE1155);
        _HeaderBar.setFontColour(0xFFFFFF);
        setTitle(_HeaderBar);
        _chon_NGANHANG = new ObjectChoiceField(_resources.getString(TIMCHITIET_NGANHANG), _BANK._ID_, 0);
        _chon_NGANHANG.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        add(_chon_NGANHANG);
        add(new SeparatorField());
        _chon_TINH_TP = new ObjectChoiceField(_resources.getString(TIMCHITIET_THANHPHO), _CITY._DETAIL_, 0);
        _chon_TINH_TP.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _chon_TINH_TP.setChangeListener(this);
        add(_chon_TINH_TP);
        add(new SeparatorField());
        _DIST.get_QUAN_HUYEN(_CITY._ID_[_chon_TINH_TP.getSelectedIndex()]);
        _chon_QUAN_HUYEN = new ObjectChoiceField(_resources.getString(TIMCHITIET_QUANHUYEN), _DIST._DETAIL_, 0);
        _chon_QUAN_HUYEN.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        add(_chon_QUAN_HUYEN);
        sf1 = new SeparatorField();
        add(sf1);
        _nf1 = new NullField(NullField.NON_FOCUSABLE) {

            protected void layout(int arg0, int arg1) {
                setExtent(getPreferredWidth(), 5);
            }
        };
        _nf2 = new NullField(NullField.NON_FOCUSABLE) {

            protected void layout(int arg0, int arg1) {
                setExtent(getPreferredWidth(), 5);
            }
        };
        add(_nf1);
        _bt_searchnow = new Button(_resources.getString(SEARCHNOW), _font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _bt_searchnow.setChangeListener(this);
        _bt_advsearch = new Button(_resources.getString(ADVSEARCH), _font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _bt_advsearch.setChangeListener(this);
        _bt_updatenow = new Button(_resources.getString(UPDATENOW), _font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _bt_updatenow.setChangeListener(this);
        add(_nf2);
        add(_bt_searchnow);
        add(_bt_advsearch);
        add(_bt_updatenow);
    }

    public void Change_QUAN_HUYEN() {
        _DIST.get_QUAN_HUYEN(_CITY._ID_[_chon_TINH_TP.getSelectedIndex()]);
        _chon_QUAN_HUYEN.setChoices(_DIST._DETAIL_);
    }

    private void ATMFinder_Result_PROCESS() {
        if (_ATMFinder_Result == null) _ATMFinder_Result = new ATMFinder_Result();
        _ATMFinder_Result.NGANHANG = _BANK._ID_[_chon_NGANHANG.getSelectedIndex()];
        _ATMFinder_Result.TINH_THANHPHO = _CITY._ID_[_chon_TINH_TP.getSelectedIndex()];
        _ATMFinder_Result.QUAN_HUYEN = _DIST._ID_[_chon_QUAN_HUYEN.getSelectedIndex()];
        _ATMFinder_Result.Set_Label_NGANHANG(_BANK._DETAIL_[_chon_NGANHANG.getSelectedIndex()] + " (" + _BANK._ID_[_chon_NGANHANG.getSelectedIndex()] + ")");
        UiApplication.getUiApplication().pushScreen(_ATMFinder_Result);
        _ATMFinder_Result.Reload_MetaData();
    }

    public void fieldChanged(Field field, int context) {
        if (_chon_TINH_TP.isFocus()) {
            Change_QUAN_HUYEN();
        } else if (_bt_searchnow == (Button) field) ATMFinder_Result_PROCESS(); else if (_bt_updatenow == (Button) field) StartUpdateNow(); else if (_bt_advsearch == (Button) field) ;
    }

    public boolean trackwheelRoll(int amount, int status, int time) {
        return false;
    }

    public boolean trackwheelUnclick(int status, int time) {
        return false;
    }

    public boolean trackwheelClick(int status, int time) {
        if (_chon_QUAN_HUYEN.isFocus() || _chon_NGANHANG.isFocus() || _chon_TINH_TP.isFocus()) this.getDefaultMenuItem(0).run(); else if (_bt_searchnow == getFieldWithFocus()) ATMFinder_Result_PROCESS(); else if (_bt_updatenow == getFieldWithFocus()) StartUpdateNow(); else if (_bt_advsearch == getFieldWithFocus()) ;
        return true;
    }

    private void StartUpdateNow() {
        if (_ATMFinder_ProgressBar == null) _ATMFinder_ProgressBar = new ATMFinder_ProgressBar(_resources.getString(CONNECTINGTOSERVER));
        ATMFinder_ConnectionThread _ATMFinder_ConnectionThread = new ATMFinder_ConnectionThread(_ATMFinder_ProgressBar, 0, 0);
        _ATMFinder_ConnectionThread.fetch("http://atmfinder.sourceforge.net/update.chk");
        _ATMFinder_ConnectionThread.start();
        _ATMFinder_ProgressBar.open();
        _ATMFinder_ProgressBar._connecttoserver = false;
    }

    public boolean keyChar(char key, int status, int time) {
        boolean retval = false;
        switch(key) {
            case Characters.ESCAPE:
                UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                retval = true;
                break;
            default:
                retval = super.keyChar(key, status, time);
        }
        return retval;
    }

    public boolean keyStatus(int keycode, int time) {
        return false;
    }

    public boolean keyRepeat(int keycode, int time) {
        return false;
    }

    public boolean keyUp(int keycode, int time) {
        return false;
    }

    public boolean keyDown(int keycode, int time) {
        return false;
    }
}

final class ATMFinder_Result_Menu extends PopupScreen implements KeyListener, ATMFinderResource {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private LabelField lb = null;

    public ATMFinder_Result_Menu() {
        super(new VerticalFieldManager(), Field.FOCUSABLE);
        setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 3));
        lb = new LabelField(_resources.getString(MENURESULT));
        add(lb);
    }

    public void ReloadHeader(String _string) {
        lb.setText(_string);
    }

    public boolean keyChar(char key, int status, int time) {
        boolean retval = false;
        switch(key) {
            case Characters.ESCAPE:
                UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                retval = true;
                break;
            default:
                retval = super.keyChar(key, status, time);
        }
        return retval;
    }

    public void paint(Graphics g) {
        super.paint(g);
    }

    public boolean keyStatus(int keycode, int time) {
        return false;
    }

    public boolean keyRepeat(int keycode, int time) {
        return false;
    }

    public boolean keyUp(int keycode, int time) {
        return false;
    }

    public boolean keyDown(int keycode, int time) {
        return false;
    }
}

final class ATMFinder_Result extends MainScreen implements KeyListener, TrackwheelListener, ATMFinderResource, FieldChangeListener {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private String _dataFile = "file:///store/home/user/ATMFinder_DATA.gz";

    private String _dataFileSDCard = "file:///SDCard/atmfinder/ATMFinder_DATA.gz";

    public String NGANHANG = null;

    public String TINH_THANHPHO = null;

    public String QUAN_HUYEN = null;

    private String _list_ATM[] = null;

    private ATMFinder_Result_Menu _ATMFinder_Result_Menu = null;

    private FlowFieldManager _FlowFieldManager = null;

    private LabelField _label_NGANHANG = null;

    private ATMFinder_ProgressBar _ATMFinder_ProgressBar = new ATMFinder_ProgressBar(_resources.getString(WAITING));

    private static GlobalCfg _GlobalCfg = new GlobalCfg();

    private int _numberoffATM = 0;

    public ATMFinder_Result() {
        super();
        _GlobalCfg.LoadConfig();
        _FlowFieldManager = new FlowFieldManager();
        LabelField _LabelField = new LabelField(_resources.getString(ATMFINDER_CAPTION) + " - " + _resources.getString(ATMFINDER_MENU_ITEM1));
        _LabelField.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.BOLD, 13));
        _FlowFieldManager.add(_LabelField);
        setTitle(_FlowFieldManager);
        _ATMFinder_Result_Menu = new ATMFinder_Result_Menu();
    }

    public void Set_Label_NGANHANG(String _str_label) {
        if (_label_NGANHANG != null) _FlowFieldManager.delete(_label_NGANHANG);
        _label_NGANHANG = new LabelField("NH " + _str_label);
        _label_NGANHANG.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _FlowFieldManager.add(_label_NGANHANG);
        deleteAll();
    }

    public void Reload_MetaData() {
        _ATMFinder_ProgressBar.open();
        _ATMFinder_ProgressBar._connecttoserver = true;
        Read_MetaData();
        for (int i = 0; i < _numberoffATM; i++) add(new ATMBTField(_list_ATM[i], this.getWidth(), 21, Field.FOCUSABLE, i));
        _ATMFinder_ProgressBar.close();
    }

    public void fieldChanged(Field field, int context) {
    }

    public boolean trackwheelRoll(int amount, int status, int time) {
        return false;
    }

    public boolean trackwheelUnclick(int status, int time) {
        return false;
    }

    public boolean trackwheelClick(int status, int time) {
        ATMBTField _ATMBTField = (ATMBTField) getFieldWithFocus();
        if (_numberoffATM > 0) {
            _ATMFinder_Result_Menu.ReloadHeader(_list_ATM[_ATMBTField._ID]);
            UiApplication.getUiApplication().pushScreen(_ATMFinder_Result_Menu);
        }
        return true;
    }

    private void Read_MetaData() {
        try {
            FileConnection filecon = (FileConnection) Connector.open(_GlobalCfg.INTERNALMEMORY - 48 == 0 ? _dataFile : _dataFileSDCard);
            InputStream inread = filecon.openInputStream();
            GZIPInputStream izip = new GZIPInputStream(inread);
            InputStreamReader isr = new InputStreamReader(izip, "utf-8");
            _list_ATM = new String[255];
            String _list_temp = "";
            String _str_FILTER = NGANHANG + "$" + TINH_THANHPHO + "$" + QUAN_HUYEN + "$";
            int j = 0;
            char[] _byte_load = new char[100];
            while (isr.read(_byte_load, 0, 100) > 0) {
                _list_temp = String.valueOf(_byte_load);
                if (_list_temp.indexOf(_str_FILTER) == 0) {
                    _list_ATM[j] = _list_temp.substring(_list_temp.indexOf(_str_FILTER) + _str_FILTER.length(), _list_temp.length());
                    j++;
                }
            }
            _numberoffATM = j;
            isr.close();
            izip.close();
            inread.close();
            filecon.close();
        } catch (java.io.IOException ex) {
            Dialog.inform(_resources.getString(ERROR_FILENOTFOUND));
        }
    }

    public boolean keyChar(char key, int status, int time) {
        boolean retval = false;
        switch(key) {
            case Characters.ESCAPE:
                UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                retval = true;
                break;
            default:
                retval = super.keyChar(key, status, time);
        }
        return retval;
    }

    public boolean keyStatus(int keycode, int time) {
        return false;
    }

    public boolean keyRepeat(int keycode, int time) {
        return false;
    }

    public boolean keyUp(int keycode, int time) {
        return false;
    }

    public boolean keyDown(int keycode, int time) {
        return false;
    }
}

final class ATMFinder_UPDATER extends MainScreen implements ATMFinderResource {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_ProgressBar _ATMFinder_ProgressBar = new ATMFinder_ProgressBar(_resources.getString(CONNECTINGTOSERVER));

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private static final Bitmap icon_soft = Bitmap.getBitmapResource("up_software.png");

    private static final Bitmap icon_data = Bitmap.getBitmapResource("up_data.png");

    private String _dataFile = "file:///store/home/user/ATMFinder_DATA.gz";

    private String _dataFileSDCard = "file:///SDCard/atmfinder/ATMFinder_DATA.gz";

    private String _dataFilesize = null;

    private String[] UPDATER = new String[6];

    private int num_updater = 0;

    private int pos_menu = 1;

    private static GlobalCfg _GlobalCfg = new GlobalCfg();

    public ATMFinder_UPDATER(String[] updater, int num_up) {
        super();
        _GlobalCfg.LoadConfig();
        HeaderBar _HeaderBar = new HeaderBar(_resources.getString(ATMFinderResource.ATMFINDER_CAPTION) + " | " + _resources.getString(ATMFinderResource.UPDATENOW));
        _HeaderBar.showDate(false);
        _HeaderBar.showTime(false);
        _HeaderBar.showBattery(false);
        _HeaderBar.showSignal(false);
        _HeaderBar.setBackgroundColour(0xEFEFEF);
        _HeaderBar.setBatteryBackground(0xEE1155);
        _HeaderBar.setFontColour(0xFFFFFF);
        setTitle(_HeaderBar);
        Reload_Config();
        UPDATER = updater;
        num_updater = num_up;
    }

    public void Reload_Config() {
        setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 13));
        _dataFilesize = GetSizeDataFile();
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (num_updater == 0) {
            return;
        }
        g.setColor(0xdce2f1);
        g.fillRect(3, pos_menu == 1 ? 51 : 104, this.getWidth() - 6, icon_soft.getHeight() + 2);
        g.drawBitmap(5, 53, icon_soft.getWidth(), icon_soft.getHeight(), icon_soft, 0, 0);
        g.drawBitmap(5, 106, icon_data.getWidth(), icon_data.getHeight(), icon_data, 0, 0);
        g.setColor(Color.DARKGRAY);
        g.drawLine(2, 46, this.getWidth() - 4, 46);
        g.drawText(_resources.getString(AUPDATE), 5, 30, DrawStyle.LEFT, this.getWidth());
        g.setColor(Color.BLACK);
        g.drawText(_resources.getString(SOFTWARE), 55, 61, DrawStyle.LEFT, this.getWidth());
        g.drawText("v" + UPDATER[0] + " | " + _resources.getString(_resources.getString(SOFTWARE_VER).equals(UPDATER[0]) ? NOUPDATE : YESUPDATE), 55, 81, DrawStyle.LEFT, this.getWidth());
        g.drawText(_resources.getString(DATA), 55, 114, DrawStyle.LEFT, this.getWidth());
        g.drawText(_resources.getString(_dataFilesize.equals(UPDATER[3]) ? NOUPDATE : YESUPDATE), 55, 134, DrawStyle.LEFT, this.getWidth());
    }

    private String GetSizeDataFile() {
        String filesize = "0";
        try {
            FileConnection filecon = (FileConnection) Connector.open(_GlobalCfg.INTERNALMEMORY - 48 == 0 ? _dataFile : _dataFileSDCard);
            filesize = String.valueOf(filecon.fileSize());
            filecon.close();
        } catch (java.io.IOException ex) {
            System.out.println(ex.toString());
        }
        return filesize;
    }

    public boolean trackwheelRoll(int amount, int status, int time) {
        pos_menu = pos_menu == 1 ? 2 : 1;
        UiApplication.getUiApplication().repaint();
        return true;
    }

    public boolean trackwheelUnclick(int status, int time) {
        return true;
    }

    public boolean trackwheelClick(int status, int time) {
        switch(pos_menu) {
            case 1:
                break;
            case 2:
                if (_dataFilesize.equals(UPDATER[3]) == false) {
                    if (Dialog.ask(Dialog.D_OK_CANCEL, _resources.getString(QAUPDATE)) == Dialog.D_OK) {
                        ATMFinder_ConnectionThread _ATMFinder_ConnectionThread = new ATMFinder_ConnectionThread(_ATMFinder_ProgressBar, 1, Integer.valueOf(UPDATER[3]).intValue());
                        _ATMFinder_ConnectionThread.fetch(UPDATER[5]);
                        _ATMFinder_ConnectionThread.start();
                        _ATMFinder_ProgressBar.open();
                        _ATMFinder_ProgressBar._connecttoserver = false;
                        synchronized (_ATMFinder_ConnectionThread) {
                            _dataFilesize = GetSizeDataFile();
                            if (_dataFilesize.equals(UPDATER[3])) Dialog.inform(_resources.getString(UPDATEOK));
                            UiApplication.getUiApplication().repaint();
                        }
                    }
                }
                break;
        }
        return true;
    }

    public boolean keyChar(char key, int status, int time) {
        boolean retval = false;
        switch(key) {
            case Characters.ESCAPE:
                UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
                retval = true;
                break;
            case Characters.ENTER:
                break;
            default:
                retval = super.keyChar(key, status, time);
        }
        return retval;
    }

    public boolean keyStatus(int keycode, int time) {
        return false;
    }

    public boolean keyRepeat(int keycode, int time) {
        return false;
    }

    public boolean keyUp(int keycode, int time) {
        return false;
    }

    public boolean keyDown(int keycode, int time) {
        return false;
    }
}

final class ATMFinder_ProgressBar implements ATMFinderResource {

    private static ResourceBundle _resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

    private ATMFinder_FontLab _font = new ATMFinder_FontLab("BBMillbank");

    private PopupScreen popup = null;

    private LabelField label = null;

    public static boolean _connecttoserver = false;

    private Bitmap[] _Bitmap = new Bitmap[23];

    private BitmapField _BitmapField = null;

    private Timer animationTimer = new Timer();

    private TimerTask animationTask;

    private int counter = 0;

    public ATMFinder_ProgressBar(String title) {
        DialogFieldManager manager = new DialogFieldManager(Field.FIELD_HCENTER);
        popup = new PopupScreen(manager);
        String strtemp = "";
        for (int i = 0; i < 23; i++) {
            if (i <= 8) strtemp = "img/progress/progress_24455_00" + String.valueOf(i + 1) + ".gif"; else strtemp = "img/progress/progress_24455_0" + String.valueOf(i + 1) + ".gif";
            ;
            _Bitmap[i] = Bitmap.getBitmapResource(strtemp);
        }
        _BitmapField = new BitmapField(_Bitmap[0]);
        animationTask = new TimerTask() {

            public void run() {
                if (counter >= 0) {
                    if (counter > 22) counter = 0;
                    _BitmapField.setBitmap(_Bitmap[counter]);
                    counter++;
                }
            }
        };
        label = new LabelField(title);
        label.setFont(_font.ReLoadFont(net.rim.device.api.ui.Font.PLAIN, 12));
        synchronized (UiApplication.getEventLock()) {
        }
        animationTimer.scheduleAtFixedRate(animationTask, 100, 100);
        manager.addCustomField(label);
        manager.addCustomField(new NullField(NullField.NON_FOCUSABLE) {

            protected void layout(int arg0, int arg1) {
                setExtent(getPreferredWidth(), 5);
            }
        });
        manager.addCustomField(_BitmapField);
    }

    public void open() {
        counter = 0;
        UiApplication.getUiApplication().pushScreen(popup);
        popup.doPaint();
    }

    public void close() {
        UiApplication.getUiApplication().invokeLater(new Runnable() {

            public void run() {
                if (popup.isDisplayed()) {
                    UiApplication.getUiApplication().popScreen(popup);
                    if (!_connecttoserver) Dialog.inform(_resources.getString(NOTCONNECTOSERVER));
                }
            }
        });
    }
}

final class ATMFinder_ConnectionThread extends Thread implements ATMFinderResource {

    private static final int TIMEOUT = 500;

    private String _dataFile = "file:///store/home/user/ATMFinder_DATA.gz";

    private String _dataFileBak = "file:///store/home/user/ATMFinder_DATA.gz.bak";

    private String _dataFileSDCard = "file:///SDCard/atmfinder/ATMFinder_DATA.gz";

    private String _dataFileBakSDCard = "file:///SDCard/atmfinder/ATMFinder_DATA.gz.bak";

    private String[] UPDATER = new String[6];

    private int num_updater = 0;

    public StreamConnection _StreamConnection = null;

    private String _theUrl = null;

    private volatile boolean _start = false;

    private volatile boolean _stop = false;

    private ATMFinder_ProgressBar _ATMFinder_ProgressBar = null;

    private static GlobalCfg _GlobalCfg = new GlobalCfg();

    private int _optiontoconnect = 0;

    private int _fileSize = 0;

    public ATMFinder_ConnectionThread(ATMFinder_ProgressBar __ATMFinder_ProgressBar, int optiontoconnect, int fileSize) {
        _GlobalCfg.LoadConfig();
        _ATMFinder_ProgressBar = __ATMFinder_ProgressBar;
        _optiontoconnect = optiontoconnect;
        _fileSize = fileSize;
    }

    public synchronized String getUrl() {
        return _theUrl;
    }

    public void fetch(String url) {
        if (_start) {
        } else {
            synchronized (this) {
                if (_start) {
                } else {
                    _start = true;
                    _theUrl = url;
                }
            }
        }
    }

    public void stop() {
        _stop = true;
    }

    public void run() {
        for (; ; ) {
            while (!_start && !_stop) {
                try {
                    sleep(TIMEOUT);
                } catch (InterruptedException e) {
                    System.err.println(e.toString());
                }
            }
            if (_stop) {
                return;
            }
            synchronized (this) {
                if (_optiontoconnect == 0) {
                    try {
                        String param = ";ConnectionTimeout=40000;deviceside=true";
                        if (_GlobalCfg.INTERNETOVER - 48 == 1) param += ";ConnectionUID=S TCP-WiFi;ConnectionSetup=delayed;retrynocontext=true";
                        _StreamConnection = (StreamConnection) Connector.open(getUrl() + param, Connector.READ, true);
                        HttpConnection httpConn = (HttpConnection) _StreamConnection;
                        int status = httpConn.getResponseCode();
                        if (status == HttpConnection.HTTP_OK) {
                            _ATMFinder_ProgressBar._connecttoserver = true;
                            _ATMFinder_ProgressBar.close();
                            InputStream inread = httpConn.openInputStream();
                            InputStreamReader iread = new InputStreamReader(inread);
                            char _byte_temp;
                            String _str_temp = "";
                            while (true) {
                                _byte_temp = (char) iread.read();
                                if (String.valueOf(_byte_temp).equals("$")) {
                                    UPDATER[num_updater] = _str_temp;
                                    System.out.println(_str_temp);
                                    _str_temp = "";
                                    num_updater++;
                                } else if (String.valueOf(_byte_temp).equals("@")) break; else _str_temp += _byte_temp;
                            }
                            iread.close();
                            inread.close();
                            UiApplication.getUiApplication().invokeLater(new Runnable() {

                                public void run() {
                                    UiApplication.getUiApplication().pushScreen(new ATMFinder_UPDATER(UPDATER, num_updater));
                                }
                            });
                        } else Dialog.inform("");
                        _StreamConnection.close();
                        stop();
                    } catch (Exception e) {
                        stop();
                        _ATMFinder_ProgressBar.close();
                    }
                } else {
                    try {
                        String param = ";ConnectionTimeout=40000;deviceside=true";
                        if (_GlobalCfg.INTERNETOVER - 48 == 1) param += ";ConnectionUID=S TCP-WiFi;ConnectionSetup=delayed;retrynocontext=true";
                        _StreamConnection = (StreamConnection) Connector.open(getUrl() + param, Connector.READ, true);
                        HttpConnection httpcon = (HttpConnection) _StreamConnection;
                        int status = httpcon.getResponseCode();
                        if (status == HttpConnection.HTTP_OK) {
                            _ATMFinder_ProgressBar._connecttoserver = true;
                            _ATMFinder_ProgressBar.close();
                            BackupDataFile();
                            InputStream inread = httpcon.openInputStream();
                            CreateDataFile();
                            FileConnection filecon = (FileConnection) Connector.open(_GlobalCfg.INTERNALMEMORY - 48 == 0 ? _dataFile : _dataFileSDCard, Connector.WRITE);
                            OutputStream outwrite = filecon.openOutputStream();
                            byte[] datatemp = new byte[_fileSize];
                            inread.read(datatemp);
                            outwrite.write(datatemp);
                            outwrite.close();
                            filecon.close();
                            inread.close();
                        } else Dialog.inform("");
                        DeleteOldDataFile();
                        _StreamConnection.close();
                        stop();
                    } catch (Exception ex) {
                        stop();
                        RestoreDataFile();
                        _ATMFinder_ProgressBar.close();
                    }
                }
                _start = false;
            }
        }
    }

    private void CreateDataFile() {
        try {
            FileConnection filecon = (FileConnection) Connector.open(_GlobalCfg.INTERNALMEMORY - 48 == 0 ? _dataFile : _dataFileSDCard);
            filecon.create();
            filecon.close();
        } catch (java.io.IOException ex) {
            System.out.println(ex.toString());
        }
    }

    private void DeleteOldDataFile() {
        try {
            FileConnection filecon = (FileConnection) Connector.open(_GlobalCfg.INTERNALMEMORY - 48 == 0 ? _dataFileBak : _dataFileBakSDCard);
            filecon.delete();
            filecon.close();
        } catch (java.io.IOException ex) {
            System.out.println(ex.toString());
        }
    }

    private void BackupDataFile() {
        try {
            FileConnection filecon = (FileConnection) Connector.open(_GlobalCfg.INTERNALMEMORY - 48 == 0 ? _dataFile : _dataFileSDCard);
            filecon.rename("ATMFinder_DATA.gz.bak");
            filecon.close();
        } catch (java.io.IOException ex) {
            System.out.println(ex.toString());
        }
    }

    private void RestoreDataFile() {
        try {
            FileConnection filecon = (FileConnection) Connector.open(_GlobalCfg.INTERNALMEMORY - 48 == 0 ? _dataFileBak : _dataFileBakSDCard);
            filecon.rename("ATMFinder_DATA.gz");
            filecon.close();
        } catch (java.io.IOException ex) {
            System.out.println(ex.toString());
        }
    }
}
