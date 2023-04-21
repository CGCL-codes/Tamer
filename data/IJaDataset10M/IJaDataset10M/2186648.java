package org.ochnygosch.jIMAP.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.ochnygosch.jIMAP.IIMAPExtension;
import org.ochnygosch.jIMAP.IIMAPProtocol;
import org.ochnygosch.jIMAP.IIMAPResponse;
import org.ochnygosch.jIMAP.IMAPProtocolException;
import org.ochnygosch.jIMAP.base.command.AppendCommand;
import org.ochnygosch.jIMAP.base.command.CapabilityCommand;
import org.ochnygosch.jIMAP.base.command.CheckCommand;
import org.ochnygosch.jIMAP.base.command.CloseCommand;
import org.ochnygosch.jIMAP.base.command.CopyCommand;
import org.ochnygosch.jIMAP.base.command.CreateCommand;
import org.ochnygosch.jIMAP.base.command.DeleteCommand;
import org.ochnygosch.jIMAP.base.command.ExamineCommand;
import org.ochnygosch.jIMAP.base.command.ExpungeCommand;
import org.ochnygosch.jIMAP.base.command.FetchCommand;
import org.ochnygosch.jIMAP.base.command.ListCommand;
import org.ochnygosch.jIMAP.base.command.LoginCommand;
import org.ochnygosch.jIMAP.base.command.LogoutCommand;
import org.ochnygosch.jIMAP.base.command.LsubCommand;
import org.ochnygosch.jIMAP.base.command.NoopCommand;
import org.ochnygosch.jIMAP.base.command.RenameCommand;
import org.ochnygosch.jIMAP.base.command.SearchCommand;
import org.ochnygosch.jIMAP.base.command.SelectCommand;
import org.ochnygosch.jIMAP.base.command.StatusCommand;
import org.ochnygosch.jIMAP.base.command.StoreCommand;
import org.ochnygosch.jIMAP.base.command.SubscribeCommand;
import org.ochnygosch.jIMAP.base.command.UidCommand;
import org.ochnygosch.jIMAP.base.response.CapabilityResponse;
import org.ochnygosch.jIMAP.base.response.ExistsResponse;
import org.ochnygosch.jIMAP.base.response.ExpungeResponse;
import org.ochnygosch.jIMAP.base.response.FetchResponse;
import org.ochnygosch.jIMAP.base.response.FlagsResponse;
import org.ochnygosch.jIMAP.base.response.ListResponse;
import org.ochnygosch.jIMAP.base.response.LogoutResponse;
import org.ochnygosch.jIMAP.base.response.LsubResponse;
import org.ochnygosch.jIMAP.base.response.PermanentflagsResponse;
import org.ochnygosch.jIMAP.base.response.RecentResponse;
import org.ochnygosch.jIMAP.base.response.SearchResponse;
import org.ochnygosch.jIMAP.base.response.StatusResponse;
import org.ochnygosch.jIMAP.base.response.UidnextResponse;
import org.ochnygosch.jIMAP.base.response.UidvalidityResponse;
import org.ochnygosch.jIMAP.base.response.UnseenResponse;
import org.ochnygosch.jIMAP.internal.IMAPResponse;

public class IMAPBaseProtocol implements IIMAPExtension {

    private IIMAPProtocol prot;

    private Map<Class, Method> canHandleMethods;

    public IMAPBaseProtocol() {
        this.canHandleMethods = new HashMap<Class, Method>();
        this.loadResponseObjects();
    }

    public void setIIMAPProtocol(IIMAPProtocol prot) {
        this.prot = prot;
    }

