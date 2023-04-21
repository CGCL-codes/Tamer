package com.aem.sticky.button.events;

import com.aem.sticky.button.Button;

/**
 * @author Alexander Schearer <aschearer@gmail.com>
 */
public interface ClickListener {

    /**
     * @param clicked
     * @param mx
     * @param my
     */
    public void onClick(Button clicked, float mx, float my);

    /**
     * @param clicked
     * @param mx
     * @param my
     */
    public void onRightClick(Button clicked, float mx, float my);

    /**
     * @param clicked
     * @param mx
     * @param my
     */
    public void onDoubleClick(Button clicked, float mx, float my);
}
