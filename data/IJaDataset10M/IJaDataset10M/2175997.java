package org.xaware.ide.xadev.richui.editor.service;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

/**
 * @author tferguson
 *
 */
public class SummaryPageSectionHolder {

    private Section section;

    private Composite client;

    private FormToolkit toolkit;

    private List<SummaryPageEntryCompositeHolder> entries;

    /**
     * 
     */
    public SummaryPageSectionHolder(final FormToolkit toolkit, final ScrolledForm form, final String title) {
        this.toolkit = toolkit;
        section = toolkit.createSection(form.getBody(), Section.TWISTIE | Section.TITLE_BAR | Section.EXPANDED);
        TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        section.setLayoutData(td);
        section.setText(title);
        section.addExpansionListener(new ExpansionAdapter() {

            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        });
        client = toolkit.createComposite(section);
        client.setLayout(new GridLayout(2, false));
        section.setClient(client);
        entries = new ArrayList<SummaryPageEntryCompositeHolder>();
    }

    public void moveAbove(Composite c) {
        section.moveAbove(c);
    }

    public void setSectionTitle(String title) {
        if (!section.getText().equals(title)) {
            section.setText(title);
        }
    }

    public void addEntry(IServiceEditorSummaryPageEntry entry) {
        SummaryPageEntryCompositeHolder holder = null;
        holder = getSpecificEntry(entry.getEntryId());
        if (holder == null) {
            holder = new SummaryPageEntryCompositeHolder(toolkit, entry);
            holder.getHeadingLabel(client);
            holder.getLineComposite(client);
            entries.add(holder);
        } else {
            holder.setEntry(entry);
        }
        section.layout();
    }

    /**
     * @param entry
     * @param holder
     * @return
     */
    private SummaryPageEntryCompositeHolder getSpecificEntry(String entryId) {
        SummaryPageEntryCompositeHolder holder = null;
        for (SummaryPageEntryCompositeHolder entryComp : entries) {
            if (entryComp.getEntry().getEntryId().equals(entryId)) {
                holder = entryComp;
                break;
            }
        }
        return holder;
    }

    public void clearEntry(String entryId) {
        SummaryPageEntryCompositeHolder e = getSpecificEntry(entryId);
        e.clearLines();
        section.layout();
    }

    public void clearAllEntries() {
        for (SummaryPageEntryCompositeHolder e : entries) {
            e.clearLines();
        }
        section.layout();
    }

    public void removeAllEntries() {
        for (SummaryPageEntryCompositeHolder e : entries) {
            e.dispose();
        }
        entries.clear();
        section.layout();
    }

    public List<IServiceEditorSummaryPageEntry> getEntries() {
        List<IServiceEditorSummaryPageEntry> returnEntries = new ArrayList<IServiceEditorSummaryPageEntry>();
        for (SummaryPageEntryCompositeHolder e : entries) {
            returnEntries.add(e.getEntry());
        }
        return returnEntries;
    }

    public String getTitle() {
        return section.getText();
    }

    public void addToEntry(IServiceEditorSummaryPageEntry e) {
        SummaryPageEntryCompositeHolder compEntry = getSpecificEntry(e.getEntryId());
        if (compEntry != null) {
            IServiceEditorSummaryPageEntry currEntry = compEntry.getEntry();
            List<String> lines = currEntry.getEntryLines();
            lines.addAll(e.getEntryLines());
            compEntry.setEntry(currEntry);
        } else {
            this.addEntry(e);
        }
    }
}
