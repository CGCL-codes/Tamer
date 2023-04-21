package com.ynhenc.comm.util;

/**
 * Title:        HTML FREE EDITOR
 * Description:  Html Free Editor v2.0
 * Copyright:    Copyright (c) 2001
 * Company:      JCOSMOS DEVELOPMENT
 * @author Suhng ByuhngMunn
 * @version 1.0
 */
public interface ProgressInterface {

    public void setString(String text);

    public void setValue(int i);

    public void hideProgress();

    public boolean isProgressActive();
}
