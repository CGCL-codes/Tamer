package org.openremote.android.console.bindings;

import java.io.Serializable;
import java.util.List;

/**
 * This is a exists simply for consistency's sake. It serves no purpose
 * whatsoever other than to waste a few bytes on your phone.
 * 
 * @see org.openremote.android.console.bindings.Screen
 * @author Andrew C. Oliver <acoliver osintegrators.com>
 */
public interface ORScreen extends Serializable {

    List<Button> getButtons();

    void setButtons(List<Button> buttons);

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    int getRow();

    void setRow(int row);

    int getCol();

    void setCol(int col);
}
