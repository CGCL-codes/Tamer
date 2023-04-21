package edu.cebanc.spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import edu.cebanc.spring.bean.Account;

@Controller
@RequestMapping(value = "/account")
public class HelloSpringController {

    private Map<String, Account> accounts = new HashMap<String, Account>();

    @RequestMapping(method = RequestMethod.GET)
    public String getMainView(Model model) {
        List<Account> aux = new ArrayList<Account>();
        Set<String> keys = accounts.keySet();
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            Account a = accounts.get(it.next());
            aux.add(a);
        }
        model.addAttribute("accounts", aux);
        return "accountMain";
    }

    @RequestMapping(value = "createForm", method = RequestMethod.GET)
    public String getCreateForm(Model model) {
        model.addAttribute(new Account());
        return "accountForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createAccount(@Valid Account account, BindingResult result) {
        if (result.hasErrors()) {
            return "accountForm";
        }
        this.accounts.put(account.getName(), account);
        return "redirect:/account/" + account.getName();
    }

    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    public String getView(@PathVariable String name, Model model) {
        Account acc = this.accounts.get(name);
        if (acc == null) {
        }
        model.addAttribute(acc);
        return "accountView";
    }
}
