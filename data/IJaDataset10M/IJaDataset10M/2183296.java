package server;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPFaultException;
import data.WsBacklog;
import data.WsIndexCard;
import data.WsIteration;
import data.WsProject;
import data.WsStoryCard;
import persister.Backlog;
import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.ForbiddenOperationException;
import persister.IndexCard;
import persister.IndexCardNotFoundException;
import persister.Iteration;
import persister.StoryCard;
import persister.SynchronousPersister;
import persister.local.PersisterToXML;

/**
 * This is the actual web service class. Most of the methods from the persister.SynchronousPersister
 * interface should be in here. If you think that something important is missing just add it.
 * 
 * @author webers
 *
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class APWebservice {

    /**
	 * any port number > 1024 
	 */
    private static final String PORT = "8080";

    /**
	 * the complete name of the host e.g. "zeus.cpsc.ucalgary.ca" 
	 */
    private static final String HOST = "localhost";

    /**
	 * will be added to host name and port 
	 */
    private static final String DIRECTORY = "services";

    private SynchronousPersister persister;

    public APWebservice() {
        try {
            this.persister = new PersisterToXML("Projects", "DefaultProject");
        } catch (ConnectionFailedException e) {
            e.printStackTrace();
        } catch (CouldNotLoadProjectException e) {
            e.printStackTrace();
        }
    }

    /**
	 * @WebMethod and @WebResult declare the method as available via the web service.
	 * Every parameter needs to be annotated with @WebParam, the name does not have
	 * to be specified but it helps as the client side sees the actual name instead
	 * of args0, args1 etc.
	 * 
	 * @param projectName
	 * @return
	 */
    @WebMethod
    @WebResult
    public WsProject load(@WebParam(name = "projectName") String projectName) {
        try {
            return new WsProject(persister.load(projectName));
        } catch (CouldNotLoadProjectException e) {
            e.printStackTrace();
            return null;
        }
    }

    @WebMethod
    @WebResult
    public WsProject getProject() {
        return new WsProject(persister.getProject());
    }

    @WebMethod
    @WebResult
    public String[] getProjectNames() {
        List<String> names = persister.getProjectNames();
        String[] n = new String[names.size()];
        return names.toArray(n);
    }

    @WebMethod
    @WebResult
    public String[][] getIterationNames(@WebParam(name = "projectName") String projectName) {
        return persister.getIterationNames(projectName);
    }

    ;

    /****************************************************************************
 * 								CREATE										*
 ****************************************************************************/
    @WebMethod
    @WebResult
    public WsProject createProject(@WebParam(name = "name") String name) {
        return new WsProject(persister.createProject(name));
    }

    @WebMethod
    @WebResult
    public WsBacklog createBacklog(@WebParam(name = "width") int width, @WebParam(name = "height") int height, @WebParam(name = "locationX") int locationX, @WebParam(name = "locationY") int locationY) {
        try {
            return new WsBacklog(persister.createBacklog(width, height, locationX, locationY));
        } catch (ForbiddenOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    @WebMethod
    @WebResult
    public WsIteration createIteration(@WebParam(name = "name") String name, @WebParam(name = "description") String description, @WebParam(name = "width") int width, @WebParam(name = "height") int height, @WebParam(name = "locationX") int locationX, @WebParam(name = "locationY") int locationY, @WebParam(name = "availableEffort") float availableEffort, @WebParam(name = "startDate") Date startDate, @WebParam(name = "endDate") Date endDate) {
        return new WsIteration(persister.createIteration(name, description, width, height, locationX, locationY, availableEffort, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())));
    }

    @WebMethod
    @WebResult
    public WsStoryCard createStoryCard(@WebParam(name = "name") String name, @WebParam(name = "description") String description, @WebParam(name = "width") int width, @WebParam(name = "height") int height, @WebParam(name = "locationX") int locationX, @WebParam(name = "locationY") int locationY, @WebParam(name = "parentid") long parentid, @WebParam(name = "bestCaseEstimate") float bestCaseEstimate, @WebParam(name = "mostlikelyEstimate") float mostlikelyEstimate, @WebParam(name = "worstCaseEstimate") float worstCaseEstimate, @WebParam(name = "actualEffort") float actualEffort, @WebParam(name = "status") String status, @WebParam(name = "color") String color) {
        try {
            return new WsStoryCard(persister.createStoryCard(name, description, width, height, locationX, locationY, parentid, bestCaseEstimate, mostlikelyEstimate, worstCaseEstimate, actualEffort, status, color));
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /****************************************************************************
 * 						DELETE & UNDELETE 									*
 ****************************************************************************/
    @WebMethod
    @WebResult
    public WsIndexCard deleteCard(@WebParam(name = "id") long id) {
        IndexCard card = null;
        try {
            card = persister.deleteCard(id);
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
        } catch (ForbiddenOperationException e) {
            e.printStackTrace();
        }
        if (card instanceof StoryCard) return new WsStoryCard((StoryCard) card); else if (card instanceof Iteration) return new WsIteration((Iteration) card); else if (card instanceof Backlog) return new WsBacklog((Backlog) card); else {
            return null;
        }
    }

    @WebMethod
    @WebResult
    public WsIndexCard undeleteCard(@WebParam(name = "indexCard") WsIndexCard indexCard) {
        IndexCard card = null;
        try {
            card = persister.undeleteCard(indexCard.toIndexCard());
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (ForbiddenOperationException e) {
            e.printStackTrace();
            return null;
        }
        if (card instanceof StoryCard) {
            return new WsStoryCard((StoryCard) card);
        } else if (indexCard instanceof Iteration) {
            return new WsIteration((Iteration) card);
        } else if (indexCard instanceof Backlog) {
            return new WsBacklog((Backlog) card);
        } else {
            return null;
        }
    }

    /****************************************************************************
 * 								UPDATE 										*
 ****************************************************************************/
    @WebMethod
    @WebResult
    public WsIndexCard updateCard(@WebParam(name = "indexCard") WsIndexCard indexCard) {
        IndexCard card = null;
        try {
            card = persister.updateCard(indexCard.toIndexCard());
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (card instanceof StoryCard) {
            return new WsStoryCard((StoryCard) card);
        } else if (indexCard instanceof Iteration) {
            return new WsIteration((Iteration) card);
        } else if (indexCard instanceof Backlog) {
            return new WsBacklog((Backlog) card);
        } else {
            return null;
        }
    }

    /****************************************************************************
 * 					MOVE STORYCARD BETWEEN PARENTS 							*
 ****************************************************************************/
    @WebMethod
    @WebResult
    public WsStoryCard moveStoryCardToNewParent(@WebParam(name = "id") long id, @WebParam(name = "oldparentid") long oldparentid, @WebParam(name = "newparentid") long newparentid, @WebParam(name = "locationX") int locationX, @WebParam(name = "locationY") int locationY) {
        try {
            return new WsStoryCard(persister.moveStoryCardToNewParent(id, oldparentid, newparentid, locationX, locationY));
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /****************************************************************************
 * 						FIND OBJECTS BY ID									*
 ****************************************************************************/
    @WebMethod
    @WebResult
    public WsIndexCard findCard(@WebParam(name = "id") long id) {
        IndexCard card = null;
        try {
            card = persister.findCard(id);
        } catch (IndexCardNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (card instanceof StoryCard) return new WsStoryCard((StoryCard) card); else if (card instanceof Iteration) return new WsIteration((Iteration) card); else if (card instanceof Backlog) return new WsBacklog((Backlog) card); else {
            return null;
        }
    }

    @WebMethod
    @WebResult
    public WsProject arrangeProject(@WebParam(name = "project") WsProject project) {
        return new WsProject(persister.arrangeProject(project.toProject()));
    }

    /**
	 * Publishes the web service
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        System.out.println("Service starting...");
        Endpoint endpoint = Endpoint.publish("http://" + HOST + ":" + PORT + "/" + DIRECTORY, new APWebservice());
        System.out.println("Service running! On " + HOST + ":" + PORT + "/" + DIRECTORY);
    }
}
