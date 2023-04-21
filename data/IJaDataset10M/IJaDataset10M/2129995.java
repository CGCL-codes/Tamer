package org.formaria.metrobank.mvc;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.AbstractCommandController;
import org.formaria.metrobank.domain.Account;
import org.formaria.metrobank.service.TransactionService;

public class TransactionsForAccountController extends AbstractCommandController {

    private static final String BASE_VIEW_NAME = "vehicleTransactions";

    public TransactionsForAccountController() {
        setCommandClass(Account.class);
        setCommandName("vehicle");
    }

    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        Account vehicle = (Account) command;
        Map model = errors.getModel();
        model.put("transactions", transactionService.getTransactionsForAccount(vehicle));
        model.put("vehicle", vehicle);
        return new ModelAndView(getViewName(request), model);
    }

    private String getViewName(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String extension = "." + requestUri.substring(requestUri.length() - 3);
        if (".htm".equals(extension)) {
            extension = "";
        }
        return BASE_VIEW_NAME + extension;
    }

    private TransactionService transactionService;

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
}
