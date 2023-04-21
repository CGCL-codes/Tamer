package net.ar.guia.own.interfaces;

import net.ar.guia.own.implementation.*;

public interface Menu extends VisualComponent {

    public VIcon getIcon();

    public PopupMenu getPopupMenu();

    public String getText();

    public void setIcon(VIcon icon);

    public void setPopupMenu(PopupMenu popupMenu);

    public void setText(String text);
}
