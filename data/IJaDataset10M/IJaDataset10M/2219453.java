package org.trinkets.ui.plaf;

import org.jetbrains.annotations.NotNull;
import org.trinkets.ui.JCalendarDayList;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * UI for {@link org.trinkets.ui.JCalendarDayList}
 *
 * @author Alexey Efimov
 */
public abstract class CalendarDayListUI extends ComponentUI {

    public abstract Point toCellPoint(@NotNull JCalendarDayList c, @NotNull Point point);

    public abstract void setHoverCell(@NotNull JCalendarDayList c, Point point);

    public abstract void setSelectionCell(@NotNull JCalendarDayList c, Point point);

    public abstract Point getHoverCell(@NotNull JCalendarDayList c);

    public abstract Point getSelectionCell(@NotNull JCalendarDayList c);
}
