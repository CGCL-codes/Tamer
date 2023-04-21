package de.haumacher.timecollect;

import de.haumacher.timecollect.common.config.Value;

public interface LayoutConfig extends Value {

    public interface Bounds extends Value {

        int getX();

        void setX(int value);

        int getY();

        void setY(int value);

        int getWidth();

        void setWidth(int value);

        int getHeight();

        void setHeight(int value);
    }

    Bounds getFetchTicketBounds();

    Bounds getManageTicketsBounds();

    Bounds getBrowserBounds();

    Bounds getAboutBounds();

    Bounds getReportBounds();

    Bounds getEditActivitiesBounds();

    Bounds getPreferencesBounds();
}
