package honeycrm.server.test.small;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import honeycrm.client.dto.Dto;
import honeycrm.client.mvp.presenters.CsvImportPresenter;
import honeycrm.client.mvp.presenters.CsvImportPresenter.Display;
import honeycrm.client.services.CreateServiceAsync;
import honeycrm.server.domain.Contact;
import honeycrm.server.test.small.mocks.CsvImportViewMock;
import junit.framework.TestCase;
import org.easymock.IAnswer;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CsvImportPresenterTest extends TestCase {

    private Display view;

    private CreateServiceAsync createService;

    private SimpleEventBus eventBus;

    private String module;

    private CsvImportPresenter presenter;

    @Override
    protected void setUp() throws Exception {
        this.view = new CsvImportViewMock();
        this.createService = createNiceMock(CreateServiceAsync.class);
        this.eventBus = new SimpleEventBus();
        this.module = Contact.class.getSimpleName();
        createService.create(isA(Dto.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(new IAnswer<Object>() {

            @Override
            public Object answer() throws Throwable {
                final Object[] arguments = getCurrentArguments();
                AsyncCallback<Long> callback = (AsyncCallback<Long>) arguments[arguments.length - 1];
                callback.onSuccess(1L);
                return null;
            }
        });
        replay(createService);
        presenter = new CsvImportPresenter(createService, eventBus, view, module);
    }

    public void testCreate() {
        presenter = new CsvImportPresenter(createService, eventBus, view, module);
    }

    public void testParsing() {
        assertEquals(0, presenter.getDtoArrayFromText("").length);
    }

    public void testImportOne() {
        final Dto contact = new Dto(Contact.class.getSimpleName());
        contact.set("name", "Mike");
        presenter.importDto(new Dto[] { contact }, 0);
    }

    public void testImportMore() {
        final int count = 10000;
        final Dto[] contacts = new Dto[count];
        for (int i = 0; i < count; i++) {
            contacts[i] = new Dto(Contact.class.getSimpleName());
            contacts[i].set("name", "Mike" + i);
        }
        presenter.importDto(contacts, 0);
    }
}
