package com.timenes.clips.platform.view;

public interface PointerListener {

    public void pointerEntered(View source, int x, int y);

    public void pointerClicked(View source, int x, int y);

    public void pointerExited(View source, int x, int y);
}
