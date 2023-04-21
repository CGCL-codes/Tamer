package sf.model.fields.event;

import java.util.EventListener;

/** @pdOid 60a7a072-6bf3-424a-9448-a7937566f2a2 */
public interface FieldChangeListener extends EventListener {

    /**
	 * @param sender
	 * @pdOid 3d908436-062d-4bd8-b5e7-79886c2bfe29
	 */
    void changePerformed(FieldChangeEvent event);
}
