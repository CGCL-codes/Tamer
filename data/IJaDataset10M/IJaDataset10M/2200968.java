package org.ufacekit.ui.qt.jface.examples;

import java.text.SimpleDateFormat;
import java.util.Collection;
import org.ufacekit.ui.example.model.AddressBook;
import org.ufacekit.ui.example.model.ExampleAddressBook;
import org.ufacekit.ui.example.model.Person;
import org.ufacekit.ui.qt.jface.TreeTableViewer;
import org.ufacekit.ui.qt.jface.TreeViewer;
import org.ufacekit.ui.qt.jface.TreeViewerColumn;
import org.ufacekit.ui.viewers.ISelectionChangedListener;
import org.ufacekit.ui.viewers.ITreeContentProvider;
import org.ufacekit.ui.viewers.IViewer;
import org.ufacekit.ui.viewers.LabelConverter;
import org.ufacekit.ui.viewers.SelectionChangedEvent;
import org.ufacekit.ui.viewers.StructuredSelection;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTreeWidget;

/**
 * 
 */
public class TreeTableViewerExample extends QDialog {

    private TreeTableViewer<Person, Collection<Person>> viewer;

    private AddressBook book = ExampleAddressBook.createAddressBook();

    /**
	 * 
	 */
    public TreeTableViewerExample() {
        QGridLayout layout = new QGridLayout();
        QTreeWidget widget = new QTreeWidget();
        viewer = new TreeTableViewer<Person, Collection<Person>>(widget);
        viewer.setContentProvider(new ITreeContentProvider<Person, Collection<Person>>() {

            public Collection<Person> getChildren(Person parentElement) {
                return parentElement.getFriends();
            }

            public Person getParent(Person element) {
                return null;
            }

            public boolean hasChildren(Person element) {
                return element.getFriends().size() > 0;
            }

            public void dispose() {
            }

            public Collection<Person> getElements(Collection<Person> inputElement) {
                return inputElement;
            }

            public void inputChanged(IViewer<Person> viewer, Collection<Person> oldInput, Collection<Person> newInput) {
            }
        });
        viewer.addSelectionChangedListener(new ISelectionChangedListener<Person>() {

            public void selectionChanged(SelectionChangedEvent<Person> event) {
                System.err.println(event.getSelection().getElements());
            }
        });
        TreeViewerColumn<Person> column = new TreeViewerColumn<Person>(viewer, "Name");
        column.setLabelProvider(new LabelConverter<Person>() {

            @Override
            public String getText(Person element) {
                return element.getName();
            }
        });
        column = new TreeViewerColumn<Person>(viewer, "Location");
        column.setLabelProvider(new LabelConverter<Person>() {

            @Override
            public String getText(Person element) {
                return element.getLocation();
            }
        });
        column = new TreeViewerColumn<Person>(viewer, "Birthday");
        column.setLabelProvider(new LabelConverter<Person>() {

            @Override
            public String getText(Person element) {
                return element.getBirthday() != null ? SimpleDateFormat.getDateInstance().format(element.getBirthday()) : "";
            }
        });
        QPushButton button = new QPushButton();
        button.setText("Set selection");
        button.clicked.connect(this, "setSelection()");
        viewer.setInput(book.getPeople());
        layout.addWidget(widget);
        layout.addWidget(button);
        setLayout(layout);
    }

    @SuppressWarnings("unused")
    private void setSelection() {
        viewer.setSelection(new StructuredSelection<Person>(book.getPeople().get(0)));
    }

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        QApplication.initialize(args);
        new TreeTableViewerExample().show();
        QApplication.exec();
    }
}
