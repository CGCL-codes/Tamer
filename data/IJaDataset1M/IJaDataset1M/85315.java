package library.interceptor;

import static library.interceptor.DataTransferInterceptor.LOGIN;
import static library.interceptor.DataTransferInterceptor.NAMESPACE_SEPARATOR;
import static library.interceptor.DataTransferInterceptor.VERIFIED;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import library.IndexAction;
import library.LibraryBaseClientAdmin;
import library.LibraryBaseClientLibrarian;
import library.LibraryBaseClientReader;
import library.LibraryBaseClientStorekeeper;
import library.LibraryBaseServer;
import library.corba.proxies.CORBALibraryClientAdmin;
import library.corba.proxies.CORBALibraryClientLibrarian;
import library.corba.proxies.CORBALibraryClientLogon;
import library.corba.proxies.CORBALibraryClientReader;
import library.corba.proxies.CORBALibraryClientStorekeeper;
import library.enums.Library;
import library.enums.Role;
import library.proxies.ActualizeBookedItem;
import library.proxies.AddBookedItems;
import library.rmi.LibraryRMIClientAdmin;
import library.rmi.LibraryRMIClientLibrarian;
import library.rmi.LibraryRMIClientLogon;
import library.rmi.LibraryRMIClientReader;
import library.rmi.LibraryRMIClientStorekeeper;
import library.socket.LibrarySocketClientAdmin;
import library.socket.LibrarySocketClientLibrarian;
import library.socket.LibrarySocketClientLogon;
import library.socket.LibrarySocketClientReader;
import library.socket.LibrarySocketClientStorekeeper;
import library.utils.BookedItemShort;
import library.utils.DocumentDescriptionShort;
import library.utils.DocumentDetails;
import library.utils.ItemInfo;
import library.utils.LogonFailedException;
import library.utils.StorekeeperInfo;
import library.utils._Properties;
import library.utils.bean.CounterBean;
import library.utils.bean.DocumentDescriptionBean;
import library.utils.bean.IdBean;
import library.utils.bean.LoginBean;
import library.utils.bean.PersonDescriptionBean;
import library.utils.bean.RoleBean;
import library.utils.bean.SearchBean;
import library.utils.bean.UserSession;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class LoginInterceptor extends AbstractInterceptor {

    protected Map<String, String> actionRole;

    protected Properties props;

    {
        props = new _Properties("/config.properties");
    }

    @SuppressWarnings("unchecked")
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        Map<String, Object> parameters = ActionContext.getContext().getParameters();
        Map<String, Object> session = invocation.getInvocationContext().getSession();
        String resultName;
        if (parameters.containsKey("credentialsSubmited")) {
            String version = props.getProperty("distributedObjects", "socket");
            final IndexAction action = (IndexAction) invocation.getAction();
            action.setActionMessages(null);
            LoginBean login = action.getLogin();
            final Map<String, Object> pullback = new HashMap<String, Object>();
            final Role _role = Role.valueOf(login.getRole());
            final String username = login.getUsername();
            final char[] password = login.getPassword().toCharArray();
            final Library library = Library.valueOf(login.getLibrary());
            final String host = login.getHost();
            try {
                if (version.equals("socket")) {
                    new LibrarySocketClientLogon() {

                        {
                            try {
                                performLogon(username, password, _role, library, host);
                            } catch (LogonFailedException e) {
                                if (action instanceof TextProvider) {
                                    String msg = ((TextProvider) action).getText("logonFailed");
                                    if (action instanceof ValidationAware) {
                                        ((ValidationAware) action).addActionMessage(msg);
                                    }
                                }
                            } catch (IOException e) {
                                if (action instanceof TextProvider) {
                                    String err = ((TextProvider) action).getText("ioError");
                                    if (action instanceof ValidationAware) {
                                        ((ValidationAware) action).addActionMessage(err);
                                    }
                                }
                            }
                            pullback.put("client", client);
                        }
                    };
                } else if (version.equals("corba")) {
                    int ORBInitialPort = Integer.parseInt(props.getProperty("ORBInitialPort"));
                    String[] args = { "-ORBInitialPort", ORBInitialPort + "" };
                    new CORBALibraryClientLogon(args) {

                        {
                            try {
                                performLogon(username, password, _role, library, host);
                            } catch (LogonFailedException e) {
                                if (action instanceof TextProvider) {
                                    String msg = ((TextProvider) action).getText("logonFailed");
                                    if (action instanceof ValidationAware) {
                                        ((ValidationAware) action).addActionMessage(msg);
                                    }
                                }
                            } catch (IOException e) {
                                if (action instanceof TextProvider) {
                                    String err = ((TextProvider) action).getText("ioError");
                                    if (action instanceof ValidationAware) {
                                        ((ValidationAware) action).addActionMessage(err);
                                    }
                                }
                            }
                            pullback.put("client", specificClient);
                        }
                    };
                } else if (version.equals("rmi")) {
                    new LibraryRMIClientLogon() {

                        {
                            try {
                                performLogon(username, password, _role, library, host);
                            } catch (LogonFailedException e) {
                                if (action instanceof TextProvider) {
                                    String msg = ((TextProvider) action).getText("logonFailed");
                                    if (action instanceof ValidationAware) {
                                        ((ValidationAware) action).addActionMessage(msg);
                                    }
                                }
                            } catch (IOException e) {
                                if (action instanceof TextProvider) {
                                    String err = ((TextProvider) action).getText("ioError");
                                    if (action instanceof ValidationAware) {
                                        ((ValidationAware) action).addActionMessage(err);
                                    }
                                }
                            }
                            pullback.put("client", client);
                        }
                    };
                }
            } catch (Exception e) {
                pullback.remove("client");
                if (action instanceof ValidationAware) {
                    String localizedMessage = e.getLocalizedMessage();
                    ((ValidationAware) action).addActionMessage(localizedMessage);
                }
            }
            Object client = pullback.get("client");
            if (client != null) {
                UserSession usession;
                final List<BookedItemShort> bookedItems = Collections.synchronizedList(new ArrayList<BookedItemShort>());
                final List<StorekeeperInfo> storekeeperBookedItems = Collections.synchronizedList(new ArrayList<StorekeeperInfo>());
                ActualizeBookedItem actualize = new ActualizeBookedItem() {

                    public boolean actualizeBookedItem(int itemID) {
                        synchronized (bookedItems) {
                            for (int i = 0; i < bookedItems.size(); i++) {
                                BookedItemShort bis = bookedItems.get(i);
                                int _itemID = bis.getItemID();
                                if (itemID == _itemID) {
                                    DocumentDescriptionShort descr = bis.getDescriptionShort();
                                    BookedItemShort _bis = new BookedItemShort(itemID, true, descr);
                                    bookedItems.remove(i);
                                    bookedItems.add(i, _bis);
                                }
                            }
                        }
                        return true;
                    }
                };
                AddBookedItems add = new AddBookedItems() {

                    public boolean addBookedItems(StorekeeperInfo[] items) {
                        storekeeperBookedItems.addAll(Arrays.asList(items));
                        return true;
                    }
                };
                if (client instanceof LibrarySocketClientLibrarian) {
                    ((LibrarySocketClientLibrarian) client).setActualizable(actualize);
                    usession = ((LibrarySocketClientLibrarian) client).readSession();
                } else if (client instanceof LibraryRMIClientLibrarian) {
                    ((LibraryRMIClientLibrarian) client).setActualizable(actualize);
                    usession = ((LibraryRMIClientLibrarian) client).readSession();
                } else if (client instanceof CORBALibraryClientLibrarian) {
                    ((CORBALibraryClientLibrarian) client).setActualizable(actualize);
                    usession = ((LibraryBaseClientLibrarian) (client = ((CORBALibraryClientLibrarian) client).getClient())).readSession();
                } else if (client instanceof LibrarySocketClientStorekeeper) {
                    ((LibrarySocketClientStorekeeper) client).setAddable(add);
                    usession = ((LibrarySocketClientStorekeeper) client).readSession();
                } else if (client instanceof LibraryRMIClientStorekeeper) {
                    ((LibraryRMIClientStorekeeper) client).setAddable(add);
                    usession = ((LibraryRMIClientStorekeeper) client).readSession();
                } else if (client instanceof CORBALibraryClientStorekeeper) {
                    ((CORBALibraryClientStorekeeper) client).setAddable(add);
                    usession = ((LibraryBaseClientStorekeeper) (client = ((CORBALibraryClientStorekeeper) client).getClient())).readSession();
                } else if (client instanceof LibrarySocketClientReader) {
                    usession = ((LibrarySocketClientReader) client).readSession();
                } else if (client instanceof LibraryRMIClientReader) {
                    usession = ((LibraryRMIClientReader) client).readSession();
                } else if (client instanceof CORBALibraryClientReader) {
                    usession = ((LibraryBaseClientReader) (client = ((CORBALibraryClientReader) client).getClient())).readSession();
                } else if (client instanceof LibrarySocketClientAdmin) {
                    usession = ((LibrarySocketClientAdmin) client).readSession();
                } else if (client instanceof LibraryRMIClientAdmin) {
                    usession = ((LibraryRMIClientAdmin) client).readSession();
                } else if (client instanceof CORBALibraryClientAdmin) {
                    usession = ((LibraryBaseClientAdmin) (client = ((CORBALibraryClientAdmin) client).getClient())).readSession();
                } else {
                    throw new RuntimeException("LibraryClient of unknown type.");
                }
                Role role = Role.valueOf(login.getRole());
                if (Role.vals().contains(role)) {
                    resultName = role.name().toLowerCase();
                } else {
                    resultName = Action.INPUT;
                }
                if (usession == null) {
                    List<String> history = new ArrayList<String>();
                    List<Boolean> checkedRegistry = new ArrayList<Boolean>();
                    CounterBean counter = new CounterBean();
                    DocumentDescriptionBean documentDescription = DocumentDescriptionBean.EMPTY;
                    int notFoundId = LibraryBaseServer.NOT_FOUND_ID;
                    DocumentDetails documentDetails = DocumentDetails.EMPTY;
                    HashMap<Integer, Map<Library, DocumentDescriptionShort[]>> findQueryResults = new HashMap<Integer, Map<Library, DocumentDescriptionShort[]>>();
                    HashMap<Integer, List<DocumentDescriptionShort>> findQueryResultsLists = new HashMap<Integer, List<DocumentDescriptionShort>>();
                    HashMap<Integer, Integer> findQuerySizes = new HashMap<Integer, Integer>();
                    IdBean id = IdBean.EMPTY;
                    ArrayList<ItemInfo> itemsInfo = new ArrayList<ItemInfo>();
                    boolean lendingMode = false;
                    List<Library> librariesRegistry = new ArrayList<Library>();
                    PersonDescriptionBean personDescription = PersonDescriptionBean.EMPTY;
                    int resultID = 0;
                    RoleBean roleEmpty = RoleBean.EMPTY;
                    ArrayList<DocumentDescriptionShort> resultList = new ArrayList<DocumentDescriptionShort>();
                    SearchBean search = SearchBean.EMPTY;
                    usession = new UserSession(history, -1, bookedItems, checkedRegistry, counter, documentDescription, documentDetails, notFoundId, findQueryResults, findQueryResultsLists, findQuerySizes, id, itemsInfo, lendingMode, librariesRegistry, personDescription, resultID, resultList, roleEmpty, search, storekeeperBookedItems);
                }
                Map<String, Boolean> verified = new HashMap<String, Boolean>();
                verified.put(role.name().toLowerCase(), true);
                action.setServer(client);
                action.setUsession(usession);
                action.setVerified(verified);
                action.setLogin(null);
                session.remove(LOGIN);
            } else {
                resultName = Action.INPUT;
            }
        } else {
            String actionName = invocation.getInvocationContext().getName();
            String namespace = invocation.getProxy().getNamespace();
            namespace = namespace.endsWith(NAMESPACE_SEPARATOR) ? namespace : namespace + NAMESPACE_SEPARATOR;
            actionName = namespace + actionName;
            String role = actionRole.get(actionName);
            Map<String, Boolean> verified = (Map<String, Boolean>) session.get(VERIFIED);
            boolean verif = verified == null ? (actionName.equals("/index") ? true : false) : (verified.get(role) == null ? false : verified.get(role));
            verif |= verified != null && actionName.equals("/index");
            if (!verif) {
                resultName = "login";
            } else {
                if (verified == null && actionName.equals("/index")) {
                    IndexAction action = (IndexAction) invocation.getAction();
                    Collection<String> actionMessages = action.getActionMessages();
                    Set<String> set = new TreeSet<String>(actionMessages);
                    action.setActionMessages(set);
                }
                resultName = invocation.invoke();
            }
        }
        return resultName;
    }

    public void setActionRoleMapping(String actionRoleMapping) {
        String[] tmp = actionRoleMapping.split(",");
        actionRole = new HashMap<String, String>();
        for (String str : tmp) {
            String[] entry = str.split("=");
            String action = entry[0];
            String role = entry[1];
            actionRole.put(action, role);
        }
    }
}
