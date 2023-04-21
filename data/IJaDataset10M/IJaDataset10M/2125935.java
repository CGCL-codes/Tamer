package si.unimb.isportal07.iiForum.controller.forumStates;

import si.unimb.isportal07.iiForum.controller.ForumController;
import si.unimb.isportal07.iiForum.dbobj.Replys;
import si.unimb.isportal07.iiForum.dbobj.Threads;
import si.unimb.isportal07.iiIskalnik.iskanje.Vnasalec;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.ControllerResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.State;
import com.jcorporate.expresso.core.controller.Transition;

public class AddThreadState extends State {

    private static final int VRSTICA = 100;

    public static final String STATE_NAME = "addThreadState";

    public AddThreadState() {
        super(AddThreadState.STATE_NAME, AddThreadState.STATE_NAME + "Description");
    }

    public void run(ControllerRequest request, ControllerResponse response) throws ControllerException, NonHandleableException {
        super.run(request, response);
        try {
            Output messageOutput = new Output();
            messageOutput.setName("messageOutput");
            messageOutput.setLabel("Sporocilo");
            if ((request.getParameter("threadInput").equals("")) || (request.getParameter("postInput").equals(""))) {
                Transition errorTrans = new Transition();
                errorTrans.setControllerObject(ForumController.class);
                errorTrans.setState(ErrorForumState.STATE_NAME);
                errorTrans.setName("errorTrans");
                setErrorTransition(errorTrans);
                messageOutput.setContent("Obrazec ni izpolnjen pravilno.");
                response.add(messageOutput);
                Transition listThreadsTransition = new Transition();
                listThreadsTransition.setControllerObject(ForumController.class);
                listThreadsTransition.setState(ListThreadsState.STATE_NAME);
                listThreadsTransition.setName("listThreadsTransition");
                listThreadsTransition.setLabel("Seznam niti");
                response.add(listThreadsTransition);
            } else {
                messageOutput.setContent("Nova nit je bila uspesno vnesena.");
                response.add(messageOutput);
                Transition errorTrans = new Transition();
                errorTrans.setControllerObject(ForumController.class);
                errorTrans.setState(ErrorForumState.STATE_NAME);
                errorTrans.setName("errorTrans");
                setErrorTransition(errorTrans);
                Threads threadsDB = new Threads();
                threadsDB.setField(Threads.NAME, request.getParameter("threadInput"));
                threadsDB.setField(Threads.CATEGORY_ID, request.getParameter("categoryInput"));
                threadsDB.add();
                String dolgString = request.getParameter("postInput");
                String temp = dolgString;
                String output = "";
                int st = temp.length() / VRSTICA;
                for (int j = 0; j < st; j++) output += temp.substring(j * VRSTICA, j * VRSTICA + VRSTICA) + "<br />";
                output += temp.substring(st * VRSTICA, temp.length());
                Replys replysDB = new Replys();
                replysDB.setField(Replys.DESCRIPTION, output);
                replysDB.setField(Replys.THREAD_ID, threadsDB.getField(Threads.THREAD_ID));
                replysDB.add();
                Output threadNameOutput = new Output();
                threadNameOutput.setName("threadOutput");
                threadNameOutput.setContent(threadsDB.getField(Threads.NAME));
                threadNameOutput.setLabel("Naslov niti");
                response.add(threadNameOutput);
                Vnasalec.indeksiraj("/iiForum/Forum.do?IDnitke=" + threadsDB.getField(Threads.THREAD_ID) + "&state=listReplysState", "Thread " + threadsDB.getField(Threads.NAME), "Nit z imenom " + threadsDB.getField(Threads.NAME) + ", ki spada v kategorijo " + threadsDB.getValidValueDescrip(Threads.CATEGORY_ID), "forum," + request.getParameter("postInput") + ", spletni forum, post, thread, nit");
                Output replyOutput = new Output();
                replyOutput.setName("replyOutput");
                replyOutput.setContent(replysDB.getField(Replys.DESCRIPTION));
                replyOutput.setLabel("Sporocilo");
                response.add(replyOutput);
                Output idOutput = new Output();
                idOutput.setName("idOutput");
                idOutput.setContent(threadsDB.getField(Threads.THREAD_ID));
                idOutput.setLabel("ID Niti");
                response.add(idOutput);
                Transition listThreadsTransition = new Transition();
                listThreadsTransition.setControllerObject(ForumController.class);
                listThreadsTransition.setState(ListThreadsState.STATE_NAME);
                listThreadsTransition.setName("listThreadsTransition");
                listThreadsTransition.setLabel("Seznam niti");
                response.add(listThreadsTransition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
