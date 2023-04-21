package com.vmware.spring.workshop.facade.web;

import java.io.IOException;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.vmware.spring.workshop.facade.support.BankDTOImportExport;
import com.vmware.spring.workshop.services.facade.BanksFacade;

/**
 * @author lgoldstein
 */
@Controller("banksController")
@RequestMapping("/banks")
public class BanksController extends AbstractWebController {

    private final BanksFacade _banksFacade;

    private final BankDTOImportExport _importer;

    public static final String BANK_CODE_PARAM_NAME = "bankCode";

    @Inject
    public BanksController(final BanksFacade banksFacade, final BankDTOImportExport importer) {
        _banksFacade = banksFacade;
        _importer = importer;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String listBanks(Model model) {
        model.addAttribute("banksList", _banksFacade.findAll());
        return getTopLevelViewPath("list");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{" + BANK_CODE_PARAM_NAME + "}")
    public String showBankBranches(@PathVariable(BANK_CODE_PARAM_NAME) final int bankCode) {
        return "redirect:branches/" + bankCode;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/export")
    public ModelAndView exportBanks() {
        return exportDTOList(_importer, _banksFacade.findAll());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/import")
    public String importBanks(@RequestParam("file") final MultipartFile file, final Model model) throws IOException {
        importDTOList(file, _importer, _banksFacade);
        return listBanks(model);
    }
}
