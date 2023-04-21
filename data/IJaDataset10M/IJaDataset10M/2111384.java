package org.ufacekit.ui.qt.jface.examples;

import java.util.ArrayList;
import java.util.Collection;
import org.ufacekit.ui.example.model.Person;
import org.ufacekit.ui.qt.jface.ComboViewer;
import org.ufacekit.ui.viewers.CollectionContentProvider;
import org.ufacekit.ui.viewers.ISelectionChangedListener;
import org.ufacekit.ui.viewers.LabelConverter;
import org.ufacekit.ui.viewers.SelectionChangedEvent;
import org.ufacekit.ui.viewers.StructuredSelection;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QPushButton;

/**
 * 
 */
public class ComboViewerExample extends QDialog {

    private ComboViewer<Person, Collection<Person>> viewer;

    private ArrayList<Person> ps;

    /**
	 * 
	 */
    public ComboViewerExample() {
        QGridLayout layout = new QGridLayout();
        QComboBox widget = new QComboBox();
        viewer = new ComboViewer<Person, Collection<Person>>(widget);
        viewer.setContentProvider(new CollectionContentProvider<Person>());
        viewer.setLabelProvider(new LabelConverter<Person>() {

            @Override
            public String getText(Person element) {
                return element.getName();
            }
        });
        viewer.addSelectionChangedListener(new ISelectionChangedListener<Person>() {

            public void selectionChanged(SelectionChangedEvent<Person> event) {
                System.err.println(event.getSelection().getElements());
            }
        });
        QPushButton button = new QPushButton();
        button.setText("Set selection");
        button.clicked.connect(this, "setSelection()");
        ps = new ArrayList<Person>();
        Person p = new Person();
        p.setName("Tom Schindl");
        ps.add(p);
        p = new Person();
        p.setName("Boris Bokowski");
        ps.add(p);
        p = new Person();
        p.setName("To Creasy");
        ps.add(p);
        viewer.setInput(ps);
        layout.addWidget(widget);
        layout.addWidget(button);
        setLayout(layout);
    }

    @SuppressWarnings("unused")
    private void setSelection() {
        viewer.setSelection(new StructuredSelection<Person>(ps.get(0)));
    }

    /**
	 * @param args
	 */
    public static void main(final String[] args) {
        QApplication.initialize(args);
        new ComboViewerExample().show();
        QApplication.exec();
    }
}
