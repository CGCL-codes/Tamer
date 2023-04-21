package org.remus.infomngmnt.bibliographic.ui;

import java.util.ArrayList;
import org.remus.infomngmnt.bibliographic.BibliographicActivator;

/**
 * Edit page for information unit "Conference"
 * 
 * @author Andreas Deinlein <dev@deasw.com>
 *
 */
public class EditConferencePage extends BibliographicAbstractInformationFormPage {

    public EditConferencePage() {
        baseTypeId = BibliographicActivator.CONFERENCE_TYPE_ID;
        requiredFields = new ArrayList<String>();
        requiredFields.add(BibliographicActivator.NODE_NAME_AUTHOR);
        requiredFields.add(BibliographicActivator.NODE_NAME_BOOKTITLE);
        requiredFields.add(BibliographicActivator.NODE_NAME_YEAR);
        optionalFields = new ArrayList<String>();
        optionalFields.add(BibliographicActivator.NODE_NAME_EDITOR);
        optionalFields.add(BibliographicActivator.NODE_NAME_PAGES);
        optionalFields.add(BibliographicActivator.NODE_NAME_ORGANIZATION);
        optionalFields.add(BibliographicActivator.NODE_NAME_PUBLISHER);
        optionalFields.add(BibliographicActivator.NODE_NAME_ADDRESS);
        optionalFields.add(BibliographicActivator.NODE_NAME_MONTH);
        optionalFields.add(BibliographicActivator.NODE_NAME_NOTE);
    }
}
