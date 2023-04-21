package net.sf.jttslite.gui.tasktreetable;

import net.sf.jttslite.gui.model.MutableDuration;
import net.sf.jttslite.model.Task;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.joda.time.ReadableDuration;
import org.joda.time.base.BaseDuration;

/**
 * Classe base del modello di rappresentazione dei task sull'albero del pannello
 * principale
 * 
 * @author g-caliendo
 * 
 */
public abstract class TaskTreeTableNode extends AbstractMutableTreeTableNode {

    final MutableDuration totalDuration = new MutableDuration(0);

    final MutableDuration todayDuration = new MutableDuration(0);

    /**
	 * Restituisce il task rappresentato dal nodo, <code>null</code> se non
	 * viene rappresentato alcun task persistente.
	 * 
	 * @see TaskTreeTableNode#isFakeRoot()
	 * 
	 * @return il task rappresentato dal nodo, <code>null</code> se non viene
	 *         rappresentato alcun task persistente.
	 */
    public abstract Task getTask();

    /**
	 * Restituisce <code>true</code> se il nodo è la radice dell'albero dei
	 * task, <code>false</code> altrimenti
	 * 
	 * @return <code>true</code> se il nodo è la radice dell'albero dei task,
	 *         <code>false</code> altrimenti
	 */
    public abstract boolean isFakeRoot();

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    public void incrementDurations(final long millis) {
        totalDuration.add(millis);
        todayDuration.add(millis);
    }

    public ReadableDuration getTotalDuration() {
        return totalDuration;
    }

    public ReadableDuration getTodayDuration() {
        return todayDuration;
    }

    public void setTotalDurationMillis(final ReadableDuration d) {
        totalDuration.setMillis(d.getMillis());
    }

    public void setTodayDurationMillis(final ReadableDuration d) {
        todayDuration.setMillis(d.getMillis());
    }

    @Override
    public TaskTreeTableNode getParent() {
        return (TaskTreeTableNode) super.getParent();
    }

    /**
	 * 
	 * {@inheritDoc}
	 * 
	 * @throws ClassCastException se il parent non è un {@code TaskTreeTableNode}
	 */
    @Override
    public void setParent(MutableTreeTableNode newParent) throws ClassCastException {
        super.setParent(newParent);
    }

    /**
	 * Enumerato delle colonne utilizzate dal nodo nella tabella. Fornisce dei
	 * metodi utili per il rendering dei valori delle colonne
	 * 
	 * @author Giuseppe Caliendo
	 * 
	 */
    public enum Column {

        /** Colonna nome del task */
        NAME {

            @Override
            public String getTitle() {
                return "taskTreeTable.Node.task";
            }

            @Override
            Object getValue(final TaskTreeTableNode node, final Task task) {
                return task.getName();
            }

            @Override
            public Class<?> getColumnClass() {
                return String.class;
            }
        }
        , /** Colonna con il rollup del tempo trascorso oggi */
        TODAY {

            @Override
            public String getTitle() {
                return "taskTreeTable.Node.today";
            }

            @Override
            Object getValue(final TaskTreeTableNode node, final Task task) {
                return node.getTodayDuration();
            }

            @Override
            public Class<?> getColumnClass() {
                return BaseDuration.class;
            }
        }
        , /** Colonna con il rollup del tempo totale trascorso */
        TOTAL {

            @Override
            public String getTitle() {
                return "taskTreeTable.Node.total";
            }

            @Override
            Object getValue(final TaskTreeTableNode node, final Task task) {
                return node.getTotalDuration();
            }

            @Override
            public Class<?> getColumnClass() {
                return BaseDuration.class;
            }
        }
        ;

        /**
		 * Restituisce la chiave di localizzazione della colonna
		 * 
		 * @return la chiave di localizzazione della colonna
		 */
        public abstract String getTitle();

        abstract Object getValue(TaskTreeTableNode node, Task task);

        /**
		 * Restituisce la classe dell'oggetto rappresentato dalla colonna
		 * 
		 * @return la classe dell'oggetto rappresentato dalla colonna
		 */
        public abstract Class<?> getColumnClass();
    }
}
