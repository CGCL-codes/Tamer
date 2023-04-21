package com.ntu.way2fungames.darkdecent;

import android.view.View;
import android.widget.AbsoluteLayout;

public class viewController {

    static void setView(AbsoluteLayout nView, String what) {
        if (what == "menu") {
            nView.findViewById(R.id.menuview).setVisibility(View.VISIBLE);
            nView.findViewById(R.id.DungeonView).setVisibility(View.GONE);
            nView.findViewById(R.id.chrview).setVisibility(View.GONE);
        }
        if (what == "dungeon") {
            nView.findViewById(R.id.menuview).setVisibility(View.GONE);
            nView.findViewById(R.id.DungeonView).setVisibility(View.VISIBLE);
            nView.findViewById(R.id.chrview).setVisibility(View.GONE);
        }
        if (what == "chr") {
            nView.findViewById(R.id.menuview).setVisibility(View.GONE);
            nView.findViewById(R.id.DungeonView).setVisibility(View.GONE);
            nView.findViewById(R.id.chrview).setVisibility(View.VISIBLE);
        }
    }
}