    public boolean canHandleResponse(IMAPResponse re) {
        Iterator<Entry<Class, Method>> it = this.canHandleMethods.entrySet().iterator();
        re.reset();
        while (it.hasNext()) {
            Entry<Class, Method> currEntry = it.next();
            Class currClass = currEntry.getKey();
            Method currMethod = currEntry.getValue();
            try {
                Boolean canHandle = (Boolean) currMethod.invoke(null, new Object[] { re });
                re.reset();
                if (canHandle.booleanValue() == true) {
                    return true;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            re.reset();
        }
        return false;
    }

    public IIMAPResponse handleResponse(IMAPResponse re) throws IMAPProtocolException {
        Iterator<Entry<Class, Method>> it = this.canHandleMethods.entrySet().iterator();
        re.reset();
        while (it.hasNext()) {
            Entry<Class, Method> currEntry = it.next();
            Class currClass = currEntry.getKey();
            Method currMethod = currEntry.getValue();
            try {
                Boolean canHandle = (Boolean) currMethod.invoke(null, new Object[] { re });
                if (canHandle.booleanValue()) {
                    re.reset();
                    try {
                        IIMAPBaseProtocolResponse resp = (IIMAPBaseProtocolResponse) currClass.newInstance();
                        resp.handleResponse(re);
                        return resp;
                    } catch (InstantiationException e) {
                        throw new IMAPProtocolException();
                    }
                } else {
                    re.reset();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        re.reset();
        return null;
    }

    public CapabilityCommand createCapabilityCommand() {
        return new CapabilityCommand(this.prot);
    }

    public LogoutCommand createLogoutCommand() {
        return new LogoutCommand(this.prot);
    }

    public LoginCommand createLoginCommand() {
        return new LoginCommand(this.prot);
    }

    public SelectCommand createSelectCommand() {
        return new SelectCommand(this.prot);
    }

    public ExamineCommand createExamineCommand() {
        return new ExamineCommand(this.prot);
    }

    public CreateCommand createCreateCommand() {
        return new CreateCommand(this.prot);
    }

    public DeleteCommand createDeleteCommand() {
        return new DeleteCommand(this.prot);
    }

    public RenameCommand createRenameCommand() {
        return new RenameCommand(this.prot);
    }

    public SubscribeCommand createSubscribeCommand() {
        return new SubscribeCommand(this.prot);
    }

    public NoopCommand createNoopCommand() {
        return new NoopCommand(this.prot);
    }

    public ListCommand createListCommand() {
        return new ListCommand(this.prot);
    }

    public LsubCommand createLsubCommand() {
        return new LsubCommand(this.prot);
    }

    public StatusCommand createStatusCommand() {
        return new StatusCommand(this.prot);
    }

    public SearchCommand createSearchCommand() {
        return new SearchCommand(this.prot);
    }

    public CheckCommand createCheckCommand() {
        return new CheckCommand(this.prot);
    }

    public CloseCommand createCloseCommand() {
        return new CloseCommand(this.prot);
    }

    public FetchCommand createFetchCommand() {
        return new FetchCommand(this.prot);
    }

    public CopyCommand createCopyCommand() {
        return new CopyCommand(this.prot);
    }

    public ExpungeCommand createExpungeCommand() {
        return new ExpungeCommand(this.prot);
    }

    public AppendCommand createAppendCommand() {
        return new AppendCommand(this.prot);
    }

    public StoreCommand createStoreCommand() {
        return new StoreCommand(this.prot);
    }

    public UidCommand createUidCommand() {
        return new UidCommand(this.prot);
    }

    private void loadResponseObjects() {
        try {
            Class<CapabilityResponse> cCap = CapabilityResponse.class;
            Method m = cCap.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cCap, m);
            Class<LogoutResponse> cLog = LogoutResponse.class;
            m = cLog.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cLog, m);
            Class<ExistsResponse> cExi = ExistsResponse.class;
            m = cExi.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cExi, m);
            Class<FlagsResponse> cFlags = FlagsResponse.class;
            m = cFlags.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cFlags, m);
            Class<PermanentflagsResponse> cPermFlags = PermanentflagsResponse.class;
            m = cPermFlags.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cPermFlags, m);
            Class<RecentResponse> cRecent = RecentResponse.class;
            m = cRecent.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cRecent, m);
            Class<UidnextResponse> cUidn = UidnextResponse.class;
            m = cUidn.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cUidn, m);
            Class<UidvalidityResponse> cUidv = UidvalidityResponse.class;
            m = cUidv.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cUidv, m);
            Class<UnseenResponse> cUnseen = UnseenResponse.class;
            m = cUnseen.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cUnseen, m);
            Class<ListResponse> cList = ListResponse.class;
            m = cList.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cList, m);
            Class<LsubResponse> cLsub = LsubResponse.class;
            m = cLsub.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cLsub, m);
            Class<StatusResponse> cStat = StatusResponse.class;
            m = cStat.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cStat, m);
            Class<ExpungeResponse> cExp = ExpungeResponse.class;
            m = cExp.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cExp, m);
            Class<SearchResponse> cSearch = SearchResponse.class;
            m = cSearch.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cSearch, m);
            Class<FetchResponse> cFetch = FetchResponse.class;
            m = cFetch.getMethod("canHandle", new Class[] { IMAPResponse.class });
            this.canHandleMethods.put(cFetch, m);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
