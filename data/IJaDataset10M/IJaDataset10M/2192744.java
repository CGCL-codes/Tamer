package com.dmurph.mvc.gui.combo;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JComboBox;
import com.dmurph.mvc.model.MVCArrayList;

/**
 * This class is for having a combo box that will always reflect the data of
 * an MVCArrayList.  There is a lot of flexibility provided with filtering
 * and sorting the elements.
 * 
 * @author Daniel Murphy
 */
public class MVCJComboBox<E> extends JComboBox {

    private static final long serialVersionUID = 1L;

    private MVCJComboBoxModel<E> model;

    private MVCArrayList<E> data;

    private IMVCJComboBoxFilter<E> filter;

    private final Object lock = new Object();

    private MVCJComboBoxStyle style;

    private Comparator<E> comparator = null;

    private final PropertyChangeListener plistener = new PropertyChangeListener() {

        @SuppressWarnings("unchecked")
        public void propertyChange(PropertyChangeEvent argEvt) {
            String prop = argEvt.getPropertyName();
            if (prop.equals(MVCArrayList.ADDED)) {
                add((E) argEvt.getNewValue());
            } else if (prop.equals(MVCArrayList.ADDED_ALL)) {
                addAll((Collection<E>) argEvt.getNewValue());
            } else if (prop.equals(MVCArrayList.CHANGED)) {
                change((E) argEvt.getOldValue(), (E) argEvt.getNewValue());
            } else if (prop.equals(MVCArrayList.REMOVED)) {
                remove((E) argEvt.getOldValue());
            } else if (prop.equals(MVCArrayList.REMOVED_ALL)) {
                synchronized (lock) {
                    model.removeAllElements();
                }
            }
        }
    };

    /**
	 * Constructs with no data, no filter, no 
	 * {@link Comparator}, and style set to
	 * {@link MVCJComboBoxStyle#ADD_NEW_TO_BEGINNING}.
	 */
    public MVCJComboBox() {
        this(null, new IMVCJComboBoxFilter<E>() {

            public boolean showItem(E argComponent) {
                return true;
            }

            ;
        }, MVCJComboBoxStyle.SORT, null);
    }

    /**
	 * Constructs a combo box with the given style.  If you want
	 * the {@link MVCJComboBoxStyle#SORT} style, then you'll want to specify
	 * a comparator as well.
	 * @param argData
	 * @param argStyle
	 */
    public MVCJComboBox(MVCJComboBoxStyle argStyle) {
        this(null, new IMVCJComboBoxFilter<E>() {

            public boolean showItem(E argComponent) {
                return true;
            }

            ;
        }, argStyle, null);
    }

    /**
	 * Constracts a dynamic combo box with the given data and
	 * default style of {@link MVCJComboBoxStyle#SORT}.
	 * @param argData
	 */
    public MVCJComboBox(MVCArrayList<E> argData, Comparator<E> argComparator) {
        this(argData, new IMVCJComboBoxFilter<E>() {

            public boolean showItem(E argComponent) {
                return true;
            }

            ;
        }, MVCJComboBoxStyle.SORT, argComparator);
    }

    /**
	 * Constructs a combo box with the given data and style.  If you want
	 * the {@link MVCJComboBoxStyle#SORT} style, then you'll want to specify
	 * a comparator as well.
	 * @param argData
	 * @param argStyle
	 */
    public MVCJComboBox(MVCArrayList<E> argData, MVCJComboBoxStyle argStyle) {
        this(argData, new IMVCJComboBoxFilter<E>() {

            public boolean showItem(E argComponent) {
                return true;
            }

            ;
        }, argStyle, null);
    }

    /**
	 * Constructs a dynamic combo box with the given data, filter, and comparator.
	 * The style will be {@link MVCJComboBoxStyle#SORT} by default.
	 * @param argData
	 * @param argFilter
	 * @param argComparator
	 */
    public MVCJComboBox(MVCArrayList<E> argData, IMVCJComboBoxFilter<E> argFilter, Comparator<E> argComparator) {
        this(argData, argFilter, MVCJComboBoxStyle.SORT, null);
    }

    /**
	 * 
	 * @param argData
	 * @param argFilter
	 * @param argStyle
	 * @param argComparator
	 */
    public MVCJComboBox(MVCArrayList<E> argData, IMVCJComboBoxFilter<E> argFilter, MVCJComboBoxStyle argStyle, Comparator<E> argComparator) {
        data = argData;
        style = argStyle;
        filter = argFilter;
        comparator = argComparator;
        model = new MVCJComboBoxModel<E>();
        super.setModel(model);
        if (data != null) {
            argData.addPropertyChangeListener(plistener);
            for (E o : data) {
                if (filter.showItem(o)) {
                    model.addElement(o);
                }
            }
            if (style == MVCJComboBoxStyle.SORT && comparator != null) {
                model.sort(comparator);
            }
        }
    }

    /**
	 * Gets the rendering style of this combo box.  Default style is
	 * {@link MVCJComboBoxStyle#SORT}. 
	 * @return
	 */
    public MVCJComboBoxStyle getStyle() {
        return style;
    }

