package co.edu.unal.ungrid.grid.master;

import net.jini.core.entry.Entry;
import net.jini.id.Uuid;

public class ResumeEntry implements Entry {

    public static final long serialVersionUID = 20041002000080L;

    public Uuid uuid;

    public ResumeEntry() {
    }

    public ResumeEntry(Uuid uuid) {
        this.uuid = uuid;
    }
}
