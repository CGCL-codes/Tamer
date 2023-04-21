package eu.mpower.framework.interoperability.sipmanagement;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mpower_hibernate.SipAccount;
import mpower_hibernate.SipUser;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author fuxreiter
 */
public class SipAccountController extends HttpServlet {

    /** 
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher disp = request.getRequestDispatcher("/SipAccountDetails");
        String message = null;
        try {
            String sipUserId = request.getParameter("sipUserId");
            String sipAccountId = request.getParameter("sipAccountId");
            String accountSipId = request.getParameter("accountSipId");
            String password = request.getParameter("password");
            String serverAddress = request.getParameter("serverAddress");
            String serverPort = request.getParameter("serverPort");
            String proxyAddress = request.getParameter("proxyAddress");
            String proxyPort = request.getParameter("proxyPort");
            String preferredTransport = request.getParameter("preferredTransport");
            String phoneNumber = request.getParameter("phoneNumber");
            String submit = request.getParameter("submit");
            if ((submit == null || submit.trim().isEmpty()) && (sipUserId != null && !sipUserId.trim().isEmpty())) {
                List<SipAccount> sipAccounts = SipFacade.getSipAccountsForSipUser(Long.parseLong(sipUserId));
                request.setAttribute("sipAccounts", sipAccounts);
                request.setAttribute("sipUserId", sipUserId);
                disp = request.getRequestDispatcher("/ListSipAccounts.jsp");
            } else if ((submit == null || submit.trim().isEmpty()) && sipUserId == null) {
                List<SipAccount> sipAccounts = SipFacade.getAllSipAccounts();
                request.setAttribute("sipAccounts", sipAccounts);
                disp = request.getRequestDispatcher("/ListSipAccounts.jsp");
            } else {
                if (submit.equals("GetSipAccount") && !sipAccountId.isEmpty() && !sipAccountId.equals("0")) {
                    SipAccount sipAccount = SipFacade.getSipAccount(Long.parseLong(sipAccountId));
                    request.setAttribute("sipAccount", sipAccount);
                } else if (submit.equals("UpdateSipAccount")) {
                    if (sipUserId.isEmpty() || sipUserId.equals("0")) {
                        message = "Please enter a valid SipUserId!";
                    } else {
                        try {
                            Long newSipUserId = Long.parseLong(sipUserId);
                            if (SipFacade.sipUserExists(newSipUserId)) {
                                SipAccount sipAccount = SipFacade.getSipAccount(Long.parseLong(sipAccountId));
                                sipAccount.setPassword(password);
                                sipAccount.setPhoneNumber(phoneNumber);
                                sipAccount.setPreferredTransport(preferredTransport);
                                sipAccount.setProxyAddress(proxyAddress);
                                sipAccount.setProxyPort(proxyPort);
                                sipAccount.setServerAddress(serverAddress);
                                sipAccount.setServerPort(serverPort);
                                sipAccount.setAccountSipId(accountSipId);
                                if (sipAccount.getSipUser().getSipUserId() != newSipUserId) {
                                    SipUser oldSipUser = sipAccount.getSipUser();
                                    SipUser newSipUser = SipFacade.getSipUser(newSipUserId);
                                    oldSipUser.getSipAccount().remove(sipAccount);
                                    sipAccount.setSipUser(null);
                                    newSipUser.getSipAccount().add(sipAccount);
                                    sipAccount.setSipUser(newSipUser);
                                }
                                request.setAttribute("sipAccount", sipAccount);
                            } else {
                                message = "SipUser with id: " + newSipUserId + "doesn't exist";
                            }
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                            message = "Please enter a valid SipUserId!";
                        } catch (ConstraintViolationException ex) {
                            ex.printStackTrace();
                            message = "SipUser with id " + sipUserId + " not found!";
                        }
                    }
                } else if (submit.equals("AddSipAccount")) {
                    if (accountSipId == null || accountSipId.trim().isEmpty()) {
                        message = "Please enter a SIP address!";
                    } else if (sipUserId.isEmpty() || sipUserId.equals("0")) {
                        message = "Please enter a valid SipUserId!";
                    } else {
                        try {
                            SipUser sipUser = SipFacade.getSipUser(Long.parseLong(sipUserId));
                            SipAccount sipAccount = new SipAccount(accountSipId, password, serverAddress, serverPort, proxyAddress, proxyPort, preferredTransport, phoneNumber, null);
                            SipFacade.addSipAccount(sipUser, sipAccount);
                            request.setAttribute("sipAccount", sipAccount);
                        } catch (ConstraintViolationException ex) {
                            ex.printStackTrace();
                            message = "SipUser with id " + sipUserId + " not found!";
                        }
                    }
                    request.setAttribute("message", message);
                } else if (submit.equals("DeleteSipAccount")) {
                    SipAccount sipAccount = SipFacade.getSipAccount(Long.parseLong(sipAccountId));
                    SipFacade.deleteSipAccount(sipAccount);
                }
            }
        } finally {
        }
        disp.forward(request, response);
    }

    /** 
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
   * Handles the HTTP <code>POST</code> method.
   * @param request servlet request
   * @param response servlet response
   */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
   * Returns a short description of the servlet.
   */
    public String getServletInfo() {
        return "Short description";
    }
}
