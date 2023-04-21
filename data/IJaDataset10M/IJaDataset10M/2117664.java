package org.thirdway.dao.employer;

import org.thirdway.dao.AbstractThirdwayDAOTest;
import org.thirdway.dao.note.NoteDAO;
import org.thirdway.dao.person.AddressDAO;
import org.thirdway.domain.common.Note;
import org.thirdway.domain.contact.Address;
import org.thirdway.domain.employer.Employer;
import org.thirdway.domain.employer.EmployerAddress;
import org.thirdway.domain.employer.EmployerNote;
import org.thirdway.domain.systemuser.SystemUser;
import org.thirdway.test.util.AddressTestUtil;
import org.thirdway.test.util.EmployerTestUtil;
import org.thirdway.test.util.NoteTestUtil;

public class EmployerDAOTest extends AbstractThirdwayDAOTest {

    AddressDAO addressDAO;

    EmployerDAO employerDAO;

    NoteDAO noteDAO;

    public void testCRUDEmployer() {
        Employer expectedEmployer = buildAndSaveEmployer();
        Employer actualEmployer = this.employerDAO.getEmployerById(expectedEmployer.getId());
        assertEquals("Expected employer didn't equal actualEmployer", expectedEmployer, actualEmployer);
    }

    public void testManageEmployerAddress() {
        Employer expectedEmployer = buildAndSaveEmployer();
        Address expectedAddress = this.addressDAO.insertAddress(AddressTestUtil.buildAddress());
        expectedEmployer.setAddress(expectedAddress);
        EmployerAddress employerAddress = new EmployerAddress(expectedEmployer, expectedAddress);
        this.employerDAO.insertEmployerAddress(employerAddress);
        Employer actualEmployer = this.employerDAO.getEmployerById(expectedEmployer.getId());
        assertEquals("Expected employer address wasn't equals to actual", expectedEmployer.getAddress(), actualEmployer.getAddress());
        this.employerDAO.deleteEmployerAddress(employerAddress);
        actualEmployer = this.employerDAO.getEmployerById(expectedEmployer.getId());
        assertTrue("Employer address wasn't null as expected", actualEmployer.getAddress() == null);
    }

    public void testManageEmployerNotes() {
        SystemUser systemUser = this.getSavedSystemUser();
        Employer expectedEmployer = buildAndSaveEmployer();
        Note note1 = this.noteDAO.insertNote(NoteTestUtil.buildNote(systemUser));
        Note note2 = this.noteDAO.insertNote(NoteTestUtil.buildNote(systemUser));
        Note note3 = this.noteDAO.insertNote(NoteTestUtil.buildNote(systemUser));
        expectedEmployer.addNote(note1);
        expectedEmployer.addNote(note2);
        expectedEmployer.addNote(note3);
        EmployerNote employerNote1 = new EmployerNote(expectedEmployer, note1);
        EmployerNote employerNote2 = new EmployerNote(expectedEmployer, note2);
        EmployerNote employerNote3 = new EmployerNote(expectedEmployer, note3);
        this.employerDAO.insertEmployerNote(employerNote1);
        this.employerDAO.insertEmployerNote(employerNote2);
        this.employerDAO.insertEmployerNote(employerNote3);
        Employer actualEmployer = this.employerDAO.getEmployerById(expectedEmployer.getId());
        assertEquals("Expected employer notes list size wasn't equals to actual notes list size", expectedEmployer.getNotes().size(), actualEmployer.getNotes().size());
        assertTrue("Expected employer notes doesn't contain all of the actual notes", expectedEmployer.getNotes().containsAll(actualEmployer.getNotes()));
        this.employerDAO.deleteEmployerNote(employerNote2);
        expectedEmployer.getNotes().remove(note2);
        actualEmployer = this.employerDAO.getEmployerById(expectedEmployer.getId());
        assertEquals("Expected employer notes list size wasn't equals to actual notes list size after removing one.", expectedEmployer.getNotes().size(), actualEmployer.getNotes().size());
        assertTrue("Expected employer notes doesn't contain all of the actual notes after removing one.", expectedEmployer.getNotes().containsAll(actualEmployer.getNotes()));
    }

    private Employer buildAndSaveEmployer() {
        return this.employerDAO.insertEmployer(EmployerTestUtil.buildEmployer());
    }

    public void setEmployerDAO(EmployerDAO employerDAO) {
        this.employerDAO = employerDAO;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }
}
