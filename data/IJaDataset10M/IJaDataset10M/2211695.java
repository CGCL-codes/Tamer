package net.sourceforge.dbtoolbox.validator;

import java.util.Collection;
import java.util.Collections;
import net.sourceforge.dbtoolbox.model.CatalogMD;
import net.sourceforge.dbtoolbox.model.DatabaseMD;
import net.sourceforge.dbtoolbox.model.SchemaMD;
import net.sourceforge.dbtoolbox.model.TableMD;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test {@link ForbiddenNameElementMDValidator}
 */
public class ForbiddenNameElementMDValidatorTest {

    private ForbiddenNameElementMDValidator<TableMD> validator;

    @Before
    public void setUp() {
        validator = new ForbiddenNameElementMDValidator<TableMD>();
        validator.setElementClass(TableMD.class);
        validator.setNames("id,name,comment");
    }

    private void testValidate(String name, int expected) {
        DatabaseMD database = new DatabaseMD();
        CatalogMD catalog = new CatalogMD(database, null);
        SchemaMD schema = new SchemaMD(catalog, "SCHEMA");
        TableMD table = new TableMD(schema, name);
        ValidatorMDVisitor validatorVisitor = new ValidatorMDVisitor(Collections.singletonList(validator));
        validatorVisitor.visitDatabase(database);
        Collection<MDValidatorMessage> validatorMessages = validatorVisitor.getValidatorMessages();
        assertEquals("Validator Message count", expected, validatorMessages.size());
    }

    @Test
    public void testValidateOK() {
        testValidate("ABC_TABLE", 0);
    }

    @Test
    public void testValidateKO() {
        testValidate("COMMENT", 1);
    }
}
