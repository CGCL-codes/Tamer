package net.sf.smartcrib.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import javax.script.ScriptException;
import java.util.logging.Logger;
import java.util.logging.Level;
import net.sf.smartcrib.SmartCribManager;
import net.sf.smartcrib.SmartCrib;
import net.sf.smartcrib.Macro;

/**
 */
@Controller
public class MacroEditorSpringController {

    private static transient Logger logger = Logger.getLogger("smartcrib.history");

    /** The persister used to retrieve/store from the data layer. */
    @Autowired
    private SmartCribManager smartCribManager;

    @RequestMapping(value = "/macros.do", method = RequestMethod.GET)
    public ModelAndView macrosView() {
        ModelAndView mv = new ModelAndView("macros");
        mv.addObject("macros", smartCribManager.getSmartCribInstance().getMacros());
        return mv;
    }

    @RequestMapping(value = "/macros.do", method = RequestMethod.POST)
    public ModelAndView macrosDelete(@RequestParam(value = "sel", required = true) String[] delete) {
        StringBuilder list = new StringBuilder();
        SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
        for (String name : delete) {
            list.append(name).append(", ");
            smartCrib.getMacros().remove(smartCrib.findMacroByName(name));
        }
        smartCribManager.saveSmartCrib(smartCrib);
        logger.info("The following macros have been deleted: " + list);
        return macrosView();
    }

    @RequestMapping(value = "/macro.do", method = RequestMethod.GET)
    public ModelAndView macroView(@RequestParam(value = "name", required = false) String name) {
        ModelAndView mv = new ModelAndView("macro");
        net.sf.smartcrib.Macro macro = null;
        if (name != null) {
            macro = smartCribManager.getSmartCribInstance().findMacroByName(name);
        }
        if (macro == null) {
            macro = (name == null ? new net.sf.smartcrib.Macro() : new Macro(name));
        }
        mv.addObject("macro", macro);
        return mv;
    }

    @RequestMapping(value = "/macro.do", method = RequestMethod.POST)
    public ModelAndView macroEdit(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "newName", required = false) String newName, @RequestParam(value = "code", required = true) String code, HttpServletRequest request) {
        if (request.getParameter("ok") != null) {
            SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
            net.sf.smartcrib.Macro macro = null;
            if (name != null && name.length() > 0) macro = smartCrib.findMacroByName(name);
            if (macro == null) {
                logger.info("Create macro " + name + ". Code: " + code);
                macro = new net.sf.smartcrib.Macro(newName);
                smartCrib.getMacros().add(macro);
            } else {
                logger.info("Edit macro " + name + (newName == null ? "" : " renamed to " + newName) + ". Code: " + code);
                if (newName != null) {
                    macro.setName(newName);
                }
            }
            macro.setCode(code);
            smartCribManager.saveSmartCrib(smartCrib);
        }
        return macrosView();
    }

    @RequestMapping(value = "/macroExecute.do", method = RequestMethod.POST)
    public ModelAndView macroExecute(@RequestParam(value = "name", required = true) String name) {
        SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
        try {
            smartCrib.executeMacro(smartCrib.findMacroByName(name));
        } catch (ScriptException e) {
            logger.log(Level.WARNING, "Error executing macro " + name, e);
        } catch (NullPointerException e) {
            logger.log(Level.WARNING, "Macro " + name + " not found");
        }
        return macroView(name);
    }
}