    /**
	 * Gets the data list.  This is used to access
	 * data with {@link #refreshData()}, so override
	 * if you want to customize what the data is (sending
	 * null to the contructor for the data 
	 * is a good idea in that case)
	 * @return
	 */
    public ArrayList<E> getData() {
        return data;
    }

    /**
	 * Sets the data of this combo box.  This causes the box
	 * to refresh it's model
	 * @param argData can be null
	 */
    public void setData(MVCArrayList<E> argData) {
        synchronized (lock) {
            if (data != null) {
                data.removePropertyChangeListener(plistener);
            }
            data = argData;
            if (data != null) {
                data.addPropertyChangeListener(plistener);
            }
        }
        refreshData();
    }

    /**
	 * Sets the comparator used for the {@link MVCJComboBoxStyle#SORT} style.
	 * @param argComparator
	 */
    public void setComparator(Comparator<E> argComparator) {
        this.comparator = argComparator;
    }

    /**
	 * Gets the comparator that's used for sorting.
	 * @return
	 */
    public Comparator<E> getComparator() {
        return comparator;
    }

    /**
	 * @return the filter
	 */
    public IMVCJComboBoxFilter<E> getFilter() {
        return filter;
    }

    /**
	 * @param argFilter the filter to set
	 */
    public void setFilter(IMVCJComboBoxFilter<E> argFilter) {
        filter = argFilter;
    }

    /**
	 * @see javax.swing.JComboBox#processKeyEvent(java.awt.event.KeyEvent)
	 */
    @Override
    public void processKeyEvent(KeyEvent argE) {
        if (argE.getKeyChar() == KeyEvent.VK_BACK_SPACE || argE.getKeyChar() == KeyEvent.VK_DELETE) {
            setSelectedItem(null);
            super.hidePopup();
        } else {
            super.processKeyEvent(argE);
        }
    }

    /**
	 * Sets the style of this combo box
	 * @param argStyle
	 */
    public void setStyle(MVCJComboBoxStyle argStyle) {
        style = argStyle;
        if (style == MVCJComboBoxStyle.SORT) {
            if (comparator == null) {
                throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
            }
            model.sort(comparator);
        }
    }

    public void refreshData() {
        synchronized (lock) {
            model.removeAllElements();
            if (getData() == null) {
                return;
            }
            for (E e : getData()) {
                if (filter.showItem(e)) {
                    model.addElement(e);
                }
            }
            if (style == MVCJComboBoxStyle.SORT) {
                if (comparator == null) {
                    throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
                }
                model.sort(comparator);
            }
        }
    }

    private void add(E argNewObj) {
        boolean b = filter.showItem(argNewObj);
        if (b == false) {
            return;
        }
        synchronized (lock) {
            switch(style) {
                case SORT:
                    {
                        if (comparator == null) {
                            throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
                        }
                        boolean inserted = false;
                        for (int i = 0; i < model.getSize(); i++) {
                            E e = model.getElementAt(i);
                            if (comparator.compare(e, argNewObj) > 0) {
                                model.insertElementAt(argNewObj, i);
                                inserted = true;
                                break;
                            }
                        }
                        if (!inserted) {
                            model.addElement(argNewObj);
                        }
                        break;
                    }
                case ADD_NEW_TO_BEGINNING:
                    {
                        model.insertElementAt(argNewObj, 0);
                        break;
                    }
                case ADD_NEW_TO_END:
                    {
                        model.addElement(argNewObj);
                    }
            }
        }
    }

    private void addAll(Collection<E> argNewObjects) {
        LinkedList<E> filtered = new LinkedList<E>();
        Iterator<E> it = argNewObjects.iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (filter.showItem(e)) {
                filtered.add(e);
            }
        }
        if (filtered.size() == 0) {
            return;
        }
        synchronized (lock) {
            switch(style) {
                case SORT:
                    {
                        if (comparator == null) {
                            throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
                        }
                        model.addElements(filtered);
                        model.sort(comparator);
                        break;
                    }
                case ADD_NEW_TO_BEGINNING:
                    {
                        model.addElements(0, filtered);
                        break;
                    }
                case ADD_NEW_TO_END:
                    {
                        model.addElements(filtered);
                    }
            }
        }
    }

    private void change(E argOld, E argNew) {
        boolean so = filter.showItem(argOld);
        boolean sn = filter.showItem(argNew);
        if (!sn) {
            remove(argOld);
            return;
        }
        if (!so) {
            if (sn) {
                add(argNew);
                return;
            } else {
                return;
            }
        }
        synchronized (lock) {
            int size = model.getSize();
            for (int i = 0; i < size; i++) {
                E e = model.getElementAt(i);
                if (e == argOld) {
                    model.setElementAt(argNew, i);
                    return;
                }
            }
            if (style == MVCJComboBoxStyle.SORT) {
                if (comparator == null) {
                    throw new NullPointerException("DynamicJComboBox style is set to Alpha Sort, but the comparator is null.");
                }
                model.sort(comparator);
            }
        }
    }

    private void remove(E argVal) {
        boolean is = filter.showItem(argVal);
        if (!is) {
            return;
        }
        synchronized (lock) {
            for (int i = 0; i < model.getSize(); i++) {
                E e = model.getElementAt(i);
                if (e == argVal) {
                    model.removeElementAt(i);
                    return;
                }
            }
        }
    }
}
