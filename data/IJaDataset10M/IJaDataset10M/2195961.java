package si.unimb.isportal07.iiWiki.controller.wikiStates;

import java.util.ArrayList;
import java.util.Iterator;
import si.unimb.isportal07.iiWiki.controller.WikiController;
import si.unimb.isportal07.iiWiki.dbobj.Clanek;
import si.unimb.isportal07.iiWiki.dbobj.Kategorije;
import si.unimb.isportal07.iiIskalnik.iskanje.Vnasalec;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;

public class DeleteThreadsState extends State {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1L;

    public static final String STATE_NAME = "deleteThreadsState";

    /**
	 * Constructor
	 */
    public DeleteThreadsState() {
        super(DeleteThreadsState.STATE_NAME, DeleteThreadsState.STATE_NAME + "Description");
    }

    /**
	 * @see com.jcorporate.expresso.core.controller.State#run(com.jcorporate.expresso.core.controller.ControllerRequest, com.jcorporate.expresso.core.controller.ControllerResponse)
	 */
    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            Transition errorTrans = new Transition();
            errorTrans.setControllerObject(WikiController.class);
            errorTrans.setState(ErrorWikiState.STATE_NAME);
            errorTrans.setName("errorTrans");
            setErrorTransition(errorTrans);
            Kategorije thread = new Kategorije();
            thread.setField(Kategorije.THREAD_ID, request.getParameter("IDnit"));
            thread.retrieve();
            thread.delete();
            Clanek mainDB = new Clanek();
            mainDB.setField(Clanek.THREAD_ID, request.getParameter("IDnit"));
            ArrayList allRecords = mainDB.searchAndRetrieveList();
            Iterator i = allRecords.iterator();
            int count = 0;
            while (i.hasNext()) {
                mainDB = (Clanek) i.next();
                count++;
                mainDB.retrieve();
                mainDB.delete();
                Output Brisanje_niti = new Output();
                Brisanje_niti.setName("Brisanje_niti");
                Brisanje_niti.setLabel(getString("brisanje_niti"));
                response.add(Brisanje_niti);
                Output Obvestilo = new Output();
                Obvestilo.setName("Obvestilo");
                Obvestilo.setLabel(getString("obvestilo"));
                response.add(Obvestilo);
                Output Nit_izbrsiana = new Output();
                Nit_izbrsiana.setName("Nit_izbrsiana");
                Nit_izbrsiana.setLabel(getString("nit_izbrsiana"));
                response.add(Nit_izbrsiana);
            }
            Vnasalec.brisi("/iiForum/Forum.do?IDnitke=" + request.getParameter("IDnit") + "&state=listMainState");
            Transition listThreadsTransition = new Transition();
            listThreadsTransition.setControllerObject(WikiController.class);
            listThreadsTransition.setState(ListThreadsState.STATE_NAME);
            listThreadsTransition.setName("listThreadsTransition");
            listThreadsTransition.setLabel(getString("nazaj_na_seznam_niti"));
            response.add(listThreadsTransition);
        } catch (Exception e) {
        } finally {
        }
    }
}
