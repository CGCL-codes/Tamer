package net.sourceforge.nattable.examples.examples._120_Selection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.nattable.NatTable;
import net.sourceforge.nattable.data.IColumnPropertyAccessor;
import net.sourceforge.nattable.data.IRowDataProvider;
import net.sourceforge.nattable.data.ListDataProvider;
import net.sourceforge.nattable.data.ReflectiveColumnPropertyAccessor;
import net.sourceforge.nattable.examples.AbstractNatExample;
import net.sourceforge.nattable.examples.runner.StandaloneNatExampleRunner;
import net.sourceforge.nattable.grid.data.DefaultColumnHeaderDataProvider;
import net.sourceforge.nattable.grid.layer.DefaultGridLayer;
import net.sourceforge.nattable.selection.RowSelectionProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class Get_and_set_selected_objects extends AbstractNatExample {

    public static void main(String[] args) {
        StandaloneNatExampleRunner.run(400, 200, new Get_and_set_selected_objects());
    }

    @Override
    public String getDescription() {
        return "This example shows how to listen to row selection events in NatTable via the " + "JFace selection api. This is done via the RowSelectionProvider adapter class. " + "This example demonstrates the default RowSelectionProvider behavior, which is " + "to only report rows that are fully selected. The RowSelectionProvider can also " + "be configured to return any row that has a selected cell in it (see source code).";
    }

    public Control createExampleControl(Composite parent) {
        Person homer = new Person("Homer", "Simpson", "Sargeant", 1234567890L);
        Person smithers = new Person("Waylon", "Smithers", "Admiral", 6666666666L);
        Person bart = new Person("Bart", "Smithers", "General", 9125798342L);
        Person nelson = new Person("Nelson", "Muntz", "Private", 0000000001L);
        Person frink = new Person("John", "Frink", "Lieutenant", 3141592654L);
        List<Person> myList = new ArrayList<Person>();
        myList.add(homer);
        myList.add(smithers);
        myList.add(bart);
        myList.add(nelson);
        myList.add(frink);
        String[] propertyNames = { "firstName", "lastName", "rank", "serialNumber" };
        IColumnPropertyAccessor<Person> columnPropertyAccessor = new ReflectiveColumnPropertyAccessor<Person>(propertyNames);
        IRowDataProvider<Person> bodyDataProvider = new ListDataProvider<Person>(myList, columnPropertyAccessor);
        DefaultGridLayer gridLayer = new DefaultGridLayer(bodyDataProvider, new DefaultColumnHeaderDataProvider(propertyNames));
        NatTable natTable = new NatTable(parent, gridLayer);
        ISelectionProvider selectionProvider = new RowSelectionProvider<Person>(gridLayer.getBodyLayer().getSelectionLayer(), bodyDataProvider, false);
        selectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                System.out.println("Selection changed:");
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                @SuppressWarnings("rawtypes") Iterator it = selection.iterator();
                while (it.hasNext()) {
                    System.out.println("  " + it.next());
                }
            }
        });
        selectionProvider.setSelection(new StructuredSelection(new Person[] { homer, smithers, nelson }));
        selectionProvider.setSelection(new StructuredSelection(new Person[] { bart, frink }));
        return natTable;
    }

    public class Person {

        private String firstName;

        private String lastName;

        private String rank;

        private long serialNumber;

        public Person(String firstName, String lastName, String rank, long serialNumber) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.rank = rank;
            this.serialNumber = serialNumber;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getRank() {
            return rank;
        }

        public long getSerialNumber() {
            return serialNumber;
        }

        @Override
        public String toString() {
            return firstName + " " + lastName + " " + rank + " " + serialNumber;
        }
    }
}
