package org.asianclassics.ace.client;

import com.google.gwt.json.client.JSONObject;

public class VolumeSearchTool extends SearchTool {

    protected HitFlipper hitFlipper = new HitFlipper();

    public VolumeSearchTool() {
        btn_search.setText("Search Title");
        btn_search.setTitle("Search text within this title");
        panel.add(hitFlipper);
        hitFlipper.setVisible(false);
    }

    public void doSearch() {
        super.doSearch();
    }

    public void setHitInfo(JSONObject hitInfo) {
        int titleHits = (int) hitInfo.get("numtitle").isNumber().doubleValue();
        int viewHits = (int) hitInfo.get("numview").isNumber().doubleValue();
        int topHitIndex = (int) hitInfo.get("topindex").isNumber().doubleValue();
        String msg;
        if (titleHits == 0) {
            msg = "Nothing found.";
            hitFlipper.setVisible(btn_reset.isVisible());
            hitFlipper.setBtnsVisible(false);
        } else {
            msg = "Hit " + (topHitIndex + 1);
            if (viewHits > 1) msg += "-" + (topHitIndex + viewHits);
            msg += " of " + titleHits;
            hitFlipper.setVisible(true);
        }
        hitFlipper.setText(msg);
        hitFlipper.setNext((int) hitInfo.get("next").isNumber().doubleValue());
        hitFlipper.setPrev((int) hitInfo.get("prev").isNumber().doubleValue());
    }

    public void reset() {
        hitFlipper.setVisible(false);
        super.reset();
    }
}
