package net.sourceforge.pyrus.widget.api;

public interface InputConverter {

    public interface Listener {

        public void mouseClicked(InputEvent e);
    }

    public void setWidget(PWidget widget, Listener listener);
}
