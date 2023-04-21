package edu.tufts.osidimpl.testing.repository;

import junit.framework.TestCase;

public class RepositoryMetadataTest extends TestCase {

    public RepositoryMetadataTest(org.osid.repository.Repository repository, org.w3c.dom.Element repositoryElement) throws org.osid.repository.RepositoryException, org.xml.sax.SAXParseException {
        String expected = Utilities.expectedValue(repositoryElement, OsidTester.DISPLAY_NAME_TAG);
        if (expected != null) {
            if (Utilities.isVerbose()) System.out.println("Found display name " + repository.getDisplayName());
            assertEquals("seeking display name " + expected, expected, repository.getDisplayName());
            System.out.println("PASSED: Repository Display Name " + expected);
        }
        expected = Utilities.expectedValue(repositoryElement, OsidTester.DESCRIPTION_TAG);
        if (expected != null) {
            if (Utilities.isVerbose()) System.out.println("Found description " + repository.getDescription());
            assertEquals("seeking description " + expected, expected, repository.getDescription());
            System.out.println("PASSED: Repository Description " + expected);
        }
        expected = Utilities.expectedValue(repositoryElement, OsidTester.ID_TAG);
        if (expected != null) {
            org.osid.shared.Id id = repository.getId();
            try {
                String idString = id.getIdString();
                if (Utilities.isVerbose()) System.out.println("Found id " + idString);
                assertEquals("seeking id " + expected, expected, idString);
                System.out.println("PASSED: Repository Id " + expected);
            } catch (org.osid.shared.SharedException iex) {
            }
        }
        expected = Utilities.expectedValue(repositoryElement, OsidTester.TYPE_TAG);
        if (expected != null) {
            if (Utilities.isVerbose()) System.out.println("Found type " + Utilities.typeToString(repository.getType()));
            assertEquals("seeking repository type " + expected, expected, Utilities.typeToString(repository.getType()));
            System.out.println("PASSED: Repository Type " + expected);
        }
    }
}
