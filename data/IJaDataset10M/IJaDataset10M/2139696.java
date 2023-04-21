package com.ruysing.filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ruysing.filemanager.bean.MyFile;
import com.ruysing.filemanager.util.FileUtil;
import com.ruysing.filemanager.view.BarMenuItem;

public class MainActivity extends Activity implements OnClickListener {

    private GridView mGridView;

    private TextView mEmptyTextView;

    private GridViewAdapter mGridViewAdapter = new GridViewAdapter();

    private LayoutInflater mLayoutInflater;

    private List<MyFile> datas = new ArrayList<MyFile>();

    private MyScanTask mTask;

    private static File sdcardFile = Environment.getExternalStorageDirectory();

    private static final String ROOT_DIR = "/sdcard";

    private BarMenuItem searchBtn;

    private BarMenuItem installBtn;

    private BarMenuItem downBtn;

    private BarMenuItem moreBtn;

    private GestureDetector mGestureDetector;

    private LinearLayout mMenuBar;

    private static Bitmap folderIcon;

    private static Bitmap fileIcon;

    /** �ײ��ˆ����P **/
    private boolean isShowingMenuBar = false;

    private Animation mMenuBarInAnim;

    private Animation mMenuBarOutAnim;

    private static final int MENU_SHOW_TIIME = 8;

    private int mShowSeconds = 0;

    private MenuBarAnimationTask mMenuBarAnimTask;

    /** ���ˆ����P **/
    private View mMoreView;

    private boolean isShowMore = false;

    private Animation mMenuMoreInAnim;

    private Animation mMenuMoreOutAnim;

    private RelativeLayout mRelativeLayout;

    /** ������� **/
    private View mSearchPanel;

    private boolean isShowSearchPanel = false;

    private EditText mSearchInputText;

    private Button mSearchButton;

