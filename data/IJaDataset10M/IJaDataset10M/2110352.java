package com.magicpwd.e.mpro.kind;

import com.magicpwd.__a.mpro.AMproAction;

/**
 *
 * @author Amon
 */
public class PromotionAction extends AMproAction {

    public PromotionAction() {
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        mproPtn.promotionSelectedKind();
    }

    @Override
    public void doInit(String value) {
    }

    @Override
    public void reInit(javax.swing.AbstractButton button, String value) {
    }
}
