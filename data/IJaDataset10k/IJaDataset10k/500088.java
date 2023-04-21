package org.snsmeet.zxing.client.android;

import org.snsmeet.zxing.ResultPoint;
import org.snsmeet.zxing.ResultPointCallback;

final class ViewfinderResultPointCallback implements ResultPointCallback {

    private final ViewfinderView viewfinderView;

    ViewfinderResultPointCallback(ViewfinderView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    public void foundPossibleResultPoint(ResultPoint point) {
        viewfinderView.addPossibleResultPoint(point);
    }
}
