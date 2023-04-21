package ingenias.editor.cell;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.jgraph.graph.*;
import ingenias.editor.entities.*;

public class GTAndDependsEdge extends NAryEdge {

    public GTAndDependsEdge(GTAndDepends userObject) {
        super(userObject);
        this.addRole("GTDependssource");
        this.setArity("GTDependssource", true, 1);
        this.setArity("GTDependssource", false, 1);
        this.addClass("GTDependssource", "ingenias.editor.entities.Goal");
        this.addRole("GTDependstarget");
        this.setArity("GTDependstarget", true, 1);
        this.setArity("GTDependstarget", false, 2147483647);
        this.addClass("GTDependstarget", "ingenias.editor.entities.Goal");
        Iterator it = this.getRoles().iterator();
        while (it.hasNext()) {
            this.add(new DefaultPort((String) it.next()));
        }
    }

    public static boolean acceptConnection(GraphModel model, GraphCell[] selected) {
        int nAryEdgesNum = 0;
        int edgesNum = 0;
        NAryEdge selectedEdge = null;
        for (int i = 0; i < selected.length; i++) if (selected[i] instanceof NAryEdge) {
            nAryEdgesNum++;
            selectedEdge = (NAryEdge) selected[i];
        } else if (selected[i] instanceof DefaultEdge) edgesNum++;
        if (nAryEdgesNum > 1 || edgesNum > 0 || !((selectedEdge == null) || (selectedEdge instanceof GTAndDependsEdge))) return false;
        GTAndDependsEdge edge = new GTAndDependsEdge(null);
        if (nAryEdgesNum == 1 && selectedEdge instanceof GTAndDependsEdge) edge = (GTAndDependsEdge) selectedEdge;
        GraphCell[] newSelected = edge.prepareSelected(selected);
        return (edge.assignRoles(newSelected, false).size() > 0);
    }

    public boolean acceptRemove(GraphCell[] selected) {
        List roles = this.getOrderedRoles();
        boolean ok = true;
        for (int i = 0; i < roles.size(); i++) {
            String roleName = (String) roles.get(i);
            Integer minAllowedTimes = this.getArity(roleName, true);
            Integer maxAllowedTimes = this.getArity(roleName, false);
            GraphCell[] roleObjects = this.getObjects(roleName);
            int currentUse = roleObjects.length;
            for (int j = 0; j < roleObjects.length; j++) for (int k = 0; k < selected.length; k++) {
                Object object = null;
                if (selected[k] instanceof DefaultEdge) {
                    DefaultPort targetPort = (DefaultPort) ((DefaultEdge) selected[k]).getTarget();
                    object = targetPort.getParent();
                } else if ((selected[k] instanceof DefaultGraphCell) && !(selected[k] instanceof DefaultPort)) {
                    object = selected[k];
                }
                if (roleObjects[j].equals(object)) currentUse--;
            }
            ok = ok && (minAllowedTimes.intValue() <= currentUse) && (currentUse <= maxAllowedTimes.intValue());
        }
        return ok;
    }

    public List assignRoles(GraphCell[] selectedNodes, boolean allSolutions) {
        Vector results = new Vector();
        Vector solution = new Vector();
        List roles = this.getOrderedRoles();
        int nodesIndex = 0;
        String nodeClass = selectedNodes[nodesIndex].getClass().getName();
        Class nodeClassc = ((DefaultGraphCell) selectedNodes[nodesIndex]).getUserObject().getClass();
        solution.add(nodesIndex, null);
        int rolesIndex = this.nextRole(roles, solution, nodesIndex).intValue();
        while ((0 <= nodesIndex) && (allSolutions || results.size() == 0)) {
            boolean roleOK = false;
            String roleName = null;
            while (rolesIndex < roles.size() && !roleOK) {
                roleName = (String) roles.get(rolesIndex);
                if (this.checkAssignation((List) solution, roleName, nodeClassc)) roleOK = true; else rolesIndex++;
            }
            if (roleOK && roleName != null) {
                solution.set(nodesIndex, roleName);
                if (checkSolution(selectedNodes, (List) solution)) {
                    Vector solutionToAdd = new Vector();
                    for (int i = 0; i < solution.size(); i++) solutionToAdd.add((String) solution.get(i));
                    results.add(solutionToAdd);
                }
                if (solution.size() < selectedNodes.length) {
                    nodesIndex++;
                    solution.add(nodesIndex, null);
                }
            } else {
                solution.remove(nodesIndex);
                nodesIndex--;
            }
            if (nodesIndex >= 0) {
                nodeClass = selectedNodes[nodesIndex].getClass().getName();
                nodeClassc = ((DefaultGraphCell) selectedNodes[nodesIndex]).getUserObject().getClass();
                rolesIndex = this.nextRole(roles, solution, nodesIndex).intValue();
            }
        }
        return results;
    }

    public DefaultEdge[] connectionsEdges(GraphCell[] selected, String[] roles) {
        Vector edges = new Vector();
        for (int i = 0; i < selected.length; i++) if (!(selected[i] instanceof DefaultEdge || selected[i] instanceof NAryEdge || selected[i] instanceof DefaultPort)) {
            if (roles[i].equalsIgnoreCase("GTDependssource")) {
                edges.add(new DefaultEdge(new GTDependssourceRole()));
            }
            if (roles[i].equalsIgnoreCase("GTDependstarget")) {
                edges.add(new DefaultEdge(new GTDependstargetRole()));
            }
        }
        DefaultEdge[] edgesSet = new DefaultEdge[edges.size()];
        for (int i = 0; i < edges.size(); i++) edgesSet[i] = (DefaultEdge) edges.get(i);
        return edgesSet;
    }

    private Integer nextRole(List relationshipRoles, List currentSolution, int currentNode) {
        int rolesIndex;
        int kk = currentSolution.size();
        if (currentSolution.get(currentNode) == null) rolesIndex = 0; else {
            rolesIndex = 0;
            String previousRole = (String) currentSolution.get(currentNode);
            for (int i = 0; i < relationshipRoles.size(); i++) if (previousRole.equals(relationshipRoles.get(i))) rolesIndex = i;
            rolesIndex++;
        }
        return (new Integer(rolesIndex));
    }
}
