package org.nightlabs.jfire.base.ui.person.search;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.nightlabs.jfire.base.ui.prop.DefaultPropertySetTableConfig;
import org.nightlabs.jfire.base.ui.prop.IPropertySetTableConfig;
import org.nightlabs.jfire.base.ui.prop.PropertySetTable;
import org.nightlabs.jfire.person.Person;
import org.nightlabs.jfire.person.PersonStruct;
import org.nightlabs.jfire.prop.IStruct;
import org.nightlabs.jfire.prop.StructField;
import org.nightlabs.jfire.prop.dao.StructLocalDAO;
import org.nightlabs.jfire.prop.id.StructFieldID;
import org.nightlabs.progress.NullProgressMonitor;

/**
 * Table Composite that displays {@link StructField} values
 * for a {@link Person}.
 * 
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 *
 */
public class PersonResultTable extends PropertySetTable<Person> {

    public PersonResultTable(Composite parent, int style) {
        super(parent, style);
    }

    @Override
    protected IPropertySetTableConfig getPropertySetTableConfig() {
        return new PersonResultTableConfig();
    }

    class PersonResultTableConfig extends DefaultPropertySetTableConfig {

        @Override
        public IStruct getIStruct() {
            return StructLocalDAO.sharedInstance().getStructLocal(Person.class, Person.STRUCT_SCOPE, Person.STRUCT_LOCAL_SCOPE, new NullProgressMonitor());
        }

        @Override
        public StructFieldID[] getStructFieldIDs() {
            return new StructFieldID[] { PersonStruct.PERSONALDATA_COMPANY, PersonStruct.PERSONALDATA_NAME, PersonStruct.PERSONALDATA_FIRSTNAME, PersonStruct.POSTADDRESS_CITY, PersonStruct.POSTADDRESS_ADDRESS, PersonStruct.POSTADDRESS_POSTCODE, PersonStruct.PHONE_PRIMARY, PersonStruct.INTERNET_EMAIL };
        }

        @Override
        public List<StructFieldID[]> getStructFieldIDsList() {
            List<StructFieldID[]> l = new ArrayList<StructFieldID[]>();
            l.add(new StructFieldID[] { PersonStruct.PERSONALDATA_COMPANY });
            l.add(new StructFieldID[] { PersonStruct.PERSONALDATA_NAME, PersonStruct.PERSONALDATA_FIRSTNAME });
            l.add(new StructFieldID[] { PersonStruct.POSTADDRESS_CITY });
            l.add(new StructFieldID[] { PersonStruct.POSTADDRESS_ADDRESS, PersonStruct.POSTADDRESS_POSTCODE });
            l.add(new StructFieldID[] { PersonStruct.PHONE_PRIMARY });
            l.add(new StructFieldID[] { PersonStruct.INTERNET_EMAIL });
            return l;
        }
    }
}
