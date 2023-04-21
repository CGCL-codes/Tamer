package leeon.mobile.BBSBrowser;

import java.io.File;
import java.util.List;
import leeon.kaixin.wap.action.RepasteAction;
import leeon.kaixin.wap.models.Repaste;
import leeon.kaixin.wap.util.HttpUtil;
import leeon.mobile.BBSBrowser.actions.HttpConfig;
import leeon.mobile.BBSBrowser.models.BoardObject;
import leeon.mobile.BBSBrowser.models.DocObject;
import leeon.mobile.BBSBrowser.utils.HTTPUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class KaixinRepasteDetailActivity extends Activity {

    private Repaste repaste;

    private String verify;

    private String uname;

    private TextView rTitle;

    private TextView rDate;

    private WebView rContent;

    private BoardObject board;

    private List<BoardObject> boardlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        this.setContentView(R.layout.kaixin_repaste_detail);
        rTitle = (TextView) this.findViewById(R.id.repasteTitle);
        rDate = (TextView) this.findViewById(R.id.repasteDate);
        rContent = (WebView) this.findViewById(R.id.repasteContent);
        repaste = (Repaste) this.getIntent().getSerializableExtra("repaste");
        uname = this.getIntent().getStringExtra("uname");
        verify = KaixinMainActivity.verify;
        if (repaste != null) {
            rTitle.setText(repaste.getTitle());
            rDate.setText(repaste.getDate());
            fetchDetail();
        }
        if (customTitleSupported) {
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.kaixin_titlebar);
            TextView title = (TextView) findViewById(R.id.kaixinTitle);
            title.setText(uname + "给大家的转帖");
        }
        board = KaixinMainActivity.board;
        boardlist = KaixinMainActivity.boardlist;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 1, "分享转帖").setIcon(android.R.drawable.ic_menu_share);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 1:
                shareRepaste();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareRepaste() {
        View view = LayoutInflater.from(this).inflate(R.layout.weibo_re_dialog, null);
        final TextView lb = (TextView) view.findViewById(R.id.wbLabel);
        final EditText et1 = (EditText) view.findViewById(R.id.wbTitle);
        final CheckBox cm1 = (CheckBox) view.findViewById(R.id.wbPostRetweet);
        final CheckBox cm2 = (CheckBox) view.findViewById(R.id.wbPostImage);
        final CheckBox cm3 = (CheckBox) view.findViewById(R.id.wbPostSource);
        final Spinner sp = (Spinner) view.findViewById(R.id.wbBoard);
        lb.setVisibility(View.GONE);
        et1.setVisibility(View.GONE);
        cm1.setVisibility(View.GONE);
        cm3.setVisibility(View.GONE);
        cm2.setChecked(false);
        String title = "转载该转帖";
        if (boardlist != null && boardlist.size() != 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            for (BoardObject b : boardlist) adapter.add("[" + b.getName() + "]" + b.getCh());
            sp.setVisibility(View.VISIBLE);
            sp.setPrompt("从收藏里选择版面");
            sp.setAdapter(adapter);
            sp.setOnItemSelectedListener(new OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> view, View v, int position, long id) {
                    board = boardlist.get(position);
                }

                public void onNothingSelected(AdapterView<?> view) {
                }
            });
            if (board == null) board = boardlist.get(0); else sp.setSelection(boardlist.indexOf(board));
        } else if (board != null) {
            title = "转载该转帖至[" + board.getName() + "]";
        }
        new AlertDialog.Builder(this).setTitle(title).setView(view).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (board != null) postDoc(cm2.isChecked(), board);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).create().show();
    }

    private void postDoc(final boolean postImage, final BoardObject board) {
        UIUtil.runActionInThread(this, new UIUtil.ActionInThread<BoardObject>() {

            @Override
            public void action() throws NetworkException, ContentException {
                DocObject newDoc = new DocObject(repaste.getTitle(), null, board);
                newDoc.setContent(RepasteAction.parseHtmlContent(repaste.getContent(), postImage ? new RepasteImageHandler() : null));
                ActionFactory.newInstance().newPostAction().sendPostDoc(newDoc, null, false, false, 0);
            }

            @Override
            public void actionFinish() {
                Toast.makeText(KaixinRepasteDetailActivity.this, "转载成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class RepasteImageHandler implements RepasteAction.ImageHandler {

        public String dealImage(String url) {
            File f = UIUtil.cacheFromURL(url, KaixinRepasteDetailActivity.this);
            try {
                if (!f.exists()) HTTPUtil.downloadFile(url, f, HttpUtil.newInstance());
                return ActionFactory.newInstance().newPostAction().sendAttFile(f, board.isAttach() ? board : HttpConfig.DEFAULT_UPLOAD_BOARD, GIFOpenHelper.readImageType(f));
            } catch (Exception e) {
                Log.i("repaste detail", "upload file to bbs error", e);
                return url;
            }
        }
    }

    private void fetchDetail() {
        UIUtil.runActionInThread(KaixinRepasteDetailActivity.this, new UIUtil.ActionInThread<BoardObject>() {

            @Override
            public void action() throws NetworkException, ContentException {
                if (verify != null) RepasteAction.detailRepaste(verify, repaste);
            }

            @Override
            public void actionFinish() {
                rContent.loadDataWithBaseURL(HttpUtil.KAIXIN_URL, repaste.getContent(), "text/html", "utf-8", "");
                rTitle.setText(repaste.getTitle());
            }
        });
    }
}
