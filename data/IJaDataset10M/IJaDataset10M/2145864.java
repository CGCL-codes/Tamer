package com.iplayawriter.novelizer.controller.operation;

import com.iplayawriter.novelizer.model.INovelElement;
import com.iplayawriter.novelizer.model.IRepository;
import com.iplayawriter.novelizer.model.IValue;
import com.iplayawriter.novelizer.model.IValues;
import com.iplayawriter.novelizer.model.ValueFactory;
import com.iplayawriter.novelizer.test.NovelizerTestHelper;
import static com.iplayawriter.novelizer.test.NovelizerTestHelper.*;
import com.iplayawriter.novelizer.test.TestPerspective;
import org.junit.Test;

/**
 *
 * @author Erik
 */
public class AddNovelElementTest {

    private final NovelizerTestHelper testHelper = new NovelizerTestHelper();

    /**
     * Test of execute method, of class AddNovelElement.
     */
    @Test
    public void testAddCharacter() {
        System.out.println("testAddCharacter");
        final IRepository repository = testHelper.getRepository();
        final PerspectiveWithAdd perspective = new PerspectiveWithAdd(CHARACTER_NAME, CHARACTER_FULLNAME, CHARACTER_SUMMARY, CHARACTER_DESCRIPTION, CHARACTER_MOTIVATION);
        final AddNovelElement operation = new AddNovelElement(INovelElement.Type.CHARACTER, perspective, repository);
        operation.execute();
        testHelper.verifyCreatedCharacter(testHelper.getFirstCharacter(repository), VERIFY_ALL);
    }

    @Test
    public void testAddCharacterBlank() {
        System.out.println("testAddCharacterBlank");
        final IRepository repository = testHelper.getRepository();
        final PerspectiveWithAdd perspective = new PerspectiveWithAdd("", "", "", "", "");
        final AddNovelElement operation = new AddNovelElement(INovelElement.Type.CHARACTER, perspective, repository);
        operation.execute();
        testHelper.verifyCreatedCharacter(testHelper.getFirstCharacter(repository), 0);
    }

    @Test
    public void testAddCharacterName() {
        System.out.println("testAddCharacterName");
        final IRepository repository = testHelper.getRepository();
        final PerspectiveWithAdd perspective = new PerspectiveWithAdd(CHARACTER_NAME, "", "", "", "");
        final AddNovelElement operation = new AddNovelElement(INovelElement.Type.CHARACTER, perspective, repository);
        operation.execute();
        testHelper.verifyCreatedCharacter(testHelper.getFirstCharacter(repository), VERIFY_NAME);
    }

    @Test
    public void testAddCharacterFullname() {
        System.out.println("testAddCharacter");
        final IRepository repository = testHelper.getRepository();
        final PerspectiveWithAdd perspective = new PerspectiveWithAdd(CHARACTER_NAME, CHARACTER_FULLNAME, "", "", "");
        final AddNovelElement operation = new AddNovelElement(INovelElement.Type.CHARACTER, perspective, repository);
        operation.execute();
        testHelper.verifyCreatedCharacter(testHelper.getFirstCharacter(repository), VERIFY_FULLNAME | VERIFY_NAME);
    }

    @Test
    public void testAddCharacterSummary() {
        System.out.println("testAddCharacter");
        final IRepository repository = testHelper.getRepository();
        final PerspectiveWithAdd perspective = new PerspectiveWithAdd(CHARACTER_NAME, "", CHARACTER_SUMMARY, "", "");
        final AddNovelElement operation = new AddNovelElement(INovelElement.Type.CHARACTER, perspective, repository);
        operation.execute();
        testHelper.verifyCreatedCharacter(testHelper.getFirstCharacter(repository), VERIFY_SUMMARY | VERIFY_NAME);
    }

    @Test
    public void testAddCharacterDescription() {
        System.out.println("testAddCharacter");
        final IRepository repository = testHelper.getRepository();
        final PerspectiveWithAdd perspective = new PerspectiveWithAdd(CHARACTER_NAME, "", "", CHARACTER_DESCRIPTION, "");
        final AddNovelElement operation = new AddNovelElement(INovelElement.Type.CHARACTER, perspective, repository);
        operation.execute();
        testHelper.verifyCreatedCharacter(testHelper.getFirstCharacter(repository), VERIFY_DESCRIPTION | VERIFY_NAME);
    }

    @Test
    public void testAddCharacterMotivation() {
        System.out.println("testAddCharacter");
        final IRepository repository = testHelper.getRepository();
        final PerspectiveWithAdd perspective = new PerspectiveWithAdd(CHARACTER_NAME, "", "", "", CHARACTER_MOTIVATION);
        final AddNovelElement operation = new AddNovelElement(INovelElement.Type.CHARACTER, perspective, repository);
        operation.execute();
        testHelper.verifyCreatedCharacter(testHelper.getFirstCharacter(repository), VERIFY_MOTIVATION | VERIFY_NAME);
    }

    private class PerspectiveWithAdd extends TestPerspective {

        private final String name;

        private final String summary;

        private final String fullname;

        private final String description;

        private final String motivation;

        PerspectiveWithAdd(String name, String fullname, String summary, String description, String motivation) {
            this.name = name;
            this.fullname = fullname;
            this.summary = summary;
            this.description = description;
            this.motivation = motivation;
        }

        @Override
        public IValues add(String string, IValues template) {
            final ValueFactory factory = ValueFactory.getInstance();
            final IValues returnValues = factory.createEmptyValues();
            returnValues.addValue(factory.createValue(VALUE_NAME, IValue.Type.STRING, name));
            returnValues.addValue(factory.createValue(VALUE_FULLNAME, IValue.Type.STRING, fullname));
            returnValues.addValue(factory.createValue(VALUE_DESCRIPTION, IValue.Type.STRING, description));
            returnValues.addValue(factory.createValue(VALUE_MOTIVATION, IValue.Type.STRING, motivation));
            returnValues.addValue(factory.createValue(VALUE_SUMMARY, IValue.Type.STRING, summary));
            return returnValues;
        }
    }
}
