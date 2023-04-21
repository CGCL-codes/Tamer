package org.fenggui;

import org.fenggui.appearance.EntryAppearance;

/**
 * @author Marc Menghin, last edited by $Author: marcmenghin $, $LastChangedDate: 2009-03-13 12:26:38 +0100 (Fr, 13 Mär 2009) $
 * @version $Revision: 602 $
 */
public interface IListModel extends IModel {

    public int getSize();

    public Item getItem(int index, EntryAppearance appearance);
}
