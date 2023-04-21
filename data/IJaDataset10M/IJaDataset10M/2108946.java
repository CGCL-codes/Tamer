package pobs.action;

import java.lang.Object;
import java.util.List;

/**
 * @author Martijn W. van der Lee
 */
public interface PList extends List, PCollection {

    public pobs.PAction addAction(int index);

    public pobs.PAction addAction(int index, final Object o);

    public pobs.PAction addAction(PNumber index);

    public pobs.PAction addAction(PNumber index, final Object o);

    public pobs.PAction addAllAction(int index, PCollection c);

    public pobs.PAction addAllAction(PNumber index, PCollection c);

    public pobs.PAction removeAction(int index);

    public pobs.PAction removeAction(PNumber index);

    public pobs.PAction setAction(int index, Object o);

    public pobs.PAction setAction(PNumber index, Object o);
}
