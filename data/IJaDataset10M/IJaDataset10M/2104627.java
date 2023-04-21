package com.project8.main;

import java.util.ArrayList;
import com.project8.book.Book;
import com.project8.book.Page;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableOfContent extends Activity implements OnTouchListener {

    Book book;

    TableOfCont tableOfContents;

    Bundle bundle = new Bundle();

    LinearLayout chapterScroller;

    ArrayList<TextView> allChapters;

    TextView currentTab;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toc_grid_layout);
        bundle = getIntent().getExtras();
        book = (Book) bundle.get("Book");
        chapterScroller = (LinearLayout) findViewById(R.id.chapterScroll);
        allChapters = new ArrayList<TextView>();
        for (int ch = 0; ch < book.numChapters(); ch++) {
            TextView chapterDisplay = new TextView(this);
            chapterDisplay.setBackgroundColor(Color.parseColor("#FF980000"));
            chapterDisplay.setTextColor(Color.parseColor("#FFCCCCCC"));
            if (ch == 0) {
                chapterDisplay.setBackgroundColor(Color.parseColor("#FFCC0000"));
                chapterDisplay.setTextColor(Color.parseColor("#FFFFFFFF"));
                currentTab = chapterDisplay;
            }
            chapterDisplay.setText(book.getChapters().get(ch).getName());
            chapterDisplay.setPadding(10, 5, 10, 5);
            chapterDisplay.setTextSize(16);
            chapterDisplay.setMaxLines(1);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 3, 0);
            chapterDisplay.setLayoutParams(layoutParams);
            chapterDisplay.setOnTouchListener((OnTouchListener) this);
            allChapters.add(chapterDisplay);
            chapterScroller.addView(chapterDisplay);
        }
        tableOfContents = new TableOfCont(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        for (int ch = 0; ch < book.numChapters(); ch++) {
            allChapters.get(ch).setBackgroundColor(Color.parseColor("#FF980000"));
            allChapters.get(ch).setTextColor(Color.parseColor("#FFCCCCCC"));
            allChapters.get(ch).setText(book.getChapters().get(ch).getName());
            if (allChapters.get(ch).getText().equals(currentTab.getText())) {
                allChapters.get(ch).setBackgroundColor(Color.parseColor("#FFCC0000"));
                allChapters.get(ch).setTextColor(Color.parseColor("#FFFFFFFF"));
                currentTab = allChapters.get(ch);
            }
        }
        tableOfContents.displayNewChapter(book.findChapter((String) currentTab.getText()));
    }

    public boolean onTouch(View view, MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                {
                    for (int v = 0; v < allChapters.size(); v++) {
                        TextView aChapter = allChapters.get(v);
                        if (aChapter.equals(view)) {
                            aChapter.setBackgroundColor(Color.parseColor("#FFCC0000"));
                            aChapter.setTextColor(Color.parseColor("#FFFFFFFF"));
                            currentTab = aChapter;
                        } else {
                            aChapter.setBackgroundColor(Color.parseColor("#FF980000"));
                            aChapter.setTextColor(Color.parseColor("#FFCCCCCC"));
                        }
                    }
                    TextView textV = (TextView) view;
                    tableOfContents.displayNewChapter(book.findChapter((String) textV.getText()));
                    break;
                }
        }
        return true;
    }

    /** Allows the Main class to jump to the appropriate book page on a double-tap in ToC **/
    public void returnResult(Page jumpPage) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Page", jumpPage);
        if (getParent() == null) {
            setResult(Activity.RESULT_OK, returnIntent);
        } else {
            getParent().setResult(Activity.RESULT_OK, returnIntent);
        }
        this.finish();
    }
}
