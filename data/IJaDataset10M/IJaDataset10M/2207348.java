package org.jabusuite.webclient.address.employee.jobapplication;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.jabusuite.webclient.controls.JbsButton;
import nextapp.echo2.app.Column;
import org.jabusuite.webclient.controls.JbsExtent;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import org.jabusuite.address.employee.jobassignment.JobAssignment;
import org.jabusuite.core.utils.JbsObject;

public abstract class ColVitaEntries extends Column {

    private static final long serialVersionUID = 6618146338498017452L;

    private VitaRows vitaRows;

    private Column colRows;

    private Set<JbsObject> jaEntries;

    public ColVitaEntries() {
        this.vitaRows = new VitaRows();
        this.setJaEntries(new LinkedHashSet<JbsObject>());
        this.setInsets(new Insets(5, 5));
        this.setCellSpacing(new JbsExtent(8));
        this.colRows = new Column();
        this.add(colRows);
        this.addColButtons();
    }

    public void addColButtons() {
        Column colButtons = new Column();
        JbsButton btnAdd = new JbsButton("+");
        btnAdd.addActionListener(new ActionListener() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                addVitaRow(createDataObject());
            }
        });
        colButtons.add(btnAdd);
        this.add(colButtons);
    }

    public void setControlData() {
        removeAllVitaRows();
        this.getVitaRows().clear();
        Iterator<JbsObject> it = this.getJaEntries().iterator();
        while (it.hasNext()) {
            this.addVitaRow(it.next());
        }
    }

    public void getControlData() {
        this.getJaEntries().clear();
        Iterator<VitaRow> it = this.getVitaRows().iterator();
        while (it.hasNext()) {
            VitaRow vitaRow = it.next();
            vitaRow.getControlData();
            this.getJaEntries().add(vitaRow.getData());
        }
    }

    /**
     * Adds a new <code>VitaRow</code> and returns the new instace
     * @param data The data that this row represents
     * @return The new <code>VitaRow</code>-Instance.
     */
    protected VitaRow addVitaRow(JbsObject data) {
        return this.addVitaRow(-1, data);
    }

    /**
     * This method is invoked every time a new vita-row is needed. Create your
     * <code>VitaRow</code>-Object here.
     * @param data
     * @return
     */
    protected abstract VitaRow createVitaRow(JbsObject data);

    /**
     * Every time a <code>VitaRow</code>-Object is needed it needs also an object
     * for it's data.
     * @return A newly created data-object for the <code>VitaRow</code>
     */
    protected abstract JbsObject createDataObject();

    /**
     * Adds a new <code>VitaRow</code> and returns the new instance
     * @param index - the index where to add the new row
     * @return
     */
    protected VitaRow addVitaRow(int index, JbsObject data) {
        VitaRow vitaRow = this.createVitaRow(data);
        if (index < 0) {
            System.out.println("add: " + String.valueOf(index));
            this.getVitaRows().add(vitaRow);
            this.getColRows().add(vitaRow.getColumn());
        } else {
            System.out.println("insert: " + String.valueOf(index));
            this.getVitaRows().add(index, vitaRow);
            refreshVitaRows();
        }
        return vitaRow;
    }

    /**
     * Removed all VitaRows from the panel. They are still stored in the List <code>vitaRows</code>
     *
     */
    protected void removeAllVitaRows() {
        for (int i = 0; i < this.getVitaRows().size(); i++) {
            this.getColRows().remove(this.getVitaRows().get(i).getColumn());
        }
    }

    /**
     * Removes a vitaRow from the panel and from the internal list.
     * @param vitaRow
     */
    public void removeVitaRow(VitaRow vitaRow) {
        this.getColRows().remove(vitaRow.getColumn());
        this.getVitaRows().remove(vitaRow);
    }

    public void insertVitaRow(VitaRow vitaRow) {
        int index = this.getVitaRows().getIndex(vitaRow);
        this.addVitaRow(index, this.createDataObject());
    }

    /**
     * Removes all VitaRows from the panel and adds them again.
     *
     */
    protected void refreshVitaRows() {
        removeAllVitaRows();
        for (int i = 0; i < this.getVitaRows().size(); i++) {
            this.getColRows().add(this.getVitaRows().get(i).getColumn());
        }
    }

    /**
     * @return the vitaRows
     */
    protected VitaRows getVitaRows() {
        return vitaRows;
    }

    /**
     * @param vitaRows the vitaRows to set
     */
    protected void setVitaRows(VitaRows vitaRows) {
        this.vitaRows = vitaRows;
    }

    /**
     * @return the colRows
     */
    protected Column getColRows() {
        return colRows;
    }

    /**
     * @param colRows the colRows to set
     */
    protected void setColRows(Column colRows) {
        this.colRows = colRows;
    }

    public Set<JbsObject> getJaEntries() {
        return this.getJaEntries(false);
    }

    public Set<JbsObject> getJaEntries(boolean doGetControlData) {
        if (doGetControlData) this.getControlData();
        return jaEntries;
    }

    public void setJaEntries(Set<JbsObject> jaEntries) {
        this.jaEntries = jaEntries;
        this.setControlData();
    }
}
