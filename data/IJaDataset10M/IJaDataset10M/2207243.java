package com.project8.main;

import java.util.ArrayList;
import com.project8.book.Chapter;
import com.project8.book.Page;
import android.widget.AdapterView;
import android.widget.GridView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class TableOfCont {

    GridView gridview;

    ArrayList<ToCAdapter> allChapterGrids;

    public TableOfCont(final TableOfContent main) {
        allChapterGrids = new ArrayList<ToCAdapter>();
        for (int c = 0; c < main.book.numChapters(); c++) {
            ArrayList<Page> listOfThumbnails = new ArrayList<Page>();
            Chapter thisChapter = main.book.getChapters().get(c);
            for (int p = 0; p < thisChapter.getPages().size(); p++) {
                listOfThumbnails.add(thisChapter.getPages().get(p));
            }
            ToCAdapter tocGrid = new ToCAdapter(main, listOfThumbnails, main, thisChapter);
            allChapterGrids.add(tocGrid);
        }
        gridview = (GridView) main.findViewById(R.id.tocGridView);
        gridview.setAdapter(allChapterGrids.get(0));
        gridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            }
        });
    }

    public void displayNewChapter(Chapter lastChapterOnDisplay) {
        gridview.setAdapter(allChapterGrids.get((int) lastChapterOnDisplay.getId()));
        gridview.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            }
        });
    }
}
