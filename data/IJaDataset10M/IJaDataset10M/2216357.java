package progranet.model.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import progranet.ganesa.metamodel.Rule;
import progranet.ganesa.metamodel.Report;
import progranet.ganesa.metamodel.View;
import progranet.ganesa.metamodel.ReportImpl;
import progranet.model.exception.ModelException;
import progranet.model.service.MailService;
import progranet.model.service.ReportService;
import progranet.omg.core.types.Type;
import progranet.omg.ocl.expression.OclException;
import progranet.utils.email.*;
import javax.mail.MessagingException;
import java.text.ParseException;
import java.io.UnsupportedEncodingException;

public class MailServiceMockImpl extends MailServiceImpl implements MailService {

    public void send(View view, Type type, Object context, Rule mailer) throws ModelException, OclException, ParseException {
        log.debug("mail sent - start");
        log.debug("View: " + view);
        log.debug("Type: " + type);
        log.debug("Context: " + context);
        log.debug("Mailer: " + mailer);
        log.debug("MailService: " + getMailService());
        log.debug("ReportService" + getReportService());
        log.debug("mail sent - stop");
    }
}
