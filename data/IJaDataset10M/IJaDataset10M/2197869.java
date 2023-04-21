package uk.ac.ebi.mg.xchg;

import com.pri.session.ClientSession;
import uk.ac.ebi.mg.xchg.objects.AssayBean;
import uk.ac.ebi.mg.xchg.objects.Study;
import uk.ac.ebi.mg.xchg.objects.StudyId;

public interface ExchangeServer {

    StudyId insertStudy(Study object, ClientSession cliSess) throws ExchangeException;

    StudyId insertAssay(AssayBean object, ClientSession cliSess) throws ExchangeException;
}
