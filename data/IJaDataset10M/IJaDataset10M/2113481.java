package org.mushroomdb.engine.operation.impl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.mushroomdb.catalog.Column;
import org.mushroomdb.catalog.WriteBackRegister;
import org.mushroomdb.catalog.impl.FillableRegister;
import org.mushroomdb.catalog.impl.FilteredRegister;
import org.mushroomdb.catalog.impl.LiteralContentRegister;
import org.mushroomdb.catalog.impl.ReorderedRegister;
import org.mushroomdb.engine.operation.AbstractUnaryRelationOperation;
import org.mushroomdb.engine.operation.RelationOperation;
import org.mushroomdb.engine.optimizer.RelationOperationVisitor;
import org.mushroomdb.executionplan.ExecutionPlanElement;
import org.mushroomdb.executionplan.ExecutionPlanElementImpl;

/**
 * @author Matias
 *
 */
public class ProjectionOperation extends AbstractUnaryRelationOperation {

    private List<Column> columns;

    private List elements;

    /**
	 * @param relationOperation
	 */
    public ProjectionOperation(RelationOperation relationOperation) {
        super(relationOperation);
        this.columns = new LinkedList<Column>();
        this.elements = new LinkedList();
    }

    /**
	 * Agrega una columna a la proyecci�n.
	 * @param column la columna que se quiere proyectar. 
	 */
    public void addProjection(Column column) {
        this.columns.add(column);
        this.elements.add(column);
    }

    /**
	 * Agrega una proyecci�n literal
	 * @param object
	 */
    public void addProjection(Object object) {
        this.elements.add(object);
        if (object instanceof Column) {
            this.columns.add((Column) object);
        }
    }

    public Iterator<Column> getColumns() {
        return this.columns.iterator();
    }

    public Iterator getElements() {
        return this.elements.iterator();
    }

    public void discardElements() {
        this.columns = new LinkedList<Column>();
        this.elements = new LinkedList();
    }

    /**
	 * @see org.mushroomdb.engine.operation.RelationOperation#execute()
	 */
    public Iterator execute() {
        final Iterator iterator;
        if (this.relationOperation == null) {
            Set nearEmptySet = new LinkedHashSet();
            FillableRegister fillableRegister = new FillableRegister();
            nearEmptySet.add(fillableRegister);
            iterator = nearEmptySet.iterator();
        } else {
            iterator = this.relationOperation.execute();
        }
        return new Iterator() {

            public void remove() {
                iterator.remove();
            }

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public Object next() {
                WriteBackRegister register = (WriteBackRegister) iterator.next();
                if (register != null) {
                    FilteredRegister filteredRegister = new FilteredRegister(register, false);
                    filteredRegister.setColumnFilter(ProjectionOperation.this.columns, true);
                    ReorderedRegister reorderedRegister = new ReorderedRegister(filteredRegister, ProjectionOperation.this.columns);
                    LiteralContentRegister literalContentRegister = new LiteralContentRegister(reorderedRegister, ProjectionOperation.this.elements);
                    return literalContentRegister;
                } else {
                    return null;
                }
            }
        };
    }

    /**
	 * toString
	 */
    public String toString() {
        String elements = "";
        Iterator iterator = this.elements.iterator();
        while (iterator.hasNext()) {
            if (elements.length() > 0) {
                elements = elements + ',';
            }
            Object element = iterator.next();
            if (element instanceof Column) {
                Column column = (Column) element;
                elements = elements + column.getTable().getName() + '.' + column.getName();
            } else if (element instanceof String) elements = elements + element.toString(); else elements = elements + element.toString();
        }
        String ret = "Pi[" + elements + "]";
        if (this.relationOperation != null) ret += "(" + this.relationOperation.toString() + ")";
        return ret;
    }

    /**
	 * @see org.mushroomdb.engine.operation.RelationOperation#buildExecutionPlan()
	 */
    public ExecutionPlanElement buildExecutionPlan() {
        List elementNames = new LinkedList();
        Iterator iterator = this.columns.iterator();
        while (iterator.hasNext()) {
            Object element = iterator.next();
            if (element instanceof Column) {
                Column column = (Column) element;
                elementNames.add(column.getTable().getName() + '.' + column.getName());
            } else if (element instanceof String) {
                elementNames.add(element.toString());
            } else {
                elementNames.add(element.toString());
            }
        }
        ExecutionPlanElementImpl executionPlan = new ExecutionPlanElementImpl("Pi", elementNames);
        if (this.relationOperation != null) {
            executionPlan.addChild(this.relationOperation.buildExecutionPlan());
        }
        return executionPlan;
    }

    /**
	 * Agregado por Hache para optimizar
	 * @return
	 */
    public void accept(RelationOperationVisitor v) {
        v.visitProjection(this);
        if (this.relationOperation != null) {
            this.getRelationOperation().accept(v);
        }
    }

    /**
	 * Clona esta operaci�n.
	 */
    public Object clone() {
        Iterator elementIterator = this.getElements();
        ProjectionOperation returnInstance = new ProjectionOperation(this.getRelationOperation());
        while (elementIterator.hasNext()) {
            Object element = elementIterator.next();
            returnInstance.addProjection(element);
        }
        return returnInstance;
    }
}
