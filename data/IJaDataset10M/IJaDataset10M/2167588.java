package com.inet.qlcbcc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.webos.core.message.ResponseMessage;
import com.inet.qlcbcc.domain.ImproveProcess;
import com.inet.qlcbcc.dto.ServantsDto;
import com.inet.qlcbcc.facade.ImproveProcessFacade;
import com.inet.qlcbcc.facade.ImproveProcessModifiableException;

/**
 * ImproveProcessController.
 *
 * @author Thoang Tran
 * @version $Id: ImproveProcessController.java Mar 14, 2012 10:48:42 PM thoangtd $
 *
 * @since 1.0
 */
@Controller
@RequestMapping(value = "/improve")
public class ImproveProcessController extends AbstractController {

    @Autowired
    @Qualifier("improveProcessFacade")
    private ImproveProcessFacade improveProcessFacade;

    @RequestMapping(value = "/save-improve.iws", method = { RequestMethod.POST })
    @ResponseBody
    ResponseMessage saveSalary(@RequestBody ServantsDto servantsDto) {
        try {
            return improveProcessFacade.save(servantsDto);
        } catch (ImproveProcessModifiableException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during saving the improve process, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during saving the improve process with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse(ex.getMessage());
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during saving the improve process, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during saving the improve process with message [{}], overcome this.", ex.getMessage());
            }
            ex.printStackTrace();
            return createFailureResponse("qlcbcc.improve_process.save");
        }
    }

    @RequestMapping(value = "/update-improve.iws", method = { RequestMethod.POST })
    @ResponseBody
    ResponseMessage saveUpdateSalary(@RequestBody ImproveProcess improveProcess) {
        try {
            return improveProcessFacade.update(improveProcess);
        } catch (ImproveProcessModifiableException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during updating the improve process, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during updating the improve process with message [{}], overcome this.", ex.getMessage());
            }
            return createFailureResponse(ex.getMessage());
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("An error occurs during updating the improve process, overcome this.", ex);
            } else if (logger.isWarnEnabled()) {
                logger.warn("An error occurs during updating the improve process with message [{}], overcome this.", ex.getMessage());
            }
            ex.printStackTrace();
            return createFailureResponse("qlcbcc.improve_process.update");
        }
    }
}
