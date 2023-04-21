package jp.gaomar.mytem;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Handler;

/**
 * データの読み書きをするマネージャーです
 * 
 * @author hide
 * 
 */
public class MytemManager {

    /** SQLite読み書きクラス */
    private MytemSqlController mytemSqlController;

    /** GAE読み書きクラス */
    private MytemGaeController mytemGaeController;

    private Context context;

    /** image画像保存ディレクトリ */
    public static File SAVE_IMAGE_DIRECTORY;

    /**
	 * コンストラクタ
	 * 
	 * @param _context
	 */
    public MytemManager(Context _context) {
        this.context = _context;
        if (!hasExternalStorage()) {
            Handler handler = new Handler();
            handler.post(new Runnable() {

                @Override
                public void run() {
                    CustomAlertDialog dialog = new CustomAlertDialog(context, R.style.CustomDialog);
                    dialog.setTitle(R.string.alert_messeage);
                    dialog.setButton("OK", (OnClickListener) null);
                    dialog.setMessage(context.getString(R.string.No_Storage_Message));
                    dialog.show();
                }
            });
        }
        mytemGaeController = new MytemGaeController(context);
        mytemSqlController = new MytemSqlController(context);
    }

    /**
	 * JANコードを引数にSQliteやGAEから商品マスタを得ます
	 * 
	 * @param janCode
	 * @return
	 */
    public MytemMaster getMytemMaster(String janCode) throws SQLException, GaeException {
        MytemMaster master = mytemGaeController.getMytemMaster(janCode);
        return master;
    }

    /**
	 * GAEに商品を登録する
	 * 
	 * @param mytemMaster
	 * @throws SQLException
	 * @throws DuplexMytemMasterException
	 * @throws GaeException
	 */
    public void createMytemMaster(MytemMaster mytemMaster) throws GaeException {
        mytemGaeController.create(mytemMaster);
    }

    /**
	 * 商品履歴を登録する
	 * 
	 * @param mytemMaster
	 * @throws SQLException
	 */
    public void createMytemHistory(String jancode, String itemname, MytemHistory mytemHistory) throws SQLException, GaeException {
        mytemSqlController.createHistory(jancode, itemname, mytemHistory);
        mytemGaeController.createHistory(jancode, mytemHistory);
    }

    /**
	 * 商品履歴を返す
	 * 
	 * @return
	 * @throws SQLException
	 */
    public List<MytemHistory> getMytemHistories() throws SQLException {
        return mytemSqlController.getMytemHistories(30);
    }

    /**
	 * 自分JANコードの商品履歴を返す
	 * 
	 * @return
	 * @throws GaeException 
	 * @throws SQLException 
	 */
    public List<MytemHistory> getMytemHistories(String janCode) throws SQLException {
        return mytemSqlController.getMytemHistories(janCode);
    }

    /**
	 * みんなの商品履歴を返す
	 * 
	 * @return
	 * @throws GaeException 
	 */
    public List<MytemHistory> getSocialMytemHistories(String janCode) throws GaeException {
        return mytemGaeController.getMytemHistories(janCode);
    }

    /**
	 * 外部ストレージチェック
	 */
    public boolean hasExternalStorage() {
        if (SAVE_IMAGE_DIRECTORY != null) {
            return true;
        }
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            SAVE_IMAGE_DIRECTORY = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
            if (!SAVE_IMAGE_DIRECTORY.exists()) {
                SAVE_IMAGE_DIRECTORY.mkdirs();
            }
            return true;
        }
        return false;
    }
}
