package net.sf.pim;

import net.sf.component.calendar.SWTCalendar;
import net.sf.component.simplenote.SimpleNote;
import net.sf.pim.model.psp.WorkList;
import net.sf.util.persistence.IDataManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.TableCursor;

/**
 * @author lzhang
 */
public interface UiCore {

    public abstract boolean isDirty();

    public abstract void setDirty(boolean b);

    public abstract boolean isMulti();

    public abstract void setMulti(boolean b);

    public abstract TableViewer getTv();

    public abstract void setTv(TableViewer tv);

    public abstract SimpleNote getMemoText();

    public abstract void setMemoText(SimpleNote text);

    public abstract SWTCalendar getCalendar();

    public abstract void setCalendar(SWTCalendar calendar);

    public abstract IDataManager getDataManager();

    public abstract void checkSave();

    public abstract void setMyStatus(String s);

    public abstract String getWorkDay();

    public abstract void setWorkDay(String s);

    public abstract void closeApp();

    public abstract void setTableCursor(TableCursor tc);

    public abstract TableCursor getTableCursor();

    public abstract WorkList getData();

    public abstract void setData(WorkList workList);
}
