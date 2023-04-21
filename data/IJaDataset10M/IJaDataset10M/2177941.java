package progranet.model.service;

import progranet.ganesa.metamodel.Facet;
import progranet.ganesa.metamodel.Mailer;
import progranet.ganesa.metamodel.View;
import progranet.model.exception.ModelException;
import progranet.omg.ocl.expression.OclException;
import progranet.omg.core.types.Type;
import java.text.ParseException;

public interface MailService {

    public void send(View view, Type type, Object context, Mailer mailer, Facet facet) throws ModelException, OclException, ParseException;
}
