package org.openconcerto.sql.model;

import org.openconcerto.sql.element.SQLElement;
import org.openconcerto.sql.element.SQLElementDirectory;
import org.openconcerto.sql.model.graph.GraFFF;
import org.openconcerto.sql.model.graph.Path;
import org.openconcerto.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Un filtre SQL, un ensemble de tables avec les ID sélectionnés.
 * 
 * @author ILM Informatique 10 mai 2004
 */
public final class SQLFilter {

    /**
     * Create a filter based on parent-child relationships.
     * 
     * @param root the root to filter.
     * @param dir the relationships.
     * @return the corresponding filter.
     */
    public static SQLFilter create(DBSystemRoot root, SQLElementDirectory dir) {
        final Collection<SQLElement> elements = dir.getElements();
        final Set<SQLField> toKeep = new HashSet<SQLField>(elements.size());
        for (final SQLElement elem : elements) {
            final String parentFF = elem.getParentForeignField();
            if (parentFF != null) toKeep.add(elem.getTable().getField(parentFF));
        }
        return new SQLFilter(dir, root.getGraph().cloneForFilterKeep(toKeep));
    }

    private final SQLElementDirectory dir;

    private final GraFFF filterGraph;

    private final List<Set<SQLRow>> filteredIDs;

    private final List<SQLFilterListener> listeners;

    public SQLFilter(SQLElementDirectory dir, final GraFFF filterGraph) {
        this.dir = dir;
        this.filterGraph = filterGraph;
        this.filteredIDs = new ArrayList<Set<SQLRow>>();
        this.listeners = new ArrayList<SQLFilterListener>();
    }

    /**
     * The path from the passed table to the filtered row.
     * 
     * @param tableToDisplay la table que l'on veut filtrer.
     * @return the path from the passed table to the filter, <code>null</code> if there's no filter
     *         or the filter isn't linked to the table.
     */
    public Path getPath(SQLTable tableToDisplay) {
        Path res = null;
        if (getDepth() > 0) {
            final Set<Path> paths = this.getPaths(tableToDisplay, this.getLeafTable());
            if (!paths.isEmpty()) res = paths.iterator().next().reverse();
        }
        return res;
    }

    /**
     * Retourne l'ensemble des chemins entre les 2 tables. Permet de presonnaliser, par exemple pour
     * les observations findAllPath au lieu de getShortestPath.
     * 
     * @param tableToDisplay la table à afficher, eg OBSERVATION.
     * @param filterTable la table filtrée, eg SITE.
     * @return un ensemble de Path.
     */
    private Set<Path> getPaths(SQLTable tableToDisplay, SQLTable filterTable) {
        final Set<Path> paths;
        Path shortestPath = this.filterGraph.getShortestPath(filterTable, tableToDisplay);
        if (shortestPath == null) paths = Collections.emptySet(); else paths = Collections.singleton(shortestPath);
        return paths;
    }

    /**
     * Filtre la table passée suivant l'ID passé.
     * 
     * @param table LOCAL
     * @param ID 3
     */
    public void setFilteredID(SQLTable table, Integer ID) {
        this.setFiltered(Collections.singletonList(Collections.singleton(new SQLRow(table, ID))));
    }

    public void setFiltered(List<Set<SQLRow>> r) {
        if (r.equals(this.filteredIDs)) return;
        final int prevDepth = getDepth();
        final SQLTable prevTable = this.getLeafTable();
        this.filteredIDs.clear();
        this.filteredIDs.addAll(r);
        final SQLTable broadestTable = prevDepth < getDepth() ? prevTable : this.getLeafTable();
        this.fireConnected(broadestTable);
    }

    private int getDepth() {
        return this.filteredIDs.size();
    }

    public final Set<SQLRow> getLeaf() {
        return CollectionUtils.getFirst(this.filteredIDs);
    }

    private final SQLTable getLeafTable() {
        final Set<SQLRow> leaf = getLeaf();
        return leaf == null ? null : leaf.iterator().next().getTable();
    }

    private final void fireConnected(final SQLTable table) {
        final Set<SQLTable> connectedSet;
        if (table == null) connectedSet = this.filterGraph.getAllTables(); else {
            final String parentForeignField = this.dir.getElement(table).getParentForeignField();
            connectedSet = this.filterGraph.getDescTables(table, table.getFieldRaw(parentForeignField));
        }
        for (final SQLFilterListener l : this.listeners) {
            l.filterChanged(connectedSet);
        }
    }

    public String toString() {
        return "SQLFilter on: " + this.filteredIDs;
    }

    public void addListener(SQLFilterListener l) {
        this.listeners.add(l);
    }

    public void rmListener(SQLFilterListener l) {
        this.listeners.remove(l);
    }
}
