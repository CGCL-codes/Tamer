package com.lars_albrecht.moviedb.components.movietablemodel;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * @see "http://www.java-forum.org/648908-post2.html"
 * @see "http://www.java-forum.org/awt-swing-swt/101885-jtable-spalten-ausblenden.html#post648908"
 * 
 */
public class HidableTableColumnModel implements TableColumnModel {

    protected ArrayList<TableColumn> m_allColumns = null;

    protected TableColumnModel m_delegateModel = null;

    public HidableTableColumnModel(final TableColumnModel delegateModel) throws NullPointerException {
        super();
        if (delegateModel == null) {
            throw new NullPointerException("Cannot have null table column model");
        }
        this.m_delegateModel = delegateModel;
        this.m_allColumns = new ArrayList<TableColumn>(this.m_delegateModel.getColumnCount());
        final Enumeration<TableColumn> columns = this.m_delegateModel.getColumns();
        while (columns.hasMoreElements()) {
            this.m_allColumns.add(columns.nextElement());
        }
    }

    public void setColumnVisible(final int viewIndex, final boolean visible) {
        this.setColumnVisible(this.m_allColumns.get(viewIndex), visible);
    }

    /**
	 * @brief blendet die genannte Spalte aus oder ein
	 * 
	 *        Diese Methode setzt die genannte Spalte auf sichtbar (einblenden)
	 *        oder unsichtbar (ausblenden), je nach Angabe.<br>
	 *        Wenn die Spalte nicht in einem der Modelle (dieses und das
	 *        Basismodell) enthalten ist, geschieht nichts.
	 * 
	 * @note Listener werden Ereignisse für Spalten-Entfernen und -Hinzufügen
	 *       erhalten.
	 * 
	 * @param column
	 *            Spaltenobjekt, das ein-/ausgeblendet werden soll
	 * @param visible
	 *            Angabe ob die Spalte ein- oder ausgeblendet wird
	 */
    public void setColumnVisible(final TableColumn column, final boolean visible) {
        if (!visible) {
            this.m_delegateModel.removeColumn(column);
        } else {
            final int visibleColumns = this.m_delegateModel.getColumnCount();
            final int invisibleColumns = this.m_allColumns.size();
            int idxVisible = 0;
            for (int idxInvisible = 0; idxInvisible < invisibleColumns; idxInvisible++) {
                final TableColumn visibleColumn = (idxVisible < visibleColumns ? this.m_delegateModel.getColumn(idxVisible) : null);
                final TableColumn testColumn = this.m_allColumns.get(idxInvisible);
                if (testColumn.equals(column)) {
                    if (!column.equals(visibleColumn)) {
                        this.m_delegateModel.addColumn(column);
                        this.m_delegateModel.moveColumn(this.m_delegateModel.getColumnCount() - 1, idxVisible);
                    }
                    return;
                }
                if (testColumn.equals(visibleColumn)) {
                    idxVisible++;
                }
            }
        }
    }

    /**
	 * @brief zeigt alle Spalten im Modell an
	 * 
	 *        Diese Methode blendet alle Spalte ein.
	 */
    public void setAllColumnsVisible() {
        final int totalSize = this.m_allColumns.size();
        for (int idxColumn = 0; idxColumn < totalSize; idxColumn++) {
            final TableColumn visibleColumn = (idxColumn < this.m_delegateModel.getColumnCount() ? this.m_delegateModel.getColumn(idxColumn) : null);
            final TableColumn invisibleColumn = this.m_allColumns.get(idxColumn);
            if (!invisibleColumn.equals(visibleColumn)) {
                this.m_delegateModel.addColumn(invisibleColumn);
                this.m_delegateModel.moveColumn(this.m_delegateModel.getColumnCount() - 1, idxColumn);
            }
        }
    }

    /**
	 * @brief Getter für Spalte anhand des Modell-Indizes
	 * 
	 *        Ermittelt das Spaltenobjekt aus der Gesamtmenge der Spalten (auch
	 *        jene, die nicht sichtbar sind), das dem gegebenen Modellindex (vom
	 *        Tabellenmodell) entspricht.
	 * 
	 * @param modelIndex
	 *            Index des Tabellenmodells der Spalte, die erfragt ist
	 * @return (erste) gefundene Spalte oder <code>null</code>
	 */
    public TableColumn getColumnByModelIndex(final int modelIndex) {
        for (int idxColumn = 0; idxColumn < this.m_allColumns.size(); idxColumn++) {
            final TableColumn column = this.m_allColumns.get(idxColumn);
            if (column.getModelIndex() == modelIndex) {
                return column;
            }
        }
        return null;
    }

