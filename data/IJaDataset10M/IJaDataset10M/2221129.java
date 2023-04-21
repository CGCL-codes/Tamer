package library.wizards;

import java.util.Date;
import library.Book;
import library.BookState;
import library.Library;
import library.RentRecord;
import library.Renter;
import library.util.LibraryUtility;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class CreateRentRecordWizard extends Wizard implements IWizard {

    private Library root;

    private RentRecord rentRecord;

    private RentableBookSearchWizardPage page1;

    private RentableRenterSearchWizardPage page2;

    public CreateRentRecordWizard(AddCommand addCommand) {
        EObject child = LibraryUtility.child(addCommand);
        if (child instanceof RentRecord) {
            rentRecord = (RentRecord) child;
            root = (Library) EcoreUtil.getRootContainer(addCommand.getOwner());
        }
        setWindowTitle("���������û");
    }

    public void addPages() {
        page1 = new RentableBookSearchWizardPage("page1", root);
        page2 = new RentableRenterSearchWizardPage("page1", root);
        addPage(page1);
        page1.setDescription("������ ������ �����մϴ�.");
        page1.setTitle("��������");
        addPage(page2);
        page2.setDescription("�����ڸ� �����մϴ�.");
        page2.setTitle("������ ����");
    }

    public boolean performFinish() {
        int day = 10;
        Renter renter = page2.getSelectedRenter();
        Book book = page1.getSelectedBook();
        rentRecord.setBook(book);
        rentRecord.setRenter(renter);
        Date now = new Date(), ret = new Date(now.getTime() + 24L * 60L * 60L * 1000L * day);
        rentRecord.setRentDate(now);
        rentRecord.setExpectedReturnDate(ret);
        book.setBookState(BookState.ON_RENT);
        renter.setQuota(renter.getQuota() - 1);
        return true;
    }
}

class RentableBookSearchWizardPage extends BookSearchWizardPage {

    RentableBookSearchWizardPage(String pageName, Library root) {
        super(pageName, root);
    }

    public void createControl(Composite arg0) {
        super.createControl(arg0);
        setPageComplete(false);
        tableViewer.addFilter(new ViewerFilter() {

            public boolean select(Viewer arg0, Object arg1, Object arg2) {
                if (arg2 instanceof Book) {
                    Book book = (Book) arg2;
                    return book.getBookState().equals(BookState.RENTABLE);
                }
                return true;
            }
        });
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent arg0) {
                setPageComplete(getSelectedBook() != null);
            }
        });
    }
}

class RentableRenterSearchWizardPage extends RenterSearchWizardPage {

    protected RentableRenterSearchWizardPage(String pageName, Library root) {
        super(pageName, root);
    }

    @Override
    public void createControl(Composite arg0) {
        super.createControl(arg0);
        setPageComplete(false);
        tableViewer.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer arg0, Object arg1, Object arg2) {
                if (arg2 instanceof Renter) {
                    Renter renter = (Renter) arg2;
                    return renter.getQuota() > 0;
                }
                return false;
            }
        });
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent arg0) {
                setPageComplete(getSelectedRenter() != null);
            }
        });
    }
}

class RentOverviewWizardPage extends WizardPage implements IWizardPage {

    private Composite container;

    protected RentOverviewWizardPage(String pageName) {
        super(pageName);
    }

    public void createControl(Composite arg0) {
        container = new Composite(arg0, SWT.NONE);
        setControl(container);
    }

    public CreateRentRecordWizard getCastedWizard() {
        return (CreateRentRecordWizard) getWizard();
    }
}
