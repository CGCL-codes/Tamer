package org.eclipse.core.examples.databinding.dom.snippets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.dom.DOMObservables;
import org.eclipse.core.databinding.dom.conversion.xerces.StringToXercesDocumentConverter;
import org.eclipse.core.databinding.dom.conversion.xerces.XercesDocumentToStringConverter;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Hello, databinding. Bind changes in a GUI to a Model object but don't worry
 * about propogating changes from the Model to the GUI.
 * <p>
 * Illustrates the basic Model-ViewModel-Binding-View architecture typically
 * used in data binding applications.
 */
public class Snippet000HelloWorld {

    public static void main(String[] args) throws Exception {
        Display display = new Display();
        DocumentBuilderFactory factory = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
        DocumentBuilder builder = factory.newDocumentBuilder();
        final Document viewModel = builder.parse(Snippet000HelloWorld.class.getResourceAsStream("Snippet000HelloWorld.xml"));
        Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {

            public void run() {
                final Shell shell = new View(viewModel).createShell();
                Display display = Display.getCurrent();
                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch()) {
                        display.sleep();
                    }
                }
            }
        });
        System.out.println("person.getName() = " + ((Element) viewModel.getElementsByTagName("Person").item(0)).getAttribute("name"));
    }

    static class View {

        private Document viewModel;

        private Text name;

        public View(Document viewModel) {
            this.viewModel = viewModel;
        }

        public Shell createShell() {
            Display display = Display.getDefault();
            Shell shell = new Shell(display);
            shell.setLayout(new RowLayout(SWT.VERTICAL));
            name = new Text(shell, SWT.BORDER);
            DataBindingContext bindingContext = new DataBindingContext();
            Element person = (Element) viewModel.getElementsByTagName("Person").item(0);
            bindingContext.bindValue(SWTObservables.observeText(name, SWT.Modify), DOMObservables.observeAttrValue(person, "name"), null, null);
            Text textareaWichObserveDocumentContent = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
            bindingContext.bindValue(SWTObservables.observeText(textareaWichObserveDocumentContent, SWT.Modify), DOMObservables.observeContent(viewModel), new UpdateValueStrategy().setConverter(new StringToXercesDocumentConverter()), new UpdateValueStrategy().setConverter(new XercesDocumentToStringConverter()));
            shell.pack();
            shell.open();
            return shell;
        }
    }
}