    /**
	 * @brief gibt an, ob Spalte sichtbar ist, oder nicht
	 * 
	 *        Diese Methode gibt an, ob die gegebene Spalte sichtbar ist, oder
	 *        nicht. Sprich ob sie ausgeblendet wurde (in diesem Fall lautet die
	 *        Rückgabe <code>false</code>).
	 * 
	 * @param column
	 *            Spaltenobjekt
	 * @return <code>true</code> wenn diese Spalte eingeblendet ist
	 */
    public boolean isColumnVisible(final TableColumn column) {
        for (int i = 0; i < this.m_delegateModel.getColumnCount(); i++) {
            if (this.m_delegateModel.getColumn(i).equals(column)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * @brief Getter für Anzahl der Spalten
	 * 
	 *        Gibt an, wie viele Spalten dieses Modell hat. Über den Parameter
	 *        lässt sich steuern, ob nur die sichtbaren, oder auch die
	 *        ausgeblendeten Spalten gezählt werden.
	 * 
	 * @param visibleOnly
	 *            wenn <code>true</code>, werden nur die sichtbaren Spalten
	 *            gezählt
	 * @return Anzahl der Spalten, je nach Parameter alle oder nur die
	 *         sichtbaren
	 * 
	 * @see "getColumnCount()"
	 */
    public int getColumnCount(final boolean visibleOnly) {
        if (visibleOnly) {
            return this.m_delegateModel.getColumnCount();
        } else {
            return this.m_allColumns.size();
        }
    }

    /**
	 * @brief gibt eine Aufzählung der Spalten zurück
	 * 
	 *        Diese Methode gibt eine Aufzählung der Spalten des Modells zurück.
	 *        Über den Parameter lässt sich steuern, ob alle Spalten (
	 *        <code>= false</code>) oder nur die sichtbaren (
	 *        <code>= true</code> ) angegeben werden sollen.<br>
	 *        Bei <code>true</code> wird das delegierte Modell befragt.
	 * 
	 * @param visibleOnly
	 *            wenn <code>true</code>, werden nur die sichtbaren Spalten
	 *            zurückgegeben
	 * @return Aufzählung über die Spalten, je nach Parameter alle oder nur die
	 *         sichtbaren
	 * 
	 * @see "getColumns()"
	 */
    public Enumeration<TableColumn> getColumns(final boolean visibleOnly) {
        if (visibleOnly) {
            return this.m_delegateModel.getColumns();
        } else {
            return new Enumeration<TableColumn>() {

                private int m_index = 0;

                @Override
                public TableColumn nextElement() {
                    return HidableTableColumnModel.this.m_allColumns.get(this.m_index++);
                }

                @Override
                public boolean hasMoreElements() {
                    return (this.m_index < HidableTableColumnModel.this.m_allColumns.size());
                }
            };
        }
    }

    /**
	 * @brief Getter für Index der ersten Spalte mit gegebenen Identifikator
	 * 
	 *        Diese Methode ermittelt den (Anzeige-)Index der ersten Spalte mit
	 *        dem gegebenen Identifikator. Es wird über die
	 *        <code>equals()</code>-Methode verglichen.<br>
	 *        Über den zweiten Parameter kann man steuern, ob nur alle
	 *        sichtbaren oder auch die ausgeblendeten Spalten geprüft werden.
	 * 
	 * @param identifier
	 *            Identifikations-Objekt der gesuchten Spalte
	 * @param visibleOnly
	 *            Angabe ob nur die sichtbaren Spalten durchsucht werden
	 * @return Index der gefundenen Spalte oder eine Ausnahme
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn der Identifikator <code>null</code> ist, oder keine
	 *             Spalte mit dem gegebenen Identifikator gefunden wurde.
	 * 
	 * @see "getColumnIndex(Object)"
	 */
    public int getColumnIndex(final Object identifier, final boolean visibleOnly) throws IllegalArgumentException {
        if (identifier == null) {
            throw new IllegalArgumentException("Identifiert cannot be null");
        }
        final Enumeration<TableColumn> columns = this.getColumns(visibleOnly);
        int index = 0;
        while (columns.hasMoreElements()) {
            final Object id = columns.nextElement().getIdentifier();
            if (identifier.equals(id)) {
                return index;
            }
            index++;
        }
        throw new IllegalArgumentException("Identifier " + identifier + " not found");
    }

    /**
	 * @brief Getter für Spalte am Index
	 * 
	 *        Diese Methode ermittelt die Spalte am gegebenen (Anzeige-)Index.
	 *        Über den zweiten Parameter lässt sich steuern, ob alle sichtbaren
	 *        oder auch die ausgeblendeten Spalten betrachtet werden.
	 * 
	 * @param columnIndex
	 *            Index der gesuchten Spalte. Der Index entspricht dem
	 *            Anzeigeindex in der Tabelle
	 * @param visibleOnly
	 *            Angabe ob nur sichtbare Spalten geprüft werden
	 * @return gefundene Spalte
	 * 
	 * @exception IndexOutOfBoundsException
	 *                Wenn der gegeben Index außerhalb des gültigen Bereichs
	 *                liegt.
	 * 
	 * @see "getColumn(int)"
	 */
    public TableColumn getColumn(final int columnIndex, final boolean visibleOnly) {
        if (visibleOnly) {
            return this.m_delegateModel.getColumn(columnIndex);
        } else {
            return this.m_allColumns.get(columnIndex);
        }
    }

    /**
	 * @brief Methode für bequeme Aktionen
	 * 
	 *        Diese Methode erstellt (bei jedem Aufruf neu) eine Menge von
	 *        Aktionen, die für jede der gesetzen Spalten (auch unsichtbare)
	 *        eine selektierte Aktion darstellt, um sie ein- und auszublenden:
	 * @code HidableTableColumnModel cModel = ...; JPopupMenu popup = new
	 *       JPopupMenu("Hide Menu"); Action[] actions =
	 *       cModel.createColumnActions(); for (Action act : actions) {
	 *       popup.add(new JCheckBoxMenuItem(act)); } //for
	 *       myTable.setComponentPopupMenu(popup);
	 * @endcode
	 * 
	 * @note Die Aktionen sind <i>nicht</i> serialisierbar!
	 * 
	 * @return Feld mit Aktionen, pro Spalte eine eigene
	 */
    @SuppressWarnings("serial")
    public Action[] createColumnActions() {
        final Action[] actions = new Action[this.m_allColumns.size()];
        for (int i = 0; i < this.m_allColumns.size(); i++) {
            final TableColumn col = this.m_allColumns.get(i);
            final Action act = new AbstractAction() {

                private final TableColumn internalColumn = col;

                {
                    this.putValue(Action.SELECTED_KEY, HidableTableColumnModel.this.isColumnVisible(col));
                    this.putValue(Action.NAME, col.getHeaderValue());
                }

                @Override
                public void actionPerformed(final ActionEvent event) {
                    HidableTableColumnModel.this.setColumnVisible(this.internalColumn, ((Boolean) this.getValue(Action.SELECTED_KEY)).booleanValue());
                }
            };
            actions[i] = act;
        }
        return actions;
    }

    /**
	 * @brief fügt neue Spalte hinzu
	 * 
	 *        Diese Methode fügt die gegebene Spalte dem Modell an letzter
	 *        Stelle hinzu.
	 * 
	 * @param column
	 *            neues Spaltenobjekt
	 */
    @Override
    public void addColumn(final TableColumn column) {
        this.m_allColumns.add(column);
        this.m_delegateModel.addColumn(column);
    }

    /**
	 * @brief entfernt die Spalte aus dem Modell
	 * 
	 *        Diese Methode entfernt die gegebene Spalte aus dem Modell.
	 * 
	 * @param column
	 *            zu entfernende Spalte
	 */
    @Override
    public void removeColumn(final TableColumn column) {
        this.m_allColumns.remove(column);
        this.m_delegateModel.removeColumn(column);
    }

    /**
	 * @brief verschiebt Spalte innerhalb der Tabelle
	 * 
	 *        Diese Methode verschiebt die Spalte an Stelle
	 *        <code>oldIndex</code> nach <code>newIndex</code>. Verwendet werden
	 *        hier die Anzeigeindizes.
	 * 
	 * @param oldIndex
	 *            Anzeigeindex der Spalte, die verschoben werden soll
	 * @param newIndex
	 *            Anzeigeindex des Ortes, an den die Spalte verschoben wird
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn einer der Indizes nicht im gültigen bereich liegt.
	 */
    @Override
    public void moveColumn(final int oldIndex, final int newIndex) throws IndexOutOfBoundsException {
        final int size = this.m_delegateModel.getColumnCount();
        if ((oldIndex < 0) || (oldIndex >= size) || (newIndex < 0) || (newIndex >= size)) {
            throw new IndexOutOfBoundsException("At least one index is invalid: '" + oldIndex + "' or '" + newIndex + "' (valid range is [0, " + size + "])");
        }
        final TableColumn oldColumn = this.m_delegateModel.getColumn(oldIndex);
        final TableColumn newColumn = this.m_delegateModel.getColumn(newIndex);
        final int idxOld = this.m_allColumns.indexOf(oldColumn);
        final int idxNew = this.m_allColumns.indexOf(newColumn);
        if (oldIndex != newIndex) {
            this.m_allColumns.remove(idxOld);
            this.m_allColumns.add(idxNew, oldColumn);
        }
        this.m_delegateModel.moveColumn(oldIndex, newIndex);
    }

    /**
	 * @brief fügt Listener hinzu
	 * 
	 *        Der Listener wird an das delegierte Modell hinzugefügt.
	 * 
	 * @param listener
	 *            neuer Listener
	 */
    @Override
    public void addColumnModelListener(final TableColumnModelListener listener) {
        this.m_delegateModel.addColumnModelListener(listener);
    }

    /**
	 * @brief entfernt Listener
	 * 
	 *        Der Listener wird vom delegierten Modell entfernt.
	 * 
	 * @param listener
	 *            Listener der entfernt wird
	 */
    @Override
    public void removeColumnModelListener(final TableColumnModelListener listener) {
        this.m_delegateModel.removeColumnModelListener(listener);
    }

    /**
	 * @brief Getter für Spalte am gegebenen Index
	 * 
	 *        Diese Methode ermittelt die Spalte am gegeben Index. Verwendet
	 *        werden nur sichtbare Spalten.
	 * 
	 * @param columnIndex
	 *            Index der gesuchten Spalte. Verwendet wird hier der
	 *            Anzeigeindex der Tabelle.
	 * @return gefundene Spalte
	 * 
	 * @exception IndexOutOfBoundsException
	 *                Wenn der gegeben Index außerhalb des gültigen Bereichs
	 *                liegt.
	 * 
	 * @see "getColumn(int, boolean)"
	 */
    @Override
    public TableColumn getColumn(final int columnIndex) {
        return this.getColumn(columnIndex, true);
    }

    /**
	 * @brief Getter für Spaltenzahl
	 * 
	 *        Gibt die Anzahl der sichtbaren (!) Spalten zurück.
	 * @return Anzahl der sichtbaren Spalten
	 * 
	 * @see "getColumnCount(boolean)"
	 */
    @Override
    public int getColumnCount() {
        return this.getColumnCount(true);
    }

    /**
	 * @brief ermittelt Index der Spalte durch Identifikator
	 * 
	 *        Diese Methode ermittelt den Index der Spalte über alle sichtbaren
	 *        Spalten. Dazu wird intern #getColumnIndex(Object, boolean)
	 *        verwendet.
	 * 
	 * @param identifier
	 *            Identifikations-Objekt der gesuchten Spalte
	 * @return Index der gefundenen Spalte oder eine Ausnahme
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn der Identifikator <code>null</code> ist, oder keine
	 *             Spalte mit dem gegebenen Identifikator gefunden wurde.
	 * 
	 * @see "getColumnIndex(Object, boolean)"
	 */
    @Override
    public int getColumnIndex(final Object identifier) throws IllegalArgumentException {
        return this.getColumnIndex(identifier, true);
    }

    /**
	 * @brief Getter für Spalte an X-Position
	 * 
	 *        Diese Methode ermittelt über das delegierte Modell den
	 *        Spaltenindex an der gegebenen X-Position.
	 * 
	 * @param position
	 *            erfragte X-Position, meist Pixelwerte
	 * @return Index der Spalte an der Position
	 */
    @Override
    public int getColumnIndexAtX(final int position) {
        return this.m_delegateModel.getColumnIndexAtX(position);
    }

    /**
	 * @brief Getter für Spaltenabstand
	 * 
	 *        Gibt den Abstand der Spalten zurück. Dies wird vom delegierten
	 *        Modell verwaltet.
	 * 
	 * @return Abstand der Spalten in Pixeln
	 * 
	 * @see "setColumnMargin(int)"
	 */
    @Override
    public int getColumnMargin() {
        return this.m_delegateModel.getColumnMargin();
    }

    /**
	 * @brief gibt an, ob Spaltenselektion erlaubt ist
	 * 
	 *        Diese Methode gibt zurück, ob die Selektion ganzer Spalten erlaubt
	 *        ist, oder nicht.
	 * 
	 * @return <code>true</code> wenn Spaltenselektion erlaubt ist
	 * 
	 * @see "setColumnSelectionAllowed(boolean)"
	 */
    @Override
    public boolean getColumnSelectionAllowed() {
        return this.m_delegateModel.getColumnSelectionAllowed();
    }

    /**
	 * @brief Getter für alle Spalten
	 * 
	 *        Diese Methode gibt eine Aufzählung über alle sichtbaren Spalten
	 *        zurück.
	 * 
	 * @return Aufzählung mit allen sichtbaren Spalten
	 * 
	 * @see "getColumns(boolean)"
	 */
    @Override
    public Enumeration<TableColumn> getColumns() {
        return this.getColumns(true);
    }

    /**
	 * @brief ermittelt die Anzahl der selektierten Spalten
	 * 
	 *        Ermittelt die Anzahl der selektierten Spalten über das delegierte
	 *        Modell.
	 * 
	 * @return Anzahl selektierter Spalten
	 */
    @Override
    public int getSelectedColumnCount() {
        return this.m_delegateModel.getSelectedColumnCount();
    }

    /**
	 * @brief ermittelt Indizes der selektierten Spalten
	 * 
	 *        Diese Methode ermittelt die Indizes der selektierten Spalten über
	 *        das delegierte Modell.
	 * 
	 * @return Feld mit den Indizes der selektierten Spalten, oder leeres Array
	 */
    @Override
    public int[] getSelectedColumns() {
        return this.m_delegateModel.getSelectedColumns();
    }

    /**
	 * @brief Getter für Selektionsmodell
	 * 
	 *        Gibt das Selektionsmodell der delegierten Modells zurück.
	 * 
	 * @return Selektionsmodell
	 */
    @Override
    public ListSelectionModel getSelectionModel() {
        return this.m_delegateModel.getSelectionModel();
    }

    /**
	 * @brief ermittelt vollständige Breite der Spalten
	 * 
	 *        Diese Methode ermittelt (über das delegierte Modell) die
	 *        vollständige Breite der Spalten in Pixeln.
	 * 
	 * @return Breite aller (sichtbaren) Spalten in Pixeln
	 */
    @Override
    public int getTotalColumnWidth() {
        return this.m_delegateModel.getTotalColumnWidth();
    }

    /**
	 * @brief Setter für Spaltenabstand
	 * 
	 *        Übernimmt den neuen Spaltenabstand in Pixeln, welcher an das
	 *        delegierte Modell weitergetragen wird.
	 * 
	 * @param newMargin
	 *            neuer Spaltenabstand
	 * 
	 * @see "getColumnMargin()"
	 */
    @Override
    public void setColumnMargin(final int newMargin) {
        this.m_delegateModel.setColumnMargin(newMargin);
    }

    /**
	 * @brief erlaubt/verbietet Spaltenselektion
	 * 
	 *        Über diese Methode kann man die Selektion ganzer Spalten erlauben,
	 *        bzw. verbieten.
	 * 
	 * @param flag
	 *            Angabe ob Selektion erlaubt ist, oder nicht
	 * 
	 * @see "getColumnSelectionAllowed()"
	 */
    @Override
    public void setColumnSelectionAllowed(final boolean flag) {
        this.m_delegateModel.setColumnSelectionAllowed(flag);
    }

    /**
	 * @brief Setter für Selektionsmodell
	 * 
	 *        Übernimmt das neue Selektionsmodell für das delegierte
	 *        Spaltenmodell.
	 * 
	 * @param model
	 *            neues Selektionsmodell
	 * 
	 * @see "getSelectionModel()"
	 */
    @Override
    public void setSelectionModel(final ListSelectionModel model) {
        this.m_delegateModel.setSelectionModel(model);
    }
}