    private ImageView mSearchCloseButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mMenuBarInAnim = AnimationUtils.loadAnimation(this, R.anim.menu_bar_in);
        mMenuBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.menu_bar_out);
        mGestureDetector = new GestureDetector(mGestuerDetectorListener);
        bindView();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mEmptyTextView.setText("Ո����Sdcard!");
            return;
        }
        folderIcon = BitmapFactory.decodeResource(getResources(), R.drawable.folder_desktop);
        fileIcon = BitmapFactory.decodeResource(getResources(), R.drawable.file);
        scan(ROOT_DIR);
        startMenuAnimation();
    }

    private void bindView() {
        mGridView = (GridView) findViewById(R.id.gridView);
        mMenuBar = (LinearLayout) findViewById(R.id.main_bar);
        mEmptyTextView = (TextView) findViewById(android.R.id.empty);
        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mGridView.setOnItemClickListener(mGridViewItemClickListener);
        mGridView.setOnItemLongClickListener(mGridViewItemLongClickListener);
        mGridView.setAdapter(mGridViewAdapter);
        searchBtn = (BarMenuItem) findViewById(R.id.menu_search);
        downBtn = (BarMenuItem) findViewById(R.id.menu_down);
        installBtn = (BarMenuItem) findViewById(R.id.menu_install);
        moreBtn = (BarMenuItem) findViewById(R.id.menu_more);
        mMoreView = findViewById(R.id.main_more_menu);
        searchBtn.setOnClickListener(this);
        downBtn.setOnClickListener(this);
        installBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
    }

    private void scan(String dir) {
        if (mTask != null && mTask.getStatus() == MyScanTask.Status.RUNNING) {
            mTask.cancel(false);
        }
        setTitle(dir);
        mTask = new MyScanTask();
        mTask.execute(dir);
    }

    private void startMenuAnimation() {
        if (mMenuBarAnimTask != null && mMenuBarAnimTask.getStatus() == MenuBarAnimationTask.Status.RUNNING) {
            mMenuBarAnimTask.cancel(true);
        }
        isShowingMenuBar = true;
        mMenuBarAnimTask = (MenuBarAnimationTask) new MenuBarAnimationTask().execute();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doExit() {
        final Dialog dialog = new Dialog(this, R.style.Theme_No_TITLE_DIALOG);
        View dView = mLayoutInflater.inflate(R.layout.main_exit_dialog_layout, null);
        dialog.setContentView(dView);
        Button okItem = (Button) dView.findViewById(R.id.exit_btn_ok);
        Button cancelItem = (Button) dView.findViewById(R.id.exit_btn_cancel);
        okItem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                release();
            }
        });
        cancelItem.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void release() {
        if (mMenuBarAnimTask.getStatus() != MenuBarAnimationTask.Status.FINISHED) {
            mMenuBarAnimTask.cancel(true);
        }
        if (mTask.getStatus() != MyScanTask.Status.FINISHED) {
            mTask.cancel(true);
        }
        mMenuBarAnimTask = null;
        mTask = null;
        mMenuBarInAnim = null;
        mMenuBarOutAnim = null;
        mMenuMoreInAnim = null;
        finish();
    }

    private GridView.OnItemClickListener mGridViewItemClickListener = new GridView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setMoreView(false);
            MyFile mf = datas.get(position);
            if (mf.getAliasPath() == null) {
            } else {
                File f = new File(mf.getAliasPath());
                if (f.isDirectory()) {
                    scan(mf.getAliasPath());
                } else {
                    selectOpenType(mf.getFile());
                }
            }
        }
    };

    private GridView.OnItemLongClickListener mGridViewItemLongClickListener = new GridView.OnItemLongClickListener() {

        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    };

    class GridViewAdapter extends BaseAdapter {

        public int getCount() {
            return datas.size();
        }

        public Object getItem(int position) {
            return datas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.desktop_item_layout, null);
            }
            final ImageView mImageView = (ImageView) convertView.findViewById(R.id.destop_item_icon);
            final TextView mTextView = (TextView) convertView.findViewById(R.id.desktop_item_name);
            MyFile mf = datas.get(position);
            mImageView.setImageBitmap(mf.getIcon());
            mTextView.setText(mf.getAliasName() == null ? mf.getFile().getName() : mf.getAliasName());
            return convertView;
        }
    }

    class MyScanTask extends AsyncTask<String, Integer, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(String... params) {
            if (params == null || params[0] == null || "".equals(params[0])) {
                return 0;
            }
            File file = new File(params[0]);
            int fileCount = 0;
            datas.clear();
            File[] files = null;
            if (!ROOT_DIR.equals(params[0])) {
                MyFile root = new MyFile();
                root.setFile(sdcardFile);
                root.setIcon(folderIcon);
                root.setAliasName("��Ŀ¼");
                root.setAliasPath("/sdcard");
                MyFile parent = new MyFile();
                parent.setFile(file.getParentFile());
                parent.setIcon(folderIcon);
                parent.setAliasName("��һ��");
                parent.setAliasPath(file.getParent());
                datas.add(root);
                datas.add(parent);
            }
            files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    fileCount++;
                    MyFile mf = new MyFile();
                    mf.setFile(f);
                    mf.setAliasName(f.getName());
                    mf.setAliasPath(f.getPath());
                    if (f.isDirectory()) {
                        mf.setIcon(folderIcon);
                    } else {
                        mf.setIcon(fileIcon);
                    }
                    datas.add(mf);
                    publishProgress(fileCount);
                }
            }
            return fileCount;
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            mEmptyTextView.setVisibility(View.GONE);
            mGridViewAdapter.notifyDataSetChanged();
        }
    }

    class MenuBarAnimationTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                while (mShowSeconds < MENU_SHOW_TIIME) {
                    Thread.sleep(1000);
                    if (!isShowMore) {
                        mShowSeconds++;
                    }
                }
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (isShowingMenuBar) {
                isShowingMenuBar = false;
                isShowMore = false;
                mMenuBar.startAnimation(mMenuBarOutAnim);
                mMenuBar.setVisibility(View.GONE);
                mMoreView.setVisibility(View.GONE);
            }
            super.onPostExecute(result);
        }
    }

    private GestureDetector.OnGestureListener mGestuerDetectorListener = new GestureDetector.OnGestureListener() {

        public boolean onDown(MotionEvent e) {
            int y = (int) e.getY();
            if (y >= 270) {
                mShowSeconds = 0;
                if (!isShowingMenuBar) {
                    isShowingMenuBar = true;
                    mMenuBar.setVisibility(View.VISIBLE);
                    mMenuBar.startAnimation(mMenuBarInAnim);
                    startMenuAnimation();
                }
            }
            return false;
        }

        public void onShowPress(MotionEvent e) {
        }

        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        public void onLongPress(MotionEvent e) {
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.menu_more:
                showPopupWindow();
                break;
            case R.id.menu_search:
                showSearchPanel();
                break;
            case R.id.search_close:
                closeSearchPanel();
                break;
            case R.id.search_btn:
                searchFile();
                break;
            case R.id.menu_down:
                break;
        }
    }

    private void searchFile() {
        String keyword = mSearchInputText.getText().toString();
        if (null == keyword || "".equals(keyword)) {
            mSearchInputText.setError("������ؼ���!");
            return;
        }
        new Thread(new SearchFileThread(this, keyword)).start();
    }

    private void closeSearchPanel() {
        Animation mAnim = AnimationUtils.loadAnimation(this, R.anim.search_panel_out);
        mSearchPanel.startAnimation(mAnim);
        mSearchPanel.setVisibility(View.GONE);
        isShowSearchPanel = false;
    }

    private void showSearchPanel() {
        mShowSeconds = 0;
        if (mMenuMoreOutAnim == null) {
            mMenuMoreOutAnim = AnimationUtils.loadAnimation(this, R.anim.menu_more_anim_out);
        }
        if (isShowMore) {
            mMoreView.startAnimation(mMenuMoreOutAnim);
            isShowMore = false;
        }
        mMoreView.setVisibility(View.GONE);
        if (mRelativeLayout == null) {
            mRelativeLayout = (RelativeLayout) findViewById(R.id.main_layout);
        }
        Animation mSearchPanelIn = AnimationUtils.loadAnimation(this, R.anim.search_panel_in);
        if (mSearchPanel == null) {
            mSearchPanel = mLayoutInflater.inflate(R.layout.search_panel_layout, null);
            mSearchPanel.startAnimation(mSearchPanelIn);
            mRelativeLayout.addView(mSearchPanel);
        } else {
            if (!isShowSearchPanel) {
                mSearchPanel.setVisibility(View.VISIBLE);
                mSearchPanel.startAnimation(mSearchPanelIn);
            }
        }
        if (mSearchButton == null) {
            mSearchButton = (Button) mSearchPanel.findViewById(R.id.search_btn);
            mSearchCloseButton = (ImageView) mSearchPanel.findViewById(R.id.search_close);
            mSearchInputText = (EditText) mSearchPanel.findViewById(R.id.search_input);
            mSearchButton.setOnClickListener(this);
            mSearchCloseButton.setOnClickListener(this);
        }
        isShowSearchPanel = true;
    }

    private void showPopupWindow() {
        if (isShowSearchPanel) {
            Animation mSearchPanelOut = AnimationUtils.loadAnimation(this, R.anim.search_panel_out);
            mSearchPanel.startAnimation(mSearchPanelOut);
            mSearchPanel.setVisibility(View.GONE);
        }
        isShowSearchPanel = false;
        if (!isShowMore) {
            isShowMore = true;
            mMoreView.setVisibility(View.VISIBLE);
            if (mMenuMoreInAnim == null) {
                mMenuMoreInAnim = AnimationUtils.loadAnimation(this, R.anim.menu_more_anim_in);
            }
            mMoreView.startAnimation(mMenuMoreInAnim);
        } else {
            if (mMenuMoreOutAnim == null) {
                mMenuMoreOutAnim = AnimationUtils.loadAnimation(this, R.anim.menu_more_anim_out);
            }
            mMoreView.startAnimation(mMenuMoreOutAnim);
            mMoreView.setVisibility(View.GONE);
            isShowMore = false;
            mShowSeconds = 0;
        }
    }

    public void show(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public Bitmap getFolderIcon() {
        return folderIcon;
    }

    public Bitmap getFileIcon() {
        return fileIcon;
    }

    public void clearData() {
        datas.clear();
    }

    public void addData(MyFile mf) {
        datas.add(mf);
    }

    public void refreshGridView() {
        mGridViewAdapter.notifyDataSetChanged();
    }

    private void setMoreView(boolean showing) {
        if (showing) {
            mMoreView.setAnimation(mMenuMoreInAnim);
            mMoreView.setVisibility(View.VISIBLE);
            isShowMore = true;
        } else {
            mMoreView.setAnimation(mMenuMoreOutAnim);
            mMoreView.setVisibility(View.GONE);
            isShowMore = false;
        }
    }

    private void selectOpenType(File f) {
        String mimeType = FileUtil.getMimeType(f);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), mimeType);
        startActivity(intent);
    }

    private void openFile(File f) {
    }
}
